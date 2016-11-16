drop view IF EXISTS max_sales,min_sales,avg_sales,CT_MAX,NY_MIN,NJ_MIN ;

create view max_sales as
	select B.prod as Product,B.quant as Max_Q,B.cust as CUSTOMER,B.day,B.month,B.year from (select prod , 	MAX(quant) as MAX_Q from sales group by prod) as A
	left join
	(select * from sales) as B
	on B.prod=A.prod and B.quant=A.MAX_Q;

Create view min_sales as
	select B.prod as Product,B.quant as MIN_Q,B.cust as CUSTOMER,B.day,B.month,B.year from (select prod , 	MIN(quant) as MIN_Q from sales group by prod) as A
	left join
	(select * from sales) as B
	on B.prod=A.prod and B.quant=A.MIN_Q;

create view avg_sales as
	select prod, AVG(quant) from sales group by prod;

select max_sales.product,max_sales.max_q,max_sales.customer,concat(max_sales.month,'/',max_sales.day,'/',max_sales.year) as MAX_DATE,min_sales.min_q,min_sales.customer,concat(min_sales.month,'/',min_sales.day,'/',min_sales.year) as MIN_DATE,avg_sales.avg 
from max_sales join min_sales on max_sales.product=min_sales.product join avg_sales on avg_sales.prod=max_sales.product;


create view CT_MAX as
	select A.cust as CUSTOMER,A.prod as Product, A.max_quant as CT_MAX, concat(B.month,'/',B.day,'/',B.year) as DATE from 
	(select cust,prod,MAX(quant) as max_quant from sales where state='CT' and year>=2000 and year<=2005 group by prod,cust) as A left join
	(select * from sales where state='CT' and year>=2000 and year<=2005) as B
	on
	A.prod=B.prod and A.cust=B.cust and A.max_quant = B.quant;

create view NY_MIN as
	select A.cust as CUSTOMER,A.prod as Product, A.min_quant as NY_MIN, concat(B.month,'/',B.day,'/',B.year) as DATE from 
	(select cust,prod,MIN(quant) as min_quant from sales where state='NY' group by prod,cust) as A left join
	(select * from sales where state='NY') as B
	on
	A.prod=B.prod and A.cust=B.cust and A.min_quant = B.quant;


create view NJ_MIN as
	select A.cust as CUSTOMER,A.prod as Product, A.min_quant as NJ_MIN, concat(B.month,'/',B.day,'/',B.year) as DATE from 
	(select cust,prod,MIN(quant) as min_quant from sales where state='NJ' group by prod,cust) as A left join
	(select * from sales where state='NJ') as B
	on
	A.prod=B.prod and A.cust=B.cust and A.min_quant = B.quant;

select CT_MAX.customer as customer, CT_MAX.product as product, CT_MAX.ct_max as CT_MAX, CT_MAX.date as ct_date, NY_MIN.ny_min as ny_min, NY_MIN.date as ny_date, NJ_MIN.nj_min as nj_min, NJ_MIN.date as nj_date from CT_MAX 
left outer join NY_MIN on CT_MAX.product=NY_MIN.product and CT_MAX.customer=NY_MIN.customer 
left outer join NJ_MIN on CT_MAX.product=NJ_MIN.product and CT_MAX.customer=NJ_MIN.customer;