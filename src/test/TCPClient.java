package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

// 클래스에 final이 들어가면? -> 이 클래스가 마지막이라는 의미. 상속하지 말라는 의미.
public class TCPClient {
	/*
	 * Java에는 상수라는 개념이 없다.
	 * final을 그나마 상수처럼 쓰기는 하는데, 엄연히 변수임.
	 * 여기에 마지막으로 지정하는 값이 지정된 값이라는 의미임. = 이 이후에는 값을 대입하지 말라는 의미
	 */
	private static final String SERVER_IP = "192.168.56.1";
	private static final int SERVER_PORT = 5000;

	// 메소드에 final이 들어가면? -> 이 함수가 마지막이라는 의미. Override 하지 말라는 의미
	public static void main(String[] args) {
		Socket socket = null;
		/*
		 * try-catch 난무하는 코드에서 가독성 높이는 방법
		 * try를 맨 위에 한 번만 선언하여 처리한다.
		 */
		try {
			// 1. 소켓 생성
			socket = new Socket();

			// 2. 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[client] connected");
			
			// 3. IOStream 받아오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			// 4. 쓰기
			String data = "Hello World\n";	// TCP는 경계가 없기 때문에 응용 단에서 개행(\n)을 만들어 줘야 한다.
			os.write(data.getBytes("utf-8"));
			
			// 5. 읽기
			byte[] buffer = new byte[256];
			int readByteCount = is.read(buffer);	// blocking된다.
			if(readByteCount == -1) {
				System.out.println("[client] closed by server");
			}
			
			// 6. 데이터 쓰기
			data = new String(buffer, 0, readByteCount, "utf-8");
			System.out.println("[client] received: " + data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
