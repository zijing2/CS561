# CS561
database management system

/*
 * File - sdap.pgc (Need the .pgc extension!) 
 *
 * Desc - This is a simple skeleton program using Embedded SQL to read all
 *        of the rows from the table "sales" and output to the console.
 *        Simple but it contains all of the essential features of ESQL 
 *        discussed in class!
 *
 * Steps to run this program : 
 *  1. In the program (L49), modify [dbname], [username], [password] to
 *     yours ([dbname] is the same as your [username] by default).
 *  2. Preprocessor - $ ecpg -I "C:\Program Files\PostgreSQL\9.4\include" sdap.pgc
 *  3. Compile      - $ gcc -c -I "C:\Program Files\PostgreSQL\9.4\include" sdap.c
 *  4. Link         - $ gcc -L "C:\Program Files\PostgreSQL\9.4\lib" -lecpg -o sdap sdap.o
 *  5. Execute      - $ .\sdap
 */
