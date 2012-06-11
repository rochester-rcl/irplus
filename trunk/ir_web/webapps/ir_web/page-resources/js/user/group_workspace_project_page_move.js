/*
   Copyright 2008 - 2012 University of Rochester

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
 * This code is for dealing with moving files and folders 
 * in the resarcher pages
 */
YAHOO.namespace("ur.projectPage.filesystem.move");


YAHOO.ur.projectPage.filesystem.move = 
{
	/**
	 * get the destination to move into
	 */
    getMoveFolder : function(destinationId)
    {
        var viewMoveFoldersAction = basePath + 'user/getMoveGroupWorkspaceProjectPageDestinations.action';
        document.getElementById("destination_id").value = destinationId;
        document.viewChildContentsForMove.action = viewMoveFoldersAction;
        document.viewChildContentsForMove.submit();
    },
    
    /**
     *  Actually perform the 
     */
    moveFolder : function()
    {
    	 var moveFoldersAction = basePath + 'user/moveGroupWorkspaceProjectPageInformation.action';
         document.viewChildContentsForMove.action = moveFoldersAction;
         document.viewChildContentsForMove.submit();
    },
}


