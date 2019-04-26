package io;

import java.io.File;
import java.util.Scanner;

public class PhoneList02 {
	
	public static void main(String[] args) {
		Scanner scanner = null;
		
		try {
			scanner = new Scanner(new File("phone.txt"));
			
			while(scanner.hasNextLine()) {
//				String name = scanner.next();
//				String phone01 = scanner.next();
//				String phone02 = scanner.next();
//				String phone03 = scanner.next();
//				
//				System.out.println(name + ": " + phone01 + "-" + phone02 + "-" + phone03);
				
				System.out.printf("%s: %s-%s-%s\n", scanner.next(), scanner.next(), scanner.next(), scanner.next());
			}
		} catch (Exception e) {	// 원래는 모든 에러에 대해서 catch로 처리해줘야 함
			System.out.println("error: " + e);
		} finally {
			if(scanner != null) {
				scanner.close();
			}
		}
	}
}
