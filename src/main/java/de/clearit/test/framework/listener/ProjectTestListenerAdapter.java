/*
 *
 * Copyright (C) 2015 Sparda-Datenverarbeitung eG
 * Freiligrathstrasse 32, 90482 Nuernberg, Germany
 *
 * This software is the confidential and proprietary information of
 * Sparda-Datenverarbeitung eG ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sparda-Datenverarbeitung eG.
 */
package de.clearit.test.framework.listener;

import org.testng.ITestResult;

public interface ProjectTestListenerAdapter
{

   public void onTestStart(final ITestResult tr);

   public void onTestFailure(final ITestResult tr);

   public void onTestSuccess(final ITestResult tr);

   public void onTestSkipped(final ITestResult tr);

}
