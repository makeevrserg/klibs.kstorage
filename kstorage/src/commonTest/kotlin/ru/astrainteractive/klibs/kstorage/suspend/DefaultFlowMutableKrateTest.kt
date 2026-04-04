package ru.astrainteractive.klibs.kstorage.suspend

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultFlowMutableKrateTest {

    @Test
    fun GIVEN_krate_without_any_value_WHEN_first_THEN_null() = runTest {
        val krate = DefaultFlowMutableKrate<Int?>(
            factory = { null },
            loader = { flowOf() },
        )
        assertNull(
            actual = krate.flow.firstOrNull(),
            message = "Flow first value should be null when no value is provided"
        )
        assertNull(
            actual = krate.getValue(),
            message = "getValue() should return null when factory is null and loader is empty"
        )
        assertNull(
            actual = krate.stateFlow(
                coroutineScope = backgroundScope,
                coroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
            ).first(),
            message = "StateFlow first value should be null when no value is provided"
        )
    }

    @Test
    fun GIVEN_krate_with_null_factory_nonnull_value_WHEN_get_THEN_non_null() = runTest {
        val krate = DefaultFlowMutableKrate(
            factory = { null },
            loader = { flowOf(1) },
        )
        assertEquals(
            expected = 1,
            actual = krate.flow.first(),
            message = "Flow should emit loader value when factory is null"
        )
        assertEquals(
            expected = 1,
            actual = krate.getValue(),
            message = "getValue() should return loader value when factory is null"
        )
        assertEquals(
            expected = 1,
            actual = krate.stateFlow(
                coroutineScope = backgroundScope,
                coroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
            ).first(),
            message = "StateFlow should emit loader value when factory is null"
        )
    }

    @Test
    fun GIVEN_krate_with_factory_null_loader_WHEN_get_THEN_from_factory() = runTest {
        val krate = DefaultFlowMutableKrate(
            factory = { 1 },
            loader = { flowOf() },
        )
        advanceUntilIdle()
        assertNull(
            actual = krate.flow.firstOrNull(),
            message = "Flow should emit null when loader is empty"
        )
        assertEquals(
            expected = 1,
            actual = krate.getValue(),
            message = "getValue() should return factory value when loader is empty"
        )
        assertEquals(
            expected = 1,
            actual = krate.stateFlow(
                coroutineScope = backgroundScope,
                coroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
            ).first(),
            message = "StateFlow should return factory value when loader is empty"
        )
    }

    @Test
    fun GIVEN_krate_with_recreated_flow_WHEN_getvalue_THEN_same() = runTest {
        val krate = DefaultFlowMutableKrate(
            factory = { 1 },
            loader = { flowOf() },
            saver = {}
        )
        assertNull(
            actual = krate.flow.firstOrNull(),
            message = "Recreated flow should emit null when loader is empty"
        )
        assertEquals(
            expected = 1,
            actual = krate.getValue(),
            message = "getValue() should return factory value for recreated flow krate"
        )
        assertEquals(
            expected = 1,
            actual = krate.stateFlow(
                coroutineScope = backgroundScope,
                coroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
            ).first(),
            message = "StateFlow should return factory value for recreated flow krate"
        )
    }

    @Test
    fun GIVEN_krate_with_replay_flow_WHEN_save_THEN_saved() = runTest {
        val stateFlow = MutableStateFlow(1)
        val krate = DefaultFlowMutableKrate(
            factory = { 1 },
            loader = { stateFlow },
            saver = { stateFlow.emit(it) }
        )
        assertEquals(
            expected = 1,
            actual = krate.getValue(),
            message = "Initial getValue() should return factory value"
        )
        assertEquals(
            expected = 1,
            actual = krate.flow.first(),
            message = "Initial flow value should equal factory value"
        )
        krate.save(2)
        assertEquals(
            expected = 2,
            actual = krate.getValue(),
            message = "getValue() should return saved value after save"
        )
        assertEquals(
            expected = 2,
            actual = krate.flow.first(),
            message = "Flow should emit saved value after save"
        )
    }
}
