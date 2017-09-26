//package assignment2;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//
//class Server {
//
//	int peopleServed;
//	int avgServerCounter;
//	float totalWaitTime;
//	float waitTimeStart;
//	float waitTimeEnd;
//	float avgServiceTime;
//	boolean status;
//	boolean waitTimeFlag;
//
//}
//
//public class Assignment2 {
//	static int ch;
//
//	static int servCounter = 1;
//	static int noOfServ = 0;
//	int numOfServers;
//	Server[] serverArray = new Server[20];
//
//	public static void main(String[] args) {
//		readData();
//	}
//
//	private static void readData() {
//		File file = new File("2.txt");
//		try {
//			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//			String line;
//			while ((line = br.readLine()) != null) {
//				//System.out.println("Data " + line);
//				if (servCounter == 1) {
//					noOfServ = Integer.valueOf(line.trim());
//				}
//				servCounter++;
//
//			}
//			System.out.println("No of Servers " + noOfServ);
//			br.close();
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//}
