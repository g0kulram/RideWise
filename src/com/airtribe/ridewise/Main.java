package com.airtribe.ridewise;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.strategy.fare.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.fare.FareStrategy;
import com.airtribe.ridewise.strategy.ridematching.LeastActiveDriverStrategy;
import com.airtribe.ridewise.strategy.ridematching.NearestDriverStrategy;
import com.airtribe.ridewise.strategy.fare.PeakHourFareStrategy;
import com.airtribe.ridewise.strategy.ridematching.RideMatchingStrategy;

import java.util.Collection;
import java.util.Scanner;

public class Main {

    private final Scanner scanner = new Scanner(System.in);
    private final RiderService riderService = new RiderService();
    private final DriverService driverService = new DriverService();

    // Default strategies injected at startup; can be swapped via the menu.
    private RideMatchingStrategy matchingStrategy = new NearestDriverStrategy();
    private FareStrategy fareStrategy = new DefaultFareStrategy();
    private final RideService rideService =
            new RideService(riderService, driverService, matchingStrategy, fareStrategy);

    static void main() {
        new Main().run();
    }

    public void run() {
        System.out.println("=== Welcome to RideWise ===");
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");

            try {
                switch (choice) {
                    case 1 -> addRider();
                    case 2 -> addDriver();
                    case 3 -> viewAvailableDrivers();
                    case 4 -> requestRide();
                    case 5 -> completeRide();
                    case 6 -> viewRides();
                    case 7 -> {
                        running = false;
                        System.out.println("Thank you for using RideWise. Goodbye!");
                    }
                    default -> System.out.println("Invalid option. Please choose between 1 and 7.");
                }
            } catch (NoDriverAvailableException | IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n--- RideWise Menu ---");
        System.out.println("1. Add Rider");
        System.out.println("2. Add Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Request Ride");
        System.out.println("5. Complete Ride");
        System.out.println("6. View Rides");
        System.out.println("7. Exit");
    }

    private void addRider() {
        System.out.print("Rider name: ");
        String name = scanner.nextLine();
        Location location = readLocation();
        Rider rider = riderService.registerRider(name, location);
        System.out.println("Registered: " + rider);
    }

    private void addDriver() {
        System.out.print("Driver name: ");
        String name = scanner.nextLine();
        Location location = readLocation();
        VehicleType vehicleType = readVehicleType();
        Driver driver = driverService.registerDriver(name, location, vehicleType);
        System.out.println("Registered: " + driver);
    }

    private void viewAvailableDrivers() {
        var available = driverService.listAvailableDrivers();
        if (available.isEmpty()) {
            System.out.println("No drivers currently available.");
            return;
        }
        System.out.println("Available drivers:");
        available.forEach(d -> System.out.println("  " + d));
    }

    private void requestRide() {
        System.out.print("Rider ID: ");
        String riderId = scanner.nextLine();
        double distance = readDouble("Trip distance (km): ");

        chooseMatchingStrategy();

        Ride ride = rideService.requestRide(riderId, distance);
        System.out.println("Ride created and assigned: " + ride);
    }

    private void completeRide() {
        System.out.print("Ride ID: ");
        String rideId = scanner.nextLine();

        chooseFareStrategy();

        Ride ride = rideService.completeRide(rideId);
        System.out.println("Ride completed: " + ride);
        System.out.println("Receipt: " + ride.getFareReceipt());
    }

    private void viewRides() {
        Collection<Ride> rides = rideService.getAllRides();
        if (rides.isEmpty()) {
            System.out.println("No rides yet.");
            return;
        }
        rides.forEach(r -> System.out.println("  " + r));
    }

    private void chooseMatchingStrategy() {
        System.out.println("Matching strategy: 1) Nearest Driver  2) Least Active Driver");
        int choice = readInt("Choose (default 1): ");
        RideMatchingStrategy strategy = (choice == 2)
                ? new LeastActiveDriverStrategy()
                : new NearestDriverStrategy();
        rideService.setMatchingStrategy(strategy);
    }

    private void chooseFareStrategy() {
        System.out.println("Fare strategy: 1) Default  2) Peak Hour");
        int choice = readInt("Choose (default 1): ");
        FareStrategy strategy = (choice == 2)
                ? new PeakHourFareStrategy()
                : new DefaultFareStrategy();
        rideService.setFareStrategy(strategy);
    }

    private Location readLocation() {
        double x = readDouble("  Location X coordinate: ");
        double y = readDouble("  Location Y coordinate: ");
        return new Location(x, y);
    }

    private VehicleType readVehicleType() {
        System.out.println("  Vehicle type: 1) BIKE  2) AUTO  3) CAR");
        int choice = readInt("  Choose: ");
        return switch (choice) {
            case 1 -> VehicleType.BIKE;
            case 2 -> VehicleType.AUTO;
            default -> VehicleType.CAR;
        };
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
