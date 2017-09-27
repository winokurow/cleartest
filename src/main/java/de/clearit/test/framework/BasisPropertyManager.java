package de.clearit.test.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * PropertyManager.
 * 
 * <P>
 * Property manager - notwendig um alle test Eigenschaften zu steuern.
 * 
 * @author Ilja Winokurow
 */
public class BasisPropertyManager {

	/* Logger */
	protected static final Logger logger = Logger.getLogger("PropertyManger");

	/** Test Eigenschaften. */
	protected final Properties defaultProps = new Properties();

	/**
	 * loadProperties.
	 * 
	 * Eigenschaften laden.
	 * 
	 */
	protected void loadProperties() {
		// create and load default properties
		loadPropertyFile("/defaultprop.properties");
		loadPropertyFile("/localprop.properties");

		final String dataset = System.getProperty("dataset", "dataset1");
		String propertiesExtension = "properties";

		String folderString2 = "/testdata/default";
		URL url = getClass().getResource(folderString2);
		if (url != null) {
			File folder2 = new File(getClass().getResource(folderString2).getFile());
			File[] files2 = folder2.listFiles();
			if (files2 != null) {
				for (final File fileEntry : files2) {

					if (fileEntry.getName().contains(propertiesExtension)) {
						loadPropertyFile(fileEntry);
					}

				}
			}
		}

		String folderString1 = "/testdata/" + dataset;
		url = getClass().getResource(folderString1);
		if (url != null) {
			File folder1 = new File(getClass().getResource(folderString1).getFile());
			if (folder1 != null) {
				File[] files1 = folder1.listFiles();
				if (files1 != null) {
					for (final File fileEntry : files1) {
						if (fileEntry.getName().contains(propertiesExtension)) {
							loadPropertyFile(fileEntry);
						}
					}
				}
			}
		}
	}

	/**
	 * loadPropertyFile.
	 * 
	 * Eigenschaften Datei laden.
	 * 
	 * @param fileName
	 *            - der Name der Datei
	 * 
	 */
	private void loadPropertyFile(final String fileName) {
		final InputStream in = getClass().getResourceAsStream(fileName);
		if (in == null) {
			throw new RuntimeException(fileName + " not found");
		}
		try {
			defaultProps.load(in);
			in.close();
		} catch (final IOException e) {
			logger.error("IOException passiert " + e.getMessage());
		}
	}

	/**
	 * loadPropertyFile.
	 * 
	 * Eigenschaften Datei laden.
	 * 
	 * @param fileName
	 *            - der Name der Datei
	 * 
	 * @throws FileNotFoundException
	 * 
	 */
	private void loadPropertyFile(final File file) {

		try {
			final InputStream in = new FileInputStream(file);
			defaultProps.load(in);
			in.close();
		} catch (final IOException e) {
			logger.error("IOException passiert " + e.getMessage());
		}
	}

	/**
	 * getProperty.
	 * 
	 * Get Wert für bestimmte Eigenschaft.
	 * 
	 * @param key
	 *            - Eigenschaft Name.
	 * @param defaultString
	 *            - was zurückgegeben werden soll, wenn der key nicht gefunden
	 *            wurde
	 */
	public String getProperty(final String key, final String defaultString) {
		String val = null;
		if (key != null) {
			val = defaultProps.getProperty(key, defaultString);
		}
		return (val);
	}

	/**
	 * getProperty.
	 * 
	 * Get Wert für bestimmte Eigenschaft.
	 * 
	 * @param key
	 *            - Eigenschaft Name.
	 */
	public String getProperty(final String key) {
		return getProperty(key, null);
	}

}
