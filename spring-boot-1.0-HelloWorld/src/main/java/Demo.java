
public class Demo {

	public static void main(String[] args) {
		int sum = 0;
		for (int i=1;i<=5;i++) {
			for (int j=1;j<=i;j++) {
				sum = sum+1;
				System.out.println("i:"+i);
				System.out.println("j:"+j);
				System.out.println("sum:"+sum);
			}
		}

	}

}
