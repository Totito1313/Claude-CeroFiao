package com.schwarckdev.cerofiao.feature.billsplitter

import com.schwarckdev.cerofiao.core.domain.usecase.ResolveExchangeRateUseCase
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.model.UserPreferences
import com.schwarckdev.cerofiao.core.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

// Simplified fake that just echoes back the USD equivalent logic for testing without actual DB lookup
class FakeResolveExchangeRateUseCaseForVM : ResolveExchangeRateUseCase(
    // We don't actually need the repo if we override the methods
    com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository::class.java.let { java.lang.reflect.Proxy.newProxyInstance(it.classLoader, arrayOf(it)) { _, _, _ -> null } as com.schwarckdev.cerofiao.core.domain.repository.ExchangeRateRepository }
) {
    override suspend fun resolve(from: String, to: String, preferredSource: ExchangeRateSource): RateResult {
        return RateResult(1.0, preferredSource) // simplistic fake
    }
    
    override suspend fun toUsd(currencyCode: String, preferredSource: ExchangeRateSource): RateResult {
        // Assume 1 USD = 60 VES for this test environment
        val rate = if (currencyCode == "VES") 1.0 / 60.0 else 1.0
        return RateResult(rate, preferredSource)
    }

    override suspend fun fromUsd(currencyCode: String, preferredSource: ExchangeRateSource): RateResult {
        val rate = if (currencyCode == "VES") 60.0 else 1.0
        return RateResult(rate, preferredSource)
    }
}

class FakeUserPreferencesRepository : UserPreferencesRepository {
    private val _prefs = MutableStateFlow(
        UserPreferences(
            displayCurrencyCode = "USD",
            preferredRateSource = ExchangeRateSource.BCV
        )
    )
    override val userPreferences: Flow<UserPreferences> = _prefs

    override suspend fun setDisplayCurrency(currencyCode: String) {}
    override suspend fun setPreferredRateSource(source: ExchangeRateSource) {}
    override suspend fun setThemeMode(mode: ThemeMode) {}
    override suspend fun setUseDynamicColor(enabled: Boolean) {}
    override suspend fun setOnboardingCompleted() {}
}

@OptIn(ExperimentalCoroutinesApi::class)
class BillSplitterViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockResolveUseCase: FakeResolveExchangeRateUseCaseForVM
    private lateinit var fakeUserPrefs: FakeUserPreferencesRepository
    private lateinit var viewModel: BillSplitterViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockResolveUseCase = FakeResolveExchangeRateUseCaseForVM()
        fakeUserPrefs = FakeUserPreferencesRepository()
        viewModel = BillSplitterViewModel(mockResolveUseCase, fakeUserPrefs)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `adding participants creates equal split by default`() = runTest {
        viewModel.onTotalAmountChanged("100")
        
        viewModel.addParticipant("Alice")
        viewModel.addParticipant("Bob")
        
        val state = viewModel.state.value
        assertEquals(2, state.participants.size)
        // Equal split of 100 between 2 people is 50 each
        assertEquals(50.0, state.participants[0].finalAmountBaseCurrency, 0.001)
        assertEquals(50.0, state.participants[1].finalAmountBaseCurrency, 0.001)
        assertEquals(0.0, state.unallocatedBase, 0.001)
    }

    @Test
    fun `fixed split reduces amount remaining for equal splits`() = runTest {
        viewModel.onTotalAmountChanged("100")
        
        viewModel.addParticipant("Alice") // Equal
        viewModel.addParticipant("Bob")   // Fixed
        
        val bobId = viewModel.state.value.participants[1].id
        viewModel.updateParticipantType(bobId, SplitType.FIXED)
        viewModel.updateParticipantFixed(bobId, 40.0)
        
        val state = viewModel.state.value
        // Bob gets 40, Alice gets remaining 60
        val alice = state.participants.find { it.name == "Alice" }!!
        val bob = state.participants.find { it.name == "Bob" }!!
        
        assertEquals(60.0, alice.finalAmountBaseCurrency, 0.001)
        assertEquals(40.0, bob.finalAmountBaseCurrency, 0.001)
        assertEquals(0.0, state.unallocatedBase, 0.001)
    }

    @Test
    fun `percentage split calculates correctly from total`() = runTest {
        viewModel.onTotalAmountChanged("200")
        
        viewModel.addParticipant("Alice")   // Percentage
        viewModel.addParticipant("Bob")     // Equal
        
        val aliceId = viewModel.state.value.participants[0].id
        viewModel.updateParticipantType(aliceId, SplitType.PERCENTAGE)
        viewModel.updateParticipantPercentage(aliceId, 25.0) // 25% of 200 = 50
        
        val state = viewModel.state.value
        val alice = state.participants.find { it.name == "Alice" }!!
        val bob = state.participants.find { it.name == "Bob" }!!
        
        assertEquals(50.0, alice.finalAmountBaseCurrency, 0.001)
        assertEquals(150.0, bob.finalAmountBaseCurrency, 0.001)
    }

    @Test
    fun `mixed split configurations resolve properly`() = runTest {
        viewModel.onTotalAmountChanged("500")
        
        viewModel.addParticipant("A") // Fixed: 100
        viewModel.addParticipant("B") // Percentage: 20% -> 100
        viewModel.addParticipant("C") // Equal
        viewModel.addParticipant("D") // Equal
        
        val stateInit = viewModel.state.value
        val idA = stateInit.participants[0].id
        val idB = stateInit.participants[1].id
        
        viewModel.updateParticipantType(idA, SplitType.FIXED)
        viewModel.updateParticipantFixed(idA, 100.0)
        
        viewModel.updateParticipantType(idB, SplitType.PERCENTAGE)
        viewModel.updateParticipantPercentage(idB, 20.0) // 20% = 100
        
        val state = viewModel.state.value
        val participants = state.participants
        
        // C and D evenly split the remaining 300
        assertEquals(100.0, participants.find { it.name == "A" }!!.finalAmountBaseCurrency, 0.001)
        assertEquals(100.0, participants.find { it.name == "B" }!!.finalAmountBaseCurrency, 0.001)
        assertEquals(150.0, participants.find { it.name == "C" }!!.finalAmountBaseCurrency, 0.001)
        assertEquals(150.0, participants.find { it.name == "D" }!!.finalAmountBaseCurrency, 0.001)
    }
}
