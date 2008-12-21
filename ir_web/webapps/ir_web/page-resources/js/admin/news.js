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
 * This code is for dealing with adding and removing folders 
 * in the workspace.
 */
YAHOO.namespace("ur.news");

// action to perform when submitting the news items.
var myNewsItemAction = basePath + 'admin/getNewsItems.action';

// actions for adding and removing folders
var updateNewsItemAction = basePath + 'admin/updateNewsItem.action';
var newNewsItemAction = basePath + 'admin/createNewsItem.action';
var deleteNewsItemAction = basePath + 'admin/deleteNewsItem.action';
var editNewsItemAction = basePath + 'admin/editNewsItem.action';

// object to hold the specified news item data.
var myNewsItemTable = new YAHOO.ur.table.Table('myNewsItems', 'newNewsItems');


YAHOO.ur.news = 
{
    /**
     * function to get all news items
     */
    getNewsItems : function(rowStart, startPageNumber, currentPageNumber, order)
    {
        var callback =
        {
            success: function(o) 
            {
                var divToUpdate = document.getElementById('newNewsItems');
                divToUpdate.innerHTML = o.responseText; 
            },
	
	        failure: function(o) 
	        {
	            alert('Get news item Failure ' + o.status + ' status text ' + o.statusText );
	        }
        }
        
         var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            myNewsItemAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(), 
            callback, null);
    },  
    
    /**
     * clear the news form
     */
    clearNewsItemForm : function()
    {
        var div = document.getElementById('newsItemError');
        div.innerHTML = "";
        document.getElementById('newNewsItemForm_name').value = "";
	    document.getElementById('newNewsItemForm_id').value = "";
	    document.newNewsItemForm.newNewsItem.value = "true";
    },
    
    /**
     * Set all the checkboxes
     */
    setCheckboxes : function()
    {
         checked = document.myNewsItems.checkAllSetting.checked;
         var newsItemIds = document.getElementsByName('newsItemIds');
         urUtil.setCheckboxes(newsItemIds, checked);
    },
    
    /**
     * Create a new news item dialog
     */
    createNewNewsItemDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        this.submit();
	    };
		
	    // handle a cancel of the adding news item dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.news.newNewsItemDialog.hide();
	        YAHOO.ur.news.clearNewsItemForm();
	    };
	    
	    var handleSuccess = function(o) 
	    {
	        //get the response from adding a news item
	        var response = eval("("+o.responseText+")");
	    
	        //if the news item was not added then show the user the error message.
	        // received from the server
	        if( response.newsItemAdded == "false" )
	        {
	            var newsItemNameError = document.getElementById('newsItemError');
	            newsItemNameError.innerHTML = '<p id="newNewsItemForm_nameError">' + response.message + '</p>';
                YAHOO.ur.news.newNewsItemDialog.showDialog();
               myNewsItemTable.submitForm(myNewsItemAction);
	        }
	        else
	        {
	            // we can clear the form if the news item was added
	            YAHOO.ur.news.clearNewsItemForm();
	            YAHOO.ur.news.newNewsItemDialog.hide();
	            window.location = editNewsItemAction + '?id=' + response.newsItemId;
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) {
	        alert('News Item submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new news item button is clicked.
	    YAHOO.ur.news.newNewsItemDialog = new YAHOO.widget.Dialog('newNewsItemDialog', 
            { width : "500px",
		      visible : false, 
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					      { text:'Cancel', handler:handleCancel } ]
		     } );
		     
		// override the submit 
        YAHOO.ur.news.newNewsItemDialog.submit = function()
        {
        	YAHOO.util.Connect.setForm('newNewsItemForm');
	        if( YAHOO.ur.news.newNewsItemDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new news item) based on the action.
                var action = newNewsItemAction;
	            if( document.newNewsItemForm.newNewsItem.value != 'true')
	            {
	               action = updateNewsItemAction;
	            }

                var cObj = YAHOO.util.Connect.asyncRequest('post',
                action, callback);
            }
        },
        
        // show and center the dialog
        YAHOO.ur.news.newNewsItemDialog.showDialog = function()
        {
            YAHOO.ur.news.newNewsItemDialog.center();
            YAHOO.ur.news.newNewsItemDialog.show();
        };

 	    // Validate the entries in the form to require that a name is entered
	    YAHOO.ur.news.newNewsItemDialog.validate = function() 
	    {
	        var name = document.getElementById('newNewsItemForm_name').value;
		    if (name == "" || name == null) 
		    {
		        alert('A News item name must be entered');
			    return false;
		    } 
		    else 
		    {
			    return true;
		    }
	    };

	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.news.newNewsItemDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showNewsItem", "click", 
	        YAHOO.ur.news.newNewsItemDialog.showDialog, 
	        YAHOO.ur.news.newNewsItemDialog, true);
    },
    
    /**
     * edit the news item
     */
    editNewsItem : function(id, name, article, dateAvailable, dateRemoved)
    {
        document.getElementById('newNewsItemForm_name').value = name;
	    document.getElementById('newNewsItemForm_id').value = id;
	    document.newNewsItemForm.newNewsItem.value = "false";
	    YAHOO.ur.news.newNewsItemDialog.show();
    },
    
    /**
     * Clear the delete news item form
     */
    clearDeleteNewsItemForm : function()
    {
        var div = document.getElementById('newsItemError');
        div.innerHTML = "";
    },
    
    /**
     * Create the dialog for dealing with deleting
     */
    createDeleteNewsItemDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        YAHOO.util.Connect.setForm('myNewsItems');
	    
	        //delete the news item 
            var cObj = YAHOO.util.Connect.asyncRequest('post',
            deleteNewsItemAction, callback);
	    };
	
	    // handle a cancel of deleting news item dialog
	    var handleCancel = function() {
	        YAHOO.ur.news.deleteNewsItemDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        //get the response from adding a news item 
	        var response = eval("("+o.responseText+")");
	    
	        //if the news item was not deleted then show the user the error message.
	        // received from the server
	        if( response.newsItemDeleted == "false" )
	        {
	            var deleteNewsItemError = document.getElementById('form_deleteNewsItemError');
                deleteNewsItemError.innerHTML = '<p id="newDeleteNewsItemError">' 
                + response.message + '</p>';
                YAHOO.ur.news.deleteNewsItemDialog.showDialog();
	        }
	        else
	        {
	            // we can clear the form if the news item were deleted
	            YAHOO.ur.news.deleteNewsItemDialog.hide();
	            YAHOO.ur.news.clearDeleteNewsItemForm();
	        }
	        // reload the table
	        myNewsItemTable.submitForm(myNewsItemAction);
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	        alert('news item submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new news item button is clicked.
	    YAHOO.ur.news.deleteNewsItemDialog = new YAHOO.widget.Dialog('deleteNewsItemDialog', 
        { width : "500px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
					  { text:'No', handler:handleCancel } ]
		 } );
		
        YAHOO.ur.news.deleteNewsItemDialog.showDialog = function()
        {
            YAHOO.ur.news.deleteNewsItemDialog.center();
            YAHOO.ur.news.deleteNewsItemDialog.show();
        }
        
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
			
	    // Render the Dialog
	    YAHOO.ur.news.deleteNewsItemDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showDeleteNewsItem", "click", 
	        YAHOO.ur.news.deleteNewsItemDialog.showDialog, 
	        YAHOO.ur.news.deleteNewsItemDialog, true);
    },
    
    init : function()
    {
        YAHOO.ur.news.getNewsItems(0,1,1,'asc');
        YAHOO.ur.news.createNewNewsItemDialog();
        YAHOO.ur.news.createDeleteNewsItemDialog();
    }
    
      
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.news.init);