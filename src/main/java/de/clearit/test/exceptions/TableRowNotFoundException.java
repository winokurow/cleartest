package de.clearit.test.exceptions;

/**
 * Die Ausnahme 'TableRowNotFoundException'.
 * 
 * @author Ilja Winokurow
 */
public class TableRowNotFoundException extends Exception
{

   /**
    * Serial Version ID
    */
   private static final long serialVersionUID = 5019836978149592779L;

   /**
    * Constructor
    * 
    * @param text
    *           - der Text
    */
   public TableRowNotFoundException(String text)
   {
      super("Die Zeile mit dem Text " + text + " ist in der Tabelle nicht gefunden.");
   }
}
