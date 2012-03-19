package ch.zhaw.swp2.stegano.controller;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ControllerTest {

	private Controller _ctrl;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		_ctrl = new Controller();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void startController() {
		assertTrue(true);

	}
}
