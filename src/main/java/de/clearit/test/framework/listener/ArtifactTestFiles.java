package de.clearit.test.framework.listener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.testng.ITestResult;

public class ArtifactTestFiles
{

   public static String filenameBasis(final ITestResult tr)
   {
      final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH_mm_ss", Locale.GERMAN);
      final String destFileBase = tr.getMethod().getMethodName() + "-"
            + dateFormat.format(new Date(tr.getStartMillis()));
      return destFileBase;
   }

   public static String findDestinationDir(String type)
   {
      return findBasedir() + "target/surefire-reports/artifact-test-files/" + type;
   }

   private static String findBasedir()
   {
      String basedir = (String) System.getProperties().get("basedir");
      if (basedir == null)
      {
         basedir = "";
      }
      else
      {
         basedir += "/";
      }
      return basedir;
   }

}
