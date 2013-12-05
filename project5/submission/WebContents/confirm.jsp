

<html>

	<body>
		<h1>Confirmation Page</h1>
		
    <p>Item ID: <%= request.getAttribute("ID") %></p> 
    <p>Item Name: <%= request.getAttribute("Name") %></p> 
   	<p>Buy Price: <%= request.getAttribute("BuyPrice") %></p> 
    <p>Credit Card: <%= request.getAttribute("CC") %></p>
    <p>Time: <%= request.getAttribute("Time") %></p>
    	
    	
		
	</body>


</html>