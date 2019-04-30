package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class UDPEchoClient {

	private static final String SERVER_IP = "127.0.0.1";

	public static void main(String[] args) {
		DatagramSocket socket = null;
		Scanner scanner = null;
		try {
			// 1. Scanner 생성(표준입력 연결)
			scanner = new Scanner(System.in);
			
			// 2. 소켓 생성
			socket = new DatagramSocket();
			
			// 2-1. 소켓 버퍼 사이즈 확인
			int receiveBufferSize = socket.getReceiveBufferSize();
			int sendBufferSize = socket.getSendBufferSize();
			System.out.println(receiveBufferSize + ":" + sendBufferSize);
			
			// 2-2. 소켓 버퍼 사이즈 변경
			socket.setReceiveBufferSize(1024 * 10);
			socket.setSendBufferSize(1024 * 10);
			receiveBufferSize = socket.getReceiveBufferSize();
			sendBufferSize = socket.getSendBufferSize();
			System.out.println(receiveBufferSize + ":" + sendBufferSize);

			while(true) {
				// 5. 키보드 입력 받기
				System.out.print(">>");
				String line = scanner.nextLine();
				if("quit".contentEquals(line)) {
					break;
				}

				// 4. 데이터 쓰기
				byte[] sendData = line.getBytes("utf-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, new InetSocketAddress(SERVER_IP, UDPEchoServer.PORT));
				socket.send(sendPacket);

				// 5. 데이터 읽기
				DatagramPacket receivePacket = new DatagramPacket(new byte[UDPEchoServer.BUFFER_SIZE], UDPEchoServer.BUFFER_SIZE);
				socket.receive(receivePacket);
				String message = new String(receivePacket.getData(), 0, receivePacket.getLength(), "utf-8");

				// 6. 콘솔 출력
				System.out.println("<<" + message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(scanner != null) {	// 굳이 안해도 자동으로 닫히긴 함
				scanner.close();
			}
			if(socket != null && !socket.isClosed()) {
				socket.close();
			}
		}
	}

	public static void log(String log) {
		System.out.println("[client] " + log);
	}
}
