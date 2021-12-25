package HTTP;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class HttpVersionTest {
	
	@Test
	void getBestComptibleVersionExactMatch() {
		try {
			HttpVersion version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
			assertNotNull(version);
			assertEquals(version, HttpVersion.HTTP_1_1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	void getBestComptibleVersionBadFormat() {
		try {
			HttpVersion version = HttpVersion.getBestCompatibleVersion("http/1.1");
			fail();
		} catch (Exception e) {
					}
	}
	
	@Test
	void getBestComptibleVersionBadFormat2() {
		try {
			HttpVersion version = HttpVersion.getBestCompatibleVersion("HTTP/1.2");
			assertNotNull(version);
			assertEquals(version, HttpVersion.HTTP_1_1);
			
		} catch (Exception e) {
			fail();
	    }
	}

}
