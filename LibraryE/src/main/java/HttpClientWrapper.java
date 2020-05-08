// The following types can appear anywhere in the code
// but say nothing about API or implementation usage
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HttpClientWrapper {

	private final HttpClient client; // private member: implementation details

	// HttpClient is used as a parameter of a public method
	// so "leaks" into the public API of this component

	/**
	 * 이 프로젝트를 라이브러리로 사용하는 외부 프로젝트 (사용자)는
	 * HttpClientWrapper 인스턴스 생성을 위해서 인자로 HttpClient 객체가 필요함.
	 *
	 * 만약 build.gradle 에서 HttpClient 라이브러리에 대한 dependency 설정을 implementation 으로 한다면
	 * 사용자는 HttpClient 라이브러리에 대한 접근이 불가하므로 HttpClient 객체 생성을 못함.
	 * (별도로 사용자의 build.gradle 설정 파일에 HttpClient 라이브러리를 사용하기 위한 dependency 설정을 해야함.)
	 *
	 * 위 영어 주석에서도 설명하듯이 HttpClient는 사용자도 사용 가능한 API가 되었기 때문에
	 * dependency 설정을 api로 하여 사용자도 HttpClient 라이브러리를 사용할 수 있어야 한다.
	 */
	public HttpClientWrapper(HttpClient client) {
		this.client = client;
	}

	// public methods belongs to your API
	public byte[] doRawGet(String url) {
		HttpGet request = new HttpGet(url);
		try {
			HttpEntity entity = doGet(request);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			entity.writeTo(baos);
			return baos.toByteArray();
		} catch (Exception e) {
			ExceptionUtils.rethrow(e); // this dependency is internal only
		} finally {
			request.releaseConnection();
		}
		return null;
	}

	// HttpGet and HttpEntity are used in a private method, so they don't belong to the API
	private HttpEntity doGet(HttpGet get) throws Exception {
		HttpResponse response = client.execute(get);
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			System.err.println("Method failed: " + response.getStatusLine());
		}
		return response.getEntity();
	}
}