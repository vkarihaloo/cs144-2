SELECT COUNT(*) FROM Users;

SELECT COUNT(*) FROM Users WHERE UsersID in (SELECT SellerID FROM Items) AND BINARY Location = 'New York';

SELECT COUNT(*) FROM (SELECT ItemID FROM Categories GROUP BY ItemID HAVING COUNT(Category) = 4) A;

SELECT ItemID, Number_of_Bids FROM Items WHERE Currently = (select max(Currently) FROM Items WHERE Ends > '2001-12-20 00:00:01' and Number_of_Bids > 0) and Number_of_Bids > 0;

SELECT COUNT(*) FROM Users WHERE UsersID in (SELECT SellerID FROM Items) AND rating > 1000;

SELECT COUNT(*) FROM (SELECT DISTINCT Items.SellerID FROM Items INNER JOIN Bids ON Items.SellerID = Bids.BidderID) A;

SELECT COUNT(*) FROM (SELECT DISTINCT Category FROM Categories WHERE ItemID in (SELECT DISTINCT ItemID FROM Bids WHERE Amount > 100.00)) A;
