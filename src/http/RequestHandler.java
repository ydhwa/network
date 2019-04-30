package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

// 요청을 처리하는 것
public class RequestHandler extends Thread {
	// 서비스 하려는 사이트의 최상위 디렉터리
	private static final String DOCUMENT_ROOT = "./webapp";
	private Socket socket;

	public RequestHandler( Socket socket ) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			/*
			 * 따로 보조 스트림을 연결하지 않음. 어떤 파일이 들어오든 byte로 처리하기 위함
			 * input쪽은 pipe로 연결해서 사용함
			 */
			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );

			// get IOStream
			OutputStream os = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));

			String request = null;
			while(true) {
				String line = br.readLine();

				// 브라우저가 연결을 끊으면...
				if(line == null) {
					break;
				}

				// Request Header만 읽음
				// 제대로 된 코드라면 이 조건문 이후 body를 읽는 코드가 들어와야 함.
				if("".equals(line)) {
					break;
				}

				// Header의 첫 번째 라인만 처리
				if(request == null) {
					request = line;
				}
			}

			//consoleLog("received: " + request);
			String[] tokens = request.split(" ");
			if("GET".contentEquals(tokens[0])) {
				consoleLog("Request: " + tokens[1]);
				// 정적인 자원을 응답하는 소스
				responseStaticResource(os, tokens[1], tokens[2]);
			} else {	// POST, PUT, DELETE, HEAD, CONNECT
						// 와 같은 Method들은 무시한다.
				consoleLog("Bad Request: " + tokens[1]);
			}

			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
//			os.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
//			os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );	// 여기까지가 response 헤더
//			os.write( "\r\n".getBytes() );	// 빈 개행. 헤더와 콘텐츠를 나눔
//			os.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );
			
		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}

			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}			
	}

	// 응답 해주는 메소드.
	private void responseStaticResource(OutputStream os, String url, String protocol) throws IOException {
		// URL을 따로 명시해주지 않았을 때, 기본 설정으로 지정해준다.
		if("/".equals(url)) {
			url = "/index.html";
		}
		
		File file = new File(DOCUMENT_ROOT + url);
		if(!file.exists()) {	// 별도의 예외처리 없이 깔끔하게 파일의 존재유무를 확인한다.
			/* 응답 예시
			 * 
			 * HTTP/1.1 404 File Not Found\r\n
			 * Content-Type:text/html; charset=utf-8\r\n
			 * \r\n
			 * HTML 에러 문서
			 */
			
//			response404Error(os, protocol);
			return;
		}
		
		// nio
		// 읽어서 네트워크에다가 뿌릴 것이므로 byte로 읽는다.
		// 예외처리는 throws를 사용한다.(예외처리 회피) responseStaticResource를 호출하는 쪽에서 예외를 처리해줘야 한다.
		byte[] body = Files.readAllBytes(file.toPath());
		
		// 응답
		os.write( (protocol + " 200 OK\r\n").getBytes( "UTF-8" ) );
		os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );	// 여기까지가 response 헤더
		os.write( "\r\n".getBytes() );	// 빈 개행. 헤더와 콘텐츠를 나눔
		os.write( body );
	}

	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}
