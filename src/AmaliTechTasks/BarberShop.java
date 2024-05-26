package AmaliTechTasks;

import java.util.Scanner;

public class BarberShop {
    private static final int maxWaitingChairs = 5;
    // Array to store customers in waiting chairs
    private static String[] waitingChairs = new String[maxWaitingChairs];
    // Variable to store the customer in the main chair
    private static String currentCustomer = null;
    //Counter Variables
    private static int ordCount = 1;
    private static int vipCount = 1;
    private static int simulationCount = 0;

    public static void main(String[] args) {
        //Reading user inputs
        Scanner scanner = new Scanner(System.in);

        System.out.println("Executive Barbering Shop");
        System.out.println("Press SPACE to simulate an event, or any other key to exit.");

        while (true) {
            System.out.print("Enter input: ");
            char input = scanner.nextLine().charAt(0);
            if (input != ' ') {
                break;
            }

            simulationCount++;
            // Generating a random event (0-3)
            int event = (int) (Math.random() * 4);
            handleEvent(event);
            displayShopState();
        }
    }
    private static void handleEvent(int event) {
        // Handling different events based on the value of event
        switch (event) {
            case 0:
                handleBarberDone();
                break;
            case 1:
                handleNewCustomer(true);
                break;
            case 2:
            case 3:
                handleNewCustomer(false);
                break;
        }
    }
//Method to handle barber done
    private static void handleBarberDone() {
        if (currentCustomer != null) {
            System.out.println("Barber is done trimming the hair of " + currentCustomer);
            currentCustomer = null;
            serveNextCustomer();
        }
    }
    //Method to handle customers
    private static void handleNewCustomer(boolean isVIP) {
        String name;
        if (currentCustomer == null) {
            name = isVIP ? "VIP" + vipCount++ : "ORD" + ordCount++;
            currentCustomer = name;
            System.out.println(name + " is seated in the main chair");
        } else {
            int emptyChairIndex = -1;
            for (int i = 0; i < maxWaitingChairs; i++) {
                if (waitingChairs[i] == null) {
                    emptyChairIndex = i;
                    break;
                }
            }

            if (emptyChairIndex != -1) {
                name = isVIP ? "VIP" + vipCount++ : "ORD" + ordCount++;
                if (isVIP) {
                    shiftCustomersBack(emptyChairIndex);
                    waitingChairs[0] = name + " (VIP)";
                    System.out.println(name + " (VIP) is seated in the first waiting chair");
                } else {
                    waitingChairs[emptyChairIndex] = name;
                    System.out.println(name + " is seated in a waiting chair");
                }
            } else {
                name = isVIP ? "VIP" + vipCount : "ORD" + ordCount;
                System.out.println(name + " has arrived, but all seats are occupied. Customer left the shop.");
            }
        }
    }
    //Method to handle moving the next customer to the main seat
    private static void serveNextCustomer() {
        if (waitingChairs[0] != null) {
            currentCustomer = waitingChairs[0];
            System.out.println(currentCustomer + " is now seated in the main chair");
            shiftCustomersForward();
        }
    }
    //Method to handle the shifting of ordinary customers back
    private static void shiftCustomersBack(int emptyChairIndex) {
        for (int i = emptyChairIndex; i > 0; i--) {
            waitingChairs[i] = waitingChairs[i - 1];
        }
        waitingChairs[0] = null;
    }
    //Method to shift customers forward
    private static void shiftCustomersForward() {
        for (int i = 0; i < maxWaitingChairs - 1; i++) {
            waitingChairs[i] = waitingChairs[i + 1];
        }
        waitingChairs[maxWaitingChairs - 1] = null;
    }
//Method to display the current state of the shop after a simulation
    private static void displayShopState() {
        System.out.println("\nSimulation " + simulationCount + ":");
        System.out.print("Customers in the shop: ");
        if (currentCustomer != null) {
            System.out.print(currentCustomer + " (Main Chair)");
        } else {
            System.out.print("Main Chair is empty");
        }

        boolean hasWaitingCustomers = false;
        for (String customer : waitingChairs) {
            if (customer != null) {
                if (!hasWaitingCustomers) {
                    System.out.print(", Waiting Chairs: ");
                    hasWaitingCustomers = true;
                }
                System.out.print(customer + " ");
            }
        }

        System.out.println();
    }
}
