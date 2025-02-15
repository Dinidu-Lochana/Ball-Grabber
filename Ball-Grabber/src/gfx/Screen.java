package gfx;

public class Screen {
    public static final int MAP_WIDTH = 64;
    public static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;

    public int[] tiles = new int[MAP_WIDTH * MAP_WIDTH];
    public int[] colors = new int[MAP_WIDTH*MAP_WIDTH*4];

    public int xOffset = 0;
    public int yOffset = 0;

    public int width;
    public int height;

    public SpriteSheet sheet;

    public Screen(int width, int height, SpriteSheet sheet){
        this.width = width;
        this.height = height;
        this.sheet = sheet;

        for(int i=0; i < MAP_WIDTH*MAP_WIDTH;i++){
            colors[i*4+0]= 0xff00ff;  // Purple (inner)
            colors[i*4+1]= 0x00ffff;  // Cyan (middle)
            colors[i*4+2]= 0xffff00;  // Yellow (outer)
            colors[i*4+3]= 0xffffff;  // White (unused/border)
        }
    }

    public void render(int[] pixels, int offset, int row) {
        for (int yTile = yOffset >> 3; yTile <= (yOffset + height) >>3; yTile++){
            int yMin = yTile * 8 - yOffset;
            int yMax = yMin + 8;
            if(yMin<0) yMin = 0;
            if(yMax>height) yMax = height;

            for (int xTile = xOffset >> 3; xTile <= (xOffset + width) >>3; xTile++) {
                int xMin = xTile * 8 - xOffset;
                int xMax = xMin + 8;
                if(xMin<0) xMin = 0;
                if(xMax>width) xMax = width;

                int tileIndex = (xTile & (MAP_WIDTH_MASK)) + (yTile & (MAP_WIDTH_MASK)) * MAP_WIDTH;

                for(int y=yMin; y<yMax; y++){
                    int sheetPixel = ((y + yOffset) & 7) * sheet.width + ((xMin + xOffset) & 7);
                    int tilePixel = offset + xMin + y * row;

                    for(int x=xMin; x<xMax; x++){
                        int localX = (x + xOffset) & 7;
                        int localY = (y + yOffset) & 7;
                        int colorIndex;

                        if(localX <= 1 || localX >= 6 || localY <= 1 || localY >= 6) {
                            colorIndex = 2; // Yellow (outer)
                        } else if(localX <= 2 || localX >= 5 || localY <= 2 || localY >= 5) {
                            colorIndex = 1; // Cyan (middle)
                        } else {
                            colorIndex = 0; // Purple (inner)
                        }

                        pixels[tilePixel++] = colors[tileIndex * 4 + colorIndex];
                    }
                }
            }
        }
    }
}