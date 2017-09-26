

import java.io.File;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class ShopService3 {
//	EventQueue<Server> idleServers = new EventQueue<>();
	PriorityQueue<Server> idleServers = new PriorityQueue();
	static int noOfServers; // no of serving servers
	static Server[] servers;
	static int servCounter = 1;
	int index = 0;
	double efficiency;
	double currentTime = 0;

	// statistics
	int numberOfCustomerServed;
	double lastCustomerServiceFinishTime;
	int numberOfCustomersBeingServed;
	int maxNumberOfCustomersServedSimultaneously;
	int totalCustomerQueueLength;
	int customerQueueMaxLength;
	Customer[] customers2 = new Customer[10000];
	LinkedList<Customer> customerFIFOQueue = new LinkedList();
//	EventQueue<Event> eventQueue = new EventQueue<>();
	PriorityQueue<Event> eventQueue = new PriorityQueue();

	boolean anyServerBusy() {
		for (Server server : servers) {
			if (server.currentCustomer != null)
				return true;
		}
		return false;
	}

	public void startShop() {
		readFile();

		int i = 0;
		if (i <= numberOfCustomerServed) {
			Customer nextCustomer = customers2[i++];
			eventQueue.add(new Event(EventType.ARRIVAL, nextCustomer.arrivalTime, nextCustomer.customerId, -1));
		}
		double eventTimeTest = 0;
		while (!eventQueue.isEmpty()) {
			Event nextEvent = eventQueue.remove();
			System.out.println("diff - id " + nextEvent.customerIndex + " " + (nextEvent.timeStamp - eventTimeTest));
			eventTimeTest = nextEvent.timeStamp;
			switch (nextEvent.eventType) {
			case ARRIVAL:
				Customer customer = customers2[nextEvent.customerIndex];
				currentTime = customer.arrivalTime;
				System.out.println("ARRIVAL:" + currentTime);
				customerFIFOQueue.addFirst(customer);
				if (i < numberOfCustomerServed) {
					Customer nextCustomer = customers2[i++];
					eventQueue.add(
							new Event(EventType.ARRIVAL, nextCustomer.arrivalTime, nextCustomer.customerId, -1));
				}
				if (customerFIFOQueue.size() > customerQueueMaxLength)
					customerQueueMaxLength = customerFIFOQueue.size();
				totalCustomerQueueLength += customerFIFOQueue.size();
				break;
			case FINISH:
				currentTime = nextEvent.timeStamp;

				System.out.println("FINISH:" + currentTime);
				ProcessFinishedServer(nextEvent);
				break;
			}
			while (!customerFIFOQueue.isEmpty() && !idleServers.isEmpty()) {
				Customer customer = customerFIFOQueue.removeLast();
				Server server = idleServers.remove();
				customer.allocate(server, currentTime); // order is important
														// here call
				// allocate before server
				server.serve(customer);
				// add finish event to event queue
				eventQueue.add(
						new Event(EventType.FINISH, customer.completionTime, customer.customerId, server.serverId));
			}
		} // while (!eventQueue.isEmpty());
		assert (idleServers.isEmpty());
	}

	private void ProcessFinishedServer(Event nextEvent) {
		Server server = servers[nextEvent.serverId];
		idleServers.add(server);
		// server.currentCustomer = null;
		lastCustomerServiceFinishTime = nextEvent.timeStamp;
		System.out.println("lastCustomerServiceFinishTime " + lastCustomerServiceFinishTime);
	}

	private void readFile() {
		Scanner file = null;
		// String filename;

		// Read File
		try {
			// Scanner readFilename = new Scanner(System.in);
			// System.out.println("Enter file name");
			// filename = readFilename.next();
			file = new Scanner(new File("2.txt"));
			noOfServers = Integer.valueOf(file.nextLine().trim());
			// System.out.println("Servers is " + noOfServers);
			servers = new Server[noOfServers]; // create the servers

			for (int i = 0; i < noOfServers; i++) {
				double efficiency = Double.valueOf(file.nextLine().trim());
				servers[i] = new Server(i, efficiency);
				idleServers.remove(servers[i]);
			}
			int customerId = 0;
			while (file.hasNextLine()) {
				// Get arrival time and Finish Time
				String customerData = file.nextLine().trim();
				String[] customerEvent = customerData.split("\\s+", 2);
				double arrivalTime = Double.valueOf(customerEvent[0]);
				double serviceTime = Double.valueOf(customerEvent[1]);
				customers2[customerId] = new Customer(customerId, arrivalTime, serviceTime);
				customerId++;
			}
			numberOfCustomerServed = customerId;
			// eventQueue.enqueue(new Event(EventType.ARRIVAL, arrivalTime,
			// customerId++, -1));
		} catch (Exception e) {
		}
	}

	public void printStatistics() {
		System.out.println("1. The number of customers served " + numberOfCustomerServed);
		System.out.println("-------------------------------------------------------------------------");
		System.out.println(
				"2. The time at which the last customer completed service " + this.lastCustomerServiceFinishTime);
		System.out.println("-------------------------------------------------------------------------");
		System.out.println("3. The greatest length reached by the queue. " + customerQueueMaxLength);
		System.out.println("-------------------------------------------------------------------------");
		System.out.println(
				"4. The average length of the queue." + ((double) totalCustomerQueueLength) / (numberOfCustomerServed));
		System.out.println("-------------------------------------------------------------------------");
		double totalWaitTime = 0;
		for (int i = 0; i < numberOfCustomerServed; i++) {
			totalWaitTime += customers2[i].waitingTime;
		}
		System.out.println(
				"5. The average time spent by a customer in the queue. " + totalWaitTime / numberOfCustomerServed);
		System.out.println("-------------------------------------------------------------------------");
		System.out.println("6.a. The number of customers serverd by server");
		for (int i = 0; i < servers.length; i++) {
			System.out.println("serverId : " + servers[i].serverId + " Efficiency " + servers[i].efficiency
					+ " CustomerCount : " + servers[i].customerCount);
		}
		System.out.println("-------------------------------------------------------------------------");
		System.out.println("6.b. The time server spent idle.");
		for (int i = 0; i < servers.length; i++) {
			System.out.println("serverId : " + servers[i].serverId + " Efficiency " + servers[i].efficiency
					+ " Idle Time : " + servers[i].idleTime);
		}
	}

	public static void main(String[] args) {
		ShopService2 s = new ShopService2();
		s.startShop();
		s.printStatistics();
	}
}
