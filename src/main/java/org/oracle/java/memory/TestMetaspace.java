package org.oracle.java.memory;

/**
 * -XX:MaxMetaspaceSize=256m -verbose:gc -Xlog:gc*
 */
public class TestMetaspace {

	static javassist.ClassPool cp = javassist.ClassPool.getDefault();

	public static void main(String[] args) throws Exception{
		for (int i = 0; ; i++) { 
			Class<?> c = cp.makeClass("eu.plumbr.demo.Generated" + i).toClass();
		}
	}

}
