#!/bin/bash

# Run the drop.sql batch file to drop existing tables
# Inside the drop.sql, you sould check whether the table exists. Drop them ONLY if they exists.
mysql CS144 < drop.sql

# Run the create.sql batch file to create the database and tables
mysql CS144 < create.sql

# Compile and run the parser to generate the appropriate load files
#ant run
ant run-all

# cat users.csv | sort | uniq > users2.csv

sort -u users-temp.csv > users.csv
sort -u category-temp.csv > category.csv

# Run the load.sql batch file to load the data
mysql CS144 < load.sql

rm users-temp.csv
rm users.csv
rm items.csv
rm bids.csv
rm category.csv
rm category-temp.csv
