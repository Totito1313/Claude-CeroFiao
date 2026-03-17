# CeroFiao - Project Conventions

## Project Overview
CeroFiao is a multi-currency expense tracking Android app for Venezuela. Supports USD, VES, USDT, and EUR with real-time exchange rates (BCV oficial, USDT mercado, EURI mercado). Offline-first with Supabase cloud sync planned for Phase 2.

## Architecture
- **Pattern:** Clean Architecture + MVVM
- **Modules:** Multi-module with convention plugins (`build-logic/convention/`)
- **Layers:** Presentation (Compose) → Domain (Use Cases) → Data (Repositories) → Database (Room)

## Module Structure
### Core Modules
- `core-model` - Domain entities (no Android deps): Transaction, Account, Category, Budget, Debt, DebtPayment, ExchangeRate, Currency, GlobalBalance
- `core-common` - Utilities: CurrencyFormatter, DateUtils, MoneyCalculator, UuidGenerator, ExpressionEvaluator
- `core-designsystem` - Theme, typography, colors (light + dark), CeroFiaoIcons
- `core-database` - Room DB, DAOs, entities, EntityMappers, DatabasePrepopulate
- `core-datastore` - DataStore preferences (theme, display currency, preferred rate source)
- `core-network` - Ktor client (dolarapi.com) for USD and EUR rates
- `core-data` - Repository implementations + DataModule (Hilt bindings)
- `core-domain` - Use cases + repository interfaces
- `core-ui` - Shared Compose components (MoneyText, EmptyState)

### Feature Modules
- `feature-dashboard` - Main screen: balance, currency breakdown, exchange rates (USD + EUR), monthly summary, top categories, recent transactions
- `feature-transactions` - Transaction list, entry (add/edit with calculator numpad), detail view
- `feature-accounts` - Account list, add account, account detail
- `feature-categories` - Category management with icon picker
- `feature-exchange-rates` - Full exchange rate screen with BCV, USDT, EURI rates and historical charts
- `feature-settings` - Theme, display currency, rate source, navigation to management screens
- `feature-onboarding` - Initial setup (accounts, display currency)
- `feature-budget` - Budget CRUD with progress tracking per category
- `feature-debt` - Debt tracking (I owe / they owe), payment registration, settle

## Coding Conventions
- **Package naming:** `com.schwarckdev.cerofiao.*` (lowercase)
- **App namespace:** `com.SchwarckDev.CeroFiao` (legacy, app module only)
- **Language:** Kotlin (2.2.10)
- **UI:** Jetpack Compose + Material 3
- **DI:** Hilt with KSP (not kapt)
- **Async:** Kotlin Coroutines + Flow
- **State:** StateFlow + collectAsStateWithLifecycle()
- **Navigation:** Jetpack Navigation Compose with type-safe routes (@Serializable)
- **Database:** Room with Flow-based DAOs
- **Smart casts:** Use local variables for nullable properties from cross-module data classes (e.g., `val note = debt.note; if (!note.isNullOrBlank())`)

## Currencies & Exchange Rates
- **Supported currencies:** USD, VES, USDT, EUR
- **Exchange rate sources (ExchangeRateSource enum):** BCV, PARALLEL, USDT, EURI, BINANCE_P2P, MANUAL
- **API:** dolarapi.com — USD rates (`/v1/dolares`), EUR rates (`/v1/euros`), historical data
- **Source mapping:** USD: "oficial" → BCV, "paralelo" → USDT; EUR: "oficial" → BCV, "paralelo" → EURI
- **Dashboard shows:** BCV + USDT (Bs/USD) and BCV + EURI (Bs/EUR) banners
- **Cache:** 4h TTL for exchange rates

## Build System
- AGP 9.1.0, Compile SDK 36, Min SDK 34, Target SDK 36
- Java 21 compatibility (Android Studio bundled JBR)
- JAVA_HOME: `C:/Program Files/Android/Android Studio/jbr`
- Convention plugins: `cerofiao.android.application`, `cerofiao.android.library`, `cerofiao.android.feature`, `cerofiao.android.compose`, `cerofiao.android.hilt`, `cerofiao.android.room`
- Version catalog: `gradle/libs.versions.toml`
- `cerofiao.android.feature` auto-includes: core-model, core-domain, core-ui, core-designsystem, core-common, navigation, hilt, lifecycle, serialization

## Key Patterns
- Every transaction stores exchange rate snapshot (`exchangeRateToUsd`, `amountInUsd`)
- All entities have `syncId` + `isDeleted` for future Supabase sync (soft delete)
- Room is single source of truth (offline-first)
- Currency math isolated in `MoneyCalculator` utility
- `ExpressionEvaluator` for calculator-style numpad input in transaction entry
- Swipe-to-delete pattern on list screens (transactions, budgets, debts)

## Commands
- Build: `JAVA_HOME="C:/Program Files/Android/Android Studio/jbr" ./gradlew assembleDebug`
- Test: `JAVA_HOME="C:/Program Files/Android/Android Studio/jbr" ./gradlew test`
- Lint: `JAVA_HOME="C:/Program Files/Android/Android Studio/jbr" ./gradlew lint`
- Note: `local.properties` must exist in worktrees (copy from main repo)

## GitHub
- Repo: Totito1313/Claude-CeroFiao
- Owner: Totito1313 (Alan Schwarck)
- Email: alanleones2013@gmail.com
- Default branch: master
