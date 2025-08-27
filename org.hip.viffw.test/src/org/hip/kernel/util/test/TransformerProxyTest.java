package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.hip.kernel.servlet.test.TestPrintWriter;
import org.hip.kernel.servlet.test.TestXSLView;
import org.hip.kernel.util.TransformerProxy;
import org.hip.kernel.util.XMLRepresentation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * TransformerProxyTest.java
 *
 * Created on 25.10.2002
 * @author Benno Luthiger
 */
public class TransformerProxyTest {
    private static final String NL = System.getProperty("line.separator");

    private final static String XML1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + NL +
            "<Root>\n" +
            "  <firstLine>This is the first line.</firstLine>\n" +
            "  <secondLine>This line follows the first.</secondLine>\n" +
            "</Root>";
    private final static String XML2 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + NL +
            "<Root>\n" +
            "  <child>Nice day today.</child>\n" +
            "</Root>";
    private final static String EXPECTED1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><transformed><Root>" + NL +
            "  <firstLine>This is the first line.</firstLine>" + NL +
            "  <secondLine>This line follows the first.</secondLine>" + NL +
            "</Root></transformed>";
    private final static String EXPECTED2 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><transformed><Root>" + NL +
            "  <firstLine>This is the first line.</firstLine>" + NL +
            "  <secondLine>This line follows the first.</secondLine>" + NL +
            "<warnings><warning>Hey, this is wrong!!!</warning></warnings></Root></transformed>";
    private final static String EXPECTED3 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><transformed><Root>" + NL +
            "  <firstLine>This is the first line.</firstLine>" + NL +
            "  <secondLine>This line follows the first.</secondLine>" + NL +
            "<messages><message>Ough, this could be difficult!</message></messages></Root></transformed>";
    private final static String EXPECTED4 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><transformed><Root>" + NL +
            "  <firstLine>This is the first line.</firstLine>" + NL +
            "  <secondLine>This line follows the first.</secondLine>" + NL +
            "<warnings><warning>Hey, this is wrong!!!</warning><warning>Arrgh, you messed it up!!!</warning></warnings></Root></transformed>";
    private final static String EXPECTED5 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><transformed><Root>" + NL +
            "  <firstLine>This is the first line.</firstLine>" + NL +
            "  <secondLine>This line follows the first.</secondLine>" + NL +
            "</Root><added/></transformed>";
    private final static String EXPECTED6 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><transformed><Root>" + NL +
            "  <firstLine>This is the first line.</firstLine>" + NL +
            "  <secondLine>This line follows the first.</secondLine>" + NL +
            "</Root><added>;jsessionid=10101</added></transformed>";
    private final static String EXPECTED7 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><transformed><Root>" + NL +
            "  <firstLine>This is the first line.</firstLine>" + NL +
            "  <secondLine>This line follows the first.</secondLine>" + NL +
            "</Root><p>disabled</p></transformed>";
    private final static String EXPECTED8 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><transformed><Root>" + NL +
            "  <firstLine>This is the first line.</firstLine>" + NL +
            "  <secondLine>This line follows the first.</secondLine>" + NL +
            "</Root><p>enabled</p></transformed>";

    private final static String XSL_FILE = "de/" + TestXSLView.XSL_FILE;
    private final static String XSL_FILE1 = "TransformerTest1.xsl";
    private final static String XSL_FILE2 = "TransformerTest2.xsl";
    private final static String XSL_SOURCE = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
            "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
            "</xsl:stylesheet>";

    @SuppressWarnings("serial")
    private class TestTransformerProxy extends TransformerProxy {
        private TestTransformerProxy(final String inXSLName, final XMLRepresentation inXML) {
            super(inXSLName, inXML, null);
        }
        public int countEntries() {
            return cTemplates.size();
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        deleteFile(XSL_FILE1);
        deleteFile(XSL_FILE2);
    }

    private void createXSLFile(final String inFileName) {
        final File lXSLFile = deleteFile(inFileName);
        try {
            final FileWriter lFileWriter = new FileWriter(lXSLFile);
            final BufferedWriter lBufferedWriter = new BufferedWriter(lFileWriter);
            lBufferedWriter.write(XSL_SOURCE);
            lBufferedWriter.close();
        }
        catch (final IOException exc) {
            fail(exc.getMessage());
        }
    }

    private File deleteFile(final String inFileName) {
        final File outFile = new File(inFileName);
        if (outFile.exists()) {
            outFile.delete();
        }
        return outFile;
    }

    /**
     * Test caching of templates.
     *
     * Before a transformation, the stylesheet is compiled and cached in
     * a Hashtable for subsequent uses.
     * Different instances of the TransformerProxy can use the same template.
     */
    @Test
    public void testTransformerProxy() throws Exception {
        createXSLFile(XSL_FILE1);
        createXSLFile(XSL_FILE2);

        final TestPrintWriter lWriter = new TestPrintWriter();
        final TestTransformerProxy lTransformer1 = new TestTransformerProxy(XSL_FILE1, new XMLRepresentation(XML1));
        assertNotNull(lTransformer1);
        final int lInitial = lTransformer1.countEntries();

        lTransformer1.renderToWriter(lWriter, "");
        assertEquals(lInitial + 1, lTransformer1.countEntries());
        lTransformer1.renderToWriter(lWriter, "");
        assertEquals(lInitial + 1, lTransformer1.countEntries());

        final TestTransformerProxy lTransformer2 = new TestTransformerProxy(XSL_FILE1, new XMLRepresentation(XML2));
        assertEquals(lInitial + 1, lTransformer2.countEntries());
        lTransformer2.renderToWriter(lWriter, "");
        assertEquals(lInitial + 1, lTransformer2.countEntries());

        final TestTransformerProxy lTransformer3 = new TestTransformerProxy(XSL_FILE2, new XMLRepresentation(XML1));
        assertEquals(lInitial + 1, lTransformer3.countEntries());
        lTransformer3.renderToWriter(lWriter, "");
        assertEquals(lInitial + 2, lTransformer3.countEntries());

        final TestTransformerProxy lTransformer4 = new TestTransformerProxy(XSL_FILE2, new XMLRepresentation(XML2));
        assertEquals(lInitial + 2, lTransformer4.countEntries());
        lTransformer4.renderToWriter(lWriter, "");
        assertEquals(lInitial + 2, lTransformer4.countEntries());
    }

    @Test
    public void testRenderToStream() throws Exception {
        TestPrintWriter lWriter = new TestPrintWriter();
        TransformerProxy lTransformer = new TransformerProxy(XSL_FILE, new XMLRepresentation(XML1), null);
        lTransformer.renderToWriter(lWriter, "");
        assertEquals(EXPECTED1, lWriter.getStreamedText());
        lWriter.close();
        lWriter = new TestPrintWriter();

        lTransformer = new TransformerProxy("de/SimpleTransform2.xsl", new XMLRepresentation(XML1), null);
        lTransformer.renderToWriter(lWriter, "");
        assertEquals(EXPECTED5, lWriter.getStreamedText());
        lWriter.close();
        lWriter = new TestPrintWriter();

        lTransformer.renderToWriter(lWriter, "10101");
        assertEquals(EXPECTED6, lWriter.getStreamedText());
    }

    @Test
    public void testRenderToStreamWithParam() throws Exception {
        TestPrintWriter lWriter = new TestPrintWriter();

        final HashMap<String, Object> lParameters = new HashMap<String, Object>();
        lParameters.put("styleParameter", new Integer(1));

        TransformerProxy lTransformer = new TransformerProxy("de/SimpleTransform3.xsl", new XMLRepresentation(XML1), null);
        lTransformer.renderToWriter(lWriter, "");
        assertEquals(EXPECTED7, lWriter.getStreamedText());
        lWriter.close();
        lWriter = new TestPrintWriter();

        lTransformer = new TransformerProxy("de/SimpleTransform3.xsl", new XMLRepresentation(XML1), lParameters);
        lTransformer.renderToWriter(lWriter, "");
        assertEquals(EXPECTED8, lWriter.getStreamedText());
    }

    @Test
    public void testIncludeErrorMessage() throws Exception {
        TestPrintWriter lWriter = new TestPrintWriter();
        final TransformerProxy lTransformer = new TransformerProxy(XSL_FILE, new XMLRepresentation(XML1), null);
        lTransformer.includeErrorMessage("Hey, this is wrong!!!");
        lTransformer.renderToWriter(lWriter, "");
        assertEquals(EXPECTED2, lWriter.getStreamedText());
        lWriter.close();
        lWriter = new TestPrintWriter();

        lTransformer.clearErrorMessages();
        lTransformer.renderToWriter(lWriter, "");
        assertEquals(EXPECTED1, lWriter.getStreamedText());
        lWriter.close();
        lWriter = new TestPrintWriter();

        lTransformer.includeErrorMessage("Hey, this is wrong!!!");
        lTransformer.includeErrorMessage("Arrgh, you messed it up!!!");
        lTransformer.renderToWriter(lWriter, "");
        assertEquals(EXPECTED4, lWriter.getStreamedText());
        lWriter.close();
        lWriter = new TestPrintWriter();

        lTransformer.clearErrorMessages();
        lTransformer.renderToWriter(lWriter, "");
        assertEquals(EXPECTED1, lWriter.getStreamedText());
    }

    @Test
    public void testIncludeMessage() throws Exception {
        TestPrintWriter lWriter = new TestPrintWriter();
        final TransformerProxy lTransformer = new TransformerProxy(XSL_FILE, new XMLRepresentation(XML1), null);
        lTransformer.includeMessage("Ough, this could be difficult!");
        lTransformer.renderToWriter(lWriter, "");
        assertEquals(EXPECTED3, lWriter.getStreamedText());
        lWriter.close();
        lWriter = new TestPrintWriter();

        lTransformer.clearMessages();
        lTransformer.renderToWriter(lWriter, "");
        assertEquals(EXPECTED1, lWriter.getStreamedText());
    }
}
