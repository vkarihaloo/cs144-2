<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<%! @SuppressWarnings("unchecked") %>

<html>
	<body>
		
		<form action="/eBay/search">
			
			Keywords: <input type="text" name="q" value='<%= request.getAttribute("query") %>' /> 
			
			<input type="hidden" name="numResultsToSkip" value='<%= request.getAttribute("numResultsToSkip") %>' />
			<input type="hidden" name="numResultsToReturn" value='<%= request.getAttribute("numResultsToReturn") %>' />
			<input type="submit" /> <br /> <br />
		
			


		<%


		SearchResult[] basicResults = (SearchResult[]) request.getAttribute("results");
		
		for(SearchResult result : basicResults) 
		{
		%>
			<%= result.getItemId() + ": " + result.getName() %> <br /><br />
		<%
		}		
		%>

		</form>
	</body>
</html>
