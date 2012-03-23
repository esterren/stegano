package ch.zhaw.swp2.stegano.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ControllerTest {

	private Controller _ctrl;
	private IfcUserInterface _userInterface = mock(IfcUserInterface.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		_ctrl = new Controller() {
			@Override
			protected IfcUserInterface getNewSteganoGUI() {
				return _userInterface;
			}
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void startController() {
		assertTrue(true);

	}
}
