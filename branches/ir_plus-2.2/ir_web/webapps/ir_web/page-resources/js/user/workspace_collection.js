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
 * This code is for dealing with adding and removing collections 
 * in the workspace.
 */
YAHOO.namespace("ur.personal.collection");

// action to perform when submitting the personal collection form.
var myCollectionAction = basePath + 'user/viewPersonalCollections.action';

// actions for adding and removing collections
var updateCollectionAction = basePath + 'user/updatePersonalCollection.action';
var newCollectionAction = basePath + 'user/addPersonalCollection.action';
var deleteCollectionAction = basePath + 'user/deletePersonalCollectionSystemObjects.action';
var getCollectionNameAction = basePath + 'user/getPersonalCollection.action';

// object to hold the specified collection data.
var myPersonalCollectionsTable = new YAHOO.ur.table.Table('myCollections', 'newPersonalCollections');

// actions for moving collections
var myAvailableCollectionMoveAction = basePath + 'user/availablePersonalCollectionsMove.action';
var moveAction = basePath + 'user/movePersonalCollectionSystemObjects.action';
var addItemAction = basePath + 'user/addItemToCollection.action';

// Action to edit Publication
var viewEditPublicationAction = basePath + 'user/viewEditItem.action';
var createPublicationVersionToEditAction = basePath + 'user/createPublicationVersionForEdit.action';

// array to hold drop down menus created
// this will be needed to later destroy the menus
// otherwise the menus will not render correctly
// in subsequent ajax calls.
var collectionMenuArray = new Array();


YAHOO.ur.personal.collection = 
{
   /**
    * Set the sort type [ asc, desc, none ]
    * Set the element to sort on
    */
    updateSort : function(sortType, sortElement)
    {
        document.getElementById('collection_sort_type').value = sortType;
        document.getElementById('collection_sort_element').value = sortElement;
        YAHOO.ur.personal.collection.destroyMenus();
        myPersonalCollectionsTable.submitForm(myCollectionAction);
    },

    /**
     * clear out any form data messages or input
     * in the new collection form
     */
    clearCollectionForm : function()
    {
        var collectionError = document.getElementById('collection_error_div');
        collectionError.innerHTML = "";  
        
	    document.newCollectionForm.collectionName.value = "";
        document.newCollectionForm.collectionDescription.value = "";
	    document.newCollectionForm.newCollection.value = "true";
	    document.newCollectionForm.updateCollectionId.value = "";
    },

   /**
    *  Function that retireves colection information
    *  based on the given collection id.
    *
    *  The collection id used to get the collection.
    */
    getCollectionById : function(collectionId)
    {
        document.getElementById('myCollections_parentCollectionId').value = collectionId;
        YAHOO.ur.personal.collection.destroyMenus();
        myPersonalCollectionsTable.submitForm(myCollectionAction);
        YAHOO.ur.personal.collection.insertHiddenParentCollectionId();
        YAHOO.ur.shared.file.inbox.getSharedFilesCount();
    },

   /**
    *  Function that retrieves collection information
    *  based on the given collection id.
    *
    *  The collection id used to get the collection.
    */
    getCollectionByIdWithoutLoadingSharedInboxFiles : function(collectionId)
    {
        document.getElementById('myCollections_parentCollectionId').value = collectionId;
        YAHOO.ur.personal.collection.destroyMenus();

        myPersonalCollectionsTable.submitForm(myCollectionAction);
        YAHOO.ur.personal.collection.insertHiddenParentCollectionId();
    },
    
    /**
     *  check/uncheck check boxes for collections and items
     */
    setCheckboxes : function()
    {
        checked = document.myCollections.checkAllSetting.checked;
     
        var collectionIds = document.getElementsByName('collectionIds');
        urUtil.setCheckboxes(collectionIds, checked);
     
        var itemIds = document.getElementsByName('itemIds');
        urUtil.setCheckboxes(itemIds, checked);
    },
    
    /**
     * Creates a YUI new collection modal dialog for when a user wants to create or update
     * an item
     *
     */
    createNewItemDialog : function()
    {
        dialog = document.getElementById("newItemDialog");
    
        // Form name to submit to create publication
        var formName = 'newItemForm';
    
	    // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
		    this.submit();
	    };
	
	    // handle a cancel of the adding collection dialog
	    var handleCancel = function() 
	    {
	        clearItemForm();
	        YAHOO.ur.personal.collection.newItemDialog.hide();
	    };
	
	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new collection button is clicked.
	    YAHOO.ur.personal.collection.newItemDialog = new YAHOO.widget.Dialog(dialog, 
        { width : "650px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					  { text:'Cancel', handler:handleCancel } ]
		} );
					
		// override the submit function
		YAHOO.ur.personal.collection.newItemDialog.submit = function()
		{
		    YAHOO.util.Connect.setForm(formName);
	    
	        if( YAHOO.ur.personal.collection.newItemDialog.validate() )
	        {
	    	    // Creating publication from files and folders page
	    	    if (formName == 'myFolders') 
	    	    {
	    	 	    document.myFolders.itemName.value = document.newItemForm.itemName.value;
	    	 	    document.myFolders.itemArticles.value = document.newItemForm.itemArticles.value;
	    	 	    document.myFolders.action = addItemAction;
	         	    document.myFolders.submit();
	    	    } 
	    	    else 
	    	    { 
	    	 	    // Creating publication from collections page
		            document.newItemForm.action = addItemAction;
		            document.newItemForm.submit();
			    }	  
            }
		};

        // create an item from files and folders view
	    YAHOO.ur.personal.collection.newItemDialog.createFromFilesFolders = function()
	    {
	         // Set files and folders table form      
		    formName = 'myFolders';

			var callback =
			{
			    success: function(o) 
			    {
			        // check for the timeout - forward user to login page if timeout
	                // occured
	                if( !urUtil.checkTimeOut(o.responseText) )
	                {       		 			    
				        var response = eval("("+o.responseText+")");
	
				        // If file added, update selected files column
				        if ( response.hasOwnFiles == "false" )
				        {
				            var divToUpdate = document.getElementById('file_ownership_error_div');
				            divToUpdate.innerHTML = response.message; 
				        }
				    
				        YAHOO.ur.personal.collection.newItemDialog.showDialog();
				    }
			    },
				
				failure: function(o) 
				{
				    alert('Add file to item Failure ' + o.status + ' status text ' + o.statusText );
				}
			}
			
			var checkFileOwnershipAction = 	basePath + 'user/checkFileOwnership.action';
			
			YAHOO.util.Connect.setForm('myFolders');
			
		    var cObj = YAHOO.util.Connect.asyncRequest('post',
		           checkFileOwnershipAction, callback);		    
		    

	        
	    };
	    
	    // create an item from personal collection view
	    YAHOO.ur.personal.collection.newItemDialog.createFromPersonalCollection = function()
	    {
	         document.newItemForm.parentCollectionId.value = document.myCollections.parentCollectionId.value;
		
		    // Set new publication form
		    formName = 'newItemForm';
	        YAHOO.ur.personal.collection.newItemDialog.showDialog();
	    };
		
		// Show and center the dialog
	    YAHOO.ur.personal.collection.newItemDialog.showDialog = function()
	    {
	        YAHOO.ur.personal.collection.newItemDialog.center();
	        YAHOO.ur.personal.collection.newItemDialog.show();
	        YAHOO.ur.shared.file.inbox.getSharedFilesCount();
	    };
						
	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.personal.collection.newItemDialog.validate = function() 
	    {
	        var data = this.getData();
		    if (data.itemName == "" ) 
		    {
		        alert('You must enter an item name');
			    return false;
		    } 
		    else 
		    {
			    return true;
		    }
	    };

	    // Render the Dialog
	    YAHOO.ur.personal.collection.newItemDialog.render();

        // clear the new item form
        var clearItemForm = function()
        {
        	// clear out the error message
	        var ownershipError = document.getElementById('file_ownership_error_div');
	        ownershipError.innerHTML = "";
        
            document.getElementById('newItemForm_itemName').value="";
            document.getElementById('newItemForm_itemArticles').value="";
            document.getElementById('newItemForm_parentCollectionId').value="";

            document.myFolders.checkAllSetting.checked = false;
            
            urUtil.setCheckboxes(document.myFolders.fileIds, false);
            urUtil.setCheckboxes(document.myFolders.folderIds, false);
        };
    },

    /**
     * This creates a hidden field appends it to the form for
     * adding new sub collections for a given parent collections id.
     */ 
    insertHiddenParentCollectionId : function()
    {
        var value = document.getElementById('myCollections_parentCollectionId').value
    
        // create the input tag
        var parentInput = document.getElementById('newCollectionForm_parentCollectionId');
	    parentInput.value = value;
    },
    
    /**
     * Creates a YUI new collection modal dialog for when a user wants to create or update
     * a collection
     */
    createNewCollectionDialog : function()
    {
        dialog = document.getElementById("newCollectionDialog");
    
	    // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        this.submit();
	    };
	
	    // handle a cancel of the adding collection dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.personal.collection.clearCollectionForm();
	        YAHOO.ur.personal.collection.newCollectionDialog.hide();
	    };
	
	    var handleSuccess = function(o)
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		 	    
	            //get the response from adding a collection
	            var response = o.responseText;
	            var collectionForm = document.getElementById('newCollectionDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            collectionForm.innerHTML = response;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("newCollectionForm_success").value;
	    
	  
	            //if the collection was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.personal.collection.newCollectionDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the collection was added
	                YAHOO.ur.personal.collection.newCollectionDialog.hide();
	                YAHOO.ur.personal.collection.clearCollectionForm();
	            }

	            YAHOO.ur.personal.collection.destroyMenus();
	            myPersonalCollectionsTable.submitForm(myCollectionAction);
	        }
	    };
	
	    var handleFailure = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert('New collection Submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new collection button is clicked.
	    YAHOO.ur.personal.collection.newCollectionDialog = new YAHOO.widget.Dialog(dialog, 
        { width : "650px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					  { text:'Cancel', handler:handleCancel } ]
		} );
		
		// override the submit
		YAHOO.ur.personal.collection.newCollectionDialog.submit = function()
		{
			YAHOO.util.Connect.setForm('newCollectionForm');
	        if( YAHOO.ur.personal.collection.newCollectionDialog.validate() )
	        {
	        	// show wait dialog
	        	YAHOO.ur.util.wait.waitDialog.showDialog();
	            //based on what we need to do (update or create a 
	            // new collection) based on the action.
                var action = newCollectionAction;
	            if( document.newCollectionForm.updateCollectionId.value != '')
	            {
	               action = updateCollectionAction;
	            }

                var cObj = YAHOO.util.Connect.asyncRequest('post',
                action, callback);
            }
		};
					
		YAHOO.ur.personal.collection.newCollectionDialog.showDialog = function()
		{
		    YAHOO.ur.personal.collection.newCollectionDialog.center();
		    YAHOO.ur.personal.collection.newCollectionDialog.show();
		    YAHOO.ur.shared.file.inbox.getSharedFilesCount();
		};
		
	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.personal.collection.newCollectionDialog.validate = function() 
	    {
	        var data = this.getData();
		    if (data.collectionName == "" ) 
		    {
		        alert('You must enter a folder name');
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
	    YAHOO.ur.personal.collection.newCollectionDialog.render();
    },
    
    /**
     * function to edit collection information
     */
    editCollection : function(collectionId)
    {
    	
	    /*
         * This call back updates the html when editing the collection
         */
        var callback =
        {
            success: function(o) 
            {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		             
                    var divToUpdate = document.getElementById('newCollectionDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
                    document.newCollectionForm.newCollection.value = "false";
                    YAHOO.ur.personal.collection.newCollectionDialog.showDialog();
                }
                
            },
	
	        failure: function(o) 
	        {
	            alert('Edit Collection Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getCollectionNameAction + '?updateCollectionId=' + collectionId +  '&bustcache='+new Date().getTime(), 
            callback, null);    	

    },

    /**
     * Select and delete a single file
     */
    deleteSingle : function(elementId)
    {
       //uncheck all the ones that have been checked
       checked = document.myCollections.checkAllSetting.checked = false;
       YAHOO.ur.personal.collection.setCheckboxes();
   
       element = document.getElementById(elementId);
       element.checked=true;
       YAHOO.ur.personal.collection.deleteCollection.showDialog();
    },
    
    /**
     * Select and move a single file
     */
    moveSingle : function(elementId)
    {
       //uncheck all the ones that have been checked
       checked = document.myCollections.checkAllSetting.checked = false;
       YAHOO.ur.personal.collection.setCheckboxes();
   
       element = document.getElementById(elementId);
       element.checked=true;
       YAHOO.ur.personal.collection.moveCollectionData();
    },
        
   /** 
    * causes the button
    * to attach the menu to 
    * unique menu id
    * id of the file 
    */
    buildItemMenu : function(element, div, 
        menuName, 
        itemId){

        var buttonMenu = document.getElementById(div);
        var xVal = YAHOO.util.Dom.getX(div);
        var yVal = YAHOO.util.Dom.getY(div);
    
        // get the menu
        var other = document.getElementById(menuName);
   
        // we only want to initialize once  first time the menu
        // has been clicked
        if( other == null )
        {
            /*
               Instantiate the menu.  The first argument passed to the 
               constructor is the id of the DOM element to be created for the 
               menu; the second is an object literal representing a set of 
               configuration properties for the menu.
            */
            var dropMenu = new YAHOO.widget.Menu(menuName, {x:xVal, y:yVal });
         
             //place the menus in an array to be destroyed later
             //this is for ajax rendering
             collectionMenuArray.push(dropMenu);

         
             dropMenu.addItems([
                 { text: '<span class="reportGoBtnImg">&nbsp;</span> Move', url: "javascript:YAHOO.ur.personal.collection.moveSingle('item_checkbox_"+ itemId +"')" },             
                 { text: '<span class="deleteBtnImg">&nbsp;</span> Delete', url: "javascript:YAHOO.ur.personal.collection.deleteSingle('item_checkbox_"+ itemId +"')" }
             ]);
         
             dropMenu.showEvent.subscribe(function () {
                 this.focus();
             });

             /*
                Since this menu is built completely from script, call the "render" 
                method passing in the id of the DOM element that the menu's 
                root element should be appended to.
              */
              dropMenu.render(buttonMenu);
              YAHOO.util.Event.addListener(element, "click", dropMenu.show, null, dropMenu);
              dropMenu.show();
        }
        
    },

   /** 
    * causes the button
    * to attach the menu to 
    * unique menu id
    * id of the file 
    */
    buildCollectionMenu : function(element, div, 
        menuName, 
        collectionId){

        var buttonMenu = document.getElementById(div);
        var xVal = YAHOO.util.Dom.getX(div);
        var yVal = YAHOO.util.Dom.getY(div);
    
        // get the menu
        var other = document.getElementById(menuName);
   
        // we only want to initialize once  first time the menu
        // has been clicked
        if( other == null )
        {
            //path to look at properties for a file
            propertiesUrl = basePath + 'user/viewPersonalFile.action?personalFileId='+ collectionId;
               
            /*
               Instantiate the menu.  The first argument passed to the 
               constructor is the id of the DOM element to be created for the 
               menu; the second is an object literal representing a set of 
               configuration properties for the menu.
            */
            var dropMenu = new YAHOO.widget.Menu(menuName, {x:xVal, y:yVal });
         
             //place the menus in an array to be destroyed later
             //this is for ajax rendering
             collectionMenuArray.push(dropMenu);

         
             dropMenu.addItems([
                 { text: '<span class="reportGoBtnImg">&nbsp;</span> Move', url: "javascript:YAHOO.ur.personal.collection.moveSingle('collection_checkbox_"+ collectionId +"')" },             
                 { text: '<span class="deleteBtnImg">&nbsp;</span> Delete', url: "javascript:YAHOO.ur.personal.collection.deleteSingle('collection_checkbox_"+ collectionId +"')" }
             ]);
         
             dropMenu.showEvent.subscribe(function () {
                 this.focus();
             });

             /*
                Since this menu is built completely from script, call the "render" 
                method passing in the id of the DOM element that the menu's 
                root element should be appended to.
              */
              dropMenu.render(buttonMenu);
              YAHOO.util.Event.addListener(element, "click", dropMenu.show, null, dropMenu);
              dropMenu.show();
        }
        
    },
    
    
    /**
     * Destroys all of the drop down menues in the table
     * this is needed for ajax calls and re-rendering the table drop down menus
     */
    destroyMenus : function()
    {
        for( var i = 0; i < collectionMenuArray.length; i++ )
        {
            collectionMenuArray[i].destroy();
            collectionMenuArray[i] = null;
        }
    
        var size = collectionMenuArray.length;
    
        for( var i = 0; i < size; i++ )
        {
            collectionMenuArray.pop();
        }
    },

    /**
     * create a dialog to confirm the creation of new publication version.
     */
    createNewPublicationVersionConfirmDialog : function() 
    {
        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
		    document.newVersionForm.action = createPublicationVersionToEditAction;
		    document.newVersionForm.submit();
		    this.hide();
	    };
	    
	    var handleCancel = function() 
	    {
		    this.hide();
	    };

	    // Instantiate the Dialog
	    YAHOO.ur.personal.collection.newVersion = 
	        new YAHOO.widget.SimpleDialog("newPublicationVersionConfirmation", 
			    { width: "600px",
				  visible: false,
				  modal : true,
				  buttons: [ { text:"Yes", handler:handleSubmit, isDefault:true },
							 { text:"No",  handler:handleCancel } ]
				} );
	
        // show and center the dialog box
        YAHOO.ur.personal.collection.newVersion.showDialog = function()
        {
            YAHOO.ur.personal.collection.newVersion.center();
            YAHOO.ur.personal.collection.newVersion.show()
        }
			
	    // Render the Dialog
	    YAHOO.ur.personal.collection.newVersion.render();
	
    },
    
    /**
     * Function to edit a publication
     */
    editPublication : function(personalItemId, genericItemId, parentCollectionId, isPublished) 
    {
		document.newVersionForm.personalItemId.value = personalItemId;
		document.newVersionForm.parentCollectionId.value = parentCollectionId;

		if (isPublished == 'true') 
		{
			YAHOO.ur.personal.collection.newVersion.showDialog();
		} 
		else 
		{
			window.location = viewEditPublicationAction + '?genericItemId=' + genericItemId + '&parentCollectionId=' + parentCollectionId;
		}
	},
	
	/**
     * Allow collections and items to be moved
     */
	moveCollectionData : function()
    {
        // make sure a collection or publication has been selected
	    if (!urUtil.checkForNoSelections(document.myCollections.collectionIds) &&
	        !urUtil.checkForNoSelections(document.myCollections.itemIds))
		{
		    alert('Please select at least one checkbox next to the folders or publications you wish to move.');
	    } 
	    else
	    {
            var viewMoveCollectionsAction = basePath + 'user/viewMovePersonalCollectionLocations.action';
            document.myCollections.action = viewMoveCollectionsAction;
            document.myCollections.submit();
        }
    },

    
	/**
	 * Delete the items and collections by submitting the form.
	 */
	deleteItemsCollections : function()
	{
		// handle a successful return
	    var handleSuccess = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
			// check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       	    
	            var response = o.responseText;
	            document.getElementById('newPersonalCollections').innerHTML = response;
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert('delete items collections failure '  + o.status + ' status text ' + o.statusText);
	    };

	    YAHOO.util.Connect.setForm('myCollections');
    
        YAHOO.util.Connect.asyncRequest('POST', deleteCollectionAction,
          {success: handleSuccess, failure: handleFailure});
	},
    
    /**
     * create a dialog to confirm the deletion of collections.
     */
    createCollectionDeleteConfirmDialog : function() 
    {
        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	    	YAHOO.ur.util.wait.waitDialog.showDialog();
	    	YAHOO.ur.personal.collection.destroyMenus();
	    	YAHOO.ur.personal.collection.deleteItemsCollections();
		    this.hide();
	    };
	    
	    var handleCancel = function() 
	    {
	    	// uncheck all collections
	    	checked = document.myCollections.checkAllSetting.checked = false;
	    	YAHOO.ur.personal.collection.setCheckboxes();
		    this.hide();
	    };

	    // Instantiate the Dialog
	    YAHOO.ur.personal.collection.deleteCollection = 
	        new YAHOO.widget.SimpleDialog("deleteCollectionDiv", 
			{ width: "500px",
			  visible: false,
			  modal: true,
			  buttons: [ { text:"Yes", handler:handleSubmit, isDefault:true },
						 { text:"No",  handler:handleCancel } ]
			} );
	
	    YAHOO.ur.personal.collection.deleteCollection.showDialog = function()
	    {
	        // make sure a collection or publication has been selected
	        if (!urUtil.checkForNoSelections(document.myCollections.collectionIds) &&
	            !urUtil.checkForNoSelections(document.myCollections.itemIds))
		    {
		        alert('Please select at least one checkbox next to the folders or publications you wish to delete.');
	        } 
	        else
	        {
	           YAHOO.ur.personal.collection.deleteCollection.center();
	           YAHOO.ur.personal.collection.deleteCollection.show();
	        }
	    };
	    
	    // Render the Dialog
	    YAHOO.ur.personal.collection.deleteCollection.render();
    },
    
    /**
     * Delete the checked collections and publications
     */
    executeDeleteAction : function()
    {
    	 YAHOO.ur.personal.collection.deleteCollection.showDialog();
         YAHOO.ur.shared.file.inbox.getSharedFilesCount();
    },
    
    /**
     * Move the selected collections and publications
     */
    executeMoveAction : function()
    {
    	 YAHOO.ur.personal.collection.moveCollectionData();
         YAHOO.ur.shared.file.inbox.getSharedFilesCount();
    },


    /** 
     * initialize the page
     * this is called once the dom has
     * been created
     */
    init: function() 
    {
	    if (document.getElementById('myCollections_showCollection').value == 'true')
	    {
		    YAHOO.ur.user.workspace.setActiveIndex("COLLECTION");
	    }
        var parentCollectionId = document.getElementById('myCollections_parentCollectionId').value;
        YAHOO.ur.personal.collection.getCollectionByIdWithoutLoadingSharedInboxFiles(parentCollectionId);
        YAHOO.ur.personal.collection.createNewCollectionDialog();
        YAHOO.ur.personal.collection.createCollectionDeleteConfirmDialog();
        YAHOO.ur.personal.collection.createNewItemDialog(); 
        YAHOO.ur.personal.collection.createNewPublicationVersionConfirmDialog();
    }  
    
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.personal.collection.init);