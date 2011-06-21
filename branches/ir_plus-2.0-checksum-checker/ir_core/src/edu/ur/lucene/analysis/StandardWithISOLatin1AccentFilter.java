
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


/**
  * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/**
 * Filters {@link StandardTokenizer} with {@link StandardFilter}, {@link
 * LowerCaseFilter} and {@link StopFilter}, using a list of English stop words.
 *
 */

public class StandardWithISOLatin1AccentFilter extends Analyzer {
	  @SuppressWarnings("unchecked")
	private Set stopSet;

	  /**
	   * Specifies whether deprecated acronyms should be replaced with HOST type.
	   * This is false by default to support backward compatibility.
	   * 
	   *
	   * See https://issues.apache.org/jira/browse/LUCENE-1068
	   */
	  private boolean replaceInvalidAcronym = true;

	  /** An array containing some common English words that are usually not
	  useful for searching. */
	  public static final String[] STOP_WORDS = StopAnalyzer.ENGLISH_STOP_WORDS;

	  /** Builds an analyzer with the default stop words ({@link #STOP_WORDS}). */
	  public StandardWithISOLatin1AccentFilter() {
	    this(STOP_WORDS);
	  }

	  /** Builds an analyzer with the given stop words. */
	  @SuppressWarnings("unchecked")
	public StandardWithISOLatin1AccentFilter(Set stopWords) {
	    stopSet = stopWords;
	  }

	  /** Builds an analyzer with the given stop words. */
	  public StandardWithISOLatin1AccentFilter(String[] stopWords) {
	    stopSet = StopFilter.makeStopSet(stopWords);
	  }

	  /** Builds an analyzer with the stop words from the given file.
	   * @see WordlistLoader#getWordSet(File)
	   */
	  public StandardWithISOLatin1AccentFilter(File stopwords) throws IOException {
	    stopSet = WordlistLoader.getWordSet(stopwords);
	  }

	  /** Builds an analyzer with the stop words from the given reader.
	   * @see WordlistLoader#getWordSet(Reader)
	   */
	  public StandardWithISOLatin1AccentFilter(Reader stopwords) throws IOException {
	    stopSet = WordlistLoader.getWordSet(stopwords);
	  }


	  /** Constructs a {@link StandardTokenizer} filtered by a {@link
	  StandardFilter}, a {@link LowerCaseFilter} and a {@link StopFilter}. */
	  public TokenStream tokenStream(String fieldName, Reader reader) {
	    StandardTokenizer tokenStream = new StandardTokenizer(reader, replaceInvalidAcronym);
	    tokenStream.setMaxTokenLength(maxTokenLength);
	    TokenStream result = new StandardFilter(tokenStream);
	    result = new LowerCaseFilter(result);
	    result = new StopFilter(result, stopSet);
	    result = new ISOLatin1AccentFilter(result);
	    return result;
	  }

	  private static final class SavedStreams {
	    StandardTokenizer tokenStream;
	    TokenStream filteredTokenStream;
	  }

	  /** Default maximum allowed token length */
	  public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

	  private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

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
	  
	  public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
	    SavedStreams streams = (SavedStreams) getPreviousTokenStream();
	    if (streams == null) {
	      streams = new SavedStreams();
	      setPreviousTokenStream(streams);
	      streams.tokenStream = new StandardTokenizer(reader);
	      streams.filteredTokenStream = new StandardFilter(streams.tokenStream);
	      streams.filteredTokenStream = new LowerCaseFilter(streams.filteredTokenStream);
	      streams.filteredTokenStream = new StopFilter(streams.filteredTokenStream, stopSet);
	    } else {
	      streams.tokenStream.reset(reader);
	    }
	    streams.tokenStream.setMaxTokenLength(maxTokenLength);

	    return streams.filteredTokenStream;
	  }
	}
