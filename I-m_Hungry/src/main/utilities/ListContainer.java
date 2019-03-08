package main.utilities;

import java.util.ArrayList;

public class ListContainer {
	private ArrayList<UserList> userLists;
	
	public ListContainer() {
		this.userLists = new ArrayList<UserList>();
		UserList favorites = new UserList("Favorites", "FAV");
		UserList doNotShow = new UserList("Do Not Show", "DNS");
		UserList toExplore = new UserList("To Explore", "XPL");
		userLists.add(favorites);
		userLists.add(doNotShow);
		userLists.add(toExplore);
	}
	public ArrayList<UserList> getLists() {
		return userLists;
	}
	public UserList getList(int index) {
		return userLists.get(index);
	}
	public UserList getFavorites() {
		return userLists.get(0);
	}
	public UserList getNoShow() {
		return userLists.get(1);
	}
	public UserList getExplore() {
		return userLists.get(2);
	}
}