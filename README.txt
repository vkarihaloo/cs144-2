Part B: Design your relational schema

1. List your relations. Please specify all keys that hold on each relation. You need not specify attribute types at this stage.

Users(UsersID, Location, Country, Rating)
UsersID is the key for the Users relation.

Items(ItemID, SellerID, Name, Currently, First_Bid, Buy_Price, Number_of_Bids, Started, Ends, Description)
ItemID is the key for the Item relation.

Bids(ItemID, BidderID, Time, Amount)
ItemID is the key for the Bids Relation.
ItemID and BidderID are the keys for the Bids relation.

2. List all completely nontrivial functional dependencies that hold on each relation, excluding those that effectively specify keys.

For the relation Users, the functional dependencies are:
UsersID -> Location, Country, Rating

For the relation Item, the functional dependencies are:
ItemID -> SellerID, Name, Currently, First_Bid, Buy_Price, Number_of_Bids, Started, Ends

For the relation Bids, the functional dependencies are:
ItemID, BidderID -> Time, Amount

3. Are all of your relations in Boyce-Codd Normal Form (BCNF)? If not, either redesign them and start over, or explain why you feel it is advantageous to use non-BCNF relations.

Yes, all the relations are in Boyce-Cood Normal Form.