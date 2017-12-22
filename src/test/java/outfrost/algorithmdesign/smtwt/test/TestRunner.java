package outfrost.algorithmdesign.smtwt.test;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class TestRunner {

	public static void main(String[] args) {
		Reflections reflections = new Reflections(TestRunner.class.getPackage().getName());
		Set<Class<? extends Test>> tests = reflections.getSubTypesOf(Test.class);
		for (Class<? extends Test> test : tests) {
			try {
				test.getConstructor().newInstance().run();
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
				System.err.println(e.getMessage());
			}
		}
	}

}
