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
        <title>Administration Help (DRAFT)</title>
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
        <ur:js src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/${pageContext.request.contextPath}/page-resources/images/help-images/admin/page-resources/images/help-images/user/page-resources/yui/yahoo-dom-event/yahoo-dom-event.js"/>
        <ur:js src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/${pageContext.request.contextPath}/page-resources/images/help-images/admin/page-resources/images/help-images/user/page-resources/yui/connection/connection-min.js"/>
        <ur:js src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/${pageContext.request.contextPath}/page-resources/images/help-images/admin/page-resources/images/help-images/user/page-resources/yui/container/container_core-min.js"/>
        <ur:js src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/${pageContext.request.contextPath}/page-resources/images/help-images/admin/page-resources/images/help-images/user/page-resources/yui/menu/menu-min.js"/>
        
        <!-- Source File -->
        <ur:js src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/${pageContext.request.contextPath}/page-resources/images/help-images/admin/page-resources/images/help-images/user/page-resources/js/menu/main_menu.js"/>
        
        <!--  base path information -->
 	    <ur:js src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/${pageContext.request.contextPath}/page-resources/images/help-images/admin/page-resources/images/help-images/user/pages/js/base_path.js"/>
 	    <ur:js src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/${pageContext.request.contextPath}/page-resources/images/help-images/admin/page-resources/images/help-images/user/page-resources/js/util/ur_util.js"/>
   
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body regin of the page -->
            <div id="bd">
               <h3>Administration Help (DRAFT)</h3>     
               

<a id="Back to top"></a>
<h4><a href="#Document Description">1. Document Description</a><br/></h4>
<ul>
<li><h5><a href="#Project Web Site">1.1  Project Web Site</a><br/></h5></li>
<li><h5><a href="#Project Group">1.2  Project Group</a><br/></h5></li>
<li><h5><a href="#Suggestions">1.3  Suggestions</a><br/></h5></li>
</ul>

<h4><a href="#Home Page">2.  Home Page</a><br/></h4>
</ br>

<h4><a href="#Creating an account">3.  Creating an account</a><br/></h4>
<ul>
<li><h5><a href="#User account types">3.1  User account types</a><br/></h5></li>
</ul>



<h4><a href="#Logging in as administrator">4.  Logging in as administrator</a><br/></h4>
<ul>
<li><h5><a href="#Home account – Initial view">4.1  Home account – Initial view</a><br/></h5></li>
</ul>
</ br>

<h4><a href="#Administration Menu">5.  Administration Menu</a><br/></h4>
<ul>
<li><h5><a href="#Repository Administration Menu">5.1  Repository Administration Menu</a><br/></h5></li>
<ul>
<li><h5><a href="#Adding pictures to the picture module">5.1.1  Adding pictures to the picture module</a><br/></h5></li>
</ul>
<li><h5><a href="#User Administration">5.2  User Administration</a><br/></h4></li>
<ul>
<li><h5><a href="#Adding a new user">5.2.1  Adding a new user</a><br/></h5></li>
<li><h5><a href="#Editing a user">5.2.2  Editing a user</a><br/></h5></li>
<ul>
<li><h5><a href="#Managing Emails">5.2.2.1  Managing Emails</a><br/></h5></li>
<li><h5><a href="#Authoritative Name Tab">5.2.2.2  Authoritative Name Tab</a><br/></h5></li>
</ul>
</ul>
<li><h5><a href="#User Groups Administration Menu">5.3  User Groups Administration Menu</a><br/></h5></li>
<ul>
<li><h5><a href="#Creating a new User Group">5.3.1  Creating a new User Group</a><br/></h5></li>
<li><h5><a href="#Add a user to a group">5.3.2  Add a user to a group</a><br/></h5></li>
<li><h5><a href="#Adding a group administrator">5.3.3  Adding a group administrator</a><br/></h5></li>
</ul>
<li><h5><a href="#Institutional Collections Administration Menu">5.4  Institutional Collections Administration Menu</a><br/></h5></li>
<ul>
<li><h5><a href="#Adding a new collection">5.4.1  Adding a new collection</a><br/></h5></li>
<li><h5><a href="#Adding Images/Logos to a collection">5.4.2  Adding Images/Logos to a collection</a><br/></h5></li>
<li><h5><a href="#Collection Permissions and Groups">5.4.3  Collection Permissions and Groups</a><br/></h5></li>
<li><h5><a href="#Collection Subscribers">5.4.4  Collection Subscribers</a><br/></h5></li>
<li><h5><a href="#Statistics (Under Construction) – Currently can be viewed by visiting the collection">5.4.5  Statistics (Under Construction) – Currently can be viewed by visiting the collection</a><br/></h5></li>
<li><h5><a href="#Department Links">5.4.6  Department Links</a><br/></h5></li>
<li><h5><a href="#Moving Collections">5.4.7  Moving Collections</a><br/></h5></li>
</ul>
<li><h5><a href="#Metadata Lists">5.5  Metadata Lists</a><br/></h5></li>
<ul>
<li><h5><a href="#Content Types">5.5.1  Content Types</a><br/></h5></li>
</ul>
<li><h5><a href="#Person Name Authority">5.6  Person Name Authority</a><br/></h5></li>
<ul>
<li><h5><a href="#Creating a new Person Name Authority">5.6.1  Creating a new Person Name Authority/a><br/></h5></li>
</ul>
<li><h5><a href="#Roles">5.7  Roles</a><br/></h5></li>
<li><h5><a href="#Affiliations">5.8  Affiliations</a><br/></h5></li>
<ul>
<li><h5><a href="#Approving Affiliations">5.8.1  Approving Affiliations</a><br/></h5></li>
</ul>
<li><h5><a href="#Mime Types">5.9  Mime Types</a><br/></h5></li>
<ul>
<li><h5><a href="#Sub Types">5.8.1  Sub Types</a><br/></h5></li>
</ul>
<li><h5><a href="#Departments">5.10  Departments</a><br/></h5></li>
<li><h5><a href="#Fields">5.11  Fields</a><br/></h5></li>
<li><h5><a href="#Statistics – Ignore IP Addresses">5.12  Statistics – Ignore IP Addresses</a><br/></h5></li>
<ul>
<li><h5><a href="#Adding an ignore IP address">5.12.1  Adding an ignore IP address</a><br/></h5></li>
</ul>
<li><h5><a href="#Review Pending Publications">5.13  Review Pending Publications</a><br/></h5></li>
<ul>
<li><h5><a href="#Accepting/Rejecting a publication">5.13.1  Accepting/Rejecting a publication</a><br/></h5></li>
</ul>
<li><h5><a href="#Managing Publications">5.14  Managing Publications</a><br/></h5></li>
<ul>
<li><h5><a href="#Edit Publication">5.14.1  Edit Publication</a><br/></h5></li>
<li><h5><a href="#Withdraw/Reinstate Publication">5.14.2  Withdraw/Reinstate Publication</a><br/></h5></li>
<li><h5><a href="#Move Publication">5.14.3  Move Publication</a><br/></h5></li>
<li><h5><a href="#Add new publication version">5.14.4  Add new publication version</a><br/></h5></li>
<li><h5><a href="#Managing Permissions">5.14.4  Managing Permissions</a><br/></h5></li>
</ul>

<li><h5><a href="#Repository Licenses">5.15  Repository Licenses</a><br/></h5></li>
<li><h5><a href="#File Storage">5.16  File Storage</a><br/></h5></li>
</ul>
<hr width="1200px"><hr width="1200px">




	
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
</p>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px"><hr width="1200px">

<p>
<h3><a id="Home Page">2. Home Page</a></h3>
Welcome to irplus. Yourhomepage should look similar to the one below (not including the arrows and
numbers). This does not show a fully loaded ir plus system; for screen shots of our alpha system with
over 5,000 items please see appendix A.<br/><br/> 

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Home_Page.jpg"><br/>

<br/>
Here is a description of the numbered items:<br/><br/>
1.	<b>Site logo</b> – also a link that takes users to the home page<br/><br/>
2.	<b>Menu Bar</b> – currently has only a login option.  Upon logging in more options are available to the user<br/><br/>
3.	<b>Browse/Search Module</b> – Allows the user to search the ir plus system<br/><br/>
4.	<b>Institutional Collections Module </b>– current set of institutional collections<br/><br/>
5.	<b>Statistics Module</b> – Basic statistics for the entire system<br/><br/>
6.	 Description of what ir plus is<br/><br/>
7.	<b>Researchers Module</b>– Section that alternates researcher pages each time the page is visited <br/><br/>
8.	<b>Pictures Module </b>– pictures/logos that can be uploaded for display for the system – also alternated each time the page is visited<br/><br/>
9.	<b>News Module</b>– news that may be important to share with users – for example maintenance dates, or scheduled down time<br/><br/>
</p>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px"><hr width="1200px">

<p>
<h3><a id="Creating an account">3.  Creating an account</a></h3>
By selecting the Login option it will take you to the following screen<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Creating_an_account_1.jpg"><br/><br/>

1. <b>Login Type</b> – a choice of two types of authentication in the system. The two currently
supported by the system are <b>Local</b> (default) and <b>Net Id</b> for systems that have LDAP
capabilities. For now we will stick to the Local account because everyone will have a
Local account. If you ever have problems with your Net Id, you can resort to using your
Local account which should always work. If you forget it, your password can be changed
for your local account.<br/><br/>
2. <b>User Name</b> – user name entered when account was created. Currently this can be
anything.<br/><br/>
3. <b>Password</b> – local password or Net Id password based on Login Type selected.<br/><br/>
4. <b>Login / Reset</b> buttons for resetting the form.<br/><br/>
5. <b>Forgot password</b> to allow you to reset a password, and Create New Account to allow
you to create a new account. Selecting <b>Forgot Password</b> lets you enter your user
name and an email will be sent to your default address. Selecting <b>Create New Account</b>
allows you to create a new basic account in the system.<br/><br/>
Select the Create New account option. This will take you to the following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Creating_an_account_2.jpg"><br/>

<br/>
Enter your information. Create a password that you feel is secure. Select an affiliation and
department that makes the most sense for you. Some affiliations may need approval by an
administrator to confirm the affiliation is correct; however, this will not prevent you from
getting an account.<br/>
Once you are done entering the information, click “Create Account.” This should result in the
following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Creating_an_account_3.jpg"><br/><br/>

You should get at least one email like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Creating_an_account_4.jpg"><br/><br/>

Follow or copy and paste the link into your browser. This should take you to the login screen –
enter your user name and password and select the “Login” button. This should show you a
screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Creating_an_account_5.jpg"><br/><br/>

<b>Note: Each time you add a new email to your account, you must verify that email.</b><br/>
<br/>
You may also get another email like the following depending on the type of affiliation you
choose:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Creating_an_account_6.jpg"><br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">

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
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px"><hr width="1200px">




<p>
<h3><a id="Logging in as administrator">4.  Logging in as administrator</a></h3>
To log in select the login option in the Menu Bar<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Logging_in_as_administrator.jpg"><br/>

<br/>

This takes you to a page where you can log in.<br/>
Log into the system using the administration username and password set up when the system was installed. <br/> <br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">

<h4><a id="Home account – Initial view">4.1  Home account – Initial view</a></h4>
Once you login you should see a home account like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Home_account_Initial view.jpg"><br/><br/>

<br/>
Here is a description of the numbered items <b>(More about the workspace is in the user manual – this is just here to describe what will be seen on an initial login by the administrator)</b>:<br/>
1.	 Menu Bar – when logged in the menu bar should change.  For an administrator there should be the following on the menu bar:<br/><br/>
a.	Welcome [user name] – this drop down menu allows a user to manage their account information.<br/><br/>
b.	Workspace – drop down menu that allows a user to navigate to their workspace and researcher page (researcher page is only shown if user has researcher page capabilities)<br/><br/>
c.	Administration – drop down menu for administration.<br/><br/>
d.	Logout – Allows a user to log out.<br/><br/>
2.	Workspace tabs (described below from left to right)<br/><br/>
a.	My Files (currently selected) – Allows a user to edit and manage their files.  This can be thought of as a web based file system.<br/><br/>
b.	My Publications – Allows a user to manage their publications (publications they have added, or “published,” in the repository)<br/><br/>
c.	Search My Workspace – Allows a user to search their workspace<br/><br/>
d.	Shared File Inbox – Area where shared files are initially put when a file is shared with the user.<br/><br/>
3.	Current file system size - The total space the user is using in their account.  This <b>Does NOT</b> include files shared with them as this is totaled as part of the owning user’s file system account.  <br/><br/>
4.	Path – current location of the user in their web file system<br/><br/>
5.	Set of actions the user can perform on checked files and folders <br/><br/>
6.	Location where files and folders will be displayed<br/><br/>
7.	Set of actions that can be performed at any time in the <b>My Files tab.</b><br/><br/>

</p>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px"><hr width="1200px">


<p>
<h3><a id="Administration Menu">5.  Administration Menu</a></h3>
Below is the Administration menu bar.  To open the menu bar all you need to do is scroll over it.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Administration_Menu.jpg"><br/><br/>

The following is a list of the menu selections:
<ol>
<li>Affiliations – manage affiliations that can be selected and the rights that can be assigned<br/><br/></li>
<li>Approve User Affiliation – approve user affiliations<br/><br/></li>
<li>Content Types – manage the content type drop down menu (Book, article, …)<br/><br/></li>
<li>Contributor Types – manage contribution type drop down (Author, Advisor, …)<br/><br/></li>
<li>Copyright Statements- Contains the copyright statements<br/><br/></li>
<li>Departments – manage the department drop down list<br/><br/></li>
<li>Extent Types – manage extent types (page size, length of file, length of playing time)<br/><br/></li>
<li>Fields – manage the fields drop down list<br/><br/></li>
<li>File Storage - contains all the file storage information<br/><br/></li>
<li>Handle Name authorities - manage all the authorised names<br/><br/></li>
<li>Identifier Types - manages the identifiers drop down menu<br/><br/></li>
<li>Ignore IP Address – ip addresses to ignore when counting statistics<br/><br/></li>
<li>Institutional Collections – Area to manage institutional collections: create, delete, or move<br/><br/></li>
<li>Language Types – manage language type drop down menu<br/><br/></li>
<li>Mime Types – manage mime types in the system<br/><br/></li>
<li>News – manage news on the home page<br/><br/></li>
<li>Person name authority - manage person name authorization<br/><br/></li>
<li>Publishers – manage publisher drop down<br/><br/></li>
<li>Repository – Basic repository management and location to add pictures to the Pictures Module.<br/><br/></li>
<li>Repository Licenses –  manage licenses <br/><br/></li>
<li>Review Pending Items – area to approve pending items<br/><br/></li>
<li>Roles – manage roles in the system<br/><br/></li>
<li>Series –   manage series drop down<br/><br/></li>
<li>Sponsors – manage sponsor drop down<br/><br/></li>
<li>User Groups – manage user groups: create, edit, or delete<br/><br/></li>
<li>Users –   manage users<br/><br/></li>
</ol>


<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Repository Administration Menu">5.1  Repository Administration Menu</a></h4>
By selecting the Repository menu option:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Repository_Administration_Menu.jpg"><br/><br/>

Here is a description of the numbered items:<br/><br/>
1. Name given to the repository when the system was installed and set up<br/><br/>
2. Name of the institution where the repository is installed<br/><br/>
3. Description of the repository<br/><br/>
4. Save/Cancel for changes<br/><br/>
5. Re-Index Institutional Items – Will re-index the all the institutional items in the repository. If you
select this option, you may want to close the browser as it may take a large amount of time to
re-index the items in the repository.<br/><br/>
6. Allows the administrator to add a picture to the repository. This will be displayed on the home
page in the pictures module.<br/><br/>
7. Information about images that have been uploaded to the system for display in the pictures
module on the home page.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">

<h4><a id="Adding pictures to the Picture Module">5.1.1  Adding pictures to the Picture Module</a></h4>
Select the <b><u>Add Repository Picture Link</u></b> in the Repository Administration section. It should bring you to a
page like the following:<br/>
<br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_pictures_to_the_Picture_Module_1.jpg"><br/><br/>

Select the browse button. Navigate to a picture on your file system and then select the upload button
to upload the picture. Once the picture is uploaded, the system will return you to the Repository
Administration page. The file will be stored and thumb nailed. The picture will now show on your home
page in the <b>Pictures module</b>.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_pictures_to_the_Picture_Module_2.jpg"><br/><br/>
</p>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<p>
<h3><a id="User Administration">5.2  User Administration</a></h3>
By selecting the Users menu option:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/User_Administration_1.jpg"><br/><br/>

You should see the following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/User_Administration_2.jpg"><br/>

<ol>
<li> Tabs for dealing with user information</li><br/>
<ul>
<li> Users – (currently selected tab) offers a full browse of all the users</li><br/>
<li> Search – Full text search for users</li><br/>
</ul><br/>
<li> Buttons for common actions</li><br/>
<ul>
<li>New User – Button to create a new user</li><br/>
<li>Delete – Deletes the checked user or users</li><br/>
</ul><br/>
<li> Page Bar – Will show all pages for users if there are a large number of users</li><br/><br/>
<li>List of user information – the following is a list of the columns</li><br/>
<ul>
<li>Id – database id of the user</li><br/>
<li>User Name – user name given to the user – must be unique</li><br/>
<li>Last Name – last name of the user</li><br/>
<li>First Name – first name of the user</li><br/>
<li>Email – user’s email</li><br/>
<li>Change Password – allows admin to change the user’s password</li><br/>
<li>Login as this user – allows administrator to login as the specified user – this takes the
administrator into the specified user account.</li><br/>
</ul><br/>
<li>Column that can be sorted – An underlined column means it can be sorted. The arrow next to
the column when pointed down means descending order.</li><br/>
</ol>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Adding a new User">5.2.1  Adding a new User</a></h4>
Every user must have a local account. Users can also use LDAP to login but that is a secondary way to
login to the system. There are several reasons for this:<br/>
<ol>
<li>Not all users of the system will be LDAP users or have an LDAP account</li><br/>
<li>If there is a problem with the user’s LDAP account, they can still access their files using their
local account, and if they forget their password, a new one can be created for their local account
using their email.</li><br/>
<br/>
From the main user Administration screen select the new User Button<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_a_new_User.jpg"><br/>

This will bring up the screen to add a new user. The following is the description of the form shown
above:<br/><br/>
<ol>
 <li>First Name – first Name of the user<br/><br/></li>
 <li>Last Name – last name of the user<br/><br/></li>
 <li>User Name – user name for the user<br/><br/></li>
 <li>Password – local password for the user<br/><br/></li>
 <li>Password Check – check to make sure password is correct<br/><br/></li>
 <li>Net Id – LDAP net id for the user (so a user can use LDAP to login)<br/><br/></li>
 <li>Default Email – Default email that the user uses<br/><br/></li>
 <li>Phone Number – phone number for the user<br/><br/></li>
 <li>Affiliation – affiliation status of the user – the following are the default options:</li><br/>
<ul>
<li>Not Affiliated</li><br/>
<li> Staff</li><br/>
<li>Undergraduate Student</li><br/>
<li>Graduate Student</li><br/>
<li> Faculty</li><br/>
<li> Alumni</li><br/>
</ul><br/>
 <li>Department – these can be customized by each institution</li><br/><br/>
 <li>Account Locked – option to set a user’s account as locked</li><br/><br/>
 <li>Account Expired – option to set a user’s account as expired (reserved for future use and setting
expirations of accounts) (should NOT currently be used)</li><br/><br/>
 <li>Credentials Expired – option to set user’s credentials as expired (Should NOT currently be used)</li><br/><br/>
 <li>Roles</li><br/>
<ul>
<li>Admin – gives a user administration role / privileges – can manage all aspects of the
system.</li><br/>
<li>User – basic user rights to login and subscribe to collections</li><br/>
<li>Author – rights to upload documents and share with other users – this includes the
ability to invite people to work on documents that may not currently be part of the
system - see user manual for further explanation.</li><br/>
<li>Researcher – gives a user researcher abilities</li><br/>
<li>Collections Administrator – allows a user to administer specific collections without
giving them full Administrator (Admin) rights. This can be used for distributed collection
administration.</li><br/>
</ul><br/>
 <li>Check Box to email the user their password and account creating information.</li><br/><br/>
 <li>Submit and Cancel Buttons</li><br/><br/>
</ol>
Filling in the information and saving will store the user data and bring you to the User Information
screen.<br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Editing a user">5.2.2  Editing a user</a></h4>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Editing_a_user_1.jpg"><br/>

1. Select the hyper linked user name. This should bring up a screen like the following:<br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Editing_a_user_2.jpg"><br/>

The following is a description of the numbered items:<br/><br/>
<ol>
<li> Back to Users Button – Takes administrator back to the list of users.</li><br/><br/>
<li> File System Size – Current amount of space being used by the user.</li><br/><br/>
<li>Tabs for all user information</li>
<br/><ul>
<li>User Information (Currently Selected) – Basic User Information</li>
<li>Emails – Set of emails that the user may have associated with their account.</li>
<li>Authoritative Name – Manage a user’s authoritative name which links them to
published material in the repository</li>
</ul>
</ol><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">

<h4><a id="Managing Emails">5.2.2.1  Managing Emails</a></h4>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Managing_Emails.jpg"><br/>

This system allows users to store multiple emails. This allows sharing of files to multiple email addresses
that belong to a single user to be associated with a single account.<br/>
The default email is the email that all messages generated by the system will be sent to. This can also be
changed by the user.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Authoritative Name Tab">5.2.2.2  Authoritative Name Tab</a></h4>
The best way to explain this section is through describing the problem.<br/>
What if you have published material for a faculty member that is not currently part of the system? Later
the faculty member wants become a user of the IR system so a user account is created for them. How
do you now link the published material to that user account? The way this is done is through linking the
user to their authoritative name. The following is how to do this.<br/>
To add an authoritative name to a user, search for the authoritative name. In the example below I have
searched for Bach. This will return all Authoritative names that contain Bach.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Authoritative_Name_Tab.jpg"><br/><br/>

Description of numbered items:<br/><br/>
<ol>
<li>Full text search to find Authoritative name for user</li><br/>
<li>List of users found when searching for Bach. “Add” link allows administrator to add the
authoritative name as the user’s authoritative name. If the user already has an authoritative
name, the current name is removed.</li><br/>
<li>The current authoritative name if one exists. Selecting the Remove option will remove the
authoritative name from the user account.</li><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h3><a id="User Groups Administration Menu">5.3  User Groups Administration Menu</a></h3>
By selecting the User Groups menu option:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/User_Groups_Administration_Menu_1.jpg"><br/><br/>

You should see a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/User_Groups_Administration_Menu_2.jpg"><br/><br/>

1. Buttons to manage user groups<br/><br/>
<ul>
<li>New User Group – create a new user group</li>
<li>Delete – deletes checked user groups</li>
</ul><br/><br/>
2. List of user Groups<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Creating a new User Group">5.3.1  Creating a new User Group</a></h4>
To create a new user group, select the New User Group Button. This will bring up a screen like the
following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Creating_a_new_User_Group_1.jpg"><br/><br/>

Enter in the Name and (optionally) the description and select the submit button. This will create a user
group with the specified name.<br/>
Selecting submit will take you to the following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Creating_a_new_User_Group_2.jpg"><br/><br/>

The following is a description of the numbered items<br/><br/>
1. Tabs for managing group information<br/><br/>
<ul>
<li>User Group Tab (currently selected) – Area to edit name and description of a group</li>
<li>Group Members Tab – Area to add and remove members</li>
<li>Group Administrators Tab – Area to add and remove administrators of the group</li>
</ul>
<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Add a user to a group">5.3.2  Add a user to a group</a></h4>
To add a user to a group first select the name of the group to edit:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Add_a_user_to_a_group_1.jpg"><br/><br/>

Select the Group Members Tab (shown below):<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Add_a_user_to_a_group_2.jpg"><br/><br/>
<ol>
<li> Search box for a full text search against all users.</li><br/><br/>
<li> Current members of the group</li><br/><br/>
<li> Results of the search for Sarr– by selecting the Add User link will add the user to the group
members.</li><br/><br/>
</ol>
Enter in a name or part of a name in the search box. Locate the name to add and select Add User to add
the user to the group members.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">

<h4><a id="Adding a group administrator">5.3.3  Adding a group administrator</a></h4>
Administrators can allow particular users to administer a group. Parts of this functionality still need to
be completed.<br/>
See adding a user to group. The same functionality applies in Group Administrator tab.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h3><a id="Institutional Collections Administration Menu">5.4  Institutional Collections Administration Menu</a></h3>
By selecting this menu option:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Institutional_Collections_Administration_Menu_1.jpg"><br/><br/>

You should get a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Institutional_Collections_Administration_Menu_2.jpg"><br/><br/>

<ol>
<li> Buttons for actions that can be performed</li><br/>
<ul>
<li>New Collection – Creates a new collection</li><br/>
<li>Delete – allows selected collections to be deleted</li><br/>
<li>Move – allows collections to be moved</li><br/>
</ul><br/>
<li>Location you are in with respect to collections. The name of the system is the root location, in
this case it is ir_plus.</li><br/><br/>
<li>List of all collections. Currently there are no collections.</li>
</ol><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">



<h4><a id="Adding a new collection">5.4.1  Adding a new collection</a></h4>
To add a new collection<br/><br/>
1. Select the New Collection Button. You should see a pop-up box like the following:
<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_a_new_collection_1.jpg"><br/><br/>

2. Enter the collection name (here we have entered Anthropology)<br/><br/>
3. Select the submit button – this will create an institutional collection with the specified name and
bring you to the following screen:<br><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_a_new_collection_2.jpg"><br/><br/>

<ol>
<li>Current location – since the system allows for an unlimited number of collections within
collections, we show the current location. In this example, Anthropology is the current location.</li><br/><br/>

<li>Set of tabs for collection information</li><br/>
<ul>
<li>Collection Information (currently selected)</li><br/>
<li>Images – images for the collection. These will be shown in the collection images
module.</li><br/>
<li>Subscribers – the list of subscribers for the collection</li><br/>
<li>Statistics – statistics for the collection <b>(Currently under construction)</b></li><br/>
<li>Department Links – links that can be added to point users to more information</li><br/>
</ul><br/>
<li>Name of the collection</li><br/><br/>
<li>Description for the collection</li><br/><br/>
<li>Option to save changes</li><br/><br/>
</ol>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">



<h4><a id="Adding Images/Logos to a collection">5.4.2  Adding Images/Logos to a collection</a></h4>
Select the Images tab on the collection page.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_Images_Logos_to_a_collection_1.jpg"><br/><br/>

The following is a list of the numbered items:
<ol>
<li><b>Upload Picture Button</b> – allows you to upload a picture</li><br/><br/>
<li><b>Primary Picture Section </b>– this is the primary picture / logo. This will always be the picture shown
when a user visits the page. It will also be thumb nailed and placed next to the collection when
displayed on the home page, or, for subcollections, on the parent collection home page.</li><br/><br/>
<li><b>Pictures Section</b> – if there are other pictures, they will be placed in this section. Users will be
able to cycle through the pictures on the collection page.</li><br/><br/>
</ol>

To upload a picture:<br/><br/>
1. Select the Upload Picture Button. You should see a screen like the following<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_Images_Logos_to_a_collection_2.jpg"><br/><br/>

2. Select the browse button and navigate to the image file on your local file system<br/><br/>
3. (Optional) If it is the primary picture, check the Primary Picture option<br/><br/>
4. Select the submit option<br/><br/>
In the example below I have uploaded an image and have selected the primary picture option.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_Images_Logos_to_a_collection_3.jpg"><br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">



<h4><a id="Collection Permissions and Groups">5.4.3  Collection Permissions and Groups</a></h4>
To add/edit permissions on a collection, select the Permissions & Groups Tab<br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Collection_Permissions_and_Groups.jpg"><br/><br/>

The following is a list of the numbered items<br/><br/>
1. Change the “collections view” status. The default is publicly viewable. It can either be publicly
   viewable or private. By changing it to private, only groups with view permissions will be able to
   see the collection.<br/><br/>
2. Link to add groups to a collection<br/><br/>
3. List of groups who have permissions for the collection<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Changing Public/Private Status">5.4.3.1  Changing Public/Private Status</a></h4>
By selecting the Change Permissions button it should bring up a dialog box like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Changing_Public_Private_Status_1.jpg"><br/><br/>

If the collection is public the options are:<br/><br/>
	1. Set the collection as private – make only the specified collection private. Do not change the
       current status of items within the collection.<br/>
	2. Set the collection as well as the Publications and files in the collection private.<br/><br/>
If the collection is private (hidden) the options are:<br/><br/>
	1. Set the collection as public– make only the specified collection public.<br/>
	2. Set the collection as well as the Publications and files in the collection public.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Adding Groups">5.4.3.2  Adding Groups</a></h4>
Select the Add Groups to Collection link. This should bring you to a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_Groups_1.jpg"><br/><br/>

The following is a list of the numbered items.<br/><br/>
1. Done Link – this allows user to return to the collection after selecting adding group permissions<br/><br/>
2. List of groups that can be given permissions on the collection<br/><br/>
To add permissions for a particular group, select the Add Action. This should bring up a screen like the
following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_Groups_2.jpg"><br/><br/>

The following are the list of actions that can be assigned to the groups:<br/><br/>
<b>ADMINISTRATION</b> – allows the users in the group to administer the institutional collection<br/><br/>
<b>REVIEWER</b> – users in the group can review submitted publications<br/><br/>
<b>DIRECT_SUBMIT </b>– users in the group can submit directly to the collection. Submissions do not need to
be reviewed prior to acceptance into the collection.<br/><br/>
<b>REVIEW_SUBMIT </b>– users in the group can submit directly to the collection, but the submission will be
reviewed prior to acceptance into the collection<br/><br/>
<b>VIEW</b> – users in the group can view the collection. This is only needed when the collection is set to
private.<br/><br/>
Once you have selected the permissions for the group, click the save button. This will save the selected
permissions.<br/><br/>

You will be returned to the collection management area. Selecting the Permissions and Groups tab
again will bring up the following screen, showing the group that has been added and their rights:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_Groups_3.jpg"><br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Collection Subscribers">5.4.4  Collection Subscribers</a></h4>
To view collection subscribers, select the subscribers tab. You will see a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Collection_Subscribers_1.jpg"><br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">



<h4><a id="Statistics (Under Construction) – Currently can be viewed by visiting the collection">5.4.5  Statistics (Under Construction) – Currently can be viewed by visiting the collection</a></h4>
See user’s manual<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Department Links">5.4.6  Department Links</a></h4>
This allows users to add links to be shown for the collection – in the Links module. The following is a
screen shot of the Department Links tab:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Department_Links_1.jpg"><br/><br/>

The following is a list of the numbered items:<br/><br/>
1. Add Link – add a new link to be displayed in the Links module of the collection<br/><br/>
2. List of ordered links<br/><br/>
To add a link, select the Add Link button. You should see a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Department_Links_2.jpg"><br/><br/>

Enter in the link information and select submit. Below, I have created two links:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Department_Links_3.jpg"><br/><br/>

1. Order can be changed by selecting the down or up arrow on the respective links.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">



<h4><a id="Moving Collections">5.4.7  Moving Collections</a></h4>
Sometimes as the collections grow, there is a need to move collections. To move a collection, navigate
to the collection you wish to move. In the example below, we will be moving the collection
“Anthropology Faculty Articles” into the “Anthropology” collection. First we check the collection we
wish to move<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Moving_Collections_1.jpg"><br/><br/>

1. Checked collection to move<br/><br/>
2. Select the Move button<br/><br/>
Selecting the Move button takes you to the following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Moving_Collections_2.jpg"><br/><br/>

The following is a description of the numbered items<br/><br/>
1. Cancel Button – cancel the move<br/><br/>
2. List of collections and publications selected to move<br/><br/>
3. Current location – the current location by selecting the move button, it will place the collection
in the current location.<br/><br/>
4. Move Button – move the collections to specified location<br/><br/>
5. Navigation – allows user to navigate to location<br/><br/>
Since we want to move the Anthropology Faculty Articles into Anthropology, we will select the
Anthropology collection. This will bring up a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Moving_Collections_3.jpg"><br/><br/>

Now we can select the Move button.<br/><br/>
This will take you to the following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Moving_Collections_4.jpg"><br/><br/>

The collection is now moved underneath the Anthropology collection<br/><br/>
1. Now shows that the path to this collection is /ir_plus/Anthropology<br/><br/>
2. The virtual path shows that Anthropology is the parent collection.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Metadata Lists">5.5  Metadata Lists</a></h4>
<b>Metadata</b> is used to control the information entered into the system. The IR Plus system uses controlled
lists to allow administrators to exercise more control over metadata information. The current metadata
lists are described below.<br/><br/>
In this section the following control lists will be covered:<br/><br/>
<b>Content Types </b>– Allows the user to describe the type of content being published (working paper, audio
file, etc.)<br/><br/>
<b>Language Types </b>– Language of the publication<br/><br/>
<b>Identifier Types </b>– Identifiers for the publication (ISBN, Handle, MeSH, etc.)<br/><br/>
<b>Contributor Types </b>– Allows user to identify the nature of the contribution (Author, Thesis Advisor,
Composer, etc.)<br/><br/>
<b>Sponsors </b>- Entities who have sponsored the publication (NIH, NSF, etc.)<br/><br/>
<b>Publishers </b>– If published before, name of the publisher<br/><br/>
<b>Series </b>– Name of series (if applicable) publication is part of<br/><br/>
<b>Extent types </b>– Size and physical information (Number of Pages, Length in Time, Size of Page).<br/><br/>
<b>Only Content types will be covered here. Administrators can assume that all control lists will work
similarly to Content Types.</b><br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Content Types">5.5.1  Content Types</a></h4>
Content types allow users to describe the type of content they are placing in the system.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Content_Types_1.jpg"><br/><br/>

Selecting the Content Types menu should take you to the screen below.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Content_Types_2.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>
<ol>
<li> Buttons to create a new content type and delete selected content types.</li><br/><br/>
<li> List of current content types</li><br/>
<ul>
<li><b>Checkbox </b>– the one in the table header is a quick select – allows you to select all boxes
at once</li><br/>
<li><b>Id </b>– database id</li>
<br/>
<li><b>Name</b> – Name of the content type – select the header will order ascending / descending</li>
<br/>
<li><b>System Code </b>– this is for developers to do processing on a particular content type</li>
<br/>
<li><b>Description</b> – description of the content type</li>
</ul><br/>
<li>Checkbox for selecting individual content types to delete</li><br/><br/>
</ol>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">



<h4><a id="Adding a new content type">5.5.2  Adding a new content type</a></h4>
Select the new content type button.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_a_new_content_type_1.jpg"><br/><br/>

The content type name must be unique. If the content type name already exists, the new content type
will not be created and the user will be notified the content type already exists.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Deleting a content type">5.5.3  Deleting a content type</a></h4>
Check the content types you wish to delete and select the delete button. You will see a confirmation
like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Deleting_a_content_type_1.jpg"><br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Person Name Authority">5.6  Person Name Authority</a></h4>
In the system we have implemented a system to help control names and provide a form of Name
Authority Control. This allows all publications to point to one record that holds the person, their birth
date, and names they have published under. To go to the name authority control administration area,
go to Administration -> Person Name Authority menu item as shown below.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Person_Name_Authority_1.jpg"><br/><br/>

You should see the following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Person_Name_Authority_2.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>

<ol>
<li>Tabs for finding person name authorities</li><br/><br/>
<ul>
<li>Person Name Authority – browse list of all names in the system</li><br/>
<li>Search – allows for full text search of person names</li><br/><br/>
</ul>
<li>Buttons for managing person name authorities</li><br/><br/>
<ul>
<li>New Person – create a new person name authority in the system</li><br/>
<li>Delete a person name authority in the system</li><br/><br/>
</ul>
<li>List of all person name authorities in the system.</li><br/><br/>
</ol>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Creating a new Person Name Authority">5.6.1  Creating a new Person Name Authority</a></h4>
Person name authorities are created in several ways. One is in the administration section shown here.
The other is during the submission process which is shown in the user manual.<br/><br/>
Select the New Person button. You should see a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Creating_a_new_Person_Name_Authority_1.jpg"><br/><br/>

Enter in the person name information and click the submit button. This will create a new authoritative
name in the system. To view all names associated with the authoritative name or add other names that
should be linked to the authoritative name, select the hyperlinked last name.<br/><br/>
By selecting the hyperlinked last name in the PREVIOUS screen, you will see a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Creating_a_new_Person_Name_Authority_2.jpg"><br/><br/>

To add another person name to this authoritative name, select the button: New Person Name. You
should see a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Creating_a_new_Person_Name_Authority_3.jpg"><br/><br/>

Notice there are a few differences. There is no birth date or death date. These are associated with the
person, not each name. You can change the authoritative name by selecting the authoritative name
check box. Below shows a Person Name Authority with two different names:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Creating_a_new_Person_Name_Authority_4.jpg"><br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">

<h4><a id="Roles">5.7  Roles</a></h4>
Roles should never be changed except by a developer. The roles control all of the permissions within
the system.<br/><br/>
Select the Roles option in the Administration drop down.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Roles_1.jpg"><br/><br/>

The following is a list of default roles for the system:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Roles_2.jpg"><br/><br/>

The following is a description of the Roles<br/><br/>
<b>ROLE_ADMIN</b> – Administrator of the system.<br/><br/>
<b>ROLE_AUTHOR </b>– Has the ability to author and share documents with other users in the system. See
user manual for authoring <br><br/>
<b>ROLE_COLLABORATOR </b>– This is a user who has the ability to work on a file with another author but does
not have the ability to start a new work on their own.<br><br/>
<b>ROLE_COLLECTION_ADMIN (Under Construction) </b>– This is a user who can mange one or more
collections but cannot administer the entire repository system like the administrator.<br/><br/>
<b>ROLE_RESEARCHER </b>– A user who can create a researcher page within the system – See the User Manual
for more about researcher page capabilities.<br/><br/>
<b>ROLE_USER </b>– This is a basic user who can view information and subscribe to collections.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Affiliations">5.8  Affiliations</a></h4>
Affiliations are a way to identify how users are associated with your organization and the permissions
automatically assigned to users with specific affiliations. Users are allowed to select their affiliation
when they register with the system. Affiliations for a particular user can be changed in the user account.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Affiliations_1.jpg"><br/><br/>

Selecting the Affiliations menu item should take you to the following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Affiliations_2.jpg"><br/><br/>

The following is a description of the numbered items<br/><br/>
<ol>
<li>Buttons for managing affiliations</li><br/><br/>
<ul>
<li>New Affiliation – create a new type of affiliation</li>
<li>Delete – remove the selected type of affiliation</li><br/><br/>
</ul>
<li>Table listing all affiliations. The following is a description of the columns:</li><br/><br/>
<ul>
<li>Name – name of the affiliation</li>
<li>Author Permission – gives a user with this affiliation author permissions when
registering with the system</li>
<li>Researcher Permission – gives a user researcher permission when registering with the
system – see researcher page in User Manual for more about researcher page
capabilities.</li>
<li>Approval Required – The affiliation must be approved before it is given to the registering
user. For example, this will prevent a user from registering as Faculty when they are
not.</li><br/><br/>
</ul>
</ol>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">



<h4><a id="Approving Affiliations">5.8.1  Approving Affiliations</a></h4>
To approve an affiliation, go to the administration menu and select:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Approving_Affiliations_1.jpg"><br/><br/>

The following screen should be shown:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Approving_Affiliations_2.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>
<ol>
<li>Approve Button – Approves all checked affiliations</li><br/><br/>
<li>List of all affiliation requests</li><br/><br/>
<ul>
<li>Id – database id of the affiliation record</li>
<li>User Name – user name of the user requesting affiliation</li>
<li>First Name – first name entered by the user</li>
<li>Last Name – Last name entered by the user</li>
<li>Type of Affiliation the user selected.</li><br/><br/>
</ul>
<li>Affiliation drop down box. The administrator can change the user’s affiliation.</li><br/><br/>
</ol>
To approve an affiliation or affiliations check the affiliations you wish to approve. Then select the
approve button.<br/><br/>
If you wish to change the affiliation before approval, use the affiliation drop down menu and select the
affiliation you wish to give the user. If approval is required, the user does not receive all capabilities
until the administrator approves the affiliation. Once the affiliation has been approved any abilities not
assigned while awaiting approval are given to the user. The user will also receive an email stating their
affiliation has been approved.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Mime Types">5.9  Mime Types</a></h4>
Mime types allow the system to determine the content type of files. These can be updated and changed
over time. To go to mime types, select the Mime Types option in the Administration menu. It will take
you to the Top Media Types which is the top level description of a mime type.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Mime_Types_1.jpg"><br/><br/>

You should see a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Mime_Types_2.jpg"><br/><br/>

The following is a list of the numbered items:<br/><br/>
<ol>
<li>Buttons to manage content (mime) types</li><br/>
<ul>
<li>New Top Media Type – allows the user to create a new media type</li>
<li>Delete – Delete selected top media types</li><br/><br/>
</ul>

<li>Table of top media types</li><br/>
<ul>
<li>Id – database id</li>
<li>Name – Name of the top media type</li>
<li>Description – description of the media type</li>
<li>Edit – Edit the name / description of the top media type</li><br/><br/>
</ul>

<li>Link to view top media type sub types</li><br/><br/>
<li>Link to edit the name and description of the top media type</li><br/><br/>
</ol>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">



<h4><a id="Sub Types">5.9.1  Sub Types</a></h4>
Sub types can be viewed by selecting the name of the top media type. Here is a screen shot of the Sub
Types for the application top media type:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Sub_Types_1.jpg"><br/><br/>

1. Points to the pdf application sub type. By selecting this, we navigate to the file extensions for
the file type shown below:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Sub_Types_2.jpg"><br/><br/>

Here an administrator can manage the extensions for a given sub type, in this case pdf.<br/><br/>

</b>NOTE: Do not put in the period for extensions.</b><br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Departments">5.10  Departments</a></h4>
Departments allow users to describe which department they belong to. To view departments go to
Administration -> Departments.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Departments_1.jpg"><br/><br/>

This will take you to the following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Departments_2.jpg"><br/><br/>

The following describes the numbered items:<br/><br/>
<ol>
<li>Buttons to mange department information</li><br/>
<ul>
<li>New Department – allows administrator to create a new department</li>
<li>Delete – Allows administrator to delete selected departments</li><br/><br/>
</ul>

<li>Table of all departments</li><br/>
<ul>
<li>Id – database id for the department</li>
<li>Name – name of the department</li>
<li>Description – description of the department</li><br/>
</ul>
</ol>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Fields">5.11  Fields</a></h4>
Fields allows users to describe the subject area(s) they are working in. To view/edit fields select Fields in
the administration menu:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Fields_1.jpg"><br/><br/>

This will take you to the following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Fields_2.jpg"><br/><br/>

The following describes the numbered items:<br/><br/>
<ol>
<li>Buttons to manage fields</li><br/>
<ul>
<li>New Field – create a new field entry</li>
<li>Delete selected fields</li><br/><br/>
</ul>
<li>List of fields</li><br/>
<ul>
<li>Id – database id for the field</li>
<li>Name – Name of the field</li>
<li>Description – description of the field</li><br/>
</ul>
</ol>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">



<h4><a id="Statistics – Ignore IP Addresses">5.12  Statistics – Ignore IP Addresses</a></h4>
This allows the administrator to ignore IP addresses that should not be included in statistics. The system
stores IP addresses, so adding an IP address to the Ignore list will retroactively remove hits from the
count. This could confuse users as the counts may go down.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Statistics_Ignore_IPAddresses_1.jpg"><br/><br/>

This should bring you to a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Statistics_Ignore_IPAddresses_2.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>
<ol>
<li>Button to mange IP addresses to ignore</li><br/>
<ul>
<li>New Ignore IpAddress – add an IP address range to ignore</li>
<li>Delete – Remove selected IP addresses to ignore</li><br/><br/>
</ul>
<li>Table of all IP addresses to ignore</li><br/>
<ul>
<li>Id – database id</li>
<li>Name – descriptive name</li>
<li>From – Start address range</li>
<li>To – ending address range</li>
<li>Description – description of the range for example “Google,” “invasive crawler,” etc.</li><br/>
</ul>
</ol>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">



<h4><a id="Adding an ignore IP address">5.12.1  Adding an ignore IP address</a></h4>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_an_ignore_IPaddress_1.jpg"><br/><br/>

1. Select New Ignore IpAddress Button.<br/><br/>
2. Enter in a name and the “From” and “To” Range<br/><br/>
3. Select submit<br/><br/>
This will remove all counts from that range when reporting the hit counts.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Review Pending Publications">5.13  Review Pending Publications</a></h4>
IR Plus allows a reviewer to review pending publications and approve or reject the publication. To access
items to review go to Administration -> Review Pending Publications<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Review_Pending_Publications_1.jpg"><br/><br/>

This will bring up the following screen<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Review_Pending_Publications_2.jpg"><br/><br/>

The following is a list of the numbered items : <br/><br/>
<ol>
<li>List of publications the user can review</li><br/>
<ul>
<li>Id – database id of the record</li>
<li>Publication Name – name of the publication</li>
<li>Collection – collection the publication was submitted to</li><br/>
</ul>
</ol>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Accepting/Rejecting a publication">5.13.1  Accepting/Rejecting a publication</a></h4>
By selecting the name of the publication, the administrator can then accept or reject the publication.
The following is the screen a reviewer will see by selecting the publication name.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Accepting_Rejecting_a_publication_1.jpg"><br/><br/>

The following is a list of the numbered items:<br/><br/>
<ol>
<li>List of files</li><br/><br/>
<li>Option to edit the metadata</li><br/><br/>
<li>Back to Pending Items – takes the user back to the pending items list</li><br/><br/>
<li>Accept Button – Accept the publication</li><br/><br/>
<li>Reject Button – Reject the publication</li><br/><br/>
</ol>

Upon accepting the publication the user will be notified by email. If the publication is rejected, the
reviewer will be shown the following screen:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Accepting_Rejecting_a_publication_2.jpg"><br/><br/>

The reviewer will be asked to enter in the reject reason. This reason for rejection will be sent to the
submitter.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Managing Publications">5.14  Managing Publications</a></h4>
Administrators have the ability to manage publications. The following is the screen an administrator
would see by visiting a publication.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Managing_Publications_1.jpg"><br/><br/>

The following is a list of the numbered items:<br/><br/>
<ol>
<li>Current location in the repository</li><br/><br/>
<li>Title of the item</li><br/><br/>
<li>List of files that make up the item (if more than one file)</li><br/><br/>
<li>Number of downloads for the file</li><br/><br/>
<li>Metadata</li><br/><br/>
<li>Edit Publication – allows administrator to edit the publication without creating a new version</li><br/><br/>
<li>Withdraw Publication – allows the administrator to withdraw the publication</li><br/><br/>
<li>Move Publication – allows the administrator to move the publication (to another collection)</li><br/><br/>
<li>Manage Permissions – allows the administrator to change file and item level permissions</li><br/><br/>
<li>Add New Version – allows administrator to add a new version to the publication (Publications
are versioned)</li><br/><br/>
<li>Delete – delete the publication</li><br/><br/>
<li>Add to My Researcher Page – allows user to add publication to their own researcher page</li><br/><br/>
<li>All Versions table – shows all versions for a publication</li><br/>
</ol>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">



<h4><a id="Edit Publication">5.14.1  Edit Publication</a></h4>
The Edit Publication button allows the administrator to edit the publication without creating a new
version. See publication process in user manual.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Withdraw/Reinstate Publication">5.14.2  Withdraw/Reinstate Publication</a></h4>
By selecting the withdraw publication button, the administrator can withdraw a publication. The
following screen will be displayed:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Withdraw_Reinstate_Publication_1.jpg"><br/><br/>

The administrator must include a reason for the withdrawal and can opt to let the metadata be
displayed. The administrator can also withdraw all versions.<br/><br/>
Once the withdrawal is complete the screen will display the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Withdraw_Reinstate_Publication_2.jpg"><br/><br/>

The administrator can see all the data but a normal user will not. The withdrawal statements will always
be in red. Also notice the withdraw button has changed to a Reinstate Publication button to allow the
publication to be reinstated at a later date.
<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Move Publication">5.14.3  Move Publication</a></h4>
If an administrator determines a publication is in the wrong collection, the publication can be moved.<br/><br/>
The following demonstrates how to move a publication after selecting the Move Publication Button.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Move_Publication_1.jpg"><br/><br/>

The following is a description of the numbered items:<br/><br/>

<ol>
<li>Cancel Button – cancel the move</li><br/><br/>
<li>Current item being moved</li><br/><br/>
<li>Current location and navigation – Selecting one of the links will move you to the parent
collection. Currently we are in the bottom-most collection</li><br/><br/>
<li>Move Button – when you have navigated to the correct location, select this button</li><br/><br/>

If we select the Anthropology Collection (#3) the following screen is displayed:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Move_Publication_2.jpg"><br/><br/>

<li>This shows the sub collections within the anthropology department.</li><br/><br/>
</ol>
By selecting the move button, the thesis will be moved into the Anthropology collection.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Add new publication version">5.14.4  Add new publication version</a></h4>
By selecting the Add New Version button we can create a new version of the publication.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Add_new_publication_version_1.jpg"><br/><br/>

This displays the current personal collections and the options to add any personal publication as the
new version.<br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Managing Permissions">5.14.5  Managing Permissions</a></h4>
The permissions can be managed for a publication by selecting the Manage Permissions button. It will
display a screen like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Managing_Permissions_1.jpg"><br/><br/>

The following is a description of the numbered items :<br/><br/>
<ol>
<li>Back to Publication – takes user back to the publication</li><br/><br/>
<li>Tabs for managing permissions</li><br/>
<ul>
<li>Publication Permissions – permissions related to the publication</li>
<li>Publication File Permissions – permissions related directly to the files in the publication</li><br/><br/>
<li></li><br/><br/>
</ul>
<li>Publication viewable – if this is set to Yes everyone can see the publication – permissions will
not be checked</li><br/><br/>
<li>Give permissions for this item to specific Groups</li><br/><br/>
<li>List of groups with permissions</li><br/>
</ol>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Adding permissions to a publication">5.14.5.1  Adding permissions to a publication</a></h4>
On the Publication Permissions tab, select Add Groups To Publication button.<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_permissions_to_a_publication_1.jpg"><br/><br/>

This displays all groups available to give permissions to. Select the add button and you will see a screen
like the following:<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Adding_permissions_to_a_publication_2.jpg"><br/><br/>

The following is a description of the permissions for the publications<br/><br/>
<ol>
<li>ITEM_FILE_EDIT – user group can add, edit and remove files from the item</li><br/><br/>
<li>ITEM_METADATA_EDIT – the user can edit the item metadata</li><br/><br/>
<li>ITEM_METADATA_READ – the user can view the item metadata</li><br/>
</ol>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px">


<h4><a id="Add permissions to a publication file">5.14.5.1  Add permissions to a publication file</a></h4>
This tab shows all of the files, although in this case there is only one file. This only allows for view
permissions. So if a particular file cannot be viewed by everyone, specific groups can be given view
permissions. If the file is viewable by all users this will allow everyone to view the file. To make the file
private, first change this option then add the group or groups that can view the file by selecting “Add
new user group.”<br/><br/>

<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Add_permissions_to_a_publication_file_1.jpg"><br/><br/>
<a href="#Back to top" style="text-align:right ; font-size=10px"> Back to top </a>
<hr width="1200px"><hr width="1200px">





















<h1><b>TO DO</b></h1>

<br/><br/><br/><br/><br/>

<h3><a id="Repository Licenses">5.15 Repository Licenses</a></h3>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Repository_Licenses_1.jpg"><br/><br/>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Repository_Licenses_2.jpg"><br/><br/>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Repository_Licenses_3.jpg"><br/><br/>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Repository_Licenses_4.jpg"><br/><br/>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Repository_Licenses_5.jpg"><br/><br/>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/Repository_Licenses_6.jpg"><br/><br/>


<h3><a id="File Storage">5.16 File Storage</a></h3>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/File_Storage_1.jpg"><br/><br/>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/File_Storage_2.jpg"><br/><br/>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/File_Storage_3.jpg"><br/><br/>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/File_Storage_4.jpg"><br/><br/>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/File_Storage_5.jpg"><br/><br/>
<img src="${pageContext.request.contextPath}/page-resources/images/help-images/admin/File_Storage_6.jpg"><br/><br/>

</body>
</html>



















                          
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>