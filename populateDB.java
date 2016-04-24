import java.sql.*;
import oracle.jdbc.*;
import java.util.Random;


public class populateDB{
	private static Connection connection;	//used to hold the jdbc connection to the DB
	private static Statement statement; //used to create an instance of the connection
	private static PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
	private static String query; //this will hold the query we are using
	private static String username = "cef39";
	private static String password = "3910744";
	private static String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
	
	public static void main(String args[]) throws SQLException
	{		
		
		try{
			System.out.println("Registering DB..");
			// Register the oracle driver.  
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			
			System.out.println("Set url..");
			//This is the location of the database.  This is the database in oracle
			//provided to the class
			
			
			System.out.println("Connect to DB..");
			//create a connection to DB on class3.cs.pitt.edu
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
				//split name from above name array
				String [] parts = name.split(" ");
				String fname = parts[0];
				String lname = parts[1];
					
				//everyone's born on 2/24. year is incremented each time
				String birthday = year + "-02-24";
				y = Integer.parseInt(year);
				y++;
				year = Integer.toString(y);
					
				String email = fname + lname + Integer.toString(i) + "@yahoo.com";
					
				//have to format birthday into Date object
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date birth = new java.sql.Date (df.parse(birthday).getTime());
					
				//gets current date and time as Timestamp
				java.util.Date date = new java.util.Date();
				java.sql.Timestamp login = new java.sql.Timestamp(date.getTime());
					
				//query to insert into table these values using prepStatement
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
				connection = DriverManager.getConnection(url, username, password);
				//get random ID from listed IDs
				Random gen = new Random();
				int friendID = gen.nextInt(100);
					
				//get random ID from listed IDs
				if (friendID == userID){
					while (friendID == userID){
						friendID = gen.nextInt(100);
					}
				}
					
				//random value for pending or establish relationship
				int friendRequest = gen.nextInt(2);
					
				//random month and day
				int month = gen.nextInt(12) + 1;
				int day = gen.nextInt(27) + 1;
					
				String m = Integer.toString(month);
				String d = Integer.toString(day);
				String ye = "2016-";
					
				String date = ye + m + "-" + d;
					
				//convert establish date of friendship into Date object
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date establishedDate = new java.sql.Date (df.parse(date).getTime());
					
				//insert friendship into table
				query = "insert into Friends values (?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, userID);
				prepStatement.setInt(2, friendID);
				prepStatement.setInt(3, friendRequest);
				prepStatement.setDate(4, establishedDate);
					
				prepStatement.executeUpdate();
					
				//inserts friendship into table, but swaps the ID's
				query = "insert into Friends values (?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, friendID);
				prepStatement.setInt(2, userID);
				prepStatement.setInt(3, friendRequest);
				prepStatement.setDate(4, establishedDate);
					
				prepStatement.executeUpdate();
				connection.close();
					
				userID++;
					
				if (userID == 100){
					userID = 0;
				}
					
			}
		}
		catch(Exception Ex){
			System.out.println("Error connecting to database. Machine Error: " +
						Ex.toString());
		}
	}
	
	public static void populateGroups(){
		try{
			int user_ID_1 = -1;
			int user_ID_2 = -1;
			
			//inserts into groups table
			query = "insert into Groups values (?, ?, ?, ?)";
			prepStatement = connection.prepareStatement(query);
					
			prepStatement.setInt(1, 0);
			prepStatement.setString(2, "Group numero uno");
			prepStatement.setString(3, "We are number one!!!");
			prepStatement.setInt(4, 5);
				
			prepStatement.executeUpdate();
			connection.commit();
			
			//adds 3 random users to one group
			for (int i = 0; i < 3; i++){
				
				//grabs random user_ID
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				//checks to make sure that second member isn't same as first member
				if (i == 1 && user_ID == user_ID_1){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
				
				//checks to make sure that third member isn't same as first two members
				if (i == 2 && (user_ID == user_ID_2 || user_ID == user_ID_1)){
					while (user_ID == user_ID_1 || user_ID == user_ID_2){
						user_ID = gen.nextInt(100);
					}
				}
				
				//saves current user_ID
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
				
				//inserts into groups table
				query = "insert into UserGroups values (?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, user_ID);
				prepStatement.setInt(2, 0);
				
				prepStatement.executeUpdate();
				connection.commit();
			}

			//inserts into groups table
			query = "insert into Groups values (?, ?, ?, ?)";
			prepStatement = connection.prepareStatement(query);
					
			prepStatement.setInt(1, 1);
			prepStatement.setString(2, "Dinos Rules");
			prepStatement.setString(3, "We are trying to conquer the world by bringing back dinosaurs.");
			prepStatement.setInt(4, 5);
				
			prepStatement.executeUpdate();
			connection.commit();
			
			user_ID_1 = -1;
			user_ID_2 = -1;
			for (int i = 0; i < 3; i++){
				
				//grabs random user_ID
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				//checks to make sure that second member isn't same as first member
				if (i == 1 && user_ID == user_ID_1){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
				
				//checks to make sure that third member isn't same as first two members
				if (i == 2 && (user_ID == user_ID_2 || user_ID == user_ID_1)){
					while (user_ID == user_ID_1 || user_ID == user_ID_2){
						user_ID = gen.nextInt(100);
					}
				}
				
				//saves current user_ID
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
				
				//inserts into groups table
				query = "insert into UserGroups values (?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, user_ID);
				prepStatement.setInt(2, 1);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			//inserts into groups table
			query = "insert into Groups values (?, ?, ?, ?)";
			prepStatement = connection.prepareStatement(query);
					
			prepStatement.setInt(1, 2);
			prepStatement.setString(2, "Cell for Emperor");
			prepStatement.setString(3, "Remember Cell from DBZ? He's our savior.");
			prepStatement.setInt(4, 5);
				
			prepStatement.executeUpdate();
			connection.commit();
			
			user_ID_1 = -1;
			user_ID_2 = -1;
			for (int i = 0; i < 3; i++){
				
				//grabs random user_ID
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				//checks to make sure that second member isn't same as first member
				if (i == 1 && user_ID == user_ID_1){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
				
				//checks to make sure that third member isn't same as first two members
				if (i == 2 && (user_ID == user_ID_2 || user_ID == user_ID_1)){
					while (user_ID == user_ID_1 || user_ID == user_ID_2){
						user_ID = gen.nextInt(100);
					}
				}
				
				//saves current user_ID
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
				
				//inserts into groups table
				query = "insert into UserGroups values (?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, user_ID);
				prepStatement.setInt(2, 2);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			//inserts into groups table
			query = "insert into Groups values (?, ?, ?, ?)";
			prepStatement = connection.prepareStatement(query);
					
			prepStatement.setInt(1, 3);
			prepStatement.setString(2, "Egg Club");
			prepStatement.setString(3, "We really like eggs.");
			prepStatement.setInt(4, 5);
				
			prepStatement.executeUpdate();
			connection.commit();
			
			user_ID_1 = -1;
			user_ID_2 = -1;
			for (int i = 0; i < 3; i++){
				
				//grabs random user_ID
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				//checks to make sure that second member isn't same as first member
				if (i == 1 && user_ID == user_ID_1){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
				
				//checks to make sure that third member isn't same as first two members
				if (i == 2 && (user_ID == user_ID_2 || user_ID == user_ID_1)){
					while (user_ID == user_ID_1 || user_ID == user_ID_2){
						user_ID = gen.nextInt(100);
					}
				}
				
				//saves current user_ID
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
				
				//inserts into groups table
				query = "insert into UserGroups values (?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, user_ID);
				prepStatement.setInt(2, 3);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			//inserts into groups table
			query = "insert into Groups values (?, ?, ?, ?)";
			prepStatement = connection.prepareStatement(query);
					
			prepStatement.setInt(1, 4);
			prepStatement.setString(2, "Book Club");
			prepStatement.setString(3, "We meet every Wednesday at 7PM at Schenley Cafe.");
			prepStatement.setInt(4, 5);
				
			prepStatement.executeUpdate();
			connection.commit();
			
			user_ID_1 = -1;
			user_ID_2 = -1;
			for (int i = 0; i < 3; i++){
				
				//grabs random user_ID
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				//checks to make sure that second member isn't same as first member
				if (i == 1 && user_ID == user_ID_1){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
				
				//checks to make sure that third member isn't same as first two members
				if (i == 2 && (user_ID == user_ID_2 || user_ID == user_ID_1)){
					while (user_ID == user_ID_1 || user_ID == user_ID_2){
						user_ID = gen.nextInt(100);
					}
				}
				
				//saves current user_ID
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
				
				//inserts into groups table
				query = "insert into UserGroups values (?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, user_ID);
				prepStatement.setInt(2, 4);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			//inserts into groups table
			query = "insert into Groups values (?, ?, ?, ?)";
			prepStatement = connection.prepareStatement(query);
					
			prepStatement.setInt(1, 5);
			prepStatement.setString(2, "Squadddddd");
			prepStatement.setString(3, "K-town represent.!!");
			prepStatement.setInt(4, 5);
				
			prepStatement.executeUpdate();
			connection.commit();
			
			user_ID_1 = -1;
			user_ID_2 = -1;
			for (int i = 0; i < 3; i++){
				
				//grabs random user_ID
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				//checks to make sure that second member isn't same as first member
				if (i == 1 && user_ID == user_ID_1){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
				
				//checks to make sure that third member isn't same as first two members
				if (i == 2 && (user_ID == user_ID_2 || user_ID == user_ID_1)){
					while (user_ID == user_ID_1 || user_ID == user_ID_2){
						user_ID = gen.nextInt(100);
					}
				}
				
				//saves current user_ID
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
				
				//inserts into groups table
				query = "insert into UserGroups values (?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, user_ID);
				prepStatement.setInt(2, 5);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			//inserts into groups table
			query = "insert into Groups values (?, ?, ?, ?)";
			prepStatement = connection.prepareStatement(query);
					
			prepStatement.setInt(1, 6);
			prepStatement.setString(2, "Vamps");
			prepStatement.setString(3, "Eat, drink, breathe like Vampires.");
			prepStatement.setInt(4, 5);
				
			prepStatement.executeUpdate();
			connection.commit();
			
			user_ID_1 = -1;
			user_ID_2 = -1;
			for (int i = 0; i < 3; i++){
				
				//grabs random user_ID
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				//checks to make sure that second member isn't same as first member
				if (i == 1 && user_ID == user_ID_1){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
				
				//checks to make sure that third member isn't same as first two members
				if (i == 2 && (user_ID == user_ID_2 || user_ID == user_ID_1)){
					while (user_ID == user_ID_1 || user_ID == user_ID_2){
						user_ID = gen.nextInt(100);
					}
				}
				
				//saves current user_ID
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
				
				//inserts into groups table
				query = "insert into UserGroups values (?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, user_ID);
				prepStatement.setInt(2, 6);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			//inserts into groups table
			query = "insert into Groups values (?, ?, ?, ?)";
			prepStatement = connection.prepareStatement(query);
					
			prepStatement.setInt(1, 7);
			prepStatement.setString(2, "George Bush");
			prepStatement.setString(3, "Yeh.");
			prepStatement.setInt(4, 5);
				
			prepStatement.executeUpdate();
			connection.commit();
			
			user_ID_1 = -1;
			user_ID_2 = -1;
			for (int i = 0; i < 3; i++){
				
				//grabs random user_ID
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				//checks to make sure that second member isn't same as first member
				if (i == 1 && user_ID == user_ID_1){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
				
				//checks to make sure that third member isn't same as first two members
				if (i == 2 && (user_ID == user_ID_2 || user_ID == user_ID_1)){
					while (user_ID == user_ID_1 || user_ID == user_ID_2){
						user_ID = gen.nextInt(100);
					}
				}
				
				//saves current user_ID
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
				
				//inserts into groups table
				query = "insert into UserGroups values (?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, user_ID);
				prepStatement.setInt(2, 7);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			//inserts into groups table
			query = "insert into Groups values (?, ?, ?, ?)";
			prepStatement = connection.prepareStatement(query);
					
			prepStatement.setInt(1, 8);
			prepStatement.setString(2, "FaceSpace Project Group");
			prepStatement.setString(3, "Group to complete this project.");
			prepStatement.setInt(4, 5);
				
			prepStatement.executeUpdate();
			connection.commit();
			
			user_ID_1 = -1;
			user_ID_2 = -1;
			for (int i = 0; i < 3; i++){
				
				//grabs random user_ID
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				//checks to make sure that second member isn't same as first member
				if (i == 1 && user_ID == user_ID_1){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
				
				//checks to make sure that third member isn't same as first two members
				if (i == 2 && (user_ID == user_ID_2 || user_ID == user_ID_1)){
					while (user_ID == user_ID_1 || user_ID == user_ID_2){
						user_ID = gen.nextInt(100);
					}
				}
				
				//saves current user_ID
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
				
				//inserts into groups table
				query = "insert into UserGroups values (?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, user_ID);
				prepStatement.setInt(2, 8);
				
				prepStatement.executeUpdate();
				connection.commit();
			}
			
			//inserts into groups table
			query = "insert into Groups values (?, ?, ?, ?)";
			prepStatement = connection.prepareStatement(query);
					
			prepStatement.setInt(1, 9);
			prepStatement.setString(2, "D-Brown");
			prepStatement.setString(3, "D-money appreciation group.");
			prepStatement.setInt(4, 5);
				
			prepStatement.executeUpdate();
			connection.commit();
			
			user_ID_1 = -1;
			user_ID_2 = -1;
			for (int i = 0; i < 3; i++){
				
				//grabs random user_ID
				Random gen = new Random();
				int user_ID = gen.nextInt(100);
				
				//checks to make sure that second member isn't same as first member
				if (i == 1 && user_ID == user_ID_1){
					while (user_ID == user_ID_1){
						user_ID = gen.nextInt(100);
					}
				}
				
				//checks to make sure that third member isn't same as first two members
				if (i == 2 && (user_ID == user_ID_2 || user_ID == user_ID_1)){
					while (user_ID == user_ID_1 || user_ID == user_ID_2){
						user_ID = gen.nextInt(100);
					}
				}
				
				//saves current user_ID
				if (i == 0)
					user_ID_1 = user_ID;
				else if (i == 1)
					user_ID_2 = user_ID;
				
				//inserts into groups table
				query = "insert into UserGroups values (?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, user_ID);
				prepStatement.setInt(2, 9);
				
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
				connection = DriverManager.getConnection(url, username, password);
				Random gen = new Random();
				
				//gets random ID from listed IDs
				int sender_ID = gen.nextInt(100);
				int receiver_ID = gen.nextInt(100);
				
				//checks to make sure they aren't the same
				if (sender_ID == receiver_ID){
					while (sender_ID == receiver_ID){
						receiver_ID = gen.nextInt(100);
					}
				}
				
				//random month and day
				int month = gen.nextInt(12) + 1;
				int day = gen.nextInt(27) + 1;
					
				String m = Integer.toString(month);
				String d = Integer.toString(day);
				String ye = "2016-";
					
				String date = ye + m + "-" + d;
					
				//converts message date into Date object
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date DATE = new java.sql.Date (df.parse(date).getTime());
					
				
				//inserts into Messages table
				query = "insert into Messages values (?, ?, ?, ?, ?)";
				prepStatement = connection.prepareStatement(query);
					
				prepStatement.setInt(1, sender_ID);
				prepStatement.setInt(2, receiver_ID);
				prepStatement.setString(3, "<3");
				prepStatement.setString(4, "Heyyyyyy:)");
				prepStatement.setDate(5, DATE);
				
				prepStatement.executeUpdate();
				connection.close();
			}
		}
		catch(Exception Ex){
			System.out.println("Error connecting to database. Machine Error: " +
						Ex.toString());
		}
	}
}
