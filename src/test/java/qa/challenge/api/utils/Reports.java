package qa.challenge.api.utils;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import qa.challenge.api.base.BaseTest;

public class Reports extends BaseTest {

    public static void attachmentsAllure(Response response) {
        try {
            if (response != null) {
                String responseDetails =
                                "Status Code: " + response.getStatusCode() + "\n" +
                                "Headers: " + response.getHeaders().toString() + "\n" +
                                "Body: " + response.getBody().asString();

                Allure.addAttachment("API Response", "text/plain", responseDetails);
            }
        } catch (Exception e) {
            logger.error("Erro ao tentar anexar logs ao Allure Report.", e);
            throw new RuntimeException("Failed to attach response to Allure report: " + e.getMessage(), e);
        }
    }
}