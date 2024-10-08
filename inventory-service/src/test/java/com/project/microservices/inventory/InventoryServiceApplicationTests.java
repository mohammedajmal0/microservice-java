package com.project.microservices.inventory;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {
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
	void testInventory() {
		var response=RestAssured.given()
				.when()
				.get("/api/inventory?skuCode=iphone_15&quantity=100")
				.then()
				.log().all()
				.statusCode(200)
				.extract().response().as(Boolean.class);
				assertTrue(response);
		var negResponse=RestAssured.given()
				.when()
				.get("/api/inventory?skuCode=iphone_15&quantity=101")
				.then()
				.log().all()
				.statusCode(200)
				.extract().response().as(Boolean.class);
				assertFalse(negResponse);
	}

}
