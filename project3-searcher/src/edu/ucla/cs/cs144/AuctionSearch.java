package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import java.util.HashMap;
import java.util.ArrayList;

import java.util.Date;
import java.util.Iterator;
import java.text.SimpleDateFormat;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchConstraint;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
         * Your code will need to reference the directory which contains your
	 * Lucene index files.  Make sure to read the environment variable 
         * $LUCENE_INDEX with System.getenv() to build the appropriate path.
	 *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	private IndexSearcher searcher = null;
	private Connection conn = null;
	
	public AuctionSearch()
	{
		try
		{
			searcher = new IndexSearcher(System.getenv("LUCENE_INDEX"));
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		
		try 
		{
	  	conn = DbManager.getConnection(true);
		} 
		catch (SQLException ex) 
		{
	    System.out.println(ex);
		}
		
	}
	

	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		// TODO: Your code here!
		
		Hits hits = getLuceneResults(query);

    
    return processResults(hits, numResultsToSkip, numResultsToReturn);
	}

	public SearchResult[] advancedSearch(SearchConstraint[] constraints, 
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		
		String SQLItemConstraints = null;
		
		String SQLBidderConstraints = null;
		
		String LuceneConstraints = null;

		
		for (int i = 0; i < constraints.length; i++)
		{
			String currField = constraints[i].getFieldName();
			String currValue = constraints[i].getValue();
			
			if (currField.equals("EndTime"))
			{
				try
			  {
					SimpleDateFormat oldformat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
					SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				        
					currValue = newformat.format(oldformat.parse(currValue));
				}
				catch(Exception e)
				{
		    	System.out.println("ERROR: Cannot parse");
				}
			}
			
			if (currField.equals("ItemName") || currField.equals("Category") || currField.equals("Description"))
			{
				//lucene search
				if (LuceneConstraints == null)
				{
					LuceneConstraints = currField + ":" + currValue;
				}
				else
				{
					LuceneConstraints += " AND " + currField + ":" + currValue;
				}
			}
			else if (currField.equals("SellerId") || currField.equals("BuyPrice") || currField.equals("EndTime"))
			{
				//mysql items table
				if (SQLItemConstraints == null)
				{
					SQLItemConstraints = getSQLField(currField) + "='" + currValue + "'";
				}
				else
				{
					SQLItemConstraints += " AND " + getSQLField(currField) + "='" + currValue + "'";
				}
				
			}
			else if (currField.equals("BidderId"))
			{
				//mysql bids table
				if (SQLBidderConstraints == null)
				{
					SQLBidderConstraints = getSQLField(currField) + "='" + currValue + "'";
				}
				else
				{
					SQLBidderConstraints += " AND " + getSQLField(currField) + "='" + currValue + "'";
				}
			}
		}
		

		
		String fullSQLQuery = null;
		if (SQLItemConstraints != null)
		{
			if (SQLBidderConstraints == null)
			{
				fullSQLQuery = "SELECT ItemID, Name from Items WHERE " + SQLItemConstraints;
			}
			else
			{
				fullSQLQuery = "SELECT ItemID, Name from Items WHERE " + SQLItemConstraints + " AND ItemID in (SELECT ItemID from Bids WHERE " + SQLBidderConstraints + ")";
			}
		}


		ResultSet SQLResults = getSQLResults(fullSQLQuery);
		Hits luceneResults = getLuceneResults(LuceneConstraints);
		
		if (SQLResults != null || luceneResults.length() > 0)
		{
			if (SQLResults == null)
			{
				//lucene only
				return processResults(luceneResults, numResultsToSkip, numResultsToReturn);
			}
			else if (luceneResults == null)
			{
				//mysql only
				return processResults(SQLResults, numResultsToSkip, numResultsToReturn);
			}
			else
			{
				//combine lucene and mysql results
				return combineResults(SQLResults, luceneResults, numResultsToSkip, numResultsToReturn);
			}
		}
		
		return new SearchResult[0];		//default
	}
  
  private SearchResult[] combineResults(ResultSet rs, Hits hits, int numResultsToSkip, int numResultsToReturn)
  {
  	if (numResultsToSkip >= numResultsToReturn)
		{
			return new SearchResult[0];
		}
		
  	HashMap<Integer, Integer> IDList = new HashMap<Integer, Integer>();
  	
  	try
  	{
  		while (rs.next()) 
			{
				IDList.put(rs.getInt("ItemID"), 1);
			}
  	}
  	catch (Exception e)
  	{
  		System.out.println(e);
  	}
  	
  	ArrayList<SearchResult> temp = new ArrayList<SearchResult>();
  	
  	for (int i = 0; i < hits.length(); i++)
  	{
  		if (numResultsToReturn <= 0)
  		{
  			break;
  		}
  		Document doc = null;
    	try
    	{
   			doc = hits.doc(i);
   		}
   		catch (Exception e)
   		{
   			System.out.println(e);
   		}
   		
   		if (IDList.containsKey(Integer.parseInt(doc.get("ItemId"))))
   		{

   			if (numResultsToSkip > 0)
   			{
   				numResultsToSkip--;
   				continue;
   			}
   			
   			temp.add(new SearchResult(doc.get("ItemId"), doc.get("ItemName")));
   			numResultsToReturn--;
   			
   		}
  	}
  	SearchResult[] finalResults = new SearchResult[temp.size()];
		finalResults = temp.toArray(finalResults);
		
		return finalResults;
  }
  
	private SearchResult[] processResults(ResultSet rs, int numResultsToSkip, int numResultsToReturn)
	{
		if (numResultsToSkip >= numResultsToReturn)
		{
			return new SearchResult[0];
		}
		
		ArrayList<SearchResult> temp = new ArrayList<SearchResult>();
		
		int skipcounter = 1;
		
		try
		{
			while (rs.next()) 
			{
				if (numResultsToReturn == 0)
				{
					break;
				}
				if (skipcounter < numResultsToSkip)
				{
					skipcounter++;
					continue;
				}
				temp.add(new SearchResult(Integer.toString(rs.getInt("ItemID")), rs.getString("Name")));
				numResultsToReturn--;
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		
		SearchResult[] finalResults = new SearchResult[temp.size()];
		finalResults = temp.toArray(finalResults);
		
		return finalResults;
		
	}
	
	private SearchResult[] processResults(Hits hits, int numResultsToSkip, int numResultsToReturn)
	{
		if (numResultsToSkip >= numResultsToReturn)
		{
			return new SearchResult[0];
		}
		
		ArrayList<SearchResult> temp = new ArrayList<SearchResult>();
		
    
    for(int i = numResultsToSkip; i < hits.length(); i++) 
    {
    	Document doc = null;
    	try
    	{
   			doc = hits.doc(i);
   		}
   		catch (Exception e)
   		{
   			System.out.println(e);
   		}
   		temp.add(new SearchResult(doc.get("ItemId"), doc.get("ItemName")));
			
 		}
		SearchResult[] finalResults = new SearchResult[temp.size()];
		finalResults = temp.toArray(finalResults);
		
		return finalResults;
	}
	
	private Hits getLuceneResults(String query)
	{
		if (query == null)
		{
			return null;
		}
		
		QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		
		Query parsedQuery = null;
		
		
		try
		{
			parsedQuery = parser.parse(query);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		
		Hits hits = null;
		
		try
		{
			hits = searcher.search(parsedQuery);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		
		return hits;
	}
	
	private ResultSet getSQLResults(String query) 
	{
		if (query == null)
		{
			return null;
		}
		Statement stmt = null;  
		ResultSet rs = null;
	  try 
	  {
	  	stmt = conn.createStatement();
	 		rs = stmt.executeQuery(query);
	 	}
	 	catch (SQLException ex) 
		{
    	System.err.println("SQLException: " + ex.getMessage());
		} 
	 	
	 	return rs;
	}
	
	private String getSQLField(String field)
	{
		//gay function

		if (field.equals("SellerId"))
		{
			field = "SellerID";
		}
		if (field.equals("BuyPrice"))
		{
			field = "Buy_Price";
		}
		if (field.equals("BuyPrice"))
		{
			field = "Buy_Price";
		}
		if (field.equals("EndTime"))
		{
			field = "Ends";
		}
		if (field.equals("BidderId"))
		{
			field = "BidderID";
		}
	
		return field;
	}
	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return null;
	}
	
	public String echo(String message) {
		return message;
	}

}
