

CREATE TABLE Users (
	UsersID VARCHAR(50) PRIMARY KEY,
	Location VARCHAR(100),
	Country VARCHAR(100),
	Rating INT
	);

CREATE TABLE Items (
	ItemID INT PRIMARY KEY, 
	SellerID VARCHAR(50),
	Name VARCHAR(100), 
	Currently DECIMAL(8,2), 
	First_Bid DECIMAL(8,2), 
	Buy_Price DECIMAL(8,2),
	Number_of_Bids INT, 
	Started TIMESTAMP, 
	Ends TIMESTAMP, 
	Description VARCHAR(4000)
	);

CREATE TABLE Bids (
	ItemID INT,
	BidderID VARCHAR(50),
	Time TIMESTAMP,
	Amount DECIMAL(8,2),
	CONSTRAINT BidsKey PRIMARY KEY (ItemID, BidderID, Time)
	);
