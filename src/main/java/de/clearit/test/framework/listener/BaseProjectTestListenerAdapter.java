package de.clearit.test.framework.listener;

import org.testng.ITestResult;

public abstract class BaseProjectTestListenerAdapter implements ProjectTestListenerAdapter
{

   @Override
   public void onTestStart(final ITestResult tr)
   {
   }

   @Override
   public void onTestFailure(final ITestResult tr)
   {
   }

   @Override
   public void onTestSuccess(final ITestResult tr)
   {
   }

   @Override
   public void onTestSkipped(final ITestResult tr)
   {
   }

}
