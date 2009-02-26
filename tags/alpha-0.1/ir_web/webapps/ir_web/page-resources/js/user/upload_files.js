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
YAHOO.namespace("ur.file.upload");


YAHOO.ur.file.upload = 
{
 
    /**
     * Add a file set for files to be uploaded
     */
    addFileSets : function(numToAdd, first)
    {
        // id to give to the table
        var id = parseInt(document.getElementById("file_upload_table_id").value);
        
        // area to place the tables
        var table_div = document.getElementById("file_forms");

        for(var i = 0; i < numToAdd; i++)
        {
            table = document.createElement('table');
            table.className="formTable, uploadFile";
            table.id = 'file_upload_table_' + id;
        
            rowCount = 0;
        	
        	// create the input tag
	        var fileInput = document.createElement('input');
	        fileInput.type = 'file';
	        fileInput.name = 'file';
	        fileInput.size = '100';

            var tableRow1 = table.insertRow(rowCount);
            var tableCell1 = tableRow1.insertCell(0);
            tableCell1.appendChild(fileInput);
            tableCell1.colSpan='2';
            
            rowCount += 1;

            var tableRow2 = table.insertRow(rowCount);
            var tableCell2 = tableRow2.insertCell(0);	        
	        
	        var text2 = document.createTextNode('File Name');
	        tableCell2.appendChild(text2);

            var tableCell3 = tableRow2.insertCell(1);
	        var text3 = document.createTextNode('Description');
	        tableCell3.appendChild(text3);
	      
	        rowCount += 1;
	        var tableRow3 = table.insertRow(rowCount); 
	        var tableCell4 = tableRow3.insertCell(0); 
	        
	        var nameInput = document.createElement('input');
	        nameInput.size = 40;
	        nameInput.type = 'text';
	        nameInput.name = 'userFileName';
	        
	        tableCell4.appendChild(nameInput);
	        
	        tableCell5 = tableRow3.insertCell(1);

	        var descriptionInput = document.createElement('textarea');
	        descriptionInput.rows = 1;
	        descriptionInput.cols = 50;
	        descriptionInput.name = 'userFileDescription';
	        
	        tableCell5.appendChild(descriptionInput);
	        
	        
	        rowCount += 1;
	        var tableRow4 = table.insertRow(rowCount); 
	        var tableCell6 = tableRow4.insertCell(0); 
	        
	        var remove = document.createElement('button');
	        remove.className = 'ur_button';
	        remove.id = 'remove_table_' + id;
	        
	         // listener for showing the dialog when clicked.
	        YAHOO.util.Event.addListener(remove.id, "click", 
	         YAHOO.ur.file.upload.removeFileSet, table.id);

	        var removeText = document.createTextNode('Remove');
	        remove.appendChild(removeText);
	        
	        
	        
	        tableCell6.appendChild(remove);
	        tableCell6.colSpan='2';
	        
	        if( !first )
            {
               var br = document.createElement('br');
               table_div.appendChild(br);
	        }
	        
	        table_div.appendChild(table);
	        
	        id = id + 1; 
        }
        // id to give to the table
       document.getElementById("file_upload_table_id").value = id;
    },
    
    /**
     * remove the file set - the event is passed in
     * by the YUI handler
     */
    removeFileSet : function(event, tableId)
    {
        var table_div = document.getElementById("file_forms");
        var child = document.getElementById(tableId);
        table_div.removeChild(child);
    },
        
    
    init : function() 
    {
        // submit the form once the page is loaded
        // create the first file set once the page is 
        // loaded.
        YAHOO.ur.file.upload.addFileSets(1, true);
    }
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.file.upload.init);
