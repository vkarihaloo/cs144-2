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

/*java Timestamp class */
import java.sql.Timestamp;
import java.text.StringCharacterIterator;
import java.text.CharacterIterator;

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
  	if (numResultsToReturn == 0)
		{
			numResultsToReturn = 999999;
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
		
		if (numResultsToReturn == 0)
		{
			numResultsToReturn = 999999;		//big number since i dont know how to get sizeof ResultSet
		}
		
		ArrayList<SearchResult> temp = new ArrayList<SearchResult>();
		
		int skipcounter = 1;
		
		try
		{
			while (rs.next()) 
			{
				
				if (numResultsToReturn <= 0)
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
		
		if (numResultsToSkip > hits.length())
		{
			return new SearchResult[0];
		}
		
		if (numResultsToReturn == 0)
		{
			numResultsToReturn = hits.length();
		}
		
		ArrayList<SearchResult> temp = new ArrayList<SearchResult>();
		
    
    for(int i = numResultsToSkip; i < hits.length(); i++) 
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
   		temp.add(new SearchResult(doc.get("ItemId"), doc.get("ItemName")));
   		numResultsToReturn--;
			
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
		String xmlString = "<Item ItemID=\"" + itemId + "\">" + "\n";

		String SellerID, Name, Currently, First_Bid, Buy_Price, Number_of_Bids, Started, Ends, Description, Rating, Location, Country;
        SellerID = "";
        Name = "";
        Currently = "";
        First_Bid = "";
        Buy_Price = "";
        Number_of_Bids = "";
        Started =  "";
        Ends = "";
        Description = "";
        Rating = "";
        Location = "";
        Country = "";

        try{
		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT * FROM Items WHERE ItemID ='" + itemId + "'");

		while (rs.next()) {
			SellerID = rs.getString("SellerID");
			Name = rs.getString("Name");
			Currently = String.format("%.2f", rs.getFloat("Currently"));
			First_Bid = String.format("%.2f", rs.getFloat("First_Bid"));
			/*if (rs.getFloat("Buy_Price") != null){
				Buy_Price = String.format("%.2f", rs.getFloat("Buy_Price"));
			}*/
			Buy_Price = String.format("%.2f", rs.getFloat("Buy_Price"));

			Number_of_Bids = Integer.toString(rs.getInt("Number_of_Bids"));
			Started = rs.getTimestamp("Started").toString();
			Ends = rs.getTimestamp("Ends").toString();
			Description = rs.getString("Description").toString();
		}

		xmlString += "	<Name>" + forXML(Name) + "</Name>" + "\n";

		rs = stmt.executeQuery("SELECT * FROM Categories WHERE ItemID='" + itemId + "'");

		while (rs.next()) {
			xmlString += "	<Category>" + forXML(rs.getString("Category")) + "</Category>" +"\n";
		}

		xmlString += "	<Currently>" + "$" + forXML(Currently) + "<Currently>" + "\n";

		if (!Buy_Price.equals("0.00")) {
			xmlString += "	<Buy_Price>" + "$" + forXML(Buy_Price) + "<Buy_Price>" + "\n";
		} 

		xmlString += "	<First_Bid>" + "$" + forXML(First_Bid) + "</First_Bid>" + "\n";

		xmlString += "	<Number_of_Bids>" + forXML(Number_of_Bids) + "</Number_of_Bids>" + "\n";

		xmlString += "	<Bids>" + "\n";

		rs = stmt.executeQuery("SELECT * FROM Bids WHERE ItemID='" + itemId + "'");

		while (rs.next()) {
			xmlString = BidXMLHelper(xmlString, rs.getString("BidderID"), rs.getTimestamp("Time").toString(),String.format("%.2f", rs.getFloat("Amount")));
		}

		xmlString += "	</Bids>" + "\n";

		rs = stmt.executeQuery("SELECT * FROM Users WHERE UserID='" + SellerID + "'");

		while (rs.next()) {

		xmlString += "	<Location>" + forXML(rs.getString("Location")) + "<Location>" + "\n";

		xmlString += "	<Country>" + forXML(rs.getString("Country")) + "<Country>" + "\n";

		Rating = Integer.toString(rs.getInt("Rating"));
		}

		xmlString += "	<Started>" + forXML(convertTime(Started)) + "<Started>" + "\n";

		xmlString += "	<Ends>" + forXML(convertTime(Ends)) + "</Ends>" + "\n";

		

		xmlString += "	<Seller " + "UserID=\"" + forXML(SellerID) + "\" " +
			"Rating=\"" + forXML(Rating) + "\"/>" + "\n";

		xmlString += "	<Description>" + forXML(Description) + "</Description>" + "\n";

		xmlString += "</Item>";
		


        } catch (SQLException ex) {};

        return xmlString;
	}

	public String BidXMLHelper(String xmlString, String BidderID, String Time, String Amount) {

		String Rating = "";
		String Location ="";
		String Country = "";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Users WHERE UserID ='" + BidderID + "'");

			while (rs.next()) {
			Rating = Integer.toString(rs.getInt("Rating"));
			Location = rs.getString("Location");
			Country = rs.getString("Country");
		}

			xmlString += "		<Bid>" + "\n";
			xmlString += "			<Bidder " + "UserID=\"" + forXML(BidderID) + "\" " +
			"Rating=\"" + forXML(Rating) + "\">" + "\n";
			xmlString += "				<Location>" + forXML(Location) + "</Location>" + "\n";
			xmlString += "				<Country>" + forXML(Country) + "</Country>" + "\n";
			xmlString += "			</Bidder>" + "\n";
			xmlString += "			<Time>" + forXML(convertTime(Time)) + "</Time>" + "\n";
			xmlString += "			<Amount>" + "$" + forXML(Amount) + "</Amount>" + "\n";
			xmlString += "		</Bid>" + "\n";

		} catch (SQLException ex) {};

		return xmlString;
	}
	
	public String echo(String message) {
		return message;
	}

	public static String forXML(String aText){
    final StringBuilder result = new StringBuilder();
    final StringCharacterIterator iterator = new StringCharacterIterator(aText);
    char character =  iterator.current();
    while (character != CharacterIterator.DONE ){
      if (character == '<') {
        result.append("&lt;");
      }
      else if (character == '>') {
        result.append("&gt;");
      }
      else if (character == '\"') {
        result.append("&quot;");
      }
      else if (character == '\'') {
        result.append("&apos;");
      }
      else if (character == '&') {
         result.append("&amp;");
      }
      else if (character == '\\') {
      	result.append("\\");
      }
      else {
        //the char is not a special one
        //add it to the result as is
        result.append(character);
      }
      character = iterator.next();
    }
    return result.toString();
  }
  
  public static String convertTime(String timeString)
    {
    	try
		  {
				SimpleDateFormat xmlformat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
				SimpleDateFormat dbformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        
				timeString = xmlformat.format(dbformat.parse(timeString));
			}
			catch(Exception pe)
			{
	    	System.out.println("ERROR: Cannot parse");
			} 
			
			return timeString;
    }

}
