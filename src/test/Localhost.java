package test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// localhost 확인
public class Localhost {

	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			
			String hostname = inetAddress.getHostName();
			String hostAddress = inetAddress.getHostAddress();
			System.out.println(hostname + " : " + hostAddress);
			
			byte[] addresses = inetAddress.getAddress();
			for(int i = 0; i < addresses.length; i++) {
				// addresses[i] & 0x000000ff
				System.out.print((addresses[i] & 0xff) + (i < addresses.length - 1 ? "." : "\n"));
			}
			
			System.out.println("----------------------------");

//			// 방법 1
//			InetAddress[] inetAddresses = InetAddress.getAllByName(hostname);
//			for(InetAddress addr: inetAddresses) {
//				if(!addr.isLinkLocalAddress()) {
//					System.out.println(addr.getHostAddress());
//				}
//			}

			// 방법2
			for(String IPAddress: hostIPAddresses()) {
				System.out.println(IPAddress);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} 
	}
	
	public static List<String> hostIPAddresses() throws SocketException {
		List<String> result = new ArrayList<>();
		
		for(NetworkInterface ifc: Collections.list(NetworkInterface.getNetworkInterfaces())) {
			// 활성화 된 NIC이 아니면 무시
			if(!ifc.isUp()) {
				continue;
			}
			// Loopback(127.0.0.1)이면 무시
			if(ifc.isLoopback()) {
				continue;
			}
			
//			// virtual box 깔아서 안되는 것 같다고 함
//			if(ifc.isVirtual()) {
//				continue;
//			}
			
			for(InetAddress inetAddr: Collections.list(ifc.getInetAddresses())) {
				// Link Address(MAC Address 무시)
				if(inetAddr.isLinkLocalAddress()) {
					continue;
				}
				result.add(inetAddr.getHostAddress());
			}
		}
		return result;
	}
}
