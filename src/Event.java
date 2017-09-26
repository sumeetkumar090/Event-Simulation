

public class Event implements Comparable<Event>{

	EventType eventType;
	double timeStamp;
	int customerIndex;
	int serverId; // -1 to denote invalid server or unassigned

	Event(EventType eventType, double timeStamp, int customerId, int serverId ) {
		this.eventType = eventType;
		this.timeStamp = timeStamp;
		this.customerIndex = customerId;
		this.serverId = serverId;
	}

	public int compareTo(Event event) {
		// TODO Auto-generated method stub
		if (this.timeStamp > event.timeStamp)
			return -1;
		else if (this.timeStamp == event.timeStamp)
			return 0;
		else return 1;
	}

}
