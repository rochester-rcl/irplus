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

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the Pre Order Tree Set Node Base.
 * 
 * @author Nathan Sarr
 *
 */
public class PreOrderTreeSetNodeImpl extends PreOrderTreeSetNodeBase
{

    private String name;
    /** root of the tree*/
    private PreOrderTreeSetNodeImpl root;
    /**  The parent node */
    private PreOrderTreeSetNodeImpl parent;
    /**  Children of this node. */
    private HashSet<PreOrderTreeSetNodeImpl> children =
            new HashSet<PreOrderTreeSetNodeImpl>();

    /**
     * Default constructor
     */
    public PreOrderTreeSetNodeImpl(String name)
    {
        this.name = name;
        root = this;
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNodeBase#getChildren()
     */
    public Set<PreOrderTreeSetNodeImpl> getChildren()
    {
        return children;
    }

    /**
     * Set the parent of this tree
     * @param parent
     */
    public void setParent(PreOrderTreeSetNodeImpl parent)
    {
        this.parent = parent;
        if (parent == null)
        {
            setTreeRoot(this);
            makeTreeRoot(this);
        }
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNodeBase#getParent()
     */
    public PreOrderTreeSetNodeImpl getParent()
    {
        return parent;
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNodeBase#getChildBasedOnLeft(java.lang.Long)
     */
    public PreOrderTreeSetNodeImpl getChildBasedOnLeft(Long leftValue)
    {
        return (PreOrderTreeSetNodeImpl) super.getChildBasedOnLeft(leftValue);
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNodeBase#getChildBasedOnRight(java.lang.Long)
     */
    public PreOrderTreeSetNodeBase getChildBasedOnRight(Long rightValue)
    {
        return (PreOrderTreeSetNodeImpl) super.getChildBasedOnRight(rightValue);
    }

    /**
     * Add a child to this tree.
     *
     * @param child to add
     * @return true if the child is added
     */
    public void addChild(PreOrderTreeSetNodeImpl child)
    {
        if (!child.isRoot())
        {
            parent.removeChild(child);
            child.getParent().cleanUpTree(child.getTreeSize(), child.getRightValue());
        }
        child.setRoot(getTreeRoot());
        makeRoomInTree(child);
        children.add(child);
        child.setParent(this);

    }

    /**
     * Remove the child from this tree. The child
     * must be in the direct decendents
     *
     * @param child - child to remove
     * @return true if the child is removed
     */
    public boolean removeChild(PreOrderTreeSetNodeImpl child)
    {
        this.cleanUpTree(child.getTreeSize(), child.getRightValue());
        child.setParent(null);
        return children.remove(child);
    }

    /**
     * Returns a PreOderTreeSetNodeImpl instead of a
     * PreOderTreeSetNodeBase.
     *
     * @see edu.ur.tree.PreOrderTreeSetNodeBase#getTreeRoot()
     */
    public PreOrderTreeSetNodeImpl getTreeRoot()
    {
        return root;
    }

    /**
     * Set the root node for the entire tree.
     *
     * @param root
     */
    void setTreeRoot(PreOrderTreeSetNodeImpl root)
    {
        this.root = root;
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNodeBase#getLeftValue()
     */
    public Long getLeftValue()
    {
        return leftValue;
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNodeBase#setLeftValue(java.lang.Long)
     */
    public void setLeftValue(Long leftValue)
    {
        this.leftValue = leftValue;
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNodeBase#getRightValue()
     */
    public Long getRightValue()
    {
        return rightValue;
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNodeBase#setRightValue(java.lang.Long)
     */
    public void setRightValue(Long rightValue)
    {
        this.rightValue = rightValue;
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNodeBase#setRoot(edu.ur.tree.PreOrderTreeSetNodeBase)
     */
    protected void setRoot(PreOrderTreeSetNodeBase root)
    {
        this.root = (PreOrderTreeSetNodeImpl) root;
    }

    public String toString()
    {
        return name;
    }

    @Override
    public String getFullPath()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPath()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void updatePaths(String path)
    {
        // TODO Auto-generated method stub
    }
}
