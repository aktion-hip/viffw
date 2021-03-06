/**
This package is part of Relations project.
Copyright (C) 2007-2014, Benno Luthiger

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.hip.kernel.bom.impl;

import org.hip.kernel.bom.IValueForSQL;

/** Implementation of <code>IValueForSQL</code> that escapes quote with double quote.
 *
 * @author Luthiger Created on 22.07.2007
 * @see IValueForSQL */
public class ValueForSQLDoubleQuote extends ValueForSQL {

    /** @param inValue */
    public ValueForSQLDoubleQuote(final Object inValue) {
        super(inValue);
    }

    @Override
    protected String checkString(final String inValue) { // NOPMD by lbenno 
        final char[] lRepl = { '\'', '\'' };
        return inValue.replaceAll("\'", new String(lRepl));
    }

}
