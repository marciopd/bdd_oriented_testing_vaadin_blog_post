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

	public void onBeforeScenario(Runnable beforeScenarioListener) {
		this.beforeScenarioListener = beforeScenarioListener;
	}

	public void onAfterScenario(Runnable afterScenarioListener) {
		this.afterScenarioListener = afterScenarioListener;
	}

	@BeforeScenario(uponType = ScenarioType.ANY)
	public void beforecenario() {
		if (beforeScenarioListener != null) {
			beforeScenarioListener.run();
		}
	}

	@AfterScenario(uponType = ScenarioType.ANY)
	public void afterScenario() {
		if (afterScenarioListener != null) {
			afterScenarioListener.run();
		}
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
