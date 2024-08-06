# tuum_assignment

The tech stack used in writing the automated tests include JAVA, Cucumber, JUnit, RestAssured, Gson, and Allure. 
The following endpoints have been included in the scope of API testing:

- Create Account: /api/v4/persons/{customerId}/accounts
- Get Balances: /api/v1/accounts/{accountId}/balances
- Create Transactions: /api/v5/accounts/{accountId}/transactions 

Below is the short summary of scenarios covering both positive and negative test cases for each endpoint, and details of each scenario 
can be found in the respective feature file e.g. /src/test/resources/features/createAccount.feature. In addition, the actual API request calls 
and response assertions can be found under src/test/java/tuumAutomation/stepDefinitions/StepDefinitions.java.

Scenario 1: Create Account
- Create account with valid data
- Create account with missing required fields
- Create account with invalid data

Scenario 2: Get Balances
- Get balances for an existing account
- Get balances for a non-existing account

Scenario 3: Create Transactions
- Create transaction with valid data
- Create transaction with invalid data
- Create transaction for a non-existent account

## Running locally

Make sure you have DOCKER installed and running on your machine

```sh
git clone https://github.com/gulkhara/tuum_assignment.git # or download zip from github and unzip
cd tuum_assignment
```

### Run containers

```sh
docker-compose up --build
```
After tests are finished, Allure report is generated. Report can be found by following link:
http://localhost:5252/allure-docker-service-ui/projects/default
