package com.coreglobal.transfer.application;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;

public class TransferSteps {
    private BigDecimal available;
    private BigDecimal remaining;
    private String status;

    @Given("a customer has {int} dollars available")
    public void available(int amount) { available = BigDecimal.valueOf(amount); }

    @When("the customer transfers {int} dollars")
    public void transfer(int amount) {
        BigDecimal requested = BigDecimal.valueOf(amount);
        status = available.compareTo(requested) >= 0 ? "ACCEPTED" : "REJECTED";
        remaining = "ACCEPTED".equals(status) ? available.subtract(requested) : available;
    }

    @Then("the transfer is accepted")
    public void accepted() { assertThat(status).isEqualTo("ACCEPTED"); }

    @Then("the transfer is rejected")
    public void rejected() { assertThat(status).isEqualTo("REJECTED"); }

    @Then("the remaining balance is {int} dollars")
    public void remaining(int amount) { assertThat(remaining).isEqualByComparingTo(BigDecimal.valueOf(amount)); }
}
