package de.clearit.test.entity;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

/**
 * Die Elternklasse für entities.
 * 
 * @author Ilja Winokurow
 */
public class AbstractEntity
{

   /* Logger */
   protected Logger logger = Logger.getLogger(this.getClass().getName());

   /**
    * Die Werte der Felder verglichen.
    * 
    * @param secondObject
    *           - zweite Objekt
    * 
    * @return ob die Objekte gleich sind
    */
   public <T> boolean compareObjectsUsingFieldsValue(T secondObject)
   {
      boolean isEqual = true;
      final Field[] fields = secondObject.getClass().getDeclaredFields();
      for (final Field field : fields)
      {
         try
         {
            field.setAccessible(true);
            if (!(field.get(this).equals(field.get(secondObject))))
            {
               logger.info("Die Gleichheits Prüfung ist fehlgeschlagen: Erwartet: " + field.get(this) + " Bekommen: "
                     + field.get(secondObject));
               isEqual = false;
            }
         }
         catch (final IllegalArgumentException e)
         {
            logger.error(e);
         }
         catch (final IllegalAccessException e)
         {
            logger.error(e);
         }
      }
      return isEqual;
   }

}
