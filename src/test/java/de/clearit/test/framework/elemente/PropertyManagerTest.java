package de.clearit.test.framework.elemente;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import de.clearit.test.framework.ErrorBoxUtil;
import de.clearit.test.framework.ExecutionTimer;
import de.clearit.test.framework.ExecutionTimerManager;
import de.clearit.test.framework.PropertyManager;
import de.clearit.test.pages.HinweisPage;

public class PropertyManagerTest
{
	   @Test
	   public void testGetPropertyNull()
	   {
		   PropertyManager propertyManager = PropertyManager.getInstance();
		   Assert.assertTrue(propertyManager.getProperty(null, "") == null);
	   }
	   
	   @Test
	   public void testGetPropertyDefault()
	   {
		   PropertyManager propertyManager = PropertyManager.getInstance();
		   Assert.assertEquals(propertyManager.getProperty("sdsd", "testtest"), "testtest");
	   }
	   
	   @Test
	   public void testGetProperty()
	   {
		   PropertyManager propertyManager = PropertyManager.getInstance();
		   Assert.assertEquals(propertyManager.getProperty("login.timeout", "150"), "180");
	   }
	   
	   @Test
	   public void testGetPropertyNullDefault()
	   {
		   PropertyManager propertyManager = PropertyManager.getInstance();
		   Assert.assertTrue(propertyManager.getProperty("sdsd") == null);
	   }
}
