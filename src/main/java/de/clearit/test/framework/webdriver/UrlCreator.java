package de.clearit.test.framework.webdriver;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlCreator
{

   public static URL createURL(final String urlString)
   {
      try
      {
         return new URL(urlString);
      }
      catch (MalformedURLException e)
      {
         throw new RuntimeException(e);
      }
   }
}
