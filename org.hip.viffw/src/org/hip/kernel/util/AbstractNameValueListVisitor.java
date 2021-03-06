/**
	This package is part of the framework used for the application VIF.
	Copyright (C) 2001-2014, Benno Luthiger

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License as published by the Free Software Foundation; either
	version 2.1 of the License, or (at your option) any later version.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	Lesser General Public License for more details.

	You should have received a copy of the GNU Lesser General Public
	License along with this library; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.hip.kernel.util;

import org.hip.kernel.sys.VObject;

/** This is the base implementation for a visitor pattern to a name value list. */
abstract public class AbstractNameValueListVisitor extends VObject implements NameValueListVisitor { // NOPMD by lbenno 
    protected transient String externalized = "";

    /** @return java.lang.String */
    @Override
    public String toString() {
        return externalized;
    }

    /** @param inNameValue org.hip.kernel.util.NameValue */
    @Override
    abstract public void visitNameValue(NameValue inNameValue);

    /** @param inNameValueList org.hip.kernel.util.NameValueList */
    @Override
    abstract public void visitNameValueList(NameValueList inNameValueList);

    /** @param inSortableItem org.hip.kernel.util.SortableItem */
    @Override
    abstract public void visitSortableItem(SortableItem inSortableItem);

    /** @param inSortedList org.hip.kernel.util.SortedList */
    @Override
    abstract public void visitSortedList(SortedList inSortedList);
}
