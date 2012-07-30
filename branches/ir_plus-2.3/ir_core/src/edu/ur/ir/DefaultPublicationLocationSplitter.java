/**  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  


package edu.ur.ir;

/**
 * Splits out the publisher from the location.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultPublicationLocationSplitter implements PublicationLocationSplitter {
	
	public String[] split(String publisher)
	{
		String[] values = {"", publisher};
		int index = -1;
		
		// deals with format of Location : Publisher
		if( (index = publisher.indexOf(':')) != -1 )
		{
			String pub = publisher.substring(0, index).trim();
			if( pub.startsWith("[") && pub.length() > 1)
			{
				pub = pub.substring(1); 
			}
			if( pub.endsWith("]") && pub.length() > 1)
			{
				pub = pub.substring(0, pub.length() - 1);
			}
			values[0] = pub;
			if( (index + 2) < publisher.length() )
			{	
			    values[1] = publisher.substring( index + 2, publisher.length()).trim();
			}
			else
			{
				values[1] = "";
			}
		}
		// deals with format Location, Publisher
		else if( (index = publisher.indexOf(',')) != -1 )
		{
            String pub = publisher.substring(0, index).trim();
			if( pub.startsWith("[") && pub.length() > 1)
			{
				pub = pub.substring(1); 
			}
			if( pub.endsWith("]") && pub.length() > 1)
			{
				pub = pub.substring(0, pub.length() - 1);
			}
			values[0] = pub;
			
			if( (index + 2) < publisher.length() )
			{	
			    values[1] = publisher.substring( index + 2, publisher.length()).trim();
			}
			else
			{
				values[1] = "";
			}
		}
		// deals with format [Location] Publisher
		else if( (index = publisher.indexOf(']')) != -1)
		{
		   if( publisher.indexOf('[') < index  && publisher.indexOf('[') != -1)
		   {
			   int beginIndex =  publisher.indexOf('[') + 1;
			   values[0] = publisher.substring(beginIndex, index).trim();
			   if( (index + 2) < publisher.length() )
			   {	
				    values[1] = publisher.substring( index + 2, publisher.length()).trim();
			   }
			   else
			   {
			        values[1] = "";
			   }
		   }
		   else
		   {
			   values[0] = publisher.substring(0, index).trim();
			   if( (index + 2) < publisher.length() )
			   {	
				    values[1] = publisher.substring( index + 2, publisher.length()).trim();
			   }
			   else
			   {
			        values[1] = "";
			   }
		   }
			
		}
			
		
		return values;
	}
	
	public static void main(String[] args)
	{
		DefaultPublicationLocationSplitter splitter = new DefaultPublicationLocationSplitter();
		String[] values = splitter.split("Moscou : P. Jurgenson,");
		System.out.println("value 0 = " + values[0] + " value 1 = " + values[1]);
		values = splitter.split("London, Pitt & Hatzefeld");
		System.out.println("value 0 = " + values[0] + " value 1 = " + values[1]);
		values = splitter.split("Paris, [Heugel et Fils] Impr. a. Chaix et Cie");
		System.out.println("value 0 = " + values[0] + " value 1 = " + values[1]);
		values = splitter.split("Paris, [Heugel et Fils] Impr. a. Chaix et Cie");
		System.out.println("value 0 = " + values[0] + " value 1 = " + values[1]);
		values = splitter.split("Cincinnati]: J. Church");
		System.out.println("value 0 = " + values[0] + " value 1 = " + values[1]);
		values = splitter.split("[Paris] W. Bessel");
		System.out.println("value 0 = " + values[0] + " value 1 = " + values[1]);
		values = splitter.split("Hello");
		System.out.println("value 0 = " + values[0] + " value 1 = " + values[1]);
		values = splitter.split("[Paris]: W. Bessel");
		System.out.println("value 0 = " + values[0] + " value 1 = " + values[1]);
		values = splitter.split("[Paris], W. Bessel");
		System.out.println("value 0 = " + values[0] + " value 1 = " + values[1]);
		values = splitter.split(" [Paris] : W. Bessel");
		System.out.println("value 0 = " + values[0] + " value 1 = " + values[1]);
		values = splitter.split(" [Paris] , W. Bessel");
		System.out.println("value 0 = " + values[0] + " value 1 = " + values[1]);
	}

}
