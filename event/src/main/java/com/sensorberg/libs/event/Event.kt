package com.sensorberg.libs.event

/**
 * Wrapper for data that is exposed via LiveData.
 * An event contains a single consumable data.
 */
class Event<out T>(private val data: T) {

	private var consumed = false

	/**
	 * Get the data or returns null if the data has been consumed.
	 *
	 * @return [T] data or null.
	 */
	fun getOrNull(): T? {
		return synchronized(this) {
			if (consumed) {
				null
			} else {
				consumed = true
				data
			}
		}
	}

	/**
	 * If this event has not been consumed yet it will call [block] with [data], otherwise it won't call [block] at all.
	 *
	 * @param block gets called with [data] if it has not been consumed before.
	 */
	fun consume(block: (T) -> Unit) {
		getOrNull()?.let(block)
	}
}