import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import com.jayway.jsonpath.JsonPath;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LicenceSteps {

    private String licenceJson;

    @Given("I have a Licence object")
    public void iHaveALicenceObject() {
        licenceJson = "{\"licenceId\":\"12345\",\"holderName\":\"John Doe\",\"startDate\":\"2023-01-01\",\"expiryDate\":\"2024-01-01\",\"licenceType\":\"Full\",\"restrictions\":[\"No heavy machinery\",\"Daytime only\"],\"conditions\":[\"Must wear safety glasses\",\"Must attend annual training\",\"Must report any incidents immediately\"],\"issuingAuthority\":\"DMV\",\"status\":\"Active\",\"endorsements\":[\"Class A\",\"Class B\"]}";
    }

    @When("I check the value of {string}")
    public void iCheckTheValueOf(String jsonpath) {
        // Intentionally left empty
    }

    @Then("it should be equal to {string}")
    public void itShouldBeEqualTo(String expectedValue, DataTable dataTable) {
        dataTable.asMaps().forEach(row -> {
            String jsonpath = row.get("jsonpath");
            String value = row.get("value");

            Object actualValue;
            try {
                actualValue = JsonPath.read(licenceJson, jsonpath);
            } catch (Exception e) {
                fail("Error reading JSONPath: " + jsonpath + ", Error: " + e.getMessage());
                return;
            }

            if (value.startsWith("date:")) {
                // Handle date comparison
                String[] parts = value.split(":");
                if (parts.length != 3) {
                    fail("Invalid date format in value column: " + value);
                    return;
                }
                String dateValue = parts[1];
                String dateFormat = parts[2];

                try {
                    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
                    Date expectedDate = formatter.parse(dateValue);

                    if (actualValue == null || !(actualValue instanceof String)) {
                        fail("Actual value at JSONPath " + jsonpath + " is null or not a string.");
                        return;
                    }

                    String actualDateString = (String) actualValue;
                    Date actualDate = formatter.parse(actualDateString);
                    assertEquals(expectedDate, actualDate);
                } catch (Exception e) {
                    fail("Date comparison failed: " + e.getMessage());
                }
            } else if (value.isEmpty()) {
                // Handle empty values
                assertTrue(actualValue == null || (actualValue instanceof List && ((List<?>) actualValue).isEmpty()));
            } else {
                // Handle other data types
                if (actualValue instanceof String) {
                    assertEquals(value, actualValue);
                } else if (actualValue instanceof Integer) {
                    assertEquals(Integer.parseInt(value), actualValue);
                } else {
                    fail("Unsupported data type for comparison at JSONPath: " + jsonpath);
                }
            }
        });
    }
}
