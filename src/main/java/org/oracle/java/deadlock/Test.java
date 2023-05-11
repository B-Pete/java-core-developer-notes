package org.oracle.java.deadlock;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;

public class Test {
	static volatile boolean quit;

	private static void addStuff(ConcurrentHashMap<String, Object> m) {
		for (int i = 0; i < 50; ++i) {
			Object o = null;
			String[] sa;

			switch (i % 3) {
			case 0:
				sa = new String[10];
				for (int j = 0; j < 10; ++j)
					sa[j] = i + "_" + j;
				o = sa;
				break;

			case 1:
				o = new Integer(i);
				break;

			case 2:
				o = "s_" + i;
				break;
			}

			m.put("i" + i, o);
		}
	}

	public static void main(String[] args) {
		ConcurrentHashMap[] nested = new ConcurrentHashMap[10];
		ConcurrentHashMap<String, Object> a = new ConcurrentHashMap<>();
		ConcurrentHashMap<String, Object> b = new ConcurrentHashMap<>();

		addStuff(a);
		addStuff(b);

		for (int i = 0; i < nested.length; ++i) {
			nested[i] = new ConcurrentHashMap<String, Object>();
			addStuff(nested[i]);
			a.put("a_nm" + i, nested[i]);
			b.put("b_nm" + i, nested[i]);
		}

		nested[0].put("aaa", a);

		new Thread(new Run1(a)).start();
		new Thread(new Run1(b)).start();
		new Thread(new Run1(a)).start();
		new Thread(new Run1(b)).start();

		new Thread(new Run2(a, "a")).start();
		new Thread(new Run2(b, "b")).start();

		new Thread(new Run2(nested[0], "n0")).start();
		new Thread(new Run2(nested[1], "n1")).start();

		new Thread(new Run3(a, nested[0], nested[1], "r3a")).start();
		new Thread(new Run3(b, nested[1], nested[1], "r3b")).start();

		try {
			Thread.sleep(30000);
		} catch (Exception e) {
		}

		System.out.println("quit");

		quit = true;
	}

	private static class Run1 implements Runnable {
		ConcurrentHashMap<String, Object> map;

		Run1(ConcurrentHashMap<String, Object> m) {
			map = m;
		}

		public void run() {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);

				while (!quit) {
					baos.reset();
					oos.writeObject(map);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static class Run2 implements Runnable {
		ConcurrentHashMap<String, Object> map;
		String prefix;

		Run2(ConcurrentHashMap<String, Object> m, String p) {
			map = m;
			prefix = p;
		}

		public void run() {
			while (!quit) {
				for (int i = 0; i < 20; ++i)
					map.put(prefix + "_" + i, new Integer(i));

				for (int i = 0; i < 20; ++i)
					map.remove(prefix + "_" + i);
			}
		}
	}

	private static class Run3 implements Runnable {
		ConcurrentHashMap<String, Object> map;
		Object obj1;
		Object obj2;
		String prefix;

		Run3(ConcurrentHashMap<String, Object> m, Object o1, Object o2, String p) {
			map = m;
			obj1 = o1;
			obj2 = o2;
			prefix = p;
		}

		public void run() {
			while (!quit) {
				map.put(prefix + "1", obj1);
				map.put(prefix + "2", obj2);

				map.put(prefix + "1", obj2);
				map.put(prefix + "2", obj1);

				map.remove(prefix + "1");
				map.remove(prefix + "2");
			}
		}
	}
}
