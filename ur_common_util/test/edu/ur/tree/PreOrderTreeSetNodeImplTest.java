
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

import org.testng.annotations.Test;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Test Pre Order Tree Node Implementation
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups =
{
    "baseTests"
}, enabled = true)
public class PreOrderTreeSetNodeImplTest
{

    /**
     * Basic tree tests
     */
    public void basicTest()
    {
        PreOrderTreeSetNodeImpl impl = new PreOrderTreeSetNodeImpl("root");
        assert impl.getLeftValue() == 1L : "Left value should be 1";
        assert impl.getRightValue() == 2L : "Right value should be 2";
        assert impl.getTreeSize() == 1L : "There should only be one node";
        assert impl.getAllowsChildren() == true : "Default Should allow children";
        assert impl.isLeaf() == true : "Doesn't have any children";
        assert impl.isRoot() == true : "Should be the root";
    }

    /**
     *  Test adding children.  Predictability helps in this one
     *  as the algorithm alwayas adds the child to the right most
     *  side with respect to left and right values.  This is
     *  also due to a speed issue.  By adding farthest to the right
     *  the least amount of nodes needs to be updated.
     */
    public void addSubtreeTest()
    {
        PreOrderTreeSetNodeImpl root = new PreOrderTreeSetNodeImpl("root");
        PreOrderTreeSetNodeImpl child_a = new PreOrderTreeSetNodeImpl("child_a");

        root.addChild(child_a);
        assert child_a.isLeaf() : "Child a should be a leaf";
        //assert !child_a.isRoot() : "Child a should not be root";

        assert root.isRoot() : "Root should still be root";
        assert !root.isLeaf() : "Root should not be a leaf";

        PreOrderTreeSetNodeImpl child_b = new PreOrderTreeSetNodeImpl("child_b");
        child_a.addChild(child_b);

        assert !child_a.isLeaf() : "Child a should no longer be a leaf";
        assert child_b.isLeaf() : "Child b should be a leaf";

        PreOrderTreeSetNodeImpl child_c = new PreOrderTreeSetNodeImpl("child_c");
        child_b.addChild(child_c);
        assert child_b.getChildren().contains(child_c) : " Should contain child c";

        PreOrderTreeSetNodeImpl child_d = new PreOrderTreeSetNodeImpl("child_d");
        root.addChild(child_d);
        assert root.getChildren().contains(child_d);

        PreOrderTreeSetNodeImpl child_e = new PreOrderTreeSetNodeImpl("child_e");
        child_a.addChild(child_e);

        PreOrderTreeSetNodeImpl child_f = new PreOrderTreeSetNodeImpl("child_f");
        PreOrderTreeSetNodeImpl child_g = new PreOrderTreeSetNodeImpl("child_g");
        PreOrderTreeSetNodeImpl child_h = new PreOrderTreeSetNodeImpl("child_h");

        assert child_d.getLeftValue() == 10L : "Left Value should be equal to 10 but is " + child_d.getLeftValue();
        assert child_d.getRightValue() == 11L : "Left Value should be equal to 11 but is " + child_d.getLeftValue();

        assert child_c.getLeftValue() == 4L : "Left Value should be equal to 4 but is " + child_c.getLeftValue();
        assert child_c.getRightValue() == 5L : "Right value should be equal to 5 " + child_c.getRightValue();

        assert child_b.getLeftValue() == 3L : "Left Value should be equal to 3 but is " + child_b.getLeftValue();
        assert child_b.getRightValue() == 6L : "Right value should be equal to 6 " + child_b.getRightValue();

        assert child_a.getLeftValue() == 2L : "Left Value should be equal to 2 but is " + child_a.getLeftValue();
        assert child_a.getRightValue() == 9L : "Right value should be equal to 9 " + child_a.getRightValue();

        assert root.getLeftValue() == 1L : "Left value should equal 1 but is " + root.getLeftValue();
        assert root.getRightValue() == 12L : "Right value should equal 10 but is " + root.getRightValue();


        child_f.addChild(child_g);
        child_f.addChild(child_h);

        assert child_f.getLeftValue() == 1L : "Left value should equal 1 but is " + child_f.getLeftValue();
        assert child_f.getRightValue() == 6L : "Right value should equal 6 but is " + child_f.getRightValue();

        assert child_g.getLeftValue() == 2L : "Left value should equal 2 but is " + child_g.getLeftValue();
        assert child_g.getRightValue() == 3L : "Right value should equal 3 but is " + child_g.getRightValue();

        assert child_h.getLeftValue() == 4L : "Left value should equal 2 but is " + child_h.getLeftValue();
        assert child_h.getRightValue() == 5L : "Right value should equal 3 but is " + child_h.getRightValue();

        child_b.addChild(child_f);

        assert root.getLeftValue() == 1L : "Left value should equal 1 but is " + root.getLeftValue();
        assert root.getRightValue() == 18L : "Right value should equal 18 but is " + root.getRightValue();

        assert child_d.getLeftValue() == 16L : "Left Value should be equal to 16 but is " + child_d.getLeftValue();
        assert child_d.getRightValue() == 17L : "Left Value should be equal to 17 but is " + child_d.getLeftValue();

        assert child_a.getLeftValue() == 2L : "Left Value should be equal to 2 but is " + child_a.getLeftValue();
        assert child_a.getRightValue() == 15L : "Right value should be equal to 15 " + child_a.getRightValue();

        assert child_b.getLeftValue() == 3L : "Left Value should be equal to 3 but is " + child_b.getLeftValue();
        assert child_b.getRightValue() == 12L : "Right value should be equal to 12 but is" + child_b.getRightValue();

        assert child_e.getLeftValue() == 13L : "Left Value should be equal to 13 but is " + child_e.getLeftValue();
        assert child_e.getRightValue() == 14L : "Left Value should be equal to 14 but is " + child_e.getRightValue();

        assert child_c.getLeftValue() == 4L : "Left Value should be equal to 4 but is " + child_c.getLeftValue();
        assert child_c.getRightValue() == 5L : "Right value should be equal to 5 but is " + child_c.getRightValue();

        assert child_f.getLeftValue() == 6L : "Left value should equal 6 but is " + child_f.getLeftValue();
        assert child_f.getRightValue() == 11L : "Right value should equal 11 but is " + child_f.getRightValue();

        assert child_h.getLeftValue() == 9L : "Left value should equal 9 but is " + child_h.getLeftValue();
        assert child_h.getRightValue() == 10L : "Right value should equal 10 but is " + child_h.getRightValue();

        assert child_g.getLeftValue() == 7L : "Left value should equal 7 but is " + child_g.getLeftValue();
        assert child_g.getRightValue() == 8L : "Right value should equal 8 but is " + child_g.getRightValue();
    }

    /**
     * Test creating a sizable tree
     **/
    public void bigTreeTest()
    {

        // add 1000 nodes at the root level
        PreOrderTreeSetNodeImpl root = new PreOrderTreeSetNodeImpl("root");
        for (int x = 1; x < 1000; x++)
        {
            PreOrderTreeSetNodeImpl tree = new PreOrderTreeSetNodeImpl("" + x);
            root.addChild(tree);
        }

        assert root.getTreeSize() == 1000 : "There should be 1000 trees but found " + root.getTreeSize();

        PreOrderTreeSetNodeImpl child_a = new PreOrderTreeSetNodeImpl("child_a");

        // add child a to the root folder
        root.addChild(child_a);

        // add 500 chidren to a
        for (int x = 1; x < 500; x++)
        {
            PreOrderTreeSetNodeImpl tree = new PreOrderTreeSetNodeImpl("a_" + x);
            child_a.addChild(tree);
        }

        assert child_a.getTreeSize() == 500L : "Child a has 500 trees but found " + child_a.getTreeSize();

        // make sure the total size of the tree is 1500
        assert root.getTreeSize() == 1500L : "Root has 1500 trees " + root.getTreeSize();

        PreOrderTreeSetNodeImpl child_b = new PreOrderTreeSetNodeImpl("child_b");

        child_a.addChild(child_b);


        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss:SS");

        Date d = new Date(System.currentTimeMillis());
        System.out.println("Test adding children to child " + sdf.format(d));
    /*
    // add 30000 children
    // each folder 1000 folders deep.
    PreOrderTreeSetNodeImpl addToChild = child_b;
    for( int x = 1; x< 30000; x++)
    {
    if( x % 1000 == 0)
    {
    d = new Date( System.currentTimeMillis() );
    System.out.println( x + " children added " + sdf.format(d));
    }

    PreOrderTreeSetNodeImpl tree = new PreOrderTreeSetNodeImpl();
    addToChild.addChild(tree);

    if( x % 1000 == 0)
    {
    System.out.println("Setting new current child left_value = " + addToChild.leftValue);
    addToChild = tree;
    }
    }

    d = new Date( System.currentTimeMillis() );
    System.out.println("Done Adding 30000 children to child " + sdf.format(d));

    assert child_b.getTreeSize() == 30000L : "Child b should have 20000 trees including itself "
    + child_b.getTreeSize();


    d = new Date( System.currentTimeMillis() );
    System.out.println("Adding child first child of root " + sdf.format(d));

    PreOrderTreeSetNodeImpl firstChild = root.getChildBasedOnLeft(2l);
    assert firstChild != null : "First child should not be null";
    assert !firstChild.isRoot() : "First child should not be root";


    // add a tree to the very fist node in the set of chilren
    // this will cause all nodes to be re-numbered.
    PreOrderTreeSetNodeImpl tree = new PreOrderTreeSetNodeImpl();

    firstChild.addChild(tree);
    d = new Date( System.currentTimeMillis() );
    System.out.println("Done Adding child first child of root " + sdf.format(d));
     */
    }

    /**
     *
     */
    public void addChildrenTest()
    {
        PreOrderTreeSetNodeImpl root = new PreOrderTreeSetNodeImpl("root");
        PreOrderTreeSetNodeImpl child_a = new PreOrderTreeSetNodeImpl("child_a");

        root.addChild(child_a);

        assert root.getLeftValue() == 1L : "Left value should equal 1 but is " + root.getLeftValue();
        assert root.getRightValue() == 4L : "Right value should equal 4 but is " + root.getRightValue();

        assert child_a.getLeftValue() == 2L : "Left Value should be equal to 2 but is " + child_a.getLeftValue();
        assert child_a.getRightValue() == 3L : "Right value should be equal to 3 but is " + child_a.getRightValue();

        PreOrderTreeSetNodeImpl child_b = new PreOrderTreeSetNodeImpl("child_b");
        child_a.addChild(child_b);

        assert child_b.getLeftValue() == 3L : "Left Value should be equal to 3 but is " + child_a.getLeftValue();
        assert child_b.getRightValue() == 4L : "Right value should be equal to 4 " + child_a.getRightValue();

        assert child_a.getLeftValue() == 2L : "Left Value should be equal to 2 but is " + child_a.getLeftValue();
        assert child_a.getRightValue() == 5L : "Right value should be equal to 5 " + child_a.getRightValue();

        assert root.getLeftValue() == 1L : "Left value should equal 1 but is " + root.getLeftValue();
        assert root.getRightValue() == 6L : "Right value should equal 6 but is " + root.getRightValue();

        PreOrderTreeSetNodeImpl child_c = new PreOrderTreeSetNodeImpl("child_c");
        child_b.addChild(child_c);

        assert child_c.getLeftValue() == 4L : "Left Value should be equal to 4 but is " + child_c.getLeftValue();
        assert child_c.getRightValue() == 5L : "Right value should be equal to 5 but is " + child_c.getRightValue();

        assert child_b.getLeftValue() == 3L : "Left Value should be equal to 3 but is " + child_b.getLeftValue();
        assert child_b.getRightValue() == 6L : "Right value should be equal to 6 but is " + child_b.getRightValue();

        assert child_a.getLeftValue() == 2L : "Left Value should be equal to 2 but is " + child_a.getLeftValue();
        assert child_a.getRightValue() == 7L : "Right value should be equal to 7 " + child_a.getRightValue();

        assert root.getLeftValue() == 1L : "Left value should equal 1 but is " + root.getLeftValue();
        assert root.getRightValue() == 8L : "Right value should equal 8 but is " + root.getRightValue();

        PreOrderTreeSetNodeImpl child_d = new PreOrderTreeSetNodeImpl("child_d");
        root.addChild(child_d);

        assert child_d.getLeftValue() == 8L : "Left Value should be equal to 8 but is " + child_d.getLeftValue();
        assert child_d.getRightValue() == 9L : "Left Value should be equal to 9 but is " + child_d.getLeftValue();

        assert child_c.getLeftValue() == 4L : "Left Value should be equal to 4 but is " + child_c.getLeftValue();
        assert child_c.getRightValue() == 5L : "Right value should be equal to 5 " + child_c.getRightValue();

        assert child_b.getLeftValue() == 3L : "Left Value should be equal to 3 but is " + child_b.getLeftValue();
        assert child_b.getRightValue() == 6L : "Right value should be equal to 6 " + child_b.getRightValue();

        assert child_a.getLeftValue() == 2L : "Left Value should be equal to 2 but is " + child_a.getLeftValue();
        assert child_a.getRightValue() == 7L : "Right value should be equal to 7 " + child_a.getRightValue();

        assert root.getLeftValue() == 1L : "Left value should equal 1 but is " + root.getLeftValue();
        assert root.getRightValue() == 10L : "Right value should equal 10 but is " + root.getRightValue();
    }

    /**
     *  Test finding children
     */
    public void findTreeTest()
    {
        PreOrderTreeSetNodeImpl root = new PreOrderTreeSetNodeImpl("root");
        PreOrderTreeSetNodeImpl child_a = new PreOrderTreeSetNodeImpl("child_a");

        root.addChild(child_a);
        PreOrderTreeSetNodeImpl child_b = new PreOrderTreeSetNodeImpl("child_b");
        child_a.addChild(child_b);
        PreOrderTreeSetNodeImpl child_c = new PreOrderTreeSetNodeImpl("child_c");
        child_b.addChild(child_c);
        PreOrderTreeSetNodeImpl child_d = new PreOrderTreeSetNodeImpl("child_d");
        root.addChild(child_d);
        PreOrderTreeSetNodeImpl child_e = new PreOrderTreeSetNodeImpl("child_e");
        child_a.addChild(child_e);

        PreOrderTreeSetNodeImpl child_f = new PreOrderTreeSetNodeImpl("child_f");
        PreOrderTreeSetNodeImpl child_g = new PreOrderTreeSetNodeImpl("child_g");
        PreOrderTreeSetNodeImpl child_h = new PreOrderTreeSetNodeImpl("child_h");

        child_f.addChild(child_g);
        child_f.addChild(child_h);

        child_b.addChild(child_f);

        // make sure all children accounted for
        assert root.getLeftValue() == 1L : "Left value should equal 1 but is " + root.getLeftValue();
        assert root.getRightValue() == 18L : "Right value should equal 18 but is " + root.getRightValue();

        assert child_d.getLeftValue() == 16L : "Left Value should be equal to 16 but is " + child_d.getLeftValue();
        assert child_d.getRightValue() == 17L : "Left Value should be equal to 17 but is " + child_d.getLeftValue();
        assert root.getChildBasedOnLeft(16L) == child_d : "Child d should be found";
        assert root.getChildBasedOnRight(17L) == child_d : "Child d should be found";


        assert child_a.getLeftValue() == 2L : "Left Value should be equal to 2 but is " + child_a.getLeftValue();
        assert child_a.getRightValue() == 15L : "Right value should be equal to 15 " + child_a.getRightValue();
        assert root.getChildBasedOnLeft(2L) == child_a : "Child a should be found";
        assert root.getChildBasedOnRight(15L) == child_a : "Child a should be found";

        assert child_a.getChildBasedOnLeft(3L) == child_b : "Child b should be found";
        assert child_a.getChildBasedOnRight(12L) == child_b : "Child b should be found";

        assert child_a.getChildBasedOnLeft(13L) == child_e : "Child e should be found";
        assert child_a.getChildBasedOnRight(14L) == child_e : "Child e should be found";

        assert child_b.getLeftValue() == 3L : "Left Value should be equal to 3 but is " + child_b.getLeftValue();
        assert child_b.getRightValue() == 12L : "Right value should be equal to 12 but is" + child_b.getRightValue();
        assert root.getChildBasedOnLeft(3L) == child_b : "Child b should be found";
        assert root.getChildBasedOnRight(12L) == child_b : "Child b should be found";


        assert child_e.getLeftValue() == 13L : "Left Value should be equal to 13 but is " + child_e.getLeftValue();
        assert child_e.getRightValue() == 14L : "Left Value should be equal to 14 but is " + child_e.getRightValue();
        assert root.getChildBasedOnLeft(13L) == child_e : "Child e should be found";

        assert child_c.getLeftValue() == 4L : "Left Value should be equal to 4 but is " + child_c.getLeftValue();
        assert child_c.getRightValue() == 5L : "Right value should be equal to 5 but is " + child_c.getRightValue();
        assert root.getChildBasedOnLeft(4L) == child_c : "Child c should be found";

        assert child_f.getLeftValue() == 6L : "Left value should equal 6 but is " + child_f.getLeftValue();
        assert child_f.getRightValue() == 11L : "Right value should equal 11 but is " + child_f.getRightValue();
        assert root.getChildBasedOnLeft(6L) == child_f : "Child f should be found";

        assert child_h.getLeftValue() == 9L : "Left value should equal 9 but is " + child_h.getLeftValue();
        assert child_h.getRightValue() == 10L : "Right value should equal 10 but is " + child_h.getRightValue();

        assert child_g.getLeftValue() == 7L : "Left value should equal 7 but is " + child_g.getLeftValue();
        assert child_g.getRightValue() == 8L : "Right value should equal 8 but is " + child_g.getRightValue();

    }

    /**
     *  Test removing children
     */
    public void removeTreeTest()
    {
        PreOrderTreeSetNodeImpl root = new PreOrderTreeSetNodeImpl("root");
        PreOrderTreeSetNodeImpl child_a = new PreOrderTreeSetNodeImpl("child_a");

        root.addChild(child_a);
        PreOrderTreeSetNodeImpl child_b = new PreOrderTreeSetNodeImpl("child_b");
        child_a.addChild(child_b);
        PreOrderTreeSetNodeImpl child_c = new PreOrderTreeSetNodeImpl("child_c");
        child_b.addChild(child_c);
        PreOrderTreeSetNodeImpl child_d = new PreOrderTreeSetNodeImpl("child_d");
        root.addChild(child_d);
        PreOrderTreeSetNodeImpl child_e = new PreOrderTreeSetNodeImpl("child_e");
        child_a.addChild(child_e);

        PreOrderTreeSetNodeImpl child_f = new PreOrderTreeSetNodeImpl("child_f");
        PreOrderTreeSetNodeImpl child_g = new PreOrderTreeSetNodeImpl("child_g");
        PreOrderTreeSetNodeImpl child_h = new PreOrderTreeSetNodeImpl("child_h");

        child_f.addChild(child_g);
        child_f.addChild(child_h);
        child_b.addChild(child_f);

        // make sure all children accounted for
        assert root.getLeftValue() == 1L : "Left value should equal 1 but is " + root.getLeftValue();
        assert root.getRightValue() == 18L : "Right value should equal 18 but is " + root.getRightValue();

        assert child_d.getLeftValue() == 16L : "Left Value should be equal to 16 but is " + child_d.getLeftValue();
        assert child_d.getRightValue() == 17L : "Left Value should be equal to 17 but is " + child_d.getLeftValue();

        assert child_a.getLeftValue() == 2L : "Left Value should be equal to 2 but is " + child_a.getLeftValue();
        assert child_a.getRightValue() == 15L : "Right value should be equal to 15 " + child_a.getRightValue();

        assert child_b.getLeftValue() == 3L : "Left Value should be equal to 3 but is " + child_b.getLeftValue();
        assert child_b.getRightValue() == 12L : "Right value should be equal to 12 but is" + child_b.getRightValue();

        assert child_e.getLeftValue() == 13L : "Left Value should be equal to 13 but is " + child_e.getLeftValue();
        assert child_e.getRightValue() == 14L : "Left Value should be equal to 14 but is " + child_e.getRightValue();

        assert child_c.getLeftValue() == 4L : "Left Value should be equal to 4 but is " + child_c.getLeftValue();
        assert child_c.getRightValue() == 5L : "Right value should be equal to 5 but is " + child_c.getRightValue();

        assert child_f.getLeftValue() == 6L : "Left value should equal 6 but is " + child_f.getLeftValue();
        assert child_f.getRightValue() == 11L : "Right value should equal 11 but is " + child_f.getRightValue();

        assert child_h.getLeftValue() == 9L : "Left value should equal 9 but is " + child_h.getLeftValue();
        assert child_h.getRightValue() == 10L : "Right value should equal 10 but is " + child_h.getRightValue();

        assert child_g.getLeftValue() == 7L : "Left value should equal 7 but is " + child_g.getLeftValue();
        assert child_g.getRightValue() == 8L : "Right value should equal 8 but is " + child_g.getRightValue();

        assert child_f.removeChild(child_g) : "Child f should be removed";

        // make sure all children accounted for
        assert root.getLeftValue() == 1L : "Left value should equal 1 but is " + root.getLeftValue();
        assert root.getRightValue() == 16L : "Right value should equal 16 but is " + root.getRightValue();

        assert child_d.getLeftValue() == 14L : "Left Value should be equal to 14 but is " + child_d.getLeftValue();
        assert child_d.getRightValue() == 15L : "Left Value should be equal to 15 but is " + child_d.getLeftValue();

        assert child_a.getLeftValue() == 2L : "Left Value should be equal to 2 but is " + child_a.getLeftValue();
        assert child_a.getRightValue() == 13L : "Right value should be equal to 13 " + child_a.getRightValue();

        assert child_b.getLeftValue() == 3L : "Left Value should be equal to 3 but is " + child_b.getLeftValue();
        assert child_b.getRightValue() == 10L : "Right value should be equal to 10 but is" + child_b.getRightValue();

        assert child_e.getLeftValue() == 11L : "Left Value should be equal to 11 but is " + child_e.getLeftValue();
        assert child_e.getRightValue() == 12L : "Left Value should be equal to 12 but is " + child_e.getRightValue();

        assert child_c.getLeftValue() == 4L : "Left Value should be equal to 4 but is " + child_c.getLeftValue();
        assert child_c.getRightValue() == 5L : "Right value should be equal to 5 but is " + child_c.getRightValue();

        assert child_f.getLeftValue() == 6L : "Left value should equal 6 but is " + child_f.getLeftValue();
        assert child_f.getRightValue() == 9L : "Right value should equal 9 but is " + child_f.getRightValue();

        assert child_h.getLeftValue() == 7L : "Left value should equal 7 but is " + child_h.getLeftValue();
        assert child_h.getRightValue() == 8L : "Right value should equal 8 but is " + child_h.getRightValue();

        assert child_g.isRoot() : "Child g should be a root now";
        assert root.getChildBasedOnLeft(7L) != child_g : "Child should not be found";

        assert child_f.removeChild(child_h) : "Child h should be removed";

        assert root.getLeftValue() == 1L : "Left value should equal 1 but is " + root.getLeftValue();
        assert root.getRightValue() == 14L : "Right value should equal 14 but is " + root.getRightValue();

        assert child_d.getLeftValue() == 12L : "Left Value should be equal to 12 but is " + child_d.getLeftValue();
        assert child_d.getRightValue() == 13L : "Left Value should be equal to 13 but is " + child_d.getLeftValue();

        assert child_a.getLeftValue() == 2L : "Left Value should be equal to 2 but is " + child_a.getLeftValue();
        assert child_a.getRightValue() == 11L : "Right value should be equal to 11 " + child_a.getRightValue();

        assert child_b.getLeftValue() == 3L : "Left Value should be equal to 3 but is " + child_b.getLeftValue();
        assert child_b.getRightValue() == 8L : "Right value should be equal to 8 but is" + child_b.getRightValue();

        assert child_e.getLeftValue() == 9L : "Left Value should be equal to 9 but is " + child_e.getLeftValue();
        assert child_e.getRightValue() == 10L : "Left Value should be equal to 10 but is " + child_e.getRightValue();

        assert child_c.getLeftValue() == 4L : "Left Value should be equal to 4 but is " + child_c.getLeftValue();
        assert child_c.getRightValue() == 5L : "Right value should be equal to 5 but is " + child_c.getRightValue();

        assert child_f.getLeftValue() == 6L : "Left value should equal 6 but is " + child_f.getLeftValue();
        assert child_f.getRightValue() == 7L : "Right value should equal 7 but is " + child_f.getRightValue();

        assert child_h.isRoot() : "Child h should be a root now";
        assert root.getChildBasedOnLeft(7L) != child_h : "Child should not be found";

        assert child_a.removeChild(child_b) : "Child b should be removed";

        assert root.getLeftValue() == 1L : "Left value should equal 1 but is " + root.getLeftValue();
        assert root.getRightValue() == 8L : "Right value should equal 8 but is " + root.getRightValue();

        assert child_a.getLeftValue() == 2L : "Left Value should be equal to 2 but is " + child_a.getLeftValue();
        assert child_a.getRightValue() == 5L : "Right value should be equal to 3 " + child_a.getRightValue();

        assert child_d.getLeftValue() == 6L : "Left Value should be equal to 6 but is " + child_d.getLeftValue();
        assert child_d.getRightValue() == 7L : "Left Value should be equal to 7 but is " + child_d.getLeftValue();

        assert child_e.getLeftValue() == 3L : "Left Value should be equal to 4 but is " + child_e.getLeftValue();
        assert child_e.getRightValue() == 4L : "Left Value should be equal to 5 but is " + child_e.getRightValue();

    }

    /**
     *  Test making a child a different tree.
     */
    public void newRootTest()
    {
        PreOrderTreeSetNodeImpl root = new PreOrderTreeSetNodeImpl("root");
        // make sure all children accounted for
        assert root.getLeftValue() == 1L : "Left value should equal 1 but is " + root.getLeftValue();
        assert root.getRightValue() == 2L : "Right value should equal 2 but is " + root.getRightValue();


        PreOrderTreeSetNodeImpl child_a = new PreOrderTreeSetNodeImpl("child_a");

        root.addChild(child_a);

        assert root.getLeftValue() == 1L : "Left value should equal 1 but is " + root.getLeftValue();
        assert root.getRightValue() == 4L : "Right value should equal 4 but is " + root.getRightValue();

        assert child_a.getLeftValue() == 2L : "Left value should equal 1 but is " + root.getLeftValue();
        assert child_a.getRightValue() == 3L : "Right value should equal 4 but is " + root.getRightValue();

        PreOrderTreeSetNodeImpl child_b = new PreOrderTreeSetNodeImpl("child_b");
        child_a.addChild(child_b);

        assert child_a.getLeftValue() == 2L : "Left value should equal 2 but is " + child_a.getLeftValue();
        assert child_a.getRightValue() == 5L : "Right value should equal 5 but is " + child_a.getRightValue();

        assert child_b.getLeftValue() == 3L : "Left value should equal 3 but is " + child_b.getLeftValue();
        assert child_b.getRightValue() == 4L : "Right value should equal 4 but is " + child_b.getRightValue();

        assert root.getLeftValue() == 1L : "Left value should equal 1 but is " + root.getLeftValue();
        assert root.getRightValue() == 6L : "Right value should equal 6 but is " + root.getRightValue();



        PreOrderTreeSetNodeImpl child_c = new PreOrderTreeSetNodeImpl("child_c");
        child_b.addChild(child_c);
        PreOrderTreeSetNodeImpl child_d = new PreOrderTreeSetNodeImpl("child_d");
        root.addChild(child_d);
        PreOrderTreeSetNodeImpl child_e = new PreOrderTreeSetNodeImpl("child_e");
        child_a.addChild(child_e);

        PreOrderTreeSetNodeImpl child_f = new PreOrderTreeSetNodeImpl("child_f");
        PreOrderTreeSetNodeImpl child_g = new PreOrderTreeSetNodeImpl("child_g");
        PreOrderTreeSetNodeImpl child_h = new PreOrderTreeSetNodeImpl("child_h");

        child_f.addChild(child_g);
        child_f.addChild(child_h);
        child_b.addChild(child_f);



        assert child_d.getLeftValue() == 16L : "Left Value should be equal to 16 but is " + child_d.getLeftValue();
        assert child_d.getRightValue() == 17L : "Left Value should be equal to 17 but is " + child_d.getLeftValue();

        assert child_a.getLeftValue() == 2L : "Left Value should be equal to 2 but is " + child_a.getLeftValue();
        assert child_a.getRightValue() == 15L : "Right value should be equal to 15 " + child_a.getRightValue();

        assert child_b.getLeftValue() == 3L : "Left Value should be equal to 3 but is " + child_b.getLeftValue();
        assert child_b.getRightValue() == 12L : "Right value should be equal to 12 but is" + child_b.getRightValue();

        assert child_e.getLeftValue() == 13L : "Left Value should be equal to 13 but is " + child_e.getLeftValue();
        assert child_e.getRightValue() == 14L : "Left Value should be equal to 14 but is " + child_e.getRightValue();

        assert child_c.getLeftValue() == 4L : "Left Value should be equal to 4 but is " + child_c.getLeftValue();
        assert child_c.getRightValue() == 5L : "Right value should be equal to 5 but is " + child_c.getRightValue();

        assert child_f.getLeftValue() == 6L : "Left value should equal 6 but is " + child_f.getLeftValue();
        assert child_f.getRightValue() == 11L : "Right value should equal 11 but is " + child_f.getRightValue();

        assert child_h.getLeftValue() == 9L : "Left value should equal 9 but is " + child_h.getLeftValue();
        assert child_h.getRightValue() == 10L : "Right value should equal 10 but is " + child_h.getRightValue();

        assert child_g.getLeftValue() == 7L : "Left value should equal 7 but is " + child_g.getLeftValue();
        assert child_g.getRightValue() == 8L : "Right value should equal 8 but is " + child_g.getRightValue();


        assert child_a.removeChild(child_b) : "Child b should be removed";

        assert root.getLeftValue() == 1L : "Root left value should = 1 but equals " + root.getLeftValue();
        assert root.getRightValue() == 8L : "Root right value should = 8 but equals " + root.getRightValue();


        // we have a new  root
        assert child_c.getLeftValue() == 2L : "Left value should equal 2 but equals " + child_c.getLeftValue();
        assert child_c.getRightValue() == 3L : "Right value should equal 3 but equals " + child_c.getRightValue();

        assert child_b.getLeftValue() == 1L : "Left value should equal 1 " + child_b.getLeftValue();
        assert child_b.getRightValue() == 10L : "Right value should equal 10 but equals " + child_b.getRightValue();

        assert child_b.getTreeRoot().equals(child_b) : " Child b tree root should be child b but is " +
                child_b.getTreeRoot();
        assert child_c.getTreeRoot().equals(child_b) : "Tree root should be child_b but is " + child_c.getTreeRoot();
        assert child_f.getTreeRoot().equals(child_b) : "Tree root should be child_b but is " + child_f.getTreeRoot();
        assert child_g.getTreeRoot().equals(child_b) : "Tree root should be child_b but is " + child_g.getTreeRoot();
        assert child_h.getTreeRoot().equals(child_b) : "Tree root should be child_b but is " + child_h.getTreeRoot();


    }
}
