package astr_pkg;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
				AlienTest.class, 
				AsteroidsGameTest.class, 
				AsteroidTest.class,
				ProjectilesAliensTest.class, 
				ProjectilesTest.class, 
				ShipTest.class })
//Some of the tests will fail if ran from here, but they should pass on their own.
public class AllTests {

}
