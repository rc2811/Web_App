import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Servlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String dbPassKey = "password";
	private static final String dbUserKey = "username";
	private static final String dbFIDKey = "friendids";
	private static final String dbFBKey = "fbid";
	private static final String dbMessageKey = "notes";
	
	private Connection conn;
	
	public Servlet()
	{
	   try {
			Class.forName("org.postgresql.Driver");

			 conn = DriverManager.getConnection (
	    	"jdbc:postgresql://db.doc.ic.ac.uk/g1227128_u",
			"g1227128_u", "lmX5xgXehM" );

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
 
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
    	try
		{

			Statement stmt;
			PreparedStatement pstmt;
			
			String command = request.getParameter("command");
			String[] arguments = request.getParameterValues("args");
			String reply = "UNDETECTED ERROR";
			
			if(command == null || arguments == null)
				return;
			
			stmt = conn.createStatement();

			ResultSet rs;
			
			if(command.equals(Command.LOGIN.toString()))
			{
				rs = stmt.executeQuery("SELECT " + dbUserKey + " FROM logins WHERE " + dbUserKey + " = '" + arguments[0] + "';");
				if(rs.next()) {
					
					rs = stmt.executeQuery("SELECT * FROM logins WHERE " + dbUserKey + " = '" + arguments[0] + "';");
					rs.next();
					
					if(BCrypt.checkpw(arguments[1], rs.getString(dbPassKey)))
					{
						reply = "OK";
					}
					else
					{
						reply = "PASSWORD INCORRECT";
					}
				}
				else {
					reply = "USER DOES NOT EXIST";
				}
			}
			else if(command.equals(Command.REGISTER.toString()))
			{
				rs = stmt.executeQuery("SELECT " + dbUserKey + " FROM logins;");
checkInput: 	{
					while(rs.next())
					{
						if(rs.getString(dbUserKey).equals(arguments[0]))
						{
							reply = "USERNAME ALREADY IN USE";
							break checkInput;
						}
					}
					
				pstmt = conn.prepareStatement("INSERT into logins VALUES('"+ arguments[0] + "', '" + BCrypt.hashpw(arguments[1], BCrypt.gensalt()) + "', " + Integer.parseInt(arguments[2]) + ");");
				pstmt.executeUpdate();
				
				pstmt = conn.prepareStatement("INSERT into userdata VALUES('" + arguments[0] + "');");
				pstmt.executeUpdate();
				
				reply = "OK";			
				}
			}
			else if(command.equals(Command.FETCHIDS.toString()))
			{
				rs = stmt.executeQuery("SELECT " + dbFIDKey + " FROM userdata WHERE " + dbUserKey + " = '" + arguments[0] + "';");
				rs.next();
				try {
					
					Long[] array = (Long[])rs.getArray(dbFIDKey).getArray();				
					reply = "";

					for(long i : array)
					{
						reply += i + ":";
					}
					
				} catch (NullPointerException e) {
						
					reply = "YOU HAVE NO FRIENDS";
				}
			}
			else if(command.equals(Command.INSERTIDS.toString()))
			{
				rs = stmt.executeQuery("SELECT * FROM userdata WHERE " + dbUserKey + " = '" + arguments[0] + "';");
				if(!rs.next())
				{
					pstmt = conn.prepareStatement("INSERT into userdata VALUES('"+ arguments[0] + "', '" + prepareToPrintWithRemoval(arguments) + "');");
					pstmt.executeUpdate();
				}
				else if(arguments.length > 1)
				{
					pstmt = conn.prepareStatement("UPDATE userdata SET " + dbFIDKey + " = array_sort_unique(" +
												dbFIDKey + " || " + prepareToPrintWithRemoval(arguments) + "::bigint[])" +
												" WHERE " + dbUserKey + " = '" + arguments[0] + "';");
					pstmt.executeUpdate();
				}
				reply = "OK";
			}
			else if(command.equals(Command.CLEARIDS.toString()))
			{
				pstmt = conn.prepareStatement("UPDATE userdata SET " + dbFIDKey + " = NULL WHERE " + dbUserKey + " = '" + arguments[0] + "';");
				pstmt.executeUpdate();
				reply = "OK";
			}
			else if(command.equals(Command.ADDFBID.toString()))
			{
				rs = stmt.executeQuery("SELECT " + dbFBKey + " FROM logins WHERE " + dbUserKey + " = '" + arguments[0] + "';");
				rs.next();
				
				if(rs.getInt(dbFBKey) > 0)
				{
					reply = "CANNOT ADD FACEBOOK ID, ONE ALREADY EXISTS";
				}
				else
				{
					pstmt = conn.prepareStatement("UPDATE logins SET " + dbFBKey + " = " + Integer.parseInt(arguments[1]) + " WHERE " + dbUserKey + " = '" + arguments[0] + "';");
					pstmt.executeUpdate();
					reply = "OK";
				}
				
			}
			else if(command.equals(Command.SENDMESSAGETO.toString()))
			{
				rs = stmt.executeQuery("SELECT " + dbUserKey + " FROM logins WHERE " + dbFBKey + " = '" + arguments[1] + "';");
				if(rs.next())
				{
					String recipient = rs.getString(dbUserKey);
					
					pstmt = conn.prepareStatement("UPDATE userdata" + " SET " + dbMessageKey + " = " +
							dbMessageKey + " || " + "ROW( '" + arguments[0] + "' , '" + arguments[2] + "' )::fridgenote" +
							" WHERE " + dbUserKey + " = '" + recipient + "';");
					pstmt.executeUpdate();
					reply = "OK";
				}
				else
				{
					reply = "CANNOT SEND MESSAGE, USER DOES NOT EXIST";
				}
			}
			else if(command.equals(Command.GETMESSAGES.toString()))
			{
				rs = stmt.executeQuery("SELECT array_to_string(" + dbMessageKey + ", '~') FROM userdata WHERE " + dbUserKey + " = '" + arguments[0] + "';");
				if(!rs.next() || (reply = rs.getString("array_to_string")) == null || reply.isEmpty())
				{
					reply = "YOU HAVE NO NOTES";
				}
			}
			else if(command.equals(Command.DELETEMESSAGE.toString()))
			{
				pstmt = conn.prepareStatement("UPDATE userdata SET " + dbMessageKey + 
												" = array_remove_item (" + dbMessageKey + ", " + arguments[1] +
												") WHERE " + dbUserKey + " = '" + arguments[0] + "';");
				pstmt.executeUpdate();
				reply = "OK";

			}
			else if(command.equals(Command.GETFRIENDS.toString()))
			{
				rs = stmt.executeQuery("SELECT " + dbUserKey + " FROM logins WHERE " + dbFBKey + "::bigint = " +
							"ANY((SELECT " + dbFIDKey + " FROM userdata WHERE " + dbUserKey + " = '" + arguments[0] + "')::bigint[]);");
				
				reply = "";

				while(rs.next())
				{
					reply += rs.getString(dbUserKey) + "~";
				}

			}
			else
			{
				reply = "UNKNOWN COMMAND";
			}
			
	    	response.setStatus(HttpServletResponse.SC_OK);
			OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
	    
			writer.write(reply);
			writer.close();
			
		} catch (SQLException e) {
				e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private String prepareToPrintWithRemoval(Object[] args)
    {
    	return prepareToPrint(Arrays.copyOfRange(args, 1, args.length));
    }
    
    private String prepareToPrint(Object[] args)
    {
    	return "ARRAY" + Arrays.toString(args);
    }
}
