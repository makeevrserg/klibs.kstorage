package ru.astrainteractive.klibs.kstorage.internal.lock

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class LockTest {

    @Test
    fun GIVEN_fresh_lock_WHEN_no_block_is_executed_THEN_is_not_locked() {
        val lock = Lock()
        assertFalse(
            actual = lock.isLocked,
            message = "A freshly created lock must report isLocked == false"
        )
    }

    @Test
    fun GIVEN_lock_WHEN_executing_block_with_return_value_THEN_value_is_returned() {
        runTest {
            val lock = Lock()
            val expected = 42
            val actual = lock.withLock { expected }
            assertEquals(
                expected = expected,
                actual = actual,
                message = "withLock should return the value produced by the block"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_block_returns_null_THEN_null_is_returned() {
        runTest {
            val lock = Lock()
            val actual: String? = lock.withLock { null }
            assertNull(
                actual = actual,
                message = "withLock must return null when the block returns null"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_executing_block_with_side_effect_THEN_side_effect_is_applied() {
        runTest {
            val lock = Lock()
            var value = 0
            lock.withLock { value = 10 }
            assertEquals(
                expected = 10,
                actual = value,
                message = "Side effect inside withLock should be applied"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_executing_withLock_THEN_is_locked_inside_and_unlocked_after() {
        runTest {
            val lock = Lock()
            var lockedDuringBlock: Boolean? = null
            lock.withLock { lockedDuringBlock = lock.isLocked }
            assertEquals(
                expected = true,
                actual = lockedDuringBlock,
                message = "isLocked must be true while the withLock block is running"
            )
            assertFalse(
                actual = lock.isLocked,
                message = "isLocked must be false once withLock returns"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_block_throws_exception_THEN_exception_is_propagated_and_lock_remains_usable() {
        runTest {
            val lock = Lock()
            val exception = runCatching {
                lock.withLock { error("test error") }
            }.exceptionOrNull()
            assertNotNull(
                actual = exception,
                message = "Exception from withLock block should be propagated"
            )
            assertTrue(
                actual = exception is IllegalStateException,
                message = "Propagated exception should be IllegalStateException"
            )
            assertEquals(
                expected = "test error",
                actual = exception.message,
                message = "Original exception message must be preserved when propagated"
            )
            // Verify lock is still usable after exception (proves unlock happened in finally)
            val result = lock.withLock { "recovered" }
            assertEquals(
                expected = "recovered",
                actual = result,
                message = "Lock should be usable after an exception is thrown inside withLock"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_withLock_is_nested_THEN_inner_block_runs_without_deadlock() {
        runTest {
            val lock = Lock()
            val actual = lock.withLock {
                lock.withLock { "inner" }
            }
            assertEquals(
                expected = "inner",
                actual = actual,
                message = "Reentrant withLock must not deadlock and must return the inner value"
            )
            assertFalse(
                actual = lock.isLocked,
                message = "Lock must be released after a nested withLock completes"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_withLock_is_nested_three_levels_THEN_all_levels_execute_outermost_first() {
        runTest {
            val lock = Lock()
            val executionOrder = mutableListOf<Int>()
            lock.withLock {
                executionOrder.add(1)
                lock.withLock {
                    executionOrder.add(2)
                    lock.withLock {
                        executionOrder.add(3)
                    }
                }
            }
            assertEquals(
                expected = listOf(1, 2, 3),
                actual = executionOrder,
                message = "Deeply nested reentrant withLock calls must all execute, outermost first"
            )
            assertFalse(
                actual = lock.isLocked,
                message = "Lock must be released after all nested levels complete"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_withLock_is_nested_THEN_is_locked_stays_true_until_the_outermost_block_returns() {
        runTest {
            val lock = Lock()
            var lockedInOuterBeforeInner: Boolean? = null
            var lockedInInner: Boolean? = null
            var lockedInOuterAfterInner: Boolean? = null
            lock.withLock {
                lockedInOuterBeforeInner = lock.isLocked
                lock.withLock {
                    lockedInInner = lock.isLocked
                }
                lockedInOuterAfterInner = lock.isLocked
            }
            assertEquals(
                expected = true,
                actual = lockedInOuterBeforeInner,
                message = "isLocked must be true in the outer block before entering the nested one"
            )
            assertEquals(
                expected = true,
                actual = lockedInInner,
                message = "isLocked must be true inside the nested block"
            )
            assertEquals(
                expected = true,
                actual = lockedInOuterAfterInner,
                message = "isLocked must remain true in the outer block after the nested block " +
                    "returns — the lock is still held at that point"
            )
            assertFalse(
                actual = lock.isLocked,
                message = "isLocked must be false only after the outermost withLock returns"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_withLock_block_throws_THEN_is_locked_is_false_afterwards() {
        runTest {
            val lock = Lock()
            runCatching {
                lock.withLock { error("boom") }
            }
            assertFalse(
                actual = lock.isLocked,
                message = "isLocked must return to false after a withLock block throws"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_nested_inner_withLock_block_throws_THEN_is_locked_is_false_afterwards() {
        runTest {
            val lock = Lock()
            runCatching {
                lock.withLock {
                    lock.withLock { error("inner boom") }
                }
            }
            assertFalse(
                actual = lock.isLocked,
                message = "isLocked must return to false after an exception unwinds all nested withLock levels"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_multiple_sequential_withLock_calls_THEN_all_blocks_execute_in_order() {
        runTest {
            val lock = Lock()
            val results = mutableListOf<Int>()
            lock.withLock { results.add(1) }
            lock.withLock { results.add(2) }
            lock.withLock { results.add(3) }
            assertEquals(
                expected = listOf(1, 2, 3),
                actual = results,
                message = "Sequential withLock calls should execute in order"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_executing_suspend_block_with_return_value_THEN_value_is_returned() {
        runTest {
            val lock = Lock()
            val expected = 42
            val actual = lock.withSuspendLock { expected }
            assertEquals(
                expected = expected,
                actual = actual,
                message = "withSuspendLock should return the value produced by the block"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_suspend_block_returns_null_THEN_null_is_returned() {
        runTest {
            val lock = Lock()
            val actual: String? = lock.withSuspendLock { null }
            assertNull(
                actual = actual,
                message = "withSuspendLock must return null when the block returns null"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_executing_suspend_block_with_side_effect_THEN_side_effect_is_applied() {
        runTest {
            val lock = Lock()
            var value = 0
            lock.withSuspendLock { value = 10 }
            assertEquals(
                expected = 10,
                actual = value,
                message = "Side effect inside withSuspendLock should be applied"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_executing_withSuspendLock_THEN_is_locked_inside_and_unlocked_after() {
        runTest {
            val lock = Lock()
            var lockedDuringBlock: Boolean? = null
            lock.withSuspendLock { lockedDuringBlock = lock.isLocked }
            assertEquals(
                expected = true,
                actual = lockedDuringBlock,
                message = "isLocked must be true while the withSuspendLock block is running"
            )
            assertFalse(
                actual = lock.isLocked,
                message = "isLocked must be false once withSuspendLock returns"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_suspend_block_throws_exception_THEN_exception_is_propagated_and_lock_remains_usable() {
        runTest {
            val lock = Lock()
            val exception = runCatching {
                lock.withSuspendLock { error("suspend error") }
            }.exceptionOrNull()
            assertNotNull(
                actual = exception,
                message = "Exception from withSuspendLock block should be propagated"
            )
            assertTrue(
                actual = exception is IllegalStateException,
                message = "Propagated exception should be IllegalStateException"
            )
            val result = lock.withSuspendLock { "recovered" }
            assertEquals(
                expected = "recovered",
                actual = result,
                message = "Lock should be usable after an exception is thrown inside withSuspendLock"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_withLock_and_withSuspendLock_are_interleaved_THEN_all_blocks_run_in_order() {
        runTest {
            val lock = Lock()
            val executionOrder = mutableListOf<String>()
            lock.withLock { executionOrder.add("a") }
            lock.withSuspendLock { executionOrder.add("b") }
            lock.withLock { executionOrder.add("c") }
            lock.withSuspendLock { executionOrder.add("d") }
            assertEquals(
                expected = listOf("a", "b", "c", "d"),
                actual = executionOrder,
                message = "Interleaved withLock and withSuspendLock calls must run in order"
            )
            assertFalse(
                actual = lock.isLocked,
                message = "Lock must be released after the last call completes"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_withLock_is_nested_inside_withSuspendLock_THEN_inner_runs_without_deadlock() {
        runTest {
            val lock = Lock()
            val actual = lock.withSuspendLock {
                lock.withLock { "inner" }
            }
            assertEquals(
                expected = "inner",
                actual = actual,
                message = "A withLock nested inside withSuspendLock must not deadlock"
            )
            assertFalse(
                actual = lock.isLocked,
                message = "Lock must be released after the nested call completes"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_withSuspendLock_is_nested_THEN_inner_runs_without_deadlock() {
        runTest {
            val lock = Lock()
            val actual = lock.withSuspendLock {
                lock.withSuspendLock { "inner" }
            }
            assertEquals(
                expected = "inner",
                actual = actual,
                message = "Reentrant withSuspendLock must not deadlock and must return the inner value"
            )
            assertFalse(
                actual = lock.isLocked,
                message = "Lock must be released after the nested suspend call completes"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_withSuspendLock_is_nested_THEN_is_locked_stays_true_until_the_outermost_block_returns() {
        runTest {
            val lock = Lock()
            var lockedInOuterAfterInner: Boolean? = null
            lock.withSuspendLock {
                lock.withSuspendLock { }
                lockedInOuterAfterInner = lock.isLocked
            }
            assertEquals(
                expected = true,
                actual = lockedInOuterAfterInner,
                message = "isLocked must remain true in the outer suspend block after the nested one returns"
            )
            assertFalse(
                actual = lock.isLocked,
                message = "isLocked must be false only after the outermost withSuspendLock returns"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_withSuspendLock_block_throws_THEN_is_locked_is_false_afterwards() {
        runTest {
            val lock = Lock()
            runCatching {
                lock.withSuspendLock { error("suspend boom") }
            }
            assertFalse(
                actual = lock.isLocked,
                message = "isLocked must return to false after a withSuspendLock block throws"
            )
        }
    }

    @Test
    fun GIVEN_lock_WHEN_many_concurrent_withLock_increments_THEN_no_updates_are_lost() {
        runTest {
            val lock = Lock()
            var result = 0
            val incrementCount = 1000
            (0 until incrementCount)
                .map { async { lock.withLock { result += 1 } } }
                .awaitAll()
            assertEquals(
                expected = incrementCount,
                actual = result,
                message = "After $incrementCount concurrent increments under lock, result should equal $incrementCount"
            )
        }
    }

    @Test
    fun GIVEN_shared_counter_WHEN_concurrent_increments_with_lock_THEN_counter_equals_total_increments() {
        runTest {
            val lock = Lock()
            var counter = 0
            val iterationsPerCoroutine = 1000
            val coroutinesCount = 10
            coroutineScope {
                (1..coroutinesCount).map {
                    async {
                        repeat(iterationsPerCoroutine) {
                            lock.withLock { counter++ }
                        }
                    }
                }.awaitAll()
            }
            assertEquals(
                expected = coroutinesCount * iterationsPerCoroutine,
                actual = counter,
                message = "Counter should equal total increments after concurrent access with lock"
            )
        }
    }

    @Test
    fun GIVEN_shared_list_WHEN_concurrent_writes_with_lock_THEN_all_elements_present_and_no_duplicates() {
        runTest {
            val lock = Lock()
            val list = mutableListOf<Int>()
            val coroutinesCount = 10
            val elementsPerCoroutine = 100
            coroutineScope {
                (0 until coroutinesCount).map { coroutineIndex ->
                    async {
                        repeat(elementsPerCoroutine) { elementIndex ->
                            lock.withLock {
                                list.add(coroutineIndex * elementsPerCoroutine + elementIndex)
                            }
                        }
                    }
                }.awaitAll()
            }
            assertEquals(
                expected = coroutinesCount * elementsPerCoroutine,
                actual = list.size,
                message = "List size should equal total elements written concurrently"
            )
            assertEquals(
                expected = list.size,
                actual = list.toSet().size,
                message = "All elements should be unique — no duplicates from concurrent writes"
            )
        }
    }

    @Test
    fun GIVEN_shared_variable_WHEN_concurrent_read_write_with_lock_THEN_reads_are_consistent() {
        runTest {
            val lock = Lock()
            var sharedValue = 0
            val iterationsPerCoroutine = 1000
            val writersCount = 5
            val readersCount = 5
            coroutineScope {
                val writers = (1..writersCount).map {
                    async(start = CoroutineStart.LAZY) {
                        repeat(iterationsPerCoroutine) {
                            lock.withLock {
                                val current = sharedValue
                                sharedValue = current + 1
                            }
                        }
                    }
                }
                val readers = (1..readersCount).map {
                    async(start = CoroutineStart.LAZY) {
                        repeat(iterationsPerCoroutine) {
                            lock.withLock {
                                assertTrue(
                                    actual = sharedValue >= 0,
                                    message = "Shared value should never be negative during concurrent reads"
                                )
                                assertTrue(
                                    actual = sharedValue <= writersCount * iterationsPerCoroutine,
                                    message = "Shared value should not exceed total expected increments"
                                )
                            }
                        }
                    }
                }
                (writers + readers).awaitAll()
            }
            assertEquals(
                expected = writersCount * iterationsPerCoroutine,
                actual = sharedValue,
                message = "Shared value should equal total writer increments after all coroutines complete"
            )
        }
    }
}
