package org.castor.jpa.scenario.lob;

import org.apache.commons.io.FileUtils;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class LobITCase {

    private static final long ID = 1L;
    private static final String CLOB = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. \n"
            + "\n"
            + "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. \n"
            + "\n"
            + "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. \n"
            + "\n"
            + "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer";
    private static byte[] blob;

    @Autowired
    private JDOManager jdoManager;

    @BeforeClass
    public static void setUp() throws Exception {
        blob = FileUtils.readFileToByteArray(
                File.createTempFile("castor_", "_lob"));
    }

    @Before
    public void initDb() throws PersistenceException {
        final Database db = jdoManager.getDatabase();
        assertNotNull(db);
    }

    @After
    public void cleanDb() throws PersistenceException {
        final Database db = jdoManager.getDatabase();
        if (db.isActive()) {
            db.rollback();
        }
        db.close();
    }

    @Test
    public void lobIsPersistedAsExpected() throws Exception {
        final Database db = jdoManager.getDatabase();
        final LobEntity entityToPersist = new LobEntity();
        entityToPersist.setId(ID);
        entityToPersist.setClob(CLOB);
        entityToPersist.setBlob(blob);

        db.begin();
        db.create(entityToPersist);
        db.commit();

        db.begin();
        final LobEntity loadedEntity = db.load(LobEntity.class, ID);
        db.commit();

        assertNotNull(loadedEntity);
        assertEquals(CLOB, loadedEntity.getClob());
        assertEquals(blob, loadedEntity.getBlob());
    }

}
