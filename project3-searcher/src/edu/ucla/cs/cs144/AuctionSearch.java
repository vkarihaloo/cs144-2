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

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

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
			hits = searcher.search(parsedQuery, new Sort(new SortField("id", SortField.INT)));
		}
		catch (Exception e)
		{
			System.out.println(e);
		}

    
    if (numResultsToReturn > hits.length())
    {
    	numResultsToReturn = hits.length();
    }
    
    SearchResult[] results = new SearchResult[numResultsToReturn]; 
    
    int count = 0;
    
    for(int i = numResultsToSkip; i < numResultsToSkip + numResultsToReturn; i++) 
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
   		SearchResult currResult = new SearchResult(doc.get("id"), doc.get("name"));
			
			results[count] = currResult;
			count++;
 		}
		return results;
	}

	public SearchResult[] advancedSearch(SearchConstraint[] constraints, 
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		
		String SQLItemConstraints = "";
		
		String SQLBidderConstraints = "";
		
		String LuceneConstraints = "";

		
		for (int i = 0; i < constraints.length; i++)
		{
			String currField = constraints[i].getFieldName();
			String currValue = constraints[i].getValue();
			
			if (currField.equals("ItemName") || currField.equals("Category") || currField.equals("Description"))
			{
				//lucene search
				if (LuceneConstraints.equals(""))
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
				if (SQLItemConstraints.equals(""))
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
				if (SQLBidderConstraints.equals(""))
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
		if (SQLBidderConstraints.equals(""))
		{
			fullSQLQuery = "SELECT ItemID, Name from Items WHERE " + SQLItemConstraints;
		}
		else
		{
			fullSQLQuery = "SELECT ItemID, Name from Items WHERE " + SQLItemConstraints + " AND ItemID in (SELECT ItemID from Bids WHERE " + SQLBidderConstraints;
		}
		
		
		System.out.println("Lucene: " + LuceneConstraints);
		System.out.println("MySQL: " + fullSQLQuery);
		
		return new SearchResult[0];
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
