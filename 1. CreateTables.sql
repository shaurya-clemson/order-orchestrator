# CPSC 89754967 - Shaurya Raghuvanshi

CREATE DATABASE IF NOT EXISTS Pizzeria;
USE Pizzeria;

CREATE TABLE IF NOT EXISTS base_price(
PizzaSize	VARCHAR(20) NOT NULL,
PizzaCrust  VARCHAR(20) NOT NULL,
BasePrice	DOUBLE,
BaseCost	DOUBLE,
PRIMARY KEY (PizzaSize, PizzaCrust));


CREATE TABLE IF NOT EXISTS customer(
CustomerID				INTEGER PRIMARY KEY NOT NULL,
CustomerName			VARCHAR(100) NOT NULL,
CustomerPhoneNumber		VARCHAR(13) UNIQUE NOT NULL);


CREATE TABLE IF NOT EXISTS the_order(
OrderID				INTEGER PRIMARY KEY NOT NULL,
OrderDate			DATETIME NOT NULL,
CustomerID			INTEGER,
TotalOrderPrice		DOUBLE,
TotalOrderCost		DOUBLE,
OrderType			VARCHAR(15) NOT NULL,
OrderState			INTEGER NOT NULL DEFAULT 0,						
FOREIGN KEY (CustomerID) REFERENCES customer(CustomerID));

CREATE TABLE IF NOT EXISTS dine_in(
OrderID				INTEGER PRIMARY KEY NOT NULL,
DineInTable			INTEGER NOT NULL,
FOREIGN KEY (OrderID) REFERENCES the_order(OrderID));

CREATE TABLE IF NOT EXISTS pickup(
OrderID				INTEGER PRIMARY KEY NOT NULL,
FOREIGN KEY (OrderID) REFERENCES the_order(OrderID));

CREATE TABLE IF NOT EXISTS delivery(
OrderID				INTEGER PRIMARY KEY NOT NULL,
CustomerAddress			VARCHAR(100) NOT NULL,
FOREIGN KEY (OrderID) REFERENCES the_order(OrderID));


CREATE TABLE IF NOT EXISTS pizza(
PizzaID				INTEGER PRIMARY KEY NOT NULL,
PizzaSize			VARCHAR(20) NOT NULL,
PizzaCrust 	 		VARCHAR(20) NOT NULL,
PizzaState			VARCHAR(20) NOT NULL,
PizzaDate			DATE,
PizzaPrice			DOUBLE,
PizzaCost			DOUBLE,
OrderID 			INTEGER NOT NULL,
FOREIGN KEY (PizzaSize, PizzaCrust) REFERENCES base_price(PizzaSize, PizzaCrust),
FOREIGN KEY (OrderID) REFERENCES the_order(OrderID));


CREATE TABLE IF NOT EXISTS topping(
ToppingID					INTEGER PRIMARY KEY NOT NULL,
ToppingName					VARCHAR(35) NOT NULL,
ToppingPrice				DOUBLE NOT NULL,
ToppingCost					DOUBLE NOT NULL,
ToppingCurrentInventory		INTEGER NOT NULL,
ToppingAmountSmall			DOUBLE NOT NULL,
ToppingAmountMedium			DOUBLE NOT NULL,
ToppingAmountLarge			DOUBLE NOT NULL,
ToppingAmountXLarge			DOUBLE NOT NULL,
ToppingMinimumInventory		INTEGER);


CREATE TABLE IF NOT EXISTS pizza_topping(
PizzaID 			INTEGER,
ToppingID			INTEGER,
ToppingIsDouble		BOOLEAN DEFAULT FALSE,
PRIMARY KEY (PizzaID, ToppingID),
FOREIGN KEY (PizzaID) REFERENCES pizza(PizzaID),
FOREIGN KEY (ToppingID) REFERENCES topping(ToppingID));


CREATE TABLE IF NOT EXISTS discount(
DiscountID			INTEGER PRIMARY KEY NOT NULL,
DiscountName		VARCHAR(35) NOT NULL,
DiscountAmount		DOUBLE,
isPercent			BOOLEAN);

CREATE TABLE IF NOT EXISTS discount_pizza(
PizzaID 		INTEGER,
DiscountID		INTEGER,
PRIMARY KEY (PizzaID, DiscountID),
FOREIGN KEY (PizzaID) REFERENCES pizza(PizzaID),
FOREIGN KEY (DiscountID) REFERENCES discount(DiscountID));

CREATE TABLE IF NOT EXISTS discount_order(
OrderID 		INTEGER,
DiscountID		INTEGER,
PRIMARY KEY (OrderID, DiscountID),
FOREIGN KEY (OrderID) REFERENCES the_order(OrderID),
FOREIGN KEY (DiscountID) REFERENCES discount(DiscountID));