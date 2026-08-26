# RideWise — Requirements

## A. Functional Requirements
1. Register riders
2. Register drivers
3. Show available drivers
4. Request a ride
5. Match ride to driver using a pluggable strategy
6. Calculate fare using a pluggable pricing strategy
7. Track ride status through its lifecycle: `REQUESTED → ASSIGNED → COMPLETED`,
   or `→ CANCELLED` from either of the first two states

## B. Non-Functional Requirements
- Easily extendable pricing algorithm — add a class, don't touch existing code
- Easily changeable driver-matching logic — same principle
- Low coupling between services — each service only knows the collaborators
  it's explicitly given
- Maintainable, readable console code with input validation

## C. Out of Scope (YAGNI)
- Persistence (all state is in-memory for the lifetime of the process)
- Real geolocation / mapping APIs — `Location` is a simple (x, y) coordinate
- Payments, authentication, concurrency/multi-user handling
- REST API or UI beyond the console menu

## Menu Flow
```
1. Add Rider
2. Add Driver
3. View Available Drivers
4. Request Ride
5. Complete Ride
6. View Rides
7. Exit
```
Every option is handled by `Main`, which only ever calls into the service
layer (`RiderService`, `DriverService`, `RideService`) — it holds no
business logic itself.
