package de.clearit.test.framework;

/**
 * Public Methods for ExecutionTimer
 */
public final class ExecutionTimerManager
{

   private static ThreadLocal<ExecutionTimer> timer = new ThreadLocal<>();

   /**
    * private constructor to hide the implicit public one
    */
   private ExecutionTimerManager()
   {

   }

   /**
    * @return Returns the executionTimer.
    */
   public static ExecutionTimer getExecutionTimer()
   {
      return timer.get();
   }

   /**
    * @param executionTimer
    *           The executionTimer to set.
    */
   public static void setExecutionTimer(ExecutionTimer executionTimer)
   {
      timer.set(executionTimer);
   }

}
