package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 사용자가 빈 줄("")을 입력하면 오늘 날짜와 현재 시각(24시간제)을 응답해주는 클래스.
 * 나머지는 Echo Server와 동일하다.
 * @author ydh
 *
 */
public class UDPTimeServer {
	public static final int PORT = 7000;
	public static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		DatagramSocket socket = null;
		
		try {
			// 1. socket 생성
			socket = new DatagramSocket(PORT);
			
			while(true) {
				// 2. 데이터 수신
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receivePacket);
				
				byte[] data = receivePacket.getData();
				int length = receivePacket.getLength();
				
				String message = new String(data, 0, length, "utf-8");
				System.out.println("[server] received: " + message);
				
				/* [과제]
				 * 클라이언트가 ""라고 전송했을 때, (그냥 엔터 쳤을 때라고 해석함)
				 * 서버는 "yyyy-MM-dd hh:mm:ss a" 형식으로 클라이언트에게 시간을 제공해줘야 한다.
				 */
				if("".equals(message)) {
					// 시간 생성은 Calendar 클래스를 사용한다.
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
					message = format.format(calendar.getTime());
				}
				
				// 3. 데이터 전송
				byte[] sendData = message.getBytes("utf-8");
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
