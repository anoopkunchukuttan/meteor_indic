import java.util.Arrays;

public class PrintEnv {
	public static void main(String[] args) {
		System.out.println("System Properties:");
		String[] keys = System.getProperties().keySet().toArray(new String[0]);
		Arrays.sort(keys);
		for (String key : keys) {
			System.out.println(key + " : " + System.getProperty(key));
		}
		System.out.println();
		System.out.println("System Environment:");
		keys = System.getenv().keySet().toArray(new String[0]);
		Arrays.sort(keys);
		for (String key : keys) {
			System.out.println(key + " : " + System.getenv(key));
		}
	}
}
