<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory" %>
<%@ page import="javax.xml.parsers.DocumentBuilder" %>

<form action="/eBay/item">
	Enter Id: <input type="text" name="id" /> <br />
	<input type="submit" />
</form>
		
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

<p>Location: <%= BidLocation[BidLocation.length-1] %></p>
<p>Country: <%= BidCountry[BidCountry.length-1] %></p>

<% String geoLocation = BidLocation[BidLocation.length-1] + ", " + BidCountry[BidCountry.length-1]; %>
<%= geoLocation %>

<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
<style type="text/css"> 
  html { height: 100% } 
  body { height: 100%; margin: 0px; padding: 0px } 
  #map_canvas { height: 100% } 
</style> 


<script type="text/javascript" 
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDvrGK52FxBndfyWYPBfV0LoQqMss2xMjE&sensor=false"> 
</script> 
<script type="text/javascript"> 
  function initialize() { 

  	geocoder = new google.maps.Geocoder();
  	var address = "<%=geoLocation%>";
  	alert(address);
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
        alert("Geocode was not successful for the following reason: " + status);
      }
    });

    
  } 

</script> 

<body onload="initialize()"> 
  <div id="map_canvas" style="width:50%; height:50%"></div> 
</body>
