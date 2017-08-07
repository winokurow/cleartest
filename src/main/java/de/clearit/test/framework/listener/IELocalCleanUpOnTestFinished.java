package de.clearit.test.framework.listener;

import org.testng.ITestResult;

import de.clearit.test.common.TestUtils;
import de.clearit.test.common.WebdriverUtils;

public class IELocalCleanUpOnTestFinished extends BaseProjectTestListenerAdapter
{

   @Override
   public void onTestFailure(final ITestResult tr)
   {
      // killLocalIE();
   }

   @Override
   public void onTestSuccess(ITestResult tr)
   {
      killLocalIE();
   }

   private void killLocalIE()
   {
      if (isLocal() && WebdriverUtils.isIE())
      {
         if (TestUtils.isProcessRunningQuiet("IEDriverServer.exe"))
         {
            TestUtils.killProcess("IEDriverServer.exe");
            TestUtils.killProcess("iexplore.exe");
            TestUtils.killProcess("conhost.exe");
         }
      }
   }

   private boolean isLocal()
   {
      return System.getProperty("remote", "false").equals("false");
   }

}
