/**  
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

package edu.ur.dspace.model;

import java.util.List;

/**
 * Permissions constants in the dspace system.
 * 
 * @author Nathan Sarr
 *
 */
public class PermissionConstants {
	
	
    /** Action of reading, viewing or downloading something */
    public static final int READ            = 0;

    /** Action of modifying something */
    public static final int WRITE           = 1;

    /**
     * Action of deleting something.  Different from removing something
     * from a container. (DELETE is now obsolete)
     * @see #REMOVE
     */
    public static final int DELETE          = 2;

    /**
     * Action of adding something to a container.  For example, to add
     * an item to a collection, a user must have <code>ADD</code> permission
     * on the collection.
    */
    public static final int ADD             = 3;

    /**
     * Action of removing something from a container.  Different from
     * deletion.
     * @see #DELETE
     */
    public static final int REMOVE          = 4;

    /** Action of performing workflow step 1 */
    public static final int WORKFLOW_STEP_1 = 5;

    /** Action of performing workflow step 2 */
    public static final int WORKFLOW_STEP_2 = 6;

    /** Action of performing workflow step 3 */
    public static final int WORKFLOW_STEP_3 = 7;

    /** Action of performing a workflow */
    public static final int WORKFLOW_ABORT  = 8;

    /** Default Read policies for Bitstreams submitted to container */
    public static final int DEFAULT_BITSTREAM_READ = 9;

    /** Default Read policies for Items submitted to container */
    public static final int DEFAULT_ITEM_READ = 10;
    
    /** represents the anonymous group */
    public static final Long ANONYMOUS_GROUP_ID = 0l;
    
    
    /**
     * Determine if the list of groups contains anonymous read.
     * 
     * @param groupPermissions - set of permissions to check
     * @return true if one of the permissions is anonymous read
     */
    public static boolean hasAnonymousRead(List<GroupPermission> groupPermissions)
    {
    	boolean hasAnonymousRead = false;
    	
    	if( groupPermissions != null )
    	{
    	    for(GroupPermission permission : groupPermissions)
    	    {
    		    if( permission.groupId.equals(ANONYMOUS_GROUP_ID) && permission.action == READ)
    		    {
    		    	return true;
    		    }
    	    }
    	}
    	
    	return hasAnonymousRead;
    }
    
    /**
     * Return the action name for the given action
     * @return
     */
    public static String getActionName(int action)
    {
    	
    	if( action == READ)
    	{
    		return "READ";
    	}
    	else if( action == WRITE)
    	{
    		return "WRITE";
    	}
    	else if( action == DELETE)
    	{
    		return "DELETE";
    	}
    	else if( action == ADD)
    	{
    		return "ADD";
    	}
    	else if( action == REMOVE)
    	{
    		return "REMOVE";
    	}
    	else if( action == WORKFLOW_STEP_1)
    	{
    		return "WORKFLOW_STEP_1";
    	}
    	else if( action == WORKFLOW_STEP_2)
    	{
    		return "WORKFLOW_STEP_2";
    	}
    	else if( action == WORKFLOW_STEP_3)
    	{
    		return "WORKFLOW_STEP_3";
    	}
    	else if( action == WORKFLOW_ABORT)
    	{
    		return "WORKFLOW_ABORT";
    	}
    	else if( action == DEFAULT_BITSTREAM_READ)
    	{
    		return "DEFAULT_BITSTREAM_READ";
    	}
    	else if( action == DEFAULT_ITEM_READ)
    	{
    		return "DEFAULT_ITEM_READ";
    	}
    		
    	return "NO ACTION";
    }
    

}
