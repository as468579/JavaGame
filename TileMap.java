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
	private int[][] map;
	private static int TILE_SIZE = 16;
	private static int DRAW_SIZE = 8;
	private static int numRows;
	private static int numCols;
	private static int width;
	private static int height;
	
	// tileset
	//private BufferedImage tileset;
	private static int numHorizontalTiles;
	private static int numVerticalTiles;
	private static Tile[][] tiles;
	
	private static Tile[][] blocks;
	private static Tile[][] hazardBlocks;
	private static Tile[][] transparentBlocks;
	private static Tile[][] functionBlocks;
	private static Tile[][] background;
	private static Tile[][] backgroundItems;
	private static ArrayList<Tile[][]> TilesetSheet = new ArrayList<>();
	
	// drawing
	private int rowOffset;   // tells which row to start drawing
	private int colOffset;   // tells which column to start drawing
	private int numRowsToDraw;  // tells how many rows to draw 
	private int numColsToDraw; // tells how many cols to draw
	
	// JSON parser
	private static ArrayList<Integer> jsonFirstIDList = new ArrayList<>();
	
	public TileMap(){
		 numRowsToDraw = ( GamePanel.HEIGHT / TILE_SIZE ) + 2; // +2?
		 numColsToDraw = ( GamePanel.WIDTH / TILE_SIZE ) + 2;
		 tween = 0.07; // test
	}
	
	
	public void loadTile(String path, int spacing, int margin){
		try{
			
			BufferedImage image = ImageIO.read(
				getClass().getResourceAsStream(path)
			);
			
			BufferedImage subimage;
			numHorizontalTiles = (image.getWidth()+spacing-margin) / (TILE_SIZE + spacing);
			numVerticalTiles = (image.getHeight()+spacing-margin) / (TILE_SIZE + spacing);
			
			tiles = new Tile[numVerticalTiles][numHorizontalTiles];
			
			for(int row = 0; row < numVerticalTiles; row++) {
				for(int col = 0; col < numHorizontalTiles; col++) {
					subimage = image.getSubimage(
							col * TILE_SIZE + margin + spacing*col,
							row * TILE_SIZE + margin + spacing*row,
							TILE_SIZE,
							TILE_SIZE );
					tiles[row][col] = new Tile(subimage, Tile.BLOCKED);
					
				}
			}
			TilesetSheet.add(tiles);
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
				if (type.equals("background")){
					System.out.println("parsing: background...");
					//background = parse((JSONArray) layer.get("data"));
				}
				else if (type.equals("background_items")){
					System.out.println("parsing: background_item...");
					backgroundItems = parse((JSONArray) layer.get("data"));
				}
				else if (type.equals("hazard_blocks")){
					System.out.println("parsing: hazard_blocks...");
					hazardBlocks = parse((JSONArray)layer.get("data"));
					
				}
				else if(type.equals("blocks")) {
					System.out.println("parsing: blocks...");
					blocks = parse((JSONArray) layer.get("data"));
				}
				else if(type.equals("function_blocks")) {
					System.out.println("parsing: function_blocks...");
					functionBlocks = parse((JSONArray) layer.get("data"));
				}
				else if(type.contentEquals("opacity_blocks")) {
					System.out.println("parsing: opacity_blocks...");
					transparentBlocks = parse((JSONArray) layer.get("data"));
				}
			}
			width = numCols * TILE_SIZE;
			height = numRows * TILE_SIZE;
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymin = 0;
			
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!>

		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<
	private static Tile[][] parse(JSONArray arr){
		Tile [][] layer = new Tile[numRows][numCols];
		int dataIndex; // all data's index
		int firstgid = 0; // firstgid to recognize which type is this index belongs to
		Tile[][] tileset = null;
		try {
			for(int row = 0; row < numRows; row++){
				for(int col = 0; col < numCols; col++){
					dataIndex = (int)(long)arr.get((row * numCols) + col);
					
					/* check index value to judge which sprite should use */
					if(dataIndex < jsonFirstIDList.get(0)){
						continue;
					}
					else if(dataIndex < jsonFirstIDList.get(1)) {	// tilesets[0]
						tileset = TilesetSheet.get(2);
						firstgid = jsonFirstIDList.get(0);
					}
					else if(dataIndex < jsonFirstIDList.get(2)) {	// tilesets[1]
						firstgid = jsonFirstIDList.get(1);
					}
					else if(dataIndex < jsonFirstIDList.get(3)) {	// tilesets[2]
						tileset = TilesetSheet.get(0);
						firstgid = jsonFirstIDList.get(2);
					}
					else if(dataIndex < jsonFirstIDList.get(4)) {	// tilesets[3]
						tileset = TilesetSheet.get(1);
						firstgid = jsonFirstIDList.get(3);
					}
					else {										// tilesets[4]
						firstgid = jsonFirstIDList.get(4);
					}
					
					layer[row][col] = getTile(tileset, dataIndex, firstgid);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return layer;
	}
	
	/*
	 * get specific tile from loaded Tile[][]
	 * */
	private static Tile getTile(Tile[][] tileset, int dataIndex, int firstgid) {
		if(dataIndex == 0) return null;
		
		int horizontal =tileset[0].length;
		int row = ((dataIndex - firstgid) / horizontal);
		int col = ((dataIndex - firstgid) % horizontal);
		return tileset[row][col];
	}
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!>
	
	public int getTileSize() { return TILE_SIZE; }
	public int getX() { return (int)x; }
	public int getY() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	
	
	public int getType(int row, int col) {
		//int rc = map[row][col];
		//int r = rc / numHorizontalTiles;
		//int c = rc % numHorizontalTiles;
		if(blockExist(row, col))
			return Tile.BLOCKED;
		else return Tile.NORMAL;
	}
	
	public void setPosition(double x, double y){

		// test * tween
		//this.x += (x - this.x) * tween;
		//this.y += (y - this.y) * tween;
		this.x = x;
		this.y = y;

		fixBounds();
		
		colOffset = (int)-this.x / TILE_SIZE;
		rowOffset = (int)-this.y / TILE_SIZE;
	}
	
	private void fixBounds() {
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	public void draw(Graphics2D g) {
		for(
			int row = rowOffset;
			row < rowOffset + numRowsToDraw;
			row++){
			
			if(row >= numRows) break;
			
			for(
				int col = colOffset;
				col < colOffset + numColsToDraw;
				col++) {
				
				if(col > numCols) break;
				//if(map[row][col] == 0) continue;
				
				//int rc = map[row][col];
				//int r = rc / numHorizontalTiles;
				//int c = rc % numHorizontalTiles;
				
				
				
				//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				
				if(backgroundItemExist(row, col)) {
					g.drawImage(
							backgroundItems[row][col].getImage(),
							(int) x + col * TILE_SIZE,
							(int) y + row * TILE_SIZE,
							null
					);
				}
				if(blockExist(row, col)) {
					g.drawImage(
							blocks[row][col].getImage(),
							(int) x + col * TILE_SIZE,
							(int) y + row * TILE_SIZE,
							null
					);
				}
				if(functionBlockExist(row, col)) {
					g.drawImage(
							functionBlocks[row][col].getImage(),
							(int) x + col * TILE_SIZE,
							(int) y + row * TILE_SIZE,
							null
					);
				}
				if(hazardBlockExist(row, col)) {
					g.drawImage(
							hazardBlocks[row][col].getImage(),
							(int) x + col * TILE_SIZE,
							(int) y + row * TILE_SIZE,
							null
					);
				}
				if(transparentBlockExist(row, col)) {
					g.drawImage(
							transparentBlocks[row][col].getImage(),
							(int) x + col * TILE_SIZE,
							(int) y + row * TILE_SIZE,
							null
					);
				}
				//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				
				
//				g.drawImage(
//						tiles[row][col].getImage(),
//						(int) x + col * TILE_SIZE,
//						(int) y + row * TILE_SIZE,
//						null
//				);
			}
				
		}
	}
	
	public void testDraw(Graphics2D g) {
		
		// after calling this func, the screen should 
		//       as same as the grasstileset.gif
		
		for(int i = 0; i < 2 ; i++) {
			for(int j = 0; j < numHorizontalTiles; j++) {
				g.drawImage(
						tiles[i][j].getImage(),
						(int) x + j * TILE_SIZE,
						(int) y + i * TILE_SIZE,
						null
				);
			}
		}
	}
	
	
	
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// check position is in bound
		public static boolean inBound(int row, int col) {
			return (row >= 0 && row < (numRows) && col >= 0 && col < (numCols));
		}
		
		/* check all object is exist */
		public static boolean blockExist(int row, int col) {
			return (inBound(row, col) && blocks[row][col] != null);
		}
		
		public static boolean backgroundExist(int row, int col) {
			return (inBound(row, col) && (background[row][col] != null));
		}
		public static boolean backgroundItemExist(int row, int col) {
			return (inBound(row, col) && (backgroundItems[row][col] != null));
		}
		public static boolean hazardBlockExist(int row, int col) {
			return (inBound(row, col) && (hazardBlocks[row][col] != null));
		}
		public static boolean transparentBlockExist(int row, int col) {
			return (inBound(row, col) && (transparentBlocks[row][col] != null));
		}
		public static boolean functionBlockExist(int row, int col) {
			return (inBound(row, col) && (functionBlocks[row][col] != null));
		}
}















