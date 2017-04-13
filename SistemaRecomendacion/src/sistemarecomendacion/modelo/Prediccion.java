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
    Matrix A,B,C,Z,O,I,R,S,Sroot,U,V,Vt,X,SUB,M,N,P,T,SQ,DEF,SOL,Rnorm, NPR;
    int dimensionK=1;
    
    public void generar(double[][] M) {
        Date start_time = new Date();
        
        R = new Matrix(M);
     
        int rd = R.getRowDimension();
        int rc = R.getColumnDimension();
        
        if( R.getRowDimension() < R.getColumnDimension() ){
            R = R.getMatrix(0, (rd-1), 0, (rd-1));
            System.out.println("m: "+rd+" < "+"n: "+rc+" se redimensiona a m=n="+rd);
        }
        
        // TO DO: econtrar NPR
        //fill-in matrix that provides naive non-personalized recommendation. 
        //Rnorm = R.plus(NPR);
        Rnorm = R;
        
        // TO DO: como asignar un buen dimensionK
        
        // - factor Rnorm using SVD to obtain U, S and V.
        SingularValueDecomposition SVD = Rnorm.svd();
        
        try {
            U = SVD.getU();
            S = SVD.getS();
            V = SVD.getV();
            Vt = V.transpose();
            
            // - reduce the matrix S to dimension k
            U = U.getMatrix(0, dimensionK, 0, dimensionK);
            
            
            S = S.getMatrix(0, dimensionK, 0, dimensionK);
            V = V.getMatrix(0, dimensionK, 0, dimensionK);
            Vt = Vt.getMatrix(0, dimensionK, 0, dimensionK);
            
            System.out.println("getRowDimension: "+U.getRowDimension());
            System.out.println("getColumnDimension: "+U.getColumnDimension());
                        
            // - compute the square-root of the reduced matrix Sk, to obtain Sk 1/2            
           //Sroot = metodoSimplificadoNewton();
            
            // - compute two resultant matrices: UkSk 1/2 and Sk1/2Vk¢ 
            //SVD.getU().times(SVD.getS().times(SVD.getV().transpose()));
         
        } catch ( java.lang.RuntimeException e ) {
            System.out.println(e);
        }
        
        Date stop_time = new Date();
      double etime = (stop_time.getTime() - start_time.getTime())/1000.;
      print("\nElapsed Time = " + 
         fixedWidthDoubletoString(etime,12,3) + " seconds\n");
      print("Adios\n");
    }

    /** aproxima la matriz Xk a la raíz cuadrada de A tras k iteraciones
     * @return  **/
    public Matrix metodoSimplificadoNewton(){
        double[][] Xk = new double[S.getColumnDimension()][S.getRowDimension()];
        return new Matrix(Xk);
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

   /** Format integer with Iw. **/

   public static String fixedWidthIntegertoString (int n, int w) {
      String s = Integer.toString(n);
      while (s.length() < w) {
         s = " " + s;
      }
      return s;
   }
    
   
   public void prueba (){
       double [][] matriz= S.getArray();
       for (int i=0; i< matriz.length; i++ ){
           for (int j=0; j<matriz[i].length;j++){
               System.out.print("i="+i);
                System.out.print(" j="+j +" ");
               System.out.print(matriz[i][j]+"\n");
           
       } 
       }
   }
}
