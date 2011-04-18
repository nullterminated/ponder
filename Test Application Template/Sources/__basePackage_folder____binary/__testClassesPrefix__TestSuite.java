package ${basePackage};

import junit.framework.Test;
import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ${initialTestPackage}.${initialTestClassName}Test;

import er.extensions.ERXExtensions;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	${initialTestClassName}Test.class
})

public class ${testClassesPrefix}TestSuite {
  public static void initialize() {
    ERXExtensions.initApp(Application.class, new String[0]);
    // just provided so MyTestCase can touch this class to get the static block
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    
    //Add to test suite here
    suite.addTest(${initialTestClassName}Test.suite());
    return suite;
  }
}
