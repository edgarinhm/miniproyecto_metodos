/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemarecomendacion.vista;

import sistemarecomendacion.modelo.Archivos;
import sistemarecomendacion.modelo.Prediccion;
import sistemarecomendacion.modelo.preparacion;

/**
 *
 * @author edgarin
 */
public class DashBoard {

    public void run() {
        Archivos archivo = new Archivos();
        archivo.leerArchivo("sparse_matrix.txt");
        preparacion matriz_lista= new preparacion();
        matriz_lista.preparacion_matriz(archivo.getColum(), archivo.getMatrizRating());
        Prediccion predicion = new Prediccion();
        predicion.generar(matriz_lista.getMatriz());
        predicion.prueba();
        
        
       }
    
        //System.out.println(archivo.getContenido());
    }
    

