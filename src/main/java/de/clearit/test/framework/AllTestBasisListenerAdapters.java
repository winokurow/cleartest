package de.clearit.test.framework;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import de.clearit.test.common.TestUtils;
import de.clearit.test.framework.listener.LogTest;
import de.clearit.test.framework.listener.ProjectTestListenerAdapter;
import de.clearit.test.framework.listener.TestCount;

public class AllTestBasisListenerAdapters extends TestListenerAdapter {

	// Logger
	private static Logger LOGGER = Logger.getLogger(AllTestBasisListenerAdapters.class.getName());

	// List mit allen benutzten Listener.
	private final List<ProjectTestListenerAdapter> testListenerAdapter = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * List mit allen benutzten Listener ausfüllen.
	 * 
	 */
	public AllTestBasisListenerAdapters() {
		testListenerAdapter.add(new LogTest());
		testListenerAdapter.add(new TestCount());
	}

	/**
	 * Wird ausgeführt wenn ein Test gestartet wird.
	 * 
	 * 
	 * @tr - TestResult Objekt
	 */
	@Override
	public void onTestStart(ITestResult tr) {
		initLog4jThreadContext();
		super.onTestStart(tr);
		for (ProjectTestListenerAdapter projectTestListenerAdapter : testListenerAdapter) {
			onTestStart(tr, projectTestListenerAdapter);
		}
	}

	/**
	 * Wird ausgeführt wenn ein Test fehlerhaft durchgelaufen ist.
	 * 
	 * Die Methode onTestFailure des jeden Listener aufrufen.
	 * 
	 * @tr - TestResult Objekt
	 */
	@Override
	public void onTestFailure(final ITestResult tr) {
		super.onTestFailure(tr);
		for (ProjectTestListenerAdapter projectTestListenerAdapter : testListenerAdapter) {
			logStartMethod(projectTestListenerAdapter);
			onTestFailure(tr, projectTestListenerAdapter);
			logEndMethod(projectTestListenerAdapter);
		}
		destroyLog4jThreadContext();
	}

	/**
	 * Wird ausgeführt wenn ein Test erfolgreich durchgelaufen ist.
	 * 
	 * Die Methode onTestSuccess des jeden Listener aufrufen.
	 * 
	 * @tr - TestResult Objekt
	 */
	@Override
	public void onTestSuccess(ITestResult tr) {
		super.onTestSuccess(tr);
		for (ProjectTestListenerAdapter projectTestListenerAdapter : testListenerAdapter) {
			logStartMethod(projectTestListenerAdapter);
			onTestSuccess(tr, projectTestListenerAdapter);
			logEndMethod(projectTestListenerAdapter);
		}
		destroyLog4jThreadContext();
	}

	/**
	 * Wird ausgeführt wenn ein Test übersprungen wird.
	 * 
	 * Die Methode onTestSkipped des jeden Listener aufrufen.
	 * 
	 * @tr - TestResult Objekt
	 */
	@Override
	public void onTestSkipped(ITestResult tr) {
		super.onTestSkipped(tr);
		for (ProjectTestListenerAdapter projectTestListenerAdapter : testListenerAdapter) {
			logStartMethod(projectTestListenerAdapter);
			onTestSkipped(tr, projectTestListenerAdapter);
			logEndMethod(projectTestListenerAdapter);
		}
		destroyLog4jThreadContext();
	}

	/**
	 * Etwas was passiert wird, wenn Test überspringen ist.
	 * 
	 * @param tr
	 *            - TestResult Objekt
	 * @param projectTestListenerAdapter
	 *            - Listener
	 */
	private void onTestSkipped(ITestResult tr, ProjectTestListenerAdapter projectTestListenerAdapter) {
		try {
			projectTestListenerAdapter.onTestSkipped(tr);
		}
		// muss Throwable sein, da sonst assertion errors die suite beenden
		catch (Throwable e) {
			LOGGER.error("Fehler beim test beenden mit skipped. ", e);
		}
	}

	/**
	 * Etwas was passiert wird, wenn Test fehlgeschlagen ist.
	 * 
	 * @tr - TestResult Objekt
	 * @param projectTestListenerAdapter
	 *            - Listener
	 */
	private void onTestFailure(final ITestResult tr, ProjectTestListenerAdapter projectTestListenerAdapter) {
		try {
			projectTestListenerAdapter.onTestFailure(tr);
		}
		// muss Throwable sein, da sonst assertion errors die suite beenden
		catch (Throwable e) {
			LOGGER.error("Fehler beim test beenden mit failure. ", e);
		}
	}

	/**
	 * Etwas was passiert wird, wenn Test erfolreich durchgelaufen ist.
	 * 
	 * @tr - TestResult Objekt
	 * @param projectTestListenerAdapter
	 *            - Listener
	 */
	private void onTestSuccess(ITestResult tr, ProjectTestListenerAdapter projectTestListenerAdapter) {
		try {
			projectTestListenerAdapter.onTestSuccess(tr);
		}
		// muss Throwable sein, da sonst assertion errors die suite beenden
		catch (Throwable e) {
			tr.setStatus(ITestResult.FAILURE);
			tr.setThrowable(e);
			LOGGER.error("Fehler beim test beenden mit success. ", e);
		}
	}

	/**
	 * Etwas was passiert wird, wenn Test gestartet wird.
	 * 
	 * @tr - TestResult Objekt
	 * @param projectTestListenerAdapter
	 *            - Listener
	 */
	private void onTestStart(ITestResult tr, ProjectTestListenerAdapter projectTestListenerAdapter) {
		try {
			projectTestListenerAdapter.onTestStart(tr);
		}
		// muss Throwable sein, da sonst assertion errors die suite beenden
		catch (Throwable e) {
			LOGGER.error("Fehler beim test start. ", e);
		}
	}

	/**
	 * Log4J Nested Diagnostic Context vorbereiten (Thread Nummer präfix)
	 */
	private void initLog4jThreadContext() {
		NDC.pop();
		NDC.push("T" + Thread.currentThread().getId());
	}

	/**
	 * Log4J Nested Diagnostic Context deaktivieren
	 */
	private void destroyLog4jThreadContext() {
		NDC.pop();
	}

	/**
	 * Log den Start der Tests
	 * 
	 * @param projectTestListenerAdapter
	 *            - der Listener
	 */
	private void logStartMethod(ProjectTestListenerAdapter projectTestListenerAdapter) {
		LOGGER.debug(String.format("%s %s start", projectTestListenerAdapter.getClass().getSimpleName(),
				TestUtils.getSubMethodName()));
	}

	/**
	 * Log den End der Tests
	 * 
	 * @param projectTestListenerAdapter
	 *            - der Listener
	 */
	private void logEndMethod(ProjectTestListenerAdapter projectTestListenerAdapter) {
		LOGGER.debug(String.format("%s %s finished", projectTestListenerAdapter.getClass().getSimpleName(),
				TestUtils.getSubMethodName()));
	}

}
