/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemarecomendacion.modelo;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import java.util.Date;
import java.util.Scanner;
/**
 *
 * @author edgarin
 */
public class Recomendacion {
    Matrix A,B,C,Z,O,I,R,S,Sroot,U,V,Vt,SUB,M,N,P,T,SQ,DEF,SOL,Rnorm, NPR, prediccion, CP, vecinos,vecindad, productos;
    int dimensionK, filRnorm, colRnorm;
    Date start_time = new Date();
    
    public void generar(double[][] M, double[][] N) {
        
        
        R = new Matrix(M);
        NPR = new Matrix(N);
     
        int rd = R.getRowDimension();
        int rc = R.getColumnDimension();
        
        if( R.getRowDimension() < R.getColumnDimension() ){
            R = R.getMatrix(0, (rd-1), 0, (rd-1));
            NPR = NPR.getMatrix(0, (rd-1), 0, (rd-1));
            System.out.println("m: "+rd+" < "+"n: "+rc+" se redimensiona a m=n="+rd);
        }
               
        // TO DO: econtrar NPR
        // fill-in matrix that provides naive non-personalized recommendation. 
        // Rnorm = R+NPR,
        //Rnorm = R.plus(NPR);
        Rnorm = NPR;
        System.out.print("Esto es Rnorm");
        Rnorm.print(6, 3);
        
        filRnorm = Rnorm.getRowDimension()-1;
        colRnorm = Rnorm.getColumnDimension()-1;
        // TO DO: como asignar un buen dimensionK
        
        // - factor Rnorm using SVD to obtain U, S and V.
        SingularValueDecomposition SVD = Rnorm.svd();
        
        try {
            /*
                Se obtiene la descomposición SVD
            */
            // Obtenemos la matriz U
            U = SVD.getU();
            
            System.out.print("esto es U");
            U.print(6, 3);
            // Obtenemos la matriz S
            S = SVD.getS();
            System.out.print("esto es S");
            S.print(6, 3);
            
            // Obtenemos la matriz V
            //V = SVD.getV();
            //System.out.print("esto es V");
            //V.print(6, 3);
            
            //Se obtiene la traspuesta de V
            //Vt = V.transpose();
            //System.out.print("Eso es V traspuesta");
            //Vt.print(6, 3);
            
            dimensionK = Rnorm.rank()-1;
            
            // - reduce the matrix U to dimension k
            U = U.getMatrix(0, filRnorm, 0, dimensionK);
            System.out.print("esto es U reducido a dimensión K");
            U.print(6, 3);
            /*
                Se obiene la reducción de las matrices S, V desde la fila 0 hasta la dimensión K,
                igualmente con con las columnas
            */
            S = S.getMatrix(0, dimensionK, 0, dimensionK);
            System.out.print("esto es S reducido a dimension K");
            S.print(6, 3);
            //V = V.getMatrix(0, mcolRnorm, 0, dimensionK);
            //Vt = Vt.getMatrix(0, dimensionK, 0, colRnorm);
            //System.out.print("esto es Vt reducido a dimensión k");
            //Vt.print(6, 3);
                                    
            // - compute the square-root of the reduced matrix Sk, to obtain Sk 1/2            
            Sroot = metodoSimplificadoNewton();
            
            // - compute two resultant matrices: UkSk 1/2 and Sk1/2Vk¢ 
            //SVD.getU().times(SVD.getS().times(SVD.getV().transpose()));
                    
            //P = Sroot.times(Vt);
            //C = U.times(Sroot);
            ///System.out.println("Sk . Vk'");
            //P.print(6, 3);
            C = U.times(Sroot);
            System.out.println("Uk . raizSk");
            C.print(6, 3);
         
        } catch ( java.lang.RuntimeException e ) {
            System.out.println(e);
        }
    }

    /** aproxima la matriz Xk a la raíz cuadrada de A tras k iteraciones
     * @return  **/
    public Matrix metodoSimplificadoNewton(){
        // X0 = I, la matriz identidad
        // Xk+1 = 1/2(Xk + AXk ^-1)
        double Ep = 1e-6;
        int  delta = 1;
        // Numero iteraciones, se introdujo arbitrariamente
        int  M = 100;
        
        Matrix X0 = Matrix.identity(S.getColumnDimension(),S.getRowDimension());
        Matrix Xk = new Matrix(S.getColumnDimension(), S.getRowDimension());
        
        for (int k = 1; k < M; k++) {
            
            Xk =  X0.plus(S.times(X0.inverse())).times(1.0/2.0);
            /*
            if( Xk.minus(X0).det() < delta || v < ep)
                break;
            */
            X0 = Xk;
        }
        
        System.out.println("Esto es la raiz de S reduciodo a dimension K:");
        Xk.print(6, 3);
        
        return Xk;
    }    
    
    /** Shorten spelling of print. **/

   private static void print (String s) {
      System.out.print(s);
   }
    
    /** Format double with Fw.d. **/

   public static String fixedWidthDoubletoString (double x, int w, int d) {
      java.text.DecimalFormat fmt = new java.text.DecimalFormat();
      fmt.setMaximumFractionDigits(d);
      fmt.setMinimumFractionDigits(d);
      fmt.setGroupingUsed(false);
      String s = fmt.format(x);
      while (s.length() < w) {
         s = " " + s;
      }
      return s;
   }
   
   public void calculoVecino (){
       int col = C.getColumnDimension();
       int fil = C.getRowDimension();
       double valorCoseno = 0.0;
       vecinos = new Matrix (fil, fil);
       A = new Matrix (1, col);
       B = new Matrix (1, col);
       
       for(int i= 0; i < C.getRowDimension(); i++){
           A = C.getMatrix(i, i, 0, col-1);
           for (int j = i+1; j < fil; j++){
               B = C.getMatrix(j, j, 0, col-1);
               valorCoseno = cosenoSimilitud(A, B);
               vecinos.set(i, j, valorCoseno);
           }
       }
       
       System.out.print("Esta es la matriz vecinos");
       vecinos.print(6, 3);
   }
   
   public double cosenoSimilitud (Matrix UA, Matrix UB){
       
       double productoPunto = UA.arrayTimes(UB).norm1();
       double distancia = UA.normF() * UB.normF();
       return productoPunto / distancia;
       
   }
   
    public void crearVecindario() {

        
        
        int numFilas = vecinos.getRowDimension();
        int numColum = vecinos.getColumnDimension();
        double mayor1;
        double mayor2;
        int pos1;
        int pos2;
        vecindad = new Matrix (numFilas, 2);
        

        /*double mayor1Colum;
        double mayor2Colum;
        int pos1Colum;
        int pos2Colum;
*/
        //sacar los dos vecinos mas similares por fila
        for (int i = 0; i < numFilas; i++) {

            mayor1 = vecinos.get(i, 0);
            pos1 = 0;

            mayor2 = vecinos.get(i, 1);
            pos2 = 1;

            for (int j = 0; j < numColum - 1; j++) {

                if (mayor1 < vecinos.get(i, j + 1)) {
                    mayor2 = mayor1;
                    pos2 = pos1;

                    mayor1 = vecinos.get(i, j + 1);
                    pos1 = j + 1;
                }
            }
            //System.out.println("primer "+mayor1);
            //System.out.println("primer "+mayor2);

            //sacar los dos vecinos mas similares por columna
            //for (int j = 0; j < numColum; j++) {
                //mayor1Colum = vecinos.get(0, j);
                //pos1Colum = 0;

                //mayor2Colum = vecinos.get(1, j);
                //pos2Colum = 1;
                for (int k = 0; k < numFilas - 1; k++) {

                    if (vecinos.get(k , i)>mayor1 ) {
                        
                        mayor2 = mayor1;
                        pos2 = pos1;
                        mayor1 = vecinos.get(k , i);
                        pos1 = k;
                    }
                    
                    else if(vecinos.get(k , i)>mayor2 ){

                        mayor2 = vecinos.get(k , i);
                        pos2 = k ;
                    }
                
            //System.out.println("segunda "+mayor1);
            //System.out.println("segunda "+mayor2);
            }
           
            
            
                    
            vecindad.set(i, 0, pos1);
            vecindad.set(i, 1, pos2);
        }
        
        
       System.out.print("Esta es la matriz vecindad");
       vecindad.print(6, 3);
        
   }
    
    public void predicciones (){
        System.out.print("Esto es la matriz original");
        R.print(6, 3);
        prediccion = new Matrix(R.getRowDimension(), R.getColumnDimension());
        for (int i = 0; i < R.getRowDimension(); i++){
            for (int j = 0; j < R.getColumnDimension(); j++){
                if(R.get(i, j) == 0){
                    int vecino1 = (int) vecindad.get(i, 0);
                    int vecino2 = (int) vecindad.get(i, 1);
                    double similitud1, similitud2;
                    
                    if(vecino1 > i){
                        similitud1 = vecinos.get(i, vecino1);
                    }else{
                        similitud1 = vecinos.get(vecino1, i);
                    }
                    
                    if(vecino2 > i){
                        similitud2 = vecinos.get(i, vecino2);
                    }else{
                        similitud2 = vecinos.get(vecino2, i);
                    }
                    
                    double cal_vecino1 = R.get(vecino1, j);
                    double cal_vecino2 = R.get(vecino2, j);
                    
                    double valor = ((similitud1 * cal_vecino1) + (similitud2 * cal_vecino2))/ (similitud1 + similitud2);
                    prediccion.set(i, j, valor);
                    
                }else{
                    prediccion.set(i, j, 0.0);
                }
            }
        }
        
        System.out.print("Esto es la matriz de predicciones");
        prediccion.print(6, 3);
    }
    
    public void top_productos(){
        int filas = R.getRowDimension();
        int columnas = R.getColumnDimension();
        productos = new Matrix (filas, columnas);
        
        for (int i = 0; i< filas; i++){
            for (int j = 0; j < columnas; j++){
                productos.set(i, j, j);
            }
        }
        
        for (int i=0; i<filas; i++){
            for (int j=0; j<columnas; j++){
                for (int k= j+1; k<columnas; k++){
                    if(prediccion.get(i,j) < prediccion.get(i,k)){
                        double auxiliar_prediccion = prediccion.get(i, j);
                        double auxiliar_productos = productos.get(i,j);
                        
                        prediccion.set(i, j, prediccion.get(i,k));
                        productos.set(i, j, productos.get(i,k));
                        
                        prediccion.set(i, k, auxiliar_prediccion);
                        productos.set(i, k, auxiliar_productos);
                    }
                }
            }
        }
        System.out.print("Esta es la matriz prediccion ordenada");
        prediccion.print(6, 3);
        System.out.print("Esta es la matriz del top de productos");
        productos.print(6, 3);
    }
    
    public void reducir_matriz(){
        Scanner n = new Scanner(System.in);
        System.out.print("Ingrese la cantidad de productos a recomendar[1-"+productos.getColumnDimension()+"]:");
        int top = n.nextInt();
        
        productos = productos.getMatrix(0, R.getRowDimension()-1, 0, top-1);
        System.out.print("Matriz de productos recomendados");
        productos.print(6, 3);
    }
}
