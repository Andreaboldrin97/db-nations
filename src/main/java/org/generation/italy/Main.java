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
		
	
		// query by country name
		queryByName(URL, USER,  PASSWORD, inputName) ;
		
		System.out.println("Inserire l'id della nazione per maggiori info :");
		int inputId = sc.nextInt();
		queryByID(URL, USER, PASSWORD, inputId);
		//chiudo lo scanner
		sc.close();
				
	}
	// QUERY BY NAME
	public static void queryByName(String URL, String USER, String PASSWORD, String inputName) {
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
			
			
			final String sql = "SELECT countries.country_id,continents.name, regions.name, countries.name\n"
								+ "FROM continents\n"
								+ "	JOIN regions\n"
								+ "		ON regions.continent_id = continents.continent_id\n"
								+ "	JOIN countries\n"
								+ "		ON countries.country_id = regions.region_id\n"
								+ "WHERE countries.name LIKE ?"
								+ "ORDER BY countries.name";
					
			try (PreparedStatement ps = connection.prepareStatement (sql)) {
				ps.setString(1, "%"+inputName+"%");
				
				try (ResultSet res = ps.executeQuery ( )){
					while (res.next()) {
						
						int idContinent = res.getInt(1);
						String continenetName = res.getString(2);
						String regionsName = res.getString(3);
						String countriesName = res.getString(4);
						
						System. out .println ( idContinent + "-" + continenetName + "-" + regionsName + "-" + countriesName );
					}
				}
			}
				
		} catch (Exception e) {
			System.err.println("ERROR: '" + e .getMessage ()) ;
		}
	}
	//QUERY BY ID
	public static void queryByID(String URL, String USER, String PASSWORD, int inputId ) {
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
			
			
			final String sql = "SELECT countries.country_id,continents.name AS continent, regions.name AS region, countries.name AS country, languages.language, country_stats.year, country_stats.population, country_stats.gdp\n"
								+ "FROM continents\n"
								+ "	JOIN regions\n"
								+ "		ON regions.continent_id = continents.continent_id\n"
								+ "	JOIN countries\n"
								+ "		ON countries.country_id = regions.region_id\n"
								+ "	JOIN country_languages\n"
								+ "		ON country_languages.country_id = countries.country_id\n"
								+ "	JOIN languages\n"
								+ "		ON languages.language_id = country_languages.language_id\n"
								+ "	JOIN country_stats\n"
								+ "		ON country_stats.country_id = countries.country_id\n"
								+ "WHERE countries.country_id = ? \n"
								+ "ORDER BY country_stats.year DESC\n"
								+ "LIMIT 1";
								
			try (PreparedStatement ps = connection.prepareStatement (sql)) {
				ps.setInt(1, inputId );
				
				try (ResultSet res = ps.executeQuery ( )){
					while (res.next()) {
						
						int idContinent = res.getInt(1);
						String continenetName = res.getString(2);
						String regionsName = res.getString(3);
						String countriesName = res.getString(4);
						String languages = res.getString(5);
						int country_stats = res.getInt(6);
						int population = res.getInt(7);
						double gdp = res.getDouble(8);
						
						System. out .println ( idContinent + "-" + continenetName + "-" 
												+ regionsName + "-" + countriesName + "-"
												+ languages + "-" + country_stats + "-"
												+ population + "-" + gdp);
					}
				}
			}
				
		} catch (Exception e) {
			System.err.println("ERROR: '" + e .getMessage ()) ;
		}
	}
}
