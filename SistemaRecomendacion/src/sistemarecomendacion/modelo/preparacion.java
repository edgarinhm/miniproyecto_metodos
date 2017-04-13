/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemarecomendacion.modelo;

/**
 *
 * @author Paola
 */
public class preparacion {
    
    double [][] matriz;
    
    public  double [][] getMatriz(){
        return matriz;
    }
    
    public void preparacion_matriz(int n, double m [][]){
        
        matriz=m;
        rellenado(n);
        normalizar_matriz();
        
        
    }
    
    
   void rellenado ( int n){
        
        double sumaColumna=0;
        int cont=0;
        for (int i = 0 ; i<n; i++){
            for (int j = 0 ; j<matriz.length; j++) {
                sumaColumna += matriz[j][i];
                cont ++;
            }
            double promedioColumna= sumaColumna/cont;
            
            for (int j = 0 ; j<matriz.length; j++) {
                if (matriz [j][i]==0){
                    matriz [j][i]=promedioColumna;
                }
            }
    }
    }
    

    void normalizar_matriz (){
        
        for (int i = 0 ; i<matriz.length; i++){
            double sumatoria = 0.0;
            double promedio = 0.0;
            int m = matriz[i].length;
            for (int j = 0; j<m; j++){
                sumatoria += matriz [i][j];
            }
            
            promedio = sumatoria/m;
            
            for (int j = 0; j<m; j++){
                matriz[i][j] = matriz[i][j]-promedio;
            }
        }
    }
            
}
