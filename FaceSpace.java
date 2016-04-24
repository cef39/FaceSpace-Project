import java.util.ArrayList;
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
	
    public static void main(String[] args) {
		try{
			System.out.println("Registering DB..");
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			// Register the oracle driver.  
	    
			System.out.println("Set url..");
			String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass"; 
			
			System.out.println("Connect to DB..");
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
				else if (choice==4){
					FaceSpaceDriver();
				}
				else if (choice==5){
					DisplayFriendsPrompt(in);
				}
				else if (choice==6){
					CreateGroupPrompt(in);
				}
				else if (choice==7){
					AddToGroupPrompt(in);
				}
				else if (choice==8){
					SendAMessagePrompt(in);
				}
				else if (choice==9){
					DisplayMessagesPrompt(in);
				}
				else if (choice==10){
					SearchForFriendsPrompt(in);
				}
				else if (choice==11){
					ThreeDegreesPrompt(in);
				}
				else {
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
    
	private static void ThreeDegreesPrompt(Scanner in) {
		int startUserID=0;
		int endUserID=0;
		System.out.print("Starting UserID: ");
		startUserID=Integer.parseInt(in.nextLine());
		System.out.print("End UserID: ");
		endUserID=Integer.parseInt(in.nextLine());
		System.out.println();
		ThreeDegrees(startUserID,endUserID);
	}

	private static void ThreeDegrees(int startUserID, int endUserID) {
		try{
			int numberOfHops=0;
			int bestFirstHop=0;
			int bestSecondHop=0;
			int firstHop=0;
			int secondHop=0;
			int thirdHop=0;
			Statement statement1=connection.createStatement();
			Statement statement2=connection.createStatement();
			ResultSet resultSet1;
			ResultSet resultSet2;
			statement=connection.createStatement();
			
			String search="SELECT friendID FROM Friends WHERE user_ID=" + startUserID + " AND relationship=1";
			resultSet=statement.executeQuery(search);
			while(resultSet.next()){
				firstHop=resultSet.getInt(1);
				if (firstHop==startUserID){
					continue;
				}
				
				if (firstHop==endUserID){
					numberOfHops=1;
					break;
				}
				else if(numberOfHops==3 || numberOfHops==0){
					String search1="SELECT friendID FROM Friends WHERE user_ID=" + firstHop + " AND relationship=1";
					resultSet1=statement1.executeQuery(search1);
					while(resultSet1.next()){
						
						
						secondHop=resultSet1.getInt(1);
						if (secondHop==firstHop || secondHop==startUserID){
							continue;
						}
						
						if (secondHop==endUserID && (numberOfHops>2 || numberOfHops==0)){
							numberOfHops=2;
							bestFirstHop=firstHop;
							break;
						}
						else if(numberOfHops==0){
							String search2="SELECT friendID FROM Friends WHERE user_ID=" + secondHop + " AND relationship=1";
							resultSet2=statement2.executeQuery(search2);
							while(resultSet2.next()){
								
								thirdHop=resultSet2.getInt(1);
								if (thirdHop==startUserID || thirdHop==firstHop || thirdHop==secondHop){
									continue;
								}
								
								if (thirdHop==endUserID && numberOfHops==0){
									numberOfHops=3;
									bestFirstHop=firstHop;
									bestSecondHop=secondHop;
									break;
								}
							}
						}
						
					}
				}
			}
			if (numberOfHops==1){
				System.out.println("Path: " + FindName(startUserID) + ", " + FindName(endUserID));
			}
			else if (numberOfHops==2){
				System.out.println("Path: " + FindName(startUserID) + ", " + FindName(bestFirstHop) + ", " + FindName(endUserID));
			}
			else if (numberOfHops==3){
				System.out.println("Path: " + FindName(startUserID) + ", " + FindName(bestFirstHop) + ", " + FindName(bestSecondHop) + ", " + FindName(endUserID));
			}
			System.out.println();
		}
		catch(SQLException e){
			System.out.println("Error: " + e.toString());
		}
	}

	private static String FindName(int userID) {
		try{
			statement=connection.createStatement();
			String sql="SELECT fname,lname FROM Profiles WHERE user_ID=" + userID;
			resultSet=statement.executeQuery(sql);
			String name="";
			while(resultSet.next()){
				name=resultSet.getString(1) + " " + resultSet.getString(2);
			}
			return name;
		}
		catch(SQLException e){
			System.out.println("Error: " + e.toString());
			return "Error";
		}
	}

	private static void SearchForFriendsPrompt(Scanner in) {
		System.out.print("Insert search keyword(s): ");
		String[] keywords=in.nextLine().split(" ");
		System.out.println();
		SearchForFriends(keywords);
	}

	private static void SearchForFriends(String[] keywords) {
		try{
			System.out.println("Search Results:");
			int count=1;
			statement=connection.createStatement();
			for(String s:keywords){
				String search="SELECT fname,lname FROM Profiles WHERE fname='" + s + "' OR lname='" + s + "'";
				resultSet=statement.executeQuery(search);
				while(resultSet.next()){
					System.out.println(count + ". " + resultSet.getString(1) + " " + resultSet.getString(2));
					count++;
				}
			}
			System.out.println();
		}
		catch(SQLException e){
			System.out.println("Error: " + e.toString());
		}
		
	}

	private static void DisplayMessagesPrompt(Scanner in) {
		int userID=0;
		System.out.print("UserID: ");
		userID=Integer.parseInt(in.nextLine());
		System.out.println();
		DisplayMessages(userID);
	}

	private static void DisplayMessages(int userID) {
		try{
			statement=connection.createStatement();
			String getMessages="SELECT p.fname,p.lname,m.msg_text FROM Profiles p JOIN Messages m ON m.sender_ID=p.user_ID WHERE m.receiver_ID=" + userID;
			System.out.println(getMessages);
			resultSet=statement.executeQuery(getMessages);
			System.out.println("\nMessages sent to user:");
			while(resultSet.next()){
				System.out.println(resultSet.getString(1) + " " + resultSet.getString(2) + ": " + resultSet.getString(3));
			}
			System.out.println();
		}
		catch(SQLException e){
			System.out.println("Error: " + e.toString());
		}
		
	}

	private static void SendAMessagePrompt(Scanner in) {
		int sender=0;
		int receiver=0;
		String subject="";
		String message="";
		System.out.print("Sender ID: ");
		sender=Integer.parseInt(in.nextLine());
		System.out.print("Receiver ID: ");
		receiver=Integer.parseInt(in.nextLine());
		System.out.print("Subject: ");
		subject=in.nextLine();
		System.out.print("Message(100 characters): ");
		message=in.nextLine();
		System.out.println();
		SendAMessage(sender,receiver,subject,message);
	}

	private static void SendAMessage(int sender, int receiver, String subject, String message) {
		try{
			java.util.Date date = new java.util.Date();
			java.sql.Date current=new java.sql.Date(date.getTime());
			String sql="INSERT INTO Messages VALUES (?,?,?,?,?)";
			prepStatement=connection.prepareStatement(sql);
			prepStatement.setInt(1, sender);
			prepStatement.setInt(2, receiver);
			prepStatement.setString(3, subject);
			prepStatement.setString(4, message);
			prepStatement.setDate(5, current);
			prepStatement.executeUpdate();
			System.out.println();
		}
		catch(SQLException e){
			System.out.println("Error: " + e.toString());
		}
	}

	private static void AddToGroupPrompt(Scanner in) {
		int userID=0;
		int groupID=0;
		System.out.print("User ID: ");
		userID=Integer.parseInt(in.nextLine());
		System.out.print("Group ID: ");
		groupID=Integer.parseInt(in.nextLine());
		System.out.println();
		AddToGroup(userID,groupID);
	}

	private static void AddToGroup(int userID, int groupID) {
		try{
			statement=connection.createStatement();
			int limit=0;
			int count=0;
			String limitQuery="SELECT group_limit FROM Groups WHERE group_ID=" + groupID;
			resultSet=statement.executeQuery(limitQuery);
			while(resultSet.next()){
				limit=resultSet.getInt(1);
			}
			String countQuery="SELECT COUNT(user_ID) FROM UserGroups WHERE group_ID=" + groupID;
			resultSet=statement.executeQuery(countQuery);
			while(resultSet.next()){
				count=resultSet.getInt(1);
			}
			if (count<limit){
				String insert="INSERT INTO UserGroups VALUES (?,?)";
				prepStatement=connection.prepareStatement(insert);
				prepStatement.setInt(1, userID);
				prepStatement.setInt(2, groupID);
				prepStatement.executeUpdate();
			}
			else{
				System.out.println("Group has reached max capacity.");
			}
			System.out.println();
		}
		catch(SQLException e){
			System.out.println("Error: " + e.toString());
		}
		
	}

	private static void CreateGroupPrompt(Scanner in) {
		String groupName="";
		String groupDescription="";
		int groupLimit=0;
		System.out.print("Group Name: ");
		groupName=in.nextLine();
		System.out.print("Group Description(150 characters): ");
		groupDescription=in.nextLine();
		System.out.print("Member Limit: ");
		groupLimit=Integer.parseInt(in.nextLine());
		System.out.println();
		CreateGroup(groupName,groupDescription,groupLimit);
	}

	private static void CreateGroup(String groupName, String groupDescription, int groupLimit) {
		try{
			statement=connection.createStatement();
			String countQuery="SELECT COUNT(Group_ID) FROM Groups";
			resultSet=statement.executeQuery(countQuery);
			int groupID=0;
			while (resultSet.next()){
				groupID=resultSet.getInt(1);
			}
			String insert="INSERT INTO Groups VALUES (?,?,?,?)";
			prepStatement=connection.prepareStatement(insert);
			prepStatement.setInt(1, groupID++);
			prepStatement.setString(2, groupName);
			prepStatement.setString(3, groupDescription);
			prepStatement.setInt(4, groupLimit);
			prepStatement.executeUpdate();
			System.out.println();
		}
		catch(SQLException e){
			System.out.println("Error: " + e.toString());
		}
	}

	private static void EstablishFriendshipPrompt(Scanner in) {
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
			
			String query = "insert into Friends (user_ID,friendID,relationship) values (?,?,?)";
			    prepStatement = connection.prepareStatement(query);
			    prepStatement.setInt(1, userIDSender); 
			    prepStatement.setInt(2, userIDReceiver);
			    prepStatement.setInt(3, 0);
			    prepStatement.executeUpdate();
			    
			    prepStatement = connection.prepareStatement(query);
			    prepStatement.setInt(1, userIDReceiver); 
			    prepStatement.setInt(2, userIDSender);
			    prepStatement.setInt(3, 0);
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
		System.out.println("4. Run FaceSpace Driver.");
		System.out.println("5. Display Friends.");
		System.out.println("6. Create Group.");
		System.out.println("7. Add To Group.");
		System.out.println("8. Send Message To User.");
		System.out.println("9. Display Messages.");
		System.out.println("10. Search For Users.");
		System.out.println("11. Three Degrees.");
		System.out.println("0. Quit");
		System.out.print("Choice: ");
		return Integer.parseInt(in.nextLine());
		
	}
	private static void FaceSpaceDriver() {
		try{
			statement = connection.createStatement();
			String[] firstNames={"Alex","Adam","Chris","Donald"};
			String[] lastNames={"Mitro","Mitro","Flickinger","Lewis"};
			String email="@gmail.com";
			String[] birthdays={"1995-05-23","1995-05-23","1994-12-05","1995-03-27"};
			for(int i=0;i<4;i++){
			
				System.out.println("Creating user " + firstNames[i] + " " + lastNames[i] + " who's email is " + firstNames[i] + email +
					" and their birthday is " + birthdays[i]);
				CreateUser(firstNames[i],lastNames[i],email,birthdays[i]);
				System.out.println("User was created successfully.\n");
			}
			System.out.println();
			String selectQuery = "SELECT * FROM Profiles";
			resultSet=statement.executeQuery(selectQuery);
			System.out.println("OUTPUT FROM DATABASE:\n");
			while (resultSet.next()){
				System.out.println("UserID: " + resultSet.getInt(1) + " Name: " + resultSet.getString(2) + " " + resultSet.getString(3) + " Email: " + resultSet.getString(4) + 
						" Birthday: " + resultSet.getDate(5).toString() + " Last Login: " + resultSet.getTimestamp(6).toString() + "\n");
			}
			System.out.println("Initiating friendship of users with UserIDs Alex and Adam.");
			InitiateFriendship(numberOfUsers-4,numberOfUsers-3);
			System.out.println("Initiating friendship of users with UserIDs Chris and Donald.");
			InitiateFriendship(numberOfUsers-2,numberOfUsers-1);
			System.out.println("Establishing friendship between 0 and 1.");
			EstablishFriendship(0,1);
			String selectQuery1 = "SELECT * FROM Friends WHERE relationship=1";
			resultSet=statement.executeQuery(selectQuery1);
			System.out.println("OUTPUT FROM DATABASE THE FRIENDS WHO ESTABLISHED A FRIENDSHIP:\n");
			while (resultSet.next()){
				System.out.println("UserID: " + resultSet.getInt(1) + " Friend UserID: " + resultSet.getInt(2) +  
						" Relationship: " + resultSet.getInt(3) + " Established Date: " + resultSet.getDate(4).toString() + "\n");
			}
		}
		catch(SQLException e){
			System.out.println("Error:" + e.toString());
		}
	}

	private static void DisplayFriendsPrompt(Scanner in) {
			int userID=0;
			System.out.println("Who's friends would you like to display?");
			System.out.print("ID: ");
			userID=Integer.parseInt(in.nextLine());
			System.out.println();
			DisplayFriends(userID);
			
	}

	private static void DisplayFriends(int userID) {
		try{
			String selectQueryEstablished="SELECT fname,lname FROM Friends f JOIN Profiles p ON f.friendID=p.user_ID WHERE f.user_ID=" + userID + " AND f.relationship=1 GROUP BY fname,lname";
			String selectQueryInitiated="SELECT fname,lname FROM Friends f JOIN Profiles p ON f.friendID=p.user_ID WHERE f.user_ID=" + userID + " AND f.relationship=0 GROUP BY fname,lname";
			resultSet=statement.executeQuery(selectQueryEstablished);
			int count=1;
			System.out.println();
			System.out.println("Friends:");
			while (resultSet.next()){
				System.out.println(count + ". " + resultSet.getString(1) + " " + resultSet.getString(2));
				count++;
			}
			System.out.println();
			count=1;
			resultSet=statement.executeQuery(selectQueryInitiated);
			System.out.println("Pending Friendships:");
			while (resultSet.next()){
				System.out.println(count + ". " + resultSet.getString(1) + " " + resultSet.getString(2));
				count++;
			}
			System.out.println();
		}
		catch(SQLException e){
			System.out.println("Error: " + e.toString());
		}
	}
}