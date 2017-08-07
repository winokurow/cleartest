package de.clearit.test.framework.listener;

import org.apache.log4j.Logger;
import org.testng.ITestResult;

public class LogTest extends BaseProjectTestListenerAdapter
{

   private static final Logger logger = Logger.getLogger(LogTest.class.getName());

   @Override
   public void onTestFailure(final ITestResult tr)
   {
      logger.error("[FAILURE] on " + testName(tr), tr.getThrowable());
   }

   @Override
   public void onTestSuccess(ITestResult tr)
   {
      logger.info("[SUCCESS] on " + testName(tr));
   }

   @Override
   public void onTestSkipped(ITestResult tr)
   {
      logger.warn("[SKIPPED] " + testName(tr));
   }

   @Override
   public void onTestStart(ITestResult tr)
   {
      // logger.info("[STARTING] " + testName(tr));
   }

   private String testName(final ITestResult tr)
   {
      return tr.getTestClass().getRealClass().getSimpleName() + "." + tr.getName();
   }
}
