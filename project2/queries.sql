1. SELECT COUNT(*) FROM Users;

2. SELECT COUNT(*) FROM Users WHERE UsersID in (SELECT SellerID FROM Items) AND BINARY Location = 'New York';

3. SELECT COUNT(*) FROM (SELECT ItemID FROM Categories GROUP BY ItemID HAVING COUNT(Category) = 4) A;

4. SELECT ItemID, Number_of_Bids FROM Items WHERE Currently = (select max(Currently) FROM Items WHERE Ends > '2001-12-20 00:00:01' and Number_of_Bids > 0) and Number_of_Bids > 0;

5. SELECT COUNT(*) FROM Users WHERE UsersID in (SELECT SellerID FROM Items) AND rating > 1000;

6. SELECT COUNT(*) FROM (SELECT DISTINCT Items.SellerID FROM Items INNER JOIN Bids ON Items.SellerID = Bids.BidderID) A;
