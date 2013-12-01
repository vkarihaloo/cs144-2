package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Date;

public class BuyServlet extends HttpServlet implements Servlet {
       
  public BuyServlet() {}

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {
  	try
  	{
	  	String queryType = request.getParameter("action");
	
	  	
	  	if (queryType.equals("confirm"))
	  	{
	  		if (request.isSecure())
	  		{
		  		HttpSession session = request.getSession(true);
		  		request.setAttribute("Name", (String)session.getAttribute("itemName"));
					request.setAttribute("ID", (String)session.getAttribute("itemId"));
					request.setAttribute("BuyPrice", (String)session.getAttribute("itemPrice"));
					request.setAttribute("CC", request.getParameter("cc"));
					request.setAttribute("Time", new Date());
					request.getRequestDispatcher("/confirm.jsp").forward(request, response);
				}
	  	}
  	}
  	catch (Exception e)
  	{
	  	HttpSession session = request.getSession(true);
	  	request.setAttribute("Name", (String)session.getAttribute("itemName"));
			request.setAttribute("ID", (String)session.getAttribute("itemId"));
			request.setAttribute("BuyPrice", (String)session.getAttribute("itemPrice"));
			request.getRequestDispatcher("/payment.jsp").forward(request, response);
		}
  }
  
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {
  	doGet(request, response);
  }
  
}