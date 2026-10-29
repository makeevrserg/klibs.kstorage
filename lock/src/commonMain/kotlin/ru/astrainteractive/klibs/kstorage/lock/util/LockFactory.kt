package ru.astrainteractive.klibs.kstorage.lock.util

import ru.astrainteractive.klibs.kstorage.lock.api.InternalLockApi
import ru.astrainteractive.klibs.kstorage.lock.api.Lock

@InternalLockApi
public expect fun Lock(): Lock
