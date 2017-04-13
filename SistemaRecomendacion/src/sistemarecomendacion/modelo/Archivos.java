/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemarecomendacion.modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

/**
 *
 * @author edgarin
 */
public class Archivos {
    private File datos;
    private Vector lineasArchivo;
    private String contenido;
    double[][] matrizRating;
    int nColumnas ;

    public void setColum(int c){
        nColumnas=c;
    }
    public int getColum(){
        return nColumnas;
    }
    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public Archivos(){}
    
    public File getDatos() {
        return datos;
    }

    public void setArchivo(File datos) {
        this.datos = datos;
    }
    
    
    
    //** metodo que guarda el resultado al archivo de texto ***//
    public String  guardarArchivo(String utilidad, String tiempo){  
        String resultado = "El archivo se ha guardado exitosamente en: "; 
        String nombreArchivo = this.datos.getName();
        String ruta = System.getProperty("user.dir");
        String rutaFinal = ruta+"\\salida_"+nombreArchivo;
        
        try {
            File archivo = new File(rutaFinal);
            FileWriter fr = new FileWriter(archivo);
            BufferedWriter bf = new BufferedWriter(fr);
            bf.write(utilidad);
            bf.newLine();
            bf.write(tiempo);
            bf.flush();
            bf.close();
            fr.close();
            resultado += rutaFinal;
        } catch (IOException ex) {
               resultado = ("Error al escribir Archivo: "+ex);
        }
        
        return resultado;
    }

    
    // metodo para validar si es un numero
    private boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    //metodo que devuelve u vector con las lineas del archivo cargado
    public Vector getLineasArchivo(){
        return lineasArchivo;
    }
    
    public double[][] getMatrizRating(){
        return matrizRating;
    } 
    
    
    // metodo que lee el contenido de un archivo
    public void leerArchivo(String nombreArchivo){
        contenido = "";
        String directorio = System.getProperty("user.dir");
        String rutaFinal = directorio+"\\"+nombreArchivo;
        String resultado = "";
        lineasArchivo = new Vector();
        
        try {
            contenido = new String(Files.readAllBytes(Paths.get(rutaFinal)));
            File file = new File(rutaFinal);
            
            if( file.exists() ){
                String linea; 
                
                FileInputStream fis = new FileInputStream(file);
                DataInputStream dis = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(dis, "UTF-8"));
                
                java.util.Vector<Double> vD = new java.util.Vector<Double>();
                java.util.Vector<double[]> v = new java.util.Vector<double[]>();
                
                int cont = 0;
                int indiceMatrixFilas = 0;
                int indiceMatrixColumnas = 0;
                int sumaDuracion = 0;
                

                while ((linea = br.readLine()) != null) {
                    
                    String[] ratings = linea.split(" ");
                    nColumnas = ratings.length;
                    double row[] = new double[nColumnas];
                            
                    for (String rating : ratings) {
                        if ( !isNumeric(rating) ) {
                            resultado = "valor no permitido: " + rating;                                    
                        }else{
                            row[indiceMatrixColumnas]=Double.valueOf(rating);
                            
                            //System.out.println("indiceMatrixFilas : " + indiceMatrixFilas + " indiceMatrixColumnas : " + indiceMatrixColumnas+ " utilidadParcela : "+rating);
                            //matrizRating[indiceMatrixFilas][indiceMatrixColumnas] = Integer.parseInt(rating);
                            indiceMatrixColumnas++;
                        }
                    }
                    
                    //System.out.println(Arrays.toString(row));
                    v.addElement(row);  // Start storing rows instead of columns.
                    indiceMatrixColumnas = 0;
                    indiceMatrixFilas++;
                }
                
                int m = v.size();  // Now we've got the number of rows.
                matrizRating = new double[m][];
                v.copyInto(matrizRating);  // copy the rows out of the vector
                
                
            }
        }catch (IOException ex) {
            contenido = ("Error al leer Archivo: "+ex);
            System.out.println(contenido);
        }        
    }
}
