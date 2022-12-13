# CPSC 89754967 - Shaurya Raghuvanshi

-- Topping Popularity

DROP VIEW IF EXISTS ToppingPopularity;

CREATE VIEW ToppingPopularity AS
SELECT
	t.ToppingName AS Topping, SUM(CASE WHEN pt.ToppingIsDouble = TRUE THEN 2 ELSE 1 END) AS ToppingCount
    FROM
    pizza_topping pt
    JOIN
    topping t ON pt.ToppingID = t.ToppingID
    GROUP BY ToppingName
    ORDER BY ToppingCount DESC;
    
SELECT *
FROM ToppingPopularity;

DROP VIEW IF EXISTS ProfitByPizza;


-- Profit By Pizza

CREATE VIEW ProfitByPizza AS
SELECT
	p.PizzaSize, p.PizzaCrust, ROUND(SUM(p.PizzaPrice - p.PizzaCost), 2) AS Profit, MAX(PizzaDate) AS LastOrderDate
    FROM
    pizza p
    JOIN
    base_price bp ON  p.PizzaSize = bp.PizzaSize AND p.PizzaCrust = bp.PizzaCrust
    GROUP BY p.PizzaSize, p.PizzaCrust
    ORDER BY Profit DESC;
    
SELECT *
FROM ProfitByPizza;


-- Profit By Order Type

DROP VIEW IF EXISTS ProfitByOrderType;

CREATE VIEW ProfitByOrderType AS
SELECT
	OrderType AS CustomerType, CONCAT(YEAR(OrderDate), '-', MONTHNAME(OrderDate)) AS OrderMonth, SUM(ROUND(TotalOrderPrice, 2)) AS TotalOrderPrice, 
    SUM(ROUND(TotalOrderCost, 2)) AS TotalOrderCost, ROUND(TotalOrderPrice - TotalOrderCost, 2) AS Profit
    FROM
    the_order
	GROUP BY CustomerType, OrderMonth
    UNION ALL
    SELECT
	'', 'Grand Total',  ROUND(SUM(TotalOrderPrice), 2), ROUND(SUM(TotalOrderCost), 2),
    ROUND(SUM(TotalOrderPrice - TotalOrderCost), 2) AS Profit
    FROM
    the_order;
    
SELECT * FROM ProfitByOrderType;    