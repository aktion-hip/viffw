package org.hip.kernel.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.hip.kernel.util.XMLRepresentation;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author Luthiger
 * Created: 21.10.2006
 */
public class XMLRepresentationTest {
    private final static String xmlTest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<languages>\n" +
            "    <language>de</language>\n" +
            "    <language>en</language>\n" +
            "</languages>";

    /**
     * Test method for {@link org.hip.kernel.util.XMLRepresentation#reveal()}.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void testReveal() throws IOException, ClassNotFoundException {
        XMLRepresentation lXML = new XMLRepresentation(xmlTest);
        Document lDoc = lXML.reveal();

        assertEquals("doc root", "languages", lDoc.getDocumentElement().getNodeName());

        NodeList lChilds = lDoc.getDocumentElement().getElementsByTagName("language");
        assertEquals(2, lChilds.getLength());
        assertEquals("language 1", "de", lChilds.item(0).getFirstChild().getNodeValue());
        assertEquals("language 2", "en", lChilds.item(1).getFirstChild().getNodeValue());

        final ByteArrayOutputStream lBytesOut = new ByteArrayOutputStream();
        final ObjectOutputStream lObjectOut = new ObjectOutputStream(lBytesOut);
        lObjectOut.writeObject(lXML);
        final byte[] lSerialized = lBytesOut.toByteArray();
        lObjectOut.close();
        lBytesOut.close();
        lXML = null;
        lDoc = null;

        final ByteArrayInputStream lBytesIn = new ByteArrayInputStream(lSerialized);
        final ObjectInputStream lObjectIn = new ObjectInputStream(lBytesIn);
        final XMLRepresentation lRetrieved = (XMLRepresentation)lObjectIn.readObject();
        lObjectIn.close();
        lBytesIn.close();

        lDoc = lRetrieved.reveal();

        assertEquals("doc root", "languages", lDoc.getDocumentElement().getNodeName());

        lChilds = lDoc.getDocumentElement().getElementsByTagName("language");
        assertEquals(2, lChilds.getLength());
        assertEquals("language 1", "de", lChilds.item(0).getFirstChild().getNodeValue());
        assertEquals("language 2", "en", lChilds.item(1).getFirstChild().getNodeValue());
    }

}
