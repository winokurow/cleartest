package de.clearit.test.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import de.clearit.test.framework.PropertyManager;

/**
 * Hilfsmethoden, die WebDriver erweitern.
 * 
 * @author Ilja Winokurow
 */
public class WebdriverUtils
{

   /* Logger */
   public static Logger logger = Logger.getLogger("WebdriverUtils");

   /**
    * 
    * Warten bis Element deaktiviert wird.
    * 
    * @param element
    *           -WebElement
    */
   public static void waitForDisable(WebDriver driver, final WebElement element)
   {
      final WebDriverWait wait = new WebDriverWait(driver,
            Long.parseLong(PropertyManager.getInstance().getProperty("webdriver.timeout")));
      wait.until(new ExpectedCondition<Boolean>()
      {
         @Override
         public Boolean apply(WebDriver driver)
         {
            String enabled = "";
            if (element.getAttribute("disabled") != null)
            {
               enabled = element.getAttribute("disabled");
            }
            if (element.getAttribute("aria-disabled") != null)
            {
               enabled = element.getAttribute("aria-disabled");
            }
            return enabled.equals("true");
         }
      });
   }

   /**
    * 
    * getIPOfNode.
    * 
    * @param remoteDriver
    *           -WebDriveer Instance
    */
   public static String getIPOfNode(RemoteWebDriver remoteDriver)
   {
      String hostFound = "";
      try
      {
         CommandExecutor commandExecutor = remoteDriver.getCommandExecutor();
         if (commandExecutor instanceof HttpCommandExecutor)
         {
            HttpCommandExecutor ce = (HttpCommandExecutor) commandExecutor;
            String hostName = ce.getAddressOfRemoteServer().getHost();
            int port = ce.getAddressOfRemoteServer().getPort();
            HttpHost host = new HttpHost(hostName, port);
            HttpClient httpClient = HttpClientBuilder.create().build();
            URL sessionURL = new URL(String.format("http://%s:%s/grid/api/testsession?session=%s", hostName, port,
                  remoteDriver.getSessionId()));
            BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest("POST",
                  sessionURL.toExternalForm());
            HttpResponse response = httpClient.execute(host, r);
            JSONObject object = extractObject(response);
            URL myURL = new URL(object.getString("proxyId"));
            if ((myURL.getHost() != null) && (myURL.getPort() != -1))
            {
               hostFound = myURL.getHost();
            }
         }
      }
      catch (Exception e)
      {
         logger.error(e);
      }
      return hostFound;
   }

   /**
    * extractObject.
    * 
    * @param resp
    *           - response
    */
   private static JSONObject extractObject(HttpResponse resp) throws IOException, JSONException
   {
      InputStream contents = resp.getEntity().getContent();
      StringWriter writer = new StringWriter();
      IOUtils.copy(contents, writer, "UTF8");
      JSONObject objToReturn = new JSONObject(writer.toString());
      return objToReturn;
   }

   /**
    * Rausfinden ob in IE ausgeführt wird.
    * 
    * @return ob in IE ausgeführt wird
    */
   public static boolean isIE()
   {
      return System.getProperty("ie", "false").equals("true");
   }
}
