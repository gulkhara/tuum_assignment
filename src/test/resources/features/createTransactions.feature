Feature: Create Transactions

  Scenario: Create transaction with valid data
    Given I have a valid token
    And I have a valid account Id
    When I create a transaction with transaction type "CARD_PUR"
    Then Response status code is 200
    And the transaction should be created successfully

  Scenario: Create transaction with invalid data
    Given I have a valid token
    And I have a valid account Id
    When I create a transaction with transaction type "ACC2ACC_RECEIVABLE"
    Then Response status code is 400
    And I should receive "err.transactionNotAllowed" error

  Scenario: Create transaction for a non-existent account
    Given I have a valid token
    And I don't have a valid account id
    When I create a transaction with transaction type "CARD_PUR"
    Then Response status code is 404
    And I should receive "err.notFound" error

