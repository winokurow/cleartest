package de.clearit.test.framework;

import org.apache.log4j.Logger;

/**
 * Framework Page Object.
 *
 * @author Ilja Winokurow
 */
public class BasisPageObject {

	/* Logger */
	protected Logger logger;

	/* Der Titel der Seite */
	protected String title;

	/* Wie lange wird es gewartet bis die Seite geladen wird (ms) */
	protected long timeout;

	/**
	 * PageObject
	 * <p>
	 * Constructor.
	 */
	public BasisPageObject() {
		logger = Logger.getLogger(getClass());
		PropertyManager properties = PropertyManager.getInstance();
		timeout = Long.parseLong(properties.getProperty("driver.timeout"));
	}

}
