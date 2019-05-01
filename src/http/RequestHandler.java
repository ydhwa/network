package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

// 요청을 처리하는 것
public class RequestHandler extends Thread {
	// 서비스 하려는 사이트의 최상위 디렉터리
	//private static final String DOCUMENT_ROOT = "./webapp";
	private static String documentRoot = "";
	// 클래스가 로드될 때 이 블럭 안의 내용이 실행됨
	static {
		documentRoot = RequestHandler.class.getClass().getResource("/webapp").getPath();
		
		InputStream is = RequestHandler.class.getClass().getResourceAsStream("/webapp/index.html");
		System.out.println("----->" + documentRoot);
	}
	
	private Socket socket;
	private String request;

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

			request = null;
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
			} else {	// POST, PUT, DELETE, HEAD, CONNECT와 같은 Method들은 무시한다.
				// 잘못된 요청(400)
				responseError(os, tokens[2], 400, "Bad Request");
				consoleLog("Bad Request: " + request);
			}
			
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
		
		File file = new File(documentRoot + url);
		if(!file.exists()) {	// 별도의 예외처리 없이 깔끔하게 파일의 존재유무를 확인한다.
			// 요청 리소스가 존재하지 않음(404)
			responseError(os, protocol, 404, "File Not Found");
			consoleLog("Bad Request: " + request);
			return;
		}
		
		// nio
		// 읽어서 네트워크에다가 뿌릴 것이므로 byte로 읽는다.
		// 예외처리는 throws를 사용한다.(예외처리 회피) responseStaticResource를 호출하는 쪽에서 예외를 처리해줘야 한다.
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());
		
		// 응답
		os.write( (protocol + " 200 OK\r\n").getBytes( "UTF-8" ) );
		os.write( ("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes( "UTF-8" ) );	// 여기까지가 response 헤더
		os.write( "\r\n".getBytes() );	// 빈 개행. 헤더와 콘텐츠를 나눔
		os.write( body );
	}

	// 에러 페이지 띄움
	private void responseError(OutputStream os, String protocol, int errorCode, String errorMessage) throws IOException {
		// 에러 페이지를 띄우는 것이므로 파일이 존재하지 않을 경우의 처리는 하지 않음
		/* 응답 예시
		 * 
		 * HTTP/1.1 [errorCode] [errorMessage]\r\n
		 * Content-Type:text/html; charset=utf-8\r\n
		 * \r\n
		 * HTML 에러 문서(./webapp/error/[errorCode].html)
		 */
		File file = new File(documentRoot + "/error/" + errorCode + ".html");
		
		// nio
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());
		
		os.write( (protocol + " " + errorCode + " " + errorMessage + "\r\n").getBytes( "UTF-8" ) );
		os.write( ("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes( "UTF-8" ) );	// 여기까지가 response 헤더
		os.write( "\r\n".getBytes() );	// 빈 개행. 헤더와 콘텐츠를 나눔
		os.write( body );
	}

	// 로그 출력
	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}
