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

import java.util.Set;

/**
 * Basic tree functions.
 *
 * @author Nathan Sarr
 *
 */
public interface TreeSetNode
{

    /**
     * Get the collection of children for this  object.
     *
     * @return the children for this node.
     */
    @SuppressWarnings("unchecked")
    Set getChildren();

    /**
     * Determine if this node allows children.
     *
     * @return true if this node allows children
     */
    boolean allowsChildren();

    /**
     * Determine if this node allows children.
     *
     * @return true if thihs node allows children
     */
    boolean getAllowsChildren();

    /**
     * Set if this node allows children.
     *
     * @param allowsChildren indicates this node allows children
     */
    void setAllowsChildren(boolean allowsChildren);

    /**
     * Get the number of direct children this object has.
     *
     * @return the number of children this node has
     */
    int getChildCount();

    /**
     * Indicates this is the parent of the tree.
     *
     * @return true if this is the parent.
     */
    boolean isRoot();

    /**
     * Indicates this is the parent of the tree.
     *
     * @return true if the node is the tree root.
     */
    boolean getIsRoot();

    /**
     * Determine if this node is a leaf.
     *
     * @return true if this node has no children
     */
    boolean isLeaf();

    /**
     * Determine if this node is a leaf.
     *
     * @return true if this node has no children
     */
    boolean getIsLeaf();
}
