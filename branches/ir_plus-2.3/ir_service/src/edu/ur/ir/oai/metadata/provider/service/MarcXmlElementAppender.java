package edu.ur.ir.oai.metadata.provider.service;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.w3c.dom.Element;

import edu.ur.ir.oai.OaiUtil;

public class MarcXmlElementAppender {
	
	protected static final String CONTROL_FIELD = "controlfield";

	protected static final String DATA_FIELD = "datafield";

	protected static final String SUBFIELD = "subfield";

	protected static final String COLLECTION = "collection";

	protected static final String RECORD = "record";

	protected static final String LEADER = "leader";
	
    /** NS URI */
    public static final String MARCXML_NS_URI = "http://www.loc.gov/MARC21/slim";
 

	@SuppressWarnings("unchecked")
	public void addToDocument(Record record, Document doc, Element parent)
	{
		// create the recordElement element of the record 
		 Element recordElement = doc.createElement(RECORD);
		 parent.appendChild(recordElement);
		 recordElement.setAttribute("xmlns", MARCXML_NS_URI);
		 
		 Element leader = doc.createElement(LEADER);
		 Text data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(record.getLeader().toString()));
		 leader.appendChild(data);
		 recordElement.appendChild(leader);
		 
		 Iterator i = record.getControlFields().iterator();
	     while (i.hasNext()) {
	         ControlField field = (ControlField) i.next();
	         Element controlField = doc.createElement(CONTROL_FIELD);
	         controlField.setAttribute("tag", field.getTag());
	         data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(field.getData()));
	         controlField.appendChild(data);
	         recordElement.appendChild(controlField);
	    }
	     
	    i = record.getDataFields().iterator();
	    while (i.hasNext()) {
	            DataField field = (DataField) i.next();
	            Element dataField = doc.createElement(DATA_FIELD);
	            
	           
	            dataField.setAttribute("tag", field.getTag());
		       
	            if(field.getIndicator1() != '\0')
	            {
	                dataField.setAttribute("ind1", String.valueOf(field
	                    .getIndicator1())); 
	            }
	            else
	            {
	            	 dataField.setAttribute("ind1", " "); 
	            }
	            if(field.getIndicator2() != '\0')
	            {
	                dataField.setAttribute("ind2", String.valueOf(field
	                    .getIndicator2())); 
	            }
	            else
	            {
	            	dataField.setAttribute("ind2", " "); 
	            }
	            recordElement.appendChild(dataField); 
	          

	            
	            Iterator j = field.getSubfields().iterator();
	            while (j.hasNext()) {
	                Subfield subfield = (Subfield) j.next();
	                Element elementSubField = doc.createElement(SUBFIELD);
	               
	                if( subfield.getCode() != '\0' )
	                {
	                    elementSubField.setAttribute("code", String
	                        .valueOf(subfield.getCode()));
	                }
	                else
	                {
	                	elementSubField.setAttribute("code", " ");
	                }
	                data = doc.createTextNode(OaiUtil.removeInvalidXmlChars(subfield.getData()));
	                elementSubField.appendChild(data);
	                dataField.appendChild(elementSubField); 
	               
	            }
	            
	        }	     
	}
	
}

