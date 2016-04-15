import java.util.Calendar;
import java.util.Scanner;
import java.sql.*;  //import the file containing definitions for the parts
import java.text.ParseException;
import java.text.SimpleDateFormat;

import oracle.jdbc.*;

public class FaceSpace {
	private static Connection connection; //used to hold the jdbc connection to the DB
    private static Statement statement; //used to create an instance of the connection
    private static PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private static ResultSet resultSet; //used to hold the result of your query (if one
    // exists)
    private String query;  //this will hold the query we are using
    private static int numberOfUsers;
	
	private static void EstablishFriendshipPrompt(Scanner in) {
		System.out.println("\nGive the UserID's of the people you want to establish a friendship for.");
		System.out.print("First UserID: ");
		int userIDSender=Integer.parseInt(in.nextLine());
		System.out.print("Second UserID: ");
		int userIDReceiver=Integer.parseInt(in.nextLine());
		System.out.println();
		EstablishFriendship(userIDSender,userIDReceiver);
	}

	private static void EstablishFriendship(int userIDSender, int userIDReceiver) {
		try{
			String updateQuery= "UPDATE Friends SET relationship=1, establishedDate=? WHERE (user_ID=? AND friendID=?) OR (user_ID=? AND friendID=?)" ;
			prepStatement=connection.prepareStatement(updateQuery);
			java.util.Date date = new java.util.Date();
			java.sql.Date current=new java.sql.Date(date.getTime());
			prepStatement.setDate(1,current);
			prepStatement.setInt(2, userIDSender);
			prepStatement.setInt(3, userIDReceiver);
			prepStatement.setInt(4, userIDReceiver);
			prepStatement.setInt(5, userIDSender);
			prepStatement.executeUpdate();
			System.out.println();
			
		} catch (SQLException e) {
			System.out.println("Error.");
		}
	}

	private static void InitiateFriendshipPrompt(Scanner in) {
		System.out.println("\nGive the UserID's of the people you want to initiate a friendship for.");
		System.out.print("First UserID: ");
		int userIDSender=Integer.parseInt(in.nextLine());
		System.out.print("Second UserID: ");
		int userIDReceiver=Integer.parseInt(in.nextLine());
		System.out.println();
		InitiateFriendship(userIDSender,userIDReceiver);
	}

	private static void InitiateFriendship(int userIDSender, int userIDReceiver) {
		try {
			statement = connection.createStatement();
			String sentFirst="";
			String sentLast="";
			String receiveFirst="";
			String receiveLast="";
			String selectQuery = "SELECT * FROM Profiles WHERE user_ID=" + userIDSender + " OR user_ID=" + userIDReceiver;
			resultSet=statement.executeQuery(selectQuery);
			while (resultSet.next()){
				if (resultSet.getInt(1)==userIDSender){
					sentFirst=resultSet.getString(2);
					sentLast=resultSet.getString(3);
				}
				else if (resultSet.getInt(1)==userIDReceiver){
					receiveFirst=resultSet.getString(2);
					receiveLast=resultSet.getString(3);
				}
				else{
					System.out.println("This userID does not exist.");
				}
			}
			String query = "insert into Friends (user_ID,friendID,friendFName,friendLName,relationship) values (?,?,?,?,?)";
			    prepStatement = connection.prepareStatement(query);
			    prepStatement.setInt(1, userIDSender); 
			    prepStatement.setInt(2, userIDReceiver);
			    prepStatement.setString(3, receiveFirst);
			    prepStatement.setString(4, receiveLast);
			    prepStatement.setInt(5, 0);
			    prepStatement.executeUpdate();
			    
			    prepStatement = connection.prepareStatement(query);
			    prepStatement.setInt(1, userIDReceiver); 
			    prepStatement.setInt(2, userIDSender);
			    prepStatement.setString(3, sentFirst);
			    prepStatement.setString(4, sentLast);
			    prepStatement.setInt(5, 0);
			    prepStatement.executeUpdate();
			    System.out.println();
			 
		} catch (SQLException e) {
			System.out.println("Error.");
		}
		
	}

	private static void CreateUserPrompt(Scanner in) {
		System.out.println("\nPlease enter the following information.\n");
		System.out.print("First Name: ");
		String firstName=in.nextLine();
		System.out.print("Last Name: ");
		String lastName=in.nextLine();
		System.out.print("Email: ");
		String email=in.nextLine();
		System.out.print("Date of Birth(YYYY-MM-DD): ");
		String dateOfBirth=in.nextLine();
		System.out.println();
		CreateUser(firstName,lastName,email,dateOfBirth);
	}

	private static void CreateUser(String firstName, String lastName, String email, String dateOfBirth) {
		
		try {
			 String query = "insert into Profiles values (?,?,?,?,?,?)";
			 java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
			 try {
				java.sql.Date birth = new java.sql.Date (df.parse(dateOfBirth).getTime());
				java.util.Date date = new java.util.Date();
				java.sql.Timestamp current=new java.sql.Timestamp(date.getTime());
			    prepStatement = connection.prepareStatement(query);
			    prepStatement.setInt(1, numberOfUsers++); 
			    prepStatement.setString(2, firstName);
			    prepStatement.setString(3, lastName);
			    prepStatement.setString(4, email);
			    prepStatement.setDate(5, birth);
			    prepStatement.setTimestamp(6, current);
			    prepStatement.executeUpdate();
			    System.out.println();
			 }
			 catch (ParseException e1){
				 System.out.println("Incorrect birthday format.");
			 }
		} catch (SQLException e) {
			System.out.println("Couldn't create new user. " + e);
		}
	}

	private static int Menu(Scanner in) {
		System.out.println("What would you like to do? (Type the number of what you want to do.)");
		System.out.println("1. Create user.");
		System.out.println("2. Initiate a friendship.");
		System.out.println("3. Establish a friendship.");
		System.out.print("Choice: ");
		return Integer.parseInt(in.nextLine());
		
	}
	public static void main(String[] args) {
		try{
			System.out.println("Registering DB..");
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			// Register the oracle driver.  
	    

			System.out.println("Set url..");
			//This is the location of the database.  This is the database in oracle
			//provided to the class
			String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass"; 
			
			System.out.println("Connect to DB..");
			//create a connection to DB on class3.cs.pitt.edu
			connection = DriverManager.getConnection(url,"amm364", "3851054"); 
			
			Scanner in = new Scanner(System.in);
			System.out.println("Welcome back to FaceSpace!");
			statement = connection.createStatement();
			String selectQuery = "SELECT COUNT(user_ID) FROM Profiles";
			resultSet=statement.executeQuery(selectQuery);
			while(resultSet.next()){
				numberOfUsers=resultSet.getInt(1);
			}
			System.out.println(numberOfUsers);
			while(true){
				int choice=Menu(in);
				if (choice==1){
					CreateUserPrompt(in);
				}	
				else if (choice==2){
					InitiateFriendshipPrompt(in);
				}	
				else if (choice==3){
					EstablishFriendshipPrompt(in);
				}	
				else{
					break;
				}
			}
			
		}
		catch(Exception Ex)  {
		    System.out.println("Error connecting to database.  Machine Error: " +
		    Ex.toString());
		}
		finally{
			try{
				connection.close();
				System.out.println("Database Closed");
			}
			catch(Exception e){
				System.out.println("Couldn't close database " + e.toString());
			}
		}
	}
}

