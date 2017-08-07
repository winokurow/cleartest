package de.clearit.test.framework;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Diese Annotation zeigt ob das markierte Element nach dem Seite Laden geprüft wird.
 * 
 * @author Ilja Winokurow
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Check {

}
