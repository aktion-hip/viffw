/**
	This package is part of the application VIF.
	Copyright (C) 2010-2015, Benno Luthiger

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.hip.kernel.servlet.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/** Wrapper class for the file upload's <code>FileItem</code> object.
 *
 * @author Luthiger Created: 22.09.2010
 * @see org.apache.commons.fileupload.FileItem */
public class FileItem {
    private final transient org.apache.commons.fileupload.FileItem item;

    /** FileItem constructor.
     *
     * @param inItem {@link org.apache.commons.fileupload.FileItem} */
    public FileItem(final org.apache.commons.fileupload.FileItem inItem) {
        item = inItem;
    }

    public InputStream getInputStream() throws IOException { // NOPMD
        return item.getInputStream();
    }

    public String getContentType() { // NOPMD
        return item.getContentType();
    }

    public String getName() { // NOPMD
        return item.getName();
    }

    public boolean isInMemory() { // NOPMD
        return item.isInMemory();
    }

    public long getSize() { // NOPMD
        return item.getSize();
    }

    public byte[] get() { // NOPMD
        return item.get();
    }

    public String getFieldName() { // NOPMD
        return item.getFieldName();
    }

    public void delete() { // NOPMD
        item.delete();
    }

    public void write(final File inFile) throws Exception { // NOPMD
        item.write(inFile);
    }

}
