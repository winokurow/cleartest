package de.clearit.test.framework.listener;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.testng.ITestResult;

public class TestCount extends BaseProjectTestListenerAdapter
{

   private static final String FIRST_TEST = "FIRST_TEST";

   private static final String TEST_COUNT = "TEST_COUNT";

   private static final Logger logger = Logger.getLogger("- - - - - - - - - ");

   private static AtomicBoolean firstTest = new AtomicBoolean(true);

   private static AtomicInteger counter = new AtomicInteger();

   @Override
   public void onTestStart(ITestResult tr)
   {
      handleFirstTestboolean(tr);
      handleCountInteger(tr);
      logger.info("Test " + counter.get());
   }

   private void handleCountInteger(ITestResult tr)
   {
      Integer countFromSuite = (Integer) tr.getTestContext().getSuite().getAttribute(TEST_COUNT);
      if (countFromSuite == null)
      {
         counter.set(1);
         tr.getTestContext().getSuite().setAttribute(TEST_COUNT, 1);
      }
      else
      {
         int newCount = countFromSuite + 1;
         counter.set(newCount);
         tr.getTestContext().getSuite().setAttribute(TEST_COUNT, newCount);
      }
   }

   private void handleFirstTestboolean(ITestResult tr)
   {
      Boolean firstTestFromSuite = (Boolean) tr.getTestContext().getSuite().getAttribute(FIRST_TEST);
      if (firstTestFromSuite == null)
      {
         firstTest.set(true);
         tr.getTestContext().getSuite().setAttribute(FIRST_TEST, true);
      }
      else
      {
         firstTest.set(false);
         tr.getTestContext().getSuite().setAttribute(FIRST_TEST, false);
      }
   }

   public static boolean isFirst()
   {
      return firstTest.get();
   }
}
