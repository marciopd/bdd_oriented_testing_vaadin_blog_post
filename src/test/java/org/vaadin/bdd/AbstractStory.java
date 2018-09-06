package org.vaadin.bdd;

import com.vaadin.testbench.HasDriver;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.io.CasePreservingResolver;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract BDD story class.
 * <p>
 * Bootstraps JBehave configuration and Vaadin Test Bench as well.
 * JBehave out of the box works with JUnit, so you can benefit from @{@link Before}, @{@link Rule} and other JUnit features.
 */
public abstract class AbstractStory extends JUnitStory implements HasDriver {

	private static final int ONE = 1;
	private static final String STORY_TIMEOUT_MILLISECONDS = "30000";

	private final TestBenchBootstrap testBenchBootstrap = new TestBenchBootstrap();
	private final Configuration configuration;

	@Rule
	public ScreenshotOnFailureRule screenshotOnFailure = new ScreenshotOnFailureRule(this, true);

	public AbstractStory() {
		configuration = new MostUsefulConfiguration()
				.useStoryControls(
						new StoryControls().doResetStateBeforeStory(false))
				.useStoryPathResolver(new CasePreservingResolver(".story"))
				.useStoryReporterBuilder(new StoryReporterBuilder()
						.withCodeLocation(CodeLocations.codeLocationFromClass(this.getClass()))
						.withDefaultFormats()
						.withFormats(Format.CONSOLE, Format.TXT)
						.withFailureTrace(true));
		useConfiguration(configuration);

		configuredEmbedder().embedderControls()
				.useStoryTimeouts(STORY_TIMEOUT_MILLISECONDS)
				.useThreads(ONE)
				.doFailOnStoryTimeout(true)
				.doIgnoreFailureInStories(false)
				.doIgnoreFailureInView(false);
	}

	@Override
	public WebDriver getDriver() {
		WebDriver driver = testBenchBootstrap.getDriver();
		if (driver == null) {
			driver = testBenchBootstrap.buildDriver();
		}
		return driver;
	}

	private void quitDriver() {
		testBenchBootstrap.quit();
	}

	@Override
	public Configuration configuration() {
		return configuration;
	}

	@Override
	public InjectableStepsFactory stepsFactory() {
		return new InstanceStepsFactory(configuration(), getStepsInstances());
	}

	private Object[] getStepsInstances() {
		try {
			final List<Class<? extends AbstractStep>> stepClasses = getStepClasses();
			final List<Object> stepInstances = new ArrayList<>();
			for (Class stepsClass : stepClasses) {
				AbstractStep step = (AbstractStep) stepsClass.newInstance();
				step.onBeforeScenario(() -> setupDriver(step));
				step.onAfterScenario(this::quitDriver);
				stepInstances.add(step);
			}

			return stepInstances.toArray();
		} catch (Exception e) {
			throw new IllegalArgumentException("Failure instantiating step class.", e);
		}
	}

	private void setupDriver(AbstractStep step) {
		step.setDriver(getDriver());
	}

	/**
	 * Each story concrete class should implement this method returning all the step
	 * classes user on the story.
	 *
	 * @return list of step classes
	 */
	public abstract List<Class<? extends AbstractStep>> getStepClasses();

	/**
	 * Concrete implementation of {@link AbstractIT}.
	 * Uses code already available to bootstrap Vaadin Test Bench.
	 */
	private static class TestBenchBootstrap extends AbstractIT {

		WebDriver buildDriver() {
			quit();
			setup();
			return getDriver();
		}

		private void quit() {
			if (driver != null) {
				driver.quit();
				driver = null;
			}
		}
	}

}
