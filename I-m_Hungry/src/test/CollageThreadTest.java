package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;

import main.threads.CollageThread;

public class CollageThreadTest {
	private CollageThread ct;
	
	@Before
	public void setUp() {
		ct = new CollageThread(null, "french+bread");
	}

	@Test
	public void testGetCSEimages() {
		try {
			JsonObject json = ct.connectReadParse(ct.buildCSE_URL("french+bread"));
			assertEquals(10, ct.getCSEimages(json).size());
		} catch (Exception e) {}		
	}

	@Test
	public void testConnectReadParse() {
		try {
			JsonObject json = ct.connectReadParse(ct.buildCSE_URL("french+bread"));
			assertTrue(json.size() > 1);
		} catch (Exception e) {}
	}

	@Test
	public void testBuildCSE_URL() {
		String CSE_url = ct.buildCSE_URL("french+bread");
		assertTrue(CSE_url.contains("https://www.googleapis.com/customsearch/v1?key="));
		assertTrue(CSE_url.contains("&searchType=image&num=10&filter=1&q=food+french+bread"));
	}
	
	@After
	public void after() {
		ct.run();
	}
}