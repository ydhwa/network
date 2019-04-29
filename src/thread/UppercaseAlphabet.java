package thread;

public class UppercaseAlphabet {

	// 직접 수정을 가하지 말고 OCP 원칙을 통해서 확장해보자.
	public void print() {
		for(char c = 'A'; c <= 'Z'; c++) {
			System.out.print(c);
		}
	}

}
