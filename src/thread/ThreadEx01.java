package thread;

public class ThreadEx01 {

	public static void main(String[] args) {
//		for(int i = 1; i <= 10; i++) {
//			System.out.print(i);
//		}
		
		Thread digitThread = new DigitThread();
		
		digitThread.start();
		
	}

}
