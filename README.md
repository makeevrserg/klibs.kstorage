[![version](https://img.shields.io/maven-central/v/ru.astrainteractive.klibs/kstorage?style=flat-square)](https://github.com/makeevrserg/kstorage)
[![kotlin_version](https://img.shields.io/badge/kotlin-2.0.0-blueviolet?style=flat-square)](https://github.com/makeevrserg/kstorage)

## KStorage

KStorage is a lightweight Kotlin Multiplatform library that provides a unified and type-safe interface for key-value
storage across different platforms. It simplifies the process of storing and retrieving data by abstracting the
underlying storage mechanisms.

## ✨ Features

- Multiplatform Support: Works across Android, iOS, Jvm and other Kotlin-supported platforms.
- Type-Safe API: Ensures compile-time type checking for stored values.
- Lightweight: Minimal overhead with a focus on simplicity and performance.
- Extensible: Easily integrate with various storage backends.

## 🚀 Installation

#### Version catalogs

```toml
[versions]
klibs-kstorage = "<latest-version>"

[libraries]
klibs-kstorage = { module = "ru.astrainteractive.klibs:kstorage", version.ref = "klibs-kstorage" }
```

#### Gradle

```kotlin
implementation("ru.astrainteractive.klibs:kstorage:<version>")
// or version catalogs
implementation(libs.klibs.kstorage)
```

## 🛠️ Basic Usage

### Blocking API

```kotlin
class SettingsApi(private val intSettingsMap: MutableMap<String, Int>) {
    /**
     * The intSettingsMap["INT_KEY"] may return null
     * But since we have factory = { 0 }, the krate will be non-null!
     */
    val mutableKrate: MutableKrate<Int> = DefaultMutableKrate(
        factory = { 0 },
        loader = { intSettingsMap["INT_KEY"] },
        saver = { value -> intSettingsMap["INT_KEY"] = value }
    )

    // Want to cache your value?
    val cachedMutableKrate: CachedKrate<Int> = mutableKrate.asCachedMutableKrate()

    // Want even more and make it StateFlow?
    val stateFlowKrate: StateFlowMutableKrate<Int> = mutableKrate.asStateFlowMutableKrate()

    // Have a nullable krate somewhere, but also somewhere need it non-null?
    val nonNullableMutableKrate: MutableKrate<Int> = DefaultMutableKrate(
        factory = { null },
        loader = { intSettingsMap["INT_KEY"] },
        saver = { value ->
            if (value == null) intSettingsMap.remove("INT_KEY")
            else intSettingsMap["INT_KEY"] = value
        }
    ).withDefault { 102 }

    // Don't want to call krate.cachedValue every time? Use delegates!
    val cachedKrateValue by cachedMutableKrate
    val cachedStateFlowKrateValue by stateFlowKrate
}
```

### Suspend API

Want suspend-style api? It's almost the same!

```kotlin
class SuspendSettingsApi(private val intSettingsMap: MutableMap<String, Int>) {
    val suspendMutableKrate: SuspendMutableKrate<Int> = DefaultSuspendMutableKrate(
        factory = { 0 },
        loader = { intSettingsMap["INT_KEY"] },
        saver = { value -> intSettingsMap["INT_KEY"] = value }
    )

    val stateFlowSuspendMutableKrate: StateFlowSuspendMutableKrate<Int> = DefaultStateFlowSuspendMutableKrate(
        factory = { 0 },
        loader = { intSettingsMap["INT_KEY"] },
        saver = { value -> intSettingsMap["INT_KEY"] = value }
    )

    val nonNullableStateFlowSuspendMutableKrate = DefaultStateFlowSuspendMutableKrate(
        factory = { null },
        loader = { intSettingsMap["INT_KEY"] },
        saver = { value ->
            if (value == null) intSettingsMap.remove("INT_KEY")
            else intSettingsMap["INT_KEY"] = value
        }
    ).withDefault { 102 }
}
```

### Flow API

Want to use Jetpack DataStore? There's Flow API for your needs!

You can even integrate it with ROOM or on-disk files if you want!

```kotlin
class FlowApi {
    private val key = intPreferencesKey("KEY_2")

    // Create Jetpack DataStore instance
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath {
        val path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "$key.preferences_pb"
        val file = path.toFile()
        if (file.exists()) file.delete()
        path
    }

    // Create your krate
    val flowKrate = DefaultFlowMutableKrate(
        factory = { 10 },
        loader = { dataStore.data.map { it[key] } },
        saver = { value ->
            dataStore.edit { preferences ->
                preferences[key] = value
            }
        }
    )

    // Make it StateFlow if you need
    val stateFlowValue = flowKrate.stateFlow(GlobalScope)
}
```

## 🧪 Advanced usage

### 🏷️ Class-based Krate Example

```kotlin
class IntKrate(
    key: String,
    settingsMap: MutableMap<String, Int>
) : MutableKrate<Int?> by DefaultMutableKrate(
    factory = { null },
    loader = { settingsMap[key] },
    saver = { value -> settingsMap[key] = value }
)
```

### 🧱 Custom Data Type Example

```kotlin
data class CustomClass(val number: Int)

class CustomClassKrate(
    key: String,
    settingsMap: MutableMap<String, Int>
) : MutableKrate<CustomClass> by DefaultMutableKrate(
    factory = { CustomClass(0) },
    loader = {
        val intValue = settingsMap[key]
        if (intValue != null) CustomClass(intValue)
        else null
    },
    saver = { value -> settings[key] = value.number }
)
```

### 🔄 Dynamic Keys

```kotlin
fun krateForUser(userId: String): MutableKrate<Int> {
    return DefaultMutableKrate(
        factory = { 0 },
        loader = { settings[userId] ?: 0 },
        saver = { value -> settings[key] = value }
    )
}
```
