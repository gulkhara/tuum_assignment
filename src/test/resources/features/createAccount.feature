Feature: Create Account

  Scenario: Create account with valid data
    Given I have a valid token
    When I create an account with valid data
    Then Response status code is 200
    And the account should be created successfully


  Scenario Outline: Create account with missing required fields
    Given I have a valid token
    When I create an account with missing required "<field>"
    Then Response status code is 400
    And I should receive validation error "<error code>" for "<field>"

    Examples:
      | field                | error code   |
      | accountTypeCode      | err.notNull  |
      | currencyCode         | err.notBlank |
      | residencyCountryCode | err.notBlank |
      | personName           | err.notBlank |


  Scenario Outline: Create account with invalid data
    Given I have a valid token
    When I create an account with invalid "<value>" for "<field>"
    Then Response status code is 400
    And I should receive "<error code>"
    Examples:
      | field                | value   | error code                      |
      | accountTypeCode      | invalid | err.invalidValue                |
      | currencyCode         | invalid | err.length                      |
      | residencyCountryCode | ZZ      | err.residencyCountryCodeInvalid |

