package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		while(true) {
			System.out.print("> ");
			String hostname = input.nextLine();
			if(hostname.equalsIgnoreCase("exit")) {
				break;
			}
			
			InetAddress[] inetAddresses;
			try {
				inetAddresses = InetAddress.getAllByName(hostname);
				for(InetAddress addr: inetAddresses) {
					System.out.println(hostname + ": " + addr.getHostAddress());
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		
		input.close();
	}
}
