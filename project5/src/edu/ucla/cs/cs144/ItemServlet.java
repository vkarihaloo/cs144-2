package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ucla.cs.cs144.AuctionSearchClient;
import org.apache.axis2.AxisFault;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import java.io.StringReader;

import javax.servlet.http.HttpSession;

import java.util.HashMap;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        

        try{

				String queryId = request.getParameter("id");
        String xmlString = AuctionSearchClient.getXMLDataForItemId(queryId);
				//out.println("results: " + reply);
				String Name = "";
				String Currently = ""; 
				String Buy_Price = ""; 
				String First_Bid = ""; 
				String Number_of_Bids = "";
				String Started = "";
				String Seller_UID = "";
				String Seller_Rating = ""; 
				String Description = "";
                String Ends = "";
				NodeList nlist;
				NodeList sellerlist;
				String[] Categories;
				String[] BidUID;
				String[] BidRating;
				String[] BidLocation;
				String[] BidCountry;
				String[] BidTime;
				String[] BidAmount;

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder builder = factory.newDocumentBuilder();
   			InputSource is = new InputSource(new StringReader(xmlString));
    		Document doc = builder.parse(is);
    		//temp = doc.getDocumentElement().getNodeName();
    		Name = doc.getElementsByTagName("Name").item(0).getTextContent();
    		Currently = doc.getElementsByTagName("Currently").item(0).getTextContent();
    		
    		if (doc.getElementsByTagName("Buy_Price").getLength() != 0) {
    			Buy_Price = doc.getElementsByTagName("Buy_Price").item(0).getTextContent();
    		}
    		
    		First_Bid = doc.getElementsByTagName("First_Bid").item(0).getTextContent();
    		Number_of_Bids = doc.getElementsByTagName("Number_of_Bids").item(0).getTextContent();
    		Started = doc.getElementsByTagName("Started").item(0).getTextContent();
            Ends = doc.getElementsByTagName("Ends").item(0).getTextContent();
    		NodeList SellerList = doc.getElementsByTagName("Seller");
    		Node sellernode = SellerList.item(0);
    		Element sellerelement = (Element) sellernode;

    		Seller_UID = sellerelement.getAttribute("UserID");
    		Seller_Rating = sellerelement.getAttribute("Rating");
    		Description = doc.getElementsByTagName("Description").item(0).getTextContent();

    		NodeList categoryList = doc.getElementsByTagName("Category");

    		Categories = new String[categoryList.getLength()];

    		for (int i = 0; i < categoryList.getLength(); i++) {
    			Categories[i] = categoryList.item(i).getTextContent();
    		}

    		NodeList tempList = doc.getElementsByTagName("Bidder");
    		Node bidNode;
    		BidUID = new String[tempList.getLength()];
    		BidRating = new String[tempList.getLength()];
    		Element bidderElement;
    		for (int i = 0; i < tempList.getLength(); i++) {
    			bidNode = tempList.item(i);
    			bidderElement = (Element) bidNode;
    			BidUID[i] = bidderElement.getAttribute("UserID");
    			BidRating[i] = bidderElement.getAttribute("Rating");
    		}

    		tempList = doc.getElementsByTagName("Location");
    		BidLocation = new String[tempList.getLength()];
    		for (int i = 0; i < tempList.getLength(); i++) {
    			BidLocation[i] = tempList.item(i).getTextContent();
    		}

    		tempList = doc.getElementsByTagName("Country");
    		BidCountry = new String[tempList.getLength()];
    		for (int i = 0; i < tempList.getLength(); i++) {
    			BidCountry[i] = tempList.item(i).getTextContent();
    		}

    		tempList = doc.getElementsByTagName("Time");
    		BidTime = new String[tempList.getLength()];
    		for (int i = 0; i < tempList.getLength(); i++) {
    			BidTime[i] = tempList.item(i).getTextContent();
    		}

    		tempList = doc.getElementsByTagName("Amount");
    		BidAmount = new String[tempList.getLength()];
    		for (int i = 0; i < tempList.getLength(); i++) {
    			BidAmount[i] = tempList.item(i).getTextContent();
    		}

            request.setAttribute("ItemID", queryId);
    		request.setAttribute("Name", Name);
				request.setAttribute("Currently", Currently);
				request.setAttribute("Buy_Price", Buy_Price);
				request.setAttribute("First_Bid", First_Bid);
				request.setAttribute("Number_of_Bids", Number_of_Bids);
				request.setAttribute("Started", Started);
                request.setAttribute("Ends", Ends);
				request.setAttribute("Seller_UID", Seller_UID);
				request.setAttribute("Seller_Rating", Seller_Rating);
				request.setAttribute("Description", Description);
				request.setAttribute("Categories", Categories);
				request.setAttribute("BidUID", BidUID);
				request.setAttribute("BidRating", BidRating);
				request.setAttribute("BidLocation", BidLocation);
				request.setAttribute("BidCountry", BidCountry);
				request.setAttribute("BidTime", BidTime);
				request.setAttribute("BidAmount", BidAmount);
				

				if (!Buy_Price.equals(""))
				{
					//add item info to the session
                HttpSession session = request.getSession(true);

                HashMap<String, String[]> map;

                if (session.getAttribute("itemMap") == null) {
                    map = new HashMap<String, String[]>();
                }
                else {
                    map = (HashMap<String, String[]>)session.getAttribute("itemMap");
                }

                if (!map.containsKey(queryId)) {
                    String[] mapvalue = new String[2];
                    mapvalue[0] = Name;
                    mapvalue[1] = Buy_Price;
                    map.put(queryId, mapvalue);
                    session.setAttribute("itemMap", map);
                    }
				}
				
    		request.getRequestDispatcher("/getItem.jsp").forward(request, response);

    		/*
    		for (int i=0; i < nList.getLength(); i++) {
    			Node nNode = nList.item(i);
    			Element eElemeent = (Element) nNode;
    			temp = eElement.getElementsByTagName()
    		}
    		*/
    	}
    	catch (Exception e) {}
    		
				

    }
}
