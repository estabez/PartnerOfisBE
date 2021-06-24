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
		            	
		            	 
		            	 
		            	 if(uretimTarihiSonuc == null || uretimTarihiSonuc == "NULL") {
		            		
		     	            Statement statementSelectCustomFields = connection.createStatement();
		     	            ResultSet resultSetCustomFields = statementSelectCustomFields.executeQuery("SELECT * FROM public.flxpointexportteklif where sku=" + "'" + resultSet2.getString("sku") + "'"+ " and sourcename=" + "'" + tedarikci + "';");
		     	            while (resultSetCustomFields.next()) {
		     	            	
		     	            	 
		     	            	customFields = resultSetCustomFields.getString("parentcustomfields");
		     	            	
		     	            	
		     	            }
		     	        
		     	           
		     	            
		     	            if(customFields != null) {
		     	            	//System.out.println("customFields:" + customFields);
		     	            	
		     		            int customFieldsLength = customFields.length();
		     		            int virgulIndex = customFields.indexOf(",");

		     		            if(virgulIndex != -1) {
		     		            	
		     				           String ilkKisim=  customFields.substring(1, virgulIndex);
		     				           String ikinciKisim = customFields.substring(virgulIndex+1, customFieldsLength-1);    
		     				           
		     				           int ilkKisimNedir = ilkKisim.indexOf("Üretim Tarihi");
		        
		     				         
		     				            if(ilkKisimNedir != -1) {
		     				           //bu durumda ilk kısım üretim tarihidir 	
		     				            	uretimTarihiSonuc = ilkKisim.split(":")[1];
		     				            	hazirlanmaSuresiSonuc = ikinciKisim.split(":")[1];
		     				            	
		     				            }else {
		     				            	//Bu durumda ikinci kısım üretim tarihidir
		     				            	hazirlanmaSuresiSonuc = ilkKisim.split(":")[1];
		     				            	uretimTarihiSonuc = ikinciKisim.split(":")[1];
		     				            	
		     				            }
		     				            
		     				          //  System.out.println("hazirlikSuresi:  " + hazirlikSuresi);
		     				         //   System.out.println("uretimTarihi:  " + uretimTarihi);

		     		            }else {
		     		            	
		     		            	int buKisimNedir = customFields.indexOf("ÜretimTarihi");
		     				         

		     				            if(buKisimNedir != -1) {
		     				           //bu durumda bu kısım üretim tarihidir 	
		     				            	uretimTarihiSonuc = customFields.split(":")[1];
		     				            	hazirlanmaSuresiSonuc = "2";
		     	
		     				            }else {
		     				            	//Bu durumda ikinci kısım üretim tarihidir
		     				            	hazirlanmaSuresiSonuc = "2";
		     				            	uretimTarihiSonuc = "Son 1 yıl içerisinde üretilmiştir.";
		     				            	
		     				            }
		     				            
		     				         //   System.out.println("hazirlikSuresi:  " + hazirlikSuresi);
		     				         //   System.out.println("uretimTarihi:  " + uretimTarihi);   	
		     		            }
		     	            }else {
		     	            	// System.out.println("CustomFields boş");
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
				
			case "Avrupa Motor Lastik Tic. Ltd. Şti.":
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
}
