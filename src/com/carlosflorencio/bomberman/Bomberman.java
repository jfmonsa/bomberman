package com.carlosflorencio.bomberman;

import com.carlosflorencio.bomberman.exceptions.BombermanException;
import com.carlosflorencio.bomberman.gui.Frame;

public class Bomberman {
	public static void main(String[] args) throws BombermanException {
		new ReadInitialConfigs();
		// Frame mainwindow = new Frame();
		new Frame();
	}
}
