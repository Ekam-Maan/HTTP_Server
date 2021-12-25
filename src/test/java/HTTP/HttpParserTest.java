package HTTP;


//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

//import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class HttpParserTest {
	
	public HttpParser httpParser;
	
	@BeforeAll
	public void beforeCalss() {
		httpParser = new HttpParser();
	}

	@Test
	public void parseHttpRequest() {
		//System.out.println("inside test");
		HttpRequest httpRequest =null;
		try {
			httpRequest = httpParser.parseHttpRequest(generateValidGETTestCase());
		} catch (HttpParsingException e) {
			// TODO Auto-generated catch block
			fail(e);
			//e.printStackTrace();
		}
		
		assertNotNull(httpRequest);
		assertEquals(httpRequest.getMethod(), HttpMethod.GET);
		assertEquals(httpRequest.getRequestTarget(), "/");
		assertEquals(httpRequest.getOriginalVersion(), "HTTP/1.1");
		assertEquals(httpRequest.getHttpVersion(), HttpVersion.HTTP_1_1);
	
	}
	
	@Test
	public void parseHttpbadRequest() {
		//System.out.println("inside test");
		
		try {
			HttpRequest httpRequest = httpParser.parseHttpRequest(generateINValidGETTestCase());
			fail();
		} catch (HttpParsingException e) {
			// TODO Auto-generated catch block
			assertEquals(e.errorCode, HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED );
			//e.printStackTrace();
		}
		
	}
	
	@Test
	public void parseHttpbadrequest2() {
		
		try {
			HttpRequest httpRequest = httpParser.parseHttpRequest(generateINValidGETTestCase2());
			fail();
		} catch (HttpParsingException e) {
			// TODO Auto-generated catch block
			assertEquals(e.errorCode, HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED );
			//e.printStackTrace();
		}
	}
	
	@Test
	public void parseHttpInvalidNumofItems() {
		
		try {
			HttpRequest httpRequest = httpParser.parseHttpRequest(generateINValidGETTestCase3());
			fail();
		} catch (HttpParsingException e) {
			// TODO Auto-generated catch block
			assertEquals(e.errorCode, HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST );
			//e.printStackTrace();
		}
	}
	
	@Test
	public void parseHttpEmptyRequestLine() {
		
		try {
			HttpRequest httpRequest = httpParser.parseHttpRequest(generateINValidGETTestCase4());
			fail();
		} catch (HttpParsingException e) {
			// TODO Auto-generated catch block
			
			assertEquals(e.errorCode, HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST );
			//e.printStackTrace();
		}
	}
	
	@Test
	public void parseHttpbadRequestLineNoLineFeed() {
		
		try {
			HttpRequest httpRequest = httpParser.parseHttpRequest(generateINValidGETTestCase5());
			fail();
		} catch (HttpParsingException e) {
			// TODO Auto-generated catch block
			
			assertEquals(e.errorCode, HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST );
			//e.printStackTrace();
		}
	}
	
	@Test
	public void parseHttpRequestBadHttpVersion() {
		
		try {
			HttpRequest httpRequest = httpParser.parseHttpRequest(generateBadHttpVersiontestCase());
			fail();
		} catch (HttpParsingException e) {
			// TODO Auto-generated catch block
			
			assertEquals(e.errorCode, HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST );
			//e.printStackTrace();
		}
	}
	
	@Test
	public void parseHttpRequestUnsupportedHttpVersion() {
		
		try {
			HttpRequest httpRequest = httpParser.parseHttpRequest(generateUnsupportedHttpVersiontestCase());
			fail();
		} catch (HttpParsingException e) {
			// TODO Auto-generated catch block
			
			assertEquals(e.errorCode, HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED );
			//e.printStackTrace();
		}
	}
	
    @Test
	public void parseHttpRequestSupportedHttpVersion() {
		
		try {
			HttpRequest httpRequest = httpParser.parseHttpRequest(generateSupportedHttpVersiontestCase());
			assertNotNull(httpRequest);
			assertEquals(httpRequest.getHttpVersion(), HttpVersion.HTTP_1_1);
			assertEquals(httpRequest.getOriginalVersion(), "HTTP/1.2");
		} catch (HttpParsingException e) {
			// TODO Auto-generated catch block
			
			assertEquals(e.errorCode, HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED );
			//e.printStackTrace();
		}
	}
	
	private InputStream generateValidGETTestCase() {
		String rawData = "GET / HTTP/1.1\r\n"
				+ "Host: localhost:8080\r\n"
				+ "Connection: keep-alive\r\n"
				+ "sec-ch-ua: \"Google Chrome\";v=\"95\", \"Chromium\";v=\"95\", \";Not A Brand\";v=\"99\"\r\n"
				+ "sec-ch-ua-mobile: ?0\r\n"
				+ "sec-ch-ua-platform: \"Windows\"\r\n"
				+ "Upgrade-Insecure-Requests: 1\r\n"
				+ "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36\r\n"
				+ "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\r\n"
				+ "Purpose: prefetch\r\n"
				+ "Sec-Fetch-Site: none\r\n"
				+ "Sec-Fetch-Mode: navigate\r\n"
				+ "Sec-Fetch-User: ?1\r\n"
				+ "Sec-Fetch-Dest: document\r\n"
				+ "Accept-Encoding: gzip, deflate, br\r\n"
				+ "Accept-Language: en-US,en;q=0.9\r\n"
				+"\r\n";
		InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
		//System.out.println("inputStream returned");
		return inputStream;
	}
	private InputStream generateINValidGETTestCase() {
		String rawData = "GeT / HTTP/1.1\r\n"
				+ "Host: localhost:8080\r\n"
				+ "Connection: keep-alive\r\n"
				
				+ "Accept-Language: en-US,en;q=0.9\r\n"
				+"\r\n";
		InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
		//System.out.println("inputStream returned");
		return inputStream;
	}
	private InputStream generateINValidGETTestCase2() {
		String rawData = "GETTTT / HTTP/1.1\r\n"
				+ "Host: localhost:8080\r\n"
				+ "Connection: keep-alive\r\n"
				
				+ "Accept-Language: en-US,en;q=0.9\r\n"
				+"\r\n";
		InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
		//System.out.println("inputStream returned");
		return inputStream;
	}
	private InputStream generateINValidGETTestCase3() {
		String rawData = "GET / EEEEE HTTP/1.1\r\n"
				+ "Host: localhost:8080\r\n"
				+ "Connection: keep-alive\r\n"
				
				+ "Accept-Language: en-US,en;q=0.9\r\n"
				+"\r\n";
		InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
		//System.out.println("inputStream returned");
		return inputStream;
	}
	private InputStream generateINValidGETTestCase4() {
		String rawData = "\r\n"
				+ "Host: localhost:8080\r\n"
				+ "Connection: keep-alive\r\n"
				
				+ "Accept-Language: en-US,en;q=0.9\r\n"
				+"\r\n";
		InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
		//System.out.println("inputStream returned");
		return inputStream;
	}
	private InputStream generateINValidGETTestCase5() {
		String rawData = "GET / HTTP/1.1\r"
				+ "Host: localhost:8080\r\n"
				+ "Connection: keep-alive\r\n"
				
				+ "Accept-Language: en-US,en;q=0.9\r\n"
				+"\r\n";
		InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
		//System.out.println("inputStream returned");
		return inputStream;
	}
	
	private InputStream generateBadHttpVersiontestCase() {
		String rawData = "GET / HTP/1.1\r"
				+ "Host: localhost:8080\r\n"
				+ "Connection: keep-alive\r\n"
				
				+ "Accept-Language: en-US,en;q=0.9\r\n"
				+"\r\n";
		InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
		//System.out.println("inputStream returned");
		return inputStream;
	}
	
	private InputStream generateUnsupportedHttpVersiontestCase() {
		String rawData = "GET / HTTP/2.1\r\n"
				+ "Host: localhost:8080\r\n"
				+ "Connection: keep-alive\r\n"
				
				+ "Accept-Language: en-US,en;q=0.9\r\n"
				+"\r\n";
		InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
		//System.out.println("inputStream returned");
		return inputStream;
	}
	
	private InputStream generateSupportedHttpVersiontestCase() {
		String rawData = "GET / HTTP/1.2\r\n"
				+ "Host: localhost:8080\r\n"
				+ "Connection: keep-alive\r\n"
				
				+ "Accept-Language: en-US,en;q=0.9\r\n"
				+"\r\n";
		InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
		//System.out.println("inputStream returned");
		return inputStream;
	}
	

}
