package com.testPostgreSQL.driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;

public class driver {
	
	

	public static void main(String[] args) {
		
		//the customer's average sale of this product
		HashMap<String,HashMap<String,Integer>> report1 = new HashMap<String,HashMap<String,Integer>>();
		HashMap<String,Integer> item;
		String prod;
		String cust;
		String month;
		
		int quant;
		String key;
		
		//For customer and product, show the average sales before and after each month
		HashMap<String,HashMap<String,Integer>> report2 = new HashMap<String,HashMap<String,Integer>>();
		HashMap<String,Integer> item2;
		String key_month;
		
		//For customer and product, count for each month, how many sales of the previous and how many sales of the following month had quantities between that month’s average sale and maximum sale
		HashMap<String,HashMap<String,Integer>> report3 = new HashMap<String,HashMap<String,Integer>>();
		HashMap<String,Integer> item3;
		String key_month_2;
		
		String usr ="postgres";
		String pwd ="";
		String url ="jdbc:postgresql://localhost:5432/huangzijing";

		try
		{
			Class.forName("org.postgresql.Driver");
			//System.out.println("Success loading Driver!");
		}
		catch(Exception e)
		{
			System.out.println(e);
			System.out.println("Fail loading Driver!");
			e.printStackTrace();
		}

		try
		{		
			Connection conn = DriverManager.getConnection(url, usr, pwd);
			//System.out.println("Success connecting server!");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Sales");
			
			while (rs.next())
			{
				 prod = rs.getString("prod");
				 cust = rs.getString("cust");
				 month = rs.getString("month");
				 quant = rs.getInt("quant");
				 key = prod+"_"+cust;
				 key_month = prod+"_"+cust+"_"+month;
				 
				//report1 
				if(!report1.containsKey(key)){
					item = new HashMap<String,Integer>();
					item.put("quant", quant);
					item.put("count", 1);
					Set<String> set = report1.keySet();
					int other_prod_quant = 0;
					int other_prod_count = 0;
					int other_cust_quant = 0;
					int other_cust_count = 0;
					for (String s:set) { 
						String s_prod = s.split("_")[0];
						String s_cust = s.split("_")[1];
						if(s_prod.equals(prod) && !s_cust.equals(cust)){							
							report1.get(s).put("other_prod_quant", report1.get(s).get("other_prod_quant")+quant);
							report1.get(s).put("other_prod_count", report1.get(s).get("other_prod_count")+1);
							other_prod_quant = report1.get(s).get("quant") + other_prod_quant;
							other_prod_count = report1.get(s).get("count") + other_prod_count;
						}
						if(!s_prod.equals(prod) && s_cust.equals(cust)){							
							report1.get(s).put("other_cust_quant", report1.get(s).get("other_cust_quant")+quant);
							report1.get(s).put("other_cust_count", report1.get(s).get("other_cust_count")+1);
							other_cust_quant = report1.get(s).get("quant") + other_cust_quant;
							other_cust_count = report1.get(s).get("count") + other_cust_count;
						}
					}
					
					item.put("other_prod_quant", other_prod_quant);
					item.put("other_prod_count", other_prod_count);
					item.put("other_cust_quant", other_cust_quant);
					item.put("other_cust_count", other_cust_count);
					report1.put(key,item);
				}else{
					item = new HashMap<String,Integer>();
					item = report1.get(key);
					item.put("quant", item.get("quant")+quant);
					item.put("count", item.get("count")+1);
					
					Set<String> set = report1.keySet();
					for (String s:set) { 
						String s_prod = s.split("_")[0];
						String s_cust = s.split("_")[1];
						if(s_prod.equals(prod) && !s_cust.equals(cust)){							
							report1.get(s).put("other_prod_quant", report1.get(s).get("other_prod_quant")+quant);
							report1.get(s).put("other_prod_count", report1.get(s).get("other_prod_count")+1);							
						}
						if(!s_prod.equals(prod) && s_cust.equals(cust)){							
							report1.get(s).put("other_cust_quant", report1.get(s).get("other_cust_quant")+quant);
							report1.get(s).put("other_cust_count", report1.get(s).get("other_cust_count")+1);							
						}
					}
	
					report1.put(key,item);
					
				}
				
				
				//report2
				if(!report2.containsKey(key_month)){
					
					Set<String> set = report2.keySet();
					int total_month_after_quant = 0;
					int total_month_after_count = 0;
					int total_month_before_quant = 0;
					int total_month_before_count = 0;
					for (String s:set) {
						String s_prod = s.split("_")[0];
						String s_cust = s.split("_")[1];
						String s_month = s.split("_")[2];
						if(prod.equals(s_prod) && cust.equals(s_cust)){
							if(Integer.parseInt(s_month) < Integer.parseInt(month)){
								total_month_before_quant = total_month_before_quant + report2.get(s).get("quant");
								total_month_before_count = total_month_before_count + 1;
								report2.get(s).put("month_after_quant", report2.get(s).get("month_after_quant")+quant);
								report2.get(s).put("month_after_count", report2.get(s).get("month_after_count")+1);
							}else if(Integer.parseInt(s_month) > Integer.parseInt(month)){
								
								total_month_after_quant = total_month_after_quant + report2.get(s).get("quant");
								total_month_after_count = total_month_after_count + 1;			
								report2.get(s).put("month_before_quant", report2.get(s).get("month_before_quant")+quant);
								report2.get(s).put("month_before_count", report2.get(s).get("month_before_count")+1);
							}
						}
						
					}
					item2 = new HashMap<String,Integer>();
					item2.put("month_after_quant", total_month_after_quant);
					item2.put("month_after_count", total_month_after_count);
					item2.put("month_before_quant", total_month_before_quant);
					item2.put("month_before_count", total_month_before_count);
					item2.put("quant", quant);
					item2.put("count", 1);
					report2.put(key_month,item2);
					
				}else{
					Set<String> set = report2.keySet();
					for (String s:set) { 
						String s_prod = s.split("_")[0];
						String s_cust = s.split("_")[1];
						String s_month = s.split("_")[2];
						if(prod.equals(s_prod) && cust.equals(s_cust)){
							if(Integer.parseInt(s_month) < Integer.parseInt(month)){
								report2.get(s).put("month_after_quant", report2.get(s).get("month_after_quant")+quant);
								report2.get(s).put("month_after_count", report2.get(s).get("month_after_count")+1);
							}else if(Integer.parseInt(s_month) > Integer.parseInt(month)){
								report2.get(s).put("month_before_quant", report2.get(s).get("month_before_quant")+quant);
								report2.get(s).put("month_before_count", report2.get(s).get("month_before_count")+1);
							}
						}
					}
					
				}
				
				
				//report3
				if(!report3.containsKey(key_month)){
					Set<String> set = report3.keySet();
					int total_month_after_tot = 0;
					int total_month_before_tot = 0;
					for (String s:set) {
						String s_prod = s.split("_")[0];
						String s_cust = s.split("_")[1];
						String s_month = s.split("_")[2];
						if(prod.equals(s_prod) && cust.equals(s_cust)){
							if(Integer.parseInt(s_month) < Integer.parseInt(month)){
								if(report3.get(s).get("quant")/report3.get(s).get("count")>quant){
									total_month_before_tot ++ ;
								}else{
									report3.get(s).put("month_after_tot", report3.get(s).get("month_after_tot")+1);
								}
							}else if(Integer.parseInt(s_month) > Integer.parseInt(month)){
								if(report3.get(s).get("quant")/report3.get(s).get("count")>quant){
									total_month_after_tot ++ ;
								}else{
									report3.get(s).put("month_before_tot", report3.get(s).get("month_before_tot")+1);
								}
					
							}
						}
						
					}
					item3 = new HashMap<String,Integer>();
					item3.put("month_before_tot", total_month_before_tot);
					item3.put("month_after_tot", total_month_after_tot);
					item3.put("quant", quant);
					item3.put("count", 1);
					report3.put(key_month,item3);
				}else{
					Set<String> set = report3.keySet();
//					
					int new_month_quant = report3.get(key_month).get("quant")+quant;
					int new_month_count = report3.get(key_month).get("count")+1;
					for (String s:set) { 
						String s_prod = s.split("_")[0];
						String s_cust = s.split("_")[1];
						String s_month = s.split("_")[2];
						
						if(prod.equals(s_prod) && cust.equals(s_cust)){
							if(Integer.parseInt(s_month) < Integer.parseInt(month)){
								//compare with the last relation: if the relation change, the tot value change too
								if(report3.get(s).get("quant")/report3.get(s).get("count") > report3.get(key_month).get("quant")/report3.get(key_month).get("count")){
									if(report3.get(s).get("quant")/report3.get(s).get("count") < new_month_quant/new_month_count){
										report3.get(key_month).put("month_before_tot", report3.get(key_month).get("month_before_tot")-1);
										report3.get(s).put("month_after_tot", report3.get(s).get("month_after_tot")+1);
									}
								}else{
									if(report3.get(s).get("quant")/report3.get(s).get("count") > new_month_quant/new_month_count){
										report3.get(key_month).put("month_before_tot", report3.get(key_month).get("month_before_tot")+1);
										report3.get(s).put("month_after_tot", report3.get(s).get("month_after_tot")-1);
									}
								}
							}else if(Integer.parseInt(s_month) > Integer.parseInt(month)){
								if(report3.get(s).get("quant")/report3.get(s).get("count") > report3.get(key_month).get("quant")/report3.get(key_month).get("count")){
									if(report3.get(s).get("quant")/report3.get(s).get("count") < new_month_quant/new_month_count){
										report3.get(key_month).put("month_after_tot", report3.get(key_month).get("month_after_tot")-1);
										report3.get(s).put("month_before_tot", report3.get(s).get("month_before_tot")+1);
									}
								}else{
									if(report3.get(s).get("quant")/report3.get(s).get("count") > new_month_quant/new_month_count){
										report3.get(key_month).put("month_after_tot", report3.get(key_month).get("month_after_tot")+1);
										report3.get(s).put("month_before_tot", report3.get(s).get("month_before_tot")-1);
									}
								}
							}	
						}
					}
					report3.get(key_month).put("quant", report3.get(key_month).get("quant")+quant);
					report3.get(key_month).put("count", report3.get(key_month).get("count")+1);
					
				}
				
				//verify
//				if(prod.equals("Coke")&&cust.equals("Helen")){
//					Set<String> set = report3.keySet();
//					for (String s:set) { 
//						String s_prod = s.split("_")[0];
//						String s_cust = s.split("_")[1];
//						String s_month = s.split("_")[2];
//						if(s_prod.equals("Coke")&&s_cust.equals("Helen")){
//							System.out.println(s + "|" + report3.get(s) + "|" + report3.get(s).get("quant")/report3.get(s).get("count"));
//						}
//					}
//					System.out.println("-------------");
//				}

				//System.out.println(rs.getString("cust") + " " + rs.getString("prod") + " " + rs.getString("day") + " " +
				//	   	rs.getString("month") + " " + rs.getString("year") + " " + rs.getString("quant"));
			}
		}
		catch(SQLException e)
		{
			System.out.println();
			System.out.println("Connection URL or username or password errors!");
			e.printStackTrace();
		}
		
		//format and output
		//report1
		Set<String> output_set = report1.keySet();
		for (String s_output:output_set) {
			String output_product = s_output.split("_")[0];
			String output_customer = s_output.split("_")[1];
			int output_the_avg = report1.get(s_output).get("quant")/report1.get(s_output).get("count");
			int output_other_prod_avg = report1.get(s_output).get("other_prod_quant")/report1.get(s_output).get("other_prod_count");			
			int	output_other_cust_avg = report1.get(s_output).get("other_cust_quant")/report1.get(s_output).get("other_cust_count");			
			System.out.println(output_product+" "+output_customer+" "+output_the_avg+" "+output_other_prod_avg+" "+output_other_cust_avg);			
		}
		
		//report2
		System.out.println("==========================");
		Set<String> output_set2 = report2.keySet();
		for (String s_output:output_set2) {	
			String output_product = s_output.split("_")[0];
			String output_customer = s_output.split("_")[1];
			String output_month = s_output.split("_")[2];
			int output_before_avg;
			int output_after_avg;
			if(report2.get(s_output).get("month_before_count")!=0){
				output_before_avg = report2.get(s_output).get("month_before_quant")/report2.get(s_output).get("month_before_count");
			}else{
				output_before_avg = 0;
			}
			
			if(report2.get(s_output).get("month_after_count")!=0){
				output_after_avg = report2.get(s_output).get("month_after_quant")/report2.get(s_output).get("month_after_count");
			}else{
				output_after_avg = 0;
			} 
			System.out.println(output_product+" "+output_customer+" "+output_month+" "+output_before_avg+" "+output_after_avg);			
		}
		
		//report3
		System.out.println("==========================");
		Set<String> output_set3 = report3.keySet();
		for (String s_output:output_set3) {	
			String output_product = s_output.split("_")[0];
			String output_customer = s_output.split("_")[1];
			String output_month = s_output.split("_")[2];
			int output_before_tot = report3.get(s_output).get("month_before_tot");
			int output_after_tot = report3.get(s_output).get("month_after_tot");
			System.out.println(output_product+" "+output_customer+" "+output_month+" "+output_before_tot+" "+output_after_tot);
		}
		
		//System.out.println(report3);
		//System.out.println(cust_avg_sale_of_other_prod);
		//System.out.println(prod_avg_sale_of_other_cust);
		
	}

}
