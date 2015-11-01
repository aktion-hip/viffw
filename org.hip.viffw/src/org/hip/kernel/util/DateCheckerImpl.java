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

import java.util.Calendar;

/** Helper class which can be used to read date fields from a form (i.e Context) and checks whether the inputed data is
 * valid.
 *
 * @author: Benno Luthiger */
public class DateCheckerImpl implements DateChecker { // NOPMD by lbenno

    // checked dates
    private transient Calendar startDate;
    private transient Calendar endDate;

    // instance variables
    private transient int mode;

    /** DateChecker default constructor. */
    public DateCheckerImpl() {
        this(false, false, DateChecker.FROM_DAY_KEY,
                DateChecker.FROM_MONTH_KEY,
                DateChecker.FROM_YEAR_KEY,
                DateChecker.TO_DAY_KEY,
                DateChecker.TO_MONTH_KEY,
                DateChecker.TO_YEAR_KEY);
    }

    /** DateChecker constructor for a single date check.
     *
     * @param inSingleDate boolean true if there's only one date to check
     * @param inIgnoreEmpty boolean true if empty date fields have to be ignored */
    public DateCheckerImpl(final boolean inSingleDate, final boolean inIgnoreEmpty) {
        this(inSingleDate, inIgnoreEmpty, DateChecker.FROM_DAY_KEY,
                DateChecker.FROM_MONTH_KEY,
                DateChecker.FROM_YEAR_KEY,
                DateChecker.TO_DAY_KEY,
                DateChecker.TO_MONTH_KEY,
                DateChecker.TO_YEAR_KEY);
    }

    /** DateChecker constructor for a date check with specified field names for form date fields.
     *
     * @param inSingleDate boolean true if there's only one date to check
     * @param inIgnoreEmpty boolean true if empty date fields have to be ignored
     * @param inFromDay java.lang.String
     * @param inFromMonth java.lang.String
     * @param inFromYear java.lang.String
     * @param inToDay java.lang.String
     * @param inToMonth java.lang.String
     * @param inToYear java.lang.String */
    public DateCheckerImpl(final boolean inSingleDate, final boolean inIgnoreEmpty, final String inFromDay,
            final String inFromMonth, final String inFromYear, final String inToDay, final String inToMonth,
            final String inToYear) {
        super();

        mode = DateChecker.DATE_OK;
    }

    @Override
    public Calendar getEndDate() { // NOPMD by lbenno
        return endDate;
    }

    @Override
    public int getMode() { // NOPMD by lbenno
        return mode;
    }

    @Override
    public Calendar getStartDate() { // NOPMD by lbenno
        return startDate;
    }

    @Override
    public String toString() { // NOPMD by lbenno
        final String lMessage = "StartDate=\"" +
                ((startDate == null) ? "null" : startDate.getTime().toString()) +
                "\" EndDate=\"" +
                ((endDate == null) ? "null" : endDate.getTime().toString()) + "\"";
        return Debug.classMarkupString(this, lMessage);
    }
}
