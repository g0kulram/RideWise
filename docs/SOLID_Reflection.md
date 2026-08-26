# RideWise — SOLID & Design Principle Reflection

## Single Responsibility Principle
- `RiderService` only registers/looks up riders.
- `DriverService` only registers drivers and tracks availability.
- `RideService` only orchestrates the ride lifecycle — it delegates matching
  to `RideMatchingStrategy` and pricing to `FareStrategy` rather than doing
  either itself.
- `IdGenerator` is the single place that knows how IDs are formatted.

## Open/Closed Principle
Adding a new matching algorithm (e.g. `HighestRatedDriverStrategy`) or a new
pricing model (e.g. `SubscriptionFareStrategy`) means writing one new class
that implements the relevant interface. `RideService`, `Main`, and every
existing strategy are untouched.

## Liskov Substitution Principle
Any `RideMatchingStrategy` can replace any other inside `RideService`
without breaking behaviour — each only relies on the rider's location and
the driver list it's handed, and always returns a `Driver` or `null`. The
same holds for `FareStrategy`: every implementation takes a `Ride` and
returns a `double`, with no hidden preconditions one implementation relies
on that another violates.

## Interface Segregation Principle
`RideMatchingStrategy` and `FareStrategy` are each a single-method
interface. No implementation is forced to provide behaviour it doesn't need.

## Dependency Inversion Principle
`RideService`'s constructor takes `RideMatchingStrategy` and `FareStrategy`
— interfaces, not concrete classes. `Main` (the composition root) decides
which concrete strategy to inject, and can swap either at runtime via
`setMatchingStrategy` / `setFareStrategy`.

## DRY
ID generation logic lives only in `IdGenerator`. Fare-surge logic in
`PeakHourFareStrategy` reuses `DefaultFareStrategy` by composition instead
of re-implementing the base-fare formula.

## KISS
`Location` is a plain (x, y) pair with Euclidean distance — no real mapping
integration, since the exercise is about LLD, not geospatial systems.

## YAGNI
No persistence layer, authentication, or concurrency handling — the brief
asks for a console MVP that demonstrates design, not a production system.

## Law of Demeter
`RideService` calls `driverService.updateAvailability(...)` and
`rider.getLocation()` directly rather than chaining through unrelated
objects' internals (e.g. it never does something like
`ride.getDriver().getService().getFleet().get(0)`). Each class only talks to
its immediate collaborators.
