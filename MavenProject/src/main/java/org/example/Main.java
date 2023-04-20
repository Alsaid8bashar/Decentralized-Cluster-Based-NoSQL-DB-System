package org.example;

import org.example.Cluster.Container;
import org.example.Cluster.NodeManager;
import org.example.Facade.DataBaseFacade;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) {
        DataBaseFacade<Object> dataBaseFacade = new DataBaseFacade<>(1);
        Container container = Container.createContainer(0, 22, "1", "best", "sada", 2);
        container.setCurrent();
        NodeManager.NODE_MANAGER.addContainer(container);
        String collectionName = "testCollection";
//        dataBaseFacade.createDataBase("testDb2");
        dataBaseFacade.useDb("testDb2");
//        dataBaseFacade.createCollection(collectionName);
//        JSONObject jsonObject = new JSONObject("{\"color\":\"Red\",\"year\":2007,\"model\":\"RDX\",\"make\":\"Acura\"}");
//        dataBaseFacade.createSchema(collectionName, jsonObject);
        JSONArray cars = new JSONArray();
        for (int i = 1; i <= 5; i++) {
            JSONObject car = new JSONObject();
            car.put("color", "Maroon");
            car.put("year", 2008);
            car.put("make", "Chevrolet");
            car.put("model", "Silverado 3500" + i);
            cars.put(car);
        }
//            dataBaseFacade.insertToCollection(collectionName, cars);
//        File collectionFile = new File("Storage/Users/0/DataBases/testDb2/Collections/testCollection");
//        File[] files = collectionFile.listFiles();
//        int start;
//        if (files.length > 0) {
//            String lastFile = files[files.length-1].getName();
//            int underscoreIndex = lastFile.indexOf(".");
//            start = Integer.parseInt(lastFile.substring(0, underscoreIndex));
//        } else {
//            start = 0;
//        }
//        System.out.println(start);

////        dataBaseFacade.insertToCollection(collectionName, cars);
        dataBaseFacade.updateDocument(collectionName,"3e14b1fb-cff9-4f67-8df6-4dc8cec5e3a2_1681361171779","year",999);
        System.out.println(dataBaseFacade.find(collectionName, "year", 999));
//

//        dataBaseFacade.updateDocument(collectionName,);
//        dataBaseFacade.deleteDocument(collectionName,"color","Maroon");
//

//        System.out.println(dataBaseFacade.find(collectionName, "color", "blue"));
//        System.out.println(dataBaseFacade.find(collectionName, "year", 2008));
//        System.out.println();
//        dataBaseFacade.deleteCollection(collectionName);
//        dataBaseFacade.deleteDb("testDb");

//        System.out.println("----------------------------------------------------");


    }
}