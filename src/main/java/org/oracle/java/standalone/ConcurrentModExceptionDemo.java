package org.oracle.java.standalone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConcurrentModExceptionDemo {

	public static void main(String[] args) {
		final List<String> listOfItems = new ArrayList<String>(
				Arrays.asList("item 1", "item 2", "item 3", "item 4", "item 5"));
		System.out.println("List of items: " + listOfItems); // Initial list

		Thread a = new Thread() {
			public void run() {
				for (int i = 0; i < 9; i++) {
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					listOfItems.add("item " + i);
					System.out.println("Add new item " + i);
				}
			}
		};

		a.start();

		for (int i = 0; i < 9; i++) {
			System.out.println("Iterate over list to do something " + i);
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Iterate over the list while other thread try to add more items to it,
			// will throw ConcurrentModificationException
			for (String item : listOfItems) {
			}
		}
		System.out.println("List after modification: " + listOfItems);
	}
}