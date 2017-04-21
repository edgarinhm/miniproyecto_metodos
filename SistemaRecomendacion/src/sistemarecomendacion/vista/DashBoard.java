/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemarecomendacion.vista;

import sistemarecomendacion.modelo.Archivos;
import sistemarecomendacion.modelo.Prediccion;
import sistemarecomendacion.modelo.preparacion;
import sistemarecomendacion.modelo.Recomendacion;

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
        predicion.imprimir(matriz_lista.getPromedios(),"promedio");
        predicion.imprimir(matriz_lista.getMatriz(),"normalizada");
        predicion.generar(archivo.getMatrizRating(), matriz_lista.getMatriz());
        predicion.calculaPrediccion();
        
        Recomendacion recomendacion = new Recomendacion();
        matriz_lista.convertirMatriz01(archivo.getMatrizRating());
        recomendacion.generar(archivo.getMatrizRating(), matriz_lista.getMatriz01());
        recomendacion.calculoVecino();
        recomendacion.crearVecindario();
        recomendacion.predicciones();
        recomendacion.top_productos();
        
        predicion.prediccion_cliente_producto();
        recomendacion.reducir_matriz();
       }
    
        //System.out.println(archivo.getContenido());
    }
    

