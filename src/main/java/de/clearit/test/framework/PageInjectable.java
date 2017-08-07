package de.clearit.test.framework;

import de.clearit.test.pages.LoggedInPage;

/**
 * PageInjectable
 */
public interface PageInjectable
{
   /**
    * @param page
    *           Die Page, in der das Element verwendet wird
    */
   void setPage(LoggedInPage page);
}
