package de.clearit.test.helper;

import org.apache.log4j.Logger;
import org.testng.Reporter;
import org.testng.annotations.Listeners;

import de.clearit.test.common.BasisLogger;
import de.clearit.test.exceptions.AllgemeineTechnischeException;
import de.clearit.test.framework.AllTestBasisListenerAdapters;
import de.clearit.test.framework.BasisPropertyManager;
import de.clearit.test.framework.ExecutionTimer;

/**
 * <P>
 * Das abstrakte Elternklass für alle Tests.
 * 
 *
 * @implements WebDriverHolder - WebDriver Holder
 * 
 * @author Ilja Winokurow
 */
@Listeners(AllTestBasisListenerAdapters.class)
public class BaseTestHelper {

	/* Logger */
	protected final static Logger logger = BasisLogger.LOGGER;

	/** Execution Timer. */
	protected ExecutionTimer executionTimer = null;

	/** Die Eigenschaften des Tests. */
	protected BasisPropertyManager properties;

	/** Test gestartet */
	protected Boolean testStartet = null;

	/** Testschritt */
	protected Integer step;

	public void nextStep(String text) {
		Reporter.log(text);
		logStep(text);
	}

	private void logStep(final String text) {
		logger.info("Step " + (step++) + ": " + text);
	}

	public String getProperty(String key) {
		return getProperties().getProperty(key);
	}

	private BasisPropertyManager getProperties() {
		if (properties == null) {
			throw new AllgemeineTechnischeException("Properties sind null. Start Test nicht aufgerufen?");
		}
		return properties;
	}

}
