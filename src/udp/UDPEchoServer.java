package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPEchoServer {
	public static final int PORT = 7000;
	public static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		// accept를 할 필요가 없으므로 server socket도 없다.
		DatagramSocket socket = null;
		try {
			// 1. socket 생성
			socket = new DatagramSocket(PORT);
			
			while(true) {
				// 2. 데이터 수신
				// 전달되는 패킷을 복사해야 한다. 패킷 안에는 버퍼가 있어야 한다.
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receivePacket);
				
				byte[] data = receivePacket.getData();
				int length = receivePacket.getLength();
				String message = new String(data, 0, length, "UTF-8");
				
				System.out.println("[server] received: " + message);
				
				// 3. 데이터 전송
				byte[] sendData = message.getBytes("utf-8");
				// 패킷에는 받는 대상의 주소가 들어 있다.
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
				socket.send(sendPacket);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(socket != null && !socket.isClosed()) {
				socket.close();
			}
		}

	}

}
