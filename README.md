# CS561
database management system

/*
 *	Author: zijing Huang
 *	CWID:10414952
 *
 * File - sdap.pgc (Need the .pgc extension!) 
 *
 * Steps to run this program : 
 *  1. create a database on your db system using db name:huangzijing or you can modify line 55 to match your db name
 *  2. create a table 'sales' and import the data we need
 *  3. Preprocessor - $ ecpg -I "C:\Program Files\PostgreSQL\9.4\include" sdap.pgc
 *  4. Compile      - $ gcc -c -I "C:\Program Files\PostgreSQL\9.4\include" sdap.c
 *  5. Link         - $ gcc -L "C:\Program Files\PostgreSQL\9.4\lib" -lecpg -o sdap sdap.o
 *  6. Execute      - $ .\sdap
 */
