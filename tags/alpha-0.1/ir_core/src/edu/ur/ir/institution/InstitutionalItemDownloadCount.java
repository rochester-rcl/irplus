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

package edu.ur.ir.institution;

/**
 * Represents institutional item and the file download count for it
 * 
 * @author Sharmila Ranganathan
 *
 */
public class InstitutionalItemDownloadCount {
	
	private long count;
	
	private InstitutionalItem institutionalItem;

	public InstitutionalItemDownloadCount(long count, InstitutionalItem institutionalItem) {
		this.count = count;
		this.institutionalItem = institutionalItem;
	}
	
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public InstitutionalItem getInstitutionalItem() {
		return institutionalItem;
	}

	public void setInstitutionalItem(InstitutionalItem institutionalItem) {
		this.institutionalItem = institutionalItem;
	}
}
