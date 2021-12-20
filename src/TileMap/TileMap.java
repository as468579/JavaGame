package TileMap;

import java.awt.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

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
	//private BufferedImage tileset;
	private int numHorizontalTiles;
	private int numVerticalTiles;
	
	private int[][] blockMap;
	private int[][] hazardBlockMap;
	private int[][] transparentBlockMap;
	private int[][] functionBlockMap;
	private int[][] backgroundItemMap;
	private ArrayList<Tile[][]> TilesSheet;
	
	private int[][] transparentTile;
	
	// drawing
	private int rowOffset;   // tells which row to start drawing
	private int colOffset;   // tells which column to start drawing
	private int numRowsToDraw; // tells how many rows to draw 
	private int numColsToDraw; // tells how many cols to draw
	
	// JSON parser
	private ArrayList<Integer> jsonFirstIDList = new ArrayList<>();
	
	// map shake
	private Random random;
	private double randomShakeX;
	private double randomShakeY;
	private boolean shaking;
	
	
	public TileMap(int tileSize){
		this.tileSize = tileSize;
		 numRowsToDraw = ( GamePanel.HEIGHT / tileSize ) + 2; 
		 numColsToDraw = ( GamePanel.WIDTH / tileSize ) + 2;
		 tween = 0.07; 
		 
		 transparentTile = new int[9][2];
		 
		 TilesSheet = new ArrayList<Tile[][]>();
		 
		 shaking = false;
		 
	}
	
	
	public void loadTile(String path, int spacing, int margin){
		try{
			
			BufferedImage image = ImageIO.read(
				getClass().getResourceAsStream(path)
			);
			
			BufferedImage subimage;
			numHorizontalTiles = (image.getWidth()+spacing-margin) / (tileSize + spacing);
			numVerticalTiles = (image.getHeight()+spacing-margin) / (tileSize + spacing);
			
			Tile[][] tiles = new Tile[numVerticalTiles][numHorizontalTiles];
		
			for(int row = 0; row < numVerticalTiles; row++) {
				for(int col = 0; col < numHorizontalTiles; col++) {
					subimage = image.getSubimage(
							col * tileSize + margin + spacing*col,
							row * tileSize + margin + spacing*row,
							tileSize,
							tileSize );
					tiles[row][col] = new Tile(subimage);
				}
			}
			TilesSheet.add(tiles);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void loadMap(String path){
		try {
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(path));
			JSONObject jObj = (JSONObject) obj;
			
			JSONArray layers = (JSONArray) jObj.get("layers"); //get json file's data that name is 'layers'
			JSONArray jTilesets = (JSONArray) jObj.get("tilesets");
			for(Object iObj : jTilesets) {
				jsonFirstIDList.add((int)(long)((JSONObject) iObj).get("firstgid"));
			}
			int amount = layers.size();
			
			// parsing all 'layers'
			for(int i = 0; i < amount; i++) {
				JSONObject layer = (JSONObject) layers.get(i);
				String type = (String) layer.get("name");
				numCols = (int) (long) layer.get("width");
				numRows = (int) (long) layer.get("height");
				
				// parsing 'data' in each 'layers'
				if (type.equals("background_items")){
					System.out.println("parsing: background_item...");
					backgroundItemMap = parse((JSONArray) layer.get("data"));
				}
				else if (type.equals("hazard_blocks")){
					System.out.println("parsing: hazard_blocks...");
					hazardBlockMap = parse((JSONArray)layer.get("data"));
					
				}
				else if(type.equals("blocks")) {
					System.out.println("parsing: blocks...");
					blockMap = parse((JSONArray) layer.get("data"));
				}
				else if(type.equals("function_blocks")) {
					System.out.println("parsing: function_blocks...");
					functionBlockMap = parse((JSONArray) layer.get("data"));
				}
				 else if (type.contentEquals("transparent_blocks")) {
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

		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	private int[][] parse(JSONArray arr){
		int [][] layer = new int[numRows][numCols];
		int data;
		try {
			for(int row = 0; row < numRows; row++){
				for(int col = 0; col < numCols; col++){
					data = (int)(long)arr.get((row * numCols) + col);
					layer[row][col] = data;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return layer;
	}
	
	
	
	public int getTileSize() { return tileSize; }
	public int getX() { return (int)x; }
	public int getY() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public boolean isShaking() { return shaking; }
	public void setShaking(boolean shaking) { this.shaking = shaking; }


	public int getType(int row, int col) {
		try {
			if(blockMapExist(row, col))
				return Tile.BLOCKED;
			
			//function block
			else if(functionBlockMapExist(row, col)) {
				if(functionBlockMap[row][col] == 24171) { // normal coin
					return Tile.COIN;
				}
				else if(functionBlockMap[row][col] == 24329 || functionBlockMap[row][col] == 24330 ||
						functionBlockMap[row][col] == 24331 || functionBlockMap[row][col] == 24332 ||
						functionBlockMap[row][col] == 24383 || functionBlockMap[row][col] == 24384 ||
						functionBlockMap[row][col] == 24385 || functionBlockMap[row][col] == 24386 ||
						functionBlockMap[row][col] == 24437 || functionBlockMap[row][col] == 24438 ||
						functionBlockMap[row][col] == 24439 || functionBlockMap[row][col] == 24440) { // vine
					return Tile.VINE;
				}
			}
			
			//hazard block
			else if(hazardBlockMapExist(row, col)) {
				if(hazardBlockMap[row][col] == 24283 || hazardBlockMap[row][col] == 24392 || hazardBlockMap[row][col] == 24582) { // water
					return Tile.WATER;
				}
				else if(hazardBlockMap[row][col] == 24529) { // dirty water
					return Tile.DIRTY_WATER;
				}
				else if(hazardBlockMap[row][col] == 24545) { // lava
					return Tile.LAVA;
				}
				else if(hazardBlockMap[row][col] == 24665) { // spike
					return Tile.SPIKE;
				}
			}
			else if(transparentBlockMapExist(row, col)) {
				return Tile.TRANSPARENT;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return Tile.NORMAL;
		}
		return Tile.NORMAL;
	}
	
	public void setPosition(double x, double y){
		
		
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		//this.x = x;
		//this.y = y;

		fixBounds();
		
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
		
	}
	
	private void fixBounds() {
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	
	public void setTween(double tween) {
		this.tween = tween;
	}
	
	
	
	public int[][] getHazardBlockMap() {
		return hazardBlockMap;
	}


	public void setHazardBlockMap(int row, int col, int value) {
		hazardBlockMap[row][col] = value;
	}

	public void setTransparentBlockMap(int row, int col, int value) {
		transparentBlockMap[row][col] = value;
	}

	public void setFunctionBlockMap(int row, int col, int value) {
		functionBlockMap[row][col] = value;
	}

	public void setBackgroundItemMap(int row, int col, int value) {
		backgroundItemMap[row][col] = value;
	}
	
	// let map shaking
	public void setShake(int shakeSize) {
		
		if(isShaking()) {
			random = new Random();
			randomShakeX = (random.nextDouble()*2 - 1)*shakeSize;
			randomShakeY = (random.nextDouble()*2 - 1)*shakeSize;
		}
		else {
			randomShakeX = 0;
			randomShakeY = 0;
		}
		setPosition(this.x + randomShakeX, this.y + randomShakeY);
		
	}


	public void explosion(int row, int col) {
		
		for(int i = (row - 1); i <= (row + 1); i++) {
			for(int j = (col - 1); j <= (col + 1); j++) {
				setFunctionBlockMap(i, j, 0);
			}
		}
	}
	
	
	
	// draw blocks except for transparentBlocks
	public void draw(Graphics2D g, int levelTileY, int levelTileHeight) {
		for(
			int row = rowOffset;
			row < rowOffset + numRowsToDraw;
			row++){
			
			if (row >= numRows || row >= (levelTileY + levelTileHeight)) break;
			if (row < levelTileY) continue;
			
			for(
				int col = colOffset;
				col < colOffset + numColsToDraw;
				col++) {
				
				if(col >= numCols) break;
				
				drawBackgroundItems(row, col, g);
				drawBlocks(row, col, g);
				drawFunctionBlocks(row, col, g);
				drawHazardBlocks(row, col, g);

				
			}
				
		}
	}
	
	public void drawTransparentBlocks(Graphics2D g, int levelTileY, int levelTileHeight) {
		for(
				int row = rowOffset;
				row < rowOffset + numRowsToDraw;
				row++){
				
				if (row >= numRows || row >= (levelTileY + levelTileHeight)) break;
				if (row < levelTileY) continue;
				
				for(
					int col = colOffset;
					col < colOffset + numColsToDraw;
					col++) {
					
					if(col >= numCols) break;
					
					for(int i = 0; i < transparentTile.length; i++)
						if(row == transparentTile[i][0] && col == transparentTile[i][1]) {
							g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
							break;
						}
					drawTransparentBlocks(row, col, g);
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
				}
					
			}
	}
	
	private void drawBackgroundItems(int row, int col, Graphics2D g) {
		if(backgroundItemMapExist(row, col)) {
			g.drawImage(
					getTile(backgroundItemMap, row, col).getImage(),
					(int) x + col * tileSize,
					(int) y + row * tileSize,
					null
			);
		}
	}
	
	private void drawBlocks(int row, int col, Graphics2D g) {
		if(blockMapExist(row, col)) {
			
			Tile subT = getTile(blockMap, row, col);
			
			g.drawImage(
					subT.getImage(),
					(int) x + col * tileSize,
					(int) y + row * tileSize,
					null
			);
		}
	}
	
	private void drawFunctionBlocks(int row, int col, Graphics2D g) {
		if(functionBlockMapExist(row, col)) {
			g.drawImage(
					getTile(functionBlockMap, row, col).getImage(),
					(int) x + col * tileSize,
					(int) y + row * tileSize,
					null
			);
		}
	}
	
	private void drawHazardBlocks(int row, int col, Graphics2D g) {
		if(hazardBlockMapExist(row, col)) {
			g.drawImage(
					getTile(hazardBlockMap, row, col).getImage(),
					(int) x + col * tileSize,
					(int) y + row * tileSize,
					null
			);
		}
	}
	
	private void drawTransparentBlocks(int row, int col, Graphics2D g) {
		if(transparentBlockMapExist(row, col)) {
			g.drawImage(
					getTile(transparentBlockMap, row, col).getImage(),
					(int) x + col * tileSize,
					(int) y + row * tileSize,
					null
			);
		}
	}
	
	
	public Tile getTile(int[][] map, int row, int col) {
		int firstgid=0;
		Tile[][] tileset = null;
		Tile[][] objTile = new Tile[numRows][numCols];
		
		/* check index value to judge which sprite should use */
		if(map[row][col] < jsonFirstIDList.get(0)){ // 0 -> didn't draw
			return null;
		}
		else if(map[row][col] < jsonFirstIDList.get(1)) {	// tilesets[0]
			firstgid = jsonFirstIDList.get(0);
		}
		else if(map[row][col] < jsonFirstIDList.get(2)) {	// tilesets[1]
			firstgid = jsonFirstIDList.get(1);
		}
		else if(map[row][col] < jsonFirstIDList.get(3)) {	// tilesets[2]
			tileset = TilesSheet.get(0);
			firstgid = jsonFirstIDList.get(2);
		}
		else if(map[row][col] < jsonFirstIDList.get(4)) {	// tilesets[3]
			tileset = TilesSheet.get(1);
			firstgid = jsonFirstIDList.get(3);
		}
		else {										// tilesets[4]
			firstgid = jsonFirstIDList.get(4);
		}
		
		int horizontal = tileset[0].length;
		int tRow = ((map[row][col] - firstgid) / horizontal);
		int tCol = ((map[row][col] - firstgid) % horizontal);
		
		objTile[row][col] = tileset[tRow][tCol];
		
		return tileset[tRow][tCol];
	}
	
	
	// given row and column, return the tile can climb or not
	public boolean isClimbable(int row, int col) {
		if (getType(row, col) == Tile.VINE) { // vine tile
			return true;
		}
		else return false;
	}
	
		
	/* check all object is exist */
	private boolean blockMapExist(int row, int col) {
		return (blockMap[row][col] > 0);
	}
	
	private boolean backgroundItemMapExist(int row, int col) {
		return (backgroundItemMap[row][col] > 0);
	}
	private boolean hazardBlockMapExist(int row, int col) {
		return (hazardBlockMap[row][col] > 0);
	}
	private boolean transparentBlockMapExist(int row, int col) {
		return (transparentBlockMap[row][col] > 0);
	}
	private boolean functionBlockMapExist(int row, int col) {
		return (functionBlockMap[row][col] > 0);
	}
	
	
	public void setTransparent(int x, int y) {
		int tileCol = x/tileSize;
		int tileRow = y/tileSize;
		if(x == -1 && y == -1) { 
			for(int i = 0; i < transparentTile.length; i++) {
				transparentTile[i][0] = 0;
				transparentTile[i][1] = 0;
			}
			return;
		}
		for(int i = 0; i < transparentTile.length; i++) {
			
			if(i / 3 == 0) transparentTile[i][0] = tileRow-1;
			else if(i / 3 == 1) transparentTile[i][0] = tileRow;
			else if(i / 3 == 2) transparentTile[i][0] = tileRow+1;
			
			if(i % 3 == 0) transparentTile[i][1] = tileCol-1;
			else if(i % 3 == 1) transparentTile[i][1] = tileCol;
			else if(i % 3 == 2) transparentTile[i][1] = tileCol+1;
			
		}
		
	}
}