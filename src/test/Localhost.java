package test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;

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
			
//			// if문 없으면 MAC Address도 같이 나옴
//			InetAddress[] inetAddresses = InetAddress.getAllByName(hostname);
//			for(InetAddress addr: inetAddresses) {
//				if(!addr.isLinkLocalAddress()) {
//					System.out.println(addr.getHostAddress());
//				}
//			}

			// 방법 2(강사님 코드 참고)
			for(NetworkInterface ifc: Collections.list(NetworkInterface.getNetworkInterfaces())) {
				if(ifc.isUp() && !ifc.isVirtual() && !ifc.isLoopback()) {
					for(InetAddress inetAddr: Collections.list(ifc.getInetAddresses())) {
						if(!inetAddr.isLinkLocalAddress()) {
							System.out.println(inetAddr.getHostAddress());
						}
					}
				}
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} 
	}
}
