package org.hip.kernel.exc.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.hip.kernel.exc.DefaultExceptionWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Luthiger
 * Created: 01.09.2007
 */
public class DefaultExceptionWriterTest {
    private final static String FILE_NAME = "TestWriteError.log";
    private File errorLog = null;

    @BeforeEach
    public void setUp() throws Exception {
        this.errorLog = createErrorLog();
    }

    @AfterEach
    public void tearDown() throws Exception {
        deleteErrorLog(this.errorLog);
    }

    @Test
    public void testPrintOut() {
        try {
            new TestObject();
        } catch (final IOException exc) {
            DefaultExceptionWriter.printOut(this, exc, false);
        }
    }

    @Test
    public void testPrintOut2() throws IOException {
        final PrintWriter lWriter = new PrintWriter(this.errorLog);
        try {
            new TestObject();
        }
        catch (final IOException exc) {
            DefaultExceptionWriter.printOut(this, exc, true, lWriter);
            lWriter.close();
            final String lContent = getFileContent(this.errorLog);
            assertEquals("exc msg", "java.io.IOException: Another test", lContent.substring(0, 33));
            assertEquals("exc root", "at org.hip.kernel.exc.test.TestObject.<init>(TestObject.java:7)", lContent.substring(34, 97));
        }
        finally {
            lWriter.close();
        }
    }

    private String getFileContent(final File inFile) throws IOException {
        final StringWriter outContent = new StringWriter();

        final BufferedWriter lWriter = new BufferedWriter(outContent);
        final BufferedReader lBuffer = new BufferedReader(new FileReader(inFile));
        String lLine;
        while ((lLine = lBuffer.readLine()) != null) {
            lWriter.write(lLine, 0, lLine.length());
        }
        lBuffer.close();
        lWriter.close();
        return outContent.toString();
    }

    private File createErrorLog() throws IOException {
        final File out = new File(FILE_NAME);
        if (!out.exists()) {
            out.createNewFile();
        }
        return out;
    }

    private void deleteErrorLog(final File inFile) {
        if (inFile == null || !inFile.exists()) {
            return;
        }

        if (!inFile.delete()) {
            inFile.deleteOnExit();
        }
    }

}
