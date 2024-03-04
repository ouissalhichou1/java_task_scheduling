package Ressources;


import java.io.Serializable;

public class Matrice implements Serializable {
    private int[][] data;

    // Constructeur
    public Matrice(int lignes, int colonnes) {
        data = new int[lignes][colonnes];
    }

    // Méthode pour accéder à un élément de la matrice
    public int getElement(int ligne, int colonne) {
        return data[ligne][colonne];
    }

    // Méthode pour modifier un élément de la matrice
    public void setElement(int ligne, int colonne, int valeur) {
        data[ligne][colonne] = valeur;
    }

    public int getNombreLignes() {
        return data.length;
    }

    // Getter pour le nombre de colonnes
    public int getNombreColonnes() {
        if (data.length > 0) {
            // On suppose que toutes les lignes ont la même longueur
            return data[0].length;
        } else {
            return 0; // Ou une valeur par défaut selon le cas
        }
    }

    // Méthode pour afficher la matrice
    public void afficherMatrice() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }
}
