/**
 * This allows a wait dialog to be greated.  The html file
 * must contain a wait_dialog_box  like the following:
 * 
 *  <!--  wait div -->
 *	<div id="wait_dialog_box" class="hidden">
 *	    <div class="hd">Processing...</div>
 *		<div class="bd">
 *		    <c:url var="wait" value="/page-resources/images/all-images/ajax-loader.gif"/>
 *		    <p><img src="${wait}"></img></p>
 *		</div>
 *	</div>       
 */
YAHOO.namespace("ur.util.wait");

YAHOO.ur.util.wait = 
{
     /**
     * Dialog to handle waiting display
     */
    createWaitDialog : function()
    {
         var handleClose = function()
         {
         	YAHOO.ur.util.wait.waitDialog.close();
         };
          
 	     // Instantiate the Dialog
         YAHOO.ur.util.wait.waitDialog = 
 	         new YAHOO.widget.Dialog("wait_dialog_box", 
 									     { width: "600px",
 										   visible: false,
 										   modal: true,
 										   close: false
 										  } );
 										
         YAHOO.ur.util.wait.waitDialog.showDialog = function()
 		 {
         	  YAHOO.ur.util.wait.waitDialog.center();
         	  YAHOO.ur.util.wait.waitDialog.show();
 		 };
 		 
 		 YAHOO.ur.util.wait.waitDialog.render();
    },
    
    /**
     *  initialize the wait dialog
     */
    init : function() 
    {
    	YAHOO.ur.util.wait.createWaitDialog();
    }
};

//initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.util.wait.init);