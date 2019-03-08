package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.utilities.Location;

public class LocationTest {
	private Location loc;
	
	@Before
	public void setUp() {
		loc = new Location("testAddress", "testLocality",
							"Los Angeles", 10.5, 20.5, "90007");
	}

	@Test
	public void testGetAddress() {
		assertTrue(loc.getAddress().equals("testAddress"));
	}

	@Test
	public void testGetLocality() {
		assertTrue(loc.getLocality().equals("testLocality"));
	}

	@Test
	public void testGetCity() {
		assertTrue(loc.getCity().equals("Los Angeles"));
	}

	@Test
	public void testGetLatitude() {
		assertTrue(Math.abs(loc.getLatitude() - 10.5) < 0.001);
	}

	@Test
	public void testGetLongitude() {
		assertTrue(Math.abs(loc.getLongitude() - 20.5) < 0.001);
	}

	@Test
	public void testGetZipcode() {
		assertTrue(loc.getZipcode().equals("90007"));
	}
}