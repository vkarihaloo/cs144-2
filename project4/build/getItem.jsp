<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory" %>
<%@ page import="javax.xml.parsers.DocumentBuilder" %>


<html>
<head>
	<link rel="stylesheet" type="text/css" href="getItem.css" />   
</head>

<body onload="initialize()"> 
<form action="/eBay/item">
	Search An ItemId: <input type="text" name="id" /> <br />
	<input type="submit" />
</form>

<hr>

<div id="leftcolumn">		
<h1><%= request.getAttribute("Name") %></h1>

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

<% if (request.getAttribute("Buy_Price") != "")
{
%>
	<p>Buy Price: <%= request.getAttribute("Buy_Price") %></p>
<%
} 
%>

<p>First Bid: <%= request.getAttribute("First_Bid") %></p>
<p>Number of Bids: <%= request.getAttribute("Number_of_Bids") %></p>
<p>Started: <%= request.getAttribute("Started") %></p>
<p>Seller UserID: <%= request.getAttribute("Seller_UID") %></p>
<p>Seller Rating: <%= request.getAttribute("Seller_Rating") %></p>

<% if (request.getAttribute("Description") != "")
{
%>
	<p>Description: <%= request.getAttribute("Description") %></p>
<%
} 
%>
<p>
<% String[] BidUID = (String[]) request.getAttribute("BidUID");
	String[] BidRating = (String[]) request.getAttribute("BidRating");
	String[] BidLocation = (String[]) request.getAttribute("BidLocation");
	String[] BidCountry = (String[]) request.getAttribute("BidCountry");
	String[] BidTime = (String[]) request.getAttribute("BidTime");
	String[] BidAmount = (String[]) request.getAttribute("BidAmount");
		
		for(int i = 0; i < BidUID.length; i++) 
		{
		if (i == 0) {
		%>
		<table border="1" cellpadding="5">
			<tr>
				<th>Bid #</th>
				<th>UserID </th>
				<th>Rating </th>
				<th>Location </th>
				<th>Country </th>
				<th>Time </th>
				<th>Amount </th>
			</tr>
		<%
	}
		%>
		<tr> 
			<td><%= i+1 %></td> 
			<td><%= BidUID[i]%></td>
			<td><%= BidRating[i]%></td>
			<td><%= BidLocation[i] %></td>
			<td><%= BidCountry[i] %></td>
			<td><%= BidTime[i] %></td>
			<td><%= BidAmount[i] %></td>
		</tr>
		<% if (i == (BidUID.length-1)) { %>
		</table>
	
		<%
		}
		%>	

		<%
		}		
		%>
</p>
</div>

<div id="rightcolumn">
<p>Location: <%= BidLocation[BidLocation.length-1] %></p>
<p>Country: <%= BidCountry[BidCountry.length-1] %></p>

<% String geoLocation = BidLocation[BidLocation.length-1] + ", " + BidCountry[BidCountry.length-1]; %>

<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 

<script type="text/javascript" 
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDvrGK52FxBndfyWYPBfV0LoQqMss2xMjE&sensor=false"> 
</script> 
<script type="text/javascript"> 
  function initialize() { 

  	geocoder = new google.maps.Geocoder();
  	var address = "<%=geoLocation%>";
    geocoder.geocode( { 'address': address}, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
      	var myOptions = {
                zoom: 14,
                center: results[0].geometry.location,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
      	var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions); 
        map.setCenter(results[0].geometry.location);
        var marker = new google.maps.Marker({
            map: map,
            position: results[0].geometry.location
        });
      } else {
        //alert("Geocode was not successful for the following reason: " + status);
      }
    });

    
  } 

</script> 
<div id="map_canvas" style="width:500px; height:400px"></div> 
</div>

</div>
</body>
</html>
