package com.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class test {
	public static void main(String[] args) throws IOException, ParseException {
		 
		
		
		Client client = ClientBuilder.newClient();
		 Entity payload = Entity.json("[  {    \"sku\": \"L-CR3997\",        \"variants\": [      {        \"sku\": \"L-CR3997\",              \"cost\": 600,    \"upc\": null,  \"archived\": false,    \"quantity\": 400       }    ] }]");
		
		
		 //		 Entity payload = Entity.json("[  {    \"sku\": \"Test-Sku-003\",        \"variants\": [      {        \"sku\": \"Test-Sku-003\",              \"cost\": 21.99,    \"upc\": null,  \"archived\": false,    \"quantity\": 7        }    ] }]");

		// Entity payload = Entity.json("[  {    \"sku\": \"L-313577\",        \"variants\": [      {        \"sku\": \"L-313577\",              \"cost\": 1,    \"upc\": null,  \"archived\": false,    \"quantity\": 1        }    ] }]");
		 
		 // Entity payload = Entity.json("[  {    \"sku\": \"L-20460\",        \"variants\": [      {        \"sku\": \"L-20460\",              \"cost\": 1,    \"upc\": null,  \"archived\": false,    \"quantity\": 1        }    ] }]");
		 
		 
		 
		 /*
		Response response = client.target("https://integrations.flxpoint.com/v1/source-products")
		  .request(MediaType.APPLICATION_JSON_TYPE)
		  .header("Authorization", "Bearer uxetgjZDwg54HBZ4jgKn8qkBaWrRxgiwsfhKGAQCMrc5ChzMjiTGzzentSeMByjKwmhIKDcAQHZ4lYq0q0QvXiLPdWfqbJbD1BlC")
		  .post(payload);

		
		System.out.println("payload ::::  " + payload);
		
		System.out.println("status: " + response.getStatus());
		System.out.println("headers: " + response.getHeaders());
		System.out.println("body:" + response.readEntity(String.class));
		*/
		
	/*	try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found " + e);
        }
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/KolayOTOdB", "postgres", "postgres")) {
       	 
			 JSONArray newJsonList = new JSONArray();
			 
	            Statement statement = connection.createStatement();
	            ResultSet resultSet = statement.executeQuery("SELECT * FROM kolayotodb.items WHERE liste_fiyati= '436'");
	            while (resultSet.next()) {
	            	
	            	
	            	JSONObject newElement = new JSONObject();
	        		newElement.put("SKU", resultSet.getString("sku"));
	        		newElement.put("Tedarikçi SKU", resultSet.getString("tedarikci_sku"));
	        		newElement.put("Ürün İsmi", resultSet.getString("urun_ismi"));
	        		newElement.put("Liste Fiyatı", resultSet.getString("liste_fiyati"));
	        		newElement.put("Satış Fiyatı", resultSet.getString("fiyat"));
	        		newElement.put("İnd.Oran(%)", "x");
	        		newElement.put("Stok Adet", resultSet.getString("stok_adet"));
	        		newElement.put("T.Adet", resultSet.getString("teklif_adet"));
	        		newElement.put("T.Sıra", resultSet.getString("teklif_sirasi"));
	        		newElement.put("Ürt.Tar.", resultSet.getString("uretim_tarihi"));
	        		newElement.put("Hz.Sr.", resultSet.getString("hazirlik_suresi"));
	        		newJsonList.add(newElement);
	        		
	                
	            }
	            System.out.println(newJsonList);
	            
	            JSONObject outputJSONObject = new JSONObject();
				
	    		
				outputJSONObject.put("exec_status", "success");
				outputJSONObject.put("data", newJsonList);

			
				
				
	        } catch (SQLException e) {
	        	JSONObject outputJSONObject = new JSONObject();			
				outputJSONObject.put("exec_status", "error");
				outputJSONObject.put("error_message", e.getMessage());		

				
	        }	
	*/
        
    }
}