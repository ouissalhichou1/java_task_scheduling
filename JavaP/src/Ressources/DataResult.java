package Ressources;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class DataResult implements Serializable {
   public int[][] image;
   public int index=0;

    public DataResult(int width,int height){
        image = new int[width][height];
    }
    public DataResult(int[][] image){
        this.image=image;
    }
    public DataResult(int[][] image,int index){
        this.image=image;
        this.index=index;
    }

    public DataResult(BufferedImage stampImage) {

    }

    public int[][] getData() {
        return image;
    }
}
