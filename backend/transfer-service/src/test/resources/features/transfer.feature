Feature: Secure account transfers
  As a Core Global client
  I want transfers to be validated before movement
  So that my money remains protected

  Scenario: Schedule a valid internal transfer
    Given a customer has 2500 dollars available
    When the customer transfers 500 dollars
    Then the transfer is accepted
    And the remaining balance is 2000 dollars

  Scenario: Reject a transfer above available funds
    Given a customer has 250 dollars available
    When the customer transfers 500 dollars
    Then the transfer is rejected

