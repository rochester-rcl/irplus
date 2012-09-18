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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Collections;

import org.apache.log4j.Logger;

/**
 * Abstract class that implements most of the tree node 
 * methods.
 * 
 * @author Nathan Sarr
 *
 */
public abstract class PreOrderTreeSetNodeBase implements PreOrderTreeSetNode
{

    /**  Left value in the tree. */
    public static final Long DEFAULT_LEFT_VALUE = 1L;
    /**  Right value in the tree. */
    public static final Long DEFAULT_RIGHT_VALUE = 2L;
    /** left value of the tree  */
    protected Long leftValue = DEFAULT_LEFT_VALUE;
    /** right value of the tree */
    protected Long rightValue = DEFAULT_RIGHT_VALUE;
    /**  Logger for the pre order tree set node  */
    private static final Logger log = Logger.getLogger(PreOrderTreeSetNodeBase.class);
    /**  Boolean that indicates if children are allowed. */
    protected boolean allowsChildren = true;
    /** parent of this node  */
    protected PreOrderTreeSetNodeBase parent;

    /**
     * The virtual path to this node in the tree.  This
     * does not include the name of the node itself.
     *
     * @return virtual path
     */
    public abstract String getPath();

    /**
     * Update the paths in the tree.  This is useful for a path
     * change.
     *
     * @param path
     */
    protected abstract void updatePaths(String path);

    /**
     * Get the full path for this node in the tree.
     *
     * @return
     */
    public abstract String getFullPath();

    /**
     * Set the parent.  This handles updating paths and root node.
     *
     * @param parent
     */
    protected void setParent(PreOrderTreeSetNodeBase parent)
    {

        // making this a root folder
        if (this.parent != null && parent == null)
        {

            setRoot(this);
            this.parent = parent;
            makeTreeRoot(this);
            updatePaths(getPath());
        } //parent change
        else
        {
            if (parent != null && !parent.equals(this.parent))
            {
                this.parent = parent;
                updatePaths(parent.getFullPath());
                if (!parent.getTreeRoot().equals(getTreeRoot()))
                {
                    setRoot(parent.getTreeRoot());
                    updateChildrenWithNewRoot(this);

                }
            }
        }
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNode#getLeftValue()
     */
    public Long getLeftValue()
    {
        return leftValue;
    }

    /**
     * Set the left value of this node.
     *
     * @param leftValue
     */
    protected void setLeftValue(Long leftValue)
    {
        this.leftValue = leftValue;
    }

    /**
     * Set the right value of this node.
     *
     * @param rightValue
     */
    protected void setRightValue(Long rightValue)
    {
        this.rightValue = rightValue;
    }

    /**
     * Set the root node.
     *
     * @param root
     */
    protected abstract void setRoot(PreOrderTreeSetNodeBase root);

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNode#getRightValue()
     */
    public Long getRightValue()
    {
        return rightValue;
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNode#getChildBasedOnLeft(java.lang.Long)
     */
    @SuppressWarnings("unchecked")
    public PreOrderTreeSetNodeBase getChildBasedOnLeft(Long leftValue)
    {
        PreOrderTreeSetNodeBase node = null;

        if (leftValue > getLeftValue() && leftValue < getRightValue())
        {
            boolean found = false;
            Iterator<PreOrderTreeSetNodeBase> iter = getChildren().iterator();
            while (iter.hasNext() && !found)
            {
                PreOrderTreeSetNodeBase child = iter.next();
                if (leftValue.equals(child.getLeftValue()))
                {
                    node = child;
                } else
                {
                    node = child.getChildBasedOnLeft(leftValue);
                }

                if (node != null)
                {
                    found = true;
                }
            }
        }
        return node;
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNode#getChildBasedOnRight(java.lang.Long)
     */
    @SuppressWarnings("unchecked")
    public PreOrderTreeSetNodeBase getChildBasedOnRight(Long rightValue)
    {
        PreOrderTreeSetNodeBase node = null;

        if (rightValue < getRightValue() && rightValue > getLeftValue())
        {
            boolean found = false;
            Iterator<PreOrderTreeSetNodeBase> iter = getChildren().iterator();
            while (iter.hasNext() && !found)
            {
                PreOrderTreeSetNodeBase child = iter.next();
                if (rightValue.equals(child.getRightValue()))
                {
                    node = child;
                } else
                {
                    node = child.getChildBasedOnRight(rightValue);
                }

                if (node != null)
                {
                    found = true;
                }
            }
        }
        return node;
    }

    /**
     * Makes room in the tree for the child node
     *
     * The tree must allow children otherwise an illegal
     * state exception is thrown. allowsChildren must be equal to true.
     *
     * @param child tree to add
     * @throws IllegalStateException if the the tree does not allow
     * children.
     *
     */
    protected void makeRoomInTree(PreOrderTreeSetNodeBase child)
    {


        if (log.isDebugEnabled())
        {
            log.debug("\n\n--------------------------------------------------------------\n\n");
            log.debug("Making room int the tree for child " + child.toString());
        }

        if (!allowsChildren)
        {
            throw new IllegalStateException("This tree Does not allow children to be added");
        }

        // number of children we are going to add to this
        // node
        Long numberOfNodes = child.getTreeSize();

        // renumber the child node we are going to add so it
        // now has the correct left and right values
        // before it is added to the tree based on the right
        // value of the child we are adding to
        Long shiftAmount = (getRightValue()) - child.getLeftValue();
        child.updateLeftRightValues(shiftAmount, 0L);

        // now make room in the tree
        PreOrderTreeSetNodeBase root = getTreeRoot();
        if (log.isDebugEnabled())
        {
            log.debug("Root is " + root.toString() + "\n\n");
        }

        root.updateLeftRightValues(numberOfNodes * 2, child.getLeftValue());

        if (log.isDebugEnabled())
        {
            log.debug("DONE Making room int the tree for child " + child.toString());
            log.debug("\n\n--------------------------------------------------------------\n\n");
        }
    }

    /**
     * Cleans up the tree to have sequential numbering.
     * after a child has been removed
     *
     * @param treeSize
     * @param childRightValue
     */
    protected void cleanUpTree(Long childTreeSize, Long childRightValue)
    {
        // update all notes to the right of the top
        // level child node

        if (log.isDebugEnabled())
        {
            log.debug("getting root");
        }

        PreOrderTreeSetNodeBase root = getTreeRoot();

        if (log.isDebugEnabled())
        {
            log.debug("Root is " + root.toString());
        }

        root.updateLeftRightValues(childTreeSize * -2,
                childRightValue);
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.TreeSetNode#getChildren()
     */
    @SuppressWarnings("unchecked")
    public abstract Set getChildren();


    /* (non-Javadoc)
     * @see edu.ur.tree.TreeSetNode#allowsChildren()
     */
    public boolean allowsChildren()
    {
        return allowsChildren;
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.TreeSetNode#getAllowsChildren()
     */
    public boolean getAllowsChildren()
    {
        return allowsChildren();
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.TreeSetNode#setAllowsChildren(boolean)
     */
    public void setAllowsChildren(boolean allowsChildren)
    {
        this.allowsChildren = allowsChildren;
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.TreeSetNode#getChildCount()
     */
    public int getChildCount()
    {
        if (getChildren() != null)
        {
            return getChildren().size();
        } else
        {
            return 0;
        }
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNode#getParent()
     */
    public abstract PreOrderTreeSetNodeBase getParent();

    /* (non-Javadoc)
     * @see edu.ur.tree.TreeSetNode#isRoot()
     */
    public boolean isRoot()
    {
        return getParent() == null;
    }


    /* (non-Javadoc)
     * @see edu.ur.tree.TreeSetNode#getIsRoot()
     */
    public boolean getIsRoot()
    {
        return isRoot();
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.TreeSetNode#isLeaf()
     */
    public boolean isLeaf()
    {
        return getChildCount() == 0;
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.TreeSetNode#getIsLeaf()
     */
    public boolean getIsLeaf()
    {
        return isLeaf();
    }

    /* (non-Javadoc)
     * @see edu.ur.tree.PreOrderTreeSetNode#getTreeSize()
     */
    public Long getTreeSize()
    {
        return (getRightValue() - getLeftValue() + 1L) / 2L;
    }

    /**
     * Updates all left and right values with the change amount. that are
     * greater than the or equal to the minimum amount. ( this can be a
     * negative or positive amount )
     *
     * @param change
     * @param minValue
     */
    @SuppressWarnings("unchecked")
    protected void updateLeftRightValues(Long change, Long minValue)
    {

        if (log.isDebugEnabled())
        {
            log.debug("\n\n ******************* ");
            log.debug("Update LeftRight Values for " + this.toString());
            log.debug("Change is " + change);
            log.debug("minValue is " + minValue);
            log.debug("My right value = " + getRightValue());
            log.debug("My left value = " + getLeftValue());
        }

        if (getLeftValue() >= minValue)
        {
            setLeftValue(getLeftValue() + change);
            log.debug("New left value = " + getLeftValue());
        }

        if (getRightValue() >= minValue)
        {
            setRightValue(getRightValue() + change);
            log.debug("New right value = " + getRightValue());
            log.debug("******************************* \n\n");
        }

        Set<PreOrderTreeSetNodeBase> children = (Set<PreOrderTreeSetNodeBase>) getChildren();

        for (PreOrderTreeSetNodeBase child : children)
        {
            if (child.getLeftValue() >= minValue || child.getRightValue() >= minValue)
            {
                child.updateLeftRightValues(change, minValue);
            }
        }
    }

    /**
     * Find the top level root folder for the entire
     * tree.
     *
     * @param tree
     * @return the root folder
     *
     * @return
     */
    public PreOrderTreeSetNodeBase getTreeRoot()
    {
        if (isRoot())
        {
            return this;
        } else
        {
            return getParent().getTreeRoot();
        }
    }

    /**
     * Find the child in the direct desendent children.
     *
     * @param child
     *            to look for
     * @return found child or null if not found.
     */
    @SuppressWarnings("unchecked")
    protected PreOrderTreeSetNodeBase getChild(PreOrderTreeSetNodeBase child)
    {
        PreOrderTreeSetNodeBase myChild = null;

        Set<PreOrderTreeSetNodeBase> children = (Set<PreOrderTreeSetNodeBase>) getChildren();

        for (PreOrderTreeSetNodeBase c : children)
        {
            if (c.equals(child))
            {
                myChild = c;
            }
        }
        return myChild;
    }

    /**
     * Re-numbers the tree starting from the new root.  The new root must not
     * have a parent otherwise an exception is thrown.
     */
    @SuppressWarnings("unchecked")
    protected static void makeTreeRoot(PreOrderTreeSetNodeBase newRoot)
    {
        if (newRoot.getParent() != null)
        {
            throw new IllegalStateException("New root " + newRoot + " has a parent " + newRoot.getParent());
        }
        newRoot.setRoot(newRoot);
        log.debug("New root tree for " + newRoot);
        Long lastValueUsed = DEFAULT_LEFT_VALUE;
        newRoot.setLeftValue(lastValueUsed);
        log.debug("Setting root left value to " + newRoot.getLeftValue());

        Set<PreOrderTreeSetNodeBase> children = (Set<PreOrderTreeSetNodeBase>) newRoot.getChildren();
        //ordering creates predictability in the tree.
        LinkedList<PreOrderTreeSetNodeBase> myChildren = new LinkedList<PreOrderTreeSetNodeBase>(children);
        Collections.sort(myChildren, new PreOrderTreeLeftComparator());
        for (PreOrderTreeSetNodeBase c : myChildren)
        {
            lastValueUsed = reNumberSubTree(lastValueUsed, c, newRoot);
        }
        newRoot.setRightValue(lastValueUsed + 1L);
        log.debug("Setting root right value to " + newRoot.getRightValue());

    }

    @SuppressWarnings("unchecked")
    private static Long reNumberSubTree(Long lastValueUsed, PreOrderTreeSetNodeBase subTree,
            PreOrderTreeSetNodeBase root)
    {
        log.debug("\n\n\n*****************************");
        Long nextValue = lastValueUsed + 1;
        subTree.setLeftValue(nextValue);
        log.debug("Setting left value to " + subTree.getLeftValue() + " on " + subTree);
        if (subTree.isLeaf())
        {

            nextValue = nextValue + 1L;
            log.debug(subTree + " is leaf setting nextValue to " + nextValue);
        } else
        {
            log.debug(subTree + " is not leaf ");

            //ordering creates predictability in the tree.
            Set<PreOrderTreeSetNodeBase> children = (Set<PreOrderTreeSetNodeBase>) subTree.getChildren();
            LinkedList<PreOrderTreeSetNodeBase> myChildren = new LinkedList<PreOrderTreeSetNodeBase>(children);
            Collections.sort(myChildren, new PreOrderTreeLeftComparator());
            for (PreOrderTreeSetNodeBase c : myChildren)
            {
                nextValue = reNumberSubTree(nextValue, c, root);
            }
            nextValue = nextValue + 1L;
        }

        subTree.setRightValue(nextValue);
        log.debug("setting right value to  " + subTree.getRightValue() + " on " + subTree);
        subTree.setRoot(root);
        return nextValue;
    }

    /**
     * Recursively make all children have the same root node as this tree does
     *
     * @param startChild
     */
    @SuppressWarnings("unchecked")
    protected void updateChildrenWithNewRoot(PreOrderTreeSetNodeBase startChild)
    {
        startChild.setRoot(this.getTreeRoot());
        for (PreOrderTreeSetNodeBase node : (Set<PreOrderTreeSetNodeBase>) startChild.getChildren())
        {
            updateChildrenWithNewRoot(node);
        }
    }
}
