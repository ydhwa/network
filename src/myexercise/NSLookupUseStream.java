package myexercise;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NSLookupUseStream {
	
	public static void main(String[] args) {
		BufferedReader br = null;
		try {
			InputStreamReader isr = new InputStreamReader(System.in, "UTF-8");
			br = new BufferedReader(isr);
			
			while(true) {
				System.out.print("> ");
				String hostname = br.readLine();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
