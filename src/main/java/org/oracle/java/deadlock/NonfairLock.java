package org.oracle.java.deadlock;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class NonfairLock {

	private static long start = System.nanoTime();

	private static void println(String msg) {
		long now = System.nanoTime();
		System.out.println((now - start) / 1000000 + "ms: " + msg);
	}

	public static void main(String[] args) {
		boolean fair = true;
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock(fair);
		final ReadLock readLock = lock.readLock();
		final WriteLock writeLock = lock.writeLock();
		new Thread() {
			@Override
			public void run() {
				readLock.lock();
				println(getName() + " got the read lock");
				quietSleep(10);
				readLock.unlock();
				println(getName() + " released the read lock");

			}
		}.start();

		quietSleep(5);

		new Thread() {
			@Override
			public void run() {
				writeLock.lock();
				println(getName() + " got the write lock");
				quietSleep(10);
				writeLock.unlock();
				println(getName() + " released the write lock");

			}
		}.start();

		for (int i = 0; i < 10; i++) {
			new Thread() {
				@Override
				public void run() {
					readLock.lock();
					println(getName() + " got the read lock");
					readLock.unlock();
					println(getName() + " released the read lock");
				}
			}.start();
		}
	}

	private static void quietSleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
