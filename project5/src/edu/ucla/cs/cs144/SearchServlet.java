package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

import edu.ucla.cs.cs144.AuctionSearchClient;
import edu.ucla.cs.cs144.SearchResult;
import org.apache.axis2.AxisFault;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        

        String query = request.getParameter("q");
				
				int numResultsToReturn = 0;
				int numResultsToSkip = 0;
				try
				{					
        	numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
        	numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
        }
        catch (Exception e)
        {
        }
        
				SearchResult[] basicResults = AuctionSearchClient.basicSearch(query, numResultsToSkip, numResultsToReturn);

				
				request.setAttribute("results", basicResults);
				request.setAttribute("query", query);
				request.setAttribute("numResultsToSkip", numResultsToSkip);
				request.setAttribute("numResultsToReturn", numResultsToReturn);
				
    		request.getRequestDispatcher("/search.jsp").forward(request, response);
    }
}
