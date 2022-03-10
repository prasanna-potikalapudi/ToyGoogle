/*
 * This is used to show the results of search strings
 * */

package com.hcl.toygoogle;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.GenericServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class SearchStringResultsServlet
 */
@WebServlet("/SearchStringResultsServlet")
public class SearchStringResultsServlet extends GenericServlet {
	private static final long serialVersionUID = 1L;
	private Connection con;

	/**
	 * @see GenericServlet#GenericServlet()
	 */
	public SearchStringResultsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#service(ServletRequest request, ServletResponse response)
	 */
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int count = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/toyGoogle", "root", "Sai@4884");
			System.out.println(con);
			
			Statement statement = con.createStatement();
			System.out.println(statement);

			ResultSet rs = statement.executeQuery("select distinct * from searchStrings");
			
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<link rel=\"stylesheet\"\r\n"
					+ "	href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\" />");
			out.println("</head>");
			out.println("<body style='background-color:#FBE4FA;'>");
			out.println(
					"<ul id='myul'  style='font-weight: bold;\r\n"
					+ "  text-align: center; top:60px;font-weight: bold;position: absolute;left:50%;padding: 30px;\r\n"
							+ "    text-align: center;'>");
			while (rs.next()) {

				out.println("<li style='list-style: none;'>" + rs.getString(1) + "</li>");
				count++;
			}

			if (count == 0) {
				out.println("<p style='display: flex;  \r\n"
						+ "  flex-flow: row wrap;\r\n"
						+ "  font-weight: bold;\r\n"
						+ "  text-align: center;top:60px; '><i class=\"fa fa-search\"></i>&nbsp&nbsp No Search Result Found!</p>");
			}
			out.println("</ul>");
			out.println("</body>");
			out.println("</html>");

			//Navigate to search string html page
			RequestDispatcher dispatcher = request.getRequestDispatcher("searchStrings.html");
			dispatcher.include(request, response);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
