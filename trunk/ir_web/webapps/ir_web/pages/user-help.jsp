<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

<!--  
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
-->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>


<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title>User Help (DRAFT)</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!-- Core + Skin CSS -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
        <ur:styleSheet href="page-resources/css/base-ur.css"/>
        <ur:styleSheet href="page-resources/yui/menu/assets/skins/sam/menu.css"/>

        <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/home_page_content_area.css"/>
        <ur:styleSheet href="page-resources/css/main_menu.css"/>
        <ur:styleSheet href="page-resources/css/tables.css"/>

        <!-- Dependencies --> 
        <ur:js src="${pageContext.request.contextPath}/page-resources/images/help-images/user/page-resources/images/help-images/user/page-resources/yui/yahoo-dom-event/yahoo-dom-event.js"/>
        <ur:js src="${pageContext.request.contextPath}/page-resources/images/help-images/user/page-resources/images/help-images/user/page-resources/yui/connection/connection-min.js"/>
        <ur:js src="${pageContext.request.contextPath}/page-resources/images/help-images/user/page-resources/images/help-images/user/page-resources/yui/container/container_core-min.js"/>
        <ur:js src="${pageContext.request.contextPath}/page-resources/images/help-images/user/page-resources/images/help-images/user/page-resources/yui/menu/menu-min.js"/>
        
        <!-- Source File -->
        <ur:js src="${pageContext.request.contextPath}/page-resources/images/help-images/user/page-resources/images/help-images/user/page-resources/js/menu/main_menu.js"/>
        
        <!--  base path information -->
 	    <ur:js src="${pageContext.request.contextPath}/page-resources/images/help-images/user/page-resources/images/help-images/user/page-resources/js/util/base_path.jsp"/>
 	    <ur:js src="${pageContext.request.contextPath}/page-resources/images/help-images/user/page-resources/images/help-images/user/page-resources/js/util/ur_util.js"/>
   
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body regin of the page -->
            <div id="bd">
               <h3>User Help (DRAFT)</h3> 
               




<a id="Back to top"></a>
<h4><a href="#Document Description">1. Document Description</a><br/></h4>
<ul>
<li><h5><a href="#Project Web Site">1.1  Project Web Site</a><br/></h5></li>
<li><h5><a href="#Project Group">1.2  Project Group</a><br/></h5></li>
<li><h5><a href="#Suggestions">1.3  Suggestions</a><br/></h5></li>
</ul>

<h4><a href="#Home Page">2.  Home Page</a><br/></h4>


<h4><a href="#Creating an account">3.  Creating an account</a><br/></h4>
<ul>
<li><h5><a href="#User account types">3.1  User account types</a><br/></h5></li>
</ul>

<h4><a href="#Managing Account Information">4.  Managing Account Information</a><br/></h4>
<ul>
<li><h5><a href="#Changing password">4.1  Changing password</a><br/></h5></li>
<li><h5><a href="#Managing Your Emails">4.2  Managing Your Emails</a><br/></h5></li>
<ul>
<li><h5><a href="#Adding a new email">4.2.1  Adding a new email</a><br/></h5></li>
</ul>
<li><h5><a href="#Managing Publication Names">4.3  Managing Publication Names</a><br/></h5></li>
<ul>
<li><h5><a href="#Adding a Publication Name">4.3.1  Adding a Publication Name</a><br/></h5></li>
</ul>
</ul>

<h4><a href="#Workspace – File and Folder Management">5.  Workspace – File and Folder Management</a><br/></h4>
<ul>
<h5><a href="#Adding a Folder">5.1  Adding a Folder</a><br/></h5>
<h5><a href="#Adding a File">5.2  Adding a File</a><br/></h5>
<h5><a href="#Editing a file">5.3  Editing a file</a><br/></h5>
<h5><a href="#Adding a new version of a file">5.4  Adding a new version of a file</a><br/></h5>
<h5><a href="#View versions and properties of a file">5.5  View versions and properties of a file</a><br/></h5>
<h5><a href="#Creating a Folder">5.6  Creating a Folder</a><br/></h5>
<h5><a href="#Deleting File(s) or Folder(s)">5.7  Deleting File(s) or Folder(s)</a><br/></h5>
<h5><a href="#Moving Files and Folders">5.8  Moving Files and Folders</a><br/></h5>
</ul>

<h4><a href="#Sharing, Collaboration and Co-Authoring">6.  Sharing, Collaboration and Co-Authoring</a><br/></h4>
<ul>
<h5><a href="#Sharing with someone who does not yet have an account">6.1  Sharing with someone who does not yet have an account</a><br/></h5>
<h5><a href="#Looking to see if someone already exists in the system">6.2  Looking to see if someone already exists in the system</a><br/></h5>
<h5><a href="#Receiving a Shared File">6.3  Receiving a Shared File</a><br/></h5>
<h5><a href="#Working on a shared File">6.4  Working on a shared File</a><br/></h5>
<h5><a href="#Overriding a Lock">6.4  Overriding a Lock</a><br/></h5>
<h5><a href="#Un-sharing and Changing Sharing Permissions">6.4  Un-sharing and Changing Sharing Permissions</a><br/></h5>
</ul>

<h4><a href="#Publishing">7.  Publishing</a><br/></h4>
<ul>
<h5><a href="#Adding files to a publication">7.1  Adding files to a publication</a><br/></h5>
<h5><a href="#Adding Information to a Publication">7.2  Adding Information to a Publication</a><br/></h5>
<h5><a href="#Adding Publication Contributors - Publishing Name Authority Control">7.3  Adding Publication Contributors - Publishing Name Authority Control</a><br/></h5>
<h5><a href="#Preview Publication">7.4  Preview Publication</a><br/></h5>
<h5><a href="#Submitting to a collection">7.5  Submitting to a collection</a><br/></h5>
<h5><a href="#My Publications Tab">7.6  My Publications Tab</a><br/></h5>
<ul>
<h5><a href="#Editing an existing publication and Versioning">7.6.1  Editing an existing publication and Versioning</a><br/></h5>
<h5><a href="#Publication Properties">7.6.2  Publication Properties</a><br/></h5>
<h5><a href="#Personal Collections – organizing publications">7.6.3  Personal Collections – organizing publications</a><br/></h5>
<h5><a href="#Deleting publications and Personal Collections">7.6.4  Deleting publications and Personal Collections</a><br/></h5>
</ul>
</ul>

<h4><a href="#Workspace Searching">8.  Workspace Searching</a><br/></h4>

<h4><a href="#Researcher Pages">9.  Researcher Pages</a><br/></h4>
<ul>
<h5><a href="#Accessing Researcher Page Tools">9.1  Accessing Researcher Page Tools</a><br/></h5>
<h5><a href="#Adding Research to your Researcher Page">9.2  Adding Research to your Researcher Page</a><br/></h5>
<ul>
<h5><a href="#Adding a Folder">9.2.1 Adding a Folder</a><br/></h5>
<h5><a href="#Adding a link">9.2.2  Adding a link</a><br/></h5>
<h5><a href="#Adding a File">9.2.3  Adding a File</a><br/></h5>
<h5><a href="#Adding a personal publication">9.2.4  Adding a personal publication</a><br/></h5>
<h5><a href="#Removing Items from the researcher page">9.2.5  Removing Items from the researcher page</a><br/></h5>
<h5><a href="#Adding Pictures">9.2.6  Adding Pictures</a><br/></h5>
<h5><a href="#Adding Links">9.2.7  Adding Links</a><br/></h5>
<h5><a href="#Previewing your researcher page">9.2.8  Previewing your researcher page</a><br/></h5>
</ul>
</ul>

<h4><a href="#Appendix A">10.  Appendix A</a><br/></h4>
<hr>
<hr>

<p>
<h3><a id="Document Description">1.  Document Description</a></h3>

This document describes administration functionality of the system.  This document assumes the system has already been installed and is ready for use.  If you need to install the system please see the Ir Plus installation manual.<br/>

<h4><a id="Project Web Site">1.1  Project Web Site</a></h4>

If interested, you can visit the project web site on Google code at the following URL: 
<a href="http://code.google.com/p/irplus/" target="_blank">http://code.google.com/p/irplus/</a><br/>
The source code can be downloaded from this location, as well as finding out more about the project.  <br/>
<h4><a id="Project Group">1.2  Project Group</a></h4>

The project also has a corresponding Google group at <a href="http://groups.google.com/group/irplus?pli=1" target="_blank">http://groups.google.com/group/irplus?pli=1 </a><br/>
<h4><a id="Suggestions">1.3  Suggestions</a></h4>

Please feel free to submit comments and suggestions for how this documentation can be improved.<br/>
<br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>
<hr>
</p>


<p>
<h3><a id="Home Page">2. Home Page</a></h3>
Welcome to irplus. Yourhomepage should look similar to the one below (not including the arrows and
numbers). This does not show a fully loaded ir plus system; for screen shots of our alpha system with
over 5,000 items please see appendix A.<br/> 

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Home_Page.jpg"><br/>


Here is a description of the numbered items:
<ol>
	<li><b>Menu Bar</b> – currently has only a login option.  Upon logging in more options are available to the user<br/><br/></li>
	<li><b>Browse/Search Module</b> – Allows the user to search the ir plus system<br/><br/></li>
<li><b>Institutional Collections Module </b>– current set of institutional collections<br/><br/></li>
	<li><b>Statistics Module</b> – Basic statistics for the entire system<br/><br/></li>
	<li><b>Researchers Module</b>– Section that alternates researcher pages each time the page is visited <br/><br/></li>
	<li><b>Pictures Module </b>– pictures/logos that can be uploaded for display for the system – also alternated each time the page is visited<br/><br/></li>
	<li><b>News Module</b>– news that may be important to share with users – for example maintenance dates, or scheduled down time<br/><br/></li>
</ol><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr><hr>
</p>

<p>
<h3><a id="Creating an account">3.  Creating an account</a></h3>

By selecting the Login option it will take you to the following screen<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/User_Login.jpg"><br/><br/>

<ol>
<li> <b>User Name</b> – user name entered when account was created. Currently this can be
anything.<br/><br/></li>
<li> <b>Password</b> – local password or Net Id password based on Login Type selected.<br/><br/></li>
<li><b>Login / Reset</b> buttons for resetting the form.<br/><br/></li>
<li><b>Forgot password</b> to allow you to reset a password, and <b>Create New Account</b> to allow
you to create a new account.<br/></li>
</ol> 
Selecting <b>Forgot Password</b> lets you enter your user name and an email will be sent to your default address. Selecting <b>Create New Account</b> allows you to create a new basic account in the system.
Select the Create New account option. This will take you to the following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Create_Acoount.jpg"><br/>

<br/>
Enter your information. Create a password that you feel is secure. Select an affiliation and
department that makes the most sense for you. Some affiliations may need approval by an
administrator to confirm the affiliation is correct; however, this will not prevent you from
getting an account.<br/><br/>
Once you are done entering the information, click “Create Account.” This should result in the
following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Account_Craetion_Successful.jpg"><br/><br/>

You should get at least one email like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Email_confirmation.jpg"><br/><br/>

Follow or copy and paste the link into your browser. This should take you to the login screen –
enter your user name and password and select the “Login” button. This should show you a
screen like the following:<br/>
<br/>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Email_Address_Verified.jpg"><br/><br/>

<b>Note: Each time you add a new email to your account, you must verify that email.</b><br/>
<br/>
You may also get another email like the following depending on the type of affiliation you
choose:

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Affilation_Verfied.jpg"><br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="User account types">3.1   User account types</a></h4>
You may be given one of several types of accounts:<br/><br/>
1. <b>Basic user</b> - This means you can log in and may be assigned to groups with special permissions.
This is the most basic type of account. You do not have any authoring capabilities and cannot
create a researcher page.<br/><br/>
2. <b>Collaborating User</b> – You can work on documents shared with you but you cannot start new
documents on your own.<br/><br/>
3. <b>Authoring User</b> - You can start authoring works, share and collaborate on documents with other
users and publish to approved collections.<br/><br/>
4. <b>Researcher</b> – You can do everything an authoring user can do as well as create a researcher
page.<br/><br/>
5. <b>Collection Administrator</b> – You have authoring abilities as well as the ability to manage specified
collections and the items within them.<br/><br/>
6. <b>Administrator</b> - Full control over the entire system.<br/><br/>

<a href="#Back to top" style="font-size=10px"> Back to top </a><hr><hr>


<h3><a id="Managing Account Information">4.  Managing Account Information</a></h3>

<b>Applies to</b>: Basic User, Collaborating User, Authoring User, Researcher, Collection Administrator,
Administrator<br/><br/>
To manage account information once you have logged in, go to Welcome [Your User Name] -> My
Account as shown below<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Go_to_my_account.jpg"><br/><br/>

This will bring you to the following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/My_account.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>
<ol>
<li>Tabs to manage account information</li><br/><br/>
<ul>
<li>User Information – basic user information (Currently Selected)</li>
<li>Emails – set of emails this user uses. Multiple emails are generally only used for
authoring / collaborating users.</li>
<li>Publication Names –names under which this user publishes. This is only used for users
who publish into the system.</li><br/><br/>
</ul>
<li>Change password option</li><br/><br/>
<li>Form to manage First Name, Last Name and Phone Number</li><br/><br/>
</ol>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="Changing password">4.1   Changing password</a></h4>

To change your password, select the change password button. This should bring up the following
screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Change_Password.jpg"><br/><br/>

Enter the new password and confirm new password. Click the submit button.<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Managing Your Emails">4.2   Managing Your Emails</a></h4>

Today many users have multiple emails. The system allows you to tie multiple emails to a single
account. Each new email address you add must go through a verification process. The default email
cannot be changed until the new email has been verified. This prevents the stealing of email addresses
within the system.<br/><br/>
The following is a screen shot of the Emails tab:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Managing_your_emails.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>
<ol>
<li>Buttons to manage emails.</li><br/><br/>
<ul>
<li>New Email – Allows you to enter a new email to associate with your account</li>
<li>Delete Email – Allows you to delete unwanted emails from the system</li><br/><br/>
</ul>
<li>List of emails for the current account</li><br/><br/>
<ul>
<li>Email Type – this can be default or nothing. The default email is the email the system
will send all messages to.</li>
<li>Id – database id ( for administration purposes only)</li>
<li>Email – email address</li>
<li>Status - the status of the email</li><br/>
<ul>
<li>Verified – means the email has been verified by the system.</li>
<li>Pending – means the system is waiting for you to follow a link provided in an
email, to verify that address in the system.</li><br/><br/>
</ul>
</ul>
</ol>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Adding a new email">4.2.1  Adding a new email</a></h4>

To add a new email, select the new email button. The following screen will appear:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_a_new_email.jpg"><br/><br/>

Enter the new email and select submit. You should see a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Email_sent_message_for_change_of_email.jpg"><br/><br/>

Clicking ok brings you back to the screen. Notice the new email says Pending Verification. You should
receive an email similar to the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Email_recieved_for_change_of_email.jpg"><br/><br/>

By following the address, you will be asked to log in and once logged in see the following verification:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/New_Email_address_verified.jpg"><br/><br/>

Going back to your account, you should see both emails as verified:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/View_new_email_added.jpg"><br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="Managing Publication Names">4.3  Managing Publication Names</a></h4>
The names tab is for the names you wish to publish under. By adding a name here, it allows you to
select the name when publishing (rather than typing it in every time). You can also add names during
the publishing process. Names will only be needed by users who will be publishing. The following is a
screen shot of the Publication Names tab:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Managing_Publication_Names.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>
<ol>
<li>Buttons to manage names</li><br/><br/>
<ul>
<li>New Name – Create a new name</li>
<li>Delete Name – Delete selected names</li><br/><br/>
</ul>
<li>List of names for the current user.</li><br/><br/>
</ol>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="Adding a Publication Name">4.3.1 Adding a Publication Name</a></h4>
To add a name under which you will publish, select the new name button<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_a_Publication_Name.jpg"><br/><br/>

Enter the information and click submit. The Authoritative name checkbox allows you to set a name as
your “authoritative” name. This allows the system to associate one primary name with your account,
while also allowing you to publish under multiple names if needed.<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr><hr>


<h3><a id="Workspace – File and Folder Management">5.  Workspace – File and Folder Management</a></h3>
<b>Applies to</b>: Collaborating User, Authoring User, Researcher, Collection Administrator, Administrator<br/><br/>
The workspace is where you can author, co-author and publish your work into the system. All users
except for basic users will be taken to this area when they log in.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Workspace_File_and_Folder_Management.jpg"><br/><br/>

Description of numbered items:<br/><br/>
<ol>
<li>Tabs for managing workspace information</li><br/><br/>
<ul>
<li>My Files (Currently Selected) – lists all of the files and folders in your workspace</li>
<li>My Publications – Area to store and manage publications that have been published or
that you are getting ready to publish</li>
<li>Search My Workspace – Full text search for files, folders and publications within the
workspace</li>
<li>Shared File Inbox – Inbox where files are first stored when users share files with you.</li><br/><br/>
</ul>
<li>Path – Current path (directory structure) you are in</li><br/><br/>
<li>Drop down for actions that can be performed on checked files and folders</li><br/><br/>
<ul>
<li>Publish – Publish all selected files and all files within selected folders</li>
<li>Share – Share the selected files and all files within selected folders</li>
<li>Delete – Delete the selected files and folders including all files within the folders</li>
<li>Move – Move the selected files and folders</li><br/><br/>
</ul>
<li>List of all files and folders within the path current path (see #2)</li><br/><br/>
<li>Quick buttons</li><br/><br/>
<ul>
<li>New Folder – create a new folder in the current path</li>
<li>Add File – upload a single file to the current path</li>
<li>Add Files – upload multiple files at once</li>
<li>Publish – take a work from your workspace and officially add it to one or more
collection(s) in the repository</li><br/><br/>
</ul>
</ol>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="Adding a Folder">5.1 Adding a Folder</a></h4>
Folders are a way to organize your files. To create a folder, simply click “New Folder” and name it.<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="Adding a File">5.2 Adding a File</a></h4>
To add a file select the add file button. You should see a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_a_File.jpg"><br/><br/>

The following is a list of the fields:<br/><br/>
<ol>
<li>File – file to upload to the system; use the Browse button to find it on your local system.</li><br/><br/>
<li>Display Name(optional) - if you would like to change the name of the file when it is uploaded
you can do it here.</li><br/><br/>
<li>File Description – Description of the file, such as “AAAI 2010 paper.”</li><br/><br/>
</ol>

To upload the file, select the browse button. You should see a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Browse_a_file.jpg"><br/><br/>

Navigate on your local system to the file you would like to upload. Once you select the file, click the
Open button.<br/><br/>
The following shows a file I have selected to upload:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Select_a_file.jpg"><br/><br/>

By selecting the submit button the file will be uploaded into the current path. Once the file is uploaded
you will see it in your workspace, as pictured below.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Info_of_the_file.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>
<ol>
<li>Checkbox to select the file</li><br/><br/>
<li>Drop Down arrow for menu options for the file</li><br/><br/>
<ul>
<li>Download</li>
<li>Rename</li>
<li>Lock & Edit</li>
<li>Add a New Version</li>
<li>Share</li>
<li>Publish</li>
<li>Delete</li>
<li>Properties</li><br/><br/>
</ul>
<li>Version – Most recent version of the file</li><br/><br/>
<li>File Size – Size of the most recent version</li><br/><br/>
<li>Properties – Link to view properties of the file, including a list of all versions</li><br/><br/>
<li>Share – Options to share the file for collaboration</li><br/><br/>
<li>Owner – owner of the file</li><br/><br/>
</ol>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Editing a file">5.3  Editing a file</a></h4>

To edit a file the best practice is to select Lock & Edit file:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Lock_&_edit_a_file.jpg"><br/><br/>

Select the drop down arrow next to the file and select Lock & Edit. This will bring up the following
screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Open_or_Save_your_file_for_editing_after_locking_it.jpg"><br/><br/>

The following is a list of the numbered items:<br/><br/>
<ol>
<li>Option to download the file. Here you can save the file to your local file system.</li><br/><br/>
<li>Lock icon – when a file is locked, the lock icon is shown, and who has locked the file is displayed
below the file.</li><br/><br/>
</ol>
You can now edit the file on your local file system.
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="Adding a new version of a file">5.4  Adding a new version of a file</a></h4>

To add a new version to a file, select the drop down and select the Add New Version option as shown
below.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Add_a_new_version_of_your_file.jpg"><br/><br/>

You will be presented with a window like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Add_a_new_version_of_your_file_1.jpg"><br/><br/>

Select the file you wish to upload using the Browse option (See the section on adding a file).<br/><br/>
1. Points to the keep locked option. If you wish to store the file in its current state to work on
later, select this option. This will keep the file locked for editing. This option would mainly apply
to a file you are collaborating on with another person, to prevent them from editing it before
you are ready.<br/><br/>
Once you have selected the new version select submit. You should see the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Add_a_new_version_of_your_file_2.jpg"><br/><br/>

Notice (1) – The version number has changed. There are now two versions of the file.<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="View versions and properties of a file">5.5  View versions and properties of a file</a></h4>

To view all the versions and properties of the file select the properties link in the table or select the
properties option in the file drop down as shown below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/View_versions_and_properties_of_a_file_1.jpg"><br/><br/>

This will show the following page:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/View_versions_and_properties_of_a_file_2.jpg"><br/><br/>

The following is a list of the numbered arrows:<br/><br/>

<ol>
<li>Virtual location of the file</li><br/><br/>
<li>Current version Administration data</li><br/><br/>
<ul>
<li>Editing status (Locked/Unlocked)</li>
<li>Name – name of the file</li>
<li>Version – version number</li>
<li>Creation date – date the current version was</li>
<li>Creator – current creator of the versionSize – Uploaded version size</li>
<li>Size – Uploaded version size</li>
<li>Size on Disk – sometimes the file is larger when loaded into memory</li>
<li>Path – Path to file in repository (Not for user use)</li>
<li>File Information Id – File information database id <b>(Not for user use)</b></li>
<li>File Information Id – File information database id <b>(Not for user use)</b></li><br/><br/>
</ul>
<li>Buttons to manage the current file</li><br/><br/>
<ul>
<li>Add new version - Add new version to the current set of versions</li>
<li>Publish – Publish the file into the repository (see publishing section)</li>
<li>Share – Share the file with other users (See collaboration section)</li>
<li>Lock & Edit – Lock and edit the file</li>
<li>Rename – Rename the file</li>
<li>Change owner – Allows you to change the owner of the file. The owner can only be
changed to collaborators on the file.</li><br/><br/>
</ul>
<li>Sharing – List of users with whom the file is shared.</li><br/><br/>
<li>All file versions – Selecting the file version number will download that particular version.</li><br/><br/>
</ol>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Creating a Folder">5.6  Creating a Folder</a></h4>
To create a folder, select the New Folder button. This should produce a screen like the following.

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Creating_a_Folder_1.jpg"><br/><br/>

Enter the Folder name and optional description and select submit. This will add the folder to the current
location.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Creating_a_Folder_2.jpg"><br/><br/>

By selecting the folder name you will navigate into the folder. For example selecting the “IR Plus
Documentation” folder, the screen will look like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Creating_a_Folder_3.jpg"><br/><br/>

Here you can add more files or folders as needed.<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Deleting File(s) or Folder(s)">5.7  Deleting File(s) or Folder(s)</a></h4>
You can use the drop down to remove files or folders one at a time. To remove multiple files and folders
at once, select the files and folders you wish to delete as shown below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Deleting_Files_or_Folders_1.jpg"><br/><br/>

Then select the “Action on checked files and folders …” box as shown below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Deleting_Files_or_Folders_2.jpg"><br/><br/>

This should bring up a confirmation for the delete:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Deleting_Files_or_Folders_3.jpg"><br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="Moving Files and Folders">5.8  Moving Files and Folders</a></h4>
You may need to organize or reorganize your files. To do this you can move files and folders around.
For example, the following shows two Folders and one File:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Moving_Files_and_Folders_1.jpg"><br/><br/>

Say we want to move the file “IR_Plus_Admin_Manual.docx” and “IR Plus Documentation” into the
Repository Information Folder. First select the folder and file you wish to move as shown below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Moving_Files_and_Folders_2.jpg"><br/><br/>

Next select the Move option in the “Action on checked files and folders …” box as shown below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Moving_Files_and_Folders_3.jpg"><br/><br/>

This will bring up a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Moving_Files_and_Folders_4.jpg"><br/><br/>

The following is a list of the numbered items:<br/><br/>
<ol>
<li>Cancel button – Cancel the move</li><br/><br/>
<li>List of files and folders to move</li><br/><br/>
<li>Location in your workspace where the files and folders will be moved</li><br/><br/>
<li>Move Button – Executes the move to the specified location in 3</li><br/><br/>
<li>Destination – Files and folders marked in red are being moved</li><br/><br/>
</ol>
Select the hyperlinked folder Repository Information in the Destination (5) to navigate into the folder.
The screen will look something like below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Moving_Files_and_Folders_5.jpg"><br/><br/>

Selecting the Move button will place the files in the path shown in the “Move To Location,” and take you
to the location where the files and folders were moved as shown below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Moving_Files_and_Folders_6.jpg"><br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr><hr>


<h3><a id="Sharing, Collaboration and CoAuthoring">6.  Sharing, Collaboration and CoAuthoring</a></h3>
The system allows you to share files. You can do this in two ways. You can share a single file at a time
by selecting the drop down for a file and selecting Share as shown below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Sharing_Collaboration_and_CoAuthoring_1.jpg"><br/><br/>

Or, you can select multiple files and folders at once using their checkboxes. By selecting a folder you will
share all files within that folder, including files within sub folders, but this DOES NOT share the folder.
Adding more files to the folder afterwards will not automatically share those files. The following
demonstrates sharing some files:<br/><br/>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Sharing_Collaboration_and_CoAuthoring_2.jpg"><br/><br/>

Then select the “Action on checked files and folders” drop down Share option, as shown below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Sharing_Collaboration_and_CoAuthoring_3.jpg"><br/><br/>

This will take you to a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Sharing_Collaboration_and_CoAuthoring_4.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>
<ol>
<li>Tabs to managing sharing</li><br/><br/>
<ul>
<li>Invite User Tab – Allows you to invite other users to view or collaborate on a file</li>
<li>Search For Users To Share With – Allows you to search for existing users to share with</li><br/><br/>
</ul>
<li>Back to Workspace – Allows you to return to your workspace</li><br/><br/>
<li>Email Address – Email address of user you wish to share with</li><br/><br/>
<li>Message – Message you want sent to user when sharing the file</li><br/><br/>
<li>Message – Message you want sent to user when sharing the file</li><br/><br/>
<ul>
<li>Message – Message you want sent to user when sharing the file</li>
<li>The user can download, edit and upload new versions. This allows users to add new
versions and collaborate.</li>
<li>The user can download, edit and upload new versions as well as share/ un-share the file
with other users and give those users permissions. This allows the users who the file
has been shared with to share with other users creating a chain of sharing.</li><br/><br/>
</ul>
<li>The set of files you are sharing with the another user.</li><br/><br/>
</ol>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="Sharing with someone who does not yet have an account">6.1  Sharing with someone who does not yet have an account</a></h4>
If the person you wish to share with does not have an account, this is not a problem. You can share with
them by typing in their email directly into the email box as shown below. If the person actually does
have an account and you didn’t realize it, do not worry, the system will realize this and handle it for you.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Sharing_with_someone_who_doesnot_yet_have_an_account_1.jpg"><br/><br/>

Here I’m sharing with an email account that does not exist in the system. Once I click the invite user
button the following screen should show:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Sharing_with_someone_who_doesnot_yet_have_an_account_2.jpg"><br/><br/>

The following shows that the file is to be shared with the person who has an email natesarr@gmail.com.
Once the person has created an account, the [to be shared] note will be gone. Notice you can also
UnShare the file. The following shows the email a user who does not exist in the system will receive
when they are invited to work on a document.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Sharing_with_someone_who_doesnot_yet_have_an_account_3.jpg"><br/><br/>

Following the link will take them to the following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Sharing_with_someone_who_doesnot_yet_have_an_account_4.jpg"><br/><br/>

There are a couple of situations when a user has been invited by email:<br/><br/>
<ol>
<li>The user already has an account but the email you used is not registered with their account. By
logging in the new email will be listed in their set of emails so it will be recognized by the system
in the future.</li><br/><br/>
<li>The user has never created an account and must now create a new account. This will take them
through the account creation process.</li><br/><br/>
</ol>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Looking to see if someone already exists in the system">6.2  Looking to see if someone already exists in the system</a></h4>
If you think the person you want to share with may already have an account in the system, you can
search for them. Select the Search For Users To Share with tab. This will bring up the following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Looking_to_see_if_someone_already_exists_in_the_system_1.jpg"><br/><br/>

Enter the name and select search. This will return all users it finds in the system matching the search.
Select the email if you wish to share with the user. This will return you to the Invite user screen to fill in
the remaining information.<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Receiving a Shared File">6.3  Receiving a Shared File</a></h4>
Once a file has been shared with you, it will be placed in your shared inbox tab – see picture below (1):<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Receiving_a_Shared_File_1.jpg"><br/><br/>

Once a file shows up in your shared file inbox, you should move it into your workspace:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Receiving_a_Shared_File_2.jpg"><br/><br/>

The following describes the numbered items:<br/><br/>
<ol>
<li>Buttons to manage moving</li><br/><br/>
<ul>
<li>Move the file into the My Files area</li>
<li>Delete the file – this allows you to reject the file</li><br/><br/>
</ul>
<li>List of files that have been shared with you</li><br/><br/>
<li>Link to download the file if you wish to view the file</li><br/><br/>
</ol>
To move the file select the check box next to the file and click the move button. You should see a
screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Receiving_a_Shared_File_3.jpg"><br/><br/>

The following is a list of the numbered items:<br/><br/>
<ol>
<li>Cancel the move</li><br/><br/>
<li>List of files to move from the shared inbox</li><br/><br/>
<li>Move button</li><br/><br/>
<li>Location to move the shared files</li><br/><br/>
</ol>
Since the user does not have any folders, the only location they can put the file is in the “root” directory,
known as My Files. Once the shared file is moved the screen will look like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Receiving_a_Shared_File_4.jpg"><br/><br/>

Now John Doe is sharing a file with ndsarr. Notice that even though the file is 3.08 MB this is not
charged to John Doe’s account (1) and the File System Size is still 0. This is because only the owner is
charged for the space used.<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="Working on a shared File">6.4  Working on a shared File</a></h4>
When working on a shared file the user should lock the file as shown in section 5.3. The other user will
be able to see who is working on the file. From the previous example, if ndsarr locked the file for
editing, John Doe would see the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Working_on_a_shared_File.jpg"><br/><br/>

Notice the note that the file is locked and the lock icon is shown to all collaborators.<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="Overriding a Lock">6.5  Overriding a Lock</a></h4>
If for some reason an owner needs to get a file back in an emergency situation – say someone went on
vacation and forgot to unlock the file – the owner can override a lock. This functionality should only be
used in rare circumstances and is generally considered rude if done for no reason at all. Only an owner
of the file can override a lock. The following shows how this is done:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Overriding_a_Lock.jpg"><br/><br/>

Once this option is selected, the lock is removed.<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Unsharing and Changing Sharing Permissions">6.6  Unsharing and Changing Sharing Permissions</a></h4>
If for some reason you have made a mistake with the sharing on your files, you can change the sharing
of a file. To manage the sharing of a file, select the Share option as shown below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Unsharing_and_Changing_Sharing_Permissions_1.jpg"><br/><br/>

The following screen should appear:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Unsharing_and_Changing_Sharing_Permissions_2.jpg"><br/><br/>

The following is a list of the numbered items<br/><br/>
<ol>
<li>List of files being shared</li><br/><br/>
<li>List of users who are sharing the file</li><br/><br/>
</ol>

To unshared the file with the user, select the Unshare option. You will get a confirmation screen like the
following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Unsharing_and_Changing_Sharing_Permissions_3.jpg"><br/><br/>

Select yes to un-share the file with the user or no to cancel.<br/><br/>

To edit the sharing permissions a user currently has, select the Edit Link. You will see a screen like the
following with the user’s current permissions shown:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Unsharing_and_Changing_Sharing_Permissions_4.jpg"><br/><br/>

Make changes as necessary.
<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr><hr>



<h3><a id="Publishing">7.  Publishing</a></h3>
Once you have finished working on a document, you may want to continue and publish the document
into the repository. This is an optional step and is not required. To start the publishing process, selectthe file or files you wish to publish and select the publish button as shown below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Publishing_1.jpg"><br/><br/>

<b>(NOTE: you do not need to select any files and files are not required in a publication. For example, if
you only want to add links to a publication).</b><br/><br/>
Once you select publish, you should see a screen like the following to enter the name of the publication:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Publishing_2.jpg"><br/><br/>

Enter the name of the publication and select submit. This should bring you to a screen like the
following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Publishing_3.jpg"><br/><br/>

The following is a description of the numbered items:<br><br/>
<ol>
<li>Name of the publication being created</li><br><br/>
<li>Current step in process</li><br><br/>
<li>Button to finish the publication process later</li><br><br/>
<li>Current location in your personal file system</li><br><br/>
<li>List of files and folders in your personal file system at the given location (see 4)</li><br><br/>
<li>Option to add a link to the publication</li><br><br/>
<li>Next button to go to the next step</li><br><br/>
<li>Current list of files added to the publication.</li><br><br/>
</ol>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Adding files to a publication">7.1  Adding files to a publication</a></h4>
You can navigate around your file system in the Add Files step of the publication, so if you forget to add
a file, don’t worry. To navigate around your file system, use the Current Location links and/or the links
listed under File System. For example, navigating into the Repository Information folder from the
previous example shows the following screen:<br><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_files_to_a_publication_1.jpg"><br/><br/>

Notice the path has changed and shows that I am in the Repository Information folder (1). An Add
button is also displayed next to the folder listed under File System. Selecting this option will add all files
in that folder and its sub folders.<br/><br/>
Number 3 shows that the Admin manual has already been added. To add an individual file, select the
Add link (4).<br/><br/>
When there is more than one file in a publication, files can be moved up and down so they are listed in
the order you want (5). You can also select the version of the file to add (6). In this example version 2
will be added to the publication. You can also add a description to the file (7). If you wish to remove a
file, select the Remove link next to the file.<br/><br/>
Shown below is an example of two files added to the publication.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_files_to_a_publication_2.jpg"><br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Adding Information to a Publication">7.2  Adding Information to a Publication</a></h4>
The following screens show the information that can be added to a publication – there are four screen
shots that show the entire form.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_Information_to_a_Publication_1.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>
<ol>
<li>Finish Later – will save the current state of the publication for finishing later</li><br/><br/>
<li>List of files added to the publication</li><br/><br/>
<li>Title of the publication</li><br/><br/>
<li>Adds a subtitle box to the publication</li><br/><br/>
<li>Description of the publication</li><br/><br/>
<li>Person who is submitting the publication</li><br/><br/>
<li>Thesis indicator</li><br/><br/>


<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_Information_to_a_Publication_2.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>

<li>Primary Type – primary type of publication – for example is it a book, article, etc.</li><br/><br/>
<li>Secondary Types – If the publication can be classified as more than one type, select the
secondary ways that this publication can be classified.</li><br/><br/>
<li>Add Existing Series – Allows you to add another series name, if your publication is part of more
than one series</li><br/><br/>
<li>Create New Series – Add a new series that does not yet exist in the system. This will add a new
entry to the Series Name drop down list.</li><br/><br/>
<li>Series Name – Select a pre-existing series name from a drop down list.</li><br/><br/>
<li>Remove Series - Remove the series name from this publication.</li><br/><br/>
<li>Add an existing identifier type to the item – Adds another box to allow for more than one
identifier.</li><br/><br/>
<li>Create New Identifier Type – Add a new identifier to the list of existing identifiers.</li><br/><br/>
<li>Remove Identifier – Remove the identifier from the item.</li><br/><br/>
<li>Drop down list of identifiers.</li><br/><br/>
<li>Add Existing Extent Type – Allows you to enter more than one type of extent , such as Page Size,
Number of Pages, and Length in Time etc.</li><br/><br/>
<li>Create New Extent Type – Allows you to create a new extent type to be displayed in the list of
extent types.</li><br/><br/>
<li>Drop down list of existing extent types.</li><br/><br/>
<li>Remove Extent – Remove the extent from the publication.</li><br/><br/>


<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_Information_to_a_Publication_3.jpg"><br/><br/>


<li>Drop down list to select language of the publication</li><br/><br/>
<li>Area to enter a list of comma separated subject key words</li><br/><br/>
<li>Abstract for the publication</li><br/><br/>
<li>Add Existing Sponsor – Creates another field to add more than one sponsor</li><br/><br/>
<li>Create New Sponsor – Allows you to add a sponsor to the drop down list of sponsors</li><br/><br/>
<li>Remove Sponsor – Remove a sponsor from the item</li><br/><br/>
<li>Drop down list of sponsors</li><br/><br/>
<li>Date the publication was presented to the public</li><br/><br/>
<li>Date this publication can be made available to the public – aka embargo date; to be used for
materials currently under review for commercial publication <b>(under construction)</b></li><br/><br/>


<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_Information_to_a_Publication_4.jpg"><br/><br/>


<li>Date the publication was originally created</li><br/><br/>
<li>Option to allow entry of information pertaining to materials that have been published in other
venues such as journals or books.Selecting No will hide fields 33-36.</li><br/><br/>
<li>Add new Publisher – Add a new publisher to the list of publishers</li><br/><br/>
<li>Drop down list of publishers</li><br/><br/>
<li>Date Published – Date the publication was published</li><br/><br/>
<li>Citation for the publication</li><br/><br/>
</ol><a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="Adding Publication Contributors Publishing Name Authority Control">7.3  Adding Publication Contributors Publishing Name Authority Control</a></h4>
The system does its best to maintain authority control with respect to authors within the system. The
following shows the screen to add contributors to publications:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_Publication_Contributors_Publishing_Name_Authority_Control_1.jpg"><br/><br/>

<ol>
<li>Full text search of existing contributor names in the system</li><br/><br/>
<li>Option to add a new contributor into the system</li><br/><br/>
</ol>
The first thing you should do is search to see if the contributor name already exists in the system.
Searching for the name “Nathan” will result in the following update to the screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_Publication_Contributors_Publishing_Name_Authority_Control_2.jpg"><br/><br/>

The system found two contributor names with the name Nathan. The following is a description of the
numbered items:<br/><br/>
<ol>
<li>Search box for full text search</li><br/><br/>
<li>First Name found: Nathan John Smith. This contributor only has one name, which is classified as
their authoritative name. Any person with only one name associated with their record will
always have that name classified as authoritative.</li><br/><br/>
<li>Second name found: This person has two different names associated with their record, either of
which can be used as a contributor name.</li><br/><br/>
<li>Option to add another name for that contributor. By selecting this option you will be able to
enter an alternate name for the contributor.</li><br/><br/>
</ol>

By selecting Add next to any of the names it will place that name in the contributor side, as shown
below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_Publication_Contributors_Publishing_Name_Authority_Control_3.jpg"><br/><br/>

The following is a description of the numbered items: <br/><br/>
<ol>
<li>If multiple contributors are associated with this publication,, you list them in the order you
want.</li><br/><br/>
<li>The contribution type can be selected: Author, Thesis Advisor, Composer, etc.</li><br/><br/>
<li>If a mistake is made, the contributor can be removed from the list.</li><br/><br/>
</ol>
If you search and cannot find the contributor, you can add a new contributor by selecting the Add New
Person button. The following shows the screen to do this:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_Publication_Contributors_Publishing_Name_Authority_Control_4.jpg"><br/><br/>

The following is a description of the numbered items<br/><br/>
<ol>
<li>Basic Name information</li><br/><br/>
<li>Check box to indicate that the name belongs to the user who is submitting the publication.</li><br/><br/>
</ol>
Adding the name information will add the name to the list of contributors, and make the name
accessible in future searches when submitting new publications.<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Preview Publication">7.4  Preview Publication</a></h4>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Preview_Publication.jpg"><br/><br/>

The following shows the information stored in the publication for preview. The following is a description
of the numbered items:<br/><br/>
<ol>
<li>Edit Files – allows the user to go back and edit the files selected</li><br/><br/>
<li>Allows the user to edit the metadata entered</li><br/><br/>
<li>Allows the user to submit to a collection</li><br/><br/>
</ol>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Submitting to a collection">7.5  Submitting to a collection</a></h4>
Once you are satisfied with the publication you can select the Submit to Publication option. The
following shows the submit to collection screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Submitting_to_a_collection_1.jpg"><br/><br/>

Depending on permissions, you may or may not be able to submit to a collection. The following lists the
three different possibilities:<br/><br/>
<ol>
<li>You have direct submit privileges, and once you click Submit, your publication will go directly
into the collection.</li><br/><br/>
<li>You have review submit privileges. Your publication will be sent to a third party for review prior
to acceptance into the collection.</li><br/><br/>
<li>Can’t Submit – You don’t have rights to submit to the specified collection.</li><br/><br/>
</ol>
By selecting the link in the submit column (Direct/Review), the publication will be placed in the
collections to submit to. For example, selecting the Anthropology collection and the Biology collection
will result in the following screen. I have permission to make “direct submissions” to the Anthropology
collection, but my submission to Biology will be reviewed before being finalized. NOTE – you can submit
to multiple collections.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Submitting_to_a_collection_2.jpg"><br/><br/>

The following shows the publication added to the Anthropology and Biology collections (1). You can
remove a collection from the “Submit to” list by selecting Remove (2). To navigate into the collection to
submit to a sub-collection, click the collection name in the table on the left. The example below shows
navigating into the Anthropology collection:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Submitting_to_a_collection_3.jpg"><br/><br/>

To navigate back, use the path links (1). (See comment – I think maybe we need one more shot, showing
what you see when you’ve clicked Submit and the thing is really, finally done.)<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="My Publications Tab">7.6  My Publications Tab</a></h4>
If you decide to finish a publication later or want to submit to new collections, all publications you
create are stored in the My Publications area. Below is a screen shot of the My Publications Tab:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/My_Publications_Tab.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>
<ol>
<li>Current location (path)</li><br/><br/>
<li>Actions on checked collections and publications. Drop down list of things you can do with your
materials. You can think of collections as ways to organize your publications, similar to the way
files and folders work.</li><br/><br/>
<li>List of Collections and Publications at the current location</li><br/><br/>
<li>Button to create a new collection for organizing publications</li><br/><br/>
<li>Button to start a new publication</li><br/><br/>
<li>Drop down menu for actions on a single publication</li><br/><br/>
</ol><a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="Editing an existing publication and Versioning">7.6.1  Editing an existing publication and Versioning</a></h4>
To edit an existing publication select the name of the publication. This will return you to the publication
process. If the publication has already been published to an existing collection, a new version of the
publication will be created. A note like the following will alert you that a new version is being created:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Editing_an_existing_publication_and_Versioning.jpg"><br/><br/>

Selecting yes will create the new version. If the publication has not yet been published, a new version
will not be created and you will be taken to the normal create publication process.<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="Publication Properties">7.6.2  Publication Properties</a></h4>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Publication_Properties.jpg"><br/><br/>

The following is a list of the numbered items:<br/><br/>
<ol>
<li>Path to the publication</li><br/><br/>
<li>List of versions for the publication. If the publication has been published and accepted into the
institutional collections, it shows where the publication exists in the institutional collections.</li><br/><br/>
</ol><a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Personal Collections – organizing publications">7.6.3  Personal Collections – organizing publications</a></h4>
To create a collection to organize your publications, select the New Collection button. The following
screen will be shown when creating a new collection:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Personal_Collections_organizing_publications_1.jpg"><br/><br/>

When you enter the name, the collection will be added to the current location. The example below
shows the created Collection:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Personal_Collections_organizing_publications_2.jpg"><br/><br/>

You can also move publications and collections. The following shows me using the drop down menu to
move a selected collection and publication:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Personal_Collections_organizing_publications_3.jpg"><br/><br/>

The following screen should be displayed:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Personal_Collections_organizing_publications_4.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>
<ol>
<li>Cancel Button – to cancel the move</li><br/><br/>
<li>List of collections and publications being moved</li><br/><br/>
<li>Location the publications and collections are moving to</li><br/><br/>
<li>Move button</li><br/><br/>
<li>Current view of the Move to Location</li><br/><br/>
</ol>
Navigating into the General Publishing Collection by clicking its name shows the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Personal_Collections_organizing_publications_5.jpg"><br/><br/>

By selecting the move option, the publication and personal collection are moved into the General
Publishing collection. I am then taken to the display of the contents of the General Publishing collection,
as shown below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Personal_Collections_organizing_publications_6.jpg"><br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>


<h4><a id="Deleting publications and Personal Collections">7.6.3  Deleting publications and Personal Collections</a></h4>
You can delete a publication if it is no longer needed or you would like to clean up your personal
workspace area. Deleting publications from your personal workspace will not delete publications that
have been published to institutional collections.<br/><br/>
To delete a publication or collection you can select the drop down next to the publication, as shown
below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Deleting_publications_and_Personal_Collections_1.jpg"><br/><br/>

You will be asked to confirm the delete as shown below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Deleting_publications_and_Personal_Collections_2.jpg"><br/><br/>

Selecting yes will delete the selected publications and collections. In this case it is a single delete.<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr><hr>

<h3><a id="Workspace Searching">8.  Workspace Searching</a></h3>
After a while, you may find that you need to search for files within your workspace. To search within the
workspace, select the “Search My Workspace” tab. This will bring up a screen like the following:
By entering<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Workspace_Searching_1.jpg"><br/><br/>

By entering a word or phrase, the system will perform a full text search against files, folders and
publications. The example below shows a search for IR:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Workspace_Searching_2.jpg"><br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr><hr>


<h3><a id="Researcher Pages">9.  Researcher Pages</a></h3>
If you have been given “Researcher Page” abilities, you will be able to create a researcher page. A
researcher page is a great way to show off your work and give people access to your publications and
links to information on the web, such as links to your articles in online journals. It is also a handy way to
share specific files from your personal workspace with selected other users (say, collaborators at
another institution).<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Accessing Researcher Page Tools">9.1  Accessing Researcher Page Tools</a></h4>
To access the researcher page tools, select the Researcher Page menu as shown below see (1):<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Accessing_Researcher_Page_Tools_1.jpg"><br/><br/>

This should take you to a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Accessing_Researcher_Page_Tools_2.jpg"><br/><br/>

The following is a description of the fields<br/><br/>
<ol>
<li>Preview Researcher Page – Allows you to view what your researcher page will look like to the
world</li><br/><br/>
<li>Tabs to manage researcher page information</li><br/><br/>
<ul>
<li>Personal Information – basic personal information about yourself</li>
<li>Research – files and folders for your researcher page</li>
<li>Pictures – pictures for the researcher page</li>
<li>Links – links to other webpages</li><br/><br/>
</ul>
<li>Title – Professor, Dr., Provost, etc.</li><br/><br/>
<li>Departments you belong to</li><br/><br/>
<ul>
<li>Click Add Department to add another drop down selection if you belong to more than
one department</li>
<li>Add New Department allows you to add another department to the set of departments
in the drop down list</li><br/><br/>
</ul>
<li>Field – Field of study that you are in</li><br/><br/>
<ul>
<li>Select Add Field if you would like to have more than one field to appear on your
researcher page</li>
<li>Add New Field – allows you to add another field to the drop down list</li><br/><br/>
</ul>
<li>Campus Location –your office address</li><br/><br/>
<li>Phone Number – phone number where you can be reached</li><br/><br/>
<li>Email – address you would like shown on your researcher page</li><br/><br/>
<li>Fax</li><br/><br/>
<li>Save – select when you have finished entering user information</li><br/><br/>
<li>Research Interests</li><br/><br/>
<li>Teaching interests</li><br/><br/>
<li>Search Key words – to help people find you when they search researchers</li><br/><br/>
<li>Public/Private option – default is private. By selecting Public your researcher page is viewable to
everyone.</li><br/><br/>
</ol><a href="#Back to top" style="font-size=10px"> Back to top </a><hr>



<h4><a id="Adding Research to your Researcher Page">9.2  Adding Research to your Researcher Page</a></h4>
Selecting the Research tab should show the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_Research_to_your_Researcher_Page.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>
<ol>
<li>Actions to add materials to your researcher page</li><br/><br/>
<li>Path to your researcher page</li><br/><br/>
<li>Current list of materials displayed on your researcher page.</li><br/><br/>
</ol><a href="#Back to top" style="font-size=10px"> Back to top </a><hr>



<h4><a id="Adding a Folder">9.2.1  Adding a Folder</a></h4>
To add a folder, select the add New Folder button. This should bring up a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_a_Folder.jpg"><br/><br/>

Enter the folder name and description and select Submit. This should create a folder in the current
location.<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Adding a link">9.2.2  Adding a link</a></h4>
To add a link to the researcher page in the current location, select the New Link option. This should
bring up a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_a_link.jpg"><br/><br/>

Enter the Link Name and Link URL and select Submit.<br/><br/><a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Adding a File">9.2.3  Adding a File</a></h4>
Selecting Add File will allow you to add files from your workspace. Selecting Add File will take you to the
following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_a_File_in_researchers_page_1.jpg"><br/><br/>

The following is a list of numbered items:<br/><br/>
<ol>
<li>Current location in your personal files and folders</li><br/><br/>
<li>Current location (in the directory, or folder, structure) on your researcher page. When you add
files to your researcher page, they will be listed here.</li><br/><br/>
<li>Current list of files and folders in your personal workspace that can be added to your researcher
page.</li><br/><br/>
<li>Current list of information on your researcher page</li><br/><br/>
<li>Takes you back to your researcher files and folders</li><br/><br/>
</ol>

You can navigate to your files by selecting folders. For example if I click on the folder name “Repository
Information” the screen will look like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_a_File_in_researchers_page_2.jpg"><br/><br/>

Selecting the Add link will add the file to the current location in your Researcher Files and Folders. For
example, if I add IR_Plus_Admin_Manual.docx, it will look like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_a_File_in_researchers_page_3.jpg"><br/><br/>

You can alter the version by selecting the drop down associated with the filename. The default is the
most recent file version (1).<br/><br/>
If you wish to navigate to a new location on your researcher page, select a folder listed under
Researcher Files and Folders. For example, if I selected Nate’s Folder, the page would look like the
following (I do not yet have any files or links in Nate’s Folder):<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_a_File_in_researchers_page_4.jpg"><br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Adding a personal publication">9.2.4  Adding a personal publication</a></h4>
<a href="#Back to top" style="font-size=10px"> Back to top </a><br/><br/>
You can add a personal publication by selecting Add Publication. This will bring you to a screen like the
following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_a_personal_publication_1.jpg"><br/><br/>

The following is a list of the numbered items:<br/><br/>
<ol>
<li>Current location in your personal collections and publications. Use this display to locate the
publication you wish to add to your researcher page.</li><br/><br/>
<li>Current location (in the folder structure) on your researcher page. This is where you add
publications to your researcher page</li><br/><br/>
<li>Current list of publications and collections in your personal files and folders, which can be added
to your researcher page.</li><br/><br/>
<li>Current list of information (files, links) on your researcher page</li><br/><br/>
<li>Button to take you back to your researcher files and folders</li><br/><br/>
</ol>

You can navigate to your publications by selecting collections. For example if I select “General
Publishing Collection” the screen will look like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_a_personal_publication_2.jpg"><br/><br/>

Selecting the Add link will add the publication to the current location in your Researcher Files and
Folders. For example, if I add IR Plus Administration, it will look like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_a_personal_publication_3.jpg"><br/><br/>

You can alter which version is displayed using the drop down. The default is the most recent version
(1).<br/><br/>
If you wish to navigate to a different location on the researcher page, select a folder name in the list of
Researcher Files and Folders. For example, if I selected Nate’s Folder, the page would look like the
following (there are currently no files or links in Nate’s Folder):<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_a_personal_publication_4.jpg"><br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Removing Items from the researcher page">9.2.5  Removing Items from the researcher page</a></h4>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Removing_Items_from_the_researcher_page_1.jpg"><br/><br/>

Select the files you wish to remove and select the Delete button. <b>(NOTE – this does not delete files or
publications in your personal account, it only removes them from your researcher page).</b><br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Adding Pictures">9.2.6  Adding Pictures</a></h4>
To add pictures, select the Pictures Tab. This should bring up a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_Pictures_to_researchers_page_1.jpg"><br/><br/>

To add a picture, select Upload Picture. This will bring up a screen like the following.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_Pictures_to_researchers_page_2.jpg"><br/><br/>

The following is a list of the numbered items:<br/><br/>
<ol>
<li>File upload box – Allows you to browse to the file you wish to upload</li><br/><br/>
<li>Primary Picture – Tells the system this is the first picture you want shown when people visit your
site.</li><br/><br/>
</ol>
Selecting and uploading an image file will result in a screen like the following:<br/><br/>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_Pictures_to_researchers_page_3.jpg"><br/><br/>

You can upload as many pictures as you like.<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>

<h4><a id="Adding Links">9.2.7  Adding Links</a></h4>
If you would like to have special links located in your personal information rather than in your research
section – for example links to your CV, personal web site or department, you can add them using the
Links tab. The screen should look like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_Links_to_reseachers_page_1.jpg"><br/><br/>

To add a link, select the Add Link button. The screen shot below shows an example where two links
have been added:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Adding_Links_to_reseachers_page_2.jpg"><br/><br/>

Selecting the up and down arrows allows you to put the links in the order you want (1).<br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><br/>

<h4><a id="Previewing your researcher page">9.2.8  Previewing your researcher page</a></h4>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/Previewing_your_researcher_page.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>
<ol>
<li>If the researcher page is hidden it will notify you – only the owner can see the researcher page
if it is hidden.</li><br/><br/>
<li>Researcher Picture – this shows the picture you uploaded. If you haven’t uploaded a picture, a
silhouette is shown.</li><br/><br/>
<li>Researcher Information – all researcher information you have entered is shown.</li><br/><br/>
<li>Links you have entered.</li><br/><br/>
<li>Research box – list of publications, etc., that you have added to the page.</li><br/><br/>
<li>Link to toggle back to edit mode.</li><br/><br/>
</ol><a href="#Back to top" style="font-size=10px"> Back to top </a><hr><hr>



<h3><a id="Appendix A">10.  Appendix A</a></h3>
This shows some pictures from our test site where we have imported the majority of our records into
our current system.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/AppendixA_1.jpg"><br/><br/>

Browse/ Search – with thumb nails<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/AppendixA_2.jpg"><br/><br/>

Collection Page<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/AppendixA_3.jpg"><br/><br/>

Researcher Page<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/AppendixA_4.jpg"><br/><br/>

Faceted Search<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/AppendixA_5.jpg"><br/><br/>

Contributor Page<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/AppendixA_6.jpg"><br/><br/>

Publication Page<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/user/AppendixA_7.jpg"><br/><br/>
<a href="#Back to top" style="font-size=10px"> Back to top </a><hr>







               
               
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
