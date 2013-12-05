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

import java.util.HashMap;

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
	  			String id = request.getParameter("id");

		  		HttpSession session = request.getSession(true);
		  		HashMap<String, String[]> tempmap = (HashMap<String, String[]>)session.getAttribute("itemMap");
		  		if (tempmap.containsKey(id))
		  		{
		  			String[] values = (String[])tempmap.get(id);
			  		request.setAttribute("Name", values[0]);
						request.setAttribute("ID", id);
						request.setAttribute("BuyPrice", values[1]);

						request.setAttribute("CC", request.getParameter("cc"));
						request.setAttribute("Time", new Date());
						request.getRequestDispatcher("/confirm.jsp").forward(request, response);
					}
				}
	  	}
  	}
  	catch (Exception e)
  	{
  		String id = request.getParameter("id");
	  	HttpSession session = request.getSession(true);

	  	session.setAttribute("currentItemBeingPurchased", id);

	  	HashMap<String, String[]> tempmap = (HashMap<String, String[]>)session.getAttribute("itemMap");
	  	if (tempmap.containsKey(id)) {
	  		String[] values = (String[])tempmap.get(id);
	  		request.setAttribute("Name", values[0]);
			request.setAttribute("ID", id);
			request.setAttribute("BuyPrice", values[1]);
			request.getRequestDispatcher("/payment.jsp").forward(request, response);
	  	}

	  	
		}
  }
  
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {
  	doGet(request, response);
  }
  
}