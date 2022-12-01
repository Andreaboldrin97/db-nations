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
			
			
			final String sqlLang = " SELECT DISTINCT language "
									 + " FROM countries "
									 + "	JOIN country_languages "
									 + "		ON countries.country_id = country_languages.country_id "
									 + "	JOIN languages "
									 + "		ON country_languages.language_id = languages.language_id "
									 + " WHERE countries.country_id = ? ";
								
			try (PreparedStatement ps = connection.prepareStatement (sqlLang)) {
				ps.setInt(1, inputId );
				
				try (ResultSet rs = ps.executeQuery()) {
					
					System.out.print("Languages: ");
					while(rs.next()) {
						
						final String lang = rs.getString(1);
						
						System.out.print(lang + (!rs.isLast() ? ", " : ""));
					}
				}
			}
			
			System.out.println("");
			
			final String sqlStat = " SELECT country_stats.* "
								 + " FROM countries "
								 + "	JOIN country_stats "
								 + "		ON countries.country_id = country_stats.country_id "
								 + " WHERE countries.country_id = ? "
								 + " ORDER BY year DESC "
								 + " LIMIT 1 ";
				try (PreparedStatement ps = connection.prepareStatement(sqlStat)) {
				
				ps.setInt(1, inputId);
				
				try (ResultSet rs = ps.executeQuery()) {
				
					System.out.println("Most recent stats: ");
					if(rs.next()) {
						
						final String year = rs.getString(2);
						final String pop = rs.getString(3);
						final String gdp = rs.getString(4);
						
						System.out.println("Year: " + year);
						System.out.println("Population: " + pop);
						System.out.println("GDP: " + gdp);
					}
				}	
			}
		} catch (Exception e) {
			System.err.println("ERROR: '" + e .getMessage ()) ;
		}
	}
}
