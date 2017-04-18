/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemarecomendacion.modelo;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import java.util.Date;

/**
 *
 * @author edgarin
 */
public class Prediccion {
    Matrix A,B,C,Z,O,I,R,S,Sroot,U,V,Vt,SUB,M,N,P,T,SQ,DEF,SOL,Rnorm, NPR, prediccion, CP;
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
            V = SVD.getV();
            System.out.print("esto es V");
            V.print(6, 3);
            
            //Se obtiene la traspuesta de V
            Vt = V.transpose();
            System.out.print("Eso es V traspuesta");
            Vt.print(6, 3);
            
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
            Vt = Vt.getMatrix(0, dimensionK, 0, colRnorm);
            System.out.print("esto es Vt reducido a dimensión k");
            Vt.print(6, 3);
                                    
            // - compute the square-root of the reduced matrix Sk, to obtain Sk 1/2            
            Sroot = metodoSimplificadoNewton();
            
            // - compute two resultant matrices: UkSk 1/2 and Sk1/2Vk¢ 
            //SVD.getU().times(SVD.getS().times(SVD.getV().transpose()));
                    
            P = Sroot.times(Vt);
            C = U.times(Sroot);
            System.out.println("Sk . Vk'");
            P.print(6, 3);
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

  
   public void calculaPrediccion (){
        CP = C.times(P);
        prediccion = new Matrix (filRnorm, colRnorm);
        for (int i = 0 ; i<filRnorm; i++){
            double sumatoria = 0.0;
            double Cpromedio = 0.0;
            int m = colRnorm;
                 for (int j = 0; j<m; j++){
                     sumatoria += R.get(i, j);
                 }

                 Cpromedio = sumatoria/m;
                
                for (int j = 0; j<m; j++){
                    double valor = 0.0;
                    valor = Cpromedio + CP.get(i, j);
                    prediccion.set(i,j, valor);
                 }
        }
        
        System.out.print("esto es la prediccion");
        prediccion.print(6, 3);
        
        Date stop_time = new Date();
        double etime = (stop_time.getTime() - start_time.getTime())/1000.;
        print("\nElapsed Time = " + 
        fixedWidthDoubletoString(etime,12,3) + " seconds\n");
        
        System.out.print("esta es nuestra matriz de entrada");
        R.print(6, 3);
        
        System.out.print(dimensionK);
    }
    
    public void imprimir (double [][] k, String nomMatriz){
        Matrix m = new Matrix (k);
        System.out.print("Matriz " +nomMatriz);
        m.print(6, 3);
    }
   
}
