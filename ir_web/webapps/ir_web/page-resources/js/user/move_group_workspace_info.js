/**
 * This code is for dealing with moving files and folders 
 * in the workspace.
 */
YAHOO.namespace("ur.group_workspace.move");

YAHOO.ur.group_workspace.move = 
{
    getMoveFolder : function(destinationId)
    {
        var viewMoveFoldersAction = basePath + 'user/getMoveGroupWorkspaceFolderDestinations.action';
        document.getElementById("destinationId").value = destinationId;
        document.viewChildContentsForMove.action = viewMoveFoldersAction;
        document.viewChildContentsForMove.submit();
	    
    },
    
    moveFolder : function()
    {
    	YAHOO.ur.util.wait.waitDialog.showDialog();
	    YAHOO.util.Connect.setForm('viewChildContentsForMove');
	    document.viewChildContentsForMove.action = basePath + "user/moveGroupWorkspaceFolderInformation.action";
	    document.viewChildContentsForMove.submit();
    }
    

}

//initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.group_workspace.move.init);