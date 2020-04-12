package com.sensorberg.libs.event

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import org.assertj.core.api.Assertions.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class EventInstrumentationTest {

	private lateinit var source: MutableLiveData<Event<String>>
	private lateinit var owner: LifecycleOwner
	private lateinit var handler: Handler

	@Before fun setUp() {
		handler = Handler(Looper.getMainLooper())
		source = MutableLiveData()
		owner = object : LifecycleOwner {
			private val cycle by lazy {
				LifecycleRegistry(this).apply {
					setCurrentState(Lifecycle.State.RESUMED)
				}
			}

			override fun getLifecycle(): Lifecycle = cycle
		}
	}

	@Test
	fun getOrNull_should_consume_event() {
		val wait = CountDownLatch(1)
		handler.post {
			source.observe(owner, GetOrNullObserve(wait, "foo"))
			source.value = Event("foo")
		}
		assertTrue(wait.await(100, TimeUnit.MILLISECONDS))
	}

	@Test
	fun consume_should_consume_event() {
		val wait = CountDownLatch(1)
		handler.post {
			source.observe(owner, ConsumeObserve(wait, "foo"))
			source.value = Event("foo")
		}
		assertTrue(wait.await(100, TimeUnit.MILLISECONDS))
	}

	@Test
	fun getOrNull_should_return_null_when_event_has_been_consumed() {
		val wait = CountDownLatch(2)
		handler.post {
			source.observe(owner, GetOrNullObserve(wait, "foo"))
			source.observe(owner, GetOrNullObserve(wait, null))
			source.value = Event("foo")
		}
		val actual = wait.await(100, TimeUnit.MILLISECONDS)
		assertThat(actual).isTrue()
	}

	@Test
	fun consume_should_return_null_when_event_has_been_consumed() {
		val wait = CountDownLatch(2)
		handler.post {
			source.observe(owner, ConsumeObserve(wait, "foo"))
			source.observe(owner, ConsumeObserve(wait, null))
			source.value = Event("foo")
		}
		val actual = wait.await(100, TimeUnit.MILLISECONDS)
		assertThat(actual).isTrue()
	}

	private class GetOrNullObserve(private val latch: CountDownLatch, val expected: String?) : Observer<Event<String>> {
		override fun onChanged(event: Event<String>) {
			val actual: String? = event.getOrNull()
			assertThat(actual).isEqualTo(expected)
			latch.countDown()
		}
	}

	private class ConsumeObserve(private val latch: CountDownLatch, val expected: String?) : Observer<Event<String>> {
		override fun onChanged(event: Event<String>) {
			var actual: String? = null
			event.consume { actual = it }
			assertThat(actual).isEqualTo(expected)
			latch.countDown()
		}
	}
}