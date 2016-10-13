package com.raviv.coupons.dao.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ConnectionPoolManager {

	//private static JdbcConnectionPoolManager jdbcConnectionPoolManager = new JdbcConnectionPoolManager();
	private static List<Connection> connectionsPool = new LinkedList <Connection>();
	
	public static void main(String[] args) {
	}
	
	public static  synchronized Connection  getConnection() throws SQLException{
		Connection con;        
		if ( connectionsPool.isEmpty() )
		{
			//  connectionsPool is empty, lets create a new connection ...
			String conUrl = "jdbc:sqlserver://localhost; databaseName=Coupons; user=sa; password=sa;";
	 		con = DriverManager.getConnection(conUrl);
	 		return con;
		}
		else
		{
			//  connectionsPool is not empty, lets take one and use it ...
			con = connectionsPool.get(0);
			connectionsPool.remove(0);
			return con;
		}
	} // end getConnection

	public static  synchronized void  returnConnection(Connection con) {		
		connectionsPool.add(con);
	} // end returnConnection


	public static  synchronized void  closeAllConnections() throws SQLException {	
		
		for (Connection con : connectionsPool)
		{
			if (con != null ) 
			{
				if ( con.isValid(0) )
				{
					con.close();
				}
			}
		}
		
		connectionsPool.clear();
		
	} // end closeAllConnections


}
