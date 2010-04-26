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

/**
 * A modified pre order tree which has a left and 
 * right value on each node and looks something like
 * the following
 * 
 * 
 *                (1) the_root (14)
 *                      /\   
 *                     /  \  
 *                    /    \ 
 *                   /      \
 *                  /        \ 
 *                 /          \
 *                /            \
 *     (2) cd_1 (5)         (6) cd_2 (13)
 *              /                \
 *             /                  \
 *            /                    \
 *    (3) sub_cd_1 (4)      (7) sub_cd_2 (12)
 *                                  /\
 *                                 /  \
 *                                /    \
 *                               /      \
 *                              /        \
 *                             /          \
 *                            /            \
 *                           /              \       
 *                          /                \
 *                         /                  \
 *             (8) sub_sub_cd_1 (9)      (10) sub_sub_cd_2 (11)
 *             
 *  This allows for easier access to find children.
 *  For example all children of sub_cd_2 will have left values greater
 *  than it's left value (6) in this case and right values less than
 *  it's right value (13) in this case.
 *  
 *  This makes database selects much easier if the data is stored this
 *  way.  The selects can be as simple as select * from tree_node table
 *  where left value greater than X and right value less than X.
 *  
 *  This also helps with determining a path to the child all nodes do 
 *  not need to be investigated because we know the child must have
 *  values between it's parent's left and right values and and 
 *  nodes that do not meet this critera can be eliminated.
 * 
 * @author Nathan Sarr
 *
 */
public interface PreOrderTreeSetNode extends TreeSetNode
{

    /**
     * The left value of node.
     *
     * @return id representing the left side of the tree
     */
     Long getLeftValue();

    /**
     * The rigt value of the node.
     *
     * @return id representing the right side of the tree
     */
     Long getRightValue();

    /**
     * Get the parent of the tree.
     *
     * @return the parent
     */
     TreeSetNode getParent();

    /**
     * Find the child based on it's left value.  Should return
     * null if the child is not found.
     *
     * @param leftValue of the child
     * @return the child based on it's left value
     */
     PreOrderTreeSetNode getChildBasedOnLeft(Long leftValue);

    /**
     * Find the child based on a right value. Should return null
     * if the child is not found.
     *
     * @param rightValue right value of the tree.
     * @return get a child based on it's right value
     */
     PreOrderTreeSetNode getChildBasedOnRight(Long rightValue);

    /**
     * The number of nodes in this tree using
     * this node as the root.  The count
     * includes itself.
     *
     * @return number of noeds in the tree.
     */
     Long getTreeSize();

    /**
     * Get the root of the tree.
     *
     * @return the root
     */
     PreOrderTreeSetNode getTreeRoot();
}
