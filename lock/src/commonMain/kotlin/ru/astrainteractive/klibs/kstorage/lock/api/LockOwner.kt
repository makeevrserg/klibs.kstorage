@file:Suppress("KindImportDirection")

package ru.astrainteractive.klibs.kstorage.lock.api

import ru.astrainteractive.klibs.kstorage.lock.util.Lock

@InternalLockApi
public interface LockOwner {
    public val lock: Lock

    public class Reusable(instance: Any) : LockOwner {
        override val lock: Lock = (instance as? LockOwner)?.lock ?: Lock()
    }

    public class Default : LockOwner {
        override val lock: Lock = Lock()
    }
}
