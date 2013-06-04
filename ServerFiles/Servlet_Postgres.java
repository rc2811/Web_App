// Servlet_Postgres.java - example connection to Postgres
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class Servlet_Postgres extends HttpServlet {
 
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
 /*       response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title> Login Test</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");
   */     

        try {
 	    Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
      //      out.println("<h1>Driver not found: " + e + e.getMessage() + "</h1>" );
        }


	try {
	    Connection conn = DriverManager.getConnection (
	    	"jdbc:postgresql://db.doc.ic.ac.uk/g1227128_u",
		"g1227128_u", "lmX5xgXehM" );

            Statement stmt = conn.createStatement();
            ResultSet rs;
            
            String user = request.getParameter("name");

            rs = stmt.executeQuery("SELECT username FROM logins WHERE username = '" + user + "';");
            response.setStatus(HttpServletResponse.SC_OK);
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
            if(rs.next()) {
            	writer.write("USER EXISTS");
            }
            else {
            	writer.write("USER DOES NOT EXIST");
            }
            writer.flush();
            writer.close();
            

            
	  /*  out.println( "<table>" );
            while ( rs.next() ) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                int type = rs.getInt("type");

                out.println("<tr><td>"+username+"</td><td>"+password+"</td><td>"+type+"</td></tr>" );
            }
	    out.println( "</table>" );
*/
            conn.close();
        } catch (Exception e) {
      //      out.println( "<h1>exception: "+e+e.getMessage()+"</h1>" );
        }

/*
	try {
	    Connection conn = DriverManager.getConnection (
	    	"jdbc:postgresql://db.doc.ic.ac.uk/g1227128_u?&ssl=true",
		"g1227128_u", "lmX5xgXehM" );

            Statement stmt = conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT *");
	    out.println( "<table>" );
            while ( rs.next() ) {
                String title = rs.getString("title");
                String director = rs.getString("director");
                String origin = rs.getString("origin");
                String made = rs.getString("made");
                String length = rs.getString("length");
                out.println("<tr><td>"+title+"</td><td>"+director+"</td><td>"+origin+"</td><td>"+
			made+"</td><td>"+length+"</td></tr>" );
            }
	    out.println( "</table>" );

            conn.close();
        } catch (Exception e) {
            out.println( "<h1>exception: "+e+e.getMessage()+"</h1>" );
        }
        out.println("</body>");
        out.println("</html>");
*/
    }

}
