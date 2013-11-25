<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<%
	int numSkip = Integer.parseInt(request.getAttribute("numResultsToSkip").toString());
	int numReturn = Integer.parseInt(request.getAttribute("numResultsToReturn").toString());
	String query = request.getAttribute("query").toString();
%>

<html>
	<head>
		<script type="text/javascript" src="autosuggest.js"></script>
		<link rel="stylesheet" type="text/css" href="autosuggest.css" />    
		<script type="text/javascript">

    	window.onload = function () {
    		
				var oTextbox = new AutoSuggestControl(document.getElementById("qbox"));        
      }
		</script>	
		
		
	</head>	
	<body>
		
		<form action="/eBay/search">
			
			Keywords: <input type="text" name="q" id="qbox" value="<%= query %>" autocomplete="off" style="width:500px;"/> 
			
			<input type="hidden" name="numResultsToSkip" value="0" />
			<input type="hidden" name="numResultsToReturn" value="11" />
			<input type="submit" /> <br /> 
		</form>
			


		<%


		SearchResult[] basicResults = (SearchResult[]) request.getAttribute("results");
		
		
		int count = basicResults.length;
		
		if (count > 10)
		{
			count = 10;
		}
		
		if (count > 0)
		{
			
			if (!basicResults[0].getItemId().equals("-1"))
			{
		%>
		
		<p> Showing Results <%= numSkip+1 %> to <%= numSkip+count %></p>
		
		<%
				count = 0;
				for (SearchResult result : basicResults) 
				{
					if (count == 10)
					{
						break;
					}
		%>
				<a href="/eBay/item?id=<%= result.getItemId() %>" > <%= result.getItemId() + ": " + result.getName() %> </a> <br /><br />
		<%
					count++;
				}		
			}
		%>
		
		<br />
		<% 
				if (numSkip > 0)
				{
					%> 
					<a href="/eBay/search?q=<%= query %>&numResultsToSkip=<%= numSkip-10 %>&numResultsToReturn=11">Previous Page</a>
					<%
				}

				if (basicResults.length - 10 > 0)
				{
					%> 
					<a href="/eBay/search?q=<%= query %>&numResultsToSkip=<%= numSkip+10 %>&numResultsToReturn=11">Next Page</a>
					<%
				}
		} 
		%>	


	</body>
</html>
