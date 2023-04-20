import org.example.Facade.DataBaseFacade;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class DataBaseTest {
    DataBaseFacade dataBaseFacade;

    @Before
    public void setUp() {
        dataBaseFacade = new DataBaseFacade<>(2);
        dataBaseFacade.createDataBase("testDb");
        dataBaseFacade.useDb("testDb");
    }

    @Test
    @Order(1)
    public void testCreateCollection() {
        String collectionName = "testCollection";
        boolean result = dataBaseFacade.createCollection(collectionName);
        Assert.assertTrue(result);
    }

    @Test
    @Order(2)
    public void testCreateSchema() {
        String collectionName = "testCollection";
        dataBaseFacade.createCollection(collectionName);
        JSONObject jsonObject = new JSONObject("{\"color\":\"Red\",\"year\":2007,\"model\":\"RDX\",\"make\":\"Acura\"}");
        boolean result = dataBaseFacade.createSchema(collectionName, jsonObject);
        Assert.assertTrue(result);
    }

    @Test
    @Order(3)
    public void testInsertValidData() {
        String collectionName = "testCollection";
        JSONArray cars = new JSONArray();
        for (int i = 1; i <= 10; i++) {
            JSONObject car = new JSONObject();
            car.put("color", "Maroon" );
            car.put("year", 2008);
            car.put("make", "Chevrolet");
            car.put("model", "Silverado 3500" + i);
            cars.put(car);
        }
        dataBaseFacade.createCollection(collectionName);
        boolean result = dataBaseFacade.insertToCollection(collectionName, cars);
        Assert.assertTrue(result);
    }
    @Test
    @Order(4)
    public void testInsertInValidData() {
        String collectionName = "testCollection";
        JSONArray cars = new JSONArray();
        for (int i = 1; i <= 10; i++) {
            JSONObject car = new JSONObject();
            car.put("make", "Chevrolet");
            cars.put(car);
        }
        dataBaseFacade.createCollection(collectionName);
        boolean result = dataBaseFacade.insertToCollection(collectionName, cars);
        Assert.assertFalse(result);
    }

    @Test
    @Order(5)
    public void testFindDataShouldReturnFalse() {
        String collectionName = "testCollection";
        dataBaseFacade.createCollection(collectionName);
        System.out.println(dataBaseFacade.find(collectionName, "_id", "1"));
        boolean result = dataBaseFacade.find(collectionName, "_id", "1").isEmpty();
        Assert.assertFalse(result);
    }
    @Test
    @Order(6)
    public void testFindShouldReturnTrue() {
        String collectionName = "testCollection";
        dataBaseFacade.createCollection(collectionName);
        System.out.println(dataBaseFacade.find(collectionName, "_id", "200"));
        boolean result = dataBaseFacade.find(collectionName, "_id", "200").isEmpty();
        Assert.assertTrue(result);
    }

}
