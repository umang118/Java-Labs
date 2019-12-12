/*
 * @author: Umangkumar Patel
 * Student Number: 040918355
 * Email: pate0585@algonquinlive.com
 * */



package utility;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import utility.IntersectUtil;

class IntersectTestNormal{

	private boolean doesIntersect;
	private double[] intersect;

	@BeforeEach
	void setUp() throws Exception{
		intersect = new double[4];
	}


	
	
	@Test
	public void testABAD(){

		//intersect array is used as buffer for result. we pass it as argument to prevent remake of array.
		//instead use the same array over and over again.
		//call assertTrue if doesIntersect is expected to be true or assertFalse if expected to be false
		//user assertEquals to compare indexes 0, 1 and 2 to expected data in the pdf or csv.
		//make sure to use assertEquals that takes delta as third argument when comparing double.
		//intersect[0] is px which is the x of intersect point
		//intersect[1] is py which is the y of intersect point
		//intersect[2] is ray scalar which is the scaler distance of ray to segment line.
		//ray is the first 4 numbers and segment is the next 4 numbers.
		//ray has starting point and end point is just direction the length of ray is infinite from start toward end.
		//segment start and end are set and length of it is limited to what is given.
	

		
		try {
			File file = new File("./test/resources/data.csv");
			
			Scanner sc = new Scanner(file);
			
			sc.nextLine();
			sc.nextLine();
			
			String i ="";
		
			
			 
			
			while(sc.hasNext()) {
				 i = sc.next();
				  
				 String []s = i.split(",");
				 
				 
				 double d1= Double.valueOf(s[0]);
				 
				// Double d1 = new Double(s[0]);
				 Double d2 = new Double(s[1]);
				 Double d3 = new Double(s[2]);
				 Double d4 = new Double(s[3]);
				 Double d5 = new Double(s[4]);
				 Double d6 = new Double(s[5]);
				 Double d7 = new Double(s[6]);
				 Double d8 = new Double(s[7]);
				 
				
				doesIntersect = IntersectUtil.getIntersection(intersect, d1, d2, d3, d4, d5, d6, d7, d8);
				
				if(doesIntersect) {
					
					
					
					Double d9 = new Double(s[9]);
					Double d10 = new Double(s[10]);
					Double d11 = new Double(s[11]);
					
	
					assertEquals(d9,intersect[0] ,0.0001 );
					assertEquals(d10, intersect[1] ,0.0001);
					assertEquals(d11, intersect[2] ,0.0001);
					
					assertTrue(doesIntersect,s[8]);
					
					
				}else
				{
				
					assertFalse(doesIntersect);
					
					
				}
				 
				 
				 
				
				
			}
			
			
		}catch (Exception e) {
			System.out.println(e);
		}
		
		
	
	}
}
