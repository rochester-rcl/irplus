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

package edu.ur.order;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Class for comparing to orderable objects.  Order is considered to be 
 * 1 is the first object, 2 is the second and so on.
 * 
 * @author Nathan Sarr
 *
 */
public class AscendingOrderComparator implements Comparator<Orderable>, Serializable
{

    /** Eclipse generated id */
    private static final long serialVersionUID = 7333091182851380699L;

    /**
     * Compare the two values.
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Orderable arg0, Orderable arg1)
    {
        if (arg1.getOrder() > arg0.getOrder())
        {
            return -1;
        } else if (arg1.getOrder() < arg0.getOrder())
        {
            return 1;
        } else
        {
            return 0;
        }
    }
}
