package org.generation.italy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		
		final String URL = "jdbc:mysql://localhost:8889/db-nations";
		final String USER = "root";
		final String PASSWORD = "root";
		//apro lo scanner per chiedere un continenete all'user
		Scanner sc = new Scanner(System.in);
		System.out.println("Inserire il nome della nazione da cercare:");
		String inputName = sc.nextLine();
		//chiudo lo scanner
		sc.close();
		
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
			
		
			final String sql = "SELECT countries.country_id,continents.name, regions.name, countries.name\n"
								+ "FROM continents\n"
								+ "	JOIN regions\n"
								+ "		ON regions.continent_id = continents.continent_id\n"
								+ "	JOIN countries\n"
								+ "		ON countries.country_id = regions.region_id\n"
								+ "WHERE countries.name LIKE ?";
					
			try (PreparedStatement ps = connection.prepareStatement (sql)) {
				ps.setString(1, inputName);
				
				try (ResultSet res = ps.executeQuery ( )){
					while (res.next()) {
						
						int idContinent = res.getInt(1);
						String continenetName = res.getString(2);
						String regionsName = res.getString(3);
						String countriesName = res.getString(4);
						
						System. out .println ( idContinent + "-" + continenetName + regionsName + "-" + countriesName );
					}
				}
			}
				
		} catch (Exception e) {
			System.err.println("ERROR: '" + e .getMessage ()) ;
		}
	}
}
