Narrative:
As an admin
I want to see menu items restricted to admins
and other users shouldn't be able to do it

Scenario: admin sees menu items restricted to admins
Given I log in as admin
Then I see menu link Users
And I see menu link Products

Scenario: barista doesn't see menu items restricted to admins
Given I log in as barista
Then I don't see menu link Users
And I don't see menu link Products
