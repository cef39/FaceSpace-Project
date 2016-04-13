import java.sql.*;
import oracle.jdbc.*;
import java.util.Random;


public class populateDB{
	private static Connection connection;
	private static Statement statement;
	private static PreparedStatement prepStatement;
	private static ResultSet resultSet;
	private static String query;
	
	
	public static void main(String args[]) throws SQLException
	{
		
		String username, password;
		username = "cef39";
		password = "3910744";
							
		
		
		try{
			System.out.println("Registering DB..");
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			
			System.out.println("Set url..");
			String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
			
			System.out.println("Connect to DB..");
			connection = DriverManager.getConnection(url, username, password);
			
			populateProfiles();
			connection = DriverManager.getConnection(url, username, password);
			populateFriends();
			connection = DriverManager.getConnection(url, username, password);
			populateGroups();
			connection = DriverManager.getConnection(url, username, password);
			populateMessages();
			
		}
		catch(Exception Ex){
			System.out.println("Error connecting to database. Machine Error: " +
						Ex.toString());
		}
		finally
		{
			connection.close();
		}
		
	}
	
	public static void populateProfiles(){
		
		String [] names = {
			"Katherine Sharp", "Jacob Kerr", "Deirdre Powell",
			"Andrea Hemmings", "Simon Thomson", "Paul Wright", "Lillian Robertson", "Lauren Fisher",
			"Samantha Clarkson", "Deirdre Newman", "Samantha MacLeod", "Kevin North",
			"Alexandra Hardacre", "Bernadette Quinn", "Dan McDonald", "Gordon Reid",
			"Ella Greene", "Alexandra Welch", "Heather Cameron", "Abigail Thomson",
			"Faith Sanderson", "Luke Skinner", "Jan McGrath", "Paul Mitchell",
			"Melanie Smith", "Lucas Ogden", "Luke Coleman", "Karen Dickens",
			"Tim Sanderson", "Alexandra Manning", "Jan Dowd", "Peter May",
			"Harry Lee", "Colin Sanderson", "Felicity Duncan", "Jason Watson",
			"John Mills", "Carolyn Oliver", "Jason McLean", "James Lambert",
			"Bella Kerr", "Ryan Vance", "Zoe Forsyth", "Victoria Graham",
			"Melanie Ross", "Mary Ferguson", "Audrey Watson", "Edward North",
			"James Black", "Paul Ince", "Carl Gill", "Caroline Murray",
			"Matt Sharp", "Tim Vaughan", "Steven Howard", "Owen Lee",
			"Angela Gray", "Bernadette Abraham", "Ava Parsons", "Carolyn Butler",
			"Lisa Langdon", "Carolyn Mackay", "Megan Anderson", "Megan Sharp",
			"Katherine Burgess", "Julian Johnston", "David Smith", "Brandon Edmunds",
			"Ella Hamilton", "Maria Jackson", "Bernadette Simpson", "Theresa McLean",
			"Kevin Bond", "Chloe Peake", "Keith Morrison",
			"Tracey Chapman", "Jacob Grant", "Amanda Davies", "Leah Tucker", "Owen Buckland",
			"Rose Springer", "Richard Sanderson", "Colin Quinn", "Justin Hodges",
			"Connor Churchill", "Dylan Blake", "Victoria Bell", "Jake Sutherland",
			"Wendy Parr", "Piers Black", "Alan Lawrence", "Stewart Brown",
			"Abigail Wilkins", "Robert Jones", "Molly Hardacre", "Nicola Ross",
			"Natalie Short", "Wendy Quinn", "Virginia Wright", "Victoria Sutherland"
		};
		
		try{
			String year = "2000";
			int y;
			for (int i = 0; i < 100; i++){
				String name = names[i];
				String [] parts = name.split(" ");
				String fname = parts[0];
				String lname = parts[1];
					
				String birthday = year + "-02-24";
				y = Integer.parseInt(year);
				y++;
				year = Integer.toString(y);
					
				String email = fname + lname + Integer.toString(i) + "@yahoo.com";
					
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date birth = new java.sql.Date (df.parse(birthday).getTime());
					
				java.util.Date date = new java.util.Date();
				java.sql.Timestamp login = new java.sql.Timestamp(date.getTime());
					
				query = "insert into Profiles values (?, ?, ?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, i);
				prepStatement.setString(2, fname);
				prepStatement.setString(3, lname);
				prepStatement.setString(4, email);
				prepStatement.setDate(5, birth);
				prepStatement.setTimestamp(6, login);
					
				prepStatement.executeUpdate();
			}
				
			connection.commit();	
			
		}
		catch(Exception Ex){
			System.out.println("Error connecting to database. Machine Error: " +
						Ex.toString());
		}
		finally
		{
			try{
				connection.close();
			}
			catch(SQLException Ex){
				System.out.println("Error connecting to database. Machine Error: " +
						Ex.toString());
			}
		}
	}
	
	public static void populateFriends(){
		try{
			int userID = 0;
			for (int i = 0; i <= 200; i++){
				Random gen = new Random();
				int friendID = gen.nextInt(100);
					
				if (friendID == userID){
					while (friendID == userID){
						friendID = gen.nextInt(100);
					}
				}
					
				int friendRequest = gen.nextInt(2);
					
				int month = gen.nextInt(12) + 1;
				int day = gen.nextInt(27) + 1;
					
				String m = Integer.toString(month);
				String d = Integer.toString(day);
				String ye = "2016-";
					
				String date = ye + m + "-" + d;
					
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date establishedDate = new java.sql.Date (df.parse(date).getTime());
					
				query = "insert into Friends values (?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, userID);
				prepStatement.setInt(2, friendID);
				prepStatement.setInt(3, friendRequest);
				prepStatement.setDate(4, establishedDate);
					
				prepStatement.executeUpdate();
					
				query = "insert into Friends values (?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, friendID);
				prepStatement.setInt(2, userID);
				prepStatement.setInt(3, friendRequest);
				prepStatement.setDate(4, establishedDate);
					
				prepStatement.executeUpdate();
				connection.commit();
					
				userID++;
					
				if (userID == 100){
					userID = 0;
				}
					
			}
			connection.commit();
		}
		catch(Exception Ex){
			System.out.println("Error connecting to database. Machine Error: " +
						Ex.toString());
		}
		finally
		{
			try{
				connection.close();
			}
			catch(SQLException Ex){
				System.out.println("Error connecting to database. Machine Error: " +
						Ex.toString());
			}
		}
	}
	
	public static void populateGroups(){
		try{
			int user_ID_1 = 0;
			int user_ID_2 = 0;
			for (int i = 0; i < 3; i++){
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
					
				if (i == 1 && user_ID == user_ID_1){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
				
				if (i == 2 && user_ID == user_ID_2){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
				
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
					
				query = "insert into Groups values (?, ?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, 0);
				prepStatement.setInt(2, user_ID);
				prepStatement.setString(3, "Group numero uno");
				prepStatement.setString(4, "We are number one!!!");
				prepStatement.setInt(5, 5);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			for (int i = 0; i < 3; i++){
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				if (i == 1 && user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
				}
				
					
				if (i == 2 && user_ID == user_ID_2){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
					
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
					
				query = "insert into Groups values (?, ?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, 1);
				prepStatement.setInt(2, user_ID);
				prepStatement.setString(3, "Dinos Rules");
				prepStatement.setString(4, "We are trying to conquer the world by bringing back dinosaurs.");
				prepStatement.setInt(5, 5);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			for (int i = 0; i < 3; i++){
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				if (i == 1 && user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
				}
				
					
				if (i == 2 && user_ID == user_ID_2){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
					
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
					
				query = "insert into Groups values (?, ?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, 2);
				prepStatement.setInt(2, user_ID);
				prepStatement.setString(3, "Cell for Emperor");
				prepStatement.setString(4, "Remember Cell from DBZ? He's our savior.");
				prepStatement.setInt(5, 5);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			for (int i = 0; i < 3; i++){
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				if (i == 1 && user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
				}
				
					
				if (i == 2 && user_ID == user_ID_2){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
					
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
					
				query = "insert into Groups values (?, ?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, 3);
				prepStatement.setInt(2, user_ID);
				prepStatement.setString(3, "Egg Club");
				prepStatement.setString(4, "We really like eggs.");
				prepStatement.setInt(5, 5);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			for (int i = 0; i < 3; i++){
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				if (i == 1 && user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
				}
				
					
				if (i == 2 && user_ID == user_ID_2){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
					
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
					
				query = "insert into Groups values (?, ?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, 4);
				prepStatement.setInt(2, user_ID);
				prepStatement.setString(3, "Book Club");
				prepStatement.setString(4, "We meet every Wednesday at 7PM at Schenley Cafe.");
				prepStatement.setInt(5, 5);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			for (int i = 0; i < 3; i++){
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				if (i == 1 && user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
				}
				
					
				if (i == 2 && user_ID == user_ID_2){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
					
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
					
				query = "insert into Groups values (?, ?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, 5);
				prepStatement.setInt(2, user_ID);
				prepStatement.setString(3, "Squadddddd");
				prepStatement.setString(4, "K-town represent.!!");
				prepStatement.setInt(5, 5);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			for (int i = 0; i < 3; i++){
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				if (i == 1 && user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
				}
				
					
				if (i == 2 && user_ID == user_ID_2){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
					
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
					
				query = "insert into Groups values (?, ?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, 6);
				prepStatement.setInt(2, user_ID);
				prepStatement.setString(3, "Vamps");
				prepStatement.setString(4, "Eat, drink, breathe like Vampires.");
				prepStatement.setInt(5, 5);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			for (int i = 0; i < 3; i++){
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				if (i == 1 && user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
				}
				
					
				if (i == 2 && user_ID == user_ID_2){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
					
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
					
				query = "insert into Groups values (?, ?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, 7);
				prepStatement.setInt(2, user_ID);
				prepStatement.setString(3, "George Bush");
				prepStatement.setString(4, "Yeh.");
				prepStatement.setInt(5, 5);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			for (int i = 0; i < 3; i++){
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				if (i == 1 && user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
				}
				
					
				if (i == 2 && user_ID == user_ID_2){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
					
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
					
				query = "insert into Groups values (?, ?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, 8);
				prepStatement.setInt(2, user_ID);
				prepStatement.setString(3, "FaceSpace Project Group");
				prepStatement.setString(4, "Group to complete this project.");
				prepStatement.setInt(5, 5);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			for (int i = 0; i < 3; i++){
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				if (i == 1 && user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
				}
				
					
				if (i == 2 && user_ID == user_ID_2){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
					
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
					
				query = "insert into Groups values (?, ?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, 9);
				prepStatement.setInt(2, user_ID);
				prepStatement.setString(3, "D-Brown");
				prepStatement.setString(4, "D-money appreciation group.");
				prepStatement.setInt(5, 5);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
		}
		catch(Exception Ex){
			System.out.println("Error connecting to database. Machine Error: " +
						Ex.toString());
		}
		finally
		{
			try{
				connection.close();
			}
			catch(SQLException Ex){
				System.out.println("Error connecting to database. Machine Error: " +
						Ex.toString());
			}
		}
	}
	
	public static void populateMessages(){
		try{
			for (int i = 0; i < 300; i ++){
				Random gen = new Random();
				
				int sender_ID = gen.nextInt(99);
				int receiver_ID = gen.nextInt(99);
				
				if (sender_ID == receiver_ID){
					while (sender_ID == receiver_ID){
						receiver_ID = gen.nextInt(99);
					}
				}
				
				int month = gen.nextInt(12) + 1;
				int day = gen.nextInt(27) + 1;
					
				String m = Integer.toString(month);
				String d = Integer.toString(day);
				String ye = "2016-";
					
				String date = ye + m + "-" + d;
					
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date DATE = new java.sql.Date (df.parse(date).getTime());
					
				
				query = "insert into Messages values (?, ?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, sender_ID);
				prepStatement.setInt(2, receiver_ID);
				prepStatement.setString(3, "<3");
				prepStatement.setString(4, "Heyyyyyy:)");
				prepStatement.setDate(5, DATE);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
		}
		catch(Exception Ex){
			System.out.println("Error connecting to database. Machine Error: " +
						Ex.toString());
		}
		finally
		{
			try{
				connection.close();
			}
			catch(SQLException Ex){
				System.out.println("Error connecting to database. Machine Error: " +
						Ex.toString());
			}
		}
	}
}
