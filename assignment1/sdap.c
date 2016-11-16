/* Processed by ecpg (4.11.0) */
/* These include files are added by the preprocessor */
#include <ecpglib.h>
#include <ecpgerrno.h>
#include <sqlca.h>
/* End of automatic include section */

#line 1 "sdap.pgc"
/*
 * Author: Zijing Huang 
 * CWID: 10414952
 */

#include	<stdio.h>

//----------------------------------------------------------------------
// HOST VARIABLES definitions
//----------------------------------------------------------------------
/* exec sql begin declare section */

  
		
		
	    
	    
		   
		
		 
	typedef struct { 
#line 14 "sdap.pgc"
 char * cust ;
 
#line 15 "sdap.pgc"
 char * prod ;
 
#line 16 "sdap.pgc"
 short dd ;
 
#line 17 "sdap.pgc"
 short mm ;
 
#line 18 "sdap.pgc"
 short yy ;
 
#line 19 "sdap.pgc"
 char * state ;
 
#line 20 "sdap.pgc"
 long quant ;
 }  prod ;

#line 21 "sdap.pgc"


  
    
     
     
  typedef struct { 
#line 24 "sdap.pgc"
 char * prod ;
 
#line 25 "sdap.pgc"
 int total ;
 
#line 26 "sdap.pgc"
 int count ;
 }  avg ;

#line 27 "sdap.pgc"


 
 //each struct array has exclusive prod name; 
 
 
 //each struct array has exclusive prod and cust combination.
 
 



#line 29 "sdap.pgc"
 prod sale_rec ;
 
#line 30 "sdap.pgc"
 prod max_product_str [ 50 ] ;
 
#line 31 "sdap.pgc"
 prod min_product_str [ 50 ] ;
 
#line 32 "sdap.pgc"
 avg avg_product [ 50 ] ;
 
#line 33 "sdap.pgc"
 prod max_ct_prod_cust [ 100 ] ;
 
#line 34 "sdap.pgc"
 prod min_ny_prod_cust [ 100 ] ;
 
#line 35 "sdap.pgc"
 prod min_nj_prod_cust [ 100 ] ;
/* exec sql end declare section */
#line 38 "sdap.pgc"


#line 1 "/Applications/Postgres.app/Contents/Versions/9.5/include/sqlca.h"
#ifndef POSTGRES_SQLCA_H
#define POSTGRES_SQLCA_H

#ifndef PGDLLIMPORT
#if  defined(WIN32) || defined(__CYGWIN__)
#define PGDLLIMPORT __declspec (dllimport)
#else
#define PGDLLIMPORT
#endif   /* __CYGWIN__ */
#endif   /* PGDLLIMPORT */

#define SQLERRMC_LEN	150

#ifdef __cplusplus
extern		"C"
{
#endif

struct sqlca_t
{
	char		sqlcaid[8];
	long		sqlabc;
	long		sqlcode;
	struct
	{
		int			sqlerrml;
		char		sqlerrmc[SQLERRMC_LEN];
	}			sqlerrm;
	char		sqlerrp[8];
	long		sqlerrd[6];
	/* Element 0: empty						*/
	/* 1: OID of processed tuple if applicable			*/
	/* 2: number of rows processed				*/
	/* after an INSERT, UPDATE or				*/
	/* DELETE statement					*/
	/* 3: empty						*/
	/* 4: empty						*/
	/* 5: empty						*/
	char		sqlwarn[8];
	/* Element 0: set to 'W' if at least one other is 'W'	*/
	/* 1: if 'W' at least one character string		*/
	/* value was truncated when it was			*/
	/* stored into a host variable.             */

	/*
	 * 2: if 'W' a (hopefully) non-fatal notice occurred
	 */	/* 3: empty */
	/* 4: empty						*/
	/* 5: empty						*/
	/* 6: empty						*/
	/* 7: empty						*/

	char		sqlstate[5];
};

struct sqlca_t *ECPGget_sqlca(void);

#ifndef POSTGRES_ECPG_INTERNAL
#define sqlca (*ECPGget_sqlca())
#endif

#ifdef __cplusplus
}
#endif

#endif

#line 39 "sdap.pgc"


//----------------------------------------------------------------------
// FUNCTION PROTOTYPE declaration
//----------------------------------------------------------------------


//----------------------------------------------------------------------
int main(int argc, char* argv[])
//----------------------------------------------------------------------
{

   //----------------------------------------------------------------------
   // DATABASE CONNECTION
   //----------------------------------------------------------------------
   // EXEC SQL CONNECT TO PostgreSQL@localhost:5432 USER PostgreSQL IDENTIFIED BY XYZ;
   { ECPGconnect(__LINE__, 0, "huangzijing@localhost:5432" , "huangzijing" , NULL , NULL, 0); }
#line 55 "sdap.pgc"

   
   if (sqlca.sqlcode != 0) {	// login error
   	printf ("Login error!!!\n");
   	return -1;
   }
   /* exec sql whenever sqlerror  sqlprint ; */
#line 61 "sdap.pgc"


   //----------------------------------------------------------------------
   // PRINT TITLE
   //----------------------------------------------------------------------   
   printf(" Product  MAX_Q CUSTOMER   DATE       ST MIN_Q CUSTOMER   DATE       ST AVE_Q \n");
   printf(" ======== ===== ========== ========== == ===== ========== ========== == ===== \n");
   
   //----------------------------------------------------------------------
   // READ RECORDS
   //----------------------------------------------------------------------
   /* declare mycursor cursor for select * from sales */
#line 72 "sdap.pgc"

   { ECPGdo(__LINE__, 0, 1, NULL, 0, ECPGst_normal, "set transaction read only", ECPGt_EOIT, ECPGt_EORT);
#line 73 "sdap.pgc"

if (sqlca.sqlcode < 0) sqlprint();}
#line 73 "sdap.pgc"

   // Open cursor
   { ECPGdo(__LINE__, 0, 1, NULL, 0, ECPGst_normal, "declare mycursor cursor for select * from sales", ECPGt_EOIT, ECPGt_EORT);
#line 75 "sdap.pgc"

if (sqlca.sqlcode < 0) sqlprint();}
#line 75 "sdap.pgc"

   // Fetch Data
   { ECPGdo(__LINE__, 0, 1, NULL, 0, ECPGst_normal, "fetch from mycursor", ECPGt_EOIT, 
	ECPGt_char,&(sale_rec.cust),(long)0,(long)1,sizeof( prod ), 
	ECPGt_NO_INDICATOR, NULL , 0L, 0L, 0L, 
	ECPGt_char,&(sale_rec.prod),(long)0,(long)1,sizeof( prod ), 
	ECPGt_NO_INDICATOR, NULL , 0L, 0L, 0L, 
	ECPGt_short,&(sale_rec.dd),(long)1,(long)1,sizeof( prod ), 
	ECPGt_NO_INDICATOR, NULL , 0L, 0L, 0L, 
	ECPGt_short,&(sale_rec.mm),(long)1,(long)1,sizeof( prod ), 
	ECPGt_NO_INDICATOR, NULL , 0L, 0L, 0L, 
	ECPGt_short,&(sale_rec.yy),(long)1,(long)1,sizeof( prod ), 
	ECPGt_NO_INDICATOR, NULL , 0L, 0L, 0L, 
	ECPGt_char,&(sale_rec.state),(long)0,(long)1,sizeof( prod ), 
	ECPGt_NO_INDICATOR, NULL , 0L, 0L, 0L, 
	ECPGt_long,&(sale_rec.quant),(long)1,(long)1,sizeof( prod ), 
	ECPGt_NO_INDICATOR, NULL , 0L, 0L, 0L, ECPGt_EORT);
#line 77 "sdap.pgc"

if (sqlca.sqlcode < 0) sqlprint();}
#line 77 "sdap.pgc"
 //fetch the first row

   while (sqlca.sqlcode == 0) {
      
      //max quant for each prod
      for(int i=0;i<50;i++){
         if(max_product_str[i].prod<=0){
            max_product_str[i] = sale_rec;         
            break;
         }else if(strcmp(max_product_str[i].prod,sale_rec.prod)==0){
            if(sale_rec.quant > max_product_str[i].quant){
               max_product_str[i] = sale_rec;
            }
            break;
         }
      }

      //min quant for each prod
      for(int i=0;i<50;i++){
         if(min_product_str[i].prod<=0){
            min_product_str[i] = sale_rec;         
            break;
         }else if(strcmp(min_product_str[i].prod,sale_rec.prod)==0){
            if(sale_rec.quant <= min_product_str[i].quant){
               min_product_str[i] = sale_rec;
            }
            break;
         }
      }

      //avg quant for each prod
      for(int i=0;i<50;i++){
         if(avg_product[i].total<=0){
            avg_product[i].prod = sale_rec.prod;
            avg_product[i].total = sale_rec.quant;  
            avg_product[i].count = 1;       
            break;
         }else if(strcmp(avg_product[i].prod,sale_rec.prod)==0){
            avg_product[i].total = avg_product[i].total+sale_rec.quant;
            avg_product[i].count = avg_product[i].count+1;
            break;
         }
      }

      //max quant of CT for each prod and customer combine
      char ct[] = "CT";
      if(sale_rec.yy>=2000 && sale_rec.yy<=2005 && strcmp(ct,sale_rec.state)==0){
         for(int i=0;i<100;i++){
           if(max_ct_prod_cust[i].prod<=0){
               max_ct_prod_cust[i] = sale_rec;         
               break;
            }else if(strcmp(max_ct_prod_cust[i].prod,sale_rec.prod)==0 && strcmp(max_ct_prod_cust[i].cust,sale_rec.cust)==0){
               if(sale_rec.quant>max_ct_prod_cust[i].quant){                  
                  max_ct_prod_cust[i] = sale_rec;
               }
               break;
               
            }
         }
      }

      
      //min quant of NY for each prod and customer combine
      char ny[] = "NY";
      if(strcmp(ny,sale_rec.state)==0){
         for(int i=0;i<100;i++){
           if(min_ny_prod_cust[i].prod<=0){
               min_ny_prod_cust[i] = sale_rec;         
               break;
            }else if(strcmp(min_ny_prod_cust[i].prod,sale_rec.prod)==0 && strcmp(min_ny_prod_cust[i].cust,sale_rec.cust)==0){
               if(sale_rec.quant<min_ny_prod_cust[i].quant){
                  min_ny_prod_cust[i] = sale_rec;
               }
               break;
            }
         }
      }
      

      //min quant of NJ for each prod and customer combine
      char nj[] = "NJ";
      if(strcmp(nj,sale_rec.state)==0){
         for(int i=0;i<100;i++){
           if(min_nj_prod_cust[i].prod<=0){
               min_nj_prod_cust[i] = sale_rec;         
               break;
            }else if(strcmp(min_nj_prod_cust[i].prod,sale_rec.prod)==0 && strcmp(min_nj_prod_cust[i].cust,sale_rec.cust)==0){
               if(sale_rec.quant<min_nj_prod_cust[i].quant){
                  min_nj_prod_cust[i] = sale_rec;
               }
               break;
            }
         }
      }

      //free the char pointer in the struct
      sale_rec.cust = 0;
      sale_rec.prod = 0;
      sale_rec.state = 0;
                    
      { ECPGdo(__LINE__, 0, 1, NULL, 0, ECPGst_normal, "fetch from mycursor", ECPGt_EOIT, 
	ECPGt_char,&(sale_rec.cust),(long)0,(long)1,sizeof( prod ), 
	ECPGt_NO_INDICATOR, NULL , 0L, 0L, 0L, 
	ECPGt_char,&(sale_rec.prod),(long)0,(long)1,sizeof( prod ), 
	ECPGt_NO_INDICATOR, NULL , 0L, 0L, 0L, 
	ECPGt_short,&(sale_rec.dd),(long)1,(long)1,sizeof( prod ), 
	ECPGt_NO_INDICATOR, NULL , 0L, 0L, 0L, 
	ECPGt_short,&(sale_rec.mm),(long)1,(long)1,sizeof( prod ), 
	ECPGt_NO_INDICATOR, NULL , 0L, 0L, 0L, 
	ECPGt_short,&(sale_rec.yy),(long)1,(long)1,sizeof( prod ), 
	ECPGt_NO_INDICATOR, NULL , 0L, 0L, 0L, 
	ECPGt_char,&(sale_rec.state),(long)0,(long)1,sizeof( prod ), 
	ECPGt_NO_INDICATOR, NULL , 0L, 0L, 0L, 
	ECPGt_long,&(sale_rec.quant),(long)1,(long)1,sizeof( prod ), 
	ECPGt_NO_INDICATOR, NULL , 0L, 0L, 0L, ECPGt_EORT);
#line 177 "sdap.pgc"

if (sqlca.sqlcode < 0) sqlprint();}
#line 177 "sdap.pgc"
 //fetch the rest rows
   }

   //output
   for(int i=0;i<50;i++){
      if(max_product_str[i].prod>0){
         printf(" %-7s ",max_product_str[i].prod);   // Product
         printf(" %5ld",max_product_str[i].quant);  // Quantity
         printf(" %-5s   ",max_product_str[i].cust);   // Customer
         printf("   %02d/",max_product_str[i].mm);     // Month
         printf("%02d/",max_product_str[i].dd);     // Day
         printf("%4d ",max_product_str[i].yy);      // Year
         printf("%-2s ",max_product_str[i].state);  // State
         printf("%5ld",min_product_str[i].quant);  // Quantity
         printf(" %-5s   ",min_product_str[i].cust);   // Customer
         printf("   %02d/",min_product_str[i].mm);     // Month
         printf("%02d/",min_product_str[i].dd);     // Day
         printf("%4d ",min_product_str[i].yy);      // Year
         printf("%-2s ",min_product_str[i].state);  // State
         printf(" %5d \n",avg_product[i].total/avg_product[i].count);  // avg
      }
   }

   //for(int i=0;i<50;i++){
   //   if(avg_product[i].count>0){
   //      printf(" %-7s ",avg_product[i].prod);   // Product
   //      printf(" %5d",avg_product[i].total);  // Quantity
   //      printf(" %5d \n",avg_product[i].count);   // Customer
   //   }      
   //}

   //----------------------------------------------------------------------
   // PRINT SECOND TITLE
   //----------------------------------------------------------------------  
   printf("\n"); 
   printf(" CUSTOMER PRODUCT CT_MAX DATE       NY_MIN DATE       NJ_MIN DATE      \n");
   printf(" ======== ======= ====== ========== ====== ========== ====== ========= \n");

   for(int i=0;i<100;i++){
      if(max_ct_prod_cust[i].prod>0){
         printf(" %-5s   ",max_ct_prod_cust[i].cust);   // Customer
         printf(" %-7s",max_ct_prod_cust[i].prod);   // Product
         printf("  %5ld",max_ct_prod_cust[i].quant);      //max ct quant
         printf(" %02d/",max_ct_prod_cust[i].mm);     // Month
         printf("%02d/",max_ct_prod_cust[i].dd);     // Day
         printf("%4d ",max_ct_prod_cust[i].yy);      // Year
         for(int j=0;j<100;j++){
            if(min_ny_prod_cust[j].prod>0){        
               if(strcmp(min_ny_prod_cust[j].cust,max_ct_prod_cust[i].cust)==0 && strcmp(min_ny_prod_cust[j].prod,max_ct_prod_cust[i].prod)==0){
                  printf(" %5ld",min_ny_prod_cust[j].quant);      //min ny quant
                  printf(" %02d/",min_ny_prod_cust[j].mm);     // Month
                  printf("%02d/",min_ny_prod_cust[j].dd);     // Day
                  printf("%4d ",min_ny_prod_cust[j].yy);      // Year
                  break;
               }
            }
            //some prod and cust combination maybe doesn't have quant;
            if(j==99){
               printf("  null");     
               printf("       null ");   
            }
         }
         
         for(int k=0;k<100;k++){
            if(min_nj_prod_cust[k].prod>0){
               if(strcmp(min_nj_prod_cust[k].cust,max_ct_prod_cust[i].cust)==0 && strcmp(min_nj_prod_cust[k].prod,max_ct_prod_cust[i].prod)==0){
                  printf(" %5ld",min_nj_prod_cust[k].quant);      //min nj quant
                  printf(" %02d/",min_nj_prod_cust[k].mm);     // Month
                  printf("%02d/",min_nj_prod_cust[k].dd);     // Day
                  printf("%4d \n",min_nj_prod_cust[k].yy);      // Year
                  break;
               }
               
            }
             //some prod and cust combination maybe doesn't have quant;
            if(k==99){
               printf("  null");   
               printf("       null\n");   
              
            }
         }
               
      }
   }
      
   // Close cursor
   { ECPGdo(__LINE__, 0, 1, NULL, 0, ECPGst_normal, "close mycursor", ECPGt_EOIT, ECPGt_EORT);
#line 263 "sdap.pgc"

if (sqlca.sqlcode < 0) sqlprint();}
#line 263 "sdap.pgc"


   return 0;
}



