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
 * This code is for dealing with display of researcher page
 * 
 */
YAHOO.namespace("ur.researcher.page");

YAHOO.ur.researcher.page = {

    /*
     * Get the picture 
     * 
     * currentLocation = location of the current picture
     * type = INIT for initial load
     *         NEXT for next picture
     *         PREV for previous picture
     */
    getResearcherPicture : function(currentLocation, type)
    {
        // action for getting the picture
        var getResearcherPictureAction =  basePath + 'nextPicture.action';

        // Success action on getting the picture
        var handleSuccess = function(o) 
        {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               
                var divToUpdate = document.getElementById('researcher_picture');
                divToUpdate.innerHTML = o.responseText; 
            }
        };
    
        //Faiure action on getting a picture
        var handleFailure = function(o) 
	    {
	        alert('Could not get picture ' 
	            + o.status + ' status text ' + o.statusText );
	    };
		
		// Get the next picture
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getResearcherPictureAction +"?currentLocation="+ 
            currentLocation +'&type='+ type +'&researcherId='+ document.getElementById('researcher_id').value +'&bustcache='+new Date().getTime(), 
            {success: handleSuccess, failure: handleFailure}, null);
    },
	
	createFolderTree : function() 
	{
			var jsonObject = document.getElementById('json_object').value;
	
			// Parsing JSON strings can throw a SyntaxError exception, so we wrap the call
			// in a try catch block
			try {
			    var researcher = YAHOO.lang.JSON.parse(jsonObject);
			}
			catch (e) {
			    alert("Invalid JSON object data");
			}

			//create the TreeView instance:
			var tree = new YAHOO.widget.TreeView("treeDiv");
			
			//get a reusable reference to the root node:
			var root = tree.getRoot();
			
			// this stops the tree from hiding the URL - this is a problem with 
			// YUI 2.7.X - see http://yuilibrary.com/projects/yui2/ticket/2527720
			tree.subscribe('clickEvent',function () {return false;});
			// build tree 
			YAHOO.ur.researcher.page.buildTree(researcher, root, researcher.id);

			
			tree.render(); 
			tree.expandAll();
	
	},
	
	/**
	 * Build tree
	 */
	buildTree : function(node, parentNode, researcherId) 
	{
	       
			// Build folders and its children
			for (var i = 0; i < node.folders.length; i++) {
			
			    var nameDescValue = node.folders[i].name;
			    if( node.folders[i].description != null &&  node.folders[i].description != '')
				{
				    nameDescValue = nameDescValue + " - " + node.folders[i].description + "<br/><br/>";
				}
			    
				var newNode = new YAHOO.widget.TextNode(nameDescValue, parentNode, false);
              
				if (node.folders[i].folders == '' && node.folders[i].files == '' &&
				    node.folders[i].links == '' && node.folders[i].publications == '') {
					
					// Set folder icon
				   newNode.labelStyle  = "icon-folder";
				}
				
				YAHOO.ur.researcher.page.buildTree(node.folders[i], newNode, researcherId);
			}
			
			// Build file node
			for (var i = 0; i < node.files.length; i++) {
	
				var fileObj = new Object;
				
				if (node.files[i].extension == 'doc' || node.files[i].extension == 'docx') {
					fileObj.html = "<span class=\"wordFileImg\">&nbsp;</span>" + "<a href=\"" + basePath + "researcherFileDownload.action?researcherFileId=" + node.files[i].id + "\">" 
							+ node.files[i].name + "</a>";
				} else if (node.files[i].extension == 'pdf') {
					fileObj.html = "<span class=\"pdfFileImg\">&nbsp;</span>" + "<a href=\"" + basePath + "researcherFileDownload.action?researcherFileId=" + node.files[i].id + "\">" 
							+ node.files[i].name + "</a>";
				} else if (node.files[i].extension == 'ppt' || node.files[i].extension == 'pptx') {
					fileObj.html = "<span class=\"powerPointFileImg\">&nbsp;</span>" + "<a href=\"" + basePath + "researcherFileDownload.action?researcherFileId=" + node.files[i].id + "\">" 
							+ node.files[i].name + "</a>";
				} else if (node.files[i].extension == 'xls' || node.files[i].extension == 'xlsx') {
					fileObj.html = "<span class=\"excelFileImg\">&nbsp;</span>" + "<a href=\"" + basePath + "researcherFileDownload.action?researcherFileId=" + node.files[i].id + "\">" 
							+ node.files[i].name + "</a>";
				} else if (node.files[i].extension == 'txt') {
					fileObj.html = "<span class=\"textFileImg\">&nbsp;</span>" + "<a href=\"" + basePath + "researcherFileDownload.action?researcherFileId=" + node.files[i].id + "\">" 
							+ node.files[i].name + "</a>";
				} else if (node.files[i].extension == 'zip' ) {
					fileObj.html = "<span class=\"compressedFileImg\">&nbsp;</span>" + "<a href=\"" + basePath + "researcherFileDownload.action?researcherFileId=" + node.files[i].id + "\">" 
							+ node.files[i].name + "</a>";
				} else if (node.files[i].extension == 'jpg' || node.files[i].extension == 'gif' || node.files[i].extension == 'png' || node.files[i].extension == 'tiff' || node.files[i].extension == 'tif') {
					fileObj.html = "<span class=\"imgFileImg\">&nbsp;</span>" + "<a href=\"" + basePath + "researcherFileDownload.action?researcherFileId=" + node.files[i].id + "\">" 
							+ node.files[i].name + "</a>";
				} else {
					fileObj.html = "<span class=\"whiteFileImg\">&nbsp;</span>" + "<a href=\"" + basePath + "researcherFileDownload.action?researcherFileId=" + node.files[i].id + "\">" 
							+ node.files[i].name + "</a>";
				}
				
                if( node.files[i].description != null &&  node.files[i].description != '')
				{
				    fileObj.html = fileObj.html + " - " + node.files[i].description + "<br/><br/>";
				}
				// Create file node
				var fileNode = new YAHOO.widget.HTMLNode(fileObj, parentNode, false, true);
				

			}
			
			// Build publication node
			for (var i = 0; i < node.publications.length; i++) {
				
				var publicationObj = new Object;

				publicationObj.html = "<span class=\"packageBtnImg\">&nbsp;</span> <a href=\"" + basePath + "researcherPublicationView.action?researcherPublicationId=" + 
				    node.publications[i].id +"\">" 
							+ node.publications[i].name + "</a>";
							
				if( node.publications[i].description != null &&  node.publications[i].description != '')
				{
				    publicationObj.html = publicationObj.html + " - " + node.publications[i].description + "<br/><br/>";
				}
						
				var pNode = new YAHOO.widget.HTMLNode(publicationObj, parentNode, false, true);
				
			}

			// Build institutional item node
			for (var i = 0; i < node.institutionalItems.length; i++) {
				
				var institutionalItemObj = new Object;

				institutionalItemObj.html = "<span class=\"packageBtnImg\">&nbsp;</span> <a href=\"" + basePath + "institutionalPublicationPublicView.action?institutionalItemId=" + 
				    node.institutionalItems[i].institutionalItemId + "\">" 
							+ node.institutionalItems[i].name + "</a>";
							
				if( node.institutionalItems[i].description != null &&  node.institutionalItems[i].description != '')
				{
				    institutionalItemObj.html = institutionalItemObj.html + " - " + node.institutionalItems[i].description + "<br/><br/>";
				}
						
				var pNode = new YAHOO.widget.HTMLNode(institutionalItemObj, parentNode, false, true);
				
			}
						
			// Build link node 
			for (var i = 0; i < node.links.length; i++) {
	
				var linkObj = new Object;
				linkImg = "<img  alt=\"link\" src=\"" + basePath + "page-resources/images/all-images/link.gif\"/>";
				linkObj.html = linkImg + "<a href=\"" + node.links[i].url + "\">" + node.links[i].name + "</a>";
				if( node.links[i].description != null &&  node.links[i].description != '')
				{
				    
				    linkObj.html = linkObj.html + " - " + node.links[i].description + "<br/><br/>";
				}
				var linkNode = new YAHOO.widget.HTMLNode(linkObj, parentNode, false, true);
				
				
			}
		
	},
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	    YAHOO.ur.researcher.page.createFolderTree();
		YAHOO.ur.researcher.page.getResearcherPicture(0, 'INIT');

	}
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.researcher.page.init);