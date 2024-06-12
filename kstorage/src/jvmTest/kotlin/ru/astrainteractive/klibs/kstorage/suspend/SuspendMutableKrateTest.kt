package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.test.runTest
import ru.astrainteractive.klibs.kstorage.suspend.test.StoreSuspendMutableKrate
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SuspendMutableKrateTest {

    @Test
    fun test(): Unit = runTest {
        val initialValue = 10
        val krate = StoreSuspendMutableKrate(
            key = "some_int_key",
            factory = { initialValue }
        )
        assertEquals(initialValue, krate.getValue())
        15.let { newValue ->
            krate.save(newValue)
            assertEquals(newValue, krate.getValue())
        }
        krate.reset()
        assertEquals(initialValue, krate.getValue())
    }
}
