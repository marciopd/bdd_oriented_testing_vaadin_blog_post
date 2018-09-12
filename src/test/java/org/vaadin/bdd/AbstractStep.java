package org.vaadin.bdd;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.ScenarioType;
import org.openqa.selenium.WebDriver;

/**
 * Abstract class to be inherited by all BDD step classes.
 * <p>
 * Provides integration with Vaadin Test Bench and some very common steps as logging in with different users.
 */
public abstract class AbstractStep extends AbstractIT {

	private Runnable beforeScenarioListener;
	private Runnable afterScenarioListener;
    private Runnable afterScenarioFailureListener;

	public void onBeforeScenario(Runnable beforeScenarioListener) {
		this.beforeScenarioListener = beforeScenarioListener;
	}

	public void onAfterScenario(Runnable afterScenarioListener) {
		this.afterScenarioListener = afterScenarioListener;
	}

    public void onAfterScenarioFailure(Runnable afterScenarioFailureListener) {
        this.afterScenarioFailureListener = afterScenarioFailureListener;
    }

	@BeforeScenario(uponType = ScenarioType.ANY)
	public void beforecenario() {
		if (beforeScenarioListener != null) {
			beforeScenarioListener.run();
		}
	}

	@AfterScenario(uponOutcome = AfterScenario.Outcome.SUCCESS)
	public void afterScenarioSuccess() {
		runAfterScenarioListener();
	}

	private void runAfterScenarioListener() {
		if (afterScenarioListener != null) {
			afterScenarioListener.run();
		}
	}

	@AfterScenario(uponOutcome = AfterScenario.Outcome.FAILURE)
	public void afterScenarioFailure() {
	    if (afterScenarioFailureListener != null) {
            afterScenarioFailureListener.run();
        }
		runAfterScenarioListener();
	}

	@Given("I log in as admin")
	public void loginAsAdminStep() {
		loginAsAdmin();
	}

	@Given("I log in as barista")
	public void loginAsBaristaStep() {
		loginAsBarista();
	}

	@FunctionalInterface
	interface DriverGetter {
		WebDriver getDriver();
	}

}
