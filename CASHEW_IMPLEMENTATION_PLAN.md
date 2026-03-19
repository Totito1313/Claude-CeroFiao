# Plan de Implementación — Inspirado en Cashew

> Referencia: [jameskokoska/Cashew](https://github.com/jameskokoska/Cashew)
> Fecha de análisis: 2026-03-17
> Versión analizada: Cashew 5.4.3+416

---

## Contexto

Se realizó un análisis comparativo del repositorio de **Cashew** — una app de finanzas personales en Flutter con 928+ commits y amplia base de usuarios — para identificar features y patrones de UX que aporten valor a **CeroFiao**.

### Diferenciadores de CeroFiao (ventajas que ya tenemos)
- Tasas venezolanas en tiempo real: BCV, USDT, EURI, Paralelo vía dolarapi.com
- Lógica de conversión multi-moneda venezolana (USD, VES, USDT, EUR)
- Snapshots de tasa en cada transacción (`exchangeRateToUsd`, `amountInUsd`)
- Arquitectura multi-módulo limpia y escalable (vs. monolito de Cashew)
- Type-safe navigation con @Serializable routes

---

## Resumen de Features a Implementar

| # | Feature | Prioridad | Esfuerzo | Sprint |
|---|---------|-----------|----------|--------|
| 1 | CSV Export | Alta | Bajo | 1 |
| 2 | Quick Actions (Home screen shortcuts) | Alta | Bajo | 1 |
| 3 | Transaction Activity Log | Alta | Bajo | 1 |
| 4 | Transacciones Recurrentes | Alta | Alto | 2 |
| 5 | Associated Titles (categorización inteligente) | Alta | Medio | 2 |
| 6 | Subcategorías | Media | Medio | 3 |
| 7 | Savings Goals / Objetivos | Media | Alto | 3 |
| 8 | Home Screen Widget (Glance API) | Media | Medio | 4 |
| 9 | Bill Splitter | Baja | Medio | 4 |
| 10 | Reemplazo total de Material UI (M3) por Componentes CeroFiao | Alta | Crítico | 7 |

---

## Sprint 1 — Quick Wins (Bajo esfuerzo, Alto impacto)

### 1. CSV Export / Import

**Referencia Cashew:**
- Archivo: `budget/lib/pages/csv_import_export.dart`
- Usa el paquete `csv: ^6.0.0`
- Soporta export por rango de fechas e import masivo
- Integra `flutter_charset_detector` para encodings

**Qué implementar en CeroFiao:**
- Pantalla `CsvExportScreen` dentro de `feature-settings`
- Export de transacciones a CSV con filtro de rango de fechas
- Import de CSV con mapeo de columnas (monto, fecha, categoría, cuenta, nota)
- Columnas de export: `fecha`, `descripcion`, `monto`, `moneda`, `monto_usd`, `tasa_usd`, `categoria`, `cuenta`, `tipo`, `nota`
- Incluir la tasa de cambio vigente al momento de la transacción (ventaja sobre Cashew)

**Módulos afectados:**
- `feature-settings` — nueva entrada en el menú + pantalla
- `core-data` — método `exportTransactionsToCsv(startDate, endDate)` en repositorio
- `core-domain` — use case `ExportTransactionsCsvUseCase`

**Dependencias a agregar:**
```toml
# gradle/libs.versions.toml
opencsv = "5.9"  # o equivalente compatible con Android
```

---

### 2. Quick Actions (Android Shortcuts)

**Referencia Cashew:**
- Usa el paquete `quick_actions: ^1.0.6` de Flutter
- Shortcuts definidos: "Agregar gasto", "Ver balance", "Escanear"

**Qué implementar en CeroFiao:**
- 3 shortcuts nativos en `app/src/main/res/xml/shortcuts.xml`
- **Agregar Transacción** → abre `feature-transactions` en modo "add"
- **Ver Balance** → abre `feature-dashboard`
- **Agregar Deuda** → abre `feature-debt` en modo "add"
- Deep link handling en `MainActivity`

**Archivos a crear/modificar:**
- `app/src/main/res/xml/shortcuts.xml` — definición de shortcuts
- `app/src/main/AndroidManifest.xml` — registrar shortcuts
- `app/src/main/java/.../MainActivity.kt` — intent handling

---

### 3. Transaction Activity Log

**Referencia Cashew:**
- Feature: "Transaction Activity" — lista de transacciones eliminadas/modificadas recientemente
- Permite recuperar transacciones borradas por error
- Muestra timestamp del borrado y datos originales

**Qué implementar en CeroFiao:**
- Aprovechar el campo `isDeleted` + `syncId` que ya existe en todas las entidades
- Nueva tabla `TransactionLog(id, transactionSyncId, action, timestamp, snapshotJson)`
- Pantalla en `feature-transactions` → "Actividad reciente"
- Mostrar las últimas 50 acciones: CREATED, EDITED, DELETED
- Opción "Restaurar" en transacciones eliminadas (soft-delete → isDeleted=false)

**Módulos afectados:**
- `core-database` — nueva entidad `TransactionLogEntity` + DAO
- `core-model` — `TransactionLog` data class
- `core-data` — lógica de logging automático al insertar/editar/eliminar
- `feature-transactions` — pantalla `TransactionActivityScreen`

---

## Sprint 2 — Features de Alto Valor

### 4. Transacciones Recurrentes

**Referencia Cashew:**
- Entidad: `Transaction` con campos `reoccurrence` y `periodLength`
- Tipos: DAILY, WEEKLY, MONTHLY, YEARLY con longitud de período configurable
- Vista especial "Upcoming transactions" con extrapolación mensual/anual
- Al llegar la fecha, genera automáticamente la transacción real

**Qué implementar en CeroFiao:**

**Nueva entidad en `core-model`:**
```kotlin
data class RecurringTransaction(
    val id: String,                        // syncId
    val title: String,
    val amount: Double,
    val currency: Currency,
    val categoryId: String,
    val accountId: String,
    val recurrence: RecurrenceType,        // DAILY, WEEKLY, MONTHLY, YEARLY
    val periodLength: Int,                 // cada N períodos
    val startDate: LocalDate,
    val endDate: LocalDate?,               // null = indefinido
    val nextDueDate: LocalDate,
    val isActive: Boolean,
    val note: String?,
    val syncId: String,
    val isDeleted: Boolean
)

enum class RecurrenceType { DAILY, WEEKLY, MONTHLY, YEARLY }
```

**Flujo de generación:**
- `WorkManager` job diario que revisa `nextDueDate <= today`
- Genera transacción real copiando datos + capturando tasa de cambio actual
- Actualiza `nextDueDate` al próximo período
- Notificación opcional al generar

**Módulos a crear/modificar:**
- `core-model` — `RecurringTransaction`, `RecurrenceType`
- `core-database` — `RecurringTransactionEntity`, `RecurringTransactionDao`
- `core-data` — `RecurringTransactionRepository`
- `core-domain` — use cases: `GetRecurringTransactionsUseCase`, `CreateRecurringTransactionUseCase`, `ProcessDueRecurringTransactionsUseCase`
- `feature-transactions` — UI para crear/editar recurrentes + lista "Próximas"
- `app` — `RecurringTransactionWorker` con WorkManager

**Ventaja sobre Cashew:** Al generar la transacción, CeroFiao captura automáticamente la tasa BCV/USDT vigente — Cashew no tiene esto.

---

### 5. Associated Titles (Categorización Inteligente)

**Referencia Cashew:**
- Tabla: `AssociatedTitle(id, title, categoryFk, isExactMatch)`
- Cuando el usuario escribe un nombre de transacción, busca coincidencias y sugiere categoría
- Aprende: al guardar una transacción, si el título es nuevo se ofrece guardarlo como asociación
- Pantalla de gestión de títulos asociados en Settings

**Qué implementar en CeroFiao:**

**Nueva entidad en `core-model`:**
```kotlin
data class TransactionTitle(
    val id: String,
    val title: String,           // ej: "Netflix"
    val categoryId: String,      // categoría sugerida
    val isExactMatch: Boolean,   // true = exacto, false = contiene
    val syncId: String,
    val isDeleted: Boolean
)
```

**Lógica de sugerencia:**
- En `AddTransactionScreen`, al escribir descripción buscar coincidencias (debounce 300ms)
- Mostrar chip/banner "¿Categoría: Entretenimiento?" con accept/dismiss
- Al guardar: si el título no tiene asociación, preguntar si desea guardarla

**Módulos afectados:**
- `core-model` — `TransactionTitle`
- `core-database` — `TransactionTitleEntity`, `TransactionTitleDao`
- `core-data` — `TransactionTitleRepository`
- `core-domain` — `SuggestCategoryByTitleUseCase`, `SaveTransactionTitleUseCase`
- `feature-transactions` — lógica de sugerencia en `AddTransactionViewModel`
- `feature-settings` — pantalla de gestión de títulos asociados

---

## Sprint 3 — Organización Avanzada

### 6. Subcategorías

**Referencia Cashew:**
- Campo `mainCategoryPk` en `Category` — referencia al padre (null = categoría raíz)
- UI: lista reordenable con expansión de subcategorías
- En transaction entry: selección jerárquica — primero categoría, luego subcategoría

**Qué implementar en CeroFiao:**

**Modificación a `Category` en `core-model`:**
```kotlin
data class Category(
    // ... campos existentes ...
    val parentCategoryId: String?,    // null = categoría raíz
    val sortOrder: Int                // para drag-and-drop
)
```

**Consideraciones de migración:**
- Room migration: agregar `parent_category_id` nullable + `sort_order`
- Las categorías existentes quedan con `parentCategoryId = null`
- Filtros en queries: cuando se filtra por categoría padre, incluir todas sus subcategorías

**Módulos afectados:**
- `core-model` — actualizar `Category`
- `core-database` — migración Room + actualizar queries del DAO
- `core-domain` — actualizar `GetCategoriesUseCase` para incluir jerarquía
- `feature-categories` — UI jerárquica con subcategorías expandibles
- `feature-transactions` — selector de categoría con dos niveles

---

### 7. Savings Goals / Objetivos

**Referencia Cashew:**
- Entidad: `Objective` — meta de ahorro o gasto vinculada a transacciones
- Tipos: `SAVINGS` (acumular hacia una meta) y `EXPENSE` (límite de gasto)
- Se vincula transacciones directamente al objetivo
- Progreso visual: barra de progreso hacia la meta
- Los débitos crean automáticamente un objetivo asociado para tracking

**Qué implementar en CeroFiao:**

**Nueva entidad en `core-model`:**
```kotlin
data class SavingsGoal(
    val id: String,
    val title: String,
    val targetAmount: Double,
    val currency: Currency,
    val currentAmount: Double,    // calculado de transacciones vinculadas
    val deadline: LocalDate?,
    val categoryId: String?,      // categoría asociada (opcional)
    val color: Int,
    val icon: String,
    val isCompleted: Boolean,
    val syncId: String,
    val isDeleted: Boolean
)
```

**Relación con transacciones:**
- Campo `goalId: String?` en `Transaction` (nullable — vinculación opcional)
- `currentAmount` calculado dinámicamente via query Room
- Notificación al alcanzar 100%

**Nuevo módulo:** `feature-goals`
- Lista de objetivos con progreso
- Pantalla de detalle con transacciones vinculadas
- Formulario de creación/edición
- Integración en `AddTransactionScreen` — opción "Vincular a objetivo"

---

## Sprint 4 — Integración con el Sistema

### 8. Home Screen Widget (Glance API)

**Referencia Cashew:**
- Usa el paquete `home_widget` de Flutter
- Muestra: balance total, gastos del mes, tasa de cambio

**Qué implementar en CeroFiao:**
- **Glance API** (Jetpack) — solución nativa Android moderna
- Nuevo módulo `widget` o implementación en `app`
- Widget 2x2: balance en moneda de display + tasa BCV del día
- Widget 4x2 (expandido): balance + resumen mensual + top categoría
- Actualización automática cada 4h (misma TTL que exchange rates)

**Módulos afectados:**
- `app` — `CeroFiaoWidget` con Glance + `CeroFiaoWidgetReceiver`
- `core-data` — `WidgetDataRepository` (acceso directo a Room + DataStore sin ViewModel)

---

### 9. Bill Splitter

**Referencia Cashew:**
- Pantalla dedicada para dividir gastos entre N personas
- Ingresa monto total + lista de personas + porcentajes o montos fijos
- Opcionalmente crea transacciones individuales para cada persona
- Historial de splits

**Qué implementar en CeroFiao:**
- Feature standalone `feature-billsplitter`
- Entrada: monto total + moneda + lista de participantes
- División: igualitaria, por porcentaje, o montos personalizados
- Resultado: muestra cuánto debe pagar cada uno con conversión VES/USD
- Opción de crear transacciones reales o solo como calculadora
- Ventaja sobre Cashew: muestra el equivalente en todas las monedas (USD, VES, USDT)

---

## Guía de Implementación — Principios

### Lo que NO replicamos de Cashew
- **In-App Purchases / Premium tier** — CeroFiao es gratuito por diseño
- **reCAPTCHA** — no tenemos auth cloud aún
- **37+ idiomas** — enfoque en español venezolano
- **Shared budgets** — complejidad alta, posponer hasta que cloud sync esté activo
- **Monolito** — mantener multi-módulo limpio

### Patrones de Cashew que sí adoptamos
- **Soft delete + Activity Log** — ya tenemos `isDeleted`, ahora logueamos acciones
- **Associated Titles** — sistema de aprendizaje simple pero muy efectivo en UX
- **Recurrentes con WorkManager** — patron robusto para background processing
- **Exportación de datos** — CSV como formato universal, no propietario

### Ventajas venezolanas que mantenemos y extendemos
- En CSV export: incluir columnas `tasa_bcv`, `tasa_usdt`, `tasa_euri` al momento de la transacción
- En recurrentes: capturar tasa vigente al generar cada instancia
- En Bill Splitter: mostrar equivalente en todas las monedas locales
- En Goals: soportar metas en cualquier moneda con conversión automática

---

## Checklist de Implementación

### Sprint 1 ✅ COMPLETADO
- [x] CSV Export — pantalla + use case + columnas venezolanas
- [x] CSV Import — parser + mapeo de columnas
- [x] Quick Actions — shortcuts.xml + manifest + intent handling
- [x] Transaction Activity Log — entidad + DAO + pantalla

### Sprint 2 ✅ COMPLETADO
- [x] RecurringTransaction — model + DB + repository
- [x] RecurringTransactionWorker — WorkManager job (pendiente — generación automática al vencer)
- [x] Feature UI — crear/editar recurrentes
- [x] Feature UI — lista "Próximas transacciones" (30 días)
- [x] TransactionTitle — model + DB + repository
- [x] Sugerencia en AddTransaction — debounce + chip UI
- [x] Gestión de títulos en Settings

### Sprint 3 — UI Overhaul ✅ COMPLETADO (nuevo sprint, no el original)
- [x] Icon system: Lucide (nav/actions) + OneUI (category icons) — reemplaza Material Icons
- [x] System keyboard: eliminar NumpadComponent, usar KeyboardType.Decimal
- [x] Currency independence: selector de moneda en transaction entry + recurring form
- [x] Multi-currency equivalents card en transaction entry
- [x] DatePicker en recurring form
- [x] Category CRUD: AddEditCategoryScreen con icon picker + color palette
- [x] Category summaries: totales mensuales por categoría en CategoryListScreen
- [x] Swipe-to-delete + tap-to-edit en CategoryListScreen

### Sprint 3 (original) — Organización Avanzada ✅ COMPLETADO
- [x] Category.parentCategoryId — Room migration (subcategorías)
- [x] UI jerárquica en feature-categories
- [x] Selector dos niveles en feature-transactions
- [x] SavingsGoal — model + DB + repository + feature-goals
- [x] Vinculación goal↔transaction en AddTransactionScreen

### Sprint 4 ✅ COMPLETADO
- [x] Glance Widget — balance + tasa BCV
- [x] Widget actualización automática
- [x] Bill Splitter — feature standalone con multi-moneda

### Sprint 7 — Eliminación de Material UI (M3)
- [ ] Construir componentes `core-ui`: `CeroFiaoButton`, `CeroFiaoTextField` y `CeroFiaoFAB`.
- [ ] Eliminar todo uso de `Button`/`TextButton`/`OutlinedButton` nativos de Material3 y usar `CeroFiaoButton`.
- [ ] Eliminar todo uso de `OutlinedTextField` / inputs nativos y usar `CeroFiaoTextField` en toda la app.
- [ ] Reemplazar todos los `FloatingActionButton` por `CeroFiaoFAB`.
- [ ] Reemplazar todos los `Card` restantes por `GlassCard` o superficies customizadas CeroFiao.

---

*Documento generado en base al análisis del repositorio Cashew (commit ~928, v5.4.3+416) realizado el 2026-03-17.*
