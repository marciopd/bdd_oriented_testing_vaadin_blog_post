package org.vaadin.bdd.ui.views.storefront;

import org.vaadin.bdd.ui.views.orderedit.OrderEditViewElement;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elementsbase.ServerClass;

@ServerClass("org.vaadin.bdd.ui.views.storefront.StorefrontView")
public class StorefrontViewElement extends StorefrontViewDesignElement {

	public OrderEditViewElement selectOrder(int index) {
		GridElement grid = getList();
		grid.getRow(index).click();
		return new ElementQuery<>(OrderEditViewElement.class).context(getDriver()).first();
	}

	public OrderEditViewElement clickNewOrder() {
		getNewOrder().click();
		return new ElementQuery<>(OrderEditViewElement.class).context(getDriver()).first();
	}

}