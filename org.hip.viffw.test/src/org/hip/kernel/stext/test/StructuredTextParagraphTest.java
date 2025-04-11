package org.hip.kernel.stext.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hip.kernel.stext.HTMLSerializer;
import org.hip.kernel.stext.StructuredTextBullet;
import org.hip.kernel.stext.StructuredTextIndented;
import org.hip.kernel.stext.StructuredTextNumbered;
import org.hip.kernel.stext.StructuredTextParagraph;
import org.hip.kernel.stext.StructuredTextPlain;
import org.hip.kernel.stext.StructuredTextSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Luthiger
 */
public class StructuredTextParagraphTest {
    private final static String NL = System.getProperty("line.separator");
    private final static String PARAGRAPH1 = "Test paragraph.";
    private final static String PARAGRAPH2 = "Another paragraph.";

    private StructuredTextBullet structuredTextBullet;
    private StructuredTextNumbered structuredTextNumbered;
    private StructuredTextPlain structuredTextPlain;
    private StructuredTextIndented structuredTextIndented;

    @BeforeEach
    public void setUp() throws Exception {
        this.structuredTextBullet = new StructuredTextBullet(PARAGRAPH1);
        this.structuredTextNumbered = new StructuredTextNumbered(PARAGRAPH1);
        this.structuredTextPlain = new StructuredTextPlain(PARAGRAPH1);
        this.structuredTextIndented = new StructuredTextIndented(PARAGRAPH1);
    }

    @Test
    public void testGetRawString() {
        final String lExpected = "Test paragraph.";
        assertEquals(lExpected, this.structuredTextBullet.getRawString());
        assertEquals(lExpected, this.structuredTextNumbered.getRawString());
        assertEquals(lExpected, this.structuredTextPlain.getRawString());
        assertEquals(lExpected, this.structuredTextIndented.getRawString());
    }

    @Test
    public void testAccept() {
        final String lExpected1 = "<ul><li><p>Test paragraph.</p></li>" + NL + "</ul>" + NL;
        final String lExpected2 = "<ol><li><p>Test paragraph.</p></li>" + NL + "</ol>" + NL;
        final String lExpected3 = "<p>Test paragraph.</p>" + NL;

        StructuredTextSerializer lSerializer = new HTMLSerializer();
        this.structuredTextBullet.accept(lSerializer);
        assertEquals(lExpected1, lSerializer.toString());

        lSerializer = new HTMLSerializer();
        this.structuredTextNumbered.accept(lSerializer);
        assertEquals(lExpected2, lSerializer.toString());

        lSerializer = new HTMLSerializer();
        this.structuredTextPlain.accept(lSerializer);
        assertEquals(lExpected3, lSerializer.toString());

        lSerializer = new HTMLSerializer();
        this.structuredTextIndented.accept(lSerializer);
        assertEquals(lExpected3, lSerializer.toString());
    }

    @Test
    public void testEqualsType() {
        final StructuredTextBullet lStructuredTextBullet1 = new StructuredTextBullet(PARAGRAPH1);
        final StructuredTextBullet lStructuredTextBullet2 = new StructuredTextBullet(PARAGRAPH2);
        assertTrue(this.structuredTextBullet.equalsType(lStructuredTextBullet1));
        assertTrue(this.structuredTextBullet.equalsType(lStructuredTextBullet2));

        assertFalse(this.structuredTextBullet.equalsType(this.structuredTextPlain));
        assertFalse(this.structuredTextPlain.equalsType(this.structuredTextIndented));
        assertFalse(this.structuredTextBullet.equalsType(this.structuredTextNumbered));
    }

    @Test
    public void testGetParagraphType() {
        assertEquals(StructuredTextParagraph.PARAGRAPH_BULLET, this.structuredTextBullet.getParagraphType());
        assertEquals(StructuredTextParagraph.PARAGRAPH_NUMBERED, this.structuredTextNumbered.getParagraphType());
        assertEquals(StructuredTextParagraph.PARAGRAPH_PLAIN, this.structuredTextPlain.getParagraphType());
        assertEquals(StructuredTextParagraph.PARAGRAPH_INDENTED, this.structuredTextIndented.getParagraphType());
    }

    @Test
    public void testAdd() {
        final String lExpected1 = "<ul><li><p>Test paragraph.</p></li>" + NL + "<li><p>Another paragraph.</p></li>" + NL + "</ul>" + NL;
        final String lExpected2 = "<ol><li><p>Test paragraph.</p></li>" + NL + "<li><p>Another paragraph.</p></li>" + NL + "</ol>" + NL;
        final String lExpected3 = "<p>Test paragraph.</p>" + NL +  NL + "<p>Another paragraph.</p>" + NL;

        StructuredTextSerializer lSerializer = new HTMLSerializer();
        this.structuredTextBullet.add(PARAGRAPH2);
        this.structuredTextBullet.accept(lSerializer);
        assertEquals(lExpected1, lSerializer.toString());

        lSerializer = new HTMLSerializer();
        this.structuredTextNumbered.add(PARAGRAPH2);
        this.structuredTextNumbered.accept(lSerializer);
        assertEquals(lExpected2, lSerializer.toString());

        lSerializer = new HTMLSerializer();
        this.structuredTextPlain.add(PARAGRAPH2);
        this.structuredTextPlain.accept(lSerializer);
        assertEquals(lExpected3, lSerializer.toString());

        lSerializer = new HTMLSerializer();
        this.structuredTextIndented.add(PARAGRAPH2);
        this.structuredTextIndented.accept(lSerializer);
        assertEquals(lExpected3, lSerializer.toString());
    }

    @Test
    public void testToString() {
        final String lExpected1 = "<org.hip.kernel.stext.StructuredTextBullet>\nTest paragraph.</org.hip.kernel.stext.StructuredTextBullet>";
        final String lExpected2 = "<org.hip.kernel.stext.StructuredTextPlain>\nTest paragraph.</org.hip.kernel.stext.StructuredTextPlain>";
        final String lExpected3 = "<org.hip.kernel.stext.StructuredTextIndented>\nTest paragraph.</org.hip.kernel.stext.StructuredTextIndented>";
        final String lExpected4 = "<org.hip.kernel.stext.StructuredTextNumbered>\nTest paragraph.</org.hip.kernel.stext.StructuredTextNumbered>";

        assertEquals(lExpected1, this.structuredTextBullet.toString());
        assertEquals(lExpected2, this.structuredTextPlain.toString());
        assertEquals(lExpected3, this.structuredTextIndented.toString());
        assertEquals(lExpected4, this.structuredTextNumbered.toString());
    }
}
