package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.hip.kernel.servlet.Context;
import org.hip.kernel.servlet.test.TestContext;
import org.hip.kernel.util.DateChecker;
import org.hip.kernel.util.DateCheckerImpl;
import org.hip.kernel.util.DefaultNameValueList;
import org.hip.kernel.util.NameValueList;
import org.junit.jupiter.api.Test;

/**
 * Test case for testing the DateChecker implementation
 *
 * @author: Benno Luthiger
 */
public class DateCheckerTest {
    private NameValueList getDateValues(final String inDay, final String inMonth, final String inYear) {
        DefaultNameValueList lNameValueList = null;
        try {
            lNameValueList = new DefaultNameValueList();
            lNameValueList.setValue(DateChecker.FROM_DAY_KEY, inDay);
            lNameValueList.setValue(DateChecker.FROM_MONTH_KEY, inMonth);
            lNameValueList.setValue(DateChecker.FROM_YEAR_KEY, inYear);
        }
        catch (final org.hip.kernel.util.VInvalidNameException exc) {}
        catch (final org.hip.kernel.util.VInvalidValueException exc) {}

        return lNameValueList;
    }

    @Test
    public void testReadFormDateFields() {
        final Context lContext = new TestContext();
        lContext.setParameters(getDateValues("24", "12", "1961"));

        DateChecker lDateChecker = new DateCheckerImpl(true, false);
        lDateChecker.readFormDateFields(lContext);
        assertEquals(DateChecker.DATE_OK, lDateChecker.getMode());
        assertEquals(new GregorianCalendar(1961, 11, 24), lDateChecker.getStartDate());

        lContext.setParameters(getDateValues("32", "12", "1961"));
        lDateChecker = new DateCheckerImpl(true, false);
        lDateChecker.readFormDateFields(lContext);
        assertEquals(DateChecker.FAILURE_INVALID_DATE, lDateChecker.getMode());
        assertEquals(new GregorianCalendar().get(Calendar.DATE), lDateChecker.getStartDate().get(Calendar.DATE));

        lContext.setParameters(getDateValues("2a", "12", "1961"));
        lDateChecker = new DateCheckerImpl(true, false);
        lDateChecker.readFormDateFields(lContext);
        assertEquals(DateChecker.FAILURE_NO_NUMERIC_VALUES, lDateChecker.getMode());
        assertEquals(new GregorianCalendar().get(Calendar.DATE), lDateChecker.getStartDate().get(Calendar.DATE));

        lContext.setParameters(getDateValues("", "12", "1961"));
        lDateChecker = new DateCheckerImpl(true, false);
        lDateChecker.readFormDateFields(lContext);
        assertEquals(DateChecker.FAILURE_EMPTY_FIELDS_FOUND, lDateChecker.getMode());

        lContext.setParameters(getDateValues("", "", ""));
        lDateChecker = new DateCheckerImpl(true, true);
        lDateChecker.readFormDateFields(lContext);
        assertEquals(DateChecker.DATE_OK, lDateChecker.getMode());

        lDateChecker = new DateCheckerImpl(true, false);
        lDateChecker.readFormDateFields(new TestContext());
        assertEquals(DateChecker.FAILURE_NO_PARAMETERS_FOUND, lDateChecker.getMode());
    }
}
