package edu.ur.hibernate.ir.groupspace.db;

import java.io.File;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FileServerService;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFile;
import edu.ur.ir.groupspace.GroupWorkspaceDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolder;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolderDAO;
import edu.ur.ir.repository.Repository;

import edu.ur.ir.user.IrUserDAO;
import edu.ur.util.FileUtil;

@Test(groups = { "baseTests" }, enabled = true)
public class GroupWorkspaceProjectPageFileDAOTest {

	// get the application context 
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	//   Transaction manager 
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
			.getBean("transactionManager");

	//  transaction definition 
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);

	//  Properties file with testing specific information. 
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	//  Get the properties file 
	Properties properties = propertiesLoader.getProperties();

	//  User relational data access  
    IrUserDAO userDAO= (IrUserDAO) ctx
	.getBean("irUserDAO");
    
    // IrFile relational data access   
    IrFileDAO fileDAO= (IrFileDAO) ctx
	.getBean("irFileDAO");
    
    //Group workspace project page file relational data access  
    GroupWorkspaceProjectPageFileDAO groupWorkspaceProjectPageFileDAO= (GroupWorkspaceProjectPageFileDAO) ctx
	.getBean("groupWorkspaceProjectPageFileDAO");
    
     // Group workspace project page folder relational data access 
    GroupWorkspaceProjectPageFolderDAO groupWorkspaceProjectPageFolderDAO= (GroupWorkspaceProjectPageFolderDAO) ctx
	.getBean("groupWorkspaceProjectPageFolderDAO");
    
    //  Researcher data access 
    GroupWorkspaceProjectPageDAO groupWorkspaceProjectPageDAO= (GroupWorkspaceProjectPageDAO) ctx.getBean("groupWorkspaceProjectPageDAO");
    
    // Unique name generator  
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) 
	ctx.getBean("uniqueNameGenerator");
	
	// group workspace data access
	GroupWorkspaceDAO groupWorkspaceDAO = (GroupWorkspaceDAO) ctx
	.getBean("groupWorkspaceDAO");
	
	
	/**
	 * Test groupWorkspaceProjectPage file persistence
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void baseGroupWorkspaceProjectPageFileDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException, LocationAlreadyExistsException {

		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer",
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");
		
		// save the repository
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "test File", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		FileServerService fileServerService = repoHelper.getFileServerService();
		FileInfo info = fileServerService.addFile(repo.getFileDatabase(), f,
				uniqueNameGenerator.getNextName(), "txt");

	
		IrFile irFile = new IrFile(info, "test File");
        
		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
       
		
        GroupWorkspaceProjectPageFile gf = groupWorkspaceProjectPage.createRootFile(irFile, 1);
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		tm.commit(ts);
		
		Long irFileId = irFile.getId();
		
		assert groupWorkspaceProjectPageFileDAO.getById(gf.getId(), false) != null: "Should be able to find groupWorkspaceProjectPage file " + gf;
	    
		Set<GroupWorkspaceProjectPageFile> files = groupWorkspaceProjectPage.getRootFiles();
		
		assert files.size() == 1 : "Files size = " + files.size() + " but should equal 1";
		
		for(GroupWorkspaceProjectPageFile mf : files )
		{
			System.out.println(" Researcher file = " + mf);
		}
		
		
		ts = tm.getTransaction(td);
		assert groupWorkspaceProjectPageFileDAO.getWithSpecifiedIrFile(groupWorkspaceProjectPage.getId(), irFileId).equals(gf) : "The groupWorkspaceProjectPage files should be equal";
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert groupWorkspaceProjectPage.remove(gf) : " file should be removed from groupWorkspaceProjectPage " + gf;
		
		groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		tm.commit(ts);
		
		// clean up
		ts = tm.getTransaction(td);
		groupWorkspaceProjectPageFileDAO.makeTransient(groupWorkspaceProjectPageFileDAO.getById(gf.getId(), false));
		fileDAO.makeTransient(fileDAO.getById(irFile.getId(), false));
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupWorkspace.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	

	}
	
    /**
	 * Test - get groupWorkspaceProjectPage file in a folder
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void getGroupWorkspaceProjectPageFileInAFolderDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException, LocationAlreadyExistsException {

		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer",
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		// save the repository
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		FileServerService fileServerService = repoHelper.getFileServerService();
		FileInfo info = fileServerService.addFile(repo.getFileDatabase(), f,
				uniqueNameGenerator.getNextName(), "txt");
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		IrFile irFile = new IrFile(info, "testFile");
		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder = groupWorkspaceProjectPage.createRootFolder("FolderName");
		
		GroupWorkspaceProjectPageFile rfile = null;
		try
		{
		    rfile= groupWorkspaceProjectPageFolder.addFile(irFile);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		assert rfile != null : "rfile should not be null";
		tm.commit(ts);
		
		// start new transaction
		ts = tm.getTransaction(td);
		groupWorkspaceProjectPage = groupWorkspaceProjectPageDAO.getById(groupWorkspaceProjectPage.getId(), false);
		assert groupWorkspaceProjectPageFileDAO.getFiles(groupWorkspaceProjectPage.getId(), groupWorkspaceProjectPageFolder.getId()).size() == 1 : "Should be equal to 1";
	    
		GroupWorkspaceProjectPageFolder existingFolder = groupWorkspaceProjectPage.getRootFolder(groupWorkspaceProjectPageFolder.getName());
		assert existingFolder != null : "Should find existing folder";
		assert groupWorkspaceProjectPageFolder.getGroupWorkspaceProjectPage().equals(groupWorkspaceProjectPage) : " folder groupWorkspaceProjectPage " +groupWorkspaceProjectPageFolder.getGroupWorkspaceProjectPage() + " should equal " + groupWorkspaceProjectPage; 
		assert groupWorkspaceProjectPageFolder.hashCode() == existingFolder.hashCode() : " hash code " + groupWorkspaceProjectPageFolder.hashCode() + " should equal " + existingFolder.hashCode();
		
		
		for(GroupWorkspaceProjectPageFolder folder : groupWorkspaceProjectPage.getRootFolders())
		{
		    assert groupWorkspaceProjectPage.getRootFolders().contains(folder);
		}
		assert groupWorkspaceProjectPage.getRootFolders().contains(existingFolder) : "root folders should contain " + existingFolder;
		assert groupWorkspaceProjectPage.getRootFolders().contains(groupWorkspaceProjectPageFolder) : "should contain " + groupWorkspaceProjectPageFolder;

		groupWorkspaceProjectPageFolder.remove(rfile);

		assert groupWorkspaceProjectPageFolder.equals(existingFolder): "groupWorkspaceProjectPage folder " + groupWorkspaceProjectPageFolder + " should equal " + existingFolder;
		
		assert groupWorkspaceProjectPage.getRootFolders().size() == 1 : "Should have one root folder but has " + groupWorkspaceProjectPage.getRootFolders().size();
		assert groupWorkspaceProjectPage.remove(groupWorkspaceProjectPageFolder) : " Folder should be removed from user " 
			+ groupWorkspaceProjectPageFolder;
		
		groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		groupWorkspaceProjectPageFileDAO.makeTransient(groupWorkspaceProjectPageFileDAO.getById(rfile.getId(), false));
        assert groupWorkspaceProjectPageFileDAO.getById(rfile.getId(), false) == null : "Should be null but is not for id " + rfile.getId(); 
        tm.commit(ts);
		
		
        // clean up
		ts = tm.getTransaction(td);
		fileDAO.makeTransient(fileDAO.getById(irFile.getId(), false));
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupWorkspace.getId(), false));
		
		repoHelper.cleanUpRepository();
		tm.commit(ts);	

	}

	/**
	 * Test root groupWorkspaceProjectPage file 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void getRootGroupWorkspaceProjectPageFilesDAOTest() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException {

		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer",
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		// save the repository
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		FileServerService fileServerService = repoHelper.getFileServerService();
		FileInfo info = fileServerService.addFile(repo.getFileDatabase(), f,
				uniqueNameGenerator.getNextName(), "txt");
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		
		IrFile irFile = new IrFile(info, "testFile");
        
		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);

		
		
		GroupWorkspaceProjectPageFile rfile= groupWorkspaceProjectPage.createRootFile(irFile, 1);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		groupWorkspaceProjectPage = groupWorkspaceProjectPageDAO.getById(groupWorkspaceProjectPage.getId(), false);
		assert groupWorkspaceProjectPage.getRootFiles().size() == 1 : "root files size = " +  groupWorkspaceProjectPage.getRootFiles().size();
		
		assert groupWorkspaceProjectPageFileDAO.getRootFiles(groupWorkspaceProjectPage.getId()).size() == 1 : "Should be equal to 1";
		GroupWorkspaceProjectPageFile setFile = groupWorkspaceProjectPage.getRootFile(irFile.getName());
		assert setFile.equals(rfile) : " Set file " + setFile + " Should equal " + rfile;
		
		assert groupWorkspaceProjectPage.getRootFiles().contains(rfile);
		
		assert groupWorkspaceProjectPage.remove(rfile) : " File should be removed from user " + rfile;
		
		groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);

		tm.commit(ts);
		
		
		
        // clean up
		ts = tm.getTransaction(td);
		groupWorkspaceProjectPageFileDAO.makeTransient(groupWorkspaceProjectPageFileDAO.getById(rfile.getId(), false));

		fileDAO.makeTransient(fileDAO.getById(irFile.getId(), false));
	    groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupWorkspace.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	

	}


}
