package com.raviv.coupons.tests;

import java.sql.*;

public class SqlTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		 Connection con;
         String conUrl = "jdbc:sqlserver://localhost; databaseName=Coupons; user=sa; password=sa;";

         con = DriverManager.getConnection(conUrl);
         
         if ( con != null ) {
        	 
        	 Statement st = con.createStatement();
        	 ResultSet rs = st.executeQuery("SELECT * FROM COUPON_TYPE");
      
        	 System.out.println("COUPON_TYPE");
        	 System.out.println("==================================");
        	 while (rs.next()) 
        	 {
        		 System.out.println(	  rs.getString(	"COUPON_TYPE_ID" ) 	+"\t" +
        				 				  rs.getString( "SYS_CREATION_DATE" ).substring(0, 19) 		+"\t" +
        				 				  rs.getString( "NAME" )
        				 				);
        	 }        	 
        	 rs.close();

        	 
        	 rs = st.executeQuery("SELECT * FROM COMPANY");
        	 System.out.println("\nCOMPANY");
        	 System.out.println("==================================");
        	 while (rs.next()) 
        	 {
        		 System.out.println(	  rs.getString(	"COMPANY_ID" ) 	+"\t" +
        				 				  rs.getString( "SYS_CREATION_DATE" ).substring(0, 19) 		+"\t" +
        				 				  rs.getString(	"USER_ID" ) 	+"\t" +
        				 				  rs.getString( "COMPANY_NAME" )
        				 				);
        	 }        	 
        	 rs.close();


        	 rs = st.executeQuery("SELECT * FROM CUSTOMER");        	 
        	 System.out.println("\nCUSTOMER");
        	 System.out.println("==================================");
        	 while (rs.next()) 
        	 {
        		 System.out.println(	  rs.getString(	"CUSTOMER_ID" ) 	+"\t" +
        				 				  rs.getString( "SYS_CREATION_DATE" ).substring(0, 19) 		+"\t" +
        				 				  rs.getString( "USER_ID" ) +"\t" +
        				 				  rs.getString( "CUSTOMER_NAME" ) +"\t" +
        				 				  rs.getString( "CUSTOMER_EMAIL" ) +"\t" 

        				 );
        	 }        	 
        	 rs.close();


        	 rs = st.executeQuery("SELECT * FROM USER_PROFILE");        	 
        	 System.out.println("\nUSER_PROFILE");
        	 System.out.println("==================================");
        	 while (rs.next()) 
        	 {
        		 System.out.println(	  rs.getString(	"USER_PROFILE_ID" ) 	+"\t" +
        				 				  rs.getString( "SYS_CREATION_DATE" ).substring(0, 19) 		+"\t" +
        				 				  rs.getString( "USER_PROFILE_DESC" ) +"\t" 
        				 );
        	 }        	 
        	 rs.close();

        	 
        	 
             con.close(); 
         }

	}

}