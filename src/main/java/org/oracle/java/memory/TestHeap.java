package org.oracle.java.memory;

/**
 * -Xmx256m -verbose:gc -Xlog:gc*
 *
 */
public class TestHeap {

	static int i = 0;

	public static void main(String[] args) {

		int n = 25;
		if (i < n) {
			i++;
			System.out.println(i + " ########## length: " + args[0].length());
			main(new String[] { (args[0] + args[0]).intern() });
		}
	}

}
