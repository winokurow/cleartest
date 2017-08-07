package de.clearit.test.framework.elemente;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import de.clearit.test.framework.Check;
import de.clearit.test.framework.ExecutionTimer;
import de.clearit.test.framework.ExecutionTimerManager;
import de.clearit.test.framework.PageObject;
import de.clearit.test.pages.LoggedInPage;

public class Test2Page extends PageObject{
	@Check
	GuiElement message = new GuiElement(By.id("message"));
	
	public Test2Page(WebDriver driver)
	{
		this.title="Seite2";
		this.driver = driver;
		this.waitForMainElementsIsShown();
	}
}
