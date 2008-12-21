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

YAHOO.namespace("ur.table");

/**
 * Constructor for a table
 */
YAHOO.ur.table.Table = function(formName, div)
{

    /** form name to be used */
    this.myFormName = formName;
    
    /** the div that the content will be inserted into**/
    this.myDiv = div;
    
    /** reference to the table */
    this.myTable = this;
}

/**
 * Successful submission of form action
 */
YAHOO.ur.table.Table.prototype.formSuccess = function(o)
{
    var divToUpdate = document.getElementById(this.myTable.myDiv);
    divToUpdate.innerHTML = o.responseText;
}

/**
 *  What to do if the form submission fails
 */
YAHOO.ur.table.Table.prototype.formFailure = function(o)
{
    alert('UrTable Failure ' + o.status + ' status text ' + o.statusText );
}

/**
 * Submit the form
 */
YAHOO.ur.table.Table.prototype.submitForm = function(action)
{
    YAHOO.util.Connect.setForm(this.myFormName);
    var cObj = YAHOO.util.Connect.asyncRequest('POST',
        action, {myTable: this.myTable, success: this.formSuccess, 
                   failure: this.formFailure});
}

