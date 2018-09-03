package org.vaadin.bdd.ui.views.orderedit;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.vaadin.bdd.app.HasLogger;
import org.vaadin.bdd.backend.data.OrderState;
import com.vaadin.ui.ComboBox;

@SpringComponent
@ViewScope
public class OrderStateSelect extends ComboBox<OrderState> implements HasLogger {

	public OrderStateSelect() {
		setEmptySelectionAllowed(false);
		setTextInputAllowed(false);
		setItems(OrderState.values());
		setItemCaptionGenerator(OrderState::getDisplayName);
	}

}
