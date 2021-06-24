package com.test;
 
import javax.ws.rs.Path;
import javax.ws.rs.POST;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;


import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.ws.rs.core.MediaType;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

@Path("/services")
public class Services {
	
	static Logger log = Logger.getLogger(Services.class.getName());  
	public static String labelLink=null;
	public static String shippingCode=null;
	public static String xml=null;
	public static String xml12=null;
	public static String rspFromUPS=null;
	public static String rspStatusFromUps=null;
	
	public static String mail = null;
	public static int affectedrows11 = 0;
	public static String orderIdFromFlxp11 = null;
	
	public static String jdbcURL="jdbc:postgresql://20.52.6.77:5432/po";
	public static String jdbcUser = "pouser";
	public static String jdbcPass = "pouser";
	
	/*
	public static String jdbcURL="jdbc:postgresql://localhost:5432/kolayoto";
	public static String jdbcUser = "postgres";
	public static String jdbcPass = "postgres";
	*/
	
	@POST
	@Path("/test12/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
  public String  getTestService() throws ClassNotFoundException {
		Properties props = System.getProperties();
		System.out.println("Current working directory is " + props.getProperty("user.dir"));
		PropertyConfigurator.configure("log4j.properties");
		
		
		log.debug("Hello this is a debug message"); 
		log.info("Hello this is an info message");  
		return props.getProperty("user.dir");
  }
	
	@POST
	@Path("/GetExcelData/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
  public String  GetExcelData(String Inputs1) {
		
		String company ="";
		String query = "";
		double indirimOrani = 0;
		String liste_fiyati = null, fiyat = null,id=null;
		int kod1=0, kod2=0, kod3=0;
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
	
		 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
        	 
			 JSONArray newJsonList = new JSONArray();
			 
	            Statement statement = connection.createStatement();
	            ResultSet resultSet = statement.executeQuery(query);
	            while (resultSet.next()) {
	            	
	            	id= resultSet.getString("id");
	            	kod1=0;
	            	kod2=0;
	            	kod3=0;
	            	
	            	
	            	
	            	
	                       	
	            	kod1=1;
	            	
	            	Boolean isNumeric = true;
	            	
	            	try {
	            	Integer.parseInt(resultSet.getString("fiyat"));
	            	}catch(NumberFormatException e) {
	            		kod2=2;
	            		isNumeric = false;
	            	}catch(NullPointerException e) {
	            		kod3=3;
	            		isNumeric = false;
	            	}
	            	
	            	if (isNumeric) {
	            		if( (resultSet.getString("liste_fiyati")!= null && !resultSet.getString("liste_fiyati").equals("0") ) && 
		            			(resultSet.getString("fiyat") != null && !resultSet.getString("fiyat").equals("0") && !resultSet.getString("fiyat").equals("null"))) {
		            		
		            		fiyat = resultSet.getString("fiyat").split("[.,]")[0];
			            	liste_fiyati = resultSet.getString("liste_fiyati").split("[.,]")[0];
		            	
		            	if((!(liste_fiyati.equals(fiyat))) && Integer.parseInt(liste_fiyati)> Integer.parseInt(fiyat)) {
		            		indirimOrani = (((  Integer.parseInt(liste_fiyati)- Integer.parseInt(fiyat))*100 / Integer.parseInt(fiyat))) ;
		            	}else {
		            		indirimOrani = 0;
		            	}
		            		
		            	

		            	}else {
		            		indirimOrani = 100;
		            	}
	            	}
	            	
	            
	            	
	            	kod2=2;
	            	
	            	
	            	
	            	
	            	
	            	JSONObject newElement = new JSONObject();
	        		newElement.put("SKU", resultSet.getString("sku"));
	        		newElement.put("Tedarikçi SKU", resultSet.getString("tedarikci_sku"));
	        		newElement.put("Ürün İsmi", resultSet.getString("urun_ismi"));
	        		newElement.put("Liste Fiyatı", resultSet.getString("liste_fiyati"));
	        		
	        		
	        		newElement.put("Satış Fiyatı", resultSet.getString("fiyat"));
	        		
	        		
	        		newElement.put("İnd.Oran(%)", round(indirimOrani, 2));
	        		newElement.put("Stok Adet", resultSet.getString("stok_adet"));
	        		newElement.put("T.Adet", resultSet.getString("teklif_adet"));
	        		newElement.put("T.Sıra", resultSet.getString("teklif_sirasi"));
	        		newElement.put("Ürt.Tar.", resultSet.getString("uretim_tarihi"));
	        		newElement.put("Hz.Sr.", resultSet.getString("hazirlik_suresi"));
	        		newJsonList.add(newElement);
	        		kod3=3;
	                
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
	        }catch (Exception e) 
		{			
				JSONObject outputJSONObject = new JSONObject();			
				outputJSONObject.put("exec_status", "error");
				outputJSONObject.put("error_message", e.getMessage());		
				outputJSONObject.put("fiyat", fiyat);
				outputJSONObject.put("liste_fiyati", liste_fiyati);
				outputJSONObject.put("id", id);
				outputJSONObject.put("kod1", kod1);
				outputJSONObject.put("kod2", kod2);
				outputJSONObject.put("kod3", kod3);
				return outputJSONObject.toJSONString();
			}
		
	        
		
		
  }
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
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
			 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
		
	        	 
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
	
	
	
	public Boolean SaveExcelDataToDB(String sku, String quantity, String cost, String company, String uretimTarihi, String teradikciSKU, String hazirlikSuresi) throws SQLException {
		
		
		try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found " + e);
        }
		 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
	
       	 
			 JSONArray newJsonList = new JSONArray();
			 
			 int affectedrows = 0;
			 
	            Statement statement = connection.createStatement();
	            affectedrows = statement.executeUpdate("UPDATE public.items SET fiyat=" + "'" + cost + "'" + ", stok_adet ="+quantity+", uretim_tarihi="+"'"+ uretimTarihi+"'"+ ", tedarikci_sku="+"'"+teradikciSKU+"'"+", hazirlik_suresi="+"'"+hazirlikSuresi+"'"    +" WHERE sku=" + "'" +sku+ "'" + " AND company=" +"'" + company + "'");
	            
	            System.out.println("affectedrows: "+ affectedrows);
				return true;
				
	        } catch (Exception e) {
	        	 System.out.println("aaa");
	        	System.out.println(e.getMessage());
	        	return false;
	        }
		

	}
	
	
	public int SaveExcelDataToDBMultipleRow(String sku, String quantity, String cost, String company, String uretimTarihi, String teradikciSKU, String hazirlikSuresi) throws SQLException {
		
		
		try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found " + e);
        }
		 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
	
       	 
			 JSONArray newJsonList = new JSONArray();
			 
			 int affectedrows = 0;
			 
	            Statement statement = connection.createStatement();
	            affectedrows = statement.executeUpdate("UPDATE public.items SET fiyat=" + "'" + cost + "'" + ", stok_adet ="+quantity+", uretim_tarihi="+"'"+ uretimTarihi+"'"+ ", tedarikci_sku="+"'"+teradikciSKU+"'"+", hazirlik_suresi="+"'"+hazirlikSuresi+"'"    +" WHERE sku=" + "'" +sku+ "'" + " AND company=" +"'" + company + "'");
	            
	            System.out.println("affectedrows: "+ affectedrows);
				return affectedrows;
				
	        } catch (Exception e) {
	        	 System.out.println("aaa");
	        	System.out.println(e.getMessage());
	        	return 0;
	        }
		

	}
	
	/*

	@POST
	@Path("/GetPOFromFlxPoint/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String GetPOFromFlxPoint(String Inputs1) 
	{
		String company = "";
		String token = "";
		
		try 
		{	
			JSONParser parser = new JSONParser();
			JSONObject inputJSONObject = (JSONObject) parser.parse(Inputs1);
			JSONObject outputJSONObject = new JSONObject();
			
			company = (String) inputJSONObject.get("company");
			
			// try (Connection connection = DriverManager.getConnection("jdbc:postgresql://20.52.143.247:5432/postgres", "postgres", "onurerdem")) {
			try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {	 
				   Statement statement = connection.createStatement();
		            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.user WHERE company=" + "'" +company + "'" + " LIMIT 1");
		            while (resultSet.next()) {	
		        		token = resultSet.getString("authtokenflxpoint");
		            }
					
		        } catch (Exception e) {
		        	
		        	System.out.println(e.getMessage());
		        	
		        }
			
			
			
			
			Client client = ClientBuilder.newClient();
			Response response = client.target("https://integrations.flxpoint.com/v1/purchase-orders?showAcknowledged=true")
					  .request(MediaType.APPLICATION_JSON_TYPE)
					  .header("Authorization", token)
					  .get();
			
			
		}
		catch (Exception e) 
		{			
			JSONObject outputJSONObject = new JSONObject();			
			outputJSONObject.put("exec_status", "error");
			outputJSONObject.put("error_message", e.getMessage());		

			
			return outputJSONObject.toJSONString();
		}
	}
	
	
	
	*/
	
	
	public static void DataCheck(JSONObject data, PreparedStatement statement) throws SQLException {
		
		String[] columns = {"id", "purchaseOrderNumber", "orderId", "transferOrderId", "shippingText", "sentAt", "sourceId", "holdUntil", "acknowledgedAt", "canceledAt", "shippingAddress", "billingAddress", "shippingAddressId", "billingAddressId", "note", "confirmationNumber", "purchaseOrderStatusId", "generatedAt", "voidedAt", "purchaseOrderFulfillmentStatusId", "accountId", "totalItems", "totalCost", "totalQuantity", "shippedQuantity", "suppressTracking", "estimatedShippingCost", "estimatedDropshipFee", "sourceShippingMethodId", "sourceShippingMethod", "packageDimensionUnitId", "packageLength", "packageWidth", "packageHeight", "packageWeightUnitId", "packageWeight", "items", "attributes", "fflInfoRequired", "fflInfo", "lastModifiedAt", "accountingSynced", "externalAccountingId", "rateShoppedId", "cancelReason", "voidedReason", "channelId", "attachmentLinks", "shippingDistance"};
		
		for(int a=1; a<50; a++) {
			if(data.get(columns[a-1]) != null) {
				statement.setString(a, (String) data.get(columns[a-1]).toString());
				}else {
					statement.setString(a, null);
				}
		}
	
	}
	
	public boolean SavePurchaseOrderToDB(String data, String company) {
		int affectedrows = 0;
		
		try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found " + e);
        }
		
		 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
		
       	 
			 JSONArray newJsonList = new JSONArray();
			 
			
				JSONParser parser = new JSONParser();
				JSONObject inputJSONObject = (JSONObject) parser.parse(data);
				JSONObject inputJSONObject1 = (JSONObject) inputJSONObject.get("data");
				JSONArray inputJSONObject2 =  (JSONArray) inputJSONObject1.get("purchaseOrders");
				
				String SQL = "INSERT INTO public.flxpointpurchaseorders(id, purchaseordernumber, orderid, transferorderid, shippingtext, sentat, sourceid, holduntil, acknowledgedat, canceledat, shippingaddress, billingaddress, shippingaddressid, billingaddressid, note, confirmationnumber, purchaseorderstatusid, generatedat, voidedat, purchaseorderfulfillmentstatusid, accountid, totalitems, totalcost, totalquantity, shippedquantity, suppresstracking, estimatedshippingcost, estimateddropshipfee, sourceshippingmethodid, sourceshippingmethod, packagedimensionunitid, packagelength, packagewidth, packageheight, packageweightunitid, packageweight, items, attributes, fflinforequired, fflinfo, lastmodifiedat, accountingsynced, externalaccountingid, rateshoppedid, cancelreason, voidedreason, channelid, attachmentlinks, shippingdistance, status, company) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
				
				
				for(int a = 0; a< inputJSONObject2.size() ; a++) {

					JSONObject inputJSONObject3 = (JSONObject) inputJSONObject2.get(a);
					
					System.out.println("sss: " + inputJSONObject3.get("id"));
					String id = inputJSONObject3.get("id").toString();
					
					String idDb= null;
					 Statement statementGet = connection.createStatement();
			            ResultSet resultSet = statementGet.executeQuery("SELECT * FROM public.flxpointpurchaseorders WHERE id=" + "'" +id + "'" + " LIMIT 1");
			            while (resultSet.next()) {	
			            	idDb = resultSet.getString("id");
			            }
			            
					if(idDb == null) {

						PreparedStatement statement = connection.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
						
						DataCheck(inputJSONObject3,statement);
						
						statement.setString(50, "PROCESSING");
						statement.setString(51, company);
						
						System.out.println(statement);
						affectedrows = statement.executeUpdate();
							
					}else {
						 int affectedrows1 = 0;
						 
					        Statement statement = connection.createStatement();
					        affectedrows1 = statement.executeUpdate("UPDATE public.flxpointpurchaseorders SET purchaseorderstatusid=" + "'" + 7 + "'" +" WHERE id=" + "'" +idDb+ "'");
					      
					}
					
					
				}
				System.out.println(affectedrows);
			 
			 
				return true;
				
	        } catch (Exception e) {
	        
	        	System.out.println(e.getMessage());
	        	return false;
	        }
		
		
	}
	
	
	
	public String GetPurchaseOrderFromFlxPoint(String token) {
		
		Client client = ClientBuilder.newClient();
		Response response = client.target("https://integrations.flxpoint.com/v1/purchase-orders?showAcknowledged=true")
				  .request(MediaType.APPLICATION_JSON_TYPE)
				  .header("Authorization", token)
				  .get();
		
		return response.readEntity(String.class);
	}
	
	
	@POST
	@Path("/Onayla/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String Onayla(String Inputs1) 
	{
	String token = null;
	String company = null;
	String query = null;
	String orderId= null;
	
	try 
	{
	JSONParser parser = new JSONParser();
	JSONObject inputJSONObject = (JSONObject) parser.parse(Inputs1);
	JSONObject outputJSONObject = new JSONObject();
	
	
	company = (String) inputJSONObject.get("company");
	orderId = (String) inputJSONObject.get("orderId");
	
	
	try {
        Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
        System.out.println("Class not found " + e);
    }
	
	try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
			 
					   Statement statement = connection.createStatement();
			            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.user WHERE company=" + "'" +company + "'" + " LIMIT 1");
			            while (resultSet.next()) {	
			        		token = resultSet.getString("authtokenflxpoint");
			            }
						
			        } catch (Exception e) {
			        	
			        	System.out.println(e.getMessage());
			        	
			        }
	
	
				Client client = ClientBuilder.newClient();
				Response response = client.target("https://integrations.flxpoint.com/v1/purchase-orders/"+ orderId +"/acknowledge")
						  .request(MediaType.APPLICATION_JSON_TYPE)
						  .header("Authorization", token)
						  .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
						  .method("PATCH",Entity.json(""));
				
				
				//System.out.println(response.readEntity(String.class));
				
				
				
				
	
	
	
	
	 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
        

		 
        int affectedrows = 0;
		 
        Statement statement = connection.createStatement();
        affectedrows = statement.executeUpdate("UPDATE public.flxpointpurchaseorders SET purchaseorderstatusid=" + "'" + 8 + "'" +" WHERE id=" + "'" +orderId+ "'");
      
        //System.out.println("affectedrows: "+ affectedrows);
		
        
        
		outputJSONObject.put("exec_status", "success");
		

		
		return outputJSONObject.toJSONString();
	
	
	
	
	
	}catch (SQLException e) {
        	
			outputJSONObject.put("exec_status", "error");
			outputJSONObject.put("error_message", e.getMessage());		

			
			return outputJSONObject.toJSONString();
        }
	


	
	}catch (Exception e) {
		JSONObject outputJSONObject = new JSONObject();			
		outputJSONObject.put("exec_status", "error");
		outputJSONObject.put("error_message", e.getMessage());		

		
		return outputJSONObject.toJSONString();
	}
			
	}

	
	


	

	 	@GET
	    @Path("GetIstatistik")
	 	@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_XML)
	    public String GetIstatistik(@Context HttpHeaders httpHeaders){
		
	
	try 
	{
	
		
		

				
				
				JSONObject outputJSONObject = new JSONObject();
			
				DocumentBuilderFactory dbFactory =
				         DocumentBuilderFactory.newInstance();
				         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				         Document doc = dBuilder.newDocument();
				         
				         // root element
				         Element rootElement = doc.createElement("body");
				         doc.appendChild(rootElement);
				
			
				         
				         

				     	try {
				             Class.forName("org.postgresql.Driver");
				         } catch (ClassNotFoundException e) {
				             System.out.println("Class not found " + e);
				         }
				
			
						try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
							
							
							JSONArray newJsonList = new JSONArray();
							
							
							Statement statement = connection.createStatement();
				            ResultSet resultSet = statement.executeQuery("SELECT sku FROM public.teklifsirasiteklifsayisi group by sku;");
				            while (resultSet.next()) {
				            	String sku = null;
				            	String ikicompany=null, ikiteklifsirasi=null, ikihazirliksuresi=null, ikiuretimtarihi=null, ikicost=null, ikiquantity=null ;
				            	String bircompany=null, birteklifsirasi=null, birhazirliksuresi=null, biruretimtarihi=null, bircost=null, birquantity=null ;
				            	JSONObject newElement = new JSONObject();
				            	
				            	
				            	sku= resultSet.getString("sku");
				            	

				        		//System.out.println(sku);
				        		
				        		
				        		Statement statement1 = connection.createStatement();
					            ResultSet resultSet1 = statement1.executeQuery("SELECT company, sku, teklifsirasi, teklifsayisi, hazirliksuresi, uretimtarihi, cost, quantity FROM public.teklifsirasiteklifsayisi where sku = "+ "'" + sku + "'"+ " AND teklifsirasi = '1' LIMIT 10;");
					            while (resultSet1.next()) {
					            	
					            	bircompany= resultSet1.getString("company");
					            	birteklifsirasi= resultSet1.getString("teklifsirasi");
					            	birhazirliksuresi= resultSet1.getString("hazirliksuresi");
					            	biruretimtarihi= resultSet1.getString("uretimtarihi");
					            	bircost= resultSet1.getString("cost");
					            	birquantity= resultSet1.getString("quantity");
			
					            }
				        		
					            Statement statement2 = connection.createStatement();
					            ResultSet resultSet2 = statement2.executeQuery("SELECT company, sku, teklifsirasi, teklifsayisi, hazirliksuresi, uretimtarihi, cost, quantity FROM public.teklifsirasiteklifsayisi where sku = "+ "'" + sku + "'"+ " AND teklifsirasi = '2' LIMIT 10;");
					            while (resultSet2.next()) {
					            	
					            	ikicompany= resultSet2.getString("company");
					            	ikiteklifsirasi= resultSet2.getString("teklifsirasi");
					            	ikihazirliksuresi= resultSet2.getString("hazirliksuresi");
					            	ikiuretimtarihi= resultSet2.getString("uretimtarihi");
					            	ikicost= resultSet2.getString("cost");
					            	ikiquantity= resultSet2.getString("quantity");
			
					            }
				        		
					            newElement.put("sku", sku);
					            
					            newElement.put("1.Teklif Tedarikçi", bircompany);
			        			newElement.put("1.Teklif Tedarikçi Maliyet", bircost);
			        			newElement.put("1.Teklif Tedarikçi Stok Adet", birquantity);
			        			newElement.put("1. teklif tedarikçi uretim_tarihi", biruretimtarihi);
			        			newElement.put("1. teklif tedarikçi  hazirlik_suresi", birhazirliksuresi);
			        			
			        			newElement.put("2.Teklif Tedarikçi", ikicompany);
			        			newElement.put("2.Teklif Tedarikçi Maliyet", ikicost);
			        			newElement.put("2.Teklif Tedarikçi Stok Adet", ikiquantity);
			        			newElement.put("2. teklif tedarikçi uretim_tarihi", ikiuretimtarihi);
			        			newElement.put("2. teklif tedarikçi  hazirlik_suresi", ikihazirliksuresi);
			        			
				        		newJsonList.add(newElement);
					            
				        		bircompany = nullEsitle(bircompany);
				        		ikicompany = nullEsitle(ikicompany);
				        		bircost = nullEsitle(bircost);
				        		birquantity = nullEsitle(birquantity);
				        		biruretimtarihi = nullEsitle(biruretimtarihi);
				        		birhazirliksuresi = nullEsitle(birhazirliksuresi);
				        		ikicost = nullEsitle(ikicost);
				        		ikiquantity = nullEsitle(ikiquantity);
				        		ikiuretimtarihi = nullEsitle(ikiuretimtarihi);
				        		ikihazirliksuresi = nullEsitle(ikihazirliksuresi);
				        		
				        		 Element data1 = doc.createElement("data");
				    	         rootElement.appendChild(data1);
				        		
				    	         Element sku1 = doc.createElement("sku");
				    	         sku1.appendChild(doc.createTextNode(sku));
				    	         data1.appendChild(sku1);
				    	         
				    	         Element bircompany1 = doc.createElement("BirinciTeklifTedarikci");
				    	         bircompany1.appendChild(doc.createTextNode(bircompany));
				    	         data1.appendChild(bircompany1);
				    	         
				    	         Element bircost1 = doc.createElement("BirinciTeklifTedarikciMaliyet");
				    	         bircost1.appendChild(doc.createTextNode(bircost));
				    	         data1.appendChild(bircost1);
				    	         
				    	         Element birquantity1 = doc.createElement("BirinciTeklifTedarikciStokAdet");
				    	         birquantity1.appendChild(doc.createTextNode(birquantity));
				    	         data1.appendChild(birquantity1);
				    	         
				    	         Element biruretimtarihi1 = doc.createElement("BirinciTeklifTedarikciUretimTarihi");
				    	         biruretimtarihi1.appendChild(doc.createTextNode(biruretimtarihi));
				    	         data1.appendChild(biruretimtarihi1);
				    	         
				    	         Element birhazirliksuresi1 = doc.createElement("BirinciTeklifTedarikciHazirlikSuresi");
				    	         birhazirliksuresi1.appendChild(doc.createTextNode(birhazirliksuresi));
				    	         data1.appendChild(birhazirliksuresi1);
				    	         
				    	         Element ikicompany1 = doc.createElement("İkinciTeklifTedarikci");
				    	         ikicompany1.appendChild(doc.createTextNode(ikicompany));
				    	         data1.appendChild(ikicompany1);
				    	         
				    	         Element ikicost1 = doc.createElement("İkinciTeklifTedarikciMaliyet");
				    	         ikicost1.appendChild(doc.createTextNode(ikicost));
				    	         data1.appendChild(ikicost1);
				    	         
				    	         Element ikiquantity1 = doc.createElement("İkinciTeklifTedarikciStokAdet");
				    	         ikiquantity1.appendChild(doc.createTextNode(ikiquantity));
				    	         data1.appendChild(ikiquantity1);
				    	         
				    	         Element ikiuretimtarihi1 = doc.createElement("İkinciTeklifTedarikciUretimTarihi");
				    	         ikiuretimtarihi1.appendChild(doc.createTextNode(ikiuretimtarihi));
				    	         data1.appendChild(ikiuretimtarihi1);
				    	         
				    	         Element ikihazirliksuresi1 = doc.createElement("İkinciTeklifTedarikciHazirlikSuresi");
				    	         ikihazirliksuresi1.appendChild(doc.createTextNode(ikihazirliksuresi));
				    	         data1.appendChild(ikihazirliksuresi1);
				    	         
				            }
							
							

							
							outputJSONObject.put("data", newJsonList);
				 
				 }
				
				
						
						JSONObject output;
						output = new JSONObject(outputJSONObject);

						
				
				//System.out.println(outputJSONObject);
				
				 TransformerFactory transformerFactory = TransformerFactory.newInstance();
		         Transformer transformer = transformerFactory.newTransformer();
		         DOMSource source = new DOMSource(doc);
				 StreamResult consoleResult = new StreamResult(System.out);
		         transformer.transform(source, consoleResult);
				
		         
		         StreamResult result = new StreamResult(new StringWriter());
		         DOMSource source1 = new DOMSource(doc);
		         transformer.transform(source1, result);
		         
		         
				String xmlResult = result.getWriter().toString();
		         
				outputJSONObject.put("exec_status", xmlResult);
				

				
				return xmlResult;
			
		
	
	
	
			 }
	


	
	catch (Exception e) {
		JSONObject outputJSONObject = new JSONObject();			
		outputJSONObject.put("exec_status", "error");
		outputJSONObject.put("error_message", e.getMessage());		

		
		return outputJSONObject.toJSONString();
	}
			
	}

	
	
	 	@POST
		@Path("/GetPO/")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		public String GetPO() throws ParseException 
		{
	 		
	 		String token = null;
			String company = null;
			String test1 = null;
			Integer bir = 0;
			Integer iki = 0;
			String FlxCevap= null;
		try 
		{
			
		 	JSONParser parser = new JSONParser();
			
			JSONObject outputJSONObject = new JSONObject();
		

			

			
			try {
		        Class.forName("org.postgresql.Driver");
		    } catch (ClassNotFoundException e) {
		        System.out.println("Class not found " + e);
		    }
		
		
			try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
				 
				   Statement statement = connection.createStatement();
		            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.user");
		            while (resultSet.next()) {	
		            	
		            	
		        		token = resultSet.getString("authtokenflxpoint");
		        		company = resultSet.getString("company");
		        		mail = resultSet.getString("mail");
		        		System.out.println(company + token + mail);
		        		
		        		test1 = company + token + mail;
		        		
		        		if(mail != null) {
		        		String purchaseOrderFromFlxPoint = GetPurchaseOrderFromFlxPoint(token);
		        		FlxCevap = purchaseOrderFromFlxPoint;
		        		
		        		Boolean dbSaveResult = SavePurchaseOrderToDB1(purchaseOrderFromFlxPoint, company);
		        		
		        	
		        		}
		        		
		        		
		        		
		            }
					
		        } catch (Exception e) {
		        	
		        	System.out.println(e.getMessage());
		        	
		        }

	        
			outputJSONObject.put("exec_status", "success");
			outputJSONObject.put("test1::", test1);
			outputJSONObject.put("purchaseOrderFromFlxPoint::", FlxCevap);
			
			return outputJSONObject.toJSONString();
		
		
		
		
		

		}catch (Exception e) {
			JSONObject outputJSONObject = new JSONObject();
			outputJSONObject.put("exec_status", "error");
			outputJSONObject.put("error_message", e.getMessage());		
			outputJSONObject.put("test1::", test1);
		
			return outputJSONObject.toJSONString();
		}
				
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@POST
	@Path("/Iptal/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String Iptal(String Inputs1) 
	{
	String token = null;
	String company = null;
	String query = null;
	String orderId= null;
	String reason= null;
	try 
	{
	JSONParser parser = new JSONParser();
	JSONObject inputJSONObject = (JSONObject) parser.parse(Inputs1);
	JSONObject outputJSONObject = new JSONObject();
	
	
	company = (String) inputJSONObject.get("company");
	orderId = (String) inputJSONObject.get("orderId");
	reason = (String) inputJSONObject.get("reason");
	
	try {
        Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
        System.out.println("Class not found " + e);
    }
	
	try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
					 
					   Statement statement = connection.createStatement();
			            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.user WHERE company=" + "'" +company + "'" + " LIMIT 1");
			            while (resultSet.next()) {	
			        		token = resultSet.getString("authtokenflxpoint");
			            }
						
			        } catch (Exception e) {
			        	
			        	System.out.println(e.getMessage());
			        	
			        }
	
	
				Client client = ClientBuilder.newClient();
				Response response = client.target("https://integrations.flxpoint.com/v1/purchase-orders/"+ orderId +"/cancel?reason=" +reason)
						  .request(MediaType.APPLICATION_JSON_TYPE)
						  .header("Authorization", token)
						  .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
						  .method("PATCH",Entity.json(""));
				
				
				//System.out.println(response.readEntity(String.class));
				
				
				
				
	
	
	
	
		 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
        

		 
        int affectedrows = 0;
		 
        Statement statement = connection.createStatement();
        affectedrows = statement.executeUpdate("UPDATE public.flxpointpurchaseorders SET purchaseorderstatusid=" + "'" + 9 + "'" +" WHERE id=" + "'" +orderId+ "'");
      
        //System.out.println("affectedrows: "+ affectedrows);
		
        
        
		outputJSONObject.put("exec_status", "success");
		

		
		return outputJSONObject.toJSONString();
	
	
	
	
	
	}catch (SQLException e) {
        	
			outputJSONObject.put("exec_status", "error");
			outputJSONObject.put("error_message", e.getMessage());		

			
			return outputJSONObject.toJSONString();
        }
	


	
	}catch (Exception e) {
		JSONObject outputJSONObject = new JSONObject();			
		outputJSONObject.put("exec_status", "error");
		outputJSONObject.put("error_message", e.getMessage());		

		
		return outputJSONObject.toJSONString();
	}
			
	}

	
	@POST
	@Path("/GetSiparisler/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String GetSiparisler(String Inputs1) 
	{
		String token = null;
		String company = null;
		String query = null;
		try 
		{
		JSONParser parser = new JSONParser();
		JSONObject inputJSONObject = (JSONObject) parser.parse(Inputs1);
		JSONObject outputJSONObject = new JSONObject();
		
		
		company = (String) inputJSONObject.get("company");
		
		
		
		try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found " + e);
        }
		
		 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
			 
						   Statement statement = connection.createStatement();
				            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.user WHERE company=" + "'" +company + "'" + " LIMIT 1");
				            while (resultSet.next()) {	
				        		token = resultSet.getString("authtokenflxpoint");
				            }
							
				        } catch (Exception e) {
				        	
				        	System.out.println(e.getMessage());
				        	
				        }
		//yeni mail atma işi sebebi ile aşağısı kapatıldı.
		
		//String purchaseOrderFromFlxPoint = GetPurchaseOrderFromFlxPoint(token);
		
		//Boolean dbSaveResult = SavePurchaseOrderToDB(purchaseOrderFromFlxPoint, company);
		
		
		query = "SELECT * FROM public.flxpointpurchaseorders WHERE company=" + "'" +company + "'" + "ORDER BY id DESC";
		
	
			 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
	        
			JSONArray newJsonList = new JSONArray();
			 
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
			
            while (resultSet.next()) {
            	
            	String[] columns = {"id", "purchaseOrderNumber", "orderId", "transferOrderId", "shippingText", "sentAt", "sourceId", "holdUntil", "acknowledgedAt", "canceledAt", "shippingAddress", "billingAddress", "shippingAddressId", "billingAddressId", "note", "confirmationNumber", "purchaseOrderStatusId", "generatedAt", "voidedAt", "purchaseOrderFulfillmentStatusId", "accountId", "totalItems", "totalCost", "totalQuantity", "shippedQuantity", "suppressTracking", "estimatedShippingCost", "estimatedDropshipFee", "sourceShippingMethodId", "sourceShippingMethod", "packageDimensionUnitId", "packageLength", "packageWidth", "packageHeight", "packageWeightUnitId", "packageWeight", "items", "attributes", "fflInfoRequired", "fflInfo", "lastModifiedAt", "accountingSynced", "externalAccountingId", "rateShoppedId", "cancelReason", "voidedReason", "channelId", "attachmentLinks", "shippingDistance","status","company","barkodlink"};
            	
            	JSONObject newElement = new JSONObject();
            	for(int a=0; a<52; a++) {
            		
            		newElement.put(columns[a], resultSet.getString(columns[a]));

            	}

        		newJsonList.add(newElement);
            	
            	
            	
            }
			
    		outputJSONObject.put("exec_status", "success");
    		outputJSONObject.put("data", newJsonList);

    		
    		return outputJSONObject.toJSONString();
		
		
		
		
		
		}catch (SQLException e) {
	        	
				outputJSONObject.put("exec_status", "error");
				outputJSONObject.put("error_message", e.getMessage());		

				
				return outputJSONObject.toJSONString();
	        }
		


		
		}catch (Exception e) {
			JSONObject outputJSONObject = new JSONObject();			
			outputJSONObject.put("exec_status", "error");
			outputJSONObject.put("error_message", e.getMessage());		

			
			return outputJSONObject.toJSONString();
		}
	}
	
	
	
	
	@POST
	@Path("/GetFaturalar/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String GetFaturalar(String Inputs1) 
	{
		String token = null;
		String company = null;
		String query = null;
		try 
		{
		JSONParser parser = new JSONParser();
		JSONObject inputJSONObject = (JSONObject) parser.parse(Inputs1);
		JSONObject outputJSONObject = new JSONObject();
		
		
		//company = (String) inputJSONObject.get("company");
	
		
		
		try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found " + e);
        }
		
	
					
		
		
	
		
		query = "SELECT * FROM public.flxpointpurchaseorders ORDER BY id DESC";
		
	
			 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
	        
			JSONArray newJsonList = new JSONArray();
			 
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
			
            while (resultSet.next()) {
            	
            	String[] columns = {"id", "purchaseOrderNumber", "orderId", "transferOrderId", "shippingText", "sentAt", "sourceId", "holdUntil", "acknowledgedAt", "canceledAt", "shippingAddress", "billingAddress", "shippingAddressId", "billingAddressId", "note", "confirmationNumber", "purchaseOrderStatusId", "generatedAt", "voidedAt", "purchaseOrderFulfillmentStatusId", "accountId", "totalItems", "totalCost", "totalQuantity", "shippedQuantity", "suppressTracking", "estimatedShippingCost", "estimatedDropshipFee", "sourceShippingMethodId", "sourceShippingMethod", "packageDimensionUnitId", "packageLength", "packageWidth", "packageHeight", "packageWeightUnitId", "packageWeight", "items", "attributes", "fflInfoRequired", "fflInfo", "lastModifiedAt", "accountingSynced", "externalAccountingId", "rateShoppedId", "cancelReason", "voidedReason", "channelId", "attachmentLinks", "shippingDistance","status","company","gelentutar","gelenmtb","iademtb","fiyatfarkmtb"};
            	
            	JSONObject newElement = new JSONObject();
            	for(int a=0; a<55; a++) {
            		
            		newElement.put(columns[a], resultSet.getString(columns[a]));

            	}

        		newJsonList.add(newElement);
            	
            	
            	
            }
			
    		outputJSONObject.put("exec_status", "success");
    		outputJSONObject.put("data", newJsonList);

    		
    		return outputJSONObject.toJSONString();
		
		
		
		
		
		}catch (SQLException e) {
	        	
				outputJSONObject.put("exec_status", "error");
				outputJSONObject.put("error_message", e.getMessage());		

				
				return outputJSONObject.toJSONString();
	        }
		


		
		}catch (Exception e) {
			JSONObject outputJSONObject = new JSONObject();			
			outputJSONObject.put("exec_status", "error");
			outputJSONObject.put("error_message", e.getMessage());		

			
			return outputJSONObject.toJSONString();
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
			String uretimTarihi = String.valueOf(row.get("Ürt.Tar."));
			String teradikciSKU = (String) (row.get("Tedarikçi SKU"));
			String hazirlikSuresi = String.valueOf(row.get("Hz.Sr."));
			
			cost = cost.replace(",",".");
			
			
			
			//SKU, Cost (satiş fiyatı), quantitiy (stok adet)
			
			resultDbSave = SaveExcelDataToDB(SKU, quantity, cost, company, uretimTarihi, teradikciSKU, hazirlikSuresi);
			try {
	            Class.forName("org.postgresql.Driver");
	        } catch (ClassNotFoundException e) {
	            System.out.println("Class not found " + e);
	        }
			
			 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
		
		       	 
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
				
				
				
				KucukGuncelleme(SKU, cost, quantity, company);
				
				
				
				
				
				outputJSONObject.put("SKU", SKU);
				outputJSONObject.put("exec_status", "success");
				
				outputJSONObject.put("cost:", cost);
				
				
				
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
	
	
	
	
	@POST
	@Path("/SaveFatura/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String SaveFatura(String Inputs1) 
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
			
			
			String POnumber = (String) row.get("SAS No");
			String gelenTutar = (String) row.get("Gelen Tutar");
			String GelenMtb = (String) row.get("Gelen Mtb");
			String IadeMtb = (String) row.get("İade Mtb");
			String FiyatFarkMtb = (String) row.get("Fiyat-Fark Mtb");
			
			try {
	            Class.forName("org.postgresql.Driver");
	        } catch (ClassNotFoundException e) {
	            System.out.println("Class not found " + e);
	        }
			 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
		
	       	 
				 JSONArray newJsonList = new JSONArray();
				 
				 int affectedrows = 0;
				 
		            Statement statement = connection.createStatement();
		            affectedrows = statement.executeUpdate("UPDATE public.flxpointpurchaseorders SET gelentutar=" + "'" + gelenTutar + "'" + ", gelenmtb ="+ "'"+GelenMtb     + "'" + ", iademtb ="+ "'"+IadeMtb  + "'" + ", fiyatfarkmtb ="+ "'"+FiyatFarkMtb + "'"+" WHERE purchaseordernumber=" + "'" +POnumber+ "'" );
		            
		            
					
					
		        } catch (Exception e) {
		        	 System.out.println("aaa");
		        	System.out.println(e.getMessage());
		        	
		        }
			
			outputJSONObject.put("gelenTutar", gelenTutar);
			outputJSONObject.put("POnumber", POnumber);
			outputJSONObject.put("exec_status", "success");
			
			return outputJSONObject.toJSONString();
		}
		catch (Exception e) 
		{			
			JSONObject outputJSONObject = new JSONObject();			
			outputJSONObject.put("exec_status", "error");
			outputJSONObject.put("error_message", e.getMessage());		

			
			return outputJSONObject.toJSONString();
		}
	}
	
	
	
	@POST
	@Path("/GetSiparisDurumlar/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String GetSiparisDurumlar(String Inputs1) 
	{

		String company = "";
		int yeniSiparisSayisi = 0;
		int onaylananSiparisSayisi = 0;
		int iptalEdilenSiparisSayisi = 0;

		try 
		{	
			JSONParser parser = new JSONParser();
			JSONObject inputJSONObject = (JSONObject) parser.parse(Inputs1);
			
			
			company = (String) inputJSONObject.get("company");
			
			
			
			try {
	            Class.forName("org.postgresql.Driver");
	        } catch (ClassNotFoundException e) {
	            System.out.println("Class not found " + e);
	        }
			 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
			
	        	 
				 JSONArray newJsonList = new JSONArray();
				 
		            Statement statement = connection.createStatement();
		            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.flxpointpurchaseorders WHERE company=" + "'" +company + "'");
		            while (resultSet.next()) {
		            	
		            	
		            	if(resultSet.getString("purchaseorderstatusid").equals("7")) {
		            		yeniSiparisSayisi++;
		            		
			            }
		            	
		            	if(resultSet.getString("purchaseorderstatusid").equals("8")) {
		            		onaylananSiparisSayisi++;
		            		
			            }
		        		
		            	if(resultSet.getString("purchaseorderstatusid").equals("9")) {
		            		iptalEdilenSiparisSayisi++;
		            		
			            }

		            }
 
		            
		            
		            
		            JSONObject outputJSONObject = new JSONObject();
					
		            

						outputJSONObject.put("exec_status", "success");
						outputJSONObject.put("token", "111");
						outputJSONObject.put("yeniSiparisSayisi", yeniSiparisSayisi);
						outputJSONObject.put("onaylananSiparisSayisi", onaylananSiparisSayisi);
						outputJSONObject.put("iptalEdilenSiparisSayisi", iptalEdilenSiparisSayisi);
				
					
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
	
	

	@POST
	@Path("/SevkiyatEkle/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String SevkiyatEkle(String Inputs1) 
	{
		String il = null;
		String ilce = null;
		String company = null;
		String orderId = null;
		String shippingaddress = null;
		String items = null;
		String ConsigneeAddress = null;
		String ConsigneeName = null;
		String ConsigneePhoneNumber = null;
		String cityCode=null;
		String areaCode = null;
		String NumberOfPackages = null;
		String DescriptionOfGoods = null;
		String token=null;
		String ilKod=null;
		String sessionId=null;
		String kod1=null;
		String kod2=null;
		String kod3=null;
		String kod4=null;
		String alreadyKargoLink=null;
		String alreadyKargoNumber=null;
		String idForFlxPoint = null;
		String sku =null;
		
		labelLink=null;
		shippingCode=null;
		
		xml=null;
		xml12=null;
		rspFromUPS=null;
		rspStatusFromUps=null;
		
		try 
		{	
			JSONParser parser = new JSONParser();
			JSONObject inputJSONObject = (JSONObject) parser.parse(Inputs1);
			
			
			company = (String) inputJSONObject.get("company");
			il = (String) inputJSONObject.get("il");
			ilce = (String) inputJSONObject.get("ilce");
			orderId = (String) inputJSONObject.get("orderId");
			ilKod = (String) inputJSONObject.get("ilKod");
			sku = (String) inputJSONObject.get("itemsSku");
			
			try {
	            Class.forName("org.postgresql.Driver");
	        } catch (ClassNotFoundException e) {
	            System.out.println("Class not found " + e);
	        }
			
			 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
			
		       	 
				   Statement statement = connection.createStatement();
		            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.user WHERE company=" + "'" +company + "'" + " LIMIT 1");
		            while (resultSet.next()) {	
		        		token = resultSet.getString("authtokenflxpoint");
		            }
					
		        } catch (Exception e) {
		        	 
		        	System.out.println(e.getMessage());
		        	
		        }
			 
			 
			 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
		
				       	 
						   Statement statement = connection.createStatement();
				            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.flxpointpurchaseorders WHERE company=" + "'" +company + "' AND purchaseordernumber="+ "'" +orderId + "'");
				            while (resultSet.next()) {	
				            	alreadyKargoLink = resultSet.getString("barkodlink");
				            	alreadyKargoNumber=resultSet.getString("kargotakipnumarasi");
				            	idForFlxPoint = resultSet.getString("id");
				            }
							
				        } catch (Exception e) {
				        	 
				        	System.out.println(e.getMessage());
				        	
				        }
			 JSONObject outputJSONObject = new JSONObject();
			 
			 if(alreadyKargoLink==null) {
				  

				 kod1 = "1";
				 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
			
			        	 
						 JSONArray newJsonList = new JSONArray();
						 
				            Statement statement = connection.createStatement();
				            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.flxpointpurchaseorders WHERE company=" + "'" +company + "' AND purchaseordernumber="+ "'" +orderId + "'");
				            while (resultSet.next()) {
				            	
				        items = resultSet.getString("items");
				        
				    	
				    	JSONArray itemsJSONArray = (JSONArray) parser.parse(items);
				    	JSONObject itemsJSONObject = (JSONObject) parser.parse( itemsJSONArray.get(0).toString());
				    
				    	DescriptionOfGoods =  (String) itemsJSONObject.get("title");
				    	if(DescriptionOfGoods.length() >127) {
				    		DescriptionOfGoods = DescriptionOfGoods.substring(0, 127);
				    	}
				    	
				    	
				        shippingaddress = resultSet.getString("shippingaddress");
						JSONObject shippingaddressJSONObject = (JSONObject) parser.parse(shippingaddress);
						
						String addressLine1 =  (String) shippingaddressJSONObject.get("addressLine1");
						
						if(shippingaddressJSONObject.get("addressLine2") != null) {
							String addressLine2 =  (String) shippingaddressJSONObject.get("addressLine2");
							ConsigneeAddress = addressLine1 + " "+ addressLine2;
						}else {
							ConsigneeAddress = addressLine1;
						}
						
						ConsigneeName = (String) shippingaddressJSONObject.get("name");
						ConsigneePhoneNumber = (String) shippingaddressJSONObject.get("phone");
						 kod2="2222";
						 if(ConsigneePhoneNumber !=null) {
							ConsigneePhoneNumber = ConsigneePhoneNumber.replace(" ","");
							ConsigneePhoneNumber = ConsigneePhoneNumber.replace("+90","");
							ConsigneePhoneNumber = ConsigneePhoneNumber.replace("055","55");
						 }
						
						NumberOfPackages = resultSet.getString("totalquantity");
				            }

				            
				            
					 }   
					
				 kod2="2";
					
					 
					try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
				
			        	 
						 JSONArray newJsonList = new JSONArray();
						 
				            Statement statement = connection.createStatement();
				            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.upslokasyonkod WHERE default_name=" + "'" +ilce   + "' AND city_code="+ "'" +ilKod + "'");
				            while (resultSet.next()) {
				            	
				            	cityCode = resultSet.getString("city_code");
				            	areaCode = resultSet.getString("id");
								
				            }

					 }   
					
					kod3="3";
				 
					sessionId =login(); 
					
					if(ConsigneePhoneNumber.length()>11) {
						ConsigneePhoneNumber = "08502888020";
					}
					
			            
					createShipment(sessionId,ConsigneeAddress,ConsigneeName,ConsigneePhoneNumber,NumberOfPackages,DescriptionOfGoods,areaCode,cityCode);
					
			          
					
					if(shippingCode.length() > 5) {
						Client client = ClientBuilder.newClient();
						Entity payload = Entity.json("{ \"carrier\": \"UPS\",  \"method\": \"Ground\",  \"trackingNumber\":"+ "\""+ shippingCode +"\"" +",  \"suppressed\": false,  \"items\": [    {      \"sku\":" + "\"" +sku +"\""+ ",      \"quantity\":" + "\"" + NumberOfPackages +"\"" + "   }  ]}");
						Response response = client.target("https://integrations.flxpoint.com/v1/purchase-orders/" +idForFlxPoint +"/shipments")
						  .request(MediaType.APPLICATION_JSON_TYPE)
						  .header("Authorization", token)
						  .post(payload);
					}
					
					try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
			
			        	 
						Statement statement = connection.createStatement();
				        statement.executeUpdate("UPDATE public.flxpointpurchaseorders SET purchaseorderstatusid=" + "'" + 10 + "'" + ",barkodlink="+ "'" + labelLink + "'"	+",kargotakipnumarasi= " 	+ "'" + shippingCode + "'"	+	",purchaseorderfulfillmentstatusid= " 	+ "'" +2 + "'"	+	"WHERE purchaseordernumber=" + "'" +orderId+ "'");
				      
				            
					 }   
					
					
					
			           

							outputJSONObject.put("exec_status", "success");
							outputJSONObject.put("token", "111");
							outputJSONObject.put("labelLink", labelLink);
							outputJSONObject.put("shippingCode", shippingCode);
							
			 }else {
				 outputJSONObject.put("exec_status", "success");
					outputJSONObject.put("token", "111");
					outputJSONObject.put("labelLink", alreadyKargoLink);
					outputJSONObject.put("shippingCode", alreadyKargoNumber);
					
			 }
			 
			 
		
				
			
			 
			 
			
					
					return outputJSONObject.toJSONString();
					
		        
			
			
		}
		catch (Exception e) 
		{			
			JSONObject outputJSONObject = new JSONObject();			
			outputJSONObject.put("exec_status", "error");
			outputJSONObject.put("error_message", e.getMessage());		
			outputJSONObject.put("kod1", kod1);		
			outputJSONObject.put("kod2", kod2);		
			outputJSONObject.put("xml12", xml12);		
			//outputJSONObject.put("xml", xml);	
			return outputJSONObject.toJSONString();
		}
	}
	
	
	

	public static String login() throws SQLException {
		try {
			 String url = "https://ws.ups.com.tr/wsCreateShipment/wsCreateShipment.asmx?op=Login_Type1";
			 URL obj = new URL(url);
			 HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			 con.setRequestMethod("POST");
			 con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
			 String CustomerNumber="8335F4";
			 String UserName="brvGe38SAbbHArRMHJ3V";
			 String Password="kBc2egkVSt9Gpy0VQLPL";
			 String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
			 		+ "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n"
			 		+ "  <soap12:Body>\n"
			 		+ "    <Login_Type1 xmlns=\"http://ws.ups.com.tr/wsCreateShipment\">\n"
			 		+ "      <CustomerNumber>" +CustomerNumber +"</CustomerNumber>\n"
			 		+ "      <UserName>" + UserName +"</UserName>\n"
			 		+ "      <Password>"+Password  +"</Password>\n"
			 		+ "    </Login_Type1>\n"
			 		+ "  </soap12:Body>\n"
			 		+ "</soap12:Envelope>";
			 con.setDoOutput(true);
			 DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			 wr.writeBytes(xml);
			 wr.flush();
			 wr.close();
			 String responseStatus = con.getResponseMessage();
			 System.out.println(responseStatus);
			 BufferedReader in = new BufferedReader(new InputStreamReader(
			 con.getInputStream()));
			 String inputLine;
			 StringBuffer response = new StringBuffer();
			 while ((inputLine = in.readLine()) != null) {
			 response.append(inputLine);
			 }
			 in.close();
			 System.out.println("response:" + response);
			 System.out.println("-----------------");
			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder;
		        InputSource is;
		        
		        builder = factory.newDocumentBuilder();
	            is = new InputSource(new StringReader(response.toString()));
	            Document doc1 = builder.parse(is);
	            NodeList list = doc1.getElementsByTagName("SessionID");
	           // System.out.println(list.item(0).getTextContent());
		        
		        return list.item(0).getTextContent();
		        
			 
			 } catch (Exception e) {
			 System.out.println(e);
			 }
		return null;
		
		
		
	
	}
	
	


	public static void createShipment(String SessionId, String ConsigneeAddress, String ConsigneeName, String ConsigneePhoneNumber, String NumberOfPackages, String DescriptionOfGoods, String ConsigneeAreaCode, String ConsigneeCityCode) throws SQLException {
		try {
			 String url = "https://ws.ups.com.tr/wsCreateShipment/wsCreateShipment.asmx?op=CreateShipment_Type1";
			 URL obj = new URL(url);
			 HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			 con.setRequestMethod("POST");
			 con.setRequestProperty("Content-Type","application/soap+xml");
			 String ShipperAccountNumber = "8335F4";
			 String ShipperName = "KOLAYOTO LISTELEME BILISIM SAN.veTIC.";
			 String ShipperAddress = "Adelet Mah. Ozan Abay Cad. NO:38 DEPO:3 Özkardeşler İş Merkezi";
			 int ShipperCityCode = 35;
			 int ShipperAreaCode = 4513;
			 String ShipperPhoneNumber = "08502888020";
			
			 int ServiceLevel=3; //2=Express Servis 3=Standart Service
			 int PaymentType=2; //1=Consignee 2=Shipper 4=Thirt Party
			 String PackageType= "K"; //D=Letter K=Package
			 String CustomerReferance;
			 String CustomerInvoiceNumber;
			 String DeliveryNotificationEmail = "info@kolayoto.com";
			 boolean ReturnLabelLink= true;
			 boolean ReturnLabelImage= true;
			 
			 
		
			 
			 xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
				 		+ "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n"
				 		+ "  <soap12:Body>\n"
				 		+ "    <CreateShipment_Type1 xmlns=\"http://ws.ups.com.tr/wsCreateShipment\">\n"
				 		+ "      <SessionID>"+ SessionId+"</SessionID>\n"
				 		+ "      <ShipmentInfo>\n"
				 		+ "        <ShipperAccountNumber>"+ShipperAccountNumber+"</ShipperAccountNumber>\n"
				 		+ "        <ShipperName>"+ShipperName+"</ShipperName>\n"
				 		+ "        <ShipperAddress>"+ShipperAddress+"</ShipperAddress>\n"
				 		+ "        <ShipperCityCode>"+ShipperCityCode+"</ShipperCityCode>\n"
				 		+ "        <ShipperAreaCode>"+ShipperAreaCode+"</ShipperAreaCode>\n"
				 		+ "        <ShipperPhoneNumber>"+ShipperPhoneNumber+"</ShipperPhoneNumber>\n"
				 		+ "        <ConsigneeName>"+ConsigneeName+"</ConsigneeName>\n"
				 		+ "        <ConsigneeAddress>"+ConsigneeAddress+"</ConsigneeAddress>\n"
				 		+ "        <ConsigneeCityCode>"+ConsigneeCityCode+"</ConsigneeCityCode>\n"
				 		+ "        <ConsigneeAreaCode>"+ConsigneeAreaCode+"</ConsigneeAreaCode>\n"
				 		+ "        <ConsigneePhoneNumber>"+ConsigneePhoneNumber+"</ConsigneePhoneNumber>\n"
				 		+ "        <ServiceLevel>"+ServiceLevel+"</ServiceLevel>\n"
				 		+ "        <PaymentType>"+PaymentType+"</PaymentType>\n"
				 		+ "        <PackageType>"+PackageType+"</PackageType>\n"
				 		+ "        <NumberOfPackages>"+NumberOfPackages+"</NumberOfPackages>\n"
				 		+ "        <DescriptionOfGoods>"+DescriptionOfGoods+"</DescriptionOfGoods>\n"
				 		+ "        <DeliveryNotificationEmail>"+DeliveryNotificationEmail+"</DeliveryNotificationEmail>\n"
				 		+ "      </ShipmentInfo>\n"
				 		+ "      <ReturnLabelLink>"+ReturnLabelLink+"</ReturnLabelLink>\n"
				 		+ "      <ReturnLabelImage>"+ReturnLabelImage+"</ReturnLabelImage>\n"
				 		+ "    </CreateShipment_Type1>\n"
				 		+ "  </soap12:Body>\n"
				 		+ "</soap12:Envelope>";
				 con.setDoOutput(true);
			 
				 xml12 = xml.replaceAll("\\\\", "");
			 DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			
			 
			// wr.writeBytes(xml);
			 wr.write(xml12.getBytes("UTF-8"));
			 wr.flush();
			 wr.close();
			 String responseStatus = con.getResponseMessage();
			 System.out.println(responseStatus);
			 rspStatusFromUps = responseStatus;
			 BufferedReader in = new BufferedReader(new InputStreamReader(
			 con.getInputStream()));
			 String inputLine;
			 StringBuffer response = new StringBuffer();
			 while ((inputLine = in.readLine()) != null) {
			 response.append(inputLine);
			 }
			 in.close();
			 rspFromUPS=response.toString();
			 System.out.println("response:" + response);
			 System.out.println("-----------------");
			
			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder;
		        InputSource is;
		        
		        builder = factory.newDocumentBuilder();
	            is = new InputSource(new StringReader(response.toString()));
	            Document doc1 = builder.parse(is);
	            NodeList list = doc1.getElementsByTagName("ShipmentNo");
	            NodeList list1 = doc1.getElementsByTagName("LinkForLabelPrinting");
	            
	            
	        	labelLink = list1.item(0).getTextContent();
	        	shippingCode=list.item(0).getTextContent();
	        	
	           
		        
		        
			 
			 } catch (Exception e) {
			 System.out.println(e);
			 }
	
		
		
		
	
	}
	

	public static void KucukGuncelleme(String girilenSku, String girilenCost, String girilenQuantity, String girilenCompany) throws SQLException {
		
		 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {     
		 String teklifsayisiSonuc=null;
         String uretimTarihiSonuc=null;
         String hazirlanmaSuresiSonuc=null;
         String tedarikci=null;
         JSONArray newJsonList = new JSONArray();
		 
         int a = 0;
         
         
         tedarikci = switchTerstenTedarikci(girilenCompany);
         
         Statement statementforSelectCheck = connection.createStatement();
         
         ResultSet resultSetforSelectCheck = statementforSelectCheck.executeQuery("Select * from public.flxpointexportteklif where sku= "+"'" + girilenSku+"'" + " AND sourcename = " + "'"+ tedarikci + "' ;");
         
         
         while (resultSetforSelectCheck.next()) {
         	
             a++;
         }
         
         
         if(a == 0) {
        	 
        	 String SQL1 = "INSERT INTO public.flxpointexportteklif (sku, sourcename, quantity, cost) VALUES (?, ?, ?, ?);";
         	PreparedStatement statementinsert = connection.prepareStatement(SQL1,Statement.RETURN_GENERATED_KEYS);
         	
         	statementinsert.setString(1, girilenSku);
         	statementinsert.setString(2, tedarikci);
         	statementinsert.setString(3, girilenQuantity);
         	statementinsert.setString(4, girilenCost);
         	
         	int affectedrows11 = statementinsert.executeUpdate();
        	 
         }else {
        	 Statement statementExportData = connection.createStatement();
          	statementExportData.executeUpdate("UPDATE public.flxpointexportteklif SET cost="+ "'" +girilenCost + "'"+", quantity=" + "'" +girilenQuantity +"'"+" WHERE sourcename="+"'"+ tedarikci + "'" + " and sku ="+"'"+ girilenSku +"'"+";");
              
         
         }
         
     
         
         Statement statement2 = connection.createStatement();
         
         ResultSet resultSet2 = statement2.executeQuery("select *,row_number() OVER () as rnum FROM(	SELECT sku,sourcename,quantity,cost FROM \n"
         		+ "(SELECT sku,sourcename,quantity,cost FROM public.flxpointexportteklif where sku ="+ "'"+ girilenSku + "'"+   "and quantity > '0' ORDER BY SUBSTRING(cost FROM '([0-9]+)')::BIGINT ASC, cost) AS query1\n"
         		+ ") as q1");
         while (resultSet2.next()) {
         	 
         	
         	 Statement statementTeklifSayisi = connection.createStatement();
         	 ResultSet resultSetTeklifSayisi = statementTeklifSayisi.executeQuery("SELECT sku, Count(*) FROM public.flxpointexportteklif where sku=" +"'" +resultSet2.getString("sku") +"'"+" And quantity > '0'  group by sku;");
         	 while (resultSetTeklifSayisi.next()) {
         		 
         		 teklifsayisiSonuc=resultSetTeklifSayisi.getString("count");
         	 }
         	 tedarikci = null;
         	 uretimTarihiSonuc=null;
	             hazirlanmaSuresiSonuc=null;
         	// System.out.println("tedarikci" + tedarikci);
         	 
         	 
         	 
         	 tedarikci = switchTedarikci(resultSet2.getString("sourcename"));
         	// System.out.println("tedarikci" + tedarikci);
         	 
         	 Statement statementUretimTarihi = connection.createStatement();
         	 ResultSet resultSetUretimTarihi = statementUretimTarihi.executeQuery("SELECT * FROM public.items where company=" +"'" +tedarikci +"'"+" And sku=" + "'" + resultSet2.getString("sku") + "';");
         	 while (resultSetUretimTarihi.next()) {
         		 
         		 uretimTarihiSonuc=resultSetUretimTarihi.getString("uretim_tarihi");
         		 hazirlanmaSuresiSonuc=resultSetUretimTarihi.getString("hazirlik_suresi");
         	 }
         	 
         	 
         	// System.out.println("uretimTarihiSonuc" + uretimTarihiSonuc);
         	 
         	 int b = 0;
         	 
             Statement statementforSelectCheckTeklif = connection.createStatement();
             
             ResultSet resultSetforSelectCheckTeklif = statementforSelectCheckTeklif.executeQuery("Select * from public.teklifsirasiteklifsayisi where sku= "+"'" + girilenSku+"'" + " AND company = " + "'"+ tedarikci + "' ;");
             
             
             while (resultSetforSelectCheckTeklif.next()) {
             	
                b++;
             }
             
             
             if(b == 0) {
            	 
            	 String SQL10 = "INSERT INTO public.teklifsirasiteklifsayisi (company, sku, teklifsirasi, teklifsayisi, hazirliksuresi, uretimtarihi, cost, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
             	PreparedStatement statementinsert1 = connection.prepareStatement(SQL10,Statement.RETURN_GENERATED_KEYS);
             	
             	statementinsert1.setString(1, girilenCompany);
             	statementinsert1.setString(2, girilenSku);
             	statementinsert1.setString(3, resultSet2.getString("rnum"));
             	statementinsert1.setString(4, teklifsayisiSonuc);
             	statementinsert1.setString(5, hazirlanmaSuresiSonuc);
             	statementinsert1.setString(6, uretimTarihiSonuc);
             	statementinsert1.setString(7, resultSet2.getString("cost"));
             	statementinsert1.setString(8, resultSet2.getString("quantity"));
             	
             	
             	int affectedrows111 = statementinsert1.executeUpdate();
            	 
             }else {

             	 Statement statement1 = connection.createStatement();
              	statement1.executeUpdate("UPDATE public.teklifsirasiteklifsayisi SET teklifsirasi="+ "'" +resultSet2.getString("rnum") + "'"+", teklifsayisi=" + "'" +teklifsayisiSonuc +"'"+ ", cost="+ "'" +resultSet2.getString("cost")  +"'"+ ", quantity="+"'"+resultSet2.getString("quantity")+"'"+ ", hazirliksuresi="+"'"+hazirlanmaSuresiSonuc +"'"+ ", uretimtarihi="+"'"+ uretimTarihiSonuc+"'"+       " WHERE company="+"'"+ girilenCompany + "'" + " and sku ="+"'"+ girilenSku +"'"+";");
     	         
             	 
             
             }
             
             

         	 Statement statementUpdateItems = connection.createStatement();
         	 statementUpdateItems.executeUpdate("UPDATE public.items SET teklif_adet="+ "'" +teklifsayisiSonuc + "'"+", teklif_sirasi=" + "'" +resultSet2.getString("rnum") +"'"+" WHERE company="+"'"+ tedarikci + "'" + " and sku ="+"'"+ resultSet2.getString("sku") +"'"+";");
	            
         }
		}
	}
	

	public static String switchTedarikci(String flxPointTedarikci) {
		switch (flxPointTedarikci) {
			case "Günmarlas Güney Marmara Lastik Oto.San.Tic.Ltd.Şti.":
				return "gunmarlas";
				
			case "Tekkeliler Otomotiv San. ve Tic.":
				return "tekkeliler";
				
			case "Avrupa Motor Lastik Tic. Ltd. Şti.":
				return "Avrupa Motor Lastik Tic. Ltd. Şti.";
				
			case "EKSPRES LASTİK SANAYİ VE TİCARET AŞ.":
				return "ekspres";
				
			case "Hangar Motorlu Araçlar A.Ş.":
				return "hangar";
				
			case "Özman Otomotiv":
				return "ozman";
				
			case "Koçak Ticaret Oto.Las.Nak.İth.İhr. Ve San.Ltd.Şti.":
				return "kocak";
				
			case "Suyolcu Otomotiv":
				return "suyolcu";
				
			case "Tatko Lastik":
				return "tatko";
				
			case "Hitaş Otomotiv":
				return "hitas";
				
			case "Go Motorsporları":
				return "gomotor";
				
			case "Mert Otomotiv":
				return "mert";
				
			case "Çakırlar Oto Lastik":
				return "cakirlar";
				
			case "Teklas Oto Lastik":
				return "teklas";
				
			case "Yusuf Ziya Keskin":
				return "yuke";
				
			case "Ferhat Değer Aş.":
				return "Ferhat Değer Aş.";
				
			case "Mollaoğlu Otomotiv":
				return "Mollaoğlu Otomotiv";
				
			case "Yılkarlas Otomotiv":
				return "Yılkarlas Otomotiv";
				
			case "USPA Lastik":
				return "USPA Lastik";
				
			case "Kormetal Pazarlama":
				return "Kormetal Pazarlama";
				
			case "Doğan Ticaret":
				return "dogan";
				
			case "PUMALAS DIŞ TİCARET OTOMOTİV VE LASTİK SANAYİ PAZARLAMA LİMİTED ŞİRKETİ":
				return "pumalas";
				
			case "Pirelli Fabrika Stokları":
				return "Pirelli Fabrika Stokları";
				
			case "KolayOtoLastik Depo Stoklari":
				return "kolayotodepo";
				
			case "TUNÇEL JANT OTO LASTİK OTOMOTİV SAN TİC LTD ŞTİ":
				return "tuncel";
				
			case "Aktaş Oto Lastik Ticaret A.Ş.":
				return "aktas";
				
			case "Ademoğlu Oto Lastik":
				return "ademoglu";
				
			default:
				return flxPointTedarikci;
				
			}
	}
	


	public static String switchTerstenTedarikci(String flxPointTedarikci) {
		switch (flxPointTedarikci) {
			case "gunmarlas":
				return "Günmarlas Güney Marmara Lastik Oto.San.Tic.Ltd.Şti.";
				
			case "tekkeliler":
				return "Tekkeliler Otomotiv San. ve Tic.";
				
			case "avrupamotor":
				return "Avrupa Motor Lastik Tic. Ltd. Şti.";
				
			case "ekspres":
				return "EKSPRES LASTİK SANAYİ VE TİCARET AŞ.";
				
			case "hangar":
				return "Hangar Motorlu Araçlar A.Ş.";
				
			case "ozman":
				return "Özman Otomotiv";
				
			case "kocak":
				return "Koçak Ticaret Oto.Las.Nak.İth.İhr. Ve San.Ltd.Şti.";
				
			case "suyolcu":
				return "Suyolcu Otomotiv";
				
			case "tatko":
				return "Tatko Lastik";
				
			case "hitas":
				return "Hitaş Otomotiv";
				
			case "gomotor":
				return "Go Motorsporları";
				
			case "mert":
				return "Mert Otomotiv";
				
			case "cakirlar":
				return "Çakırlar Oto Lastik";
				
			case "teklas":
				return "Teklas Oto Lastik";
				
			case "yuke":
				return "Yusuf Ziya Keskin";
				
			case "ferhat":
				return "Ferhat Değer Aş.";
				
			case "mollaoglu":
				return "Mollaoğlu Otomotiv";
				
			case "yilkarlas":
				return "Yılkarlas Otomotiv";
				
			case "uspa":
				return "USPA Lastik";
				
			case "kormetal":
				return "Kormetal Pazarlama";
				
			case "dogan":
				return "Doğan Ticaret";
				
			case "pumalas":
				return "PUMALAS DIŞ TİCARET OTOMOTİV VE LASTİK SANAYİ PAZARLAMA LİMİTED ŞİRKETİ";
				
			case "Pirelli Fabrika Stokları":
				return "Pirelli Fabrika Stokları";
				
			case "kolayotodepo":
				return "KolayOtoLastik Depo Stoklari";
				
			case "tuncel":
				return "TUNÇEL JANT OTO LASTİK OTOMOTİV SAN TİC LTD ŞTİ";
				
			case "aktas":
				return "Aktaş Oto Lastik Ticaret A.Ş.";
				
			case "ademoglu":
				return "Ademoğlu Oto Lastik";
				
			default:
				return flxPointTedarikci;
				
			}
	}
	
	
	
	 private static String nullEsitle(String deger) 
	 {
		 
		 if(deger == null) {
			 deger = "NULL";
		 }
		 
		 return deger;
	 }

	 
	 public static boolean SavePurchaseOrderToDB1(String data, String company) {
			affectedrows11 = 0;
			System.out.println("333");
			try {
	            Class.forName("org.postgresql.Driver");
	        } catch (ClassNotFoundException e) {
	            System.out.println("Class not found " + e);
	        }
			
			 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
			
	       	 
				 JSONArray newJsonList = new JSONArray();
				 
				
					JSONParser parser = new JSONParser();
					JSONObject inputJSONObject = (JSONObject) parser.parse(data);
					JSONObject inputJSONObject1 = (JSONObject) inputJSONObject.get("data");
					JSONArray inputJSONObject2 =  (JSONArray) inputJSONObject1.get("purchaseOrders");
					
					String SQL = "INSERT INTO public.flxpointpurchaseorders(id, purchaseordernumber, orderid, transferorderid, shippingtext, sentat, sourceid, holduntil, acknowledgedat, canceledat, shippingaddress, billingaddress, shippingaddressid, billingaddressid, note, confirmationnumber, purchaseorderstatusid, generatedat, voidedat, purchaseorderfulfillmentstatusid, accountid, totalitems, totalcost, totalquantity, shippedquantity, suppresstracking, estimatedshippingcost, estimateddropshipfee, sourceshippingmethodid, sourceshippingmethod, packagedimensionunitid, packagelength, packagewidth, packageheight, packageweightunitid, packageweight, items, attributes, fflinforequired, fflinfo, lastmodifiedat, accountingsynced, externalaccountingid, rateshoppedid, cancelreason, voidedreason, channelid, attachmentlinks, shippingdistance, status, company) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
					
					System.out.println(inputJSONObject2.size());
					for(int a = 0; a< inputJSONObject2.size() ; a++) {

						JSONObject inputJSONObject3 = (JSONObject) inputJSONObject2.get(a);
						
						
						
						orderIdFromFlxp11 = inputJSONObject3.get("orderId").toString();
								System.out.println("sss: " + orderIdFromFlxp11);
						String id = inputJSONObject3.get("id").toString();
						
						String idDb= null;
						 Statement statementGet = connection.createStatement();
				            ResultSet resultSet = statementGet.executeQuery("SELECT * FROM public.flxpointpurchaseorders WHERE id=" + "'" +id + "'" + " LIMIT 1");
				            while (resultSet.next()) {	
				            	idDb = resultSet.getString("id");
				            }
				            
						if(idDb == null) {

							PreparedStatement statement = connection.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
							
							DataCheck(inputJSONObject3,statement);
							
							statement.setString(50, "PROCESSING");
							statement.setString(51, company);
							
							System.out.println(statement);
							affectedrows11 = statement.executeUpdate();
							
							System.out.println("--------------------------------");
							System.out.println("company ::" + company);
							System.out.println("id ::" + orderIdFromFlxp11);
							
							sendMail(company, orderIdFromFlxp11, mail);
								
						}else {
							 int affectedrows1 = 0;
							 
						        Statement statement = connection.createStatement();
						        affectedrows1 = statement.executeUpdate("UPDATE public.flxpointpurchaseorders SET purchaseorderstatusid=" + "'" + 7 + "'" +" WHERE id=" + "'" +idDb+ "'");
						      
						        
						        System.out.println("dbde var");
						}
						
						
					}
					System.out.println(affectedrows11);
				 
				 
					return true;
					
		        } catch (Exception e) {
		        
		        	System.out.println(e.getMessage());
		        	return false;
		        }
			
			
		}
		
	
	 
	 public static void sendMail(String company, String SASNo, String mailTo) {

		 
		 String companyisim = switchTerstenTedarikci(company);
		 
		  // Recipient's email ID needs to be mentioned.
     String to = mailTo;

     // Sender's email ID needs to be mentioned
     String from = "info@kolayoto.com";

     // Assuming you are sending email from through gmails smtp
     String host = "email-smtp.us-east-1.amazonaws.com";

     // Get system properties
     Properties properties = System.getProperties();

     // Setup mail server
     properties.put("mail.smtp.host", host);
     properties.put("mail.smtp.port", "587");
     
     properties.put("mail.smtp.auth", "true");
     
     // Get the Session object.// and pass username and password
     Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

         protected PasswordAuthentication getPasswordAuthentication() {

             return new PasswordAuthentication("AKIAJOZOF25XHW2WIJHQ", "Aq04N53p6Nd5SDNlg0smTudgv9Uw53MEgLv2Rsu4NKFB");

         }

     });

    
     session.setDebug(true);

     try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

         // Set Subject: header field
         message.setSubject("Yeni Bir Sipariş Var - "+ SASNo);

         // Now set the actual message
         message.setText("Değerli "+ companyisim + " ,\n"
         		+ "\n"
         		+ "KolayOto'dan yeni bir siparişiniz var!\n"
         		+ SASNo +" numaralı siparişinizle ilgili bilgileri aşağıda görebilirsiniz. \n"
         		+ "Tedarikçi Portalı (https://partner.kolayoto.com/) ziyaret ederek bekleyen işlemlerinizi tamamlamanızı rica ederiz. \n"
         		+ "\n"
         		+ "İşbirliğimizin artarak devam etmesi dileğiyle \n"
         		+ "\n"
         		+ "KolayOto \n"
         		+ "");

         System.out.println("sending...");
         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
     } catch (MessagingException mex) {
         mex.printStackTrace();
     }
		
		
		
	}
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 

		@POST
		@Path("/SaveMultipleRow/")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		public String SaveMultipleRow(String Inputs1) 
		{
			String company = "";
			String token = "";
			String sku = "";
			String quantity = "";
			String cost = "";
			String uretimTarihi = "";
			String teradikciSKU = "";
			String hazirlikSuresi = "";
			int affectedrows = 0;
			
			
			Boolean resultDbSave = false;
			try 
			{	
				JSONParser parser = new JSONParser();
				JSONObject inputJSONObject = (JSONObject) parser.parse(Inputs1);
				JSONObject outputJSONObject = new JSONObject();
				
				
				company = (String) inputJSONObject.get("company");
				
				JSONArray rows =  (JSONArray) inputJSONObject.get("rows");
				
				try {
		            Class.forName("org.postgresql.Driver");
		        } catch (ClassNotFoundException e) {
		            System.out.println("Class not found " + e);
		        }
				
				
				
				try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
					
			       	 
					   Statement statement = connection.createStatement();
			            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.user WHERE company=" + "'" +company + "'" + " LIMIT 1");
			            while (resultSet.next()) {	
			        		token = resultSet.getString("authtokenflxpoint");
			            }
						
			        } catch (Exception e) {
			        	
			        	System.out.println(e.getMessage());
			        	
			        }
				
				
				int b = 0;
				JSONArray row = new JSONArray();
				
				String payloadMain = "";
				
				for(int a = 0; a<rows.size(); a++) {
					
					String payloadCreater = "";
					
					row = (JSONArray) rows.get(a);
					
					sku = (String) row.get(1);
					quantity = (String) row.get(7);
					cost = (String) row.get(5);
					uretimTarihi =  String.valueOf( row.get(10));
					teradikciSKU = (String) row.get(2);
					hazirlikSuresi =  String.valueOf( row.get(11));
					
					int affectedrow = SaveExcelDataToDBMultipleRow(sku, quantity, cost, company, uretimTarihi, teradikciSKU,  hazirlikSuresi);
					
					
					payloadCreater = "{ \"sku\":"+ "\"" + sku + "\"" +", \"variants\": [  {   \"sku\":" + "\"" + sku + "\"" + ",  \"cost\":"+ "\""+ cost+ "\""+ ",  \"archived\": false,  \"quantity\":"+ "\"" + quantity + "\"" + " } ] }  ";
					
					
					if(a != (rows.size()-1 ) ) {
						
						payloadMain = payloadMain + payloadCreater +  "," ;
						
					}
					
					if(a == (rows.size()-1) ) {
						payloadMain = payloadMain + payloadCreater;
					}
					
					
					
					if(affectedrow == 1) {
						affectedrows++;
					}
					
				}
				
				
					
				Entity payload = Entity.json("[" + payloadMain +"]");
				
				
				
				
				
				Client client = ClientBuilder.newClient();
				Response response = client.target("https://integrations.flxpoint.com/v1/source-products")
						  .request(MediaType.APPLICATION_JSON_TYPE)
						  .header("Authorization", token)
						  .post(payload);
				
				
				
				
				
				outputJSONObject.put("exec_status", "success");
				outputJSONObject.put("token", "111");
				/*
				outputJSONObject.put("rowsSize", rows.size());
				outputJSONObject.put("payloadMain", payloadMain);
				outputJSONObject.put("payload", payload);
				outputJSONObject.put("affectedrows", affectedrows);
				*/
				return outputJSONObject.toJSONString();
				
				
				
				
				
				
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