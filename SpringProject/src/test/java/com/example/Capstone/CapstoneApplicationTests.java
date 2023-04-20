package com.example.Capstone;

import org.example.Facade.DataBaseFacade;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class CapstoneApplicationTests {
	DataBaseFacade dataBaseFacade;
	@BeforeEach
	public void setUp() {
		dataBaseFacade=new DataBaseFacade(1);
	}
	@Test
	@Order(1)
	public void createDatabaseTest(){
		String databaseName = "my_database";
		boolean flag = dataBaseFacade.createDataBase(databaseName);
		assertTrue("Database creation should return true", flag);
	}

	@Test
	@Order(2)
	public void testUseDb() {
		String databaseName = "my_database";
		boolean flag = 	dataBaseFacade.useDb(databaseName);
		assertTrue("Database creation should return true", flag);
	}
	@Test
	@Order(3)
	public void testCreateCollection() {
		dataBaseFacade.useDb("my_database");
		boolean flag = dataBaseFacade.createCollection("cars");
		assertTrue("Database creation should return true", flag);
	}

	@Test
	@Order(4)
	public void testInsertToCollectionShouldReturnFalse() throws JSONException {
		dataBaseFacade.useDb("my_database");
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("color","Red");
		jsonObject.put("year",2007);
		jsonObject.put("model","RDX");
		jsonObject.put("make","Acura");
		boolean flag = dataBaseFacade.insertToCollection("cars",jsonObject );
		assertTrue("Database creation should return false", flag);
	}


}


