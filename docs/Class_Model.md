# RideWise — Class Model

## model/
| Class | Responsibility                                                         |
|---|------------------------------------------------------------------------|
| `Location` | Immutable (x, y) coordinate; has method to calculate distance.         |
| `Rider` | id, name, location.                                                    |
| `Driver` | id, name, currentLocation, available, vehicleType, completedRideCount. |
| `Ride` | id, rider, driver, distance, status, fareReceipt.                      |
| `FareReceipt` | rideId, amount, generatedAt — composed inside `Ride`.                  |
| `RideStatus` (enum) | `REQUESTED`, `ASSIGNED`, `COMPLETED`, `CANCELLED`.                     |
| `VehicleType` (enum) | `BIKE`, `AUTO`, `CAR`.                                                 |

## strategy/
| Interface | Method | Implementations |
|---|---|---|
| `RideMatchingStrategy` | `Driver findDriver(Rider, List<Driver>)` | `NearestDriverStrategy`, `LeastActiveDriverStrategy` |
| `FareStrategy` | `double calculateFare(Ride)` | `DefaultFareStrategy`, `PeakHourFareStrategy` |

`PeakHourFareStrategy` composes a `FareStrategy` (defaults to
`DefaultFareStrategy`) and applies a surge multiplier during peak windows —
composition over inheritance, applied even between two strategies.

## service/
| Class | Responsibility | Depends on |
|---|---|---|
| `RiderService` | Register / look up riders. | — |
| `DriverService` | Register drivers, track availability, list available drivers. | — |
| `RideService` | Orchestrate the ride lifecycle: request → assign → complete/cancel. | `RiderService`, `DriverService`, `RideMatchingStrategy`, `FareStrategy` (all injected via constructor) |

## exception/
- `NoDriverAvailableException` — thrown by `RideService` when no driver can
  be matched.

## util/
- `IdGenerator` — centralised, thread-safe ID generation per entity type
  (`R-`, `D-`, `RIDE-` prefixes) so no service invents its own numbering.

## Main
Composition root: constructs the services, wires default strategies into
`RideService`, and runs the console menu loop. Contains no domain logic —
every menu option delegates to a service method.
