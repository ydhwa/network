package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KeyboardTest {

	public static void main(String[] args) {	
		BufferedReader br = null;
	
		try {
			// 기반 스트림(=주 스트림. 표준 입력, 키보드, System.in).
			// JVM이 준비해주므로 따로 준비할 필요 없다.
			
			// 보조스트림 1
			// byte|byte|byte -> char
			InputStreamReader isr = new InputStreamReader(System.in, "utf-8");
			
			// 보조스트림 2
			// char1|char2|char3|\n -> char1char2char3
			br = new BufferedReader(isr);
			
			// read
			String line = null;
			while((line = br.readLine()) != null) {
				if("exit".equals(line)) {
					break;
				}
				System.out.println(">> " + line);
			}
			
		} catch (IOException e) {
			System.out.println("error: " + e);
		} finally {
			// 최상위 buffer만 해제해주면 됨
			try {
				if(br != null) {
					br.close();
				}
			} catch (IOException e) {
				System.out.println("error: " + e);
			}
		}
	}
}
