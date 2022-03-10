/*
 * This servlet is used to fetch results based on string
 * */
package com.hcl.toygoogle;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.GenericServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ResultServlet
 */
@WebServlet("/ResultServlet")
public class ResultServlet extends GenericServlet {
	private static final long serialVersionUID = 1L;
	private Connection con;

	/**
	 * @see GenericServlet#GenericServlet()
	 */
	public ResultServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#service(ServletRequest request, ServletResponse response)
	 */
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {

		// create instance of the WebCrawlerExampleWithDepth class
		WebCrawlerWithDepth obj = new WebCrawlerWithDepth();

		// pick a URL from the frontier and call the getPageLinks()method and pass 0 as
		// starting depth
		obj.getPageLinks("http://localhost:8080/WebCrawler/index.html", 0);
		
		// initialize arrayList
		ArrayList<String> splitToken = null;

		Iterator<String> itr = obj.urlLinks.iterator();

		while (itr.hasNext()) {
			splitToken = obj.start(itr.next());
		}
		Iterator<String> itr1 = splitToken.iterator();

		// get value of search
		String search = request.getParameter("search");
		ArrayList<String> serachFound = obj.searchString(search);

		Iterator<String> itr3 = serachFound.iterator();

		// set what type content
		response.setContentType("text/html");

		// use printwriter to display output
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\"\r\n"
				+ "	href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\" />");
		out.println("</head>");

		out.println("<body>");
		out.println("<ul id='myul'  style='top:78px;background-color: #4C5A65;color:white;font-weight: bold;\r\n"
				+ "    border: 3px solid #f1f1f1;\r\n" + "position: fixed;left:62%;padding: 20px;\r\n"
				+ "    text-align: center;z-index: 99;'>");

		int count = 0;
		while (itr3.hasNext()) {
			String[] splitStr = itr3.next().split("#@#&");
			out.println(
					"<li style='list-style: none;'><a style='color: beige;text-decoration: none;' href= " + splitStr[0]
							+ ">" + splitStr[0] + "<a><p style='color:yellow;'>&nbsp" + splitStr[1] + "</p></li>");
			count++;
		}
		System.out.println("count: " + count);
		if (count == 0) {
			out.println("<i class=\"fa fa-search\"></i>&nbsp No search found!");

		}
		out.println("</ul>");

		out.println(
				"<button onclick=\"toggle(this);\" style='width: 80px;font-size: 12px;text-align: center;cursor: pointer;float: right;margin: 25px 1400px;border-color: #ccc;z-index: 99;position: fixed;'>Clear Results</button>\r\n"
						+ "\r\n" + "<script>\r\n" + "  let toggle = button => {\r\n"
						+ "    let element = document.getElementById(\"myul\");\r\n"
						+ "    let hidden = element.getAttribute(\"hidden\");\r\n" + "\r\n" + "    if (hidden) {\r\n"
						+ "       element.removeAttribute(\"hidden\");\r\n"
						+ "       button.innerText = \"Clear Results\";\r\n" + "    } else {\r\n"
						+ "       element.setAttribute(\"hidden\", \"hidden\");\r\n"
						+ "       button.innerText = \"Show Results\";\r\n" + "    }\r\n" + "  }\r\n" + "</script>\r\n"
						+ "");

		//Using Database connection to store search string value
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/toyGoogle", "root", "Sai@4884");
			System.out.println(con);
			
			PreparedStatement ps = con.prepareStatement("insert into searchStrings values(?)");
			ps.setString(1, search);
			int i = ps.executeUpdate();
			System.out.println(ps.executeUpdate());
			
			out.println("</body>");
			out.println("</html>");
			
			//navigate to index html page
			RequestDispatcher dispatcher = request.getRequestDispatcher("index.html");
			dispatcher.include(request, response);

			serachFound.clear();

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
