package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.io.IOException;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

//imports for search
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
/*****************/

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
 
    public void rebuildIndexes() throws Exception {

        Connection conn = null;
        IndexWriter indexWriter = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
		try 
		{
			indexWriter = new IndexWriter(System.getenv("LUCENE_INDEX"), new StandardAnalyzer(), true);
		}
		catch (IOException ex)
		{
			System.out.println(ex);
		}

	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */
	
	 
	 	ResultSet rs = getAllItems(conn);
		if (rs != null)
		{
			while (rs.next()) 
			{
				Document doc = new Document();
  			doc.add(new Field("id", Integer.toString(rs.getInt("ItemId")), Field.Store.YES, Field.Index.NO));
  			doc.add(new Field("name", rs.getString("Name"), Field.Store.YES, Field.Index.TOKENIZED));
  			doc.add(new Field("description", rs.getString("Description"), Field.Store.YES, Field.Index.TOKENIZED));
 				
 				String itemCats = getCategories(conn, rs.getInt("ItemId"));
 				
 				String fullSearchableText = rs.getString("Name") + " " + rs.getString("Description") + " " + itemCats;

  			doc.add(new Field("content", fullSearchableText, Field.Store.NO, Field.Index.TOKENIZED));
  			
  			indexWriter.addDocument(doc);


			} 
		}

	 
	 try
	 {
		indexWriter.close();
	 }
	 catch (IOException ex)
	 {
	 	System.out.println(ex);
	 }

        // close the database connection
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
    }    

		private static ResultSet getAllItems(Connection conn)
		{
			Statement stmt = null;  
			ResultSet rs = null;
	  	try 
	  	{
	  		stmt = conn.createStatement();
	 			rs = stmt.executeQuery("SELECT ItemId, Name, Description FROM Items");
	 		}
	 		catch (SQLException ex) 
			{
    		System.err.println("SQLException: " + ex.getMessage());
			} 
			return rs;
		}
		
		private static String getCategories(Connection conn, int ItemId)
		{
			Statement stmt = null;  
			String cats = "";
	  	try 
	  	{
	  		stmt = conn.createStatement();
	 			ResultSet rs = stmt.executeQuery("SELECT group_concat(Category separator ' ') as ItemCats FROM Categories WHERE ItemId = '" + ItemId + "'");
	 			
 				if (rs.next())
 				{
 					cats = rs.getString("ItemCats");
 		  	}
	 		}
	 		catch (SQLException ex) 
			{
    		System.err.println("SQLException: " + ex.getMessage());
			} 
 		  
			return cats;
		}
		
		
    public static void main(String args[]) throws Exception {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
        
        /* TESTING THE SEARCH */
        /*IndexSearcher searcher = new IndexSearcher(System.getenv("LUCENE_INDEX"));
   			QueryParser parser = new QueryParser("content", new StandardAnalyzer());


    		Query query = parser.parse("superman");
    		Hits hits = searcher.search(query);
        System.out.println("Results found: " + hits.length());
        
        for(int i = 0; i < hits.length(); i++) 
        {
   				Document doc = hits.doc(i);
   				String hotelName = doc.get("name");

 				}
        */
        
        
        
    }   
}
