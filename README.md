[![version](https://img.shields.io/maven-central/v/ru.astrainteractive.klibs/kstorage?style=flat-square)](https://github.com/makeevrserg/kstorage)
[![kotlin_version](https://img.shields.io/badge/kotlin-2.3.0-blueviolet?style=flat-square)](https://github.com/makeevrserg/kstorage)

## KStorage

KStorage is a lightweight Kotlin Multiplatform library that provides a unified and type-safe
interface for key-value
storage across different platforms. It simplifies the process of storing and retrieving data by
abstracting the
underlying storage mechanisms.

## ✨ Features

- **Multiplatform Support**: Works across Android, iOS, JVM, JS, Linux, macOS, Windows, tvOS,
  watchOS and WasmJS.
- **Type-Safe API**: Ensures compile-time type checking for stored values.
- **Thread-Safe by Default**: Every Krate is protected by a built-in platform-specific lock — no
  manual synchronization needed.
- **Reactive**: First-class `StateFlow` and `Flow` support for observing value changes.
- **Lightweight**: Minimal overhead with a focus on simplicity and performance.
- **Extensible**: Easily integrate with any storage backend — SharedPreferences, DataStore, ROOM,
  files, or your own.

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

Use `MutableKrate` when your storage is fast and synchronous (e.g. in-memory maps,
SharedPreferences):

```kotlin
class SettingsApi(private val settings: MutableMap<String, Int>) {

  // Basic mutable krate — reads/writes are synchronous
  val volumeKrate: MutableKrate<Int> = DefaultMutableKrate(
    factory = { 50 },
    loader = { settings["volume"] },
    saver = { value -> settings["volume"] = value }
  )
}
```

The `factory` provides the default value when the `loader` returns `null`. That's it — three lambdas
and you have
a fully thread-safe, type-safe storage accessor.

### CachedKrate — Keep a Value in Memory

Wrap any `Krate` to avoid repeated loads:

```kotlin
val cachedVolume: CachedMutableKrate<Int> = volumeKrate.asCachedMutableKrate()

// Access the in-memory cached value directly
val current = cachedVolume.cachedValue
```

### StateFlowKrate — Reactive Observation

Need to observe changes in your UI? Wrap into a `StateFlowMutableKrate`:

```kotlin
val reactiveVolume: StateFlowMutableKrate<Int> = volumeKrate.asStateFlowMutableKrate()

// Collect in your ViewModel / Compose / SwiftUI
reactiveVolume.cachedStateFlow.collect { volume ->
    println("Volume changed: $volume")
}
```

### Property Delegation

Don't want to call `.cachedValue` every time? Use Kotlin's `by` delegation:

```kotlin
val volume by reactiveVolume          // delegates to cachedStateFlow.value
val cached by cachedVolume            // delegates to cachedValue
```

### Suspend API

Use `SuspendMutableKrate` for I/O-bound storage (databases, files, network):

```kotlin
class SuspendSettingsApi(private val settings: MutableMap<String, Int>) {

    val volumeKrate: SuspendMutableKrate<Int> = DefaultSuspendMutableKrate(
        factory = { 50 },
        loader = { settings["volume"] },
        saver = { value -> settings["volume"] = value }
    )
}
```

The API mirrors the blocking version, but every operation is `suspend`:

```kotlin
// Save a value
volumeKrate.save(75)

// Transform and save
volumeKrate.save { current -> current + 10 }

// Transform, save, and get the new value
val newVolume = volumeKrate.saveAndGet { current -> current + 10 }

// Reset to factory default
volumeKrate.reset()
```

### StateFlowSuspendMutableKrate — Reactive + Suspend

Combine suspend operations with StateFlow observation:

```kotlin
val reactiveVolume: StateFlowSuspendMutableKrate<Int> = DefaultStateFlowSuspendMutableKrate(
    factory = { 50 },
    loader = { settings["volume"] },
    saver = { value -> settings["volume"] = value }
)

// Observe reactively
reactiveVolume.cachedStateFlow.collect { volume ->
    println("Volume: $volume")
}

// Write asynchronously
reactiveVolume.save(100)
```

### Flow API

Use `FlowMutableKrate` to integrate with Jetpack DataStore or any `Flow`-based backend:

```kotlin
class FlowSettingsApi {
    private val key = intPreferencesKey("volume")
    private val dataStore: DataStore<Preferences> = /* ... */

    val volumeKrate: FlowMutableKrate<Int> = DefaultFlowMutableKrate(
        factory = { 50 },
        loader = { dataStore.data.map { it[key] } },
        saver = { value -> dataStore.edit { it[key] = value } }
    )

    // Convert to StateFlow when needed
    val stateFlow: StateFlow<Int> = volumeKrate.stateFlow(viewModelScope)
}
```

You can even convert a `FlowMutableKrate` into a `StateFlowSuspendMutableKrate`:

```kotlin
val reactiveKrate = volumeKrate.asStateFlowSuspendMutableKrate(viewModelScope)
```

## 🔒 Thread Safety & Locks

**Every Krate is thread-safe by default.** You don't need to add any synchronization yourself.

On multithreaded platforms, each Krate is backed by a platform-specific recursive lock:

| Platform                     | Lock Implementation       |
|------------------------------|---------------------------|
| JVM / Android                | `ReentrantLock`           |
| iOS / macOS / tvOS / watchOS | `NSRecursiveLock`         |
| Linux / MinGW                | Recursive `pthread_mutex` |

On **JS / WasmJS** there is no real locking — the runtime is single-threaded, so the `Lock`
implementation is a
lightweight no-op wrapper that simply delegates to the block directly.

### How It Works

- Every Krate internally implements `LockOwner`, which holds a `Lock` instance.
- All read and write operations are automatically wrapped in `withLock {}` (blocking) or
  `withSuspendLock {}` (suspend).
- **Nested wrappers share the same lock** — when you call `.asCachedMutableKrate()` or
  `.asStateFlowMutableKrate()`, the wrapper reuses the lock from the original Krate via
  `LockOwner.Reusable`. This prevents deadlocks and keeps everything consistent.

```kotlin
val krate: MutableKrate<Int> = DefaultMutableKrate(
    factory = { 0 },
    loader = { settings["key"] },
    saver = { value -> settings["key"] = value }
)

// The cached wrapper shares the same lock — no deadlocks, no double-locking
val cached = krate.asCachedMutableKrate()
val stateFlow = krate.asStateFlowMutableKrate()
```

### Concurrent Access

You can safely read and write from multiple threads or coroutines without any additional
synchronization:

```kotlin
// Safe to call from any thread
coroutineScope {
    repeat(100) {
        launch(Dispatchers.Default) {
            krate.save { current -> current + 1 }
        }
    }
}
// krate.getValue() == 100 ✅
```

## ✂️ Null-to-Non-Null with `.withDefault`

Have a nullable Krate but need a non-null one somewhere? Use `.withDefault`:

```kotlin
val nullableKrate: MutableKrate<Int?> = DefaultMutableKrate(
    factory = { null },
    loader = { settings["key"] },
    saver = { value ->
        if (value == null) settings.remove("key")
        else settings["key"] = value
    }
)

// Wrap it — getValue() will never return null
val nonNullKrate: MutableKrate<Int> = nullableKrate.withDefault { 42 }
```

`.withDefault` is available on every Krate type — `Krate`, `MutableKrate`, `SuspendKrate`,
`SuspendMutableKrate`, `FlowKrate`, `FlowMutableKrate`, `StateFlowKrate`,
`StateFlowSuspendKrate`, and `StateFlowSuspendMutableKrate`.

## 🧩 In-Memory Krates

Need a quick in-memory store for testing or caching?

```kotlin
// Blocking
val counter: MutableKrate<Int> = InMemoryMutableKrate { 0 }

// Suspend
val asyncCounter: SuspendMutableKrate<Int> = InMemorySuspendMutableKrate { 0 }
```

## 🧪 Advanced Usage

### 🏷️ Class-Based Krate

Encapsulate a Krate as a dedicated class using delegation:

```kotlin
class IntKrate(
    key: String,
    settings: MutableMap<String, Int>
) : MutableKrate<Int?> by DefaultMutableKrate(
    factory = { null },
    loader = { settings[key] },
    saver = { value -> settings[key] = value }
)
```

### 🧱 Custom Data Types

Store any type by mapping to/from the underlying storage format:

```kotlin
data class UserSettings(val theme: String, val fontSize: Int)

class UserSettingsKrate(
    jsonStore: MutableMap<String, String>,
    json: Json
) : MutableKrate<UserSettings> by DefaultMutableKrate(
    factory = { UserSettings(theme = "light", fontSize = 14) },
    loader = {
        jsonStore["user_settings"]?.let { json.decodeFromString(it) }
    },
    saver = { value ->
        jsonStore["user_settings"] = json.encodeToString(value)
    }
)
```

### 🔄 Dynamic Keys

Create Krates on-the-fly for different entities:

```kotlin
fun scoreKrateForUser(userId: String): MutableKrate<Int> {
    return DefaultMutableKrate(
        factory = { 0 },
        loader = { settings["score_$userId"] },
        saver = { value -> settings["score_$userId"] = value }
    )
}
```

### 🔗 Wrapping & Composing Krates

The real power of KStorage is the ability to layer behaviors:

```kotlin
val krate: MutableKrate<Int?> = DefaultMutableKrate(
  factory = { null },
  loader = { settings["key"] },
  saver = { value -> settings["key"] = value }
)

// 1. Make it non-null
val nonNull: MutableKrate<Int> = krate.withDefault { 0 }

// 2. Add in-memory caching
val cached: CachedMutableKrate<Int> = nonNull.asCachedMutableKrate()

// 3. Make it reactive
val reactive: StateFlowMutableKrate<Int> = nonNull.asStateFlowMutableKrate()
```

All wrappers share the same underlying lock, so the entire chain is thread-safe.

## 🌍 Platform Support

KStorage supports a wide range of Kotlin Multiplatform targets:

- **JVM** / **Android**
- **iOS** (x64, arm64, simulator)
- **macOS** (x64, arm64)
- **tvOS** (x64, arm64, simulator)
- **watchOS** (x64, arm64, simulator)
- **Linux** (x64)
- **Windows** (mingwX64)
- **JS** (IR)
- **WasmJS**
