package ch.zhaw.swp2.stegano.model;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

/**
 * This Test Class tests the FileExtensionFactory
 * 
 * @author rest
 * 
 */
public class FileNameFactoryTest {

	@Rule
	public TemporaryFolder _tmpFolder = new TemporaryFolder();
	@Rule
	public ExpectedException _exception = ExpectedException.none();

	private FileNameFactory _feFactory;
	private File _testFile1;
	private File _testFile2;
	private File _testFile3;
	private File _testFile4;
	private File _testFile5;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		_testFile1 = _tmpFolder.newFile("test.txt");
		_testFile2 = _tmpFolder.newFile("TEST.TXT");
		_testFile3 = _tmpFolder.newFile("FirstPicture.png");
		_testFile4 = _tmpFolder.newFile("FileWithoutExtension");
		_testFile5 = null;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void newFileExtensionFactory() {
		_feFactory = new FileNameFactory();
	}

	@Test
	public void testGetExtensionWithValidInput() {
		assertTrue("txt".equals(FileNameFactory.getExtension(_testFile1)));
		assertTrue("txt".equals(FileNameFactory.getExtension(_testFile2)));
		assertTrue("png".equals(FileNameFactory.getExtension(_testFile3)));
		assertTrue("".equals(FileNameFactory.getExtension(_testFile4)));

	}

	@Test
	public void testGetExtensionWithNull() {
		_exception.expect(IllegalArgumentException.class);
		FileNameFactory.getExtension(_testFile5);
	}
}
