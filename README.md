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

## Creating MutableStorageValue

```kotlin
class SettingsApi(private val settings: Settings) {
    val mutableStorageValue = DefaultMutableKrate(
        factory = { 0 },
        loader = { settings["INT_KEY"] },
        saver = { value -> settings["INT_KEY"] = value }
    )
}
```

## Creating Custom MutableStorageValue

```kotlin
class SettingsApi(private val settings: Settings) {
    data class CustomClass(val customInt: Int)

    private class CustomClassStorageValue(
        key: String,
        factory: DefaultValueFactory<CustomClass>
    ) : MutableKrate<CustomClass> by DefaultMutableKrate(
        factory = factory,
        loader = { settings[key]?.let(::CustomClass) },
        saver = { customClass -> settings[key] = customClass.customInt }
    )

    val customStorageValue: MutableKrate<CustomClass> = CustomClassStorageValue(
        key = "CUSTOM_KEY",
        factory = { CustomClass(100) }
    )
}
```

## Converting nullable into non-null

It also works with StateFlowMutableStorageValue

This allows you to create parsers **only** for nullable values. After you can easily convert it to
non-nullable by `withDefault` extension!

```kotlin
class SettingsApi(private val settings: Settings) {
    data class CustomClass(val customInt: Int)

    private class CustomClassStorageValue(
        key: String,
        factory: DefaultValueFactory<CustomClass?>
    ) : MutableKrate<CustomClass?> by DefaultMutableKrate(
        factory = factory,
        loader = { settings[key]?.let(::CustomClass) },
        saver = { customClass -> settings[key] = customClass?.customInt }
    )

    val customStorageValue: MutableKrate<CustomClass> = CustomClassStorageValue(
        key = "CUSTOM_KEY",
    ).withDefault(15)
}
```

## Don't want to use blocking style? Use it with datastore then or with any suspend library

```kotlin
class SettingsApi(private val dataStore: DataStore<Preferences>) {

    internal class DataStoreFlowMutableKrate<T>(
        key: Preferences.Key<T>,
        dataStore: DataStore<Preferences>,
        factory: SuspendValueFactory<T>,
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

    val intKrate = DataStoreFlowMutableKrate<Int?>(
        key = intPreferencesKey("some_int_key"),
        dataStore = dataStore,
        factory = { null }
    ).withDefault(12)
}
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