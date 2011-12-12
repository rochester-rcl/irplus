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

package edu.ur.lucene.analysis;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;


/**
 * This was adapted from apache standard analyzer
 * 
 * @author Nathan Sarr
 *
 */
public class StandardWithACIIFoldingFilter extends StopwordAnalyzerBase {
	
	 /** Default maximum allowed token length */
	  public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

	  private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

	  /**
	   * Specifies whether deprecated acronyms should be replaced with HOST type.
	   * See {@linkplain "https://issues.apache.org/jira/browse/LUCENE-1068"}
	   */
	  private final boolean replaceInvalidAcronym;

	  /** An unmodifiable set containing some common English words that are usually not
	  useful for searching. */
	  public static final Set<?> STOP_WORDS_SET = StopAnalyzer.ENGLISH_STOP_WORDS_SET; 
	  
	  
	  public  StandardWithACIIFoldingFilter()
	  {
		  this(Version.LUCENE_35, STOP_WORDS_SET);
	  }

	  /** Builds an analyzer with the given stop words.
	   * @param matchVersion Lucene version to match See {@link
	   * <a href="#version">above</a>}
	   * @param stopWords stop words */
	  public StandardWithACIIFoldingFilter(Version matchVersion, Set<?> stopWords) {
	    super(matchVersion, stopWords);
	    replaceInvalidAcronym = matchVersion.onOrAfter(Version.LUCENE_24);
	  }

	  /** Builds an analyzer with the default stop words ({@link
	   * #STOP_WORDS_SET}).
	   * @param matchVersion Lucene version to match See {@link
	   * <a href="#version">above</a>}
	   */
	  public StandardWithACIIFoldingFilter(Version matchVersion) {
	    this(matchVersion, STOP_WORDS_SET);
	  }

	  /** Builds an analyzer with the stop words from the given file.
	   * @see WordlistLoader#getWordSet(Reader, Version)
	   * @param matchVersion Lucene version to match See {@link
	   * <a href="#version">above</a>}
	   * @param stopwords File to read stop words from */
	  public StandardWithACIIFoldingFilter(Version matchVersion, File stopwords) throws IOException {
	    this(matchVersion, WordlistLoader.getWordSet(IOUtils.getDecodingReader(stopwords,
	        IOUtils.CHARSET_UTF_8), matchVersion));
	  }

	  /** Builds an analyzer with the stop words from the given reader.
	   * @see WordlistLoader#getWordSet(Reader, Version)
	   * @param matchVersion Lucene version to match See {@link
	   * <a href="#version">above</a>}
	   * @param stopwords Reader to read stop words from */
	  public StandardWithACIIFoldingFilter(Version matchVersion, Reader stopwords) throws IOException {
	    this(matchVersion, WordlistLoader.getWordSet(stopwords, matchVersion));
	  }

	  /**
	   * Set maximum allowed token length.  If a token is seen
	   * that exceeds this length then it is discarded.  This
	   * setting only takes effect the next time tokenStream or
	   * reusableTokenStream is called.
	   */
	  public void setMaxTokenLength(int length) {
	    maxTokenLength = length;
	  }
	    
	  /**
	   * @see #setMaxTokenLength
	   */
	  public int getMaxTokenLength() {
	    return maxTokenLength;
	  }


	  
	  @Override
	  protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
	    final StandardTokenizer src = new StandardTokenizer(matchVersion, reader);
	    src.setMaxTokenLength(maxTokenLength);
	    src.setReplaceInvalidAcronym(replaceInvalidAcronym);
	    TokenStream tok = new StandardFilter(matchVersion, src);
	    tok = new LowerCaseFilter(matchVersion, tok);
	    tok = new StopFilter(matchVersion, tok, stopwords);
	    tok =  new ASCIIFoldingFilter(tok);
	    return new TokenStreamComponents(src, tok) {
	      @Override
	      protected boolean reset(final Reader reader) throws IOException {
	        src.setMaxTokenLength(StandardWithACIIFoldingFilter.this.maxTokenLength);
	        return super.reset(reader);
	      }
	    };
	  }


	


}
