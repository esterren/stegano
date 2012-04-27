package ch.zhaw.swp2.stegano.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.zip.CRC32;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CRCFactoryTest {

	CRCFactory crcF;
	CRC32 checksum;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		crcF = new CRCFactory();

		checksum = new CRC32();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetCRCemptyByte() throws IOException {
		byte[] bA1 = new byte[] {};
		checksum.update(bA1);
		assertEquals(checksum.getValue(), byteArrayToLong(CRCFactory.getCRC(bA1)));

	}

	@Test
	public void testGetCRC10RndValues() throws IOException {
		int bALength = 10;
		byte[] bA = new byte[bALength];
		Random randomGenerator = new Random();
		randomGenerator.nextBytes(bA);
		for (int i = 0; i < bALength; i++) {
			checksum.reset();
			checksum.update(bA[i]);
			assertEquals(checksum.getValue(), byteArrayToLong(CRCFactory.getCRC(new byte[] { bA[i] })));

		}

	}

	private static long byteArrayToLong(byte[] b) {
		ByteBuffer buf = ByteBuffer.wrap(b);
		return buf.getLong();
	}
}
