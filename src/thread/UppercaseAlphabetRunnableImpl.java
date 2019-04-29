package thread;

public class UppercaseAlphabetRunnableImpl extends UppercaseAlphabet implements Runnable {

	// 상속과 인터페이스만으로 구현함. 이전 예제와는 다른 방식으로 thread를 돌려야 함
	@Override
	public void run() {
		print();
	}

}
