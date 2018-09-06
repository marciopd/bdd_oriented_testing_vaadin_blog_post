Scenario: barista is able to update order details
Given I log in as barista
When I select the first order
Then Cancel button should be displayed
And Save button should be displayed

Given I change the order date
And I change the order state
And I change the order customer
And I change the pickup location
And I change the order products
And I change the order total
When I click Save
Then Edit button should be displayed
And the order data should be updated
