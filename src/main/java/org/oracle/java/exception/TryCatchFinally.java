package org.oracle.java.exception;

public class TryCatchFinally {

	public static void main(String[] args) throws Exception {

		try {
			System.out.println('A');
			try {
				System.out.println('B');
				throw new Exception("threw exception in B");
			} finally {
				System.out.println('X');
			}
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			System.out.println('Y');
		} finally {
			System.out.println('Z');
		}
	}
}
