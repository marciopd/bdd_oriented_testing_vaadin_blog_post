package org.vaadin.bdd.ui.views;

import com.vaadin.data.ValueContext;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.vaadin.bdd.AbstractStep;
import org.vaadin.bdd.AbstractStory;
import org.vaadin.bdd.backend.data.OrderState;
import org.vaadin.bdd.backend.data.entity.Customer;
import org.vaadin.bdd.ui.utils.DollarPriceConverter;
import org.vaadin.bdd.ui.views.orderedit.OrderEditViewElement;
import org.vaadin.bdd.ui.views.orderedit.ProductInfoElement.ProductOrderData;
import org.vaadin.bdd.ui.views.orderedit.UpdateOrderIT;
import org.vaadin.bdd.ui.views.storefront.StorefrontViewElement;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.vaadin.bdd.ui.views.orderedit.OrderEditViewElement.OrderInfo;

/**
 * Former {@link UpdateOrderIT#updateOrderInfo()} test as a BDD story.
 */
public class UpdateOrderInfoIT extends AbstractStory {

	@Override
	public List<Class<? extends AbstractStep>> getStepClasses() {
		return Collections.singletonList(StorySteps.class);
	}

	public static class StorySteps extends AbstractStep {

		private StorefrontViewElement storeFront;
		private OrderEditViewElement orderEdit;
		private OrderState oldState;
		private OrderInfo currentOrder;
		private OrderInfo updatedOrder;

		@When("I select the first order")
		public void whenISelectTheFirstOrder() {
			storeFront = $(StorefrontViewElement.class).first();
			orderEdit = storeFront.selectOrder(1);
			oldState = OrderState.forDisplayName(orderEdit.getStateLabel().getText());
			orderEdit.getEditOrCancel().click();
		}

		@Then("Cancel button should be displayed")
		public void thenCancelButtonShouldBeDisplayed() {
			Assert.assertEquals("Cancel button has wrong caption", "Cancel", orderEdit.getEditOrCancel().getCaption());
		}

		@Then("Save button should be displayed")
		public void thenSaveButtonShouldBeDisplayed() {
			Assert.assertEquals("Save button has wrong caption", "Save", orderEdit.getOk().getCaption());
		}

		@Given("I change the order date")
		public void givenIChangeTheOrderDate() {
			currentOrder = orderEdit.getOrderInfo();
			updatedOrder = new OrderInfo();

			LocalDate newDate = currentOrder.dueDate.plusDays(1);
			orderEdit.getDueDate().setDate(newDate);
			updatedOrder.dueDate = newDate;
		}

		@Given("I change the order state")
		public void givenIChangeTheOrderState() {
			int nextStateIndex = (oldState.ordinal() + 1) % OrderState.values().length;
			OrderState newState = OrderState.values()[nextStateIndex];
			updatedOrder.state = newState;
			orderEdit.getState().selectByText(updatedOrder.state.getDisplayName());
		}

		@Given("I change the order customer")
		public void givenIChangeTheOrderCustomer() {
			Customer currentCustomer = currentOrder.customer;
			Customer updatedCustomer = new Customer();

			updatedCustomer.setFullName(currentCustomer.getFullName() + "-updated");
			updatedCustomer.setPhoneNumber(currentCustomer.getPhoneNumber() + "-updated");
			updatedCustomer.setDetails(currentCustomer.getDetails() + "-updated");
			updatedOrder.customer = updatedCustomer;
			orderEdit.setCustomerInfo(updatedCustomer);
		}

		@Given("I change the pickup location")
		public void givenIChangeThePickupLocation() {
			updatedOrder.pickupLocation = "Store".equals(currentOrder.pickupLocation) ? "Bakery" : "Store";
			orderEdit.getPickupLocation().selectByText(updatedOrder.pickupLocation);
		}

		@Given("I change the order products")
		public void givenIChangeTheOrderProducts() {
			updatedOrder.products = new ArrayList<>();
			for (int i = 0; i < currentOrder.products.size(); i++) {
				ProductOrderData updatedProduct = new ProductOrderData();
				updatedOrder.products.add(updatedProduct);
				ProductOrderData currentProduct = currentOrder.products.get(i);
				updatedProduct.setComment(currentProduct.getComment() + "-updated");
				updatedProduct.setQuantity(currentProduct.getQuantity() + 1);
				// Product is intentionally kept the same as we do not know what
				// products there are in the DB
				updatedProduct.setProduct(currentProduct.getProduct());
				updatedProduct.setPrice(currentProduct.getPrice());
			}

			orderEdit.setProducts(updatedOrder.products);
		}

		@Given("I change the order total")
		public void givenIChangeTheOrderTotal() {
			int updatedTotal = 0;
			for (ProductOrderData data : updatedOrder.products) {
				updatedTotal += data.getQuantity() * data.getPrice();
			}
			NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
			format.setMaximumFractionDigits(2);
			format.setMinimumFractionDigits(2);

			DollarPriceConverter convert = new DollarPriceConverter();
			updatedOrder.total = convert.convertToPresentation(updatedTotal, new ValueContext(Locale.US));
		}

		@When("I click Save")
		public void whenIClickSave() {
			orderEdit.getOk().click();
		}

		@Then("Edit button should be displayed")
		public void thenEditButtonShouldBeDisplayed() {
			Assert.assertEquals("Save failed", "Edit", orderEdit.getEditOrCancel().getCaption());
		}

		@Then("the order data should be updated")
		public void thenTheOrderDataShouldBeUpdated() {
			orderEdit.assertOrder(updatedOrder);
		}
	}

}
