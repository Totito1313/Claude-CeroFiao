# CeroFiao - Project Conventions

## Project Overview
CeroFiao is a multi-currency expense tracking Android app for Venezuela. Offline-first with Supabase cloud sync planned for Phase 2.

## Architecture
- **Pattern:** Clean Architecture + MVVM
- **Modules:** Multi-module with convention plugins (`build-logic/convention/`)
- **Layers:** Presentation (Compose) → Domain (Use Cases) → Data (Repositories) → Database (Room)

## Module Structure
- `core-model` - Domain entities (no Android deps)
- `core-common` - Utilities, extensions
- `core-designsystem` - Theme, typography, colors (light + dark)
- `core-database` - Room DB, DAOs, entities
- `core-datastore` - DataStore preferences
- `core-network` - Ktor client (dolarapi.com)
- `core-data` - Repository implementations
- `core-domain` - Use cases + repository interfaces
- `core-ui` - Shared Compose components
- `feature-*` - Feature modules (dashboard, transactions, accounts, etc.)

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

## Build System
- AGP 9.1.0, Compile SDK 36, Min SDK 34, Target SDK 36
- Java 11 compatibility
- Convention plugins: `cerofiao.android.application`, `cerofiao.android.library`, `cerofiao.android.feature`, `cerofiao.android.compose`, `cerofiao.android.hilt`, `cerofiao.android.room`
- Version catalog: `gradle/libs.versions.toml`

## Key Patterns
- Every transaction stores exchange rate snapshot (`exchangeRateToUsd`, `amountInUsd`)
- All entities have `syncId` + `isDeleted` for future Supabase sync
- Room is single source of truth (offline-first)
- Exchange rates cached with 4h TTL from dolarapi.com
- Currency math isolated in `MoneyCalculator` utility

## Commands
- Build: `./gradlew assembleDebug`
- Test: `./gradlew test`
- Lint: `./gradlew lint`

## GitHub
- Owner: Totito1313 (Alan Schwarck)
- Email: alanleones2013@gmail.com
