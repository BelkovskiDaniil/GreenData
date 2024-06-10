package com.example.testtask;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TesttaskApplicationTestsDeposits {

    private static final String DEPOSIT_ID = "1";
    private static final String SORT = "dateOpen";
    private static final String FILTER = "monthPeriod";
    private static final Integer FILTER_VALUE = 1;
    private static final String CLIENT_ID = "1";
    private static final String BANK_ID = "1";

    boolean checkSortingAndFiltering(Response response, String sortBy, String filterBy, Integer filterValue) {
        List<Map<String, Object>> deposits = response.jsonPath().getList(".");
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

        boolean isFiltered = deposits.stream()
                .allMatch(deposit -> filterValue.equals(deposit.get(filterBy)));

        if (!isFiltered) {
            System.out.println("Ошибка филтьтрации!");
            return false;
        }

        for (int i = 0; i < deposits.size() - 1; i++) {
            ZonedDateTime date1 = ZonedDateTime.parse((String) deposits.get(i).get(sortBy), formatter);
            ZonedDateTime date2 = ZonedDateTime.parse((String) deposits.get(i + 1).get(sortBy), formatter);
            if (date1.isAfter(date2)) {
                System.out.println("Ошибка сортировки!");
                return false;
            }
        }

        return true;
    }



    @Test
    void testCreateDepositCorrectly() {
        RestAssured.baseURI = "http://localhost:8080/api/v1";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        JSONObject requestBody = new JSONObject();
        requestBody.put("percent", 1);
        requestBody.put("dateOpen", "2024-09-28");
        requestBody.put("monthPeriod", 1);

        Response response = request.body(requestBody.toString())
                .post("/deposit?client_id=" + CLIENT_ID + "&bank_id=" + BANK_ID)
                .prettyPeek();

        int statusCode = response.getStatusCode();
        if (statusCode == 200) {
            System.out.println("Депозит добавлен!");
        } else {
            System.out.println("Ошибка. Статус код: " + statusCode);
        }

        response.then().statusCode(200);
    }


    @Test
    void testCreateDepositValidation() {
        RestAssured.baseURI = "http://localhost:8080/api/v1";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        JSONObject requestBody = new JSONObject();
        requestBody.put("percent", 67);
        requestBody.put("dateOpen", "2024-09-28"); // Исправлен формат даты
        requestBody.put("monthPeriod", 52);

        Response response = request.body(requestBody.toString())
                .post("/deposit?client_id=" + CLIENT_ID + "&bank_id=" + BANK_ID)
                .prettyPeek();

        int statusCode = response.getStatusCode();
        if (statusCode != 200) {
            System.out.println("Корректно поймана ошибка! Статус код: " + statusCode);
        } else {
            System.out.println("Ошибка упущена.");
        }

        response.then().statusCode(400);
    }

    @Test
    void testUpdateDepositCorrectly() {
        RestAssured.baseURI = "http://localhost:8080/api/v1";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        JSONObject requestBody = new JSONObject();
        requestBody.put("percent", 1);
        requestBody.put("dateOpen", "2024-09-28");
        requestBody.put("monthPeriod", 1);

        Response response = request.body(requestBody.toString())
                .put("/deposit/" + DEPOSIT_ID + "?client_id=" + CLIENT_ID + "&bank_id=" + BANK_ID)
                .prettyPeek();

        int statusCode = response.getStatusCode();
        if (statusCode == 200) {
            System.out.println("Депозит обновлен!");
        } else {
            System.out.println("Ошибка. Статус код: " + statusCode);
        }

        response.then().statusCode(200);
    }

    @Test
    void testUpdateDepositValidation() {
        RestAssured.baseURI = "http://localhost:8080/api/v1";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        JSONObject requestBody = new JSONObject();
        requestBody.put("percent", 12421);
        requestBody.put("dateOpen", "2024-09-28"); // Исправлен формат даты
        requestBody.put("monthPeriod", 23131);

        Response response = request.body(requestBody.toString())
                .put("/deposit/" + DEPOSIT_ID + "?client_id=" + CLIENT_ID + "&bank_id=" + BANK_ID)
                .prettyPeek();

        int statusCode = response.getStatusCode();
        if (statusCode != 200) {
            System.out.println("Корректно поймана ошибка! Статус код: " + statusCode);
        } else {
            System.out.println("Ошибка упущена.");
        }

        response.then().statusCode(400);
    }

    @Test
    void testGetDepositWithSortAndFilter() {
        RestAssured.baseURI = "http://localhost:8080/api/v1";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");

        Response response = request.get("/deposit?sort_by=" + SORT + "&filter_by=" + FILTER + "&filter_value=" + String.valueOf(FILTER_VALUE))
                .prettyPeek();

        int statusCode = response.getStatusCode();
        if (statusCode == 200) {
            System.out.println("Получен ответ от сервера!");
            String responseBody = response.getBody().asString();
            System.out.println("Ответ сервера: " + responseBody);
            boolean isSortedAndFiltered = checkSortingAndFiltering(response, SORT, FILTER, FILTER_VALUE);
            if (isSortedAndFiltered) {
                System.out.println("Сортировка и фильтрация выполнены корректно.");
            } else {
                System.out.println("Ошибка в сортировке или фильтрации.");
            }
        } else {
            System.out.println("Ошибка. Статус код: " + statusCode);
        }

        response.then().statusCode(200);
    }

    @Test
    void testDeleteDepositById() {
        RestAssured.baseURI = "http://localhost:8080/api/v1";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");

        Response response = request.delete("/deposit/" + DEPOSIT_ID)
                .prettyPeek();

        int statusCode = response.getStatusCode();
        if (statusCode == 200) {
            System.out.println("Депозит успешно удален!");
        } else {
            System.out.println("Ошибка при удалении депозита. Статус код: " + statusCode);
        }

        response.then().statusCode(200);
    }

}
