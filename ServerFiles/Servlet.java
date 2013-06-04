import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class Servlet extends HttpServlet {
	
	private static final String dbPassKey = "password";
	private static final String dbUserKey = "username";
	
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
        Statement stmt;
		try
		{

        
			String command = request.getParameter("command");
			String[] arguments = request.getParameterValues("args");
			String reply = "";
			
			stmt = conn.createStatement();
			ResultSet rs;
		//	System.out.println("TEST " + command);
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

    public enum Command {
    	LOGIN, REGISTER
    }
}
