<% String secureURL = "https://" + request.getServerName() + ":8443" + request.getContextPath() + "/buy"; %>

<html>

	<body>
		<h1>Credit Card Input Page</h1>
		
		<form method="post" action="<%= secureURL %>">
    	<p>Item ID: <%= request.getAttribute("ID") %></p> 
    	<p>Item Name: <%= request.getAttribute("Name") %></p> 
    	<p>Buy Price: <%= request.getAttribute("BuyPrice") %></p> <br />
    	
        <input type="hidden" name="id" value="<%= request.getAttribute("ID") %>" />
    	<input type="hidden" name="action" value="confirm" />
    	Enter Credit Card #: <input type="text" name="cc" /> <br />
    	
    	<input type="submit" value="Submit" />
    </form>
		
	</body>


</html>