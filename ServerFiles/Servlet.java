import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Arrays;


public class Servlet extends HttpServlet {
	
	private static final String dbPassKey = "password";
	private static final String dbUserKey = "username";
	private static final String dbFIDKey = "friendids";
	
	private static final long serialVersionUID = 1L;
	
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
					
					if(rs.getString(dbPassKey).equals(arguments[1]))
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
					
				pstmt = conn.prepareStatement("INSERT into logins VALUES('"+ arguments[0] + "', '" + arguments[1] + "', " + Integer.parseInt(arguments[2]) + ");");

				pstmt.executeUpdate();
				reply = "OK";
					
				}
			}
			else if(command.equals(Command.FETCHIDS.toString()))
			{
				rs = stmt.executeQuery("SELECT * FROM userdata WHERE " + dbUserKey + " = '" + arguments[0] + "';");
				rs.next();
				Integer[] array = (Integer[])rs.getArray(dbFIDKey).getArray();

				for(int i : array)
				{
					reply += i + ":";
				}
			}
			else if(command.equals(Command.INSERTIDS.toString()))
			{
				rs = stmt.executeQuery("SELECT * FROM userdata WHERE " + dbUserKey + " = '" + arguments[0] + "';");
				if(!rs.next())
				{
					pstmt = conn.prepareStatement("INSERT into userdata VALUES('"+ arguments[0] + "', '" + prepareToPrintWithRemoval(arguments) + "');");
				}
				else
				{
					rs = stmt.executeQuery("SELECT * FROM userdata WHERE " + dbUserKey + " = '" + arguments[0] + "';");
					rs.next();
					Integer[] array = (Integer[])rs.getArray(dbFIDKey).getArray();
					
					pstmt = conn.prepareStatement("UPDATE userdata" + " SET " + dbFIDKey + " = " +
												"array_cat(" + prepareToPrint(array) +
														"," + prepareToPrintWithRemoval(arguments) + ") WHERE " + dbUserKey + " = '" + arguments[0] + "';");
				}
				pstmt.executeUpdate();
				reply = "OK";
			}
			else
			{

				reply = "UNKNOWN COMMAND";
			}
			
			
			response.setStatus(HttpServletResponse.SC_OK);
			
			OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
        
			writer.write(reply);
			writer.flush();
			writer.close();
			
		} catch (SQLException e) {
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

    public enum Command {
    	LOGIN, REGISTER, FETCHIDS, INSERTIDS, ADDFBID, SENDMESSAGETO, GETMESSAGES
    }
}
