import java.io.*;
import java.util.*;

public class MainApp {

```
static String currentUser = null;
static int bookingCounter = 1001;

public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
        System.out.println("\n===== RAPIDO BOOKING SYSTEM =====");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter Choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                register(sc);
                break;

            case 2:
                if (login(sc)) {
                    userMenu(sc);
                }
                break;

            case 3:
                System.out.println("Thank You!");
                System.exit(0);

            default:
                System.out.println("Invalid Choice!");
        }
    }
}

static void register(Scanner sc) {
    try {
        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        BufferedWriter bw = new BufferedWriter(
                new FileWriter("users.txt", true));

        bw.write(username + "," + password);
        bw.newLine();
        bw.close();

        System.out.println("Registration Successful!");

    } catch (Exception e) {
        System.out.println("Registration Failed!");
    }
}

static boolean login(Scanner sc) {

    System.out.print("Enter Username: ");
    String username = sc.nextLine();

    System.out.print("Enter Password: ");
    String password = sc.nextLine();

    try {
        BufferedReader br =
                new BufferedReader(new FileReader("users.txt"));

        String line;

        while ((line = br.readLine()) != null) {

            String data[] = line.split(",");

            if (data[0].equals(username)
                    && data[1].equals(password)) {

                currentUser = username;
                br.close();

                System.out.println("Login Successful!");
                return true;
            }
        }

        br.close();

    } catch (Exception e) {
        System.out.println("Login Error!");
    }

    System.out.println("Invalid Credentials!");
    return false;
}

static void userMenu(Scanner sc) {

    while (true) {

        System.out.println("\n===== USER MENU =====");
        System.out.println("1. Book Ride");
        System.out.println("2. View Bookings");
        System.out.println("3. Cancel Booking");
        System.out.println("4. Logout");
        System.out.print("Enter Choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {

            case 1:
                bookRide(sc);
                break;

            case 2:
                viewBookings();
                break;

            case 3:
                cancelBooking(sc);
                break;

            case 4:
                currentUser = null;
                System.out.println("Logged Out!");
                return;

            default:
                System.out.println("Invalid Choice!");
        }
    }
}

static void suggestVehicle(double distance) {

    System.out.println("\nRecommended Vehicles:");

    if (distance <= 100) {
        System.out.println("Bike  - ₹5/km");
        System.out.println("Auto  - ₹10/km");
        System.out.println("Car   - ₹15/km");
    }
    else if (distance <= 500) {
        System.out.println("Car   - ₹15/km");
    }
    else {
        System.out.println("No Vehicle Available");
    }
}

static void bookRide(Scanner sc) {

    try {

        System.out.print("Pickup Location: ");
        String pickup = sc.nextLine();

        System.out.print("Drop Location: ");
        String drop = sc.nextLine();

        System.out.print("Distance (KM): ");
        double distance = sc.nextDouble();
        sc.nextLine();

        suggestVehicle(distance);

        System.out.print("Vehicle Type (bike/auto/car): ");
        String vehicle = sc.nextLine().toLowerCase();

        double rate = 0;

        switch (vehicle) {

            case "bike":
                if (distance > 100) {
                    System.out.println("Bike Limit Exceeded!");
                    return;
                }
                rate = 5;
                break;

            case "auto":
                if (distance > 100) {
                    System.out.println("Auto Limit Exceeded!");
                    return;
                }
                rate = 10;
                break;

            case "car":
                if (distance > 500) {
                    System.out.println("Car Limit Exceeded!");
                    return;
                }
                rate = 15;
                break;

            default:
                System.out.println("Invalid Vehicle!");
                return;
        }

        System.out.print("Payment Method (UPI/Cash): ");
        String payment = sc.nextLine();

        double fare = distance * rate;
        int time = (int)(distance * 5);

        int bookingId = bookingCounter++;

        BufferedWriter bw =
                new BufferedWriter(
                        new FileWriter("bookings.txt", true));

        bw.write("BookingID:" + bookingId);
        bw.newLine();

        bw.write("User:" + currentUser);
        bw.newLine();

        bw.write("Pickup:" + pickup);
        bw.newLine();

        bw.write("Drop:" + drop);
        bw.newLine();

        bw.write("Distance:" + distance);
        bw.newLine();

        bw.write("Vehicle:" + vehicle);
        bw.newLine();

        bw.write("Fare:" + fare);
        bw.newLine();

        bw.write("Payment:" + payment);
        bw.newLine();

        bw.write("Time:" + time + " mins");
        bw.newLine();

        bw.write("--------------------------------");
        bw.newLine();

        bw.close();

        System.out.println("\nRide Booked Successfully!");
        System.out.println("Booking ID : " + bookingId);
        System.out.println("Fare       : ₹" + fare);
        System.out.println("ETA        : " + time + " mins");

    } catch (Exception e) {
        System.out.println("Booking Failed!");
    }
}

static void viewBookings() {

    try {

        BufferedReader br =
                new BufferedReader(
                        new FileReader("bookings.txt"));

        String line;
        boolean show = false;

        while ((line = br.readLine()) != null) {

            if (line.equals("User:" + currentUser)) {
                show = true;
            }

            if (show) {
                System.out.println(line);
            }

            if (line.equals("--------------------------------")) {
                if (show) {
                    System.out.println();
                }
                show = false;
            }
        }

        br.close();

    } catch (Exception e) {
        System.out.println("No Bookings Found!");
    }
}

static void cancelBooking(Scanner sc) {

    System.out.print("Enter Booking ID: ");
    String bookingId = sc.nextLine();

    try {

        File inputFile = new File("bookings.txt");
        File tempFile = new File("temp.txt");

        BufferedReader br =
                new BufferedReader(
                        new FileReader(inputFile));

        BufferedWriter bw =
                new BufferedWriter(
                        new FileWriter(tempFile));

        String line;

        boolean skip = false;
        boolean found = false;

        while ((line = br.readLine()) != null) {

            if (line.equals("BookingID:" + bookingId)) {
                skip = true;
                found = true;
            }

            if (!skip) {
                bw.write(line);
                bw.newLine();
            }

            if (skip &&
                    line.equals("--------------------------------")) {
                skip = false;
            }
        }

        br.close();
        bw.close();

        inputFile.delete();
        tempFile.renameTo(inputFile);

        if (found)
            System.out.println("Booking Cancelled!");
        else
            System.out.println("Booking Not Found!");

    } catch (Exception e) {
        System.out.println("Cancellation Failed!");
    }
}
```

}
