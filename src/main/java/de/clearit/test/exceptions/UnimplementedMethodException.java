package de.clearit.test.exceptions;

/**
 * Wird geworfen, wenn nicht implementierte Method angerufen wird.
 * 
 * @author Ilja Winokurow
 */
public class UnimplementedMethodException extends RuntimeException
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
   public UnimplementedMethodException(Exception exception)
   {
      super(exception);
   }

   /**
    * Constructor
    * 
    * @param text
    *           - die Ausnahmemeldung
    */
   public UnimplementedMethodException(String text)
   {
      super(text);
   }
}
