/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }

    public static String ellipsis(final String text)
 		{
 			if (text.length() > 4000)
    	{
    		return text.substring(0, 4000 - 3) + "...";
    	}
    	return text;
 		}
    
    public static String convertTime(String timeString)
    {
    	try
		  {
				SimpleDateFormat oldformat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
				SimpleDateFormat newformat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
			        
				timeString = newformat.format(oldformat.parse(timeString));
			}
			catch(ParseException pe)
			{
	    	System.out.println("ERROR: Cannot parse");
			} 
			
			return timeString;
    }
       
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */

				try 
        {
	        FileWriter userStream = new FileWriter("users-temp.csv", true);
	        FileWriter itemStream = new FileWriter("items.csv", true);
	        FileWriter categoryStream = new FileWriter("category.csv", true);
	        FileWriter bidStream = new FileWriter("bids.csv", true);
	        
					BufferedWriter userOut = new BufferedWriter(userStream);
					BufferedWriter itemOut = new BufferedWriter(itemStream);
					BufferedWriter categoryOut = new BufferedWriter(categoryStream);
					BufferedWriter bidOut = new BufferedWriter(bidStream);
	        
	        Element currItem = getElementByTagNameNR(doc.getDocumentElement(), "Item");
	        
	        while (currItem != null)
	        {
	        	//do stuff
      
		        String itemName = getElementTextByTagNameNR(currItem, "Name");
		        itemName = itemName.replace("\"","\\\"");
		        
		        int itemID = Integer.parseInt(currItem.getAttributes().item(0).getNodeValue());
		        String currVal = strip(getElementTextByTagNameNR(currItem, "Currently"));
						String firstBid = strip(getElementTextByTagNameNR(currItem, "First_Bid"));
						String buyPrice = strip(getElementTextByTagNameNR(currItem, "Buy_Price"));
						String numBids = getElementTextByTagNameNR(currItem, "Number_Of_Bids");
						
						String description = ellipsis(getElementTextByTagNameNR(currItem, "Description"));
						String startTime = convertTime(getElementTextByTagNameNR(currItem, "Started"));
						String endTime = convertTime(getElementTextByTagNameNR(currItem, "Ends"));
						
						Element userinfo = getElementByTagNameNR(currItem, "Seller");
						String username = userinfo.getAttributes().item(1).getNodeValue();
						String userRating = userinfo.getAttributes().item(0).getNodeValue();
						String location = getElementTextByTagNameNR(currItem, "Location");
						String country = getElementTextByTagNameNR(currItem, "Country");
								        
        		
        		Element[] categories = getElementsByTagNameNR(currItem, "Category");
		        
		        for (int i = 0; i < categories.length; i++)
		        {
		        	categoryOut.append(itemID + "," + "\"" + getElementText(categories[i]) + "\"" + '\n');
		        }
		        
		        Element Bids = getElementByTagNameNR(currItem, "Bids");
		        Element currBid = getElementByTagNameNR(Bids, "Bid");
		        
		        while (currBid != null)
		        {
		        	Element bidderInfo = getElementByTagNameNR(currBid, "Bidder");
							String bidder = bidderInfo.getAttributes().item(1).getNodeValue();
							String bidderRating = bidderInfo.getAttributes().item(0).getNodeValue();
							String b_location = getElementTextByTagNameNR(bidderInfo, "Location");
							String b_country = getElementTextByTagNameNR(bidderInfo, "Country");
							
							String bidTime = convertTime(getElementTextByTagNameNR(currBid, "Time"));
							String bidAmount = strip(getElementTextByTagNameNR(currBid, "Amount"));
							
							userOut.append(bidder + "," + "\"" + b_location + "\"," + "\"" + b_country + "\"," + bidderRating + '\n');
							
							bidOut.append(itemID + "," + bidder + "," + bidTime + "," + bidAmount + '\n');
							
		        	currBid = (Element) currBid.getNextSibling();
		        }
		        
		        
						itemOut.append(itemID + "," + username + "," + "\"" + itemName + "\"" + "," + currVal + "," + firstBid + "," + buyPrice + "," + numBids + "," + startTime + "," + endTime + "," + "\"" + description + "\"" + '\n');
						
						userOut.append(username + "," + "\"" + location + "\"," + "\"" + country + "\"," + userRating + '\n');
					
						currItem = (Element) currItem.getNextSibling();
	        }
	        
					userOut.close();
					itemOut.close();
					categoryOut.close();
					bidOut.close();
			  }
			  catch(IOException e)
				{
	     		e.printStackTrace();
				}        
        
        
        /**************************************************************/
        
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
