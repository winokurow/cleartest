package de.clearit.test.helper;

import org.apache.log4j.Logger;

import de.clearit.test.framework.PropertyManager;
import de.clearit.test.framework.webdriver.WebDriverWrapper;


/**
 * <p>
 * Die Klasse kümmert sich um die Bereitstellung der notwendigen Informationen, damit durchgeführte
 * Selenium-Tests in Dynatrace unter "Test Automation" -&gt; "Test Results" in der Kategorie "Browser/UI-Driven Tests"
 * erscheinen.
 * </p>
 * <p>
 * <p>
 * <b>Zweck</b>
 * </p>
 *
 * Laut <a href=
 * "https://community.dynatrace.com/community/display/DOCDT65/UI-Driven+Browser+Test#UI-DrivenBrowserTest-TagtheUEMvisitasatest">Dynatrace
 * Dokumentation</a> muss zur Zuordnung der Selenium-Tests zu den gemessenen Daten in Dynatrace eine entsprechende
 * Information per JavaScript an den im Browser vorhandenen Dynatrace-Agenten übermittelt werden. Dies geschieht
 * hier in der {@link #aktivieren(WebDriverWrapper) aktivieren}-Methode. Zudem muss das Ende des Testdurchlaufs an den
 * Agentenübermittelt werden. Dies geschieht ebenfalls über einen JavaScript-Aufruf unter {@link #beenden()}.
 * </p>
 * <p>
 * <p>
 * <b>Verwendung</b>
 * </p>
 * <p>
 * Um einen oder mehrere Selenium-Tests zu vermessen, muss
 * <ul>
 * <li>in der <tt>local.properties</tt>-Konfigurationsdatei ein Eintrag mit <tt>{@value #DYNATRACE_MESSUNG_KEY}</tt>
 * vorhanden sein</li>
 * <li>die Testausführung über die IDE mittels dem Dynatrace AppMon Plugin ausgeführt werden</li>
 * <li>der Application Server muss entsprechend für die Ausführung mit Dynatrace konfiguriert sein</li>
 * </ul>
 * </p>
 * <p>
 * <p>
 * <b>Hintergrund zur technischen Realisierung</b>
 * </p>
 *
 * Die Dynatrace-Messsung speichert Daten für die spätere Verwendung, wenn der {@link WebDriverWrapper
 * WebDriverWrapper} erzeugt wird. Leider ist das notwendig, da an den Stellen des Systemtest-Frameworks, an denen der
 * WebDriver erzeugt wird, kein einfacher Zugriff auf den Testnamem möglich ist.
 *
 * </p>
 */
public class DynatraceMessung
{
   private static final Logger logger = Logger.getLogger(DynatraceMessung.class);
   private static final String DYNATRACE_MESSUNG_KEY = "dynatrace_messung";

   // Attribute, welche bei Instanzierung gespeichert und bei Ausführung benöigt werden (dirty...)
   private final String testname;
   private final boolean istAktiviert;

   /**
    * Für das Festhalten von WebDriverWrapper. Wird benöigt, um das für Dynatrace notwendige JavaScript
    * auf der Webseite auszuführen
    */
   private WebDriverWrapper driver;

   public DynatraceMessung(String testklasse, String testmethode)
   {

      if ("true".equals(PropertyManager.getInstance().getProperty(DYNATRACE_MESSUNG_KEY, "false")))
      {

         if (testklasse != null && testmethode != null)
         {
            testname = testklasse + "." + testmethode;
            logger.info("[DYNATRACE] Aktiviere Dynatrace-Messung für Test '" + testmethode + "'.");
            istAktiviert = true;
         }
         else
         {
            logger.info("[DYNATRACE] Dynatrace-Messung aktiviert, aber Testname unbekannt! Deaktiviere Messung.");
            testname = "";
            istAktiviert = false;
         }

      }
      else
      {
         testname = "";
         istAktiviert = false;
      }

   }

   /**
    * Aktiviert die Dynatrace Messung für die Selenium Tests. An sich wird ein JavaScript ausgeführt vor dem Laufen
    * aller Tests.
    *
    * @param webDriverWrapper der WebDriver, welche das JavaScript ausführen soll
    */
   public void aktivieren(WebDriverWrapper webDriverWrapper)
   {

      if (istAktiviert)
      {
         if (webDriverWrapper == null || webDriverWrapper.isClosedOrQuit())
         {
            logger.warn("[DYNATRACE] Dynatrace-Messung aktiviert, aber WebDriverWrapper ist null oder nicht offen. "
                  + "Dynatrace-Messung wird nicht durchgeführt.");
         }
         else
         {
            logger.info("[DYNATRACE] Dynatrace-Messung ist aktiviert für Test '" + testname + "'.");
            driver = webDriverWrapper;
            webDriverWrapper.executeScript("sessionStorage.DT_TESTNAME = \"" + testname + "\";");
         }
      }

   }

   /**
    * Beendet die Dynatrace Messung für die Selenium Tests. Hier wird ein JavaScript über den WebDriver
    * ausgeführt, welches dann das Ende der Messung an Dynatrace signalisiert.
    */
   void beenden()
   {

      if (istAktiviert)
      {
         if (driver != null && !driver.isClosedOrQuit())
         {
            logger.info("[Dynatrace] Messung wird beendet.");

            // Für den Fall, dass Dynatrace in den Properties aktiviert wurde, jedoch nicht im Browser eingebunden
            // ist (z. B. bei Testausführung von lokal auf eine QS1), wird hier zur Sicherheit kontrolliert
            boolean isDynatraceJavaScriptVorhanden = (boolean) driver.executeScript("typeof dynaTrace != "
                  + "'undefined'");
            if (isDynatraceJavaScriptVorhanden)
            {
               driver.executeScript("dynaTrace.endVisit();");
            }
         }
      }
   }
}
