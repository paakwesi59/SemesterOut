package AmaliTechTasks;

import java.util.Scanner; // Import the Scanner class to read user input

public class BarberShop {
    private static final int MAX_WAITING_CHAIRS = 5; // Maximum number of waiting chairs
    private static String[] waitingChairs = new String[MAX_WAITING_CHAIRS]; // Array to store customers in waiting chairs
    private static String currentCustomer = null; // Variable to store the customer in the main chair
    private static int ordCount = 1; // Counter for ordinary customer names
    private static int vipCount = 1; // Counter for VIP customer names
    private static int simulationCount = 0; // Simulation counter

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Create a Scanner object to read user input

        System.out.println("Barbering Shop Simulation"); // Print the simulation title
        System.out.println("Press SPACE to simulate an event, or any other key to exit."); // Print instructions

        while (true) {
            System.out.print("Enter input: "); // Prompt the user for input
            char input = scanner.nextLine().charAt(0); // Read the first character of the user input
            if (input != ' ') { // If the input is not a space character
                break; // Exit the loop
            }

            simulationCount++; // Increment the simulation counter

            int event = (int) (Math.random() * 4); // Generate a random event (0-3)
            handleEvent(event); // Handle the event
            displayShopState(); // Display the current state of the shop
        }
    }

    private static void handleEvent(int event) {
        // Handle different events based on the value of 'event'
        switch (event) {
            case 0: // If the event is 0 (barber is done)
                handleBarberDone(); // Handle the barber done event
                break;
            case 1: // If the event is 1 (new VIP customer)
                handleNewCustomer(true); // Handle the new customer event with isVIP = true
                break;
            case 2: // If the event is 2 or 3 (new ordinary customer)
            case 3:
                handleNewCustomer(false); // Handle the new customer event with isVIP = false
                break;
        }
    }

    private static void handleBarberDone() {
        if (currentCustomer != null) { // If there is a customer in the main chair
            System.out.println("Barber is done trimming the hair of " + currentCustomer); // Print a message
            currentCustomer = null; // Set the current customer to null
            serveNextCustomer(); // Dequeue from the front of the queue
        }
    }

    private static void handleNewCustomer(boolean isVIP) {
        String name;
        if (currentCustomer == null) { // If the main chair is empty
            name = isVIP ? "VIP" + vipCount++ : "ORD" + ordCount++; // Generate a customer name
            currentCustomer = name; // Assign the new customer to the main chair
            System.out.println(name + " is seated in the main chair"); // Print a message
        } else {
            int emptyChairIndex = -1;
            for (int i = 0; i < MAX_WAITING_CHAIRS; i++) { // Loop through the waiting chairs
                if (waitingChairs[i] == null) { // If the chair is empty
                    emptyChairIndex = i; // Store the index of the empty chair
                    break; // Exit the loop
                }
            }

            if (emptyChairIndex != -1) { // If an empty waiting chair was found
                name = isVIP ? "VIP" + vipCount++ : "ORD" + ordCount++; // Generate a customer name
                if (isVIP) { // If the new customer is a VIP
                    shiftCustomersBack(emptyChairIndex); // Shift customers backward to make room for the VIP
                    waitingChairs[0] = name + " (VIP)"; // Enqueue the VIP at the front of the queue
                    System.out.println(name + " (VIP) is seated in the first waiting chair"); // Print a message
                } else { // If the new customer is an ordinary customer
                    waitingChairs[emptyChairIndex] = name; // Enqueue the ordinary customer at the end of the queue
                    System.out.println(name + " is seated in a waiting chair"); // Print a message
                }
            } else { // If all waiting chairs are occupied
                name = isVIP ? "VIP" + vipCount : "ORD" + ordCount; // Generate a customer name
                System.out.println(name + " has arrived, but all seats are occupied. They left the shop."); // Print a message
            }
        }
    }

    private static void serveNextCustomer() {
        if (waitingChairs[0] != null) { // If the first waiting chair is occupied
            currentCustomer = waitingChairs[0]; // Dequeue from the front of the queue
            System.out.println(currentCustomer + " is now seated in the main chair"); // Print a message
            shiftCustomersForward(); // Shift remaining customers forward
        }
    }

    private static void shiftCustomersBack(int emptyChairIndex) {
        for (int i = emptyChairIndex; i > 0; i--) { // Loop from the empty chair index to the first waiting chair
            waitingChairs[i] = waitingChairs[i - 1]; // Shift the customers back
        }
        waitingChairs[0] = null; // Make room for the VIP at the front
    }

    private static void shiftCustomersForward() {
        for (int i = 0; i < MAX_WAITING_CHAIRS - 1; i++) { // Loop from the first waiting chair to the second-to-last waiting chair
            waitingChairs[i] = waitingChairs[i + 1]; // Shift the customers forward
        }
        waitingChairs[MAX_WAITING_CHAIRS - 1] = null; // Remove the last customer from the queue
    }

    private static void displayShopState() {
        System.out.println("\nSimulation " + simulationCount + ":"); // Print the simulation count
        System.out.print("Customers in the shop: ");
        if (currentCustomer != null) { // If there is a customer in the main chair
            System.out.print(currentCustomer + " (Main Chair)"); // Print the customer name and "Main Chair"
        } else {
            System.out.print("Main Chair is empty"); // Print that the main chair is empty
        }

        boolean hasWaitingCustomers = false;
        for (String customer : waitingChairs) { // Loop through the waiting chairs
            if (customer != null) { // If the chair is occupied
                if (!hasWaitingCustomers) { // If this is the first occupied waiting chair
                    System.out.print(", Waiting Chairs: "); // Print the "Waiting Chairs:" label
                    hasWaitingCustomers = true; // Set the flag to indicate there are waiting customers
                }
                System.out.print(customer + " "); // Print the customer name
            }
        }

        System.out.println(); // Print a newline character
    }
}