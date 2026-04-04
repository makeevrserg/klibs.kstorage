package ru.astrainteractive.klibs.kstorage.internal.lock

interface LockOwner {
    val lock: Lock

    class Reusable(instance: Any) : LockOwner {
        override val lock: Lock = (instance as? LockOwner)?.lock ?: Lock()
    }

    class Default : LockOwner {
        override val lock: Lock = Lock()
    }
}
