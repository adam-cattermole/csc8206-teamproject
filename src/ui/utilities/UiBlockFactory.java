package ui.utilities;

import backend.Block;

public class UiBlockFactory {

	public static UiBlock getUiBlock(String uiBlockType, Block block, double x, double y) {
		UiBlock b = null;
		
		switch (uiBlockType) {
		case "UiSection":
			b = new UiSection(x,y,block);
			break;
			
		case "UiPointUp":
			b = new UiPointUp(x,y,block);
			break;
			
		case "UiPointUpInverse":
			b = new UiPointUpInverse(x,y,block);
			break;
			
		case "UiPointDown":
			b = new UiPointDown(x,y,block);
			break;
			
		case "UiPointDownInverse":
			b = new UiPointDownInverse(x,y,block);
			break;
		}
		
		return b;
	}
}
