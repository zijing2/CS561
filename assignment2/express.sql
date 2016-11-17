/* report1 */
With THE_AVG AS (
select cust,prod,avg(quant) as avg
from sales
group by cust,prod),

OTHER_PROD_AVG AS (
select L.cust as cust,L.prod as prod,avg(R.quant) as avg
from sales L, sales R
where L.cust=R.cust and L.prod<>R.prod
group by L.cust,L.prod
),

OTHER_CUST_AVG AS (
select L.cust as cust,L.prod as prod,avg(R.quant) as avg
from sales L, sales R
where L.cust<>R.cust and L.prod=R.prod
group by L.cust,L.prod
)

select THE_AVG.cust as CUSTOMER,THE_AVG.prod as PRODUCT,floor(THE_AVG.avg) as THE_AVG,floor(OTHER_PROD_AVG.avg) as OTHER_PROD_AVG,floor(OTHER_CUST_AVG.avg) as OTHER_CUST_AVG
from (THE_AVG full outer join OTHER_PROD_AVG on THE_AVG.cust=OTHER_PROD_AVG.cust and THE_AVG.prod=OTHER_PROD_AVG.prod) full outer join OTHER_CUST_AVG on 
THE_AVG.cust=OTHER_CUST_AVG.cust and THE_AVG.prod=OTHER_CUST_AVG.prod



/* report2 */
With BEFORE_AVG AS (
select L.cust as cust,L.prod as prod,L.month as month,avg(R.quant) as avg
from sales L, sales R
where L.cust=R.cust and L.prod=R.prod and R.month=L.month-1
group by L.cust,L.prod,L.month),
AFTER_AVG AS (
select L.cust as cust,L.prod as prod,L.month as month,avg(R.quant) as avg
from sales L, sales R
where L.cust=R.cust and L.prod=R.prod and R.month=L.month+1
group by L.cust,L.prod,L.month)

select sales.cust as CUSTOMER,sales.prod as PRODUCT,sales.month as MONTH,floor(BEFORE_AVG.avg) as BEFORE_AVG,floor(AFTER_AVG.avg) as AFTER_AVG
from (sales full outer join BEFORE_AVG on sales.cust=BEFORE_AVG.cust and sales.prod=BEFORE_AVG.prod and sales.month=BEFORE_AVG.month) full outer join AFTER_AVG on sales.cust=AFTER_AVG.cust and sales.prod=AFTER_AVG.prod and sales.month=AFTER_AVG.month

/* report3 */
WITH AVG_MAX AS(
select cust,prod,month,avg(quant) as avg,max(quant) as max
from sales 
group by cust,prod,month
),
BEFORE_TOT AS(
select AVG_MAX.prod as prod,AVG_MAX.cust as cust,AVG_MAX.month as month,count(*) as count
from AVG_MAX,sales
where AVG_MAX.cust = sales.cust and AVG_MAX.prod = sales.prod and sales.month=AVG_MAX.month-1 and sales.quant<=AVG_MAX.max and sales.quant>=AVG_MAX.avg
group by AVG_MAX.prod,AVG_MAX.cust,AVG_MAX.month
),
AFTER_TOT AS(
select AVG_MAX.prod as prod,AVG_MAX.cust as cust,AVG_MAX.month as month,count(*) as count
from AVG_MAX,sales
where AVG_MAX.cust = sales.cust and AVG_MAX.prod = sales.prod and sales.month=AVG_MAX.month+1 and sales.quant<=AVG_MAX.max and sales.quant>=AVG_MAX.avg
group by AVG_MAX.prod,AVG_MAX.cust,AVG_MAX.month
)

select sales.cust as CUSTOMER,sales.prod as PRODUCT,sales.month as MONTH,BEFORE_TOT.count as BEFORE_TOT,AFTER_TOT.count as AFTER_TOT
from (sales full outer join BEFORE_TOT on sales.cust=BEFORE_TOT.cust and sales.prod=BEFORE_TOT.prod and sales.month=BEFORE_TOT.month) full outer join AFTER_TOT on sales.cust=AFTER_TOT.cust and sales.prod=AFTER_TOT.prod and sales.month=AFTER_TOT.month

