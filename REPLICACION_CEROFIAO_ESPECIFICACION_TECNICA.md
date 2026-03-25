# CeroFiao — Especificación Técnica para Replicación por IA

> Documento exhaustivo para que otra IA pueda reconstruir la aplicación CeroFiao desde cero.  
> Fecha: 2025-03-20 | Repo: Totito1313/Claude-CeroFiao

---

## 1. Resumen Ejecutivo

**CeroFiao** es una app Android de seguimiento de gastos multi-moneda para Venezuela. Características principales:

- **Monedas:** VES (Base de referencia visualizada como Bs.), USD (Tasa BCV), USDT (Paralelo), EUR (BCV), EURI (Paralelo)
- **Tasas en tiempo real:** BCV (oficial USD y EURO), USDT/EURI (paralelo) vía dolarapi.com
- **Arquitectura:** Clean Architecture + MVVM, multi-módulo
- **UI:** Jetpack Compose (Evitar usar Material 3, preferiblemente usar componentes de diseño propietarios con un estilo moderno y minimalista), offline-first
- **Sincronización cloud:** Planeada (Supabase), no implementada aún

---

## 2. Stack Tecnológico

| Categoría | Tecnología | Versión |
|-----------|------------|---------|
| Lenguaje | Kotlin | 2.2.10 |
| AGP | Android Gradle Plugin | 9.1.0 |
| Compile SDK | Android | 36 |
| Min SDK | Android | 34 |
| Target SDK | Android | 36 |
| Java | JDK | 21 |
| UI | Jetpack Compose | BOM 2024.12.01 |
| Material | Material 3 | (via Compose BOM) |
| DI | Hilt | 2.59.2 |
| Procesador | KSP | 2.2.10-2.0.2 |
| Base de datos | Room | 2.7.1 |
| Red | Ktor Client | 3.1.1 |
| Async | Kotlin Coroutines | 1.10.1 |
| Fechas | kotlinx-datetime | 0.6.2 |
| Serialización | kotlinx-serialization | 1.8.1 |
| Preferencias | DataStore | 1.1.4 |
| WorkManager | WorkManager | 2.10.1 |
| Gráficos | Vico (charts) | 2.1.0 |
| Imágenes | Coil 3 | 3.1.0 |
| Iconos | Lucide Icons | 1.0.0 |
| Iconos categorías | OneUI Icons | 1.1.0 |
| Widget | Glance | 1.1.1 |
| CSV | OpenCSV | 5.9 |

---

## 3. Estructura del Proyecto

```
CeroFiao/
├── app/                          # Módulo principal (applicationId: com.SchwarckDev.CeroFiao)
├── build-logic/convention/       # Plugins de convención Gradle
├── core/
│   ├── core-model/               # Entidades de dominio (sin deps Android)
│   ├── core-common/              # Utilidades (CurrencyFormatter, DateUtils, MoneyCalculator, UuidGenerator)
│   ├── core-designsystem/        # Tema, colores, tipografía, iconos (Lucide + OneUI)
│   ├── core-database/            # Room DB, DAOs, entidades, mappers, prepopulate
│   ├── core-datastore/           # DataStore (preferencias de usuario)
│   ├── core-network/             # Cliente Ktor para dolarapi.com
│   ├── core-data/                # Implementaciones de repositorios + DataModule (Hilt)
│   ├── core-domain/              # Casos de uso + interfaces de repositorios
│   └── core-ui/                  # Componentes Compose compartidos (MoneyText, EmptyState, GlassCard)
└── feature/
    ├── feature-dashboard/        # Pantalla principal
    ├── feature-transactions/     # Lista, entrada, detalle, transferencia, recurrentes, actividad
    ├── feature-accounts/         # Cuentas (lista, detalle, agregar)
    ├── feature-categories/       # Categorías CRUD con subcategorías
    ├── feature-exchange-rates/   # Tasas BCV, USDT, EURI + gráficos históricos
    ├── feature-settings/         # Tema, moneda, CSV, títulos asociados
    ├── feature-onboarding/       # Setup inicial
    ├── feature-budget/           # Presupuestos CRUD
    ├── feature-debt/             # Deudas (debo/me deben)
    ├── feature-goals/            # Objetivos de ahorro
    └── feature-billsplitter/     # Divisor de cuentas
```

---

## 4. Sistema de Build (Convention Plugins)

Los plugins se definen en `build-logic/convention/`:

### 4.1 `cerofiao.android.application`
- apply: `com.android.application`, `org.jetbrains.kotlin.plugin.serialization`
- compileSdk: 36, minSdk: 34, targetSdk: 36
- Java 21
- testInstrumentationRunner: AndroidJUnitRunner

### 4.2 `cerofiao.android.library`
- apply: `com.android.library`, `org.jetbrains.kotlin.plugin.serialization`
- compileSdk: 36, minSdk: 34
- Java 21

### 4.3 `cerofiao.android.compose`
- apply: `org.jetbrains.kotlin.plugin.compose`
- Dependencias: compose-bom, ui, material3, foundation, animation
- debug: ui-tooling, ui-test-manifest

### 4.4 `cerofiao.android.feature`
- apply: `cerofiao.android.library`, `cerofiao.android.compose`, `cerofiao.android.hilt`
- Dependencias auto: core-model, core-domain, core-ui, core-designsystem, core-common
- navigation-compose, hilt-navigation-compose, lifecycle-viewmodel-compose, kotlinx-serialization-json

### 4.5 `cerofiao.android.hilt`
- apply: `com.google.dagger.hilt.android`, `com.google.devtools.ksp`
- hilt-android, hilt-compiler

### 4.6 `cerofiao.android.room`
- apply: `androidx.room`, `com.google.devtools.ksp`
- schemaDirectory: `$projectDir/schemas`
- room-runtime, room-ktx, room-compiler

---

## 5. Capa de Modelo (core-model)

Paquete: `com.schwarckdev.cerofiao.core.model`

Todas las entidades usan `@Serializable` de kotlinx.serialization.

### 5.1 Transaction

```kotlin
enum class TransactionType { INCOME, EXPENSE, TRANSFER }

data class Transaction(
    val id: String,
    val type: TransactionType,
    val originalAmount: Double,
    val originalCurrency: String,
    val frozenVesAmount: Double,       // Valor congelado triangulado a VES
    val appliedExchangeRate: Double,   // Tasa congelada en el momento
    val rateSource: ExchangeRateSource,
    val commissionAmount: Double = 0.0,
    val commissionCurrency: String? = null,
    val accountId: String,
    val categoryId: String? = null,
    val note: String? = null,
    val date: Long,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val transferLinkedId: String? = null,
    val transferToAccountId: String? = null,
    val receiptImagePath: String? = null,
)
```

### 5.2 Account

```kotlin
enum class AccountType { CASH, BANK, DIGITAL_WALLET, CRYPTO_EXCHANGE }

enum class AccountPlatform(
    val displayName: String,
    val defaultType: AccountType,
    val defaultCurrencyCode: String,
    private val domain: String 
) {
    // Generales
    NONE("Otro", AccountType.CASH, "USD", ""),

    // Bancos Nacionales
    VENEZUELA("Banco de Venezuela", AccountType.BANK, "VES", "bancodevenezuela.com"),
    BANESCO("Banesco", AccountType.BANK, "VES", "banesco.com"),
    MERCANTIL("Mercantil", AccountType.BANK, "VES", "mercantilbanco.com"),
    PROVINCIAL("Provincial", AccountType.BANK, "VES", "provincial.com"),
    BNC("BNC", AccountType.BANK, "VES", "bncenlinea.com"),
    BANCAMIGA("Bancamiga", AccountType.BANK, "VES", "bancamiga.com"),
    BANCARIBE("Bancaribe", AccountType.BANK, "VES", "bancaribe.com.ve"),
    BANPLUS("Banplus", AccountType.BANK, "VES", "banplus.com"),
    BICENTENARIO("Bicentenario", AccountType.BANK, "VES", "bicentenariobu.com"),
    TESORO("Banco del Tesoro", AccountType.BANK, "VES", "bt.gob.ve"),
    EXTERIOR("Banco Exterior", AccountType.BANK, "VES", "bancoexterior.com"),
    BFC("Fondo Común (BFC)", AccountType.BANK, "VES", "bfc.com.ve"),
    BVC("Venezolano de Crédito", AccountType.BANK, "VES", "venezolano.com"),
    PLAZA("Banco Plaza", AccountType.BANK, "VES", "bancoplaza.com"),
    CARONI("Banco Caroní", AccountType.BANK, "VES", "bancocaroni.com.ve"),
    ACTIVO("Banco Activo", AccountType.BANK, "VES", "bancoactivo.com"),
    SOFITASA("Banco Sofitasa", AccountType.BANK, "VES", "sofitasa.com"),
    CIEN_POR_CIENTO("100% Banco", AccountType.BANK, "VES", "100x100banco.com.ve"),
    DEL_SUR("Banco Del Sur", AccountType.BANK, "VES", "delsur.com.ve"),
    MI_BANCO("Mi Banco", AccountType.BANK, "VES", "mibanco.com.ve"),
    BANCRECER("Bancrecer", AccountType.BANK, "VES", "bancrecer.com.ve"),

    // Billeteras Digitales y Fintechs
    ZELLE("Zelle", AccountType.DIGITAL_WALLET, "USD", "zellepay.com"),
    ZINLI("Zinli", AccountType.DIGITAL_WALLET, "USD", "zinli.com"),
    WALLY("Wally", AccountType.DIGITAL_WALLET, "USD", "wally.tech"),
    PAYPAL("PayPal", AccountType.DIGITAL_WALLET, "USD", "paypal.com"),
    UBII("Ubii App", AccountType.DIGITAL_WALLET, "VES", "ubiiapp.com"),
    AIRTM("AirTM", AccountType.DIGITAL_WALLET, "USD", "airtm.com"),
    PAYONEER("Payoneer", AccountType.DIGITAL_WALLET, "USD", "payoneer.com"),
    SKRILL("Skrill", AccountType.DIGITAL_WALLET, "USD", "skrill.com"),
    CASH_APP("Cash App", AccountType.DIGITAL_WALLET, "USD", "cash.app"),

    // Exchanges de Criptomonedas
    BINANCE("Binance", AccountType.CRYPTO_EXCHANGE, "USDT", "binance.com"),
    EL_DORADO("El Dorado", AccountType.CRYPTO_EXCHANGE, "USDT", "eldorado.io"),
    BYBIT("Bybit", AccountType.CRYPTO_EXCHANGE, "USDT", "bybit.com"),
    KUCOIN("KuCoin", AccountType.CRYPTO_EXCHANGE, "USDT", "kucoin.com"),
    OKX("OKX", AccountType.CRYPTO_EXCHANGE, "USDT", "okx.com");

    /**
     * Genera la URL dinámica del logo en alta calidad.
     * Ya incluye tu API Key de forma nativa.
     */
    fun getLogoUrl(): String {
        if (this == NONE || domain.isEmpty()) return ""
        
        val token = "pk_D0EjcN7MSum6KtDpMv6uAQ"
        
        // Usamos la búsqueda por dominio que es más exacta que /name/
        // Agregamos format=png para respetar las transparencias del diseño CeroFiao
        return "https://img.logo.dev/$domain?token=$token&format=png"
    }
}

//Uso con Jetpack Compose 
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest

@Composable
fun AccountLogo(
    platform: AccountPlatform, 
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(platform.getLogoUrl())
            .crossfade(true) // Animación suave al cargar
            // 1. Guarda en RAM: Carga instantánea si ya lo vio en esta sesión
            .memoryCachePolicy(CachePolicy.ENABLED) 
            // 2. Guarda en Almacenamiento: No vuelve a descargar de internet si cierra y abre la app
            .diskCachePolicy(CachePolicy.ENABLED)   
            // Opcional: Si el logo falla o está vacío, Coil no hará nada extraño
            .build(),
        contentDescription = "Logo de ${platform.displayName}",
        modifier = modifier
            .size(44.dp) // Tamaño estándar de tu diseño CeroFiao
            .clip(CircleShape)
    )
}
//Fin de uso de LOGOS

data class Account(
    val id: String,
    val name: String,
    val type: AccountType,
    val platform: AccountPlatform = NONE,
    val currencyCode: String,
    val balance: Double = 0.0,
    val initialBalance: Double = 0.0,
    val iconName: String? = null,
    val colorHex: String? = null,
    val isActive: Boolean = true,
    val includeInTotal: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)
```

### 5.3 Category

```kotlin
enum class CategoryType { INCOME, EXPENSE }

data class Category(
    val id: String,
    val name: String,
    val type: CategoryType,
    val iconName: String,
    val colorHex: String,
    val parentId: String? = null,      // subcategorías
    val isDefault: Boolean = false,
    val sortOrder: Int = 0,
    val isActive: Boolean = true,
)

data class CategoryNode(val category: Category, val subcategories: List<Category>)


enum class TransactionType { INCOME, EXPENSE }

enum class TransactionCategory(
    val id: String,
    val displayName: String,
    val type: TransactionType,
    val parentGroup: String,
    val iconKey: String // Para enlazar con tu IconResolver
) {
    // === INGRESOS ===
    SALARY("inc_salary", "Salario", TransactionType.INCOME, "Laboral", "ic_wallet"),
    FREELANCE("inc_freelance", "Freelance", TransactionType.INCOME, "Laboral", "ic_laptop"),
    BONUS("inc_bonus", "Bonos", TransactionType.INCOME, "Laboral", "ic_star"),
    SALES("inc_sales", "Ventas", TransactionType.INCOME, "Negocios", "ic_store"),
    REMITTANCE("inc_remittance", "Remesas", TransactionType.INCOME, "Transferencias", "ic_globe"),
    RECEIVED_TRANSFER("inc_transfer", "Transferencia", TransactionType.INCOME, "Transferencias", "ic_arrow_down_left"),
    INVESTMENTS("inc_investments", "Inversiones/Cripto", TransactionType.INCOME, "Pasivos", "ic_trending_up"),
    OTHER_INCOME("inc_other", "Otros Ingresos", TransactionType.INCOME, "Otros", "ic_plus_circle"),

    // === GASTOS ===
    // Alimentación
    GROCERIES("exp_groceries", "Mercado", TransactionType.EXPENSE, "Alimentación", "ic_shopping_cart"),
    RESTAURANT("exp_restaurant", "Restaurantes", TransactionType.EXPENSE, "Alimentación", "ic_restaurant"),
    SNACKS("exp_snacks", "Snacks y Antojos", TransactionType.EXPENSE, "Alimentación", "ic_cookie"),
    COFFEE("exp_coffee", "Café y Panadería", TransactionType.EXPENSE, "Alimentación", "ic_coffee"),

    // Transporte
    PUBLIC_TRANSIT("exp_transit", "Transporte Público", TransactionType.EXPENSE, "Transporte", "ic_bus"),
    TAXI("exp_taxi", "Taxi / Apps", TransactionType.EXPENSE, "Transporte", "ic_car"),
    GAS("exp_gas", "Gasolina", TransactionType.EXPENSE, "Transporte", "ic_gas_station"),
    AUTO_REPAIR("exp_auto_repair", "Mantenimiento Vehículo", TransactionType.EXPENSE, "Transporte", "ic_wrench"),

    // Vivienda y Servicios
    RENT("exp_rent", "Alquiler / Hipoteca", TransactionType.EXPENSE, "Vivienda", "ic_home"),
    UTILITIES("exp_utilities", "Servicios (Luz/Agua/Gas)", TransactionType.EXPENSE, "Vivienda", "ic_zap"),
    INTERNET_TV("exp_internet", "Internet y TV", TransactionType.EXPENSE, "Vivienda", "ic_wifi"),
    MOBILE_RECHARGE("exp_recharge", "Recargas Celular", TransactionType.EXPENSE, "Vivienda", "ic_smartphone"),

    // Salud y Cuidado
    PHARMACY("exp_pharmacy", "Farmacia", TransactionType.EXPENSE, "Salud", "ic_pill"),
    MEDICAL("exp_medical", "Consultas Médicas", TransactionType.EXPENSE, "Salud", "ic_stethoscope"),
    PERSONAL_CARE("exp_personal_care", "Peluquería / Cuidado", TransactionType.EXPENSE, "Salud", "ic_scissors"),
    GYM("exp_gym", "Gimnasio", TransactionType.EXPENSE, "Salud", "ic_dumbbell"),

    // Entretenimiento
    SUBSCRIPTIONS("exp_subscriptions", "Suscripciones", TransactionType.EXPENSE, "Entretenimiento", "ic_play_circle"),
    NIGHTLIFE("exp_nightlife", "Salidas / Fiestas", TransactionType.EXPENSE, "Entretenimiento", "ic_glass_water"),
    GAMING("exp_gaming", "Videojuegos", TransactionType.EXPENSE, "Entretenimiento", "ic_gamepad"),

    // Compras
    CLOTHING("exp_clothing", "Ropa y Calzado", TransactionType.EXPENSE, "Compras", "ic_shirt"),
    TECH("exp_tech", "Tecnología", TransactionType.EXPENSE, "Compras", "ic_laptop"),
    PETS("exp_pets", "Mascotas", TransactionType.EXPENSE, "Compras", "ic_paw"),

    // Educación
    EDUCATION("exp_education", "Universidad / Cursos", TransactionType.EXPENSE, "Educación", "ic_book"),

    // Financiero
    FEES("exp_fees", "Comisiones Bancarias", TransactionType.EXPENSE, "Financiero", "ic_receipt"),
    EXCHANGE("exp_exchange", "Cambio de Divisas", TransactionType.EXPENSE, "Financiero", "ic_refresh"),
    DEBT_PAYMENT("exp_debt", "Pago de Deudas", TransactionType.EXPENSE, "Financiero", "ic_credit_card"),

    // Otros
    EMERGENCY("exp_emergency", "Imprevistos", TransactionType.EXPENSE, "Otros", "ic_alert_triangle"),
    OTHER_EXPENSE("exp_other", "Otros Gastos", TransactionType.EXPENSE, "Otros", "ic_minus_circle");

    companion object {
        fun getIncomes() = entries.filter { it.type == TransactionType.INCOME }
        fun getExpenses() = entries.filter { it.type == TransactionType.EXPENSE }
        
        // Útil para armar menús desplegables agrupados en tu UI
        fun getExpensesGrouped() = getExpenses().groupBy { it.parentGroup }
    }
}

### 5.4 Currency & ExchangeRate Engine

import java.util.UUID

// ==========================================
// 1. ENUMS Y CONFIGURACIÓN BASE
// ==========================================

enum class ExchangeRateSource { BCV, PARALELO, BINANCE_P2P, MANUAL }

data class Currency(
    val code: String, // VES, USD, USDT, EUR, EURI...
    val name: String,
    val symbol: String,
    val decimalPlaces: Int = 2,
    val isActive: Boolean = true,
    val sortOrder: Int = 0,
)

object Currencies {
    val VES = Currency("VES", "Bolívar", "Bs.", 2, sortOrder = 1)
    val USD = Currency("USD", "Dólar", "$", 2, sortOrder = 2)
    val USDT = Currency("USDT", "Tether US", "USDT", 2, sortOrder = 3)
    val EUR = Currency("EUR", "Euro", "€", 2, sortOrder = 4)
    val EURI = Currency("EURI", "Euro Digital", "EURI", 2, sortOrder = 5)

    val all = listOf(VES, USD, USDT, EUR, EURI)

    /**
     * Define qué tasa se usa por DEFECTO al convertir esta moneda hacia o desde VES.
     * (El usuario siempre podrá sobrescribirla eligiendo MANUAL).
     */
    fun defaultSource(code: String): ExchangeRateSource = when (code) {
        "USD", "EUR" -> ExchangeRateSource.BCV
        "USDT", "EURI" -> ExchangeRateSource.PARALELO 
        else -> ExchangeRateSource.BCV
    }
}

// ==========================================
// 2. MODELOS DE DATOS (ENTITIES)
// ==========================================

// Usado para cálculos en memoria (Motor de conversión)
data class ExchangeRate(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: Double,
    val date: String,
    val source: ExchangeRateSource,
    val fetchedAt: Long = 0L,
)

// Usado para guardar el historial en la Base de Datos (Room/Firebase)
data class ExchangeRateRecord(
    val id: String = UUID.randomUUID().toString(),
    val currencyCode: String,          // Ej: "USD"
    val rateToVes: Double,             // Ej: 42.50
    val source: ExchangeRateSource,    // Ej: BCV
    val timestamp: Long,               // Milisegundos exactos
    val dateString: String             // Ej: "2026-03-20"
)

// Transaction: modelo canónico en sección 5.1 (frozenVesAmount, appliedExchangeRate, rateSource, comisiones)

// ==========================================
// 3. MOTOR DE CONVERSIÓN INTELIGENTE
// ==========================================

class CurrencyConverter(private val currentRates: List<ExchangeRate>) {

    fun convert(
        amount: Double, 
        fromCode: String, 
        toCode: String, 
        preferredSource: ExchangeRateSource? = null
    ): Double {
        if (fromCode == toCode) return amount

        // 1. Intentar buscar una tasa directa EXACTA primero (Ej: USD <-> USDT Manual)
        val directRate = getDirectRateOrNull(fromCode, toCode, preferredSource)
        if (directRate != null) {
            return amount * directRate
        }

        // 2. Si no hay tasa directa, y ninguna es VES, triangulamos por Bolívares (Arbitraje)
        if (fromCode != "VES" && toCode != "VES") {
            val sourceFrom = preferredSource ?: Currencies.defaultSource(fromCode)
            val sourceTo = preferredSource ?: Currencies.defaultSource(toCode)

            val rateToVes = getRate(foreign = fromCode, local = "VES", source = sourceFrom)
            val rateFromVes = getRate(foreign = toCode, local = "VES", source = sourceTo)
            
            val amountInVes = amount * rateToVes
            return amountInVes / rateFromVes
        }

        // 3. Conversión de Extranjera a VES
        if (toCode == "VES") {
            val source = preferredSource ?: Currencies.defaultSource(fromCode)
            return amount * getRate(fromCode, "VES", source)
        }

        // 4. Conversión de VES a Extranjera
        if (fromCode == "VES") {
            val source = preferredSource ?: Currencies.defaultSource(toCode)
            return amount / getRate(toCode, "VES", source)
        }

        return amount
    }

    private fun getDirectRateOrNull(from: String, to: String, source: ExchangeRateSource?): Double? {
        val rateObj = currentRates.find { 
            ((it.fromCurrency == from && it.toCurrency == to) ||
            (it.fromCurrency == to && it.toCurrency == from)) && 
            (source == null || it.source == source)
        } ?: return null

        return if (rateObj.fromCurrency == from) rateObj.rate else (1 / rateObj.rate)
    }

    private fun getRate(foreign: String, local: String, source: ExchangeRateSource): Double {
        return getDirectRateOrNull(foreign, local, source) 
            ?: throw IllegalArgumentException("No hay tasa registrada entre $foreign y $local para la fuente $source")
    }
}

// ==========================================
// 4. REPOSITORIOS Y CASOS DE USO (LÓGICA DE GUARDADO)
// ==========================================

interface ExchangeRateRepository {
    suspend fun getCurrentRate(currency: String, source: ExchangeRateSource): ExchangeRateRecord
    suspend fun getRateHistory(currency: String, source: ExchangeRateSource): List<ExchangeRateRecord>
    suspend fun saveRates(rates: List<ExchangeRateRecord>)
}

interface TransactionRepository {
    suspend fun saveTransaction(transaction: Transaction)
}

/**
 * Caso de Uso: Registra una transacción congelando su valor y registrando comisiones.
 */
class RegisterTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val exchangeRateRepository: ExchangeRateRepository
) {
    suspend operator fun invoke(
        amount: Double, 
        currencyCode: String, 
        categoryId: String,
        commissionAmount: Double = 0.0,
        commissionCurrency: String = currencyCode,
        manualRate: Double? = null // Pasar un valor si el usuario editó la tasa a mano
    ) {
        // 1. Si la moneda es VES y no hay tasa manual, se guarda tal cual.
        if (currencyCode == "VES" && manualRate == null) {
            val tx = Transaction(
                originalAmount = amount,
                originalCurrency = "VES",
                frozenVesAmount = amount,
                appliedExchangeRate = 1.0,
                rateSource = ExchangeRateSource.BCV,
                commissionAmount = commissionAmount,
                commissionCurrency = commissionCurrency,
                categoryId = categoryId
            )
            transactionRepository.saveTransaction(tx)
            return
        }

        // 2. Determinar la fuente y la tasa a aplicar
        val source = if (manualRate != null) ExchangeRateSource.MANUAL else Currencies.defaultSource(currencyCode)
        val appliedRate = if (manualRate != null) {
            manualRate
        } else {
            val currentRateRecord = exchangeRateRepository.getCurrentRate(currencyCode, source)
            currentRateRecord.rateToVes
        }

        // 3. Calcular el monto congelado en VES (Para que jamás cambie en el historial)
        val amountInVes = amount * appliedRate

        // 4. Crear la transacción
        val transaction = Transaction(
            originalAmount = amount,
            originalCurrency = currencyCode,
            frozenVesAmount = amountInVes, 
            appliedExchangeRate = appliedRate,
            rateSource = source,
            commissionAmount = commissionAmount,
            commissionCurrency = commissionCurrency,
            categoryId = categoryId
        )

        // 5. Guardar
        transactionRepository.saveTransaction(transaction)
    }
}
```
### 5.5 GlobalBalance

```kotlin
// ==========================================
// 1. MODELOS DE ESTADO (UI / DOMAIN)
// ==========================================

data class GlobalBalance(
    val totalInDisplayCurrency: Double,
    val displayCurrencyCode: String, // Ej: El usuario eligió ver su resumen en "USD" o "VES"
    val breakdownByAccount: List<AccountBalance>,
    val breakdownByCurrency: List<CurrencyBalance>
)

data class AccountBalance(
    val accountId: String,
    val accountName: String,
    val originalCurrencyCode: String,
    val balanceInOriginalCurrency: Double, // Ej: 5000 Bs en Banesco
    val balanceInDisplayCurrency: Double   // Ej: 119.04 USD (Calculado con la tasa de HOY)
)

data class CurrencyBalance(
    val currencyCode: String,
    val totalInOriginalCurrency: Double, // Ej: Todo el USDT sumado (Binance + El Dorado)
    val totalInDisplayCurrency: Double   // Su equivalente en la moneda principal
)

// ==========================================
// 2. CASO DE USO (Calcula el balance usando el nuevo CurrencyConverter)
// ==========================================

class CalculateGlobalBalanceUseCase(
    private val currencyConverter: CurrencyConverter
) {
    /**
     * @param accounts Lista de cuentas del usuario con su saldo actual real.
     * @param displayCurrency La moneda en la que el usuario quiere ver su Total (Ej: "USD").
     */
    operator fun invoke(
        accounts: List<AccountSnapshot>, // Tu modelo de BD que tiene el saldo de la cuenta
        displayCurrency: String
    ): GlobalBalance {
        
        val accountBalances = mutableListOf<AccountBalance>()
        var globalTotal = 0.0

        // 1. Calcular el balance por cuenta
        for (acc in accounts) {
            // Usamos nuestro motor inteligente para llevar el saldo de la cuenta a la moneda de vista
            val convertedBalance = currencyConverter.convert(
                amount = acc.currentBalance,
                fromCode = acc.currencyCode,
                toCode = displayCurrency
            )
            
            globalTotal += convertedBalance
            
            accountBalances.add(
                AccountBalance(
                    accountId = acc.id,
                    accountName = acc.name,
                    originalCurrencyCode = acc.currencyCode,
                    balanceInOriginalCurrency = acc.currentBalance,
                    balanceInDisplayCurrency = convertedBalance
                )
            )
        }

        // 2. Agrupar saldos por Moneda (Ej: Sumar todos los dólares físicos de distintas billeteras)
        val breakdownByCurrency = accountBalances
            .groupBy { it.originalCurrencyCode }
            .map { (currencyCode, accountsInThisCurrency) ->
                CurrencyBalance(
                    currencyCode = currencyCode,
                    totalInOriginalCurrency = accountsInThisCurrency.sumOf { it.balanceInOriginalCurrency },
                    totalInDisplayCurrency = accountsInThisCurrency.sumOf { it.balanceInDisplayCurrency }
                )
            }
            // Opcional: Ordenar para que las de mayor peso salgan primero
            .sortedByDescending { it.totalInDisplayCurrency }

        // 3. Retornar el Estado Global
        return GlobalBalance(
            totalInDisplayCurrency = globalTotal,
            displayCurrencyCode = displayCurrency,
            breakdownByAccount = accountBalances,
            breakdownByCurrency = breakdownByCurrency
        )
    }
}

// Modelo de apoyo simulando la BD
data class AccountSnapshot(val id: String, val name: String, val currencyCode: String, val currentBalance: Double)
```

### 5.6 Budget

```kotlin
enum class BudgetPeriod { WEEKLY, BIWEEKLY, MONTHLY }

data class Budget(
    val id: String,
    val name: String,
    val limitAmount: Double,
    val anchorCurrencyCode: String,
    val period: BudgetPeriod,
    val categoryId: String? = null,
    val startDate: String,
    val isRecurring: Boolean = true,
    val isActive: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)
```

### 5.7 Debt / DebtPayment

```kotlin
enum class DebtType { I_OWE, THEY_OWE }

data class Debt(
    val id: String,
    val personName: String,
    val personPhone: String? = null,
    val type: DebtType,
    val originalAmount: Double,
    val currencyCode: String,
    val remainingAmount: Double,
    val exchangeRateToVesAtCreation: Double = 1.0,  // Cambiado a VES
    val note: String? = null,
    val dueDate: Long? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val isSettled: Boolean = false,
    val settledAt: Long? = null,
)

data class DebtPayment(
    val id: String,
    val debtId: String,
    val transactionId: String? = null,
    val amount: Double,
    val currencyCode: String,
    val appliedExchangeRate: Double = 1.0,  // Tasa congelada al momento del pago
    val paidAt: Long = 0L,
    val note: String? = null,
)
```

### 5.8 RecurringTransaction

```kotlin
enum class RecurrenceType { DAILY, WEEKLY, MONTHLY, YEARLY }

data class RecurringTransaction(
    val id: String,
    val title: String,
    val amount: Double,
    val currencyCode: String,
    val categoryId: String?,
    val accountId: String,
    val type: TransactionType,
    val recurrence: RecurrenceType,
    val periodLength: Int = 1,
    val startDate: Long,
    val endDate: Long? = null,
    val nextDueDate: Long,
    val isActive: Boolean = true,
    val note: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
)
```

### 5.9 TransactionTitle (Associated Titles)

```kotlin
data class TransactionTitle(
    val id: String,
    val title: String,
    val categoryId: String,
    val isExactMatch: Boolean = false,
)
```

### 5.10 TransactionLog

```kotlin
enum class TransactionLogAction { CREATED, EDITED, DELETED }

data class TransactionLog(
    val id: String,
    val transactionId: String,
    val action: TransactionLogAction,
    val timestamp: Long,
    val snapshotJson: String,
)
```

### 5.11 SavingsGoal / SavingsContribution

```kotlin
data class SavingsGoal(
    val id: String,
    val name: String,
    val targetAmount: Double,
    val currencyCode: String,
    val currentAmountInVes: Double = 0.0,  // Cambiado a VES
    val iconName: String? = null,
    val colorHex: String? = null,
    val deadline: Long? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
)

data class SavingsContribution(
    val id: String,
    val goalId: String,
    val transactionId: String? = null,
    val amount: Double,
    val currencyCode: String,
    val appliedExchangeRate: Double = 1.0,  // Tasa usada al aportar
    val contributedAt: Long = 0L,
)
```

### 5.12 UserPreferences

```kotlin
enum class ThemeMode { LIGHT, DARK, SYSTEM }

data class UserPreferences(
    val displayCurrencyCode: String = "USD",
    val preferredRateSource: ExchangeRateSource = ExchangeRateSource.PARALELO,
    val themeMode: ThemeMode = SYSTEM,
    val useDynamicColor: Boolean = false,
    val hasCompletedOnboarding: Boolean = false,
)
```

---

## 6. Capa de Utilidades (core-common)

### 6.1 MoneyCalculator

```kotlin
object MoneyCalculator {
    fun convert(amount: Double, rate: Double): Double  // BigDecimal HALF_UP, scale 2
    fun toVes(amountForeign: Double, rateToVes: Double): Double
    fun fromVes(amountVes: Double, rateFromVes: Double): Double
    fun applyCommission(amount: Double, commissionPercent: Double): Double
    fun calculateCommission(amount: Double, commissionPercent: Double): Double
}
```

### 6.2 CurrencyFormatter

- `format(amount, currencyCode, showSymbol)`: formatea según moneda
- `formatCompact(amount, currencyCode)`: 1.5M Bs, $2.3K, etc.
- VES: separador miles `.`, decimal `,`
- Otras: formato US estándar

### 6.3 DateUtils

```kotlin
object DateUtils {
    fun now(): Long
    fun todayIsoDate(): String
    fun toIsoDate(epochMillis: Long): String
    fun toLocalDate(epochMillis: Long): LocalDate
    fun formatDisplayDate(epochMillis: Long): String  // dd/MM/yyyy
    fun formatDisplayDateTime(epochMillis: Long): String
    fun isToday(epochMillis: Long): Boolean
    fun startOfDay(epochMillis: Long): Long
    fun startOfMonth(epochMillis: Long): Long
    fun endOfMonth(epochMillis: Long): Long
    fun getCurrentMonthRange(): Pair<Long, Long>
    fun parseDdMmYyyy(dateStr: String): Long?
}
```

Usa `kotlinx.datetime` (Clock, Instant, LocalDate, TimeZone).

### 6.4 UuidGenerator

```kotlin
object UuidGenerator { fun generate(): String }
```

---

## 7. Base de Datos (core-database)

### 7.1 Tablas (Room)

| Tabla | Notas |
|-------|-------|
| currency | Monedas |
| account | Cuentas (syncId, isDeleted) |
| transactions | FK account, category; syncId, isDeleted |
| category | parentId para subcategorías; syncId, isDeleted |
| exchange_rate | fromCurrency, toCurrency, rate, date, source, fetchedAt |
| debt | syncId, isDeleted |
| debt_payment | FK debt |
| savings_goal | syncId, isDeleted |
| savings_contribution | FK goalId |
| budget | syncId, isDeleted |
| sync_metadata | Para futura sincronización |
| transaction_logs | id, transactionId, action, timestamp, snapshotJson |
| recurring_transactions | syncId, isDeleted |
| transaction_titles | title, categoryId, isExactMatch |

### 7.2 Migraciones

- **1→2:** Crear `transaction_logs`
- **2→3:** Crear `recurring_transactions`, `transaction_titles`

### 7.3 DatabasePrepopulate

- Inserta categorías por defecto
- Inserta monedas USD, VES, USDT, EUR

### 7.4 Entity → Model

Mappers en `EntityMappers.kt`: `toModel()`, `toEntity()` para cada entidad. Las entidades Room tienen campos adicionales `syncId`, `isDeleted` para sync futuro.

---

## 8. API de Tasas (core-network)

### 8.1 Base URL

`https://ve.dolarapi.com/v1`

### 8.2 Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | /dolares | Lista tasas USD |
| GET | /dolar-oficial | Tasa oficial (BCV) |
| GET | /dolar-paralelo | Tasa paralela |
| GET | /euros | Lista tasas EUR |
| GET | /historicos/dolares | Histórico USD |
| GET | /historicos/euros | Histórico EUR |

### 8.3 Modelo de Respuesta

```kotlin
@Serializable
data class DolarApiResponse(
    val moneda: String,
    val fuente: String,   // "oficial" -> BCV, "paralelo" -> USDT/EURI
    val nombre: String,
    val compra: Double? = null,
    val venta: Double? = null,
    val promedio: Double,
    val fechaActualizacion: String,
)

@Serializable
data class HistoricalRateResponse(
    val fuente: String,
    val compra: Double? = null,
    val venta: Double? = null,
    val promedio: Double,
    val fecha: String,
    val moneda: String? = null,
)
```

### 8.4 Mapeo fuente → ExchangeRateSource

- USD: `oficial` → BCV, `paralelo` → USDT
- EUR: `oficial` → BCV, `paralelo` → EURI

### 8.5 Caché

TTL: 4 horas. Si `(now - fetchedAt) < 4h` y ya hay datos del día, no se vuelve a llamar.

### 8.6 Cliente Ktor

- ContentNegotiation con kotlinx.serialization
- Logging BODY
- connectTimeout/socketTimeout: 15s
- Engine: Android

---

## 9. Capa de Dominio (core-domain)

### 9.1 Repositorios (interfaces)

- `TransactionRepository`
- `AccountRepository`
- `CategoryRepository`
- `ExchangeRateRepository`
- `DebtRepository`
- `BudgetRepository`
- `UserPreferencesRepository`
- `TransactionLogRepository`
- `RecurringTransactionRepository`
- `TransactionTitleRepository`
- `SavingsGoalRepository`

### 9.2 Casos de Uso Principales

- `GetGlobalBalanceUseCase`
- `GetTransactionsUseCase`
- `RecordTransactionUseCase` (incluye goalId opcional para contribución a objetivo)
- `RecordTransferUseCase`
- `GetAccountsUseCase`
- `GetCategoriesUseCase` (incluye jerarquía subcategorías)
- `GetExchangeRateUseCase`
- `ResolveExchangeRateUseCase` (conversión entre cualquier par vía VES)
- `RefreshExchangeRatesUseCase`
- `ExportTransactionsCsvUseCase`
- `ImportTransactionsCsvUseCase`
- `SuggestCategoryByTitleUseCase`
- `GetActiveGoalsUseCase`
- `ProcessDueRecurringTransactionsUseCase`

### 9.3 ResolveExchangeRateUseCase / CurrencyConverter

Lógica clave del Motor de Conversión:
- **Prioridad 1:** Buscar tasa MANUAL o directa entre las dos monedas (Ej: cambio en efectivo USD a USDT con comisión implícita).
- **Prioridad 2 (Arbitraje / Triangulación):** Si no hay tasa directa entre dos monedas extranjeras (Ej: USDT a EUR), se convierte primero a VES usando la tasa Paralelo de USDT, y luego ese resultado en VES se pasa a EUR usando la tasa BCV oficial.
- El objeto `Currencies` define la fuente implícita (oficial o paralelo) que le corresponde a cada moneda por defecto.

---

## 10. DataStore (core-datastore)

Preferencias: `user_preferences`

Keys: `display_currency`, `preferred_rate_source`, `theme_mode`, `use_dynamic_color`, `has_completed_onboarding`

---

## 11. Sistema de Diseño (core-designsystem)

### 11.1 Colores

- Brand: Purple #8A2BE2, Orange #FF6B00, Lavender #CA98FF
- Accent: NeonGreen #00FF66, Cyan #00D4FF, Gold #F0B90B
- Light/Dark schemes M3 completos
- Semantic: IncomeGreen, ExpenseRed, TransferBlue

### 11.2 CeroFiaoColorTokens

Tokens extendidos: bg, bgSecondary, surface, surfaceBorder, text, textSecondary, navBg, divider, inputBg, success, danger, chartLabel, etc. Versiones Dark y Light.

### 11.3 Gradientes

- `BrandGradient`: Purple → Orange
- `SuccessGradient`, `DangerGradient`

### 11.4 CeroFiaoShapes

CardRadius 24dp, SmallCardRadius 16dp, ButtonRadius 28dp, ChipRadius 20dp, BottomSheetRadius 32dp, IconRadius 14dp

### 11.5 Iconos

- **Lucide (ImageVector):** Navegación, acciones, tipos de transacción
- **OneUI (@DrawableRes):** Iconos de categoría
- `CeroFiaoIcons.categoryIconMap`: mapeo nombre → drawable
- `legacyIconNameMap`: Material icons antiguos → OneUI (DirectionsCar→Car, etc.)
- `getCategoryIconRes(iconName)`: resuelve con fallback a ic_oui_apps

---

## 12. Navegación

### 12.1 Rutas Type-Safe (@Serializable)

- `DashboardRoute`, `TransactionListRoute`, `TransactionEntryRoute(transactionId?)`, `TransactionDetailRoute(transactionId)`
- `TransferRoute`, `TransactionActivityRoute`, `RecurringListRoute`, `RecurringFormRoute`
- `DebtListRoute`, `AddDebtRoute(debtId?)`, `DebtDetailRoute(debtId)`
- `MoreRoute`
- `AccountListRoute`, `AccountDetailRoute(accountId)`, `AddAccountRoute`
- `CategoryListRoute`, `AddEditCategoryRoute(categoryId?)`
- `ExchangeRateRoute`
- `SettingsRoute`, `CsvExportRoute`, `AssociatedTitlesRoute`
- `OnboardingRoute`
- `BudgetListRoute`, `AddBudgetRoute(budgetId?)`, `AnalyticsRoute`, `AlcanciaRoute`
- `GoalsRoute`, `AddEditGoalRoute(goalId?)`, `GoalDetailRoute(goalId)`
- `BillSplitterRoute`

### 12.2 Tabs Principales (Bottom Bar)

1. **Inicio** (DashboardRoute)
2. **Movimientos** (TransactionListRoute)
3. **CeroFiao** (DebtListRoute)
4. **Más** (MoreRoute)

### 12.3 Patrón de Navegación por Feature

Cada feature expone:
- `*Screen(..., modifier)` composable
- `navigateTo*()` extension en NavController
- `*Screen(...)` en NavHost con `composable<Route>`

---

## 13. Pantallas y Flujos

### 13.1 Onboarding

- Pantalla inicial si `!hasCompletedOnboarding`
- Crear al menos una cuenta, elegir moneda de display
- Al completar: `setOnboardingCompleted()`, navegar a Dashboard

### 13.2 Dashboard

- Header con balance total (toggle visibilidad)
- Banners de tasas: BCV + USDT (Bs/USD), BCV + EURI (Bs/EUR)
- Desglose por moneda
- Resumen mensual (ingresos vs gastos)
- Top categorías del mes
- Últimas transacciones
- Pull-to-refresh para tasas
- FAB o acceso para agregar transacción

### 13.3 Transacciones

- Lista con swipe-to-delete
- Filtros por fecha, cuenta, categoría
- Entrada: monto, moneda, cuenta, categoría, fecha, nota; selector de objetivo opcional
- Teclado: `KeyboardType.Decimal` (no numpad custom)
- Sugerencia de categoría por título (Associated Titles) con debounce 300ms
- Transferencia: cuenta origen, destino, monto, comisión opcional
- Recurrentes: formulario con RecurrenceType, periodLength, startDate, endDate

### 13.4 Deudas

- Lista: debo / me deben
- Detalle: pagos, registrar pago, liquidar

### 13.5 Más (MoreRoute)

- Accesos: Cuentas, Analíticas, Alcancía, Configuración, Bill Splitter

### 13.6 Settings

- Tema (Light/Dark/System), colores dinámicos
- Moneda de display
- Fuente de tasa preferida
- Categorías, Tasas, Presupuestos, Deudas
- Exportar CSV, Importar CSV
- Transacciones recurrentes
- Títulos asociados
- Objetivos de ahorro

### 13.7 Bill Splitter

- Monto total + moneda
- Participantes con porcentaje o monto fijo
- Resultado en todas las monedas (USD, VES, USDT)

---

## 14. Widget (Glance)

- 2x2: balance en moneda de display + tasa BCV
- Actualización automática (WorkManager o Glance)
- `CeroFiaoWidget`, `CeroFiaoWidgetReceiver`

---

## 15. Shortcuts

`res/xml/shortcuts.xml`:

- `add_transaction` → `ACTION_ADD_TRANSACTION`
- `view_balance` → `ACTION_VIEW_BALANCE`
- `add_debt` → `ACTION_ADD_DEBT`

MainActivity en `onCreate` lee `intent?.action`, mapea a `ShortcutAction`, navega según acción.

---

## 16. CSV Export/Import

**Export:** columnas: fecha, descripcion, monto, moneda, monto_ves (frozenVesAmount), tasa_aplicada (appliedExchangeRate), categoria, cuenta, tipo, nota

**Import:** mapeo de columnas, parser OpenCSV, charset detection opcional.

---

## 17. Transaction Activity Log

- Tabla `transaction_logs`: CREATED, EDITED, DELETED
- Snapshot JSON del estado anterior
- Pantalla para ver últimas 50 acciones
- Restaurar transacciones eliminadas (soft-delete)

---

## 18. Orden de Implementación Sugerido

1. **Estructura base**
   - Gradle + build-logic + settings.gradle.kts
   - Módulos core (model, common) sin Android
   - Convention plugins

2. **core-database**
   - Entidades, DAOs, migraciones, prepopulate
   - DatabaseModule (Hilt)

3. **core-datastore**
   - UserPreferencesDataStore

4. **core-network**
   - Ktor, DolarApiDataSource, modelos
   - NetworkModule

5. **core-designsystem**
   - Colores, tema, CeroFiaoTokens, iconos
   - CeroFiaoTheme

6. **core-domain**
   - Interfaces de repositorios
   - Casos de uso básicos

7. **core-data**
   - Repositorios
   - DataModule

8. **core-ui**
   - MoneyText, EmptyState, GlassCard, etc.
   - CeroFiaoTopBar, CeroFiaoFloatingNavBar

9. **feature-onboarding**

10. **feature-dashboard**

11. **feature-transactions** (lista, entrada, detalle, transferencia)

12. **feature-accounts**

13. **feature-categories**

14. **feature-exchange-rates**

15. **feature-settings**

16. **feature-debt**, **feature-budget**, **feature-goals**

17. **Recurrentes, TransactionLog, Associated Titles**

18. **CSV, Bill Splitter, Widget, Shortcuts**

---

## 19. Patrones Clave

- **State:** `StateFlow` + `collectAsStateWithLifecycle()`
- **Async:** `suspend` + `Flow` en repositorios
- **Smart casts:** Usar variables locales para propiedades nullable cross-module
- **Swipe-to-delete:** En listas de transacciones, presupuestos, deudas, categorías
- **Room como única fuente de verdad** (offline-first)
- **Paquetes:** `com.schwarckdev.cerofiao.*` (lowercase)
- **App namespace:** `com.SchwarckDev.CeroFiao` (legacy, solo app module)

---

## 20. Dependencias Críticas (libs.versions.toml)

```
agp = "9.1.0"
kotlin = "2.2.10"
ksp = "2.2.10-2.0.2"
room = "2.7.1"
ktor = "3.1.1"
hilt = "2.59.2"
navigationCompose = "2.8.5"
composeBom = "2024.12.01"
glance = "1.1.1"
workManager = "2.10.1"
vico = "2.1.0"
opencsv = "5.9"
lucideIcons = "1.0.0"
oneuiIcons = "1.1.0"
```

---

## 21. Comandos de Build

```bash
# Windows (PowerShell)
$env:JAVA_HOME = "C:/Program Files/Android/Android Studio/jbr"
./gradlew assembleDebug

# Linux/macOS
JAVA_HOME=".../jbr" ./gradlew assembleDebug
```

---

*Documento generado para facilitar la replicación de CeroFiao por otra IA. Incluir `local.properties` con `sdk.dir` en worktrees.*
