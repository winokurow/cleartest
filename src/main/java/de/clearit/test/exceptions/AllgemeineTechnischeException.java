package de.clearit.test.exceptions;

/**
 * Die Grundausnahme.
 * 
 * @author Ilja Winokurow
 */
public class AllgemeineTechnischeException extends RuntimeException
{

   /**
    * Comment for
    */
   private static final long serialVersionUID = -7988669567573654127L;

   /**
    * Constructor
    * 
    * @param exception
    *           - die Ausnahme
    */
   public AllgemeineTechnischeException(Exception exception)
   {
      super(exception);
   }

   /**
    * Constructor
    * 
    * @param text
    *           - die Ausnahmemeldung
    */
   public AllgemeineTechnischeException(String text)
   {
      super(text);
   }
}
