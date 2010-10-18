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

package edu.ur.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Simple file utilities
 * 
 * @author Nathan Sarr
 *
 */
public class FileUtil
{

    /**
     * Create a directory in the specified location.
     *
     * @param directory to create
     */
    public void createDirectory(File directory)
    {
        try
        {
            FileUtils.forceMkdir(directory);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a file in the specified directory with the
     * specified name and place in it the specified contents.
     *
     * @param directory - directory in which to create the file
     * @param fileName - name of the file to create
     * @param contents - Simple string to create the file
     */
    public File creatFile(File directory, String fileName, String contents)
    {
        File f = new File(directory.getAbsolutePath() + IOUtils.DIR_SEPARATOR + fileName);

        // create the file
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;

        try
        {

            f.createNewFile();
            fos = new FileOutputStream(f);
            bos = new BufferedOutputStream(fos);

            bos.write(contents.getBytes());
            bos.flush();
            bos.close();
            fos.close();
        } catch (Exception e)
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                } catch (Exception ec)
                {
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                } catch (Exception ec)
                {
                }
            }
            throw new RuntimeException(e);
        }

        return f;
    }
}
