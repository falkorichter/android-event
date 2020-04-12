package com.sensorberg.libs.event

import org.assertj.core.api.Assertions.*
import org.junit.Test

class EventTest {

	@Test
	fun `getOrNull should return same data`() {
		val classUnderTest = Event("foo")

		val actual = classUnderTest.getOrNull()

		assertThat(actual).isEqualTo("foo")
	}

	@Test
	fun `getOrNull should return null after second call to getOrNull`() {
		val classUnderTest = Event("foo")

		classUnderTest.getOrNull()
		val actual = classUnderTest.getOrNull()

		assertThat(actual).isNull()
	}

	@Test
	fun `consume should call lambda with data`() {
		val classUnderTest = Event("foo")

		var actual: String? = null
		classUnderTest.consume {
			actual = it
		}

		assertThat(actual).isEqualTo("foo")
	}

	@Test
	fun `consume should not call lambda after second call`() {
		val classUnderTest = Event("foo")

		var actual: String? = null
		classUnderTest.getOrNull()
		classUnderTest.consume {
			actual = it
		}

		assertThat(actual).isNull()
	}
}