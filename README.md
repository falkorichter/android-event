# Event
[ ![Download](https://api.bintray.com/packages/sensorberg/maven/event/images/download.svg) ](https://bintray.com/sensorberg/maven/event/_latestVersion)

This is a little library that provides an Event class which can be used in LiveData. Event's can only be consumed once.
This is required when you are using LiveData but you do not want the value inside the LiveData be emitted again on
configuration changes.

```
val event1 =  Event<String>("foo")
event1.getOrNull() // returns "foo"
event1.getOrNull() // returns null

val event2 =  Event<String>("bar")
event2.consume { value: String -> 
    // value is "bar"
}
event2.consume {
    // this lambda will not get called because the value has been consumed
}
```