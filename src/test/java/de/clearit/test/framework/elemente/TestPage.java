package de.clearit.test.framework.elemente;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import de.clearit.test.framework.Check;
import de.clearit.test.framework.ExecutionTimer;
import de.clearit.test.framework.ExecutionTimerManager;
import de.clearit.test.framework.PageObject;
import de.clearit.test.pages.LoggedInPage;

public class TestPage extends PageObject{
	@Check
	GuiElement lastname = new GuiElement(By.id("lastname"));
	
	GuiElement firstname = new GuiElement(By.id("firstname"));
	public TestPage(WebDriver driver)
	{
		this.driver = driver;
		ExecutionTimer exec = new ExecutionTimer();
		exec.init("timmm");
		ExecutionTimerManager.setExecutionTimer(exec);

	}
}
