/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.oai.format;

import java.util.List;

import edu.ur.ir.institution.InstitutionalItemVersion;

/**
 * @author Nathan S
 *
 */
public class BasicOaiSimpleDcVersion_1_1 implements OaiSimpleDcVersion_1_1{
	
	/**
	 * Institutional Item version to extract OAI information from
	 */
	private InstitutionalItemVersion institutionalItemVersion;
	
	/**
	 * Default constructor.
	 * 
	 * @param institutionalItemVersion
	 */
	public BasicOaiSimpleDcVersion_1_1(InstitutionalItemVersion institutionalItemVersion)
	{
		setInstitutionalItemVersion(institutionalItemVersion);
	}



	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getContributors()
	 */
	@Override
	public List<String> getContributors() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getCoverages()
	 */
	@Override
	public List<String> getCoverages() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getCreators()
	 */
	@Override
	public List<String> getCreators() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getDates()
	 */
	@Override
	public List<String> getDates() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getDescriptions()
	 */
	@Override
	public List<String> getDescriptions() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getFormats()
	 */
	@Override
	public List<String> getFormats() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getIdentifiers()
	 */
	@Override
	public List<String> getIdentifiers() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getLanguages()
	 */
	@Override
	public List<String> getLanguages() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getPublishers()
	 */
	@Override
	public List<String> getPublishers() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getRelations()
	 */
	@Override
	public List<String> getRelations() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getRights()
	 */
	@Override
	public List<String> getRights() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getSources()
	 */
	@Override
	public List<String> getSources() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getSubjects()
	 */
	@Override
	public List<String> getSubjects() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getTitles()
	 */
	@Override
	public List<String> getTitles() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getTypes()
	 */
	@Override
	public List<String> getTypes() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public InstitutionalItemVersion getInstitutionalItemVersion() {
		return institutionalItemVersion;
	}

	protected void setInstitutionalItemVersion(
			InstitutionalItemVersion institutionalItemVersion) {
		this.institutionalItemVersion = institutionalItemVersion;
	}

}
