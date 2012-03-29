package ch.zhaw.swp2.stegano.model;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public class InPictureTest {

	@Rule
	public TemporaryFolder _tmpFolder = new TemporaryFolder();
	@Rule
	public ExpectedException _exception = ExpectedException.none();

	private File _testBaseFile1;
	private File _testHiddenFile1;

	private SteganoStrategy _inPictureStrategy;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		_testBaseFile1 = _tmpFolder.newFile("test.png");
		_testHiddenFile1 = _tmpFolder.newFile("test.txt");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInPictureStrategy() {
		// _inPictureStrategy = new InPictureStrategy(_testBaseFile1,
		// _testBaseFile1);
	}

}
