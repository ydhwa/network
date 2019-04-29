package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while(true) {
			try {
				System.out.print("> ");
				String hostname = scanner.nextLine();
				if("exit".equalsIgnoreCase(hostname)) {
					break;
				}
			
				InetAddress[] inetAddresses;
				inetAddresses = InetAddress.getAllByName(hostname);
				for(InetAddress addr: inetAddresses) {
					System.out.println(hostname + ": " + addr.getHostAddress());
				}
			} catch (UnknownHostException e) {
				System.out.println("알 수 없는 호스트입니다.");
			}
		}
		
		scanner.close();
	}
}
