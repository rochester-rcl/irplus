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

package edu.ur.tree;

import java.io.Serializable;

/**
 * Orders a set of trees by their left value in ascending order.
 * 
 * @author Nathan Sarr
 *
 */
public class PreOrderTreeLeftComparator implements java.util.Comparator<PreOrderTreeSetNode>, Serializable
{

    /** eclipse generated serial version id. */
    private static final long serialVersionUID = -1137126401281523681L;

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(PreOrderTreeSetNode node1, PreOrderTreeSetNode node2)
    {
        if (node1.getLeftValue().equals(node2.getLeftValue()))
        {
            return 0;
        }
        else
        {
            if (node1.getLeftValue() <= node2.getLeftValue())
            {
                return -1;
            }
        }

        return 1;
    }
}
