<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<%
	int numSkip = Integer.parseInt(request.getAttribute("numResultsToSkip").toString());
	int numReturn = Integer.parseInt(request.getAttribute("numResultsToReturn").toString());
	String query = request.getAttribute("query").toString();
%>

<html>
	<body>
		
		<form action="/eBay/search">
			
			Keywords: <input type="text" name="q" value="<%= query %>" /> 
			
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
		%>
		
		<p> Showing Results <%= numSkip+1 %> to <%= numSkip+count %></p>
		
		<%

			count = 0;
		
			for(SearchResult result : basicResults) 
			{
				if (count == 10)
				{
					break;
				}
				if (!result.getItemId().equals("-1"))
				{
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
					%> <a href="/eBay/search?q=<%= query %>&numResultsToSkip=<%= numSkip-10 %>&numResultsToReturn=11">Previous Page</a>
					<%
				}

				if (basicResults.length - 10 > 0)
				{
					%> <a href="/eBay/search?q=<%= query %>&numResultsToSkip=<%= numSkip+10 %>&numResultsToReturn=11">Next Page</a>
					<%
				}
		}
		%>	


	</body>
</html>
