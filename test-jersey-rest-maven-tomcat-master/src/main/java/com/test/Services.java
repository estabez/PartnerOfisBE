package com.test;
 
import javax.ws.rs.Path;
import javax.ws.rs.POST;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import com.test.Configurations;

import com.test.EncryptionAndDecryption;


@Path("/services")
public class Services {

	@POST
	@Path("/test12/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
  public String  getTestService() throws ClassNotFoundException {
		
		  try {
			   URL flxpoint = new URL("https://private-anon-9ba3802f35-flxpoint.apiary-mock.com/v1/listings/syncing");
		        URLConnection yc = flxpoint.openConnection();
		        BufferedReader in = new BufferedReader(
		                                new InputStreamReader(
		                                yc.getInputStream()));
		        String inputLine;
		        StringBuilder response = new StringBuilder();
		        
		        while ((inputLine = in.readLine()) != null) 
		        	 response.append(inputLine);
		        in.close();
		      
		        return response.toString();
		  } catch (Exception e) {
		    e.printStackTrace();
		    return null;
		  }
		
		
  }
	
	@POST
	@Path("/GetExcelData/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
  public String  GetExcelData(String Inputs1) {
		
		String company ="";
		String query = "";
		
		try {
			JSONParser parser = new JSONParser();
			JSONObject inputJSONObject = (JSONObject) parser.parse(Inputs1);
			
			company = (String) inputJSONObject.get("company");
			
			
			switch (company) {
			case "Sekizgen":
				query = "SELECT * FROM public.items";
				break;
			default:
				query = "SELECT * FROM public.items WHERE company=" + "'" +company + "'";
				break;
			}
			
			
		}catch (Exception e) 
		{			
			JSONObject outputJSONObject = new JSONObject();			
			outputJSONObject.put("exec_status", "error");
			outputJSONObject.put("error_message", e.getMessage());		

			
			return outputJSONObject.toJSONString();
		}
		
		
		
		
		try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found " + e);
        }
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {
		// try (Connection connection = DriverManager.getConnection("jdbc:postgresql://20.52.143.247:5432/postgres", "postgres", "onurerdem")) {
        	 
			 JSONArray newJsonList = new JSONArray();
			 
	            Statement statement = connection.createStatement();
	            ResultSet resultSet = statement.executeQuery(query);
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

			
				
				return outputJSONObject.toJSONString();
				
	        } catch (SQLException e) {
	        	JSONObject outputJSONObject = new JSONObject();			
				outputJSONObject.put("exec_status", "error");
				outputJSONObject.put("error_message", e.getMessage());		

				
				return outputJSONObject.toJSONString();
	        }
		
	        
		
		
  }

	
	
	@POST
	@Path("/LoginWithCredentials/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String LoginWithCredentials(String Inputs1) 
	{
	
		String usernameInput = "";
		String passwordInput = "";
		String dbPassword = "";
		String company = "";
		try 
		{	
			JSONParser parser = new JSONParser();
			JSONObject inputJSONObject = (JSONObject) parser.parse(Inputs1);
			
			usernameInput = (String) inputJSONObject.get("username");
			String encryptedpasswordInput = (String) inputJSONObject.get("password");
			
			
			
			try {
	            Class.forName("org.postgresql.Driver");
	        } catch (ClassNotFoundException e) {
	            System.out.println("Class not found " + e);
	        }
			// try (Connection connection = DriverManager.getConnection("jdbc:postgresql://20.52.143.247:5432/postgres", "postgres", "onurerdem")) {
			 try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {
	        	 
				 JSONArray newJsonList = new JSONArray();
				 
		            Statement statement = connection.createStatement();
		            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.user WHERE username=" + "'" +usernameInput + "'");
		            while (resultSet.next()) {
		            	
     	
		        		dbPassword = resultSet.getString("password");
		        		company = resultSet.getString("company");
		        		

		            }
 
		            JSONObject outputJSONObject = new JSONObject();
					
		            if(dbPassword.equals(encryptedpasswordInput)) {
		            	

						outputJSONObject.put("exec_status", "success");
						outputJSONObject.put("token", "111");
						outputJSONObject.put("company", company);
	
		            }else {

						outputJSONObject.put("exec_status", "error");
						outputJSONObject.put("error_message", "Kullanıcı adı veya şifre yanlış");		
		            	
		            }
		    		
				
					
					return outputJSONObject.toJSONString();
					
		        } 
			
			
		}
		catch (Exception e) 
		{			
			JSONObject outputJSONObject = new JSONObject();			
			outputJSONObject.put("exec_status", "error");
			outputJSONObject.put("error_message", e.getMessage());		

			
			return outputJSONObject.toJSONString();
		}
	}
	
	
	
	public Boolean SaveExcelDataToDB(String sku, String quantity, String cost, String company) throws SQLException {
		
		
		try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found " + e);
        }
		// try (Connection connection = DriverManager.getConnection("jdbc:postgresql://20.52.143.247:5432/postgres", "postgres", "onurerdem")) {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {
       	 
			 JSONArray newJsonList = new JSONArray();
			 
			 int affectedrows = 0;
			 
	            Statement statement = connection.createStatement();
	            affectedrows = statement.executeUpdate("UPDATE public.items SET fiyat=" + "'" + cost + "'" + ", stok_adet ="+quantity+" WHERE sku=" + "'" +sku+ "'" + " AND company=" +"'" + company + "'");
	            
	            System.out.println("affectedrows: "+ affectedrows);
				return true;
				
	        } catch (Exception e) {
	        	 System.out.println("aaa");
	        	System.out.println(e.getMessage());
	        	return false;
	        }
		

	}
	
	
	@POST
	@Path("/SaveRow/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String SaveRow(String Inputs1) 
	{
		String company = "";
		String token = "";
		Boolean resultDbSave = false;
		try 
		{	
			JSONParser parser = new JSONParser();
			JSONObject inputJSONObject = (JSONObject) parser.parse(Inputs1);
			JSONObject outputJSONObject = new JSONObject();
			
			
			company = (String) inputJSONObject.get("company");
			
			JSONObject row =  (JSONObject) inputJSONObject.get("row");
			
			
			String SKU = (String) row.get("SKU");
			String quantity = (String) row.get("Stok Adet");
			String cost = (String) row.get("Satış Fiyatı");
			
			
			//SKU, Cost (satiş fiyatı), quantitiy (stok adet)
			
			resultDbSave = SaveExcelDataToDB(SKU, quantity, cost, company);
			try {
	            Class.forName("org.postgresql.Driver");
	        } catch (ClassNotFoundException e) {
	            System.out.println("Class not found " + e);
	        }
			
			// try (Connection connection = DriverManager.getConnection("jdbc:postgresql://20.52.143.247:5432/postgres", "postgres", "onurerdem")) {
			try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {
		       	 
				   Statement statement = connection.createStatement();
		            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.user WHERE company=" + "'" +company + "'" + " LIMIT 1");
		            while (resultSet.next()) {	
		        		token = resultSet.getString("authtokenflxpoint");
		            }
					
		        } catch (Exception e) {
		        	 System.out.println("aaa");
		        	System.out.println(e.getMessage());
		        	
		        }
			
			
			
			
			
			System.out.println("resultDbSave:  " + resultDbSave);
			if(resultDbSave) {
				
				
				Entity payload = Entity.json("[  {    \"sku\":"+ "\"" +SKU+ "\"" +",        \"variants\": [      {        \"sku\":" + "\"" + SKU + "\"" + ",              \"cost\":" + "\"" + cost + "\"" +  ",     \"archived\": false,    \"quantity\":" + "\"" +  quantity + "\"" + "      }    ] }]");
				
				
				Client client = ClientBuilder.newClient();
				
				
				System.out.println("payload:  " + payload);
				
				

				
				
				
				Response response = client.target("https://integrations.flxpoint.com/v1/source-products")
				  .request(MediaType.APPLICATION_JSON_TYPE)
				  .header("Authorization", token)
				  .post(payload);

				System.out.println("status: " + response.getStatus());
				System.out.println("headers: " + response.getHeaders());
				System.out.println("body:" + response.readEntity(String.class));
				
				
				
				outputJSONObject.put("SKU", SKU);
				outputJSONObject.put("exec_status", "success");
				
				return outputJSONObject.toJSONString();
				
			}else {
				outputJSONObject.put("exec_status", "error");
				outputJSONObject.put("error_message", "Kayıt Başarısız");	
				
				return outputJSONObject.toJSONString();
			}
		
			
		}
		catch (Exception e) 
		{			
			JSONObject outputJSONObject = new JSONObject();			
			outputJSONObject.put("exec_status", "error");
			outputJSONObject.put("error_message", e.getMessage());		

			
			return outputJSONObject.toJSONString();
		}
	}
	
}