/**
	This package is part of the framework used for the application VIF.
	Copyright (C) 2006-2014, Benno Luthiger

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

package org.hip.kernel.bom.impl;

import org.hip.kernel.bom.IGetValueStrategy;
import org.hip.kernel.bom.KeyCriterion;

/** Returns a 'prepared' value. This class can be used for keys in prepared update statements where the value in the SQL
 * statement should be '?'.
 *
 * @author Luthiger Created on 11.07.2007 */
@SuppressWarnings("serial")
public class PreparedValueStrategy implements IGetValueStrategy { // NOPMD by lbenno 

    @Override
    public String getValue(final KeyCriterion inCriterion) { // NOPMD by lbenno 
        final ValueForSQL lSQLValue = new ValueForSQL(inCriterion.getValue());
        return lSQLValue.getValueForPrepared();
    }

}
