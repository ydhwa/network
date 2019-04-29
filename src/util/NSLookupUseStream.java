package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Stream을 사용해서 구현한 nslookup 명령어 기능을 하는 프로그램.
 * nslookup이란 도메인 이름의 호스트의 IP 주소를 검색할 수 있고 네임 서버가 올바르게 작동하는지 확인할 수 있는 명령어이다.
 * @author ydh
 *
 */
public class NSLookupUseStream {

	public static void main(String[] args) {
		BufferedReader br = null;
		InputStreamReader isr;
		try {
			isr = new InputStreamReader(System.in, "UTF-8");
			br = new BufferedReader(isr);

			String hostname = null;
			System.out.printf("> ");
			while((hostname = br.readLine()) != null) {
				if("exit".equalsIgnoreCase(hostname)) {
					System.out.println("NSLookup 프로그램이 종료됩니다.");
					break;
				}

				try {
					InetAddress[] inetAddresses = InetAddress.getAllByName(hostname);
					for(InetAddress addr: inetAddresses) {
						System.out.printf("%s: %s\n", hostname, addr.getHostAddress());
					}
				} catch (UnknownHostException e) {
					System.out.printf("ERROR: 알 수 없는 호스트입니다.\n");
				}
				System.out.printf("> ");
			}
		} catch (IOException e) {
			System.out.printf("ERROR: %s\n", e);
		} finally {
			if(br != null) {
				try {
					br.close();	// close()는 자동으로 flush()를 호출함
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
