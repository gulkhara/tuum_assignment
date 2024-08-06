package tuumAutomation.stepDefinitions;

import com.google.gson.JsonObject;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;


public class StepDefinitions {
    private String token;
    private Response response;
    private String accountId;
    private final String customerId = "123456789";

    private final String AUTH_URL = "https://auth-api.sandbox.tuumplatform.com";
    private final String ACCOUNT_URL = "https://account-api.sandbox.tuumplatform.com";
    private final Map<String, String> headers = new HashMap<>();


    @Given("I have valid authentication credentials")
    public void iHaveValidAuthenticationCredentials() {
    }

    @When("I authenticate with the API")
    public void iAuthenticateWithTheAPI() {
        JsonObject payload = new JsonObject();
        payload.addProperty("username", "modular.system");
        payload.addProperty("password", "pass");

        response = given()
                .contentType("application/json")
                .body(payload)
                .post(AUTH_URL + "/api/v1/employees/authorise");

        response.then().log().all();
        token = response.jsonPath().getString("data.token");
    }

    @Then("I should receive a valid token")
    public void iShouldReceiveAValidToken() {
        responseStatusCodeIs(200);
        response.then().body("data.token", notNullValue());
    }

    @Given("I have a valid token")
    public void iHaveAValidToken() {
        if (token == null) {
            iHaveValidAuthenticationCredentials();
            iAuthenticateWithTheAPI();
        }
        headers.put("x-auth-token", token);
        headers.put("x-channel-code", "SYSTEM");
        headers.put("x-tenant-code", "MB");
    }

    @When("I create an account with valid data")
    public void iCreateAnAccountWithValidData() {
        JsonObject payload = new JsonObject();
        payload.addProperty("personName", "Peter Alexander Schmidt");
        payload.addProperty("residencyCountryCode", "DE");
        payload.addProperty("currencyCode", "EUR");
        payload.addProperty("accountTypeCode", "INTERNAL");

        response = given()
                .headers(headers)
                .contentType("application/json")
                .body(payload)
                .post(ACCOUNT_URL + "/api/v4/persons/" + customerId + "/accounts");

        response.then().log().all();
    }

    @Then("Response status code is {int}")
    public void responseStatusCodeIs(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @And("the account should be created successfully")
    public void theAccountShouldBeCreatedSuccessfully() {
        response.then().body("data.accountId", notNullValue());
        accountId = response.jsonPath().getString("data.accountId");
    }

    @When("I create an account with missing required {string}")
    public void iCreateAnAccountWithMissingRequiredField(String field) {
        JsonObject payload = new JsonObject();
        payload.addProperty("personName", "Peter Alexander Schmidt");
        payload.addProperty("residencyCountryCode", "DE");
        payload.addProperty("currencyCode", "EUR");
        payload.addProperty("accountTypeCode", "INTERNAL");

        // Remove required field
        payload.remove(field);

        response = given()
                .headers(headers)
                .contentType("application/json")
                .body(payload)
                .post(ACCOUNT_URL + "/api/v4/persons/" + "12345" + "/accounts");

        response.then().log().all();
    }

    @And("I should receive validation error {string} for {string}")
    public void iShouldReceiveValidationError(String error, String field) {
        Assert.assertEquals(response.body().jsonPath().getString("validationErrors.field[0]"), field);
        Assert.assertEquals(response.body().jsonPath().getString("validationErrors.errors[0].code[0]"), error);
    }

    @When("I create an account with invalid {string} for {string}")
    public void iCreateAnAccountWithInvalidData(String invalidValue, String field) {
        JsonObject payload = new JsonObject();
        payload.addProperty("personName", "Peter Alexander Schmidt");
        payload.addProperty("residencyCountryCode", "DE");
        payload.addProperty("currencyCode", "EUR");
        payload.addProperty("accountTypeCode", "INTERNAL");

        // Remove the relevant field and add invalid value
        payload.remove(field);
        payload.addProperty(field, invalidValue);

        response = given()
                .headers(headers)
                .contentType("application/json")
                .body(payload)
                .post(ACCOUNT_URL + "/api/v4/persons/" + customerId + "/accounts");

        response.then().log().all();
    }

    @And("I should receive {string}")
    public void iShouldReceiveError(String errorCode) {
        Assert.assertEquals(response.body().jsonPath().getString("validationErrors.errors[0].code[0]"), errorCode);
    }

    @And("I have a valid account Id")
    public void iHaveValidAccountID() {
        if (accountId == null) {
           iCreateAnAccountWithValidData();
           theAccountShouldBeCreatedSuccessfully();
        }
    }


    @When("I get balances for an existing account")
    public void iGetBalancesForExistingAccount() {
        response = given()
                .headers(headers)
                .get(ACCOUNT_URL + "/api/v1/accounts/" + accountId + "/balances");
        response.then().log().all();
    }

    @And("I should receive valid balance information")
    public void iShouldReceiveValidBalanceInformation() {
        response.then().body("data.balanceId", notNullValue());
    }

    @When("I get balances for a non-existing account")
    public void iGetBalancesForNonExistingAccount() {
        response = given()
                .headers(headers)
                .get(ACCOUNT_URL + "/api/v1/accounts/invalidAccount/balances");
        response.then().log().all();
    }

    @And("I should receive empty data")
    public void iShouldReceiveEmptyData() {
        response.then().body("data.balanceId", empty());
    }

    @When("I create a transaction with transaction type {string}")
    public void iCreateATransaction(String transactionType) {
        JsonObject money = new JsonObject();
        money.addProperty("amount", 100);
        money.addProperty("currencyCode", "EUR");

        JsonObject payload = new JsonObject();
        payload.add("money", money);
        payload.addProperty("transactionTypeCode", transactionType);

        response = given()
                .headers(headers)
                .contentType("application/json")
                .body(payload)
                .post(ACCOUNT_URL + "/api/v5/accounts/" + accountId + "/transactions");
        response.then().log().all();
    }

    @And("the transaction should be created successfully")
    public void theTransactionShouldBeCreatedSuccessfully() {
        response.then().body("data.accountTransactionId", notNullValue());
    }

    @And("I should receive {string} error")
    public void iShouldReceiveABadRequestWithError(String error) {
        Assert.assertEquals(response.body().jsonPath().getString("validationErrors.errors[0].code[0]"), error);
    }


    @And("I don't have a valid account id")
    public void iDontHaveAValidAccountId() {
    }
}
