package com.carlosflorencio.bomberman.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;

import com.carlosflorencio.bomberman.Board;
import com.carlosflorencio.bomberman.Game;
import com.carlosflorencio.bomberman.entities.LayeredEntity;
import com.carlosflorencio.bomberman.entities.mob.Player;
import com.carlosflorencio.bomberman.entities.mob.enemy.Balloom;
import com.carlosflorencio.bomberman.entities.mob.enemy.Doll;
import com.carlosflorencio.bomberman.entities.mob.enemy.Kondoria;
import com.carlosflorencio.bomberman.entities.mob.enemy.Minvo;
import com.carlosflorencio.bomberman.entities.mob.enemy.Oneal;
import com.carlosflorencio.bomberman.entities.tile.GrassTile;
import com.carlosflorencio.bomberman.entities.tile.PortalTile;
import com.carlosflorencio.bomberman.entities.tile.WallTile;
import com.carlosflorencio.bomberman.entities.tile.destroyable.BrickTile;
import com.carlosflorencio.bomberman.entities.tile.powerup.PowerUpCustomBallom;
import com.carlosflorencio.bomberman.entities.tile.powerup.PowerupBombs;
import com.carlosflorencio.bomberman.entities.tile.powerup.PowerupFlames;
import com.carlosflorencio.bomberman.entities.tile.powerup.PowerupSpeed;
import com.carlosflorencio.bomberman.exceptions.LoadLevelException;
import com.carlosflorencio.bomberman.graphics.Screen;
import com.carlosflorencio.bomberman.graphics.Sprite;

public class FileLevel extends Level {
	int rand_x;
	int rand_y;

	public FileLevel(String path, Board board) throws LoadLevelException {
		super(path, board);
	}

	@Override
	public void loadLevel(String path) throws LoadLevelException {
		try {
			URL absPath = FileLevel.class.getResource("/" + path);

			BufferedReader in = new BufferedReader(
					new InputStreamReader(absPath.openStream()));

			String data = in.readLine();
			StringTokenizer tokens = new StringTokenizer(data);

			_level = Integer.parseInt(tokens.nextToken());
			_height = Integer.parseInt(tokens.nextToken());
			_width = Integer.parseInt(tokens.nextToken());

			_lineTiles = new String[_height];

			for (int i = 0; i < _height; ++i) {
				_lineTiles[i] = in.readLine().substring(0, _width);
			}

			in.close();
		} catch (IOException e) {
			throw new LoadLevelException("Error loading level " + path, e);
		}
	}

	@Override
	public void createEntities() {
		genRandPoint();
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				// addLevelEntity(_lineTiles[y].charAt(x), x, y);
				if (x == rand_x && y == rand_y) {
					System.out.println("x: " + rand_x + " , y:" + rand_y);
					addLevelEntity('y', rand_x, rand_y);
				} else {
					addLevelEntity(_lineTiles[y].charAt(x), x, y);
				}
			}
		}
	}

	// ======================= Funciones para el punto random =============

	public void genRandPoint() {
		// Datos para el calculo del punto
		int min_x = 2;
		int max_x = _width - 1;
		int min_y = 2;
		int max_y = _height - 1;
		// Punto calculado sin verificar
		rand_x = (int) Math.floor((Math.random() * (max_x - min_x + 1) + min_x));
		rand_y = (int) Math.floor((Math.random() * (max_y - min_y + 1) + min_y));
		System.out.println("Punto parcial: " + rand_x + "," + rand_x);

		// Verificar que este vacio -> si no volver a generar punto
		if (!isEmpty(rand_x, rand_y)) {
			genRandPoint();
		}
	}

	public Boolean isEmpty(int x_rand, int y_rand) {
		Boolean b = false;

		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (x == x_rand && y == y_rand) {
					b = (_lineTiles[y].charAt(x) == ' ');
					System.out.println("Esta vacio? " + b);
					return b;
				}
				// addLevelEntity(_lineTiles[y].charAt(x), x_rand, y_rand);
			}
		}
		return b;

	}

	// ======================= Funciones para el punto random =============

	public void addLevelEntity(char c, int x, int y) {
		int pos = x + y * getWidth();

		switch (c) {
			case '#':
				_board.addEntitie(pos, new WallTile(x, y, Sprite.wall));
				break;
			case 'b':
				LayeredEntity layer = new LayeredEntity(x, y,
						new GrassTile(x, y, Sprite.grass),
						new BrickTile(x, y, Sprite.brick));

				if (_board.isPowerupUsed(x, y, _level) == false) {
					layer.addBeforeTop(new PowerupBombs(x, y, _level, Sprite.powerup_bombs));
				}

				_board.addEntitie(pos, layer);
				break;
			case 's':
				layer = new LayeredEntity(x, y,
						new GrassTile(x, y, Sprite.grass),
						new BrickTile(x, y, Sprite.brick));

				if (_board.isPowerupUsed(x, y, _level) == false) {
					layer.addBeforeTop(new PowerupSpeed(x, y, _level, Sprite.powerup_speed));
				}

				_board.addEntitie(pos, layer);
				break;
			case 'f':
				layer = new LayeredEntity(x, y,
						new GrassTile(x, y, Sprite.grass),
						new BrickTile(x, y, Sprite.brick));

				if (_board.isPowerupUsed(x, y, _level) == false) {
					layer.addBeforeTop(new PowerupFlames(x, y, _level, Sprite.powerup_flames));
				}

				_board.addEntitie(pos, layer);
				break;
			case '*':
				_board.addEntitie(pos, new LayeredEntity(x, y,
						new GrassTile(x, y, Sprite.grass),
						new BrickTile(x, y, Sprite.brick)));
				break;
			case 'x':
				_board.addEntitie(pos, new LayeredEntity(x, y,
						new GrassTile(x, y, Sprite.grass),
						new PortalTile(x, y, _board, Sprite.portal),
						new BrickTile(x, y, Sprite.brick)));
				break;
			case ' ':
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass));
				break;
			case 'p':
				_board.addMob(
						new Player(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
				Screen.setOffset(0, 0);

				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass));
				break;
			// Enemies
			case '1':
				_board.addMob(
						new Balloom(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass));
				break;
			case '2':
				_board.addMob(
						new Oneal(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass));
				break;
			case '3':
				_board.addMob(
						new Doll(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass));
				break;
			case '4':
				_board.addMob(
						new Minvo(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass));
				break;
			case '5':
				_board.addMob(
						new Kondoria(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass));
				break;
			// Poder customizado
			case 'y':
				layer = new LayeredEntity(x, y,
						new GrassTile(x, y, Sprite.grass),
						new BrickTile(x, y, Sprite.brick));

				if (_board.isPowerupUsed(x, y, _level) == false) {
					layer.addBeforeTop(new PowerUpCustomBallom(x, y, _level, Sprite.powerup_custom));
				}

				_board.addEntitie(pos, layer);
				break;
			default:
				_board.addEntitie(pos, new GrassTile(x, y, Sprite.grass));
				break;
		}
	}

}