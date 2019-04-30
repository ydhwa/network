package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			// 1. 서버 소켓 생성
			serverSocket = new ServerSocket();
			
			// 1-1. Time-Wait 시간에 소켓에 포트번호 할당을 가능하게 하기 위해서
			serverSocket.setReuseAddress(true);
			
			// 2. 바인딩(binding)
			// Socket에 SocketAddress(IPAddress + Port)를 바인딩한다.
//			InetAddress inetAddress = InetAddress.getLocalHost();
//			String localhost = inetAddress.getHostAddress();
//			serverSocket.bind(new InetSocketAddress(localhost, 5000));
//			serverSocket.bind(new InetSocketAddress(inetAddress, 5000));
			serverSocket.bind(new InetSocketAddress("0.0.0.0", 5000));	// 버추얼박스 호스트용, 이더넷 둘 다 됨
			
			// 3. accept
			// 클라이언트에 연결요청을 기다린다.
			Socket socket = serverSocket.accept();	// blocking됨
			
			// Peer 쪽의 정보를 얻어옴
			// down casting이므로(반환타입이 부모인데 자식 객체로 내야 하는 상황) 강제 형변환을 시켜 불러낸다.
			InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			String remoteHostAddress = inetRemoteSocketAddress.getAddress().getHostAddress();
			int remotePort = inetRemoteSocketAddress.getPort();
			
			// 확인은 XShell로. telnet [IP주소] [포트번호]
			System.out.printf("[server] connected by client[%s:%d]\n", remoteHostAddress, remotePort);
			
			try {
				// 4. IOStream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
			
				while(true) {
					// 5. 데이터 읽기
					byte[] buffer = new byte[256];	// 데이터 통신은 byte임. 나중에 프로그래밍 할때는 버퍼 사이즈 신경 안써도 됨
				
					int readByteCount = is.read(buffer);	// blocking
					if(readByteCount == -1) {
						// 클라이언트가 정상종료한 경우. 갑자기 끈게 아니라 close를 사용하여 종료함. 우아한 종료(FIN Sync)
						// 그냥 갑자기 꺼버리면(=연결이 사라지면) Socket Exception이 발생함
						// close() 메소드 호출
						System.out.println("[server] closed by client");
						break;
					}
					
					// buffer중에 0부터 readByteCount만큼만 UTF-8로 인코딩해주라
					// telnet으로 짜면 개행이 하나 더 감
					String data = new String(buffer, 0, readByteCount, "UTF-8");
					System.out.printf("[server] received: %s\n", data);
					
					// 6. 데이터 쓰기
					// echo server
					// PrintWriter를 사용하면 개행 버리기 쉬움 (writeln이 있기 때문). 나중에 과제로는 여기에 멀티스레드 적용한것 구현.
					// 두 개 열어두면 동작이 되지 않는다.
					try {	// 지연을 걸어 SO_TIMEOUT 확인해 봄
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					os.write(data.getBytes("UTF-8"));
				}
			} catch(SocketException e) {	// SocketException도 IOException 단에서 처리가 되어버리므로 SocketException이 우선 나와야 함
				System.out.println("[server] sudden closed by client");
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(socket != null && !socket.isClosed()) {
						socket.close();
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {	// 잘 발생하지 않는 오류
			e.printStackTrace();
		} finally {
			try {
				if(serverSocket != null && !serverSocket.isClosed()) {	// 닫은 소켓을 또 닫으면 에러 생김
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
