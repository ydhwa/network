package thread;

public class AlphabetThread extends Thread {

	@Override
	public void run() {
		// for 루프는 선점이 잘 되지 않음
		for(char c = 'a'; c <= 'z'; c++) {
			System.out.print(c);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
