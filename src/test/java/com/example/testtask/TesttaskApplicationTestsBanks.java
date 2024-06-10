package com.example.testtask;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TesttaskApplicationTestsBanks {

    private static final String BANK_ID = "1";
    private static final String SORT = "name";
    private static final String FILTER = "bankIdentificationCode";
    private static final Integer FILTER_VALUE = 12312321;

    boolean checkSortingAndFiltering(Response response, String sortBy, String filterBy, Integer filterValue) {
        List<Map<String, Object>> banks = response.jsonPath().getList("");

        boolean isFiltered = banks.stream()
                .allMatch(client -> filterValue.equals(client.get(filterBy)));

        if (!isFiltered) {
            System.out.println("Ошибка филтьтрации!");
            return false;
        }

        for (int i = 0; i < banks.size() - 1; i++) {
            String name1 = (String) banks.get(i).get(sortBy);
            String name2 = (String) banks.get(i + 1).get(sortBy);
            if (name1.compareTo(name2) > 0) {
                System.out.println("Ошибка сортировки!");
                return false;
            }
        }

        return true;
    }


    @Test
    void testCreateBankCorrectly() {
        RestAssured.baseURI = "http://localhost:8080/api/v1";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        String requestBody = "{\n" +
                "    \"name\":\"testcorrectly\",\n" +
                "    \"bankIdentificationCode\":12312321\n" +
                "}";
        Response response = request.body(requestBody)
                .post("/bank")
                .prettyPeek();

        int statusCode = response.getStatusCode();
        if (statusCode == 200) {
            System.out.println("Банк добавлен!");
        } else {
            System.out.println("Ошибка. Статус код: " + statusCode);
        }

        response.then().statusCode(200);
    }

    @Test
    void testCreateBankValidation() {
        RestAssured.baseURI = "http://localhost:8080/api/v1";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        String requestBody = "{\n" +
                "    \"name\":\"a\",\n" +
                "}";
        Response response = request.body(requestBody)
                .post("/bank")
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
    void testUpdateBankCorrectly() {
        RestAssured.baseURI = "http://localhost:8080/api/v1";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        String requestBody = "{\n" +
                "    \"name\":\"testcorrectlyupdated\",\n" +
                "    \"bankIdentificationCode\":123243235312321\n" +
                "}";
        Response response = request.body(requestBody)
                .put("/bank/" + BANK_ID)
                .prettyPeek();

        int statusCode = response.getStatusCode();
        if (statusCode == 200) {
            System.out.println("Банк обновлен!");
        } else {
            System.out.println("Ошибка. Статус код: " + statusCode);
        }

        response.then().statusCode(200);
    }

    @Test
    void testUpdateBankValidation() {
        RestAssured.baseURI = "http://localhost:8080/api/v1";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");
        String requestBody = "{\n" +
                "    \"name\":\"gr\",\n" +
                "    \"bankIdentificationCode\":123243235312321\n" +
                "}";
        Response response = request.body(requestBody)
                .put("/bank/" + BANK_ID)
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
    void testGetBankWithSortAndFilter() {
        RestAssured.baseURI = "http://localhost:8080/api/v1";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");

        Response response = request.get("/bank?sort_by=" + SORT + "&filter_by=" + FILTER + "&filter_value=" + String.valueOf(FILTER_VALUE))
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
    void testDeleteBankById() {
        RestAssured.baseURI = "http://localhost:8080/api/v1";
        RequestSpecification request = given();
        request.header("Content-Type", "application/json");

        Response response = request.delete("/bank/" + BANK_ID)
                .prettyPeek();

        int statusCode = response.getStatusCode();
        if (statusCode == 200) {
            System.out.println("Банк успешно удален!");
        } else {
            System.out.println("Ошибка при удалении банка. Статус код: " + statusCode);
        }

        response.then().statusCode(200);
    }

}
