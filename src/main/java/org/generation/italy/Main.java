package org.generation.italy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {
	public static void main(String[] args) {
		
		final String URL = "jdbc:mysql://localhost:8889/db-nations";
		final String USER = "root";
		final String PASSWORD = "root";
		
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
			
		
			final String sql = "SELECT * FROM continents";
					
			try (PreparedStatement ps = connection.prepareStatement (sql)) {
				try (ResultSet res = ps.executeQuery ( )){
					while (res.next()) {
						
						int idContinent = res.getInt(1);
						String continenetName = res.getString(2);
						
						System. out .println ( idContinent + "-" + continenetName);
					}
				}
			}
				
		} catch (Exception e) {
			System.err.println("ERROR: ' + e .getMessage ()") ;
		}
	}
}
