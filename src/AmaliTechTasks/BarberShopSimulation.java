package AmaliTechTasks;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

// Class representing a customer
class Customer {
    String name;
    boolean isVIP;

    // Method to set customer details
    void setCustomerDetails(String name, boolean isVIP) {
        this.name = name;
        this.isVIP = isVIP;
    }

    // Method to return customer's name
    String getCustomerName() {
        return name;
    }
}

// Main class for the barbershop simulation
public class BarberShopSimulation extends JFrame implements KeyListener {
    // Maximum number of waiting chairs
    private static final int MAX_WAITING_CHAIRS = 5;
    // Array to represent waiting customers
    private static Customer[] waitingQueue = new Customer[MAX_WAITING_CHAIRS];
    // Currently served customer
    private static Customer currentCustomer = null;
    // Counter for ordinary customers
    private static int ordCount = 1;
    // Counter for VIP customers
    private static int vipCount = 1;
    // Counter for simulation events
    private static int simulationCount = 0;
    // Current size of the waiting queue
    private static int queueSize = 0;
    // Random object to generate random events
    private Random random;

    // Constructor
    public BarberShopSimulation() {
        setTitle("Barber Shop Simulation");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        random = new Random();
        initWaitingQueue();
    }

    // Method to initialize waitingQueue with Customer objects
    private void initWaitingQueue() {
        for (int i = 0; i < MAX_WAITING_CHAIRS; i++) {
            waitingQueue[i] = new Customer();
        }
    }

    // Method to handle different events
    private void handleEvent(int event) {
        simulationCount = event; // Assign the random number to simulationCount
        if (event == 0) {
            handleBarberDone(); // Barber finishes with the current customer
        } else if (event == 1) {
            handleNewCustomer(true); // New VIP customer arrives
        } else {
            handleNewCustomer(false); // New ordinary customer arrives
        }
    }

    // Method to handle the barber finishing with a customer
    private void handleBarberDone() {
        if (currentCustomer != null) {
            // Print the event and current state of the shop
            System.out.println(simulationCount + " ---> (--" + currentCustomer.getCustomerName() + ") [" + getCustomerListString() + "]");
            currentCustomer = null; // Barber is now free
            serveNextCustomer(); // Serve the next customer from the queue
        }
    }

    // Method to handle a new customer arriving
    private void handleNewCustomer(boolean isVIP) {
        String customerName = isVIP ? "VIP" + vipCount++ : "ORD" + ordCount++;

        // If barber is free and no waiting customers, serve immediately
        if (currentCustomer == null && queueSize == 0) {
            currentCustomer = new Customer();
            currentCustomer.setCustomerDetails(customerName, isVIP);
        } else if (queueSize < MAX_WAITING_CHAIRS) {
            Customer newCustomer = new Customer();
            newCustomer.setCustomerDetails(customerName, isVIP);
            if (isVIP) {
                // Enqueue VIP customer at the front
                enqueueVIP(newCustomer);
            } else {
                // Enqueue ordinary customer at the end
                enqueue(newCustomer);
            }
        } else {
            // If there are no spaces in the waiting queue, customer leaves without being served
            System.out.println(simulationCount + " ---> (+-" + customerName + ") [" + getCustomerListString() + "]");
            return;
        }

        // Print the state of the shop after adding the new customer
        System.out.println(simulationCount + " ---> (++" + customerName + ") [" + getCustomerListString() + "]");
    }

    // Method to serve the next customer from the queue
    private void serveNextCustomer() {
        if (queueSize > 0) {
            // Dequeue the first customer
            currentCustomer = waitingQueue[0];
            for (int i = 1; i < queueSize; i++) {
                // Shift remaining customers forward
                waitingQueue[i - 1] = waitingQueue[i];
            }
            // Decrease queue size
            queueSize--;
            waitingQueue[queueSize] = null;
        }
    }

    // Method to enqueue a VIP customer at the front of the waiting queue
    private void enqueueVIP(Customer vipCustomer) {
        int insertPos = 0;

        // Insert the VIP customer without shifting other VIPs
        for (int i = 0; i < queueSize; i++) {
            if (!waitingQueue[i].isVIP) {
                insertPos = i;
                break;
            }
            insertPos = i + 1;
        }

        // Shift all non-VIP customers backward to make room for the new VIP
        for (int i = queueSize; i > insertPos; i--) {
            waitingQueue[i] = waitingQueue[i - 1];
        }

        waitingQueue[insertPos] = vipCustomer;
        queueSize++;
    }

    // Method to enqueue an ordinary customer at the end of the waiting queue
    private void enqueue(Customer customer) {
        waitingQueue[queueSize] = customer;
        queueSize++;
    }

    // Method to get a string representation of the current state of the shop
    private String getCustomerListString() {
        StringBuilder result = new StringBuilder();
        if (currentCustomer != null) {
            result.append(currentCustomer.getCustomerName());
        }
        for (int i = 0; i < queueSize; i++) {
            result.append(" ").append(waitingQueue[i].getCustomerName());
        }
        return result.toString();
    }

    // KeyListener methods
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            int event = random.nextInt(4);
            handleEvent(event);
        } else {
            // If any other key is pressed, terminate the program
            System.exit(0);
        }
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BarberShopSimulation simulation = new BarberShopSimulation();
            simulation.setVisible(true);
        });
    }
}