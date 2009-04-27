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


package edu.ur.ir.web.action.researcher;

import java.io.File;
import java.util.Collection;

import org.apache.log4j.Logger;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.Field;
import edu.ur.ir.researcher.FieldService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherIndexService;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.user.Department;
import edu.ur.ir.user.DepartmentService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Edit the researcher page.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class EditResearcher extends ActionSupport implements UserIdAware, Preparable {

	/**  Logger. */
	private static final Logger log = Logger.getLogger(EditResearcher.class);

	/**  Generated version id */
	private static final long serialVersionUID = 8163031274176961387L;	
	
	/** Researcher */
	private Researcher researcher;
	
	/** Id of the researcher */
	private Long researcherId;

	/** Researcher service */
	private ResearcherService researcherService;
	
	/** Department service */
	private DepartmentService departmentService;

	/** Field service */
	private FieldService fieldService;

	/** Set of departments for viewing the departments */
	private Collection<Department> departments;
	
	/** Set of fields*/
	private Collection<Field> fields;

	/** User service */
	private UserService userService;

	/** User id */
    private Long userId;
    
    /** department id  */
    private Long departmentId;
    
    /** field id  */
    private Long fieldId;
    
    /** Indicates whether the researcher page is public / hidden  */
    private boolean isPublic;
    
	/** the id of the parent folder  */
	private Long parentFolderId = 0L;

	private boolean showFoldersTab;
	
	/** Researcher folders as JSON object */
	private JSONObject researcherJSONObject;
	
	/** Researcher Index Service */
	private ResearcherIndexService researcherIndexService;

	/** Researcher Service */
	private RepositoryService repositoryService;
	
	/** Number of fields for researcher */
	private int researcherFieldsCount;
	
	/** Id of selected fields */
	private Long[] fieldIds;

	/** Number of departments for researcher */
	private int researcherDepartmentsCount;
	
	/** Id of selected departments */
	private Long[] departmentIds;

	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Update personal researcher information 
	 * 
	 */
	public String updatePersonalInformation() throws NoIndexFoundException
	{
		log.debug("update called researcher Id = " + researcherId );
		IrUser user = researcher.getUser();
		
		if(user== null || !user.hasRole(IrRole.RESEARCHER_ROLE) || 
				!researcher.getUser().getId().equals(userId))
		{
			return "accessDenied";
		}
		
		researcher.removeAllFields();
		researcher.removeAllDepartments();
		researcherService.saveResearcher(researcher);
		
		// Adds the selected fields
		if (fieldIds != null) {
			for (int i =0; i < fieldIds.length; i++ ) {
				if (fieldIds[i] != 0) {
					Field f = fieldService.getField(fieldIds[i], false);
					researcher.addField(f);
				}
			}
		}
		
		// Adds the selected departments
		if (departmentIds != null) {
			for (int i =0; i < departmentIds.length; i++ ) {
				if (departmentIds[i] != 0) {
					Department d = departmentService.getDepartment(departmentIds[i], false);
					researcher.addDepartment(d);
				}
			}
		}
		
		researcherService.saveResearcher(researcher);
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
				false);
		researcherIndexService.updateIndex(researcher, 
				new File(repository.getResearcherIndexFolder()) );

        return SUCCESS;
	}

	/**
	 * Get pictures of researcher
	 * 
	 * @return
	 */
	public String getPictures()
	{
		
		log.debug("get pictures called");
		if (userId != null) {
			IrUser user = userService.getUser(userId, false);
			if( !user.hasRole(IrRole.RESEARCHER_ROLE))
			{
				return "accessDenied";
			}
			researcher = user.getResearcher();
		}
		else
		{
			return "accessDenied";
		}
		return SUCCESS;
	}
	
	/**
	 * View the researcher.
	 * 
	 * @return
	 */ 
	public String view()
	{
		log.debug("view called for researcher ");
		
		if (userId != null)
		{
			IrUser user = userService.getUser(userId, false);
			researcher = user.getResearcher();
			
			log.debug( "user " + user + " has role " + user.hasRole(IrRole.RESEARCHER_ROLE) );
			if( !user.hasRole(IrRole.RESEARCHER_ROLE))
			{
				return "accessDenied";
			}
		}
		else
		{
			return "accessDenied";
		}
		
		
		if (researcher != null) 
		{
			researcherJSONObject = researcher.toJSONObject();
		}
		
		getDepartmentsInformation();
		getFieldsInformation();
		
		
		return "view";
	}

	/**
	 * Sets the researcher page public / hidden
	 * 
	 * @return
	 */
	public String setResearcherPagePermission() {
		
		log.debug("isPublic::"+isPublic);
		if (userId != null) {
			IrUser user = userService.getUser(userId, false);
			researcher = user.getResearcher();
			
			log.debug( "user " + user + " has role " + user.hasRole(IrRole.RESEARCHER_ROLE) );
			if( !user.hasRole(IrRole.RESEARCHER_ROLE))
			{
				return "accessDenied";
			}
		}
		else
		{
			return "accessDenied";
		}
		researcher.setPublic(isPublic);
		
		researcherService.saveResearcher(researcher);
		
		return SUCCESS;
		
	}
	
	/**
	 * Get all departments
	 * 
	 * @return
	 */
	public String getAllDepartments() {
		
		departments = departmentService.getAllDepartmentsNameOrder();
		return SUCCESS;
	}

	/**
	 * Get departments information
	 * 
	 * @return
	 */
	public String getDepartmentsInformation() {
		if (userId != null) {
			IrUser user = userService.getUser(userId, false);
			researcher = user.getResearcher();
			
			log.debug( "user " + user + " has role " + user.hasRole(IrRole.RESEARCHER_ROLE) );
			if( !user.hasRole(IrRole.RESEARCHER_ROLE))
			{
				return "accessDenied";
			}
		}
		else
		{
			return "accessDenied";
		}
		researcherDepartmentsCount = researcher.getDepartments().size();
		departments= departmentService.getAllDepartmentsNameOrder();
		return SUCCESS;
	}
	
	/**
	 * Get fields information
	 * 
	 * @return
	 */
	public String getFieldsInformation() {
		if (userId != null) {
			IrUser user = userService.getUser(userId, false);
			researcher = user.getResearcher();
			
			log.debug( "user " + user + " has role " + user.hasRole(IrRole.RESEARCHER_ROLE) );
			if( !user.hasRole(IrRole.RESEARCHER_ROLE))
			{
				return "accessDenied";
			}
		}
		else
		{
			return "accessDenied";
		}
		researcherFieldsCount = researcher.getFields().size();
		fields = fieldService.getAllFieldsNameOrder();
		return SUCCESS;
	}
	
	/**
	 * Get all fields
	 * 
	 * @return
	 */
	public String getAllFields() {
		fields = fieldService.getAllFieldsNameOrder();
		return SUCCESS;
	}
	
	/**
	 * The number of Researcher pictures.
	 * 
	 * @return
	 */
	public int getNumberOfResearcherPictures() {
		if (userId != null) {
			researcher = userService.getUser(userId, false).getResearcher();
		}
		else
		{
			return 0;
		}
		return researcher.getPictures().size();
		
	}

	public Researcher getResearcher() {
		return researcher;
	}


	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}


	public Long getResearcherId() {
		return researcherId;
	}


	public void setResearcherId(Long researcherId) {
		this.researcherId = researcherId;
	}


	public ResearcherService getResearcherService() {
		return researcherService;
	}


	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public DepartmentService getDepartmentService() {
		return departmentService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public Collection<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(Collection<Department> departments) {
		this.departments = departments;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public FieldService getFieldService() {
		return fieldService;
	}

	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	public Collection<Field> getFields() {
		return fields;
	}

	public void setFields(Collection<Field> fields) {
		this.fields = fields;
	}

	public Long getFieldId() {
		return fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	
	public Long getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	public boolean isShowFoldersTab() {
		return showFoldersTab;
	}

	public void setShowFoldersTab(boolean showFoldersTab) {
		this.showFoldersTab = showFoldersTab;
	}
	
	/**
	 * Get JSON object for researcher
	 *  
	 * @return
	 */
	public JSONObject getResearcherJSONObject() {
		return researcherJSONObject;
	}

	public ResearcherIndexService getResearcherIndexService() {
		return researcherIndexService;
	}

	public void setResearcherIndexService(
			ResearcherIndexService researcherIndexService) {
		this.researcherIndexService = researcherIndexService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public int getResearcherFieldsCount() {
		return researcherFieldsCount;
	}

	public Long[] getFieldIds() {
		return fieldIds;
	}

	public void setFieldIds(Long[] fieldIds) {
		this.fieldIds = fieldIds;
	}

	public int getResearcherDepartmentsCount() {
		return researcherDepartmentsCount;
	}

	public void setDepartmentIds(Long[] departmentIds) {
		this.departmentIds = departmentIds;
	}

	
	public void prepare() throws Exception {
		if( researcherId != null )
		{
			researcher = researcherService.getResearcher(researcherId, false);
		}
		
	}
}
