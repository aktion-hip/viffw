package org.hip.kernel.bom.impl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.sql.Blob;
import java.sql.SQLException;

import org.hip.kernel.bom.BOMException;
import org.hip.kernel.exc.VException;
import org.hip.kernel.sys.VSys;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlobHomeImplTest {
    private static TestBlobHome home;

    @BeforeAll
    public static void init() {
        home = (TestBlobHome)VSys.homeManager.getHome(TestBlobImpl.HOME_CLASS_NAME);
    }

    @BeforeEach
    public void tearDown() throws Exception {
        DataHouseKeeper.INSTANCE.deleteAllFrom("tblBlobTest");
    }

    @Test
    public void testCreate() throws BOMException, VException, SQLException {
        assertEquals(0, home.getCount());

        final TestBlobImpl lModel = (TestBlobImpl)home.create();
        lModel.set(TestBlobHome.KEY_NAME, "Content");
        lModel.insert(true);

        assertEquals( 1, home.getCount());
    }

    @Test
    public void testNew() throws VException, SQLException {
        final File lFile = new File("tools.gif");
        final TestBlobImpl lModel = home.ucNew("Content",  lFile);
        assertNotNull( lModel);

        final Blob lBlob = (Blob)lModel.get(TestBlobHome.KEY_XVALUE);
        assertNotNull( lBlob);
        assertEquals( lFile.length(), lBlob.length());
    }

}
