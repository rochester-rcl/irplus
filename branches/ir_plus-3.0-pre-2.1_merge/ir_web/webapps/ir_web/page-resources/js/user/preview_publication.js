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
 * This code is viewing the publication preview
 */
YAHOO.namespace("ur.item.preview");

var viewAddItemToCollectionsAction = basePath + 'user/viewAddItemToCollections.action';
var publicationWorkspaceAction = basePath + 'user/workspace.action?tabName=COLLECTION';
var viewInstitutionalItemAction = basePath + 'institutionalPublicationPublicView.action';
var viewContributorsAction = basePath + 'user/viewItemContributor.action';
var viewAddFilesAction = basePath + 'user/viewEditItem.action';
var viewAddMetadataAction = basePath + 'user/viewItemMetadata.action';


YAHOO.ur.item.preview = {
	
	/**
	 * Function to submit item to collections
	 *
	 */
	submitItem : function()
	{
		document.previewForm.action = viewAddItemToCollectionsAction;
	    document.previewForm.submit();
	},
	
	/**
	 * Function to goto the Publication table
	 *
	 */
	finishLater : function()
	{
		if (document.previewForm.institutionalItemId.value != '') {
			document.previewForm.action = viewInstitutionalItemAction;
		} else {
			document.previewForm.action = publicationWorkspaceAction;;
		}

	    document.previewForm.submit();
	},
	
	/**
	 * Function to view Add contributors screen
	 *
	 */
	viewContributors : function()
	{
		document.previewForm.action = viewContributorsAction;
	    document.previewForm.submit();
	},
	
	/**
	 * Function to view Add files screen
	 *
	 */
	viewAddFiles : function()
	{
		document.previewForm.action = viewAddFilesAction;
	    document.previewForm.submit();
	},
	
	/**
	 * Function to view Add metadata screen
	 *
	 */
	viewAddMatadata : function()
	{
		document.previewForm.action = viewAddMetadataAction;
	    document.previewForm.submit();
	}
}	