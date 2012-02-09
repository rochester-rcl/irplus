package edu.ur.ir.groupspace;

import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.user.IrUser;

/**
 * Testing for group workspace project page.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class GroupWorkspaceProjectPageTest {
	
	/**
	 * Test adding a member to the group project page
	 * 
	 * @throws IllegalFileSystemNameException 
	 * @throws DuplicateNameException 
	 */
	public void testAddMember() throws IllegalFileSystemNameException, DuplicateNameException
	{
		IrUser u1 = new IrUser("user1", "password");
		IrUser u2 = new IrUser("user2", "password");
		GroupWorkspace groupSpace = new GroupWorkspace("group" , "test group");
		GroupWorkspaceUser gwu1 = groupSpace.add(u1, true);
		GroupWorkspaceUser gwu2 = groupSpace.add(u2, false);
        GroupWorkspaceProjectPage page = groupSpace.getGroupWorkspaceProjectPage();
        page.addMember(gwu1);
        page.addMember(gwu2);
        
        assert page.getMember(gwu1).getGroupWorkspaceUser().equals(gwu1) : "Should find member 1";
        assert page.getMember(gwu2).getGroupWorkspaceUser().equals(gwu2) : "Should find member 2";
        
        assert page.removeMember(page.getMember(gwu1)) : "Member should be removed";
        assert page.removeMember(page.getMember(gwu2)) : "Member should be removed" ;
	}
	
	/**
	 * Test adding a member to the group project page
	 * 
	 * @throws IllegalFileSystemNameException 
	 * @throws DuplicateNameException 
	 */
	public void testMoveMember() throws IllegalFileSystemNameException, DuplicateNameException
	{
		IrUser u1 = new IrUser("user1", "password");
		IrUser u2 = new IrUser("user2", "password");
		GroupWorkspace groupSpace = new GroupWorkspace("group" , "test group");
		GroupWorkspaceUser gwu1 = groupSpace.add(u1, true);
		GroupWorkspaceUser gwu2 = groupSpace.add(u2, false);
        GroupWorkspaceProjectPage page = groupSpace.getGroupWorkspaceProjectPage();
        GroupWorkspaceProjectPageMember member1 = page.addMember(gwu1);
        GroupWorkspaceProjectPageMember member2 = page.addMember(gwu2);
        
        assert member1.getOrder() == 1 : "order should be 1 but is " + member1.getOrder();
        assert member2.getOrder() == 2 : "order should be 2 but is " + member2.getOrder();
        
        page.moveMemberDown(member1);
        assert member1.getOrder() == 2 : "order should be 2 but is " + member1.getOrder();
        assert member2.getOrder() == 1 : "order should be 1 but is " + member2.getOrder();
        
	}

}
