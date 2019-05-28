package TileMap;

import java.awt.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Main.GamePanel;

public class TileMap {

	// position
	private double x;
	private double y;

	// boundary
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;

	private double tween;

	// map
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;

	// tileset
	// private BufferedImage tileset;
	private int numHorizontalTiles;
	private int numVerticalTiles;

	private int[][] blockMap;
	private int[][] hazardBlockMap;
	private int[][] transparentBlockMap;
	private int[][] functionBlockMap;
	private int[][] backgroundItemMap;
	private ArrayList<Tile[][]> TilesSheet;

	// drawing
	private int rowOffset; // tells which row to start drawing
	private int colOffset; // tells which column to start drawing
	private int numRowsToDraw; // tells how many rows to draw
	private int numColsToDraw; // tells how many cols to draw

	// JSON parser
	private ArrayList<Integer> jsonFirstIDList = new ArrayList<>();

	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = (GamePanel.HEIGHT / tileSize) + 2;
		numColsToDraw = (GamePanel.WIDTH / tileSize) + 2;

		TilesSheet = new ArrayList<Tile[][]>();
	}

	public void loadTile(String path, int spacing, int margin) {

		try {

			BufferedImage image = ImageIO.read(getClass().getResourceAsStream(path));

			BufferedImage subimage;
			numHorizontalTiles = (image.getWidth() + spacing - margin) / (tileSize + spacing);
			numVerticalTiles = (image.getHeight() + spacing - margin) / (tileSize + spacing);

			Tile[][] tiles = new Tile[numVerticalTiles][numHorizontalTiles];

			for (int row = 0; row < numVerticalTiles; row++) {
				for (int col = 0; col < numHorizontalTiles; col++) {
					subimage = image.getSubimage(col * tileSize + margin + spacing * col,
							row * tileSize + margin + spacing * row, tileSize, tileSize);
					tiles[row][col] = new Tile(subimage);
					// all tile is blocked?

				}
			}
			TilesSheet.add(tiles);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadMap(String path) {

		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(path));
			JSONObject jObj = (JSONObject) obj;

			JSONArray layers = (JSONArray) jObj.get("layers"); // get json file's data that name is 'layers'
			JSONArray jTilesets = (JSONArray) jObj.get("tilesets");
			for (Object iObj : jTilesets) {
				jsonFirstIDList.add((int) (long) ((JSONObject) iObj).get("firstgid"));
			}
			int amount = layers.size();

			// parsing all 'layers'
			for (int i = 0; i < amount; i++) {
				JSONObject layer = (JSONObject) layers.get(i);
				String type = (String) layer.get("name");
				numCols = (int) (long) layer.get("width");
				numRows = (int) (long) layer.get("height");

				// parsing 'data' in each 'layers'
				if (type.equals("background_items")) {
					System.out.println("parsing: background_item...");
					backgroundItemMap = parse((JSONArray) layer.get("data"));
				} else if (type.equals("hazard_blocks")) {
					System.out.println("parsing: hazard_blocks...");
					hazardBlockMap = parse((JSONArray) layer.get("data"));

				} else if (type.equals("blocks")) {
					System.out.println("parsing: blocks...");
					blockMap = parse((JSONArray) layer.get("data"));
				} else if (type.equals("function_blocks")) {
					System.out.println("parsing: function_blocks...");
					functionBlockMap = parse((JSONArray) layer.get("data"));
				} else if (type.contentEquals("transparent_blocks")) {
					System.out.println("parsing: transparent_blocks...");
					transparentBlockMap = parse((JSONArray) layer.get("data"));
				}
			}
			width = numCols * tileSize;
			height = numRows * tileSize;
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private int[][] parse(JSONArray arr) {
		int[][] layer = new int[numRows][numCols];
		int data;
		try {
			for (int row = 0; row < numRows; row++) {
				for (int col = 0; col < numCols; col++) {
					data = (int) (long) arr.get((row * numCols) + col);
					layer[row][col] = data;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return layer;
	}

	public int getTileSize() {
		return tileSize;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getType(int row, int col) {

		// int rc = map[row][col];
		// int r = rc / numHorizontalTiles;
		// int c = rc % numHorizontalTiles;
		// return tiles[r][c].getType();
		try {
			if (blockMapExist(row, col))
				return Tile.BLOCKED;
			else
				return Tile.NORMAL;
		} catch (ArrayIndexOutOfBoundsException e) {
			return Tile.BLOCKED;
		}
	}

	public void setPosition(double x, double y) {

		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		// this.x = x;
		// this.y = y;

		fixBounds();

		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;

	}

	private void fixBounds() {
		if (x < xmin)
			x = xmin;
		if (y < ymin)
			y = ymin;
		if (x > xmax)
			x = xmax;
		if (y > ymax)
			y = ymax;
	}

	public void setTween(double tween) {
		this.tween = tween;
	}

	public void draw(Graphics2D g, int levelTileY, int levelTileHeight) {
		for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {

			if (row >= numRows || row > (levelTileY + levelTileHeight)) break;
			if (row < levelTileY) continue;

			for (int col = colOffset; col < colOffset + numColsToDraw; col++) {

				if (col > numCols)
					break;
				try {
					drawBackgroundItems(row, col, g);
					drawBlocks(row, col, g);
					drawFunctionBlocks(row, col, g);
					drawHazardBlocks(row, col, g);
				} catch (ArrayIndexOutOfBoundsException e) {
//					System.out.println(e);
					break;
				}
			}
		}
	}

	public void drawTransparent(Graphics2D g, int levelTileY, int levelTileHeight) {
		for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {

			if (row >= numRows || row > (levelTileY + levelTileHeight) || row < levelTileY)
				continue;

			for (int col = colOffset; col < colOffset + numColsToDraw; col++) {

				if (col > numCols)
					break;
				try {
					drawTransparentBlocks(row, col, g);
				} catch (ArrayIndexOutOfBoundsException e) {
//						System.out.println(e);
					break;
				}
			}
		}
	}

	public void drawBackgroundItems(int row, int col, Graphics2D g) {
		if (backgroundItemMapExist(row, col)) {
			g.drawImage(getTile(backgroundItemMap, row, col).getImage(), (int) x + col * tileSize,
					(int) y + row * tileSize, null);
		}
	}

	public void drawBlocks(int row, int col, Graphics2D g) {
		if (blockMapExist(row, col)) {

			Tile subT = getTile(blockMap, row, col);

			int horizontal = TilesSheet.get(0).length;
			int tRow = ((blockMap[row][col] - jsonFirstIDList.get(2)) / horizontal);
			int tCol = ((blockMap[row][col] - jsonFirstIDList.get(2)) % horizontal);

			g.drawImage(subT.getImage(), (int) x + col * tileSize, (int) y + row * tileSize, null);
		}
	}

	public void drawFunctionBlocks(int row, int col, Graphics2D g) {
		if (functionBlockMapExist(row, col)) {
			g.drawImage(getTile(functionBlockMap, row, col).getImage(), (int) x + col * tileSize,
					(int) y + row * tileSize, null);
		}
	}

	public void drawHazardBlocks(int row, int col, Graphics2D g) {
		if (hazardBlockMapExist(row, col)) {
			g.drawImage(getTile(hazardBlockMap, row, col).getImage(), (int) x + col * tileSize,
					(int) y + row * tileSize, null);
		}
	}

	public void drawTransparentBlocks(int row, int col, Graphics2D g) {
		if (transparentBlockMapExist(row, col)) {
			g.drawImage(getTile(transparentBlockMap, row, col).getImage(), (int) x + col * tileSize,
					(int) y + row * tileSize, null);
		}
	}

	public Tile getTile(int[][] map, int row, int col) {
		int firstgid = 0;
		Tile[][] tileset = null;
		Tile[][] objTile = new Tile[numRows][numCols];
		/* check index value to judge which sprite should use */
		if (map[row][col] < jsonFirstIDList.get(0)) { // 0 -> didn't draw
			return null;
		} else if (map[row][col] < jsonFirstIDList.get(1)) { // tilesets[0]
			firstgid = jsonFirstIDList.get(0);
		} else if (map[row][col] < jsonFirstIDList.get(2)) { // tilesets[1]
			firstgid = jsonFirstIDList.get(1);
		} else if (map[row][col] < jsonFirstIDList.get(3)) { // tilesets[2]
			tileset = TilesSheet.get(0);
			firstgid = jsonFirstIDList.get(2);
		} else if (map[row][col] < jsonFirstIDList.get(4)) { // tilesets[3]
			tileset = TilesSheet.get(1);
			firstgid = jsonFirstIDList.get(3);
		} else { // tilesets[4]
			firstgid = jsonFirstIDList.get(4);
		}

		int horizontal = tileset[0].length;
		int tRow = ((map[row][col] - firstgid) / horizontal);
		int tCol = ((map[row][col] - firstgid) % horizontal);

		objTile[row][col] = tileset[tRow][tCol];

		return tileset[tRow][tCol];
	}

	/* check all object is exist */
	public boolean blockMapExist(int row, int col) {
		return (blockMap[row][col] > 0);
	}

	public boolean backgroundItemMapExist(int row, int col) {
		return (backgroundItemMap[row][col] > 0);
	}

	public boolean hazardBlockMapExist(int row, int col) {
		return (hazardBlockMap[row][col] > 0);
	}

	public boolean transparentBlockMapExist(int row, int col) {
		return (transparentBlockMap[row][col] > 0);
	}

	public boolean functionBlockMapExist(int row, int col) {
		return (functionBlockMap[row][col] > 0);
	}

	public int getFunctionBlock(int row, int col) {
		try {
			if (functionBlockMap[row][col] == 24171) { // normal coin
				return FunctionBlockType.COIN;
			} else if (functionBlockMap[row][col] == 24225) { // coin2
				return FunctionBlockType.COIN2;
			} else if (functionBlockMap[row][col] == 24329 || functionBlockMap[row][col] == 24330
					|| functionBlockMap[row][col] == 24331 || functionBlockMap[row][col] == 24332
					|| functionBlockMap[row][col] == 24383 || functionBlockMap[row][col] == 24384
					|| functionBlockMap[row][col] == 24385 || functionBlockMap[row][col] == 24386
					|| functionBlockMap[row][col] == 24437 || functionBlockMap[row][col] == 24438
					|| functionBlockMap[row][col] == 24439 || functionBlockMap[row][col] == 24440) { // vine
				return FunctionBlockType.VINE;
			} else
				return -1;
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}

	public int getHazardBlock(int row, int col) {
		try {
			if (hazardBlockMap[row][col] == 24283 || hazardBlockMap[row][col] == 24392
					|| hazardBlockMap[row][col] == 24582) { // water
				return HazardBlockType.WATER;
			} else if (hazardBlockMap[row][col] == 24529) { // dirty water
				return HazardBlockType.DIRTY_WATER;
			} else if (hazardBlockMap[row][col] == 24545) { // lava
				return HazardBlockType.LAVA;
			} else if (hazardBlockMap[row][col] == 24665) { // spike
				return HazardBlockType.SPIKE;
			} else
				return -1;
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}

}
