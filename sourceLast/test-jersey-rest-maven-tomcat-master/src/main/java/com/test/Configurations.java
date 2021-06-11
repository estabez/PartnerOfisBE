package com.test;

public class Configurations {
	
	
	 
	    private static String tokenDogan = "Bearer uxetgjZDwg54HBZ4jgKn8qkBaWrRxgiwsfhKGAQCMrc5ChzMjiTGzzentSeMByjKwmhIKDcAQHZ4lYq0q0QvXiLPdWfqbJbD1BlC";
	    private static String tokenTekkeliler = "Bearer iOooq6fJnE0cpHeJBkt6rMmwejKV05feI2W5XLpFt7msARfr8b9EiyHhmnosgohjRA3e7gcX0s1MegfI9ZNvQxrI674x3oDikkUA";
	    private static Configurations instance = new Configurations(); // Eagerly Loading of singleton instance

	    private Configurations(){
	        // private to prevent anyone else from instantiating
	    }

	    public static Configurations getInstance() {
	        return instance;
	    }

	    public static String getDogan() {
	        return tokenDogan;
	    }

	    public static String getTekkeliler() {
	        return tokenTekkeliler;
	    }
}
