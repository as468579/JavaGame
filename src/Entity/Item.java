package Entity;

import java.awt.image.BufferedImage;

import TileMap.TileMap;

public abstract class Item extends MapObject{

	protected boolean touched;
	protected boolean remove;
	
	public Item (TileMap tm) {
		super(tm);
	}
	
	public void setTouched() {
		if(touched) return;
		touched = true;
	}
	
	public boolean isTouched() {
		return touched;
	}
	
	public boolean shouldRemove() {
		return remove;
	}

	public abstract void update();
	
}
