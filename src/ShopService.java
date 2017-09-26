

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ShopService {
	// List<Server> activeServers = new ArrayList<>(); // TODO
	// static Server[] activeServer;
	EventQueue1<Server> idleServers = new EventQueue1<>();
	static int noOfServers; // no of serving servers
	static Server[] servers;
	static int servCounter = 1;
	int index = 0;
	double efficiency;

	// statistics
	int numberOfCustomerServed;
	double lastCustomerServiceFinishTime;
	int numberOfCustomersBeingServed;
	int maxNumberOfCustomersServedSimultaneously;
	int totalCustomerQueueLength;
	int customerQueueMaxLength;
	ArrayList<Customer> customers = new ArrayList<>();
	QueueImpl<Customer> customerFIFOQueue = new QueueImpl<>(20000);
	EventQueue1<Event> eventQueue = new EventQueue1<>();

	public void startShop() {
		readFile();

		// Create mEventQueue with all customers;
		// Iterator<Customer> customerIterator = customers.iterator();

		// System.out.println("Time , Server: %d| 1| 2| 3| 4| 5|Queue Length");

		Event nextEvent;
		while (!(eventQueue.isEmpty())) {
			nextEvent = eventQueue.dequeue();
			// if(nextEvent.eventType == EventType.FINISH)
			// {
			// int id = servers[nextEvent.serverId].currentCustomer.customerId;
			// servers[nextEvent.serverId].currentCustomer.customerId = -1;
			//
			// System.out.printf("%10f , %4d|%4d|%4d|%4d|%4d|%4d | (%d|%10f) ",
			// nextEvent.timeStamp,
			// servers[0].currentCustomer.customerId,
			// servers[1].currentCustomer.customerId,
			// servers[2].currentCustomer.customerId,
			// servers[3].currentCustomer.customerId,
			// servers[4].currentCustomer.customerId,
			// customerFIFOQueue.currentSize,
			// id,
			// (nextEvent.timeStamp-servers[nextEvent.serverId].currentCustomer.allocationTime)/servers[nextEvent.serverId].efficiency
			// );
			// } else {
			// System.out.printf("%10f , %4d|%4d|%4d|%4d|%4d|%4d ",
			// nextEvent.timeStamp,
			// servers[0].currentCustomer.customerId,
			// servers[1].currentCustomer.customerId,
			// servers[2].currentCustomer.customerId,
			// servers[3].currentCustomer.customerId,
			// servers[4].currentCustomer.customerId,
			// customerFIFOQueue.currentSize
			// );
			// }
			// System.out.println(nextEvent.eventType);

			switch (nextEvent.eventType) {
			case ARRIVAL:
				Customer customer = customers.get(nextEvent.customerIndex);
				// Check Server Queue
				if (idleServers.isEmpty()) {
					// System.out.println(customer.customerId);
					customerFIFOQueue.enqueue(customer);
					totalCustomerQueueLength += customerFIFOQueue.size();
					if (customerFIFOQueue.size() > customerQueueMaxLength)
						customerQueueMaxLength = customerFIFOQueue.size();
					break;
				} else {
					assert (customerFIFOQueue.currentSize == 0);
					Server server = idleServers.dequeue();
					// activeServers.add(server);
					// activeServer[server.c];
					//customer.allocate(server); // order is important here call
												// allocate before server
					server.serve(customer);
					// add finish event to event queue
					eventQueue.enqueue(
							new Event(EventType.FINISH, customer.completionTime, customer.customerId, server.serverId));
				}
				break;

			case FINISH:
				// find which customer finished
				// remove fn
				// ProcessFinishedCustomer(customers.get(nextEvent.customerIndex));
				ProcessFinishedServer(servers[nextEvent.serverId]);
				// which server finished
				break;
			}
			totalCustomerQueueLength += customerFIFOQueue.size();
		}
		// assert (activeServers.isEmpty());
		assert (idleServers.isEmpty());
	}

	private void ProcessFinishedServer(Server server) {
		// check FIFO queue for next customer
		if (!customerFIFOQueue.isQueueEmpty()) {
			// put server to idle servers
			Customer customer = customerFIFOQueue.dequeue();
			//customer.allocate(server); // order is important here call
			// allocate before server
			server.serve(customer);
			// add finish event to event queue
			eventQueue.enqueue(
					new Event(EventType.FINISH, customer.completionTime, customer.customerId, server.serverId));
		} else {
			idleServers.enqueue(servers[server.serverId]);
			lastCustomerServiceFinishTime = server.serviceFinishTimeStamp;
		}

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
				idleServers.enqueue(servers[i]);
			}
			int customerId = 0;
			while (file.hasNext()) {
				// Get arrival time and Finish Time
				String customerData = file.nextLine().trim();
				String[] customerEvent = customerData.split("\\s+", 2);
				double arrivalTime = Double.valueOf(customerEvent[0]);
				double serviceTime = Double.valueOf(customerEvent[1]);
				customers.add(new Customer(customerId, arrivalTime, serviceTime));
				eventQueue.enqueue(new Event(EventType.ARRIVAL, arrivalTime, customerId++, -1));
			}
		} catch (Exception e) {
		}
	}

	public void printStatistics() {
		System.out.println("1. The number of customers served " + customers.size());
		System.out.println("-------------------------------------------------------------------------");
		System.out.println(
				"2. The time at which the last customer completed service " + this.lastCustomerServiceFinishTime);
		System.out.println("-------------------------------------------------------------------------");
		System.out.println("3. The greatest length reached by the queue. " + customerQueueMaxLength);
		System.out.println("-------------------------------------------------------------------------");
		System.out.println(
				"4. The average length of the queue." + ((double) totalCustomerQueueLength) / (2 * customers.size()));
		System.out.println("-------------------------------------------------------------------------");
		double totalWaitTime = 0;
		for (int i = 0; i < customers.size(); i++) {
			totalWaitTime += customers.get(i).waitingTime;
		}
		System.out.println("5. The average time spent by a customer in the queue. " + totalWaitTime / customers.size());
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
		ShopService s = new ShopService();
		s.startShop();
		s.printStatistics();
	}
}
