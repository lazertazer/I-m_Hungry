package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.utilities.ListContainer;

public class ListContainerTest {
	private ListContainer lists;

	@Before
	public void setUp() {
		lists = new ListContainer();
	}
	
	@Test
	public void testGetLists() {
		assertEquals(3, lists.getLists().size());
	}

	@Test
	public void testGetList() {
		assertTrue(lists.getList(0).getListName().equals("Favorites"));
		assertTrue(lists.getList(1).getListName().equals("Do Not Show"));
		assertTrue(lists.getList(2).getListName().equals("To Explore"));
	}

	@Test
	public void testGetFavorites() {
		assertTrue(lists.getFavorites().getListName().equals("Favorites"));
		assertEquals(0, lists.getFavorites().getAllItems().size());
	}

	@Test
	public void testGetNoShow() {
		assertTrue(lists.getNoShow().getListName().equals("Do Not Show"));
		assertEquals(0, lists.getNoShow().getAllItems().size());
	}

	@Test
	public void testGetExplore() {
		assertTrue(lists.getExplore().getListName().equals("To Explore"));
		assertEquals(0, lists.getExplore().getAllItems().size());
	}
}