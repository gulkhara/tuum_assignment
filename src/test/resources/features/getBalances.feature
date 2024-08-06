Feature: Get Balances

  Scenario: Get balances for an existing account
    Given I have a valid token
    And I have a valid account Id
    When I get balances for an existing account
    Then Response status code is 200
    And I should receive valid balance information

  Scenario: Get balances for a non-existing account
    Given I have a valid token
    When I get balances for a non-existing account
    Then Response status code is 200
    And I should receive empty data


