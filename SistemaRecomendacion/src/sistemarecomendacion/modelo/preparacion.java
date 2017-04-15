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
    double [][] promedios;
    double [][] matriz01;
    
    public  double [][] getMatriz(){
        return matriz;
    }
    
    public  double [][] getPromedios(){
        return promedios;
    }
    
    public  double [][] getMatriz01(){
        return matriz01;
    }
    
    public void preparacion_matriz(int n, double m [][]){
        
        promedios=m;
        matriz = new double [promedios.length][n];
        rellenado(n);
        normalizar_matriz();
        
        
    }
    
    
   void rellenado ( int n){
        
        
        
        for (int i = 0 ; i<n; i++){
            double sumaColumna=0;
            int cont=0;
            for (int j = 0 ; j<promedios.length; j++) {
                sumaColumna += promedios[j][i];
                cont ++;
            }
            
            
            double promedioColumna= sumaColumna/cont;
            
            for (int j = 0 ; j<promedios.length; j++) {
                if (promedios [j][i]==0){
                    promedios [j][i]=promedioColumna;
                    
                    
                }
                
                
            }
            
        }
        
        //promedios = matriz;
    }
    

    void normalizar_matriz (){
        
        for (int i = 0 ; i<promedios.length; i++){
            double sumatoria = 0.0;
            double promedio = 0.0;
            int m = promedios[i].length;
            
            for (int j = 0; j<m; j++){
                sumatoria += promedios [i][j];
            }
            
            promedio = sumatoria/m;
            
            for (int j = 0; j<m; j++){
                matriz[i][j] = promedios[i][j]-promedio;
            }
        }
    }
     
    public void convertirMatriz01 (double [][] m){
        for (int i = 0 ; i<m.length; i++){
            
            for (int j = 0; j<m[i].length; j++){
                if(m[i][j] != 0){
                    m[i][j]= 1;
                }
            }      
                       
        }
        matriz01 = m;
    }
}
