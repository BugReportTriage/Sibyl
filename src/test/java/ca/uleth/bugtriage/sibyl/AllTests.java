package ca.uleth.bugtriage.sibyl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BugReportJsonTest.class, BugzillaDatasetTest.class, MozillaClassificationTest.class })
public class AllTests {

}
