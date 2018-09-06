package org.vaadin.bdd.ui.views;

import org.jbehave.core.annotations.Then;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.vaadin.bdd.AbstractStep;
import org.vaadin.bdd.AbstractStory;

import java.util.Collections;
import java.util.List;

/**
 * MenuIT test rewritten as a BDD story.
 */
public class MenuIT extends AbstractStory {

	@Override
	public List<Class<? extends AbstractStep>> getStepClasses() {
		return Collections.singletonList(StorySteps.class);
	}

	public static class StorySteps extends AbstractStep {
		@Then("I see menu link $linkCaption")
		public void assertMenuLinkExists(String linkCaption) {
			Assert.assertNotNull(getMenuLink(linkCaption));
		}

		@Then("I don't see menu link $linkCaption")
		public void assertMenuLinkDoesntExist(String linkCaption) {
			Assert.assertNull(getMenuLink(linkCaption));
		}

		private WebElement getMenuLink(String linkCaption) {
			MenuElement menu = $(MenuElement.class).first();
			return menu.getMenuLink(linkCaption);
		}
	}

}
