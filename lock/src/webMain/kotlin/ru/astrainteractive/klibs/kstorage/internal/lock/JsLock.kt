package ru.astrainteractive.klibs.kstorage.internal.lock

class JsLock : Lock {
    override fun <T> withLock(block: () -> T): T {
        return block.invoke()
    }
}
