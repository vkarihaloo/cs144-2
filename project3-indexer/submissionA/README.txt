William Seto 803885258

For the Lucene Indexes, we decided to make indexes on the name, category, and description
attributes on the tables Items and Categories. We did this because we think it's better to
create inverted indexes for longer text fields.

For the MySQL Indexes, we decided to make indexes on the seller, buy price, bidder, and
ending time, on the tables Items and Bids because these fields are easier to search with mysql.
