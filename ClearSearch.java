/*
 * 	This is used to clear browsing data
 */

package com.hcl.toygoogle;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
 * Servlet implementation class ClearSearch
 */
@WebServlet("/ClearSearch")
public class ClearSearch extends GenericServlet {
	private static final long serialVersionUID = 1L;
	private Connection con;

	/**
	 * @see GenericServlet#GenericServlet()
	 */
	public ClearSearch() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#service(ServletRequest request, ServletResponse response)
	 */
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		//This is used to clear browsing data
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/toyGoogle", "root", "Sai@4884");
			
			PreparedStatement ps = con.prepareStatement("delete from  searchStrings");
			
			PrintWriter out = response.getWriter();
			
			int i = ps.executeUpdate();
			System.out.println(ps.executeUpdate());
			
			out.println("<html>");
			out.println("<body>");
			out.println("<script>");
			out.println("alert('Successfully Cleared Browsing Data')");
			out.println("</script>");
			out.println("</html>");
			out.println("</body>");
			
			//redirect to search string html page
			RequestDispatcher dispatcher = request.getRequestDispatcher("searchStrings.html");
			dispatcher.include(request, response);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
