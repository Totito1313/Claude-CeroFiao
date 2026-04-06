# SKILL: Logica Cambiaria y Triangulacion — CeroFiao

> Estandariza la conversion, registro historico y triangulacion de multiples monedas,
> previniendo errores de paridad (ej. USD vs USDT) en el contexto economico de Venezuela.
>
> **Tests de validacion:** `feature/feature-transactions/src/test/.../TriangulationTest.kt`

---

## 1. REGLA DE ORO: La Moneda Ancla es USD (no VES)

La moneda ancla para toda la contabilidad, logica interna y persistencia de datos en CeroFiao es el **Dolar Estadounidense (USD)**.

### Por que USD y no VES?

En un contexto de hiperinflacion, el VES pierde significado historico rapidamente. Un monto de `4,550 Bs` guardado hoy no dice nada dentro de 3 meses si el bolivar se devaluo 50%. En cambio, `$100 USD` sigue siendo interpretable en el tiempo.

**El VES es el INTERMEDIARIO de triangulacion, no la base de almacenamiento.**

### Fijacion Historica

Todo ingreso/gasto, sin importar la moneda de origen, DEBE convertirse a USD utilizando la tasa de cambio exacta de ese instante, y guardar:

- El monto original en su moneda (`amount`)
- La tasa usada para convertir a USD (`exchangeRateToUsd`)
- La fuente de la tasa (`exchangeRateSource`)
- El monto normalizado en USD (`amountInUsd`)

Ese `amountInUsd` es el **Valor Ancla Historico** inmutable en la base de datos.

---

## 2. DIFERENCIACION ESTRICTA DE ACTIVOS (Cero Asunciones 1:1)

Queda **PROHIBIDO** asumir una paridad 1:1 entre monedas fiat y sus contrapartes digitales/cripto. Cada moneda es un activo independiente con su propia tasa de cambio respecto al VES.

| Moneda | Codigo | Fuente de tasa | Tipo |
|--------|--------|----------------|------|
| Dolar Fiat | `USD` | BCV oficial | Fiat |
| Tether (Dolar Cripto) | `USDT` | Paralelo/P2P (fuente `USDT`) | Cripto |
| Euro Fiat | `EUR` | BCV oficial | Fiat |
| Euro Informal | `EURI` | Paralelo digital (fuente `EURI`) | Cripto |
| Bolivar | `VES` | 1:1 (es la moneda intermediaria) | Local |

**Consecuencia critica:** 100 USD **NO** compran 100 USDT. Tienen distinto poder adquisitivo en VES.

### Normalizacion de monedas para lookups (`Currencies.baseCurrency`)

```kotlin
// core-model/Currency.kt
fun baseCurrency(code: String): String = when (code) {
    "USDT" -> "USD"  // USDT usa los pares USD↔VES en la DB
    "EURI" -> "EUR"  // EURI usa los pares EUR↔VES en la DB
    else -> code
}

fun implicitSource(code: String): ExchangeRateSource? = when (code) {
    "USDT" -> ExchangeRateSource.USDT   // siempre usa la fuente paralela
    "EURI" -> ExchangeRateSource.EURI   // siempre usa la fuente paralela
    else -> null                         // usa la fuente preferida del usuario
}
```

**IMPORTANTE:** `baseCurrency("USDT") = "USD"` NO significa 1:1. Solo indica que los pares de tasa en la DB son `USD↔VES`. La diferenciacion ocurre por la **fuente** (BCV vs USDT).

---

## 3. ESTRUCTURA DE DATOS OBLIGATORIA (El Registro Historico)

Cada transaccion en la base de datos guarda estos campos para garantizar la integridad historica:

```kotlin
// core-model/Transaction.kt
data class Transaction(
    val id: String,
    val amount: Double,                              // Monto original (ej. 100.0)
    val currencyCode: String,                        // Moneda original (ej. "USD")
    val exchangeRateToUsd: Double,                   // Tasa snapshot al momento (ej. 1.0 para USD, 0.02 para VES)
    val exchangeRateSource: ExchangeRateSource?,     // BCV, USDT, EURI, MANUAL, etc.
    val amountInUsd: Double,                         // amount * exchangeRateToUsd = valor ancla
    val date: Long,                                  // Timestamp de la transaccion
    // ... otros campos
)
```

### Ejemplo de registro:

| Campo | Valor | Explicacion |
|-------|-------|-------------|
| `amount` | 100.00 | "Recibi 100 dolares" |
| `currencyCode` | "USD" | Moneda de la transaccion |
| `exchangeRateToUsd` | 1.0 | USD→USD es identidad |
| `exchangeRateSource` | BCV | Fuente usada |
| `amountInUsd` | 100.00 | Valor ancla: 100 * 1.0 |

Para una transaccion en bolivares:

| Campo | Valor | Explicacion |
|-------|-------|-------------|
| `amount` | 4,550.00 | "Recibi 4,550 Bs" |
| `currencyCode` | "VES" | Bolivares |
| `exchangeRateToUsd` | 0.02198 | Inverso de BCV: 1/45.50 |
| `exchangeRateSource` | BCV | Fuente usada |
| `amountInUsd` | 100.00 | 4550 * 0.02198 = ~100 USD |

**Visualizacion:** El frontend puede mostrar "Recibiste 4,550 Bs", pero los calculos de balances y triangulaciones siempre parten de `amountInUsd`.

---

## 4. ALMACENAMIENTO DE TASAS DE CAMBIO

Las tasas se almacenan en la tabla `exchange_rate` con clave compuesta:

```
PRIMARY KEY: (fromCurrency, toCurrency, date, source)
```

### Pares almacenados (bidireccional):

| Par | Fuente | Ejemplo de tasa | Significado |
|-----|--------|-----------------|-------------|
| `USD → VES` | BCV | 45.50 | 1 USD = 45.50 Bs (oficial) |
| `USD → VES` | USDT | 48.00 | 1 USD = 48.00 Bs (paralelo) |
| `VES → USD` | BCV | 0.02198 | 1 Bs = 0.02198 USD (inverso) |
| `VES → USD` | USDT | 0.02083 | 1 Bs = 0.02083 USD (inverso) |
| `EUR → VES` | BCV | 49.20 | 1 EUR = 49.20 Bs (oficial) |
| `EUR → VES` | EURI | 52.00 | 1 EUR = 52.00 Bs (paralelo) |
| `VES → EUR` | BCV | 0.02033 | Inverso |
| `VES → EUR` | EURI | 0.01923 | Inverso |

**NO existen** pares directos `USD → USDT`, `USD → EUR`, `EUR → EURI`. Estas conversiones se **triangulan** via VES.

---

## 5. ALGORITMO DE TRIANGULACION

Para convertir de una Moneda A a una Moneda B, siempre se pasa por VES como puente, usando las tasas del mismo instante temporal.

### Formula

```
Monto_VES = Monto_A * Tasa(A → VES, fuente_A)
Monto_B   = Monto_VES * Tasa(VES → B, fuente_B)
```

Equivalente: `rate(A→B) = Tasa(A→VES) * Tasa(VES→B)`

### Implementacion real (`ResolveExchangeRateUseCase`)

```kotlin
// core-domain/usecase/ResolveExchangeRateUseCase.kt
suspend fun resolve(from: String, to: String, preferredSource: ExchangeRateSource): RateResult {
    val baseFrom = Currencies.baseCurrency(from)   // USDT → "USD"
    val baseTo = Currencies.baseCurrency(to)       // EURI → "EUR"

    // Identidad
    if (from == to) return RateResult(1.0, preferredSource)

    // Fuentes: cada moneda usa su fuente implicita, o la preferida del usuario
    val sourceFrom = Currencies.implicitSource(from) ?: preferredSource
    val sourceTo = Currencies.implicitSource(to) ?: preferredSource

    // Detectar conversion entre pares con misma base (USD↔USDT, EUR↔EURI)
    val isParityLoss = baseFrom == baseTo && from != to

    // Intentar lookup directo (solo si NO es parity loss)
    val direct = repo.getLatestRateBySource(baseFrom, baseTo, directSource)
        ?: repo.getLatestRate(baseFrom, baseTo)
    if (direct != null && !isParityLoss) return RateResult(direct.rate, direct.source)

    // Cross-rate via VES: cada pata usa la fuente de su moneda
    val toVes = repo.getLatestRateBySource(baseFrom, "VES", sourceFrom)?.rate
    val fromVes = repo.getLatestRateBySource("VES", baseTo, sourceTo)?.rate

    if (toVes != null && fromVes != null) {
        return RateResult(MoneyCalculator.convert(toVes, fromVes), sourceFrom, isParityLoss, toVes)
    }

    return RateResult(1.0, ExchangeRateSource.MANUAL) // fallback
}
```

### Ejemplo concreto: 100 USD → USDT

```
from = "USD", to = "USDT", preferredSource = BCV

1. baseCurrency("USD")  = "USD"
   baseCurrency("USDT") = "USD"     ← misma base!
   isParityLoss = true              ← NO es 1:1, hay perdida cambiaria

2. Skip direct lookup (isParityLoss = true)

3. Cross-rate via VES:
   sourceFrom = implicitSource("USD")  = null → BCV (preferida)
   sourceTo   = implicitSource("USDT") = USDT (implicita)

   toVes  = getRate("USD", "VES", BCV)  = 45.50 Bs/$
   fromVes = getRate("VES", "USD", USDT) = 1/48.00 = 0.02083 $/Bs

   rate = 45.50 * 0.02083 = 0.9479

4. Resultado: 100 USD * 0.9479 = 94.79 USDT
   → 100 dolares compran solo 94.79 USDT por la diferencia BCV/paralelo
```

---

## 6. REGLAS DE DISPLAY (Pantalla de Historial)

Cuando el usuario cambia la moneda de visualizacion en el hero del historial:

```kotlin
// TransactionListViewModel.kt
fun setDisplayCurrency(code: String) {
    viewModelScope.launch {
        if (code == "USD") {
            // Identidad, no hay conversion
            _displayCurrency.update { DisplayCurrencySettings("USD", "USD", "$", "USD", 1.0) }
            return@launch
        }
        val prefs = userPreferencesRepository.userPreferences.first()
        val result = resolveExchangeRate.resolve("USD", code, prefs.preferredRateSource)
        // result.rate contiene la tasa triangulada correcta
        // ...
    }
}
```

### Tabla de resultados esperados (con tasas ejemplo)

| Seleccion | Tasa `resolve("USD", X)` | 100 USD se muestra como | Formato |
|-----------|--------------------------|-------------------------|---------|
| USD | 1.0 | $100.00 | USD |
| Bs (VES) | 45.50 (BCV) | 4.550,00 Bs | VES |
| USDT | 0.9479 (triangulado) | 94.79 USDT | USDT |
| EUR | 0.9248 (triangulado) | 92.48 EUR | EUR |
| EURI | 0.8750 (triangulado) | 87.50 EURI | EURI |

**Todos los valores son DIFERENTES.** Si dos muestran lo mismo, hay un bug.

---

## 7. INSTRUCCIONES DIRECTAS PARA LA IA

Al leer este documento, debes:

1. **Jamas** tratar USD y USDT (o EUR y EURI) como alias de la misma variable. Son activos independientes con tasas distintas.

2. **Siempre** triangular via VES cuando no exista un par directo en la base de datos. La formula es: `rate = Tasa(A→VES, fuente_A) * Tasa(VES→B, fuente_B)`.

3. **Siempre** usar `ResolveExchangeRateUseCase` para conversiones — nunca hacer lookups crudos al repositorio de tasas, a menos que necesites una fuente especifica que el resolver no maneje.

4. **Siempre** guardar la fuente (`exchangeRateSource`) junto con la tasa en cada transaccion. Sin la fuente, una auditoria de triangulacion es imposible.

5. **Respetar** las fuentes implicitas: `USDT` siempre usa fuente `ExchangeRateSource.USDT`, `EURI` siempre usa fuente `ExchangeRateSource.EURI`. Las demas monedas usan la fuente preferida del usuario.

6. **Nunca** usar VES como moneda base de almacenamiento. Usar `amountInUsd` como ancla historica. VES solo es intermediario de triangulacion.

7. Al agregar nuevas monedas, asegurar que existan:
   - Un par `X → VES` con su fuente en la tabla de tasas
   - El inverso `VES → X` calculado automaticamente
   - Un entry en `Currencies.baseCurrency()` si es derivada (como USDT de USD)
   - Un entry en `Currencies.implicitSource()` si tiene fuente propia

---

## 8. TESTS DE VALIDACION

El archivo `TriangulationTest.kt` contiene 17 tests que validan toda la logica:

**Ubicacion:** `feature/feature-transactions/src/test/kotlin/.../TriangulationTest.kt`

**Ejecutar:** `./gradlew :feature:feature-transactions:testDebugUnitTest`

### Cobertura de tests:

| Categoria | Tests | Que valida |
|-----------|-------|-----------|
| Identidad | 2 | USD→USD = 1.0, VES→VES = 1.0 |
| Tasa directa | 3 | BCV y USDT retornan tasas distintas, premium paralelo existe |
| Triangulacion core | 5 | USD↔USDT no es 1:1, EUR↔EURI no es 1:1, cross-currency via VES |
| Display currency | 2 | Las 5 monedas producen valores distintos, fuente preferida afecta resultado |
| Roundtrip | 2 | USD→VES→USD = original, EUR→VES→EUR = original (sin ganancia fantasma) |
| Ejemplo skill | 1 | 100 USD = ~94.79 USDT (validacion directa del documento) |
| Edge cases | 2 | Zero y negativos se propagan correctamente |

### Tasas usadas en los tests:

```kotlin
USD→VES BCV:   45.50 Bs/$   (oficial)
USD→VES USDT:  48.00 Bs/$   (paralelo, ~5.5% premium)
EUR→VES BCV:   49.20 Bs/EUR (oficial)
EUR→VES EURI:  52.00 Bs/EUR (paralelo, ~5.7% premium)
```

Si estos tests fallan despues de un cambio, la triangulacion esta rota.

---

## 9. ERRORES COMUNES A EVITAR

| Error | Por que es incorrecto | Solucion |
|-------|----------------------|----------|
| `getExchangeRateUseCase("USD", "USDT")` | No existe par USD→USDT en DB, retorna null → 1.0 | Usar `resolveExchangeRate.resolve("USD", "USDT", pref)` |
| `exchangeRateRepository.getLatestRate("USD", "EUR")` | No existe par directo, retorna null | Usar el resolver que triangula via VES |
| Mostrar USDT como "Bs. al precio USDT" | Confunde la moneda con la fuente | USDT es un activo, no una tasa. Mostrar en unidades USDT |
| Guardar `monto_base_ves` como ancla | VES pierde significado historico en hiperinflacion | Guardar `amountInUsd` como ancla |
| Asumir que cambiar fuente preferida no cambia VES | BCV ≠ USDT, la conversion a Bs depende de la fuente | Respetar la fuente al calcular |
