# reactive-programming-using-reactor
This project has the necessary code for the reactive programming course using Project Reactor

## Swagger-UI

-   Check the following [link](http://localhost:8080/movies/swagger-ui.html) for swagger.

## What is the goal
- Essentially, Reactive Streams is a specification for asynchronous stream processing. 
In other words, a system where lots of events are being produced and consumed asynchronously.
- Think about a stream of thousands of stock updates per second coming into a financial application, and for it to have to respond to those updates in a timely manner.
- One of the main goals of this is to address the problem of *backpressure*. 
- If we have a producer which is emitting events to a consumer faster than it can process them, then eventually the consumer will be overwhelmed with events, running out of system resources.
- Backpressure means that our consumer should be able to tell the producer how much data to send in order to prevent this, and this is what is laid out in the specification.
