import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Main {
	public static void main(String[] args) {
		ClassB classB = new ClassB();
		System.out.println(classB.whereIsMyJoke());

		ClassD classD = new ClassD();
		System.out.println(classD.tellMeJoke());

		ClassA classA = new ClassA();
		System.out.println(classA.whereIsMyJoke());

		HttpClientWrapper httpClientWrapper = new HttpClientWrapper(HttpClients.createDefault());
	}
}
