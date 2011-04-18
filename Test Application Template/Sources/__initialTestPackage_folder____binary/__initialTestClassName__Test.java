package ${initialTestPackage};

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ${basePackage}.${testClassesPrefix}TestCase;

public class ${initialTestClassName}Test extends ${testClassesPrefix}TestCase {
	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}
	
	public static junit.framework.Test suite() { 
		return new JUnit4TestAdapter(${initialTestClassName}Test.class); 
	}
	
	@Test
	public void testSomeMethod() {
		//TODO Implement test
		Assert.fail("Test not implemented.");
	}

}
