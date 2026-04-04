package ru.astrainteractive.klibs.kstorage.internal.lock

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class LockTest {
    @Test
    fun GIVEN_lock_factory_WHEN_creating_instance_THEN_lock_is_not_null() {
        runTest {
            // Just assert it doesn't crash when executing
            val lock = Lock()
            assertNotNull(
                actual = lock,
                message = "Lock instance should not be null after creation"
            )
        }
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
            // Verify lock is still usable after exception (proves unlock happened in finally)
            val result = lock.withLock { "recovered" }
            assertEquals(
                expected = "recovered",
                actual = result,
                message = "Lock should be usable after exception is thrown"
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
    fun GIVEN_lock_WHEN_multiple_async_sum_up_to_1000_inside_lock_THEN_returns_1000() {
        runTest {
            val lock = Lock()
            var result = 0
            List(1000) { i -> i }
                .map { async { lock.withLock { result += 1 } } }
                .awaitAll()
            assertEquals(
                expected = 1000,
                actual = result,
                message = "After 1000 concurrent increments with lock, result should be 1000"
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
