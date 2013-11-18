<%@ page import="edu.ucla.cs.cs144.SearchResult" %>

<%


		SearchResult[] basicResults = (SearchResult[]) request.getAttribute("results");
	
		for(SearchResult result : basicResults) 
		{
		%>
			<%= result.getItemId() + ": " + result.getName() %> <br /><br />
		<%
		}
		
		
%>
		
		
