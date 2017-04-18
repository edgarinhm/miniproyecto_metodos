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
        
        promedios = new double[m.length][m[0].length];
        matriz = new double[m.length][m[0].length];
        rellenado(n, m);
        normalizar_matriz();
        
        
    }
    
    
   void rellenado ( int n, double[][] m ){
        
        for (int i = 0 ; i<n; i++){
            double sumaColumna=0;
            int cont=0;
            for (int j = 0 ; j<m.length; j++) {
                sumaColumna += m[j][i];
                cont ++;
            }
            
            
            double promedioColumna= sumaColumna/cont;
            
            for (int j = 0 ; j<m.length; j++) {
                if ( m[j][i]==0){
                    promedios[j][i]=promedioColumna;
                }else{
                    promedios[j][i] = m[j][i];
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
        
        matriz01 = new double[m.length][m[0].length];
                
        for (int i = 0 ; i<m.length; i++){
            
            for (int j = 0; j<m[i].length; j++){
                if(m[i][j] != 0){
                    matriz01[i][j]= 1;
                }else{
                    matriz01[i][j]= m[i][j];
                }
            }      
                       
        }        
    }
}
