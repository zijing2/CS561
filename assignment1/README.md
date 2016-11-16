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
 *
 * File - express.sql is the SQL version. Use load query in PostgreSQL to run it and get the result.
 *
 */



ecpg -I "/Applications/Postgres.app/Contents/Versions/9.5/include" sdap.pgc && 
gcc -c -I "/Applications/Postgres.app/Contents/Versions/9.5/include" sdap.c &&
gcc -L "/Applications/Postgres.app/Contents/Versions/9.5/lib" -lecpg -o sdap sdap.o