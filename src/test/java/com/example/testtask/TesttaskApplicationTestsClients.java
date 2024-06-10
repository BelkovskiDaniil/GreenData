package com.example.testtask;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

class TesttaskApplicationTestsClients {

	private static final String CLIENT_ID = "2";
	private static final String SORT = "form";
	private static final String FILTER = "name";
	private static final String FILTER_VALUE = "test";

	boolean checkSortingAndFiltering(Response response, String sortBy, String filterBy, String filterValue) {
		List<Map<String, String>> clients = response.jsonPath().getList("");

		boolean isFiltered = clients.stream()
				.allMatch(client -> client.get(filterBy).equals(filterValue));

		if (!isFiltered) {
			System.out.println("Ошибка филтьтрации!");
			return false;
		}

		for (int i = 0; i < clients.size() - 1; i++) {
			String form1 = clients.get(i).get(sortBy);
			String form2 = clients.get(i + 1).get(sortBy);
			if (form1.compareTo(form2) > 0) {
				System.out.println("Ошибка сортировки!");
				return false;
			}
		}

		return true;
	}

	@Test
	void testCreateClientCorrectly() {
		RestAssured.baseURI = "http://localhost:8080/api/v1";
		RequestSpecification request = given();
		request.header("Content-Type", "application/json");
		String requestBody = "{\n" +
				"    \"name\":\"testtest\",\n" +
				"    \"shortName\":\"testtest\",\n" +
				"    \"address\":\"testtest\",\n" +
				"    \"form\":\"A\"\n" +
				"}";
		Response response = request.body(requestBody)
				.post("/client")
				.prettyPeek();

		int statusCode = response.getStatusCode();
		if (statusCode == 200) {
			System.out.println("Пользователь добавлен!");
		} else {
			System.out.println("Ошибка. Статус код: " + statusCode);
		}

		response.then().statusCode(200);
	}

	@Test
	void testCreateClientValidation() {
		RestAssured.baseURI = "http://localhost:8080/api/v1";
		RequestSpecification request = given();
		request.header("Content-Type", "application/json");
		String requestBody = "{\n" +
				"    \"name\":\"\",\n" +
				"    \"shortName\":\"\",\n" +
				"    \"address\":\"\",\n" +
				"    \"form\":\"A\"\n" +
				"}";
		Response response = request.body(requestBody)
				.post("/client")
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
	void testUpdateClientCorrectly() {
		RestAssured.baseURI = "http://localhost:8080/api/v1";
		RequestSpecification request = given();
		request.header("Content-Type", "application/json");
		String requestBody = "{\n" +
				"    \"name\":\"updatedtest\",\n" +
				"    \"shortName\":\"updatedtest\",\n" +
				"    \"address\":\"updatedtest\",\n" +
				"    \"form\":\"C\"\n" +
				"}";
		Response response = request.body(requestBody)
				.put("/client/" + CLIENT_ID)
				.prettyPeek();

		int statusCode = response.getStatusCode();
		if (statusCode == 200) {
			System.out.println("Пользователь обновлен!");
		} else {
			System.out.println("Ошибка. Статус код: " + statusCode);
		}

		response.then().statusCode(200);
	}

	@Test
	void testUpdateClientValidation() {
		RestAssured.baseURI = "http://localhost:8080/api/v1";
		RequestSpecification request = given();
		request.header("Content-Type", "application/json");
		String requestBody = "{\n" +
				"    \"name\":\"up\",\n" +
				"    \"shortName\":\"up\",\n" +
				"    \"address\":\"up\",\n" +
				"    \"form\":\"C\"\n" +
				"}";
		Response response = request.body(requestBody)
				.put("/client/" + CLIENT_ID)
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
	void testGetClientWithSortAndFilter() {
		RestAssured.baseURI = "http://localhost:8080/api/v1";
		RequestSpecification request = given();
		request.header("Content-Type", "application/json");

		Response response = request.get("/client?sort_by=" + SORT + "&filter_by=" + FILTER + "&filter_value=" + FILTER_VALUE)
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
	void testDeleteClientById() {
		RestAssured.baseURI = "http://localhost:8080/api/v1";
		RequestSpecification request = given();
		request.header("Content-Type", "application/json");

		Response response = request.delete("/client/" + CLIENT_ID)
				.prettyPeek();

		int statusCode = response.getStatusCode();
		if (statusCode == 200) {
			System.out.println("Клиент успешно удален!");
		} else {
			System.out.println("Ошибка при удалении клиента. Статус код: " + statusCode);
		}

		response.then().statusCode(200);
	}

}
