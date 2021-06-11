package com.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.Properties;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.*;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class test {
	 static Logger log = Logger.getLogger(test.class.getName());  
	 
		public static String labelLink=null;
		public static String shippingCode=null;
		
		public static String jdbcURL="jdbc:postgresql://20.52.6.77:5432/po";
		public static String jdbcUser = "pouser";
		public static String jdbcPass = "pouser";

	public static void main(String[] args) throws IOException, ParseException, SQLException, TransformerException, ParserConfigurationException {
		
		String ConsigneePhoneNumber = "+90 553 00 97 936";
		
		ConsigneePhoneNumber = ConsigneePhoneNumber.replace(" ","");
		ConsigneePhoneNumber = ConsigneePhoneNumber.replace("+90","");
		ConsigneePhoneNumber = ConsigneePhoneNumber.replace("055","55");
		
		if(ConsigneePhoneNumber.length()>15) {
			ConsigneePhoneNumber = "08502888020";
		}
		
		
		System.out.println("a:  " + ConsigneePhoneNumber);
        /*
		String girilenSku = "L-20800";
		String tedarikci= "Hitaş Otomotiv";
		String girilenQuantity = "3";
		String girilenCost = "15";
		int a = 0;
		
		 try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {     
Statement statementforSelectCheck = connection.createStatement();
        
        ResultSet resultSetforSelectCheck = statementforSelectCheck.executeQuery("Select * from public.flxpointexportteklif where sku= "+"'" + girilenSku+"'" + " AND sourcename = " + "'"+ tedarikci + "' ;");
        
        
        while (resultSetforSelectCheck.next()) {
        	
            a++;
        }
		
        System.out.println("a:  " + a);
        
        if(a == 0 ) {
        	String SQL = "INSERT INTO public.flxpointexportteklif (sku, sourcename, quantity, cost) VALUES (?, ?, ?, ?);";
        	PreparedStatement statement = connection.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
        	
        	statement.setString(1, girilenSku);
        	statement.setString(2, tedarikci);
        	statement.setString(3, girilenQuantity);
        	statement.setString(4, girilenCost);
        	
        	int affectedrows11 = statement.executeUpdate();
        }else {
        	 System.out.println("a:ssss  " + a);
        }
        
        
       
        
		 }
		*/
		/*
		String SKU = "L-214536";
		String cost = "290";
		String quantity = "50";
		cost = cost.replace(",",".");
		Entity payload = Entity.json("[  {    \"sku\":"+ "\"" +SKU+ "\"" +",        \"variants\": [      {        \"sku\":" + "\"" + SKU + "\"" + ",              \"cost\":" + "\"" + cost + "\"" +  ",     \"archived\": false,    \"quantity\":" + "\"" +  quantity + "\"" + "      }    ] }]");
		
		
		Client client = ClientBuilder.newClient();
		
		
		System.out.println("payload:  " + payload);
		
		

		
		
		
		Response response = client.target("https://integrations.flxpoint.com/v1/source-products")
		  .request(MediaType.APPLICATION_JSON_TYPE)
		  .header("Authorization", "Bearer gcO0wgSeuDZ2TLyZJfjVsOfF5h430IxN1zeusfeNTN1G5doNqmcpCEqvfTqXpxHZd7eZRjzWRhOmG8qppioDHhwObPEvj5VV6KQg")
		  .post(payload);

		System.out.println("status: " + response.getStatus());
		System.out.println("headers: " + response.getHeaders());
		System.out.println("body:" + response.readEntity(String.class));
		*/
		/*
		
		String phone = "+90 505 522 09 15";
		
		
	
		
		String sessionId = null;
		
		
		sessionId =login(); 
		System.out.println(sessionId); 
		
		
		String ConsigneeAddress = "Adalet mah 10078 sk no:8/A";
		String ConsigneeName = "Serkan Toker";
		String ConsigneePhoneNumber = "0 541 917 18 86";
		
		System.out.println(ConsigneePhoneNumber);
		System.out.println(ConsigneePhoneNumber.replace("+90",""));
		
		ConsigneePhoneNumber= ConsigneePhoneNumber.replace("+90","");
		
		
		String NumberOfPackages = "4";
		String DescriptionOfGoods = null;
		String areaCode = "264";
		String cityCode = "20";
		
		createShipment(sessionId,ConsigneeAddress,ConsigneeName,ConsigneePhoneNumber,NumberOfPackages,DescriptionOfGoods,areaCode,cityCode);
*/
		
		
		
		/*
		JSONObject outputJSONObject = new JSONObject();
		DocumentBuilderFactory dbFactory =
		         DocumentBuilderFactory.newInstance();
		         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		         Document doc = dBuilder.newDocument();
		         
		         // root element
		         Element rootElement = doc.createElement("body");
		         doc.appendChild(rootElement);
		
		
		
		//try (Connection connection = DriverManager.getConnection("jdbc:postgresql://20.52.6.77:5432/po", "pouser", "pouser")) {
				try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {
					
					
					JSONArray newJsonList = new JSONArray();
					
					
					Statement statement = connection.createStatement();
		            ResultSet resultSet = statement.executeQuery("SELECT sku FROM public.teklifsirasiteklifsayisi LIMIT 10;");
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
        // transformer.transform(source, consoleResult);
		
         
         String onur = null;
         
         StreamResult result = new StreamResult(new StringWriter());
         DOMSource source1 = new DOMSource(doc);
         transformer.transform(source1, result);
         
         
         onur = result.getWriter().toString();
         
         System.out.println("------------------");
         System.out.println(onur);
		*/
		/*
		try {
	         DocumentBuilderFactory dbFactory =
	         DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.newDocument();
	         
	         // root element
	         Element rootElement = doc.createElement("body");
	         doc.appendChild(rootElement);

	         // supercars element
	         Element supercar = doc.createElement("data");
	         rootElement.appendChild(supercar);


	         // carname element
	         Element carname = doc.createElement("BirinciTeklifTedarikciHazirlikSuresi");
	         carname.appendChild(doc.createTextNode("1"));
	         supercar.appendChild(carname);

	         
	         
	         Element supercar1 = doc.createElement("data");
	         rootElement.appendChild(supercar1);
	         
	         Element carname2 = doc.createElement("BirinciTeklifTedarikciHazirlikSuresi");
	         carname2.appendChild(doc.createTextNode("1"));
	         supercar1.appendChild(carname2);
	       
	         // write the content into xml file
	         TransformerFactory transformerFactory = TransformerFactory.newInstance();
	         Transformer transformer = transformerFactory.newTransformer();
	         DOMSource source = new DOMSource(doc);
	       //  StreamResult result = new StreamResult(new File("C:\\cars.xml"));
	        // transformer.transform(source, result);
	         
	         // Output to console for testing
	         StreamResult consoleResult = new StreamResult(System.out);
	         transformer.transform(source, consoleResult);
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		
		
		*/
		
		
		
		
		
		
		
		
		
		
		
		
		
	/*
		String sku = "L-21450";
		String quantity= "2";
		String cost = "239";
		String company = "kolayotodepo";
		
		 try (Connection connection = DriverManager.getConnection("jdbc:postgresql://20.52.6.77:5432/po", "pouser", "pouser")) {
				//try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {
		       	 
					 JSONArray newJsonList = new JSONArray();
					 
					 int affectedrows = 0;
					 
			            Statement statement = connection.createStatement();
			            affectedrows = statement.executeUpdate("UPDATE public.items SET fiyat=" + "'" + cost + "'" + ", stok_adet ="+quantity+" WHERE sku=" + "'" +sku+ "'" + " AND company=" +"'" + company + "'");
			            
			            System.out.println("affectedrows: "+ affectedrows);
						
						
			        } catch (Exception e) {
			        	 System.out.println("aaa");
			        	System.out.println(e.getMessage());
			        	
			        }
		*/
		
		/*
		String testSKU = "L-1017231";
		
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres")) {
       	 
			 JSONArray newJsonList = new JSONArray();
			 
	            Statement statement = connection.createStatement();
	            ResultSet resultSet = statement.executeQuery("select *,row_number() OVER () as rnum FROM(	SELECT sku,sourcename,quantity,cost FROM \n"
	            		+ "(SELECT sku,sourcename,quantity,cost FROM public.testsira where sku ="+ "'"+ testSKU + "'"+   "and quantity > '0' ORDER BY cost ASC) AS query1\n"
	            		+ ") as q1");
	            while (resultSet.next()) {
	            	
	            	 System.out.println(" teklif sırası::" + resultSet.getString("rnum") +"------"+ resultSet.getString("sku") + " tedarikci::" + resultSet.getString("sourcename")+ " quantity::" + resultSet.getString("quantity")+ " cost::" + resultSet.getString("cost"));  
	 	      
					
	            }

	           
		 }  
		*/
		
		/*
		String sku = "L-21482";
		String shippingCode = "1Z8335F46800668573";
		String idForFlxPoint= "474904";
		Client client = ClientBuilder.newClient();
		Entity payload = Entity.json("{ \"carrier\": \"UPS\",  \"method\": \"Ground\",  \"trackingNumber\":"+ "\""+ shippingCode +"\"" +",  \"suppressed\": false,  \"items\": [    {      \"sku\":" + "\"" +sku +"\""+ ",      \"quantity\": 1    }  ]}");
		Response response = client.target("https://integrations.flxpoint.com/v1/purchase-orders/" +idForFlxPoint +"/shipments")
		  .request(MediaType.APPLICATION_JSON_TYPE)
		  .header("Authorization", "Bearer uxetgjZDwg54HBZ4jgKn8qkBaWrRxgiwsfhKGAQCMrc5ChzMjiTGzzentSeMByjKwmhIKDcAQHZ4lYq0q0QvXiLPdWfqbJbD1BlC")
		  .post(payload);

		System.out.println("status: " + response.getStatus());
		System.out.println("headers: " + response.getHeaders());
		System.out.println("body:" + response.readEntity(String.class));
		
		*/
	
		
	
		
		

		/*
		
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
		
			JSONParser parser = new JSONParser();
			
			
			company = "kolayotodepo";
			il = "Izmir";
			ilce = "BAYRAKLI";
			orderId = "5062416-1";
			ilKod = "35";
			sku = "A-1004303";
			
			try {
	            Class.forName("org.postgresql.Driver");
	        } catch (ClassNotFoundException e) {
	            System.out.println("Class not found " + e);
	        }
			
			 try (Connection connection = DriverManager.getConnection("jdbc:postgresql://20.52.6.77:5432/po", "pouser", "pouser")) {
			//try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {
		       	 
				   Statement statement = connection.createStatement();
		            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.user WHERE company=" + "'" +company + "'" + " LIMIT 1");
		            while (resultSet.next()) {	
		        		token = resultSet.getString("authtokenflxpoint");
		            }
					
		        } catch (Exception e) {
		        	 
		        	System.out.println(e.getMessage());
		        	
		        }
			 
			 
			 try (Connection connection = DriverManager.getConnection("jdbc:postgresql://20.52.6.77:5432/po", "pouser", "pouser")) {
		//	try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {
				       	 
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
			 
			 System.out.println(alreadyKargoLink);
			 JSONObject outputJSONObject = new JSONObject();
			 
			 if(alreadyKargoLink==null) {
				  

				
				 
				 try (Connection connection = DriverManager.getConnection("jdbc:postgresql://20.52.6.77:5432/po", "pouser", "pouser")) {
				//	try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {
			        	 
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
						
						NumberOfPackages = resultSet.getString("totalquantity");
				            }

				            
				            
					 }   
					
				 kod2="2";
					
					 
					try (Connection connection = DriverManager.getConnection("jdbc:postgresql://20.52.6.77:5432/po", "pouser", "pouser")) {
				//	try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {
			        	 
						 JSONArray newJsonList = new JSONArray();
						 
				            Statement statement = connection.createStatement();
				            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.upslokasyonkod WHERE default_name=" + "'" +ilce   + "' AND city_code="+ "'" +ilKod + "'");
				            while (resultSet.next()) {
				            	
				            	
				            	cityCode = resultSet.getString("city_code");
				            	areaCode = resultSet.getString("id");
								
				            }

				            
					 }   
					
					
					
				 
							 }
			           /*
					
					
					try (Connection connection = DriverManager.getConnection("jdbc:postgresql://20.52.6.77:5432/po", "pouser", "pouser")) {
			//		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {
			        	 
						Statement statement = connection.createStatement();
				        statement.executeUpdate("UPDATE public.flxpointpurchaseorders SET purchaseorderstatusid=" + "'" + 10 + "'" + ",barkodlink="+ "'" + labelLink + "'"	+",kargotakipnumarasi= " 	+ "'" + shippingCode + "'"	+	",purchaseorderfulfillmentstatusid= " 	+ "'" +2 + "'"	+	"WHERE purchaseordernumber=" + "'" +orderId+ "'");
				      
				            
					 }   
					
					 
					System.out.println("idForFlxPoint::" + idForFlxPoint );


					
					Client client = ClientBuilder.newClient();
					Entity payload = Entity.json("{ \"carrier\": \"UPS\",  \"method\": \"Ground\",  \"trackingNumber\":"+ "\""+ shippingCode +"\"" +",  \"suppressed\": false,  \"items\": [    {      \"sku\":" + "\"" +sku +"\""+ ",      \"quantity\": 1    }  ]}");
					Response response = client.target("https://integrations.flxpoint.com/v1/purchase-orders/" +idForFlxPoint +"/shipments")
					  .request(MediaType.APPLICATION_JSON_TYPE)
					  .header("Authorization", "Bearer uxetgjZDwg54HBZ4jgKn8qkBaWrRxgiwsfhKGAQCMrc5ChzMjiTGzzentSeMByjKwmhIKDcAQHZ4lYq0q0QvXiLPdWfqbJbD1BlC")
					  .post(payload);
			

					System.out.println("status: " + response.getStatus());
					System.out.println("headers: " + response.getHeaders());
					System.out.println("body:" + response.readEntity(String.class));
						
			 }else {
				 outputJSONObject.put("exec_status", "success");
					outputJSONObject.put("token", "111");
					outputJSONObject.put("labelLink", alreadyKargoLink);
					outputJSONObject.put("shippingCode", alreadyKargoNumber);
				 
			 }
			 
			 
		
		
			
					
				
		        
			
		
		
		
		
		
		
		*/
		
		
		
		/*Properties props = System.getProperties();
		System.out.println("Current working directory is " + props.getProperty("user.dir"));
		PropertyConfigurator.configure("/log4j.properties");
		
		//C:\Users\soperasyon27\Desktop\BE\test-jersey-rest-maven-tomcat-master\src\main\webapp\WEB-INF\log4j.properties
		  log.debug("Hello this is a debug message");  
	      log.info("Hello this is an info message");  
		
	      */
	      
	      
	      
	      
	      
	      /*
	      
	      String POnumber = "4901287-1";
			String gelenTutar = "1";
			String GelenMtb = null;
			String IadeMtb = null;
			String FiyatFarkMtb = null;
			String company = "dogan";
			
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
		            affectedrows = statement.executeUpdate("UPDATE public.flxpointpurchaseorders SET gelentutar=" + "'" + gelenTutar + "'" + ", gelenmtb ="+ "'"+GelenMtb     + "'" + ", iademtb ="+ "'"+IadeMtb  + "'" + ", fiyatfarkmtb ="+ "'"+FiyatFarkMtb + "'"+" WHERE purchaseordernumber=" + "'" +POnumber+ "'" + " AND company=" +"'" + company + "'");
		            
		            
					
					
		        } catch (Exception e) {
		        	 System.out.println("aaa");
		        	System.out.println(e.getMessage());
		        	
		        }
			*/
		/*
		String ListeFiyatı = "10";
		String fiyat = "0";
		fiyat = fiyat.split("[.,]")[0];

	   System.out.println(fiyat.split("[.,]")[0]);
		
	    
	   	if( (ListeFiyatı != null && ListeFiyatı != "0" ) && (fiyat != null && fiyat != "0")) {
	   		
	   		
	   		
	   		System.out.println("ss");
    	
    	if((!(ListeFiyatı.equals(fiyat) )  ) && (Integer.parseUnsignedInt(ListeFiyatı)>Integer.parseUnsignedInt(fiyat))) {
    		 System.out.println((((  Integer.parseUnsignedInt(ListeFiyatı)- Integer.parseUnsignedInt(fiyat))* 100 / Integer.parseUnsignedInt(fiyat))) );
    	}else {
    		 System.out.println(0);
    	}
    		
    	

    	}else {
    		System.out.println(100);
    	}
	    
	    */
	    
		
		
		 //		 Entity payload = Entity.json("[  {    \"sku\": \"Test-Sku-003\",        \"variants\": [      {        \"sku\": \"Test-Sku-003\",              \"cost\": 21.99,    \"upc\": null,  \"archived\": false,    \"quantity\": 7        }    ] }]");

		// Entity payload = Entity.json("[  {    \"sku\": \"L-313577\",        \"variants\": [      {        \"sku\": \"L-313577\",              \"cost\": 1,    \"upc\": null,  \"archived\": false,    \"quantity\": 1        }    ] }]");
		 
		 // Entity payload = Entity.json("[  {    \"sku\": \"L-20460\",        \"variants\": [      {        \"sku\": \"L-20460\",              \"cost\": 1,    \"upc\": null,  \"archived\": false,    \"quantity\": 1        }    ] }]");
		 
		/* Response r = ClientBuilder.newClient()
				    .target("https://integrations.flxpoint.com/v1/purchase-orders/474904/cancel?reason=reason")
				    .request()
				    .header("Authorization", "Bearer uxetgjZDwg54HBZ4jgKn8qkBaWrRxgiwsfhKGAQCMrc5ChzMjiTGzzentSeMByjKwmhIKDcAQHZ4lYq0q0QvXiLPdWfqbJbD1BlC")
				    .build("PATCH", Entity.text("patch"))
				    .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
				    .invoke();*/
		 
		/*
		 Response response = client.target("https://integrations.flxpoint.com/v1/purchase-orders/purchase_order_id/cancel?reason=reason")
				  .request(MediaType.APPLICATION_JSON_TYPE)
				  .header("Authorization", "Bearer uxetgjZDwg54HBZ4jgKn8qkBaWrRxgiwsfhKGAQCMrc5ChzMjiTGzzentSeMByjKwmhIKDcAQHZ4lYq0q0QvXiLPdWfqbJbD1BlC")
				  .patch();*/

			/*
			
		 
			Response response = client.target("https://integrations.flxpoint.com/v1/purchase-orders?showAcknowledged=true")
					  .request(MediaType.APPLICATION_JSON_TYPE)
					  .header("Authorization", "Bearer uxetgjZDwg54HBZ4jgKn8qkBaWrRxgiwsfhKGAQCMrc5ChzMjiTGzzentSeMByjKwmhIKDcAQHZ4lYq0q0QvXiLPdWfqbJbD1BlC")
					  .get();
			 
			 
		String data = response.readEntity(String.class);
		
		System.out.println("status: " + response);
		System.out.println("headers: " + response.getHeaders());
		System.out.println("body:" + data);
		int affectedrows = 0;
		try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found " + e);
        }
		
		JSONParser parser = new JSONParser();
		JSONObject inputJSONObject = (JSONObject) parser.parse(data);
		JSONObject inputJSONObject1 = (JSONObject) inputJSONObject.get("data");
		JSONArray inputJSONObject2 =  (JSONArray) inputJSONObject1.get("purchaseOrders");
		
		System.out.println("sss: " + inputJSONObject2.size());
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {
	       	 
			 JSONArray newJsonList = new JSONArray();
	
	            
				String SQL = "INSERT INTO public.flxpointpurchaseorders(id, purchaseordernumber, orderid, transferorderid, shippingtext, sentat, sourceid, holduntil, acknowledgedat, canceledat, shippingaddress, billingaddress, shippingaddressid, billingaddressid, note, confirmationnumber, purchaseorderstatusid, generatedat, voidedat, purchaseorderfulfillmentstatusid, accountid, totalitems, totalcost, totalquantity, shippedquantity, suppresstracking, estimatedshippingcost, estimateddropshipfee, sourceshippingmethodid, sourceshippingmethod, packagedimensionunitid, packagelength, packagewidth, packageheight, packageweightunitid, packageweight, items, attributes, fflinforequired, fflinfo, lastmodifiedat, accountingsynced, externalaccountingid, rateshoppedid, cancelreason, voidedreason, channelid, attachmentlinks, shippingdistance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
				
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
						
						
						System.out.println(statement);
						affectedrows = statement.executeUpdate();
							
					}
					
					
					
				}
				
				System.out.println(affectedrows);
			 
			 
				
				
	        } catch (Exception e) {
	        
	        	System.out.println(e.getMessage());
	        
	        }
		
		
		//JSONObject inputJSONObject3 = (JSONObject) inputJSONObject2.get(0);
		*/
		
		/*
		try {
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
				 
				 
				 
				 
				 String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
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
				 
				 System.out.println(xml);
				 
				 DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				//wr.writeBytes(xml);
				 wr.write(xml.getBytes("UTF-8"));
				 
				 wr.flush();
				 wr.close();
				 String responseStatus = con.getResponseMessage();
				 System.out.println("-----------------");
				// System.out.println(xml);
				// System.out.println(responseStatus);
				 System.out.println("-----------------");
				 int respCode= con.getResponseCode();
				 System.out.println(con.getResponseCode());
				
				 System.out.println(con.getErrorStream());
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
		            NodeList list = doc1.getElementsByTagName("ShipmentNo");
		            NodeList list1 = doc1.getElementsByTagName("LinkForLabelPrinting");
		            
		            
		        	labelLink = list1.item(0).getTextContent();
		        	shippingCode=list.item(0).getTextContent();
		        	
		            
		        	 System.out.println(shippingCode);
		            System.out.println(labelLink);
				 
				 } catch (Exception e) {
				 System.out.println(e);
				 }
		
			
			
			
		
		}
		
		
		 private static String nullEsitle(String deger) 
		 {
			 
			 if(deger == null) {
				 deger = "NULL";
			 }
			 
			 return deger;
		 }
	
	
	
	
	 private static Document convertStringToXMLDocument(String response) 
	    {
	        //Parser that produces DOM object trees from XML content
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	         
	        //API to obtain DOM Document instance
	        DocumentBuilder builder = null;
	        try
	        {
	            //Create DocumentBuilder with default configuration
	            builder = factory.newDocumentBuilder();
	             
	            //Parse the content to Document object
	            Document doc = builder.parse(new InputSource(new StringReader(response)));
	            return doc;
	        } 
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	        }
	        return null;
	    }
}