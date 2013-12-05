package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ucla.cs.cs144.AuctionSearchClient;
import org.apache.axis2.AxisFault;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        
        String queryId = request.getParameter("id");
        String xmlString = AuctionSearchClient.getXMLDataForItemId(queryId);
				//out.println("results: " + reply);
				
				request.setAttribute("results", xmlString);
    		request.getRequestDispatcher("/getItem.jsp").forward(request, response);

    }
}
