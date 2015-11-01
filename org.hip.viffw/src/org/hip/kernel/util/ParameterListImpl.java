/**
 This package is part of the servlet framework used for the application VIF.
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

/** This class implements an AbstractNameValueList to be used as a parameter list.
 *
 * @author Benno Luthiger
 * @see org.hip.kernel.util.ParameterList
 * @see org.hip.kernel.util.AbstractNameValueList */
@SuppressWarnings("serial")
public class ParameterListImpl extends AbstractNameValueList implements ParameterList { // NOPMD by lbenno 

    /** This method creates a DefaultNameValue.
     *
     * @return org.hip.kernel.util.NameValue
     * @param inName java.lang.String
     * @param inValue java.lang.Object
     * @exception org.hip.kernel.util.VInvalidNameException
     * @exception org.hip.kernel.util.VInvalidValueException */
    @Override
    public NameValue create(final String inName, final Object inValue) {
        return new DefaultNameValue(this, inName, inValue);
    }

    /** Dynamic add is allowed in a ParameterList
     *
     * @return boolean */
    @Override
    public boolean dynamicAddAllowed() {
        return true;
    }
}
