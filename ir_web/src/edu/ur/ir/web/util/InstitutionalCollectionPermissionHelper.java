package edu.ur.ir.web.util;

import java.util.List;

import edu.ur.ir.institution.InstitutionalCollectionSecurityService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.user.IrUser;



public class InstitutionalCollectionPermissionHelper {

	private InstitutionalItemService institutionalItemService;
	
    private InstitutionalCollectionSecurityService institutionalCollectionSecurityService;

	
	public boolean isInstitutionalCollectionAdmin(IrUser user, Long genericItemId){
		List<InstitutionalItem> items = institutionalItemService.getInstitutionalItemsByGenericItemId(genericItemId);
		for(InstitutionalItem item: items){
			Long count = institutionalCollectionSecurityService.hasPermission(item.getInstitutionalCollection(),
					user, InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION);
			if( count > 0){
				return true;
			}
		
		}
		return false;
		
	}
	
	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}
	
	public void setInstitutionalCollectionSecurityService(
			InstitutionalCollectionSecurityService institutionalCollectionSecurityService) {
		this.institutionalCollectionSecurityService = institutionalCollectionSecurityService;
	}

}
