package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

	/*
	 * Java에는 상수라는 개념이 없다.
	 * final을 그나마 상수처럼 쓰기는 하는데, 엄연히 변수임.
	 * 여기에 마지막으로 지정하는 값이 지정된 값이라는 의미임. = 이 이후에는 값을 대입하지 말라는 의미
	 */
	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 7000;

	// 메소드에 final이 들어가면? -> 이 함수가 마지막이라는 의미. Override 하지 말라는 의미
	public static void main(String[] args) {
		Socket socket = null;
		Scanner scanner = null;
		/*
		 * try-catch 난무하는 코드에서 가독성 높이는 방법
		 * try를 맨 위에 한 번만 선언하여 처리한다.
		 */
		try {
			// 1. Scanner 생성(표준입력 연결)
			scanner = new Scanner(System.in);
			// 2. 소켓 생성
			socket = new Socket();

			// 3. 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			log("connected");
			
			// 4. IOStream 받아오기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			PrintWriter pr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true); // auto flush => true
			
			while(true) {
				// 5. 키보드 입력 받기
				System.out.print(">>");
				String line = scanner.nextLine();
				if("quit".contentEquals(line)) {
					break;
				}
				
				// 6. 데이터 쓰기
				pr.println(line);
				
				// 7. 데이터 읽기
				String data = br.readLine();
				if(data == null) {
					log("closed by server");
					break;
				}
				
				// 8. 콘솔 출력
				System.out.println("<<" + data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(scanner != null) {	// 굳이 안해도 자동으로 닫히긴 함
					scanner.close();
				}
				if(socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void log(String log) {
		System.out.println("[client] " + log);
	}
}
