[![version](https://img.shields.io/maven-central/v/ru.astrainteractive.klibs/kstorage?style=flat-square)](https://github.com/makeevrserg/kstorage)
[![kotlin_version](https://img.shields.io/badge/kotlin-1.9.0-blueviolet?style=flat-square)](https://github.com/makeevrserg/kstorage)

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

See also [MobileX](https://github.com/makeevrserg/MobileX) as parent library for more useful kotlin
code

## Creating MutableStorageValue

```kotlin
class SettingsApi(private val settings: Settings) {
    val mutableStorageValue = MutableStorageValue(
        default = 0,
        loadSettingsValue = {
            settings["INT_KEY"]
        },
        saveSettingsValue = { value ->
            settings["INT_KEY"] = value
        }
    )
}
```

## Creating Custom MutableStorageValue

```kotlin
class SettingsApi(private val settings: Settings) {
    data class CustomClass(val customInt: Int)

    class CustomClassStorageValue(
        key: String,
        default: CustomClass
    ) : MutableStorageValue<CustomClass> by MutableStorageValue(
        default = default,
        loadSettingsValue = {
            settings[key]?.let(::CustomClass) ?: default
        },
        saveSettingsValue = { customClass ->
            settings[key] = customClass.customInt
        }
    )

    val customStorageValue = CustomClassStorageValue(key = "CUSTOM_KEY", default = CustomClass(100))
}
```

## Converting nullable into non-null

It also works with StateFlowMutableStorageValue

This allows you to create parsers **only** for nullable values. After you can easily convert it to
non-nullable by `withDefault` extension!

```kotlin
class SettingsApi(private val settings: Settings) {
    class IntMutableStorageValue(
        key: String
    ) : MutableStorageValue<Int?> by MutableStorageValue(
        default = null as Int?,
        loadSettingsValue = { settings[key] },
        saveSettingsValue = { integerValue: Int? ->
            settings[key] = integerValue
        }
    )

    val customStorageValue: MutableStorageValue<Int> = IntMutableStorageValue("int_value").withDefault(15)
}
```

That's it! As easy as it looks

## Storage Components

- `StorageValue` - parent interface of all storage values
- `MutableStorageValue` - parent interface for mutable storage values
- `StateFlowMutableStorageValue` - coroutines StateFlow implementation of MutableStorageValue
- `InMemoryStateFlowMutableStorageValue` - this interface using in-memory storage