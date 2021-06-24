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


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

public class test2 {
	
	public static String jdbcURL="jdbc:postgresql://20.52.6.77:5432/po";
	public static String jdbcUser = "pouser";
	public static String jdbcPass = "pouser";
	
	public static int affectedrows = 0;
	public static String orderIdFromFlxp = null;
	public static String mail = null;

	public static void main(String[] args) throws IOException, ParseException, SQLException, TransformerException, ParserConfigurationException {
		
		
		
		
		
		
		//db den tüm user ve tokenları çekecek
		
		
		String token = null;
		String company = null;

		
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
				        		
				        		System.out.println("------------------------");
				        		
				        		
				        		if(mail != null) {
				        	
				        			System.out.println(company + token + mail);
					        		//String purchaseOrderFromFlxPoint = GetPurchaseOrderFromFlxPoint(token);
					        		
					        		
					        		
					        		
					        		//Boolean dbSaveResult = SavePurchaseOrderToDB1(purchaseOrderFromFlxPoint, company);
					        		
					        		
					        		
				        		}
				        	
				        		
				        		
				        		
				        		
				            }
							
				        } catch (Exception e) {
				        	
				        	System.out.println(e.getMessage());
				        	
				        }
		
		// gidip bu token lar ile tek tek flxpoint den siparişleri get ile alacak ve db yazacak
		
		
		//eğer bu sorguda yeni sipariş gelirse bunu ilgili tedarikçiye mail atacak.
			
			
			
			
        
    }

		

	public static String GetPurchaseOrderFromFlxPoint(String token) {
		
		Client client = ClientBuilder.newClient();
		Response response = client.target("https://integrations.flxpoint.com/v1/purchase-orders?showAcknowledged=true")
				  .request(MediaType.APPLICATION_JSON_TYPE)
				  .header("Authorization", token)
				  .get();
		
		
		
		return response.readEntity(String.class);
	}
	

	public static boolean SavePurchaseOrderToDB1(String data, String company) {
		affectedrows = 0;
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
					
					
					
					orderIdFromFlxp = inputJSONObject3.get("orderId").toString();
							System.out.println("sss: " + orderIdFromFlxp);
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
						
						System.out.println("--------------------------------");
						System.out.println("company ::" + company);
						System.out.println("id ::" + orderIdFromFlxp);
						
						sendMail(company, orderIdFromFlxp, mail);
							
					}else {
						 int affectedrows1 = 0;
						 
					        Statement statement = connection.createStatement();
					        affectedrows1 = statement.executeUpdate("UPDATE public.flxpointpurchaseorders SET purchaseorderstatusid=" + "'" + 7 + "'" +" WHERE id=" + "'" +idDb+ "'");
					      
					        
					        System.out.println("dbde var");
					}
					
					
				}
				System.out.println(affectedrows);
			 
			 
				return true;
				
	        } catch (Exception e) {
	        
	        	System.out.println(e.getMessage());
	        	return false;
	        }
		
		
	}
	
	
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
	
	
	public static void sendMail(String company, String SASNo, String mailTo) {

		  // Recipient's email ID needs to be mentioned.
      String to = "erdemo@sekizgen.com";

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

      // Used to debug SMTP issues
      session.setDebug(true);

      try {
          // Create a default MimeMessage object.
          MimeMessage message = new MimeMessage(session);

          // Set From: header field of the header.
          message.setFrom(new InternetAddress(from));

          // Set To: header field of the header.
          message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

          // Set Subject: header field
          message.setSubject("Yeni Bir Sipariş Var - xxxxxxx");

          // Now set the actual message
          message.setText("Değerli XXXX Ticaret,\n"
          		+ "\n"
          		+ "KolayOto'dan yeni bir siparişiniz var!\n"
          		+ "xxxxxx numaralı siparişinizle ilgili bilgileri aşağıda görebilirsiniz. \n"
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

}