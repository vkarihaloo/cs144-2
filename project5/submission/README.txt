Partner: William Seto 803885258

Q1: For which communication(s) do you use the SSL encryption? If you are encrypting the communication from (1) to (2) in Figure 2, for example, write (1)→(2) in your answer.
We encrypted the communication from (4)-(5) and(5)-(6).

Q2: How do you ensure that the item was purchased exactly at the Buy_Price of that particular item?
Whenever a user visits an item page, we store the buy_price that sunflower provides us into the session array.

Q3: How do you guarantee that the user cannot scroll horizontally?
Using the meta viewport tag, we set the width to be the device-width, and also the user-scalable field to no.

Q4: How do you guarantee that the width of your textbox component(s) can fit the screen width of a mobile device? Note: you have to explain "how", and you can't simply state that "we use a XXX downloaded from YYY, and it magically solve the problem."
To do this, we set the css width to be 80% of the browser window instead of a fixed pixel width.

Note: If you get help from any source other than those mentioned in this page, at the end of your README, please clearly cite all references you use, and breifly explain how they help you, such as which portion(s) is/are particularly helpful.
