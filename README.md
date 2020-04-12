# Event
This is a little library that provides an Event class which can be used in LiveData. 
Event's can only be consumed once. This is required when you are using LiveData but you do not want the value inside the LiveData be emitted again on configuration changes.