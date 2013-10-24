CREATE TABLE Items (
	id INT PRIMARY KEY, 
	seller VARCHAR(50),
	name VARCHAR(100), 
	currently DECIMAL(8,2), 
	firstbid DECIMAL(8,2), 
	buyprice DECIMAL(8,2), 
	started TIMESTAMP, 
	ends TIMESTAMP, 
	description VARCHAR(4000));

CREATE TABLE Users (
