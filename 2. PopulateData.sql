# CPSC 89754967 - Shaurya Raghuvanshi

INSERT INTO topping VALUES(1, 'Pepperoni', 1.25, 0.2, 100, 2, 2.75, 3.5, 4.5, 0);
INSERT INTO topping VALUES(2, 'Sausage', 1.25, 0.15, 100, 2.5, 3, 3.5, 4.25, 0);
INSERT INTO topping VALUES(3, 'Ham', 1.5, 0.15, 78, 2, 2.5, 3.25, 4, 0);
INSERT INTO topping VALUES(4, 'Chicken', 1.75, 0.25, 56, 1.5, 2, 2.25, 3, 0);
INSERT INTO topping VALUES(5, 'Green Pepper', 0.5, 0.02, 79, 1, 1.5, 2, 2.5, 0);
INSERT INTO topping VALUES(6, 'Onion', 0.5, 0.02, 85, 1, 1.5, 2, 2.75, 0);
INSERT INTO topping VALUES(7, 'Roma Tomato', 0.75, 0.03, 86, 2, 3, 3.5, 4.5, 0);
INSERT INTO topping VALUES(8, 'Mushrooms', 0.75, 0.1, 52, 1.5, 2, 2.5, 3, 0);
INSERT INTO topping VALUES(9, 'Black Olives', 0.6, 0.1, 39, 0.75, 1, 1.5, 2, 0);
INSERT INTO topping VALUES(10, 'Pineapple', 1, 0.25, 15, 1, 1.25, 1.75, 2, 0);
INSERT INTO topping VALUES(11, 'Jalapenos', 0.5, 0.05, 64, 0.5, 0.75, 1.25, 1.75, 0);
INSERT INTO topping VALUES(12, 'Banana Peppers', 0.5, 0.05, 36, 0.6, 1, 1.3, 1.75, 0);
INSERT INTO topping VALUES(13, 'Regular Cheese', 1.5, 0.12, 250, 2, 3.5, 5, 7, 0);
INSERT INTO topping VALUES(14, 'Four Cheese Blend', 2, 0.15, 150, 2, 3.5, 5, 7, 0);
INSERT INTO topping VALUES(15, 'Feta Cheese', 2, 0.18, 75, 1.75, 3, 4, 5.5, 0);
INSERT INTO topping VALUES(16, 'Goat Cheese', 2, 0.2, 54, 1.6, 2.75, 4, 5.5, 0);
INSERT INTO topping VALUES(17, 'Bacon', 1.5, 0.25, 89, 1, 1.5, 2, 3, 0);


INSERT INTO discount VALUES(1, 'Employee', 15, TRUE);
INSERT INTO discount VALUES(2, 'Lunch Special Medium', 1, FALSE);
INSERT INTO discount VALUES(3, 'Lunch Special Large', 2, FALSE);
INSERT INTO discount VALUES(4, 'Speciality Pizza', 1.5, FALSE);
INSERT INTO discount VALUES(5, 'Gameday Special', 20, TRUE);


INSERT INTO base_price VALUES('Small', 'Thin', 3, 0.5);
INSERT INTO base_price VALUES('Small' , 'Original', 3, 0.75);
INSERT INTO base_price VALUES('Small', 'Pan', 3.5, 1);
INSERT INTO base_price VALUES('Small', 'Gluten-Free', 4, 2);
INSERT INTO base_price VALUES('Medium', 'Thin', 5, 1);
INSERT INTO base_price VALUES('Medium', 'Original', 5, 1.5);
INSERT INTO base_price VALUES('Medium', 'Pan', 6, 2.25);
INSERT INTO base_price VALUES('Medium', 'Gluten-Free', 6.25, 3);
INSERT INTO base_price VALUES('Large', 'Thin', 8, 1.25);
INSERT INTO base_price VALUES('Large', 'Original', 8, 2);
INSERT INTO base_price VALUES('Large', 'Pan', 9, 3);
INSERT INTO base_price VALUES('Large', 'Gluten-Free', 9.5, 4);
INSERT INTO base_price VALUES('X-Large', 'Thin', 10, 2);
INSERT INTO base_price VALUES('X-Large', 'Original', 10, 3);
INSERT INTO base_price VALUES('X-Large', 'Pan', 11.5, 4.5);
INSERT INTO base_price VALUES('X-Large', 'Gluten-Free', 12.5, 6);

-- order 1
INSERT INTO customer VALUES(1, 'Instore Customer', '111-111-1111');
INSERT INTO the_order VALUES(1, '2022-03-5 12:03:00', 1, null, null, 'Dine In', 1);
INSERT INTO pizza VALUES(1, 'Large', 'Thin', 'Completed', '2022-03-5 12:03:00', 13.50, 3.68, 1);
INSERT INTO discount_pizza VALUES(1, 3);
INSERT INTO pizza_topping VALUES(1, 1, FALSE);
INSERT INTO pizza_topping VALUES(1, 2, FALSE);
INSERT INTO pizza_topping VALUES(1, 13, TRUE);
INSERT INTO dine_in VALUES(1, 14);

-- order 2
INSERT INTO the_order VALUES(2, '2022-04-3 12:05:00', 1, null, null, 'Dine In', 1);
INSERT INTO pizza VALUES(2, 'Medium', 'Pan', 'Completed', '2022-04-3 12:05:00', 10.60, 3.23, 2);
INSERT INTO pizza VALUES(3, 'Small', 'Original', 'Completed', '2022-04-3 12:05:00', 6.75, 1.40, 2);
INSERT INTO discount_pizza VALUES(2, 2);
INSERT INTO discount_pizza VALUES(3, 4);
INSERT INTO pizza_topping VALUES(2, 7, FALSE);
INSERT INTO pizza_topping VALUES(2, 8, FALSE);
INSERT INTO pizza_topping VALUES(2, 9, FALSE);
INSERT INTO pizza_topping VALUES(2, 12, FALSE);
INSERT INTO pizza_topping VALUES(2, 15, FALSE);
INSERT INTO pizza_topping VALUES(3, 4, FALSE);
INSERT INTO pizza_topping VALUES(3, 12, FALSE);
INSERT INTO pizza_topping VALUES(3, 13, FALSE);
INSERT INTO dine_in VALUES(2, 4);

-- order 3
INSERT INTO customer VALUES(2, 'Andrew Wilkes-Krier', '864-254-5861');
INSERT INTO the_order VALUES(3, '2022-03-5 09:30:00', 2, null, null, 'Pickup', 1);
INSERT INTO pizza VALUES(4, 'Large', 'Original', 'Completed', '2022-03-5 09:30:00', 10.75, 3.30, 3);
INSERT INTO pizza VALUES(5, 'Large', 'Original', 'Completed', '2022-03-5 09:30:00', 10.75, 3.30, 3);
INSERT INTO pizza VALUES(6, 'Large', 'Original', 'Completed', '2022-03-5 09:30:00', 10.75, 3.30, 3);
INSERT INTO pizza VALUES(7, 'Large', 'Original', 'Completed', '2022-03-5 09:30:00', 10.75, 3.30, 3);
INSERT INTO pizza VALUES(8, 'Large', 'Original', 'Completed', '2022-03-5 09:30:00', 10.75, 3.30, 3);
INSERT INTO pizza VALUES(9, 'Large', 'Original', 'Completed', '2022-03-5 09:30:00', 10.75, 3.30, 3);
INSERT INTO pizza_topping VALUES(4, 1, FALSE);
INSERT INTO pizza_topping VALUES(4, 13, FALSE);
INSERT INTO pizza_topping VALUES(5, 1, FALSE);
INSERT INTO pizza_topping VALUES(5, 13, FALSE);
INSERT INTO pizza_topping VALUES(6, 1, FALSE);
INSERT INTO pizza_topping VALUES(6, 13, FALSE);
INSERT INTO pizza_topping VALUES(7, 1, FALSE);
INSERT INTO pizza_topping VALUES(7, 13, FALSE);
INSERT INTO pizza_topping VALUES(8, 1, FALSE);
INSERT INTO pizza_topping VALUES(8, 13, FALSE);
INSERT INTO pizza_topping VALUES(9, 1, FALSE);
INSERT INTO pizza_topping VALUES(9, 13, FALSE);
INSERT INTO pickup VALUES(3);

-- order 4
INSERT INTO the_order VALUES(4, '2022-04-20 19:11:00', 2, null, null, 'Delivery', 1);
INSERT INTO pizza VALUES(10, 'X-Large', 'Original', 'Completed', '2022-04-20 19:11:00', 14.50, 5.59, 4);
INSERT INTO pizza VALUES(11, 'X-Large', 'Original', 'Completed', '2022-04-20 19:11:00', 17, 5.59, 4);
INSERT INTO pizza VALUES(12, 'X-Large', 'Original', 'Completed', '2022-04-20 19:11:00', 14.00, 5.68, 4);
INSERT INTO pizza_topping VALUES(10, 1, FALSE);
INSERT INTO pizza_topping VALUES(10, 2, FALSE);
INSERT INTO pizza_topping VALUES(10, 14, FALSE);
INSERT INTO pizza_topping VALUES(11, 3, TRUE);
INSERT INTO pizza_topping VALUES(11, 10, TRUE);
INSERT INTO pizza_topping VALUES(11, 14, FALSE);
INSERT INTO pizza_topping VALUES(12, 11, FALSE);
INSERT INTO pizza_topping VALUES(12, 17, FALSE);
INSERT INTO pizza_topping VALUES(12, 14, FALSE);
INSERT INTO discount_pizza VALUES(11, 4);
INSERT INTO discount_order VALUES(4, 5);
INSERT INTO delivery VALUES(4, '115 Party Blvd, Anderson, SC, 29621');

-- order 5
INSERT INTO customer VALUES(3, 'Matt Engers', '864-474-9953');
INSERT INTO the_order VALUES(5, '2022-03-02 17:30:00', 3, null, null, 'Pickup', 1);
INSERT INTO pizza VALUES(13, 'X-Large', 'Gluten-free', 'Completed', '2022-03-02 17:30:00', 16.85, 7.85, 5);
INSERT INTO pizza_topping VALUES(13, 5, FALSE);
INSERT INTO pizza_topping VALUES(13, 6, FALSE);
INSERT INTO pizza_topping VALUES(13, 7, FALSE);
INSERT INTO pizza_topping VALUES(13, 8, FALSE);
INSERT INTO pizza_topping VALUES(13, 9, FALSE);
INSERT INTO pizza_topping VALUES(13, 16, FALSE);
INSERT INTO discount_pizza VALUES(13, 4);
INSERT INTO pickup VALUES(5);

-- order 6
INSERT INTO customer VALUES(4, 'Frank Turner', '864-232-8944');
INSERT INTO the_order VALUES(6, '2022-03-02 18:17:00', 4, null, null, 'Delivery', 1);
INSERT INTO pizza VALUES(14, 'Large', 'Thin', 'Completed', '2022-03-02 18:17:00', 13.25, 3.20, 6);
INSERT INTO pizza_topping VALUES(14, 4, FALSE);
INSERT INTO pizza_topping VALUES(14, 5, FALSE);
INSERT INTO pizza_topping VALUES(14, 6, FALSE);
INSERT INTO pizza_topping VALUES(14, 8, FALSE);
INSERT INTO pizza_topping VALUES(14, 14, TRUE);
INSERT INTO delivery VALUES(6, '6745 Wessex St Anderson, SC, 29621');

-- order 7
INSERT INTO customer VALUES(5, 'Milo Auckerman', '864-878-5679');
INSERT INTO the_order VALUES(7, '2022-04-13 20:32:00', 5, null, null, 'Delivery', 1);
INSERT INTO pizza VALUES(15, 'Large', 'Thin', 'Completed', '2022-04-13 20:32:00', 12, 3.75, 7);
INSERT INTO pizza VALUES(16, 'Large', 'Thin', 'Completed', '2022-04-13 20:32:00', 12, 2.55, 7);
INSERT INTO discount_order VALUES(7, 1);
INSERT INTO pizza_topping VALUES(15, 14, TRUE);
INSERT INTO pizza_topping VALUES(16, 1, TRUE);
INSERT INTO pizza_topping VALUES(16, 13, FALSE);
INSERT INTO delivery VALUES(7, '8879 Suburban Home, Anderson, SC, 29621');

SET SQL_SAFE_UPDATES = 0;
UPDATE the_order, (SELECT OrderID, SUM(PizzaPrice) AS PizzaPrice, SUM(PizzaCost) AS PizzaCost FROM pizza GROUP BY OrderID) AS t
SET TotalOrderPrice = t.PizzaPrice, TotalOrderCost = t.PizzaCost
WHERE the_order.OrderID = t.OrderID;
SET SQL_SAFE_UPDATES = 1;
