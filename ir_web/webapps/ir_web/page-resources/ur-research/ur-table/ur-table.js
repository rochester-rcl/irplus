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

// Declare a table class
// which can be instanciated with the correct
// variables

function UrTable(formName, div)
{
    /** form name to be used */
    this.formName = formName;
    
    /** the div that the content will be inserted into**/
    this.div = div;
    
    /** reference to this object for call backs */
    var urTable = this;
    
    /** The next value  in the array of callback methods*/
    this.nextValue = 0;
    
    /** callback method to handle form success 
        this updates the div with the given text
     */
    this.formSuccess = function(o)
    {
        var divToUpdate = document.getElementById(urTable.div);
        divToUpdate.innerHTML = o.responseText;
    };
    
    /** function to handle form failure*/
    this.formFailure = function(o)
    {
        alert('UrTable Failure ' + o.status + ' status text ' + o.statusText );
    };
    
    /** Submit the from to the specified action*/
    this.submitForm = function(action)
    {
        YAHOO.util.Connect.setForm(formName);
        var cObj = YAHOO.util.Connect.asyncRequest('POST',
        action, urTable.formSubmitCallback);
    };
    
    /** Callback for dealing with success and failure */
    urTable.formSubmitCallback = 
    {
         success: urTable.formSuccess,
         failure: urTable.formFailure
    };
}


UrTable.prototype = {
    /** place a sort as the input field and submit the form*/
    addSort : function(position, property, order, action, ignoreCase)
    { 
        var inputField = document.getElementById(this.formName + '_sort_' + property);
        inputField.value = order + '_' + position + '_' + ignoreCase;
        this.submitForm(action);
    }
};

/**
 * Create a dynamic filter
 */
function DynamicFilter(urTable, filter, property, action) {
	this.property = property;
	this.filter = filter;
    this.urTable = urTable;
	
	this.urAddFilterText = myUrAddFilterText;
	this.urAddFilterToInput = myUrAddFilterToInput;
	
    var dynamicFilter = this;
	var dynamicFilterDiv = document.getElementById('dynFilterDiv');
	
	
	// already have a filter
	if (dynamicFilterDiv) {
		return; 
	}
	
	// create the div tag
	dynamicFilterDiv = document.createElement('div');
	dynamicFilterDiv.id = 'dynFilterDiv';
	filter.appendChild(dynamicFilterDiv);

	// create the input tag
	var dynFilterInput = document.createElement('input');
	dynFilterInput.type = 'text';
	dynFilterInput.id = 'dynFilterInput';
	dynFilterInput.name = 'filter';
	
	var inputValue = filter.firstChild.nodeValue;
	if (!inputValue) {
		inputValue = '';
	}
	
	dynFilterInput.value = inputValue;
	dynamicFilterDiv.appendChild(dynFilterInput);

	/**
	 * Callback handler for a keypress
	 */
	this.handleKeypress = function(event)
	{
	    if (event.keyCode == 13) {
		    var dynamicFilterDiv = document.getElementById('dynFilterDiv');
		    var dynFilterInput = document.getElementById('dynFilterInput');
		    dynamicFilter.urAddFilterText(dynamicFilterDiv);
		    dynamicFilter.urAddFilterToInput(dynFilterInput.value);
		    dynamicFilter.urTable.submitForm(action);
	    }
	};
	
	/**
	 * Callback handler for on blur
	 */
	this.handleBlur = function(event)
	{
	    var dynamicFilterDiv = document.getElementById('dynFilterDiv');
	    var dynFilterInput = document.getElementById('dynFilterInput');
	    dynamicFilter.urAddFilterText(dynamicFilterDiv);
	    dynamicFilter.urAddFilterToInput(dynFilterInput.value);
	};
	
	// use the yahoo libraries to attach events.
	YAHOO.util.Event.addListener('dynFilterInput',"keypress", dynamicFilter.handleKeypress);
	YAHOO.util.Event.addListener('dynFilterInput', "blur",  dynamicFilter.handleBlur);
	
	dynFilterInput.focus();
	
	// add text to the filter if a user has entered text into
	// the input.
	function myUrAddFilterText(dynamicFilterDiv)
	{
		var dynFilterInput = document.getElementById('dynFilterInput');
	    var inputValue = dynFilterInput.value;
	    var textNode = document.createTextNode(inputValue);
	
	    var node = dynamicFilter.filter.firstChild;
	    if (node.nodeType == 3) { // node type of text
		    dynamicFilter.filter.replaceChild(textNode, node);
	    } else {
		    dynamicFilter.filter.insertBefore(textNode, node);
	    }
	    dynamicFilter.filter.removeChild(dynamicFilterDiv);
	}
	
	// 
	function myUrAddFilterToInput(value)
	{
	    var inputField = document.getElementById(dynamicFilter.urTable.formName + 
	        '_filter_' + 
	        dynamicFilter.property);
	        
        inputField.value = value;
	}
	
}

