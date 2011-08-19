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

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.PlaceOfPublication;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.Series;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.PersonName;

/**
 * Data access for institutional item index processing record.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalItemIndexProcessingRecordDAO 
    extends CrudDAO<InstitutionalItemIndexProcessingRecord>, CountableDAO{
	
	/**
	 * Get all institutional item indexing processing record ordered by updated date descending.
	 * 
	 * @param rowStart - start position
	 * @param maxResults - maximum number of results to get.
	 *  
	 * @return all institutional item index processing records or an empty list if none found
	 */
	public List<InstitutionalItemIndexProcessingRecord> getAllOrderByItemIdUpdatedDate(int rowStart,
			int maxResults);
	
	/**
	 * Get all institutional item index processing records for a given index processing type ordered
	 * by updated date descending
	 * 
	 * @param processingTypeId - id of the processing type
	 * 
	 * @return list of records found
	 */
	public List<InstitutionalItemIndexProcessingRecord> getAllByProcessingTypeUpdatedDate(Long processingTypeId);

	
	/**
	 * Get the processing record by item id and processing type.
	 * 
	 * @param itemId - id of the item
	 * @param processingType - processing type.
	 * 
	 * @return - the index processing record if found otherwise null
	 */
	public InstitutionalItemIndexProcessingRecord get(Long itemId, IndexProcessingType processingType);

	
	/**
	 * Insert all items within the specified collection to be processed.
	 * 
	 * @param - institutional collection
	 * @param processingType - processing type.
	 * 
	 * @return - number of records created.
	 */
	public Long insertAllItemsForCollection(InstitutionalCollection institutionalCollection, IndexProcessingType processingType);
	
	/**
	 * Insert all items for the repository
	 * 
	 * @param processingType - processing type.
	 * 
	 * @return - number of records created for processing
	 */
	public Long insertAllItemsForRepository(IndexProcessingType processingType);
	
	/**
	 * Set all institutional items to be re-indexed based on the content type id.  This
	 * should include items that use the content type as a secondary content type.
	 * 
	 * @param contentType - the content type
	 * @param processingType - type of processing to complete
	 * @return number of records created for processing
	 */
	public Long insertAllItemsForContentType(ContentType contentType, IndexProcessingType processingType);
	
	/**
	 * Sets all institutional items with a current item that has the specified contributor type associated with it 
	 * to be re-indexed based on the contributor type.
	 * 
	 * @param contributorType - the contributor type to be updated
	 * @param processingType - type of indexing to be performed
	 * 
	 * @return - number of items set for re-indexing
	 */
	public Long insertAllItemsForContributorType(ContributorType contributorType, IndexProcessingType processingType);

	/**
	 * Sets all institutional items with a current item that has the specified identifier type associated with it 
	 * to be re-indexed based on the identifier type.
	 * 
	 * @param identifierType - the identifier type to be updated
	 * @param processingType - type of indexing to be performed
	 * 
	 * @return - number of items set for re-indexing
	 */
	public Long insertAllItemsForIdentifierType(IdentifierType identifierType, IndexProcessingType processingType);
	
	/**
	 * Sets all institutional items with a current item that has the specified language type associated with it 
	 * to be re-indexed based on the language type.
	 * 
	 * @param languageType - the language type to be updated
	 * @param processingType - type of indexing to be performed
	 * 
	 * @return - number of items set for re-indexing
	 */
	public Long insertAllItemsForLanguageType(LanguageType languageType, IndexProcessingType processingType);
	
	/**
	 * Sets all institutional items with a current item that has the specified place of publicaiton associated with it 
	 * to be re-indexed based on the place of publication.
	 * 
	 * @param place of publication - the place of publication to be updated
	 * @param processingType - type of indexing to be performed
	 * 
	 * @return - number of items set for re-indexing
	 */
	public Long insertAllItemsForPlaceOfPublication(PlaceOfPublication placeOfPublicaiton, IndexProcessingType processingType);
	
	
	/**
	 * Sets all institutional items with a current item that has the specified person name associated with it 
	 * to be re-indexed based on the person name.
	 * 
	 * @param personName - the person name to be updated
	 * @param processingType - type of indexing to be performed
	 * 
	 * @return - number of items set for re-indexing
	 */
	public Long insertAllItemsForPersonName(PersonName personName, IndexProcessingType processingType);
	
	/**
	 * Sets all institutional items with a current item that has the specified publisher associated with it 
	 * to be re-indexed based on the publisher.
	 * 
	 * @param publisher - the publisher to be updated
	 * @param processingType - type of indexing to be performed
	 * 
	 * @return - number of items set for re-indexing
	 */
	public Long insertAllItemsForPublisher(Publisher publisher, IndexProcessingType processingType);
	
	/**
	 * Sets all institutional items with a current item that has the specified series associated with it 
	 * to be re-indexed based on the series.
	 * 
	 * @param series - the series to be updated
	 * @param processingType - type of indexing to be performed
	 * 
	 * @return - number of items set for re-indexing
	 */
	public Long insertAllItemsForSeries(Series series, IndexProcessingType processingType);
	
	/**
	 * Sets all institutional items with a current item that has the specified sponsor associated with it 
	 * to be re-indexed based on the sponsor.
	 * 
	 * @param sponsor - the sponsor to be updated
	 * @param processingType - type of indexing to be performed
	 * 
	 * @return - number of items set for re-indexing
	 */
	public Long insertAllItemsForSponsor(Sponsor sponsor, IndexProcessingType processingType);

}
