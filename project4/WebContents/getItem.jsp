<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory" %>
<%@ page import="javax.xml.parsers.DocumentBuilder" %>


<p>Name: <%= request.getAttribute("Name") %></p>



<p>Categories: 
<% String[] Categories = (String[]) request.getAttribute("Categories");
		
		for(int i = 0; i < Categories.length; i++) 
		{
			if (i == 0) {
		%>
		<%=Categories[i]%>
		<%
			}
			else {
			%>
			<%= ", " + Categories[i]%>

<%
		}
		}		
		%>
</p>
<p>Currently: <%= request.getAttribute("Currently") %></p>
<p>Buy Price: <%= request.getAttribute("Buy_Price") %></p>
<p>First Bid: <%= request.getAttribute("First_Bid") %></p>
<p>Number of Bids: <%= request.getAttribute("Number_of_Bids") %></p>
<p>Started: <%= request.getAttribute("Started") %></p>
<p>Seller UserID: <%= request.getAttribute("Seller_UID") %></p>
<p>Seller Rating: <%= request.getAttribute("Seller_Rating") %></p>
<p>Description: <%= request.getAttribute("Description") %></p>

<p>
<% String[] BidUID = (String[]) request.getAttribute("BidUID");
	String[] BidRating = (String[]) request.getAttribute("BidRating");
	String[] BidLocation = (String[]) request.getAttribute("BidLocation");
	String[] BidCountry = (String[]) request.getAttribute("BidCountry");
	String[] BidTime = (String[]) request.getAttribute("BidTime");
	String[] BidAmount = (String[]) request.getAttribute("BidAmount");
		
		for(int i = 0; i < BidUID.length; i++) 
		{
		%>
		<%= "Bid #" + (i+1) %> <br />
		<%= "Bidder UserID: " + BidUID[i]%> <br />
		<%= "Bidder Rating: " + BidRating[i]%> <br />
		<%= "Bidder Location: " + BidLocation[i] %> <br />
		<%= "Bidder Country: " + BidCountry[i] %> <br />
		<%= "Bid Time: " + BidTime[i] %> <br />
		<%= "Bid Amount: " + BidAmount[i] %> <br /> 
		<br />
		<%
		}		
		%>
</p>
