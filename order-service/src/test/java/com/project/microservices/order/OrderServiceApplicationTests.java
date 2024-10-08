package com.project.microservices.order;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {
	@ServiceConnection
	static MySQLContainer mySQLContainer=new MySQLContainer("mysql:8.3.0");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setUp() {
        RestAssured.baseURI="http://localhost:"+port;
    }
	static {
		mySQLContainer.start();
	}
	@Test
	void shouldPlaceOrder() {
		String submitOrder= """
				{
				    "skuCode":"iphone_15",
				    "price":"700",
				    "quantity":1
				}
				""";
		var responseBody= RestAssured.given()
				.contentType("application/json")
				.body(submitOrder)
				.when()
				.post("http://localhost:"+port+"/api/order")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().asString();
//		assertThat(responseBody.equalTo("Order placed successfully"));
	}

}
