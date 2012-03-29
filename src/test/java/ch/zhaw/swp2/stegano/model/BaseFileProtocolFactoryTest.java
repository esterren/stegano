package ch.zhaw.swp2.stegano.model;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public class BaseFileProtocolFactoryTest {

	@Rule
	public TemporaryFolder _tmpFolder = new TemporaryFolder();
	@Rule
	public ExpectedException _exception = ExpectedException.none();

	private FileNameFactory _feFactory;
	private File _testHFile1;
	private File _testBFile1;
	private File _testHFile2;
	private File _testBFile2;
	private File _testFileNull;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		_testHFile1 = _tmpFolder.newFile("test.txt");
		_testBFile1 = _tmpFolder.newFile("FirstPicture.png");
		_testHFile2 = _tmpFolder.newFile("TEST.TXT");
		_testHFile2 = _tmpFolder.newFile("FirstPicture.bmp");
		_testFileNull = null;

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerateHeaderWithNull() {

		_exception.expect(IllegalArgumentException.class);
		BaseFileProtocolFactory.generateHeader(_testFileNull, _testFileNull);
	}

}