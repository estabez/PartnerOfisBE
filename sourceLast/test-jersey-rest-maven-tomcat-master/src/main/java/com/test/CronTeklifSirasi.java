package com.test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

public class CronTeklifSirasi {
	public static String sku=null;
	public static String company=null;
	
	public static void main(String[] args) throws IOException, ParseException, SQLException {
		
		/*
		String girilenSku = "L-1806100";
		String girilenCost = "2037.3566";
		String girilenQuantity = "20";
		String girilenCompany="kolayotodepo";
		*/
		BuyukGuncelleme();
	//	KucukGuncelleme(girilenSku, girilenCost, girilenQuantity, girilenCompany);
		
	}
	
	
	public static void KucukGuncelleme(String girilenSku, String girilenCost, String girilenQuantity, String girilenCompany) throws SQLException {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {
		     
		 String teklifsayisiSonuc=null;
         String uretimTarihiSonuc=null;
         String hazirlanmaSuresiSonuc=null;
         String tedarikci=null;
         JSONArray newJsonList = new JSONArray();
		 
         tedarikci = switchTerstenTedarikci(girilenCompany);
         
         
         
     	 Statement statementExportData = connection.createStatement();
     	statementExportData.executeUpdate("UPDATE public.flxpointexportteklif SET cost="+ "'" +girilenCost + "'"+", quantity=" + "'" +girilenQuantity +"'"+" WHERE sourcename="+"'"+ tedarikci + "'" + " and sku ="+"'"+ girilenSku +"'"+";");
         
    
         
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
         	 
         	 
         	 
         	 
         	 tedarikci = switchTedarikci(resultSet2.getString("sourcename"));
         	 System.out.println("tedarikci" + tedarikci);
         	 
         	 Statement statementUretimTarihi = connection.createStatement();
         	 ResultSet resultSetUretimTarihi = statementUretimTarihi.executeQuery("SELECT * FROM public.items where company=" +"'" +tedarikci +"'"+" And sku=" + "'" + resultSet2.getString("sku") + "';");
         	 while (resultSetUretimTarihi.next()) {
         		 
         		 uretimTarihiSonuc=resultSetUretimTarihi.getString("uretim_tarihi");
         		 hazirlanmaSuresiSonuc=resultSetUretimTarihi.getString("hazirlik_suresi");
         	 }
         	 
         	 
         	 
         	System.out.println("teklifsirasi" + resultSet2.getString("rnum"));
         	System.out.println("teklifsayisiSonuc" + teklifsayisiSonuc);
         	System.out.println("cost" + resultSet2.getString("cost"));
         	 Statement statement1 = connection.createStatement();
         	statement1.executeUpdate("UPDATE public.teklifsirasiteklifsayisi SET teklifsirasi="+ "'" +resultSet2.getString("rnum") + "'"+", teklifsayisi=" + "'" +teklifsayisiSonuc +"'"+ ", cost="+ "'" +resultSet2.getString("cost")  +"'"+ ", quantity="+"'"+resultSet2.getString("quantity")+"'"+ ", hazirliksuresi="+"'"+hazirlanmaSuresiSonuc +"'"+ ", uretimtarihi="+"'"+ uretimTarihiSonuc+"'"+       " WHERE company="+"'"+ girilenCompany + "'" + " and sku ="+"'"+ girilenSku +"'"+";");
	         
         	 /*
         	 String SQL = "INSERT INTO public.teklifsirasiteklifsayisi("
         	 		+ "	company, sku, teklifsirasi, teklifsayisi, hazirliksuresi, uretimtarihi, cost, quantity)"
         	 		+ "	VALUES (?, ?, ?, ?, ?, ?, ?,?);";
         	 
         	 PreparedStatement statement1 = connection.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
         	 
         	 statement1.setString(1,tedarikci);
         	 statement1.setString(2,resultSet2.getString("sku"));
         	 statement1.setString(3,resultSet2.getString("rnum"));
         	 statement1.setString(4,teklifsayisiSonuc);
         	 statement1.setString(5,hazirlanmaSuresiSonuc);
         	 statement1.setString(6,uretimTarihiSonuc);
         	 statement1.setString(7,resultSet2.getString("cost"));
         	 
         	 statement1.setString(8,resultSet2.getString("quantity"));
         	 
         	 statement1.executeUpdate();
         	 */
         	 
         	 Statement statementUpdateItems = connection.createStatement();
         	 statementUpdateItems.executeUpdate("UPDATE public.items SET teklif_adet="+ "'" +teklifsayisiSonuc + "'"+", teklif_sirasi=" + "'" +resultSet2.getString("rnum") +"'"+" WHERE company="+"'"+ girilenCompany + "'" + " and sku ="+"'"+ resultSet2.getString("sku") +"'"+";");
         	System.out.println("----------"); 
         }
		}
	}
	
	
	public static void BuyukGuncelleme() {

		ArrayList <String> skuList = new ArrayList<String>();
		String customFields = null;
		
		//try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kolayoto", "postgres", "postgres")) {
		 try (Connection connection = DriverManager.getConnection("jdbc:postgresql://20.52.6.77:5432/po", "pouser", "pouser")) { 	 
			
			
			String SQLDELETE = "DELETE FROM public.teklifsirasiteklifsayisi";
			PreparedStatement statementDelete = connection.prepareStatement(SQLDELETE,Statement.RETURN_GENERATED_KEYS);
			statementDelete.executeUpdate();
			
			
	            Statement statementSelectSKU = connection.createStatement();
	            ResultSet resultSetSKU = statementSelectSKU.executeQuery("SELECT sku FROM public.flxpointexportteklif group by sku");
	            while (resultSetSKU.next()) {
	            	
	            	sku = resultSetSKU.getString("sku"); 		
	            	skuList.add(sku);
	            }
	            
	            System.out.println("skuList length:" + skuList.size());
	      
	            
	           
			
			
			
	            
	            
	            String teklifsayisiSonuc=null;
	            String uretimTarihiSonuc=null;
	            String hazirlanmaSuresiSonuc=null;
	            String tedarikci=null;
	            
			 JSONArray newJsonList = new JSONArray();
			 
	            Statement statement2 = connection.createStatement();
	            for(int a=0; a<skuList.size(); a++) {
	            	ResultSet resultSet2 = statement2.executeQuery("select *,row_number() OVER () as rnum FROM(	SELECT sku,sourcename,quantity,cost FROM \n"
		            		+ "(SELECT sku,sourcename,quantity,cost FROM public.flxpointexportteklif where sku ="+ "'"+ skuList.get(a) + "'"+   "and quantity > '0' ORDER BY SUBSTRING(cost FROM '([0-9]+)')::BIGINT ASC, cost) AS query1\n"
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
		            	
		            	 
		            	 
		            	 
		            	 tedarikci = switchTedarikci(resultSet2.getString("sourcename"));
		            	// System.out.println("tedarikci: " + tedarikci);
		            	// System.out.println("sku: " +  resultSet2.getString("sku"));
		            	
		            	 
		            	 
		            	 Statement statementUretimTarihi = connection.createStatement();
		            	 ResultSet resultSetUretimTarihi = statementUretimTarihi.executeQuery("SELECT * FROM public.items where company=" +"'" +tedarikci +"'"+" And sku=" + "'" + resultSet2.getString("sku") + "';");
		            	 while (resultSetUretimTarihi.next()) {
		            		 
		            		 uretimTarihiSonuc=resultSetUretimTarihi.getString("uretim_tarihi");
		            		 hazirlanmaSuresiSonuc=resultSetUretimTarihi.getString("hazirlik_suresi");
		            	 }
		            	 
		            	 
		            	 
		            	 if(uretimTarihiSonuc ==null) {
		            		 
		            		
		         			
		     	            Statement statementSelectCustomFields = connection.createStatement();
		     	            ResultSet resultSetCustomFields = statementSelectCustomFields.executeQuery("SELECT * FROM public.flxpointexportteklif where sku=" + "'" + resultSet2.getString("sku") + "'"+ " and sourcename=" + "'" + tedarikci + "';");
		     	            while (resultSetSKU.next()) {
		     	            
		     	            	
		     	            	customFields = resultSetSKU.getString("Parent Custom Fields");
		     	            	
		     	            	
		     	            }
		     	            
		     	           
		     	            
		     	            if(customFields != null) {
		     	            	//System.out.println("customFields:" + customFields);
		     	            	
		     		            int customFieldsLength = customFields.length();
		     		            int virgulIndex = customFields.indexOf(",");

		     		            if(virgulIndex != -1) {
		     		            	
		     				           String ilkKisim=  customFields.substring(1, virgulIndex);
		     				           String ikinciKisim = customFields.substring(virgulIndex+1, customFieldsLength-1);    
		     				           
		     				           int ilkKisimNedir = ilkKisim.indexOf("??retimTarihi");
		        
		     				         
		     				            if(ilkKisimNedir != -1) {
		     				           //bu durumda ilk k??s??m ??retim tarihidir 	
		     				            	uretimTarihiSonuc = ilkKisim.split(":")[1];
		     				            	hazirlanmaSuresiSonuc = ikinciKisim.split(":")[1];
		     				            	
		     				            }else {
		     				            	//Bu durumda ikinci k??s??m ??retim tarihidir
		     				            	hazirlanmaSuresiSonuc = ilkKisim.split(":")[1];
		     				            	uretimTarihiSonuc = ikinciKisim.split(":")[1];
		     				            	
		     				            }
		     				            
		     				          //  System.out.println("hazirlikSuresi:  " + hazirlikSuresi);
		     				         //   System.out.println("uretimTarihi:  " + uretimTarihi);

		     		            }else {
		     		            	
		     		            	int buKisimNedir = customFields.indexOf("??retimTarihi");
		     				         

		     				            if(buKisimNedir != -1) {
		     				           //bu durumda bu k??s??m ??retim tarihidir 	
		     				            	uretimTarihiSonuc = customFields.split(":")[1];
		     				            	hazirlanmaSuresiSonuc = "2";
		     	
		     				            }else {
		     				            	//Bu durumda ikinci k??s??m ??retim tarihidir
		     				            	hazirlanmaSuresiSonuc = "2";
		     				            	uretimTarihiSonuc = "Son 1 y??l i??erisinde ??retilmi??tir";
		     				            	
		     				            }
		     				            
		     				         //   System.out.println("hazirlikSuresi:  " + hazirlikSuresi);
		     				         //   System.out.println("uretimTarihi:  " + uretimTarihi);   	
		     		            }
		     	            }else {
		     	            	// System.out.println("CustomFields bo??");
		     	            }
		            		 
		            	 }
		            	 
		            	 
		            	 
		            	 
		            	 
		            	 
		            	 
		            	 
		            	 String SQL = "INSERT INTO public.teklifsirasiteklifsayisi("
		            	 		+ "	company, sku, teklifsirasi, teklifsayisi, hazirliksuresi, uretimtarihi, cost, quantity)"
		            	 		+ "	VALUES (?, ?, ?, ?, ?, ?, ?,?);";
		            	 
		            	 PreparedStatement statement1 = connection.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
		            	 
		            	 statement1.setString(1,tedarikci);
		            	 statement1.setString(2,resultSet2.getString("sku"));
		            	 statement1.setString(3,resultSet2.getString("rnum"));
		            	 statement1.setString(4,teklifsayisiSonuc);
		            	 statement1.setString(5,hazirlanmaSuresiSonuc);
		            	 statement1.setString(6,uretimTarihiSonuc);
		            	 statement1.setString(7,resultSet2.getString("cost"));
		            	 
		            	 statement1.setString(8,resultSet2.getString("quantity"));
		            	 
		            	 statement1.executeUpdate();
		            	 
		            	 
		            	 Statement statementUpdateItems = connection.createStatement();
		            	 statementUpdateItems.executeUpdate("UPDATE public.items SET teklif_adet="+ "'" +teklifsayisiSonuc + "'"+", teklif_sirasi=" + "'" +resultSet2.getString("rnum") +"'"+" WHERE company="+"'"+ tedarikci + "'" + " and sku ="+"'"+ resultSet2.getString("sku") +"'"+";");
		 	            
		            }
		            
		          System.out.println("------------");
	            }
	            
	           
	            
		}catch (Exception e) {
			 System.out.println("Error message:" + e);
		 }
		
	           
	}
	
	
	public static String switchTedarikci(String flxPointTedarikci) {
		switch (flxPointTedarikci) {
			case "G??nmarlas G??ney Marmara Lastik Oto.San.Tic.Ltd.??ti.":
				return "gunmarlas";
				
			case "Tekkeliler Otomotiv San. ve Tic.":
				return "tekkeliler";
				
			case "Avrupa Motor Lastik Tic. Ltd. ??ti.":
				return "Avrupa Motor Lastik Tic. Ltd. ??ti.";
				
			case "EKSPRES LAST??K SANAY?? VE T??CARET A??.":
				return "ekspres";
				
			case "Hangar Motorlu Ara??lar A.??.":
				return "hangar";
				
			case "??zman Otomotiv":
				return "ozman";
				
			case "Ko??ak Ticaret Oto.Las.Nak.??th.??hr. Ve San.Ltd.??ti.":
				return "kocak";
				
			case "Suyolcu Otomotiv":
				return "suyolcu";
				
			case "Tatko Lastik":
				return "tatko";
				
			case "Hita?? Otomotiv":
				return "hitas";
				
			case "Go Motorsporlar??":
				return "gomotor";
				
			case "Mert Otomotiv":
				return "mert";
				
			case "??ak??rlar Oto Lastik":
				return "cakirlar";
				
			case "Teklas Oto Lastik":
				return "teklas";
				
			case "Yusuf Ziya Keskin":
				return "yuke";
				
			case "Ferhat De??er A??.":
				return "Ferhat De??er A??.";
				
			case "Mollao??lu Otomotiv":
				return "Mollao??lu Otomotiv";
				
			case "Y??lkarlas Otomotiv":
				return "Y??lkarlas Otomotiv";
				
			case "USPA Lastik":
				return "USPA Lastik";
				
			case "Kormetal Pazarlama":
				return "Kormetal Pazarlama";
				
			case "Do??an Ticaret":
				return "dogan";
				
			case "PUMALAS DI?? T??CARET OTOMOT??V VE LAST??K SANAY?? PAZARLAMA L??M??TED ????RKET??":
				return "pumalas";
				
			case "Pirelli Fabrika Stoklar??":
				return "Pirelli Fabrika Stoklar??";
				
			case "KolayOtoLastik Depo Stoklari":
				return "kolayotodepo";
				
			case "TUN??EL JANT OTO LAST??K OTOMOT??V SAN T??C LTD ??T??":
				return "tuncel";
				
			case "Akta?? Oto Lastik Ticaret A.??.":
				return "aktas";
				
			case "Ademo??lu Oto Lastik":
				return "ademoglu";
				
			default:
				return flxPointTedarikci;
				
			}
	}
	


	public static String switchTerstenTedarikci(String flxPointTedarikci) {
		switch (flxPointTedarikci) {
			case "gunmarlas":
				return "G??nmarlas G??ney Marmara Lastik Oto.San.Tic.Ltd.??ti.";
				
			case "tekkeliler":
				return "Tekkeliler Otomotiv San. ve Tic.";
				
			case "Avrupa Motor Lastik Tic. Ltd. ??ti.":
				return "Avrupa Motor Lastik Tic. Ltd. ??ti.";
				
			case "ekspres":
				return "EKSPRES LAST??K SANAY?? VE T??CARET A??.";
				
			case "hangar":
				return "Hangar Motorlu Ara??lar A.??.";
				
			case "ozman":
				return "??zman Otomotiv";
				
			case "kocak":
				return "Ko??ak Ticaret Oto.Las.Nak.??th.??hr. Ve San.Ltd.??ti.";
				
			case "suyolcu":
				return "Suyolcu Otomotiv";
				
			case "tatko":
				return "Tatko Lastik";
				
			case "hitas":
				return "Hita?? Otomotiv";
				
			case "gomotor":
				return "Go Motorsporlar??";
				
			case "mert":
				return "Mert Otomotiv";
				
			case "cakirlar":
				return "??ak??rlar Oto Lastik";
				
			case "teklas":
				return "Teklas Oto Lastik";
				
			case "yuke":
				return "Yusuf Ziya Keskin";
				
			case "Ferhat De??er A??.":
				return "Ferhat De??er A??.";
				
			case "Mollao??lu Otomotiv":
				return "Mollao??lu Otomotiv";
				
			case "Y??lkarlas Otomotiv":
				return "Y??lkarlas Otomotiv";
				
			case "USPA Lastik":
				return "USPA Lastik";
				
			case "Kormetal Pazarlama":
				return "Kormetal Pazarlama";
				
			case "dogan":
				return "Do??an Ticaret";
				
			case "pumalas":
				return "PUMALAS DI?? T??CARET OTOMOT??V VE LAST??K SANAY?? PAZARLAMA L??M??TED ????RKET??";
				
			case "Pirelli Fabrika Stoklar??":
				return "Pirelli Fabrika Stoklar??";
				
			case "kolayotodepo":
				return "KolayOtoLastik Depo Stoklari";
				
			case "tuncel":
				return "TUN??EL JANT OTO LAST??K OTOMOT??V SAN T??C LTD ??T??";
				
			case "aktas":
				return "Akta?? Oto Lastik Ticaret A.??.";
				
			case "ademoglu":
				return "Ademo??lu Oto Lastik";
				
			default:
				return flxPointTedarikci;
				
			}
	}
}
