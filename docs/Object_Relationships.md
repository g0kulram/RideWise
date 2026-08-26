# RideWise — Object Relationships

| Relationship | Type | Notes |
|---|---|---|
| `Rider` → `Ride` | Association | A rider is linked to rides via `Ride.rider`, but `Ride` objects are created and owned by `RideService`, not by `Rider` itself. |
| `Driver` → `Ride` | Association | Same pattern: `Ride.driver` references a `Driver`; the ride's lifecycle is managed externally by `RideService`. |
| `Ride` → `FareReceipt` | Composition | A `FareReceipt` has no independent existence — it's created only when a ride completes (`RideService.completeRide`) and is stored inside that specific `Ride`. If the `Ride` is discarded, so is its receipt. |
| `RideService` → `RideMatchingStrategy` / `FareStrategy` | Composition (via Dependency Injection) | `RideService` holds a reference to each strategy for its entire lifetime and cannot function without one — but the strategy objects themselves are supplied from outside (constructor injection), keeping `RideService` decoupled from any specific algorithm. |
| `PeakHourFareStrategy` → `FareStrategy` | Composition | `PeakHourFareStrategy` wraps another `FareStrategy` (defaulting to `DefaultFareStrategy`) instead of extending it, demonstrating composition over inheritance between two strategies. |
| `Main` → `RiderService` / `DriverService` / `RideService` | Composition | `Main` is the composition root: it constructs every service once and wires them together for the lifetime of the application. |
