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
		
		//report2
		HashMap<String,HashMap<String,Integer>> report2 = new HashMap<String,HashMap<String,Integer>>();
		HashMap<String,Integer> item2;
		String key_month;
		
		//report3
		HashMap<String,HashMap<String,Integer>> report3 = new HashMap<String,HashMap<String,Integer>>();
		HashMap<String,Integer> item3;
		
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
						if(!s_prod.equals(prod) && s_cust.equals(cust)){							
							report1.get(s).put("other_prod_quant", report1.get(s).get("other_prod_quant")+quant);
							report1.get(s).put("other_prod_count", report1.get(s).get("other_prod_count")+1);
							other_prod_quant = report1.get(s).get("quant") + other_prod_quant;
							other_prod_count = report1.get(s).get("count") + other_prod_count;
						}
						if(s_prod.equals(prod) && !s_cust.equals(cust)){							
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
						if(!s_prod.equals(prod) && s_cust.equals(cust)){							
							report1.get(s).put("other_prod_quant", report1.get(s).get("other_prod_quant")+quant);
							report1.get(s).put("other_prod_count", report1.get(s).get("other_prod_count")+1);							
						}
						if(s_prod.equals(prod) && !s_cust.equals(cust)){							
							report1.get(s).put("other_cust_quant", report1.get(s).get("other_cust_quant")+quant);
							report1.get(s).put("other_cust_count", report1.get(s).get("other_cust_count")+1);							
						}
					}
					report1.put(key,item);				
				}
				
				//report2
				if(!report2.containsKey(key_month)){	
					item2 = new HashMap<String,Integer>();
					item2.put("sum_quant", quant);
					item2.put("count", 1);
					item2.put("avg", quant);
					report2.put(key_month,item2);					
				}else{
					report2.get(key_month).put("sum_quant", report2.get(key_month).get("sum_quant")+quant);
					report2.get(key_month).put("count", report2.get(key_month).get("count")+1);
					report2.get(key_month).put("avg", (report2.get(key_month).get("sum_quant"))/(report2.get(key_month).get("count")));					
				}
				
				//report3
				if(!report3.containsKey(key_month)){
					item3 = new HashMap<String,Integer>();
					item3.put("sum_quant", quant);
					item3.put("count", 1);
					item3.put("max_quant", quant);
					item3.put("avg", quant);
					report3.put(key_month,item3);	
				}else{
					report3.get(key_month).put("sum_quant", report3.get(key_month).get("sum_quant")+quant);
					report3.get(key_month).put("count", report3.get(key_month).get("count")+1);
					report3.get(key_month).put("avg", (report3.get(key_month).get("sum_quant"))/(report3.get(key_month).get("count")));	
					if(quant>report3.get(key_month).get("max_quant")){
						report3.get(key_month).put("max_quant",quant);
					}
				}		

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
		
		//report3 handling
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
				 String before_key_month = prod+"_"+cust+"_"+(Integer.parseInt(month)-1);
				 String after_key_month = prod+"_"+cust+"_"+(Integer.parseInt(month)+1);
				 if(report3.containsKey(before_key_month)){
					 if(report3.get(before_key_month).get("avg")<=quant && quant<=report3.get(before_key_month).get("max_quant")){
						 if(report3.get(before_key_month).containsKey("after_tot")){
							 report3.get(before_key_month).put("after_tot", report3.get(before_key_month).get("after_tot")+1);
						 }else{
							 report3.get(before_key_month).put("after_tot",1);
						 }
					 }
				 }
				 if(report3.containsKey(after_key_month)){
					 if(report3.get(after_key_month).get("avg")<=quant && quant<=report3.get(after_key_month).get("max_quant")){
						 if(report3.get(after_key_month).containsKey("before_tot")){
							 report3.get(after_key_month).put("before_tot", report3.get(after_key_month).get("before_tot")+1);
						 }else{
							 report3.get(after_key_month).put("before_tot",1);
						 }
					 }
				 }
			}
		}catch(SQLException e)
		{
			System.out.println();
			System.out.println("Connection URL or username or password errors!");
			e.printStackTrace();
		}
		
		
		
		//format and output
		//report1
		System.out.println("CUSTOMER PRODUCT THE_AVG OTHER_PROD_AVG OTHER_CUST_AVG");
		System.out.println("======== ======= ======= ============== ==============");
		Set<String> output_set = report1.keySet();
		for (String s_output:output_set) {
			String output_product = s_output.split("_")[0];
			String output_customer = s_output.split("_")[1];
			int output_the_avg = report1.get(s_output).get("quant")/report1.get(s_output).get("count");
			int output_other_prod_avg = report1.get(s_output).get("other_prod_quant")/report1.get(s_output).get("other_prod_count");			
			int	output_other_cust_avg = report1.get(s_output).get("other_cust_quant")/report1.get(s_output).get("other_cust_count");			
			//System.out.println(output_product+" "+output_customer+" "+output_the_avg+" "+output_other_prod_avg+" "+output_other_cust_avg);
			System.out.format("%-8s %-7s %7d %14d %14d\n",output_customer,output_product,output_the_avg,output_other_prod_avg,output_other_cust_avg);
		}
		
		//report2
		System.out.println("\n");
		System.out.println("CUSTOMER PRODUCT MONTH BEFORE_AVG AFTER_AVG");
		System.out.println("======== ======= ===== ========== =========");
		Set<String> output_set2 = report2.keySet();
		for (String s_output:output_set2) {	
			String output_product = s_output.split("_")[0];
			String output_customer = s_output.split("_")[1];
			String output_month = s_output.split("_")[2];
			String before_key = output_product+"_"+output_customer+"_"+(Integer.parseInt(output_month)-1);
			Object output_before_avg;
			if(report2.containsKey(before_key)){
				output_before_avg = report2.get(before_key).get("avg");
			}else{
				output_before_avg = null;
			}
			String after_key = output_product+"_"+output_customer+"_"+(Integer.parseInt(output_month)+1);
			Object output_after_avg;
			if(report2.containsKey(after_key)){
				output_after_avg = report2.get(after_key).get("avg");
			}else{
				output_after_avg = null;
			}
			
			//System.out.println(output_product+" "+output_customer+" "+output_month+" "+output_before_avg+" "+output_after_avg);
			System.out.format("%-8s %-7s %5s %10d %9d\n",output_customer,output_product,output_month,output_before_avg,output_after_avg);
		}

		
		//report3
		HashMap<String,HashMap<String,Integer>> report3_output = new HashMap<String,HashMap<String,Integer>>();
		HashMap<String,Integer> item3_output;
		//System.out.println("CUSTOMER PRODUCT MONTH BEFORE_TOT AFTER_TOT");
		//System.out.println("======== ======= ===== ========== =========");
		Set<String> output_set3 = report3.keySet();
		for (String s_output:output_set3) {	
			String output_product = s_output.split("_")[0];
			String output_customer = s_output.split("_")[1];
//			String output_month = s_output.split("_")[2];
//			Object output_before_tot;
//			if(report3.get(s_output).containsKey("before_tot")){
//				output_before_tot = report3.get(s_output).get("before_tot");
//			}else{
//				output_before_tot = null;
//			}
//			Object output_after_tot;
//			if(report3.get(s_output).containsKey("after_tot")){
//				output_after_tot = report3.get(s_output).get("after_tot");
//			}else{
//				output_after_tot = null;
//			}
//			if("Butter_Helen".equals(output_product+"_"+output_customer)){
//				System.out.println(report3.get(s_output));
//			}
			
			if(report3_output.containsKey(output_product+"_"+output_customer)){
				if(report3.get(s_output).containsKey("before_tot")){
					report3_output.get(output_product+"_"+output_customer).put("before_tot", report3_output.get(output_product+"_"+output_customer).get("before_tot")+report3.get(s_output).get("before_tot"));
				}
				if(report3.get(s_output).containsKey("after_tot")){
					report3_output.get(output_product+"_"+output_customer).put("after_tot", report3_output.get(output_product+"_"+output_customer).get("after_tot")+report3.get(s_output).get("after_tot"));
				}
			}else{
				item3_output = new HashMap<String,Integer>();
				
				if(report3.get(s_output).containsKey("before_tot")){
					item3_output.put("before_tot",report3.get(s_output).get("before_tot"));
					report3_output.put(output_product+"_"+output_customer,item3_output);
				}else{
					item3_output.put("before_tot",0);
					report3_output.put(output_product+"_"+output_customer,item3_output);
				}
				report3_output.put(output_product+"_"+output_customer,item3_output);
				if(report3.get(s_output).containsKey("after_tot")){
					item3_output.put("after_tot",report3.get(s_output).get("after_tot"));
					//report3_output.get(output_product+"_"+output_customer).put("after_tot",report3_output.get(s_output).get("after_tot"));
				}else{
					item3_output.put("after_tot",0);
					//report3_output.get(output_product+"_"+output_customer).put("after_tot",0);
				}
				report3_output.put(output_product+"_"+output_customer,item3_output);
			}
			
//			if("Butter_Helen".equals(output_product+"_"+output_customer)){
//				System.out.println(report3_output);
//			}
			
			//System.out.println(output_customer+" "+output_product+" "+output_month+" "+output_before_tot+" "+output_after_tot);
			//System.out.format("%-8s %-7s %5s %10d %9d\n",output_customer,output_product,output_month,output_before_tot,output_after_tot);
		}
		
		System.out.println("\n");
		System.out.println("CUSTOMER PRODUCT BEFORE_TOT AFTER_TOT");
		System.out.println("======== ======= ========== =========");
		Set<String> output_set4 = report3_output.keySet();
		for (String s_output1:output_set4) {	
			String output_product = s_output1.split("_")[0];
			String output_customer = s_output1.split("_")[1];
			int output_before_tot = report3_output.get(s_output1).get("before_tot");
			int output_after_tot = report3_output.get(s_output1).get("after_tot");
			//System.out.println(output_customer+" "+output_product+" "+output_before_tot+" "+output_after_tot);
			System.out.format("%-8s %-7s %10d %9d\n",output_customer,output_product,output_before_tot,output_after_tot);
		}
		
		//System.out.println(report3_output);
		//System.out.println(cust_avg_sale_of_other_prod);
		//System.out.println(prod_avg_sale_of_other_cust);
		
	}

}
