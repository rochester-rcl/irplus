/*
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

/**
 * Utilities 
 */
var urUtil = {};

/*
 * trims the white space
 */
urUtil.trim = function(stringValue)
{
     return stringValue.replace(/(^\s+|\s+$)/g, "");
}

// Check all checkbox - field to set and
// the value true or false	
urUtil.setCheckboxes = function(field, boolValue) {

   if( field != null )
   {
       for (i=0;i<field.length;i++) 
       {
           field[i].checked=boolValue;
       }
   }
}

// Validates the email format
urUtil.emailcheck = function(str) {

		var at="@"
		var dot="."
		var lat=str.indexOf(at)
		var lstr=str.length
		var ldot=str.indexOf(dot)
		if (str.indexOf(at)==-1){
		   return false
		}

		if (str.indexOf(at)==-1 || str.indexOf(at)==0 || str.indexOf(at)==lstr){
		   return false
		}

		if (str.indexOf(dot)==-1 || str.indexOf(dot)==0 || str.indexOf(dot)==lstr){
		    return false
		}

		 if (str.indexOf(at,(lat+1))!=-1){
		    return false
		 }

		 if (str.substring(lat-1,lat)==dot || str.substring(lat+1,lat+2)==dot){
		    return false
		 }

		 if (str.indexOf(dot,(lat+2))==-1){
		    return false
		 }
		
		 if (str.indexOf(" ")!=-1){
		    return false
		 }

 		 return true;					
}

	
// Check if at least 1 checkbox is checked		
urUtil.checkForNoSelections = function(field) {
	  if( field == null )
	  {
	      return false;
	  }
	
	  if ( !field.length && field.checked) {
	    return true;
	  }
	
	  for (var i = 0; i < field.length; i++) {
	    if (field[i].checked) {
			return true;
	    }
	  }
	
	  return false;
}
