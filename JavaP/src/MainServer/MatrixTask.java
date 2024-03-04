package MainServer;

import Ressources.Matrice;

import java.io.Serializable;

public class MatrixTask implements Serializable {

    private Matrice matrix1;
    private Matrice matrix2;
    private Matrice matrix3;
    private String operation;
    public MatrixTask(Matrice matrice1,String operation,Matrice matrice2){
        this.matrix1 = matrice1;
        this.matrix2 = matrice2;
        this.operation = operation;
    }


    public Matrice getMatrix3(){
        return this.matrix3;
    }

    public void execute() {
        if(operation.equals("Addition")){
            System.out.println("Uhyuhinik;;j");
            matrix3 = new Matrice (matrix1.getNombreLignes(),matrix1.getNombreColonnes());
            for(int i=0 ; i<matrix1.getNombreLignes();i++)
                for(int j=0 ; j<matrix1.getNombreColonnes();j++){
                    matrix3.setElement(i,j,matrix1.getElement(i,j)+matrix2.getElement(i,j));}
        }
        else{
            int result = 0;
            matrix3 = new Matrice(matrix1.getNombreLignes(),matrix2.getNombreColonnes());
            for (int i = 0; i < matrix1.getNombreLignes(); i++) {
                for (int j = 0; j < matrix2.getNombreColonnes(); j++) {
                    for (int k = 0; k < matrix1.getNombreColonnes(); k++) {
                        result += matrix1.getElement(i,k) * matrix2.getElement(k,j);
                    }
                    matrix3.setElement(i,j,result);
                    result = 0;
                }
            }
        }


    }
}
