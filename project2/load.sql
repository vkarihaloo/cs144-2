
LOAD DATA LOCAL INFILE 'users.csv' into TABLE Users
  FIELDS TERMINATED BY ',' ENCLOSED BY '"';

LOAD DATA LOCAL INFILE 'items.csv' into TABLE Items
  FIELDS TERMINATED BY ',' ENCLOSED BY '"';
  
LOAD DATA LOCAL INFILE 'bids.csv' into TABLE Bids
  FIELDS TERMINATED BY ',';