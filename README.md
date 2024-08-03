[![version](https://img.shields.io/maven-central/v/ru.astrainteractive.klibs/kstorage?style=flat-square)](https://github.com/makeevrserg/kstorage)
[![kotlin_version](https://img.shields.io/badge/kotlin-2.0.0-blueviolet?style=flat-square)](https://github.com/makeevrserg/kstorage)

## KStorage

KStorage is super lightweight Kotlin Multiplatform Storage wrapper library

## Installation

Gradle

```kotlin
implementation("ru.astrainteractive.klibs:kstorage:<version>")
```

Version catalogs

```toml
[versions]
klibs-kstorage = "<latest-version>"

[libraries]
klibs-kstorage = { module = "ru.astrainteractive.klibs:kstorage", version.ref = "klibs-kstorage" }
```

## Creating MutableKrate

```kotlin
class SettingsApi(private val settings: Settings) {
    // Create krate just in place
    val mutableKrate = DefaultMutableKrate(
        factory = { 0 },
        loader = { settings["INT_KEY"] },
        saver = { value -> settings["INT_KEY"] = value }
    )

    // Or create class
    class IntKrate(
        key: String,
        settings: Settings
    ) : MutableKrate<Int?> by DefaultMutableKrate<Int?>(
        factory = { null },
        saver = { value -> settings[key] = value },
        loader = { settings[key] }
    )

    // And register just like that
    val mutableKrate2 = IntKrate("KEY", settings)
}

fun callFunction(api: SettingsApi) {
    api.mutableKrate.loadAndGet()
    api.mutableKrate.save(12)
}
```

## Creating Custom MutableKrate

```kotlin
class SettingsApi(private val settings: Settings) {
    // Create custom class for your krate
    data class CustomClass(val customInt: Int)

    // Create custom type-safe parser for your krate
    private class CustomClassKrate(
        key: String,
        factory: ValueFactory<CustomClass>
    ) : MutableKrate<CustomClass> by DefaultMutableKrate(
        factory = factory,
        loader = { settings[key]?.let(::CustomClass) },
        saver = { customClass -> settings[key] = customClass.customInt }
    )

    // Register krate
    val customKrate: MutableKrate<CustomClass> = CustomClassKrate(
        key = "CUSTOM_KEY",
        factory = { CustomClass(100) }
    )
}
```

## Converting nullable into non-null

It also works with StateFlowMutableKrate

This allows you to create parsers **only** for nullable values. After you can easily convert it to
non-nullable by `withDefault` extension!

```kotlin
class SettingsApi(private val settings: Settings) {
    // Create custom class for your krate
    data class CustomClass(val customInt: Int)

    // Create custom nullable parser for your krate
    private class CustomClassKrate(
        key: String,
    ) : MutableKrate<CustomClass?> by DefaultMutableKrate(
        factory = { null },
        loader = { settings[key]?.let(::CustomClass) },
        saver = { customClass -> settings[key] = customClass?.customInt }
    )

    // Register krate with default parameter
    val customKrate: MutableKrate<CustomClass> = CustomClassKrate(
        key = "CUSTOM_KEY",
    ).withDefault { CustomClass(15) }
}
```

## Don't want to use blocking style? Use it with datastore then or with any suspend library

```kotlin
class SettingsApi(private val dataStore: DataStore<Preferences>) {

    // Wrap any library with custom implementation, DataStore for example
    internal class DataStoreFlowMutableKrate<T>(
        key: Preferences.Key<T>,
        dataStore: DataStore<Preferences>,
        factory: ValueFactory<T>,
    ) : FlowMutableKrate<T> by DefaultFlowMutableKrate(
        factory = factory,
        loader = { dataStore.data.map { it[key] } },
        saver = { value ->
            dataStore.edit { preferences ->
                if (value == null) preferences.remove(key)
                else preferences[key] = value
            }
        }
    )

    // Initialize krate value
    val intKrate = DataStoreFlowMutableKrate<Int?>(
        key = intPreferencesKey("some_int_key"),
        dataStore = dataStore,
        factory = { null }
    ).withDefault(12)
}

suspend fun callFunction(api: SettingsApi) {
    api.intKrate.getValue()
    api.intKrate.save(12)
}
```

## Extensions

```kotlin
val mutableKrate: MutableKrate<Int> = TODO()
val suspendMutableKrate: SuspendMutableKrate<Int> = TODO()
// Reset to default and get a new value
mutableKrate.resetAndGet()
suspendMutableKrate.resetAndGet()
// Update with a reference to current value
mutableKrate.update { value -> value + 1 }
suspendMutableKrate.update { value -> value + 1 }
// Update with a reference to current value and get 
mutableKrate.updateAndGet { value -> value + 1 }
suspendMutableKrate.updateAndGet { value -> value + 1 }
```

That's it! As easy as it looks

## Storage Components

#### Blocking

- `Krate` - Default read-only krate
- `StateFlowKrate` - Read-only krate with StateFlow
- `MutableKrate` - Mutable read-write krate
- `StateFlowMutableKrate` - Mutable read-write krate with StateFlow

#### Suspend

- `SuspendKrate` - Default suspend read-only krate
- `FlowKrate` - Read-only suspend krate with Flow
- `SuspendMutableKrate` - Mutable suspend read-write krate
- `FlowMutableKrate` - Mutable suspend read-write krate with Flow