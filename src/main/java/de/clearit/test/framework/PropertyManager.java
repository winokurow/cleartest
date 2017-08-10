package de.clearit.test.framework;

import org.apache.log4j.Logger;

/**
 * PropertyManager.
 * 
 * <P>
 * Property manager - notwendig um alle test Eigenschaften zu steuern.
 * 
 * @author Ilja Winokurow
 */
public class PropertyManager extends BasisPropertyManager {

	/* Logger */
	protected static final Logger logger = Logger.getLogger("PropertyManger");

	/** Property manager instance. */
	private static PropertyManager instance = null;

	/** Lock Objekt. Notwendig für sync Ausführung. */
	private static final Object lock = new Object();

	/**
	 * getInstance.
	 * 
	 * PropertyManager Objekt initialisieren.
	 * 
	 * @return PropertyManager Objekt.
	 */
	public static PropertyManager getInstance() {
		// if (instance == null)
		// {
		synchronized (lock) {
			if (instance == null) {
				instance = new PropertyManager();
				instance.loadProperties();
			}
		}
		// }
		return instance;
	}

	/**
	 * Constructor.
	 * 
	 * Wird nie ausgeführt. Um PropertyManager zu initialisieren rufen Method
	 * getInstance an.
	 */
	private PropertyManager() {
	}

}
