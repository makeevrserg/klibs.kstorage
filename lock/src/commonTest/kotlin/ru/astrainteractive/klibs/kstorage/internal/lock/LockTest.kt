package ru.astrainteractive.klibs.kstorage.internal.lock

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
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
            assertNotNull(lock)
        }
    }

    @Test
    fun GIVEN_lock_WHEN_executing_block_with_return_value_THEN_value_is_returned() {
        runTest {
            val lock = Lock()
            val expected = 42
            val actual = lock.withLock { expected }
            assertEquals(expected, actual)
        }
    }

    @Test
    fun GIVEN_lock_WHEN_executing_block_with_side_effect_THEN_side_effect_is_applied() {
        runTest {
            val lock = Lock()
            var value = 0
            lock.withLock { value = 10 }
            assertEquals(10, value)
        }
    }

    @Test
    fun GIVEN_lock_WHEN_block_throws_exception_THEN_exception_is_propagated_and_lock_remains_usable() {
        runTest {
            val lock = Lock()
            val exception = runCatching {
                lock.withLock { error("test error") }
            }.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is IllegalStateException)
            // Verify lock is still usable after exception (proves unlock happened in finally)
            val result = lock.withLock { "recovered" }
            assertEquals("recovered", result)
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
            assertEquals(listOf(1, 2, 3), results)
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
            assertEquals(1000, result)
        }
    }

    @Test
    fun GIVEN_shared_counter_WHEN_concurrent_increments_with_lock_THEN_counter_equals_total_increments() {
        runTest {
            val lock = Lock()
            var counter = 0
            val iterationsPerCoroutine = 1000
            val coroutinesCount = 10
            withContext(Dispatchers.Default) {
                coroutineScope {
                    (1..coroutinesCount).map {
                        async {
                            repeat(iterationsPerCoroutine) {
                                lock.withLock { counter++ }
                            }
                        }
                    }.awaitAll()
                }
            }
            assertEquals(coroutinesCount * iterationsPerCoroutine, counter)
        }
    }

    @Test
    fun GIVEN_shared_list_WHEN_concurrent_writes_with_lock_THEN_all_elements_present_and_no_duplicates() {
        runTest {
            val lock = Lock()
            val list = mutableListOf<Int>()
            val coroutinesCount = 10
            val elementsPerCoroutine = 100
            withContext(Dispatchers.Default) {
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
            }
            assertEquals(coroutinesCount * elementsPerCoroutine, list.size)
            assertEquals(list.size, list.toSet().size)
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
            withContext(Dispatchers.Default) {
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
                                    assertTrue(sharedValue >= 0)
                                    assertTrue(sharedValue <= writersCount * iterationsPerCoroutine)
                                }
                            }
                        }
                    }
                    (writers + readers).awaitAll()
                }
            }
            assertEquals(writersCount * iterationsPerCoroutine, sharedValue)
        }
    }
}
