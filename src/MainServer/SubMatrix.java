// SubMatrix.java
package MainServer;

import java.io.Serializable;
import java.util.Arrays;

public class SubMatrix implements Serializable {
    public int[][] matrix;
    public int startRow;
    public int endRow;
    public int index;

    public SubMatrix(int[][] matrix, int startRow, int endRow, int index) {
        this.matrix = new int[endRow - startRow + 2][matrix[0].length + 2];
        for (int[] row : this.matrix) {
            Arrays.fill(row, 0);
        }

        this.startRow = startRow;
        this.endRow = endRow;
        this.index = index;

        for (int i = startRow; i < endRow; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i - startRow], 0, matrix[i].length);
        }
    }
}
