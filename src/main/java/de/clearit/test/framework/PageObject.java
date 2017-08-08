package de.clearit.test.framework;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import de.clearit.test.common.SeitenladePruefer;
import de.clearit.test.framework.elemente.WebBaseElement;
import de.clearit.test.pages.HinweisPage;

/**
 * Framework Page Object.
 *
 * @author Ilja Winokurow
 */
public class PageObject
{

   /* Logger */
   protected Logger logger;

   /* WebDriver */
   protected WebDriver driver;

   /* Der Titel der Seite */
   protected String title;

   /* Wie lange wird es gewartet bis die Seite geladen wird (ms) */
   protected long timeout;

   /**
    * PageObject
    * <p>
    * Constructor.
    */
   public PageObject()
   {
      logger = Logger.getLogger(getClass());
      PropertyManager properties = PropertyManager.getInstance();
      timeout = Long.parseLong(properties.getProperty("webdriver.timeout"));
   }

   /**
    * waitForMainElementsIsShown
    * <p>
    * Warten bis alle Elemente, die mit dem Annotation Check markiert sind, angezeigt werden. Ausserdem werden in den
    * GuiElementen die WebDriver gesetzt.
    */
   public void waitForMainElementsIsShown()
   {
      waitForMainElementsIsShown(-1);
   }

   /**
    * Warten bis die Seite geladen wird
    */
   public void waitForPageIsLoad()
   {
      SeitenladePruefer.mitDriver(driver).waitForPageIsLoad();
   }

   /**
    * waitForPageIsLoad
    * <p>
    * Warten bis die Seite geladen wird <br>
    * (warten bis der Status der Seite=ready, Block UI ist weg, Ajax Indicator ist weg)
    *
    * @param timeoutSeconds
    *           - das Timeout
    */
   public void waitForPageIsLoad(long timeoutSeconds)
   {
      SeitenladePruefer.mitDriver(driver).waitForPageIsLoad(timeoutSeconds);
   }

   /**
    * waitForPageIsReady
    * <p>
    * Warten bis die Seite den Status "Ready" bekommt
    */
   public void waitForPageIsReady()
   {
      SeitenladePruefer.mitDriver(driver).waitForPageIsReady();
   }

   /**
    * waitForMainElementsIsShown
    * <p>
    * Warten bis alle Elemente, die mit dem Annotation Check markiert sind, angezeigt werden. Ausserdem werden in den
    * GuiElementen die WebDriver gesetzt.
    * 
    * @param timeoutInSeconds
    *           - das Timeout (in Sekunden)
    */
   public void waitForMainElementsIsShown(long timeoutInSeconds)
   {
      if (timeoutInSeconds == -1)
      {
         waitForPageIsLoad();
      }
      else
      {
         waitForPageIsLoad(timeoutInSeconds);
      }
      logErrorMessageBoxWennGefundenUndFailWennTechnischerFehlerMitID();
      //checkForMaskenTitle();
      setWebDriverOnAllGuiElementsAndWaitForVisibleIfWanted(true);

      // Stop timer when page is loaded (Page and not the Popup)
      if (!(this instanceof HinweisPage))
      {
         ExecutionTimerManager.getExecutionTimer().end(title);
      }
   }

   /**
    * setWebDriverOnAllGuiElementsAndWaitForVisibleIfWanted
    * <p>
    * WebDriver für alle GuiElementen setzen und überprüfen wenn notwendig (Annotation Check)
    *
    * @param waitForVisible
    *           - ob Elemente auf Sichtbarkeit geprüft werden
    */
   private void setWebDriverOnAllGuiElementsAndWaitForVisibleIfWanted(boolean waitForVisible)
   {
      final List<Field> fields = getAllFields(new ArrayList<Field>(), this.getClass());
      try
      {
         for (final Field field : fields)
         {
            field.setAccessible(true);
            Object value = field.get(this);
            if (value instanceof WebDriverInjectable)
            {
               WebDriverInjectable webDriverVerwender = (WebDriverInjectable) value;
               webDriverVerwender.setDriver(driver);
            }
            if (value instanceof WebBaseElement)
            {
               WebBaseElement guielement = (WebBaseElement) value;
               if (field.isAnnotationPresent(Check.class) && waitForVisible)
               {
                  guielement.waitForVisible();
               }
            }

         }
      }
      catch (final IllegalAccessException e)
      {
         logger.error(e);
      }
   }

   /**
    * Überprüfen ob Masken Titel richtig ist
    */
   private void checkForMaskenTitle()
   {
      String gefundeneMaske = "";
      if (title != null && !title.isEmpty() && (!gefundeneMaske.equals(title)))
      {

         String zusaetzlicherFehlerhinweis = "";
         String errorTextAufMaske = ErrorBoxUtil.holeAktuellenErrorText(driver, 10L);
         if (!StringUtils.isEmpty(errorTextAufMaske))
         {
            zusaetzlicherFehlerhinweis = String.format(" Die aktuelle Maske hat den Fehlertext '%s' ausgegeben.",
                  errorTextAufMaske);
         }

         Assert.fail(String.format("Maske '%s' erwartet, aber nicht gefunden. Aktuelle Maske ist '%s'.%s", title,
               gefundeneMaske, zusaetzlicherFehlerhinweis));
      }
   }

   /**
    * Eine Fehlermeldung ausgeben wenn angezeigt
    */
   private void logErrorMessageBoxWennGefundenUndFailWennTechnischerFehlerMitID()
   {
      String errorTextAufMaske = ErrorBoxUtil.holeAktuellenErrorText(driver, 1L);
      if (!StringUtils.isEmpty(errorTextAufMaske))
      {
         logger.info(StringUtils.CR + StringUtils.CR
               + "**************************************************************************************"
               + StringUtils.CR + "Error Message Box auf der Seite hat folgendes Ausgegeben:" + StringUtils.CR
               + StringUtils.CR + errorTextAufMaske + StringUtils.CR + StringUtils.CR
               + "**************************************************************************************"
               + StringUtils.CR);

         FataleFehlerWerfer.pruefeFehlerTextUndFailWennFatal(errorTextAufMaske);
      }
   }

   /**
    * Get all members
    *
    * @param fields
    *           - initial list of fields
    * @param type
    *           - class
    * @return list of members
    */
   private static List<Field> getAllFields(List<Field> fields, final Class<?> type)
   {
      List<Field> tempFields = new ArrayList<>();
      tempFields.addAll(fields);
      tempFields.addAll(Arrays.asList(type.getDeclaredFields()));

      if (type.getSuperclass() != null)
      {
         tempFields = getAllFields(tempFields, type.getSuperclass());
      }

      return tempFields;
   }

   /**
    * Das Element betätigen um die Seite wechseln.
    *
    * @param element
    *           - das Element zu betätigen (muss sichtbar sein)
    * @param text
    *           - Der Logzeilentext für den Test
    */
   public void clickWithPageChange(final WebBaseElement element, final String text)
   {
      if (!(text.isEmpty()))
      {
         logger.info(text);
      }
      element.waitForVisible();
      element.waitForEnable();
      element.click();
      ExecutionTimerManager.getExecutionTimer().start(text);
      waitForPageIsLoad();
   }

   /**
    * getTitle.
    *
    * @return title
    */
   public String getTitle()
   {
      return title;
   }
}
