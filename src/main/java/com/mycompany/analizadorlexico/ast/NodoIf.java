package com.mycompany.analizadorlexico.ast;

import java.util.List;

import com.mycompany.analizadorlexico.GeneradorCodigo;

public class NodoIf extends NodoSentencia {
    private final NodoCondicion condicion;
    private final List<NodoSentencia> sentenciasThen;
    private final List<NodoSentencia> sentenciasElse; // puede ser null

    public NodoIf(NodoCondicion condicion,
                  List<NodoSentencia> sentenciasThen,
                  List<NodoSentencia> sentenciasElse) {
        super("IF");
        this.condicion = condicion;
        this.sentenciasThen = sentenciasThen;
        this.sentenciasElse = sentenciasElse;
    }

    @Override
    public void generarASM(java.io.PrintWriter pw, GeneradorCodigo gc) {
        // 1. Generamos nombres de etiquetas unicas para controlar el flujo
        String etiquetaElse = gc.nuevoAuxiliar().replace("@aux", "Etiq_Else_");
        String etiquetaFinal = gc.nuevoAuxiliar().replace("@aux", "Etiq_Final_");

        pw.println("; --- INICIO DE UN BLOQUE IF ---");
        
        // 2. Procesamos la condicion. Asumimos que NodoCondicion genera los FCOM/SAHF 
        // y nos retorna el salto que debemos dar si la condicion es FALSA.
        // Ejemplo: Si la condicion es (a > b), el salto falso seria JNA.
        String saltoFalso = this.condicion.generarSaltoFalso(pw, gc); 
        
        // Saltamos al bloque Else si la condicion no se cumple
        pw.println("  " + saltoFalso + " " + etiquetaElse);
        pw.println("; --- BLOQUE THEN ---");

        // 3. Traducimos las sentencias del bloque THEN
        for (NodoSentencia sentencia : this.sentenciasThen) {
            sentencia.generarASM(pw, gc);
        }

        // Si entramos al THEN, al terminar debemos esquivar el ELSE con un salto incondicional
        if (this.sentenciasElse != null) {
            pw.println("  jmp " + etiquetaFinal);
        }

        // 4. Estampamos el inicio del bloque ELSE
        pw.println(etiquetaElse + ":");

        // Traducimos las sentencias del bloque ELSE si es que existen
        if (this.sentenciasElse != null) {
            pw.println("; --- BLOQUE ELSE ---");
            for (NodoSentencia sentencia : this.sentenciasElse) {
                sentencia.generarASM(pw, gc);
            }
            // Estampamos el final definitivo para cuando salte el THEN
            pw.println(etiquetaFinal + ":");
        }
        
        pw.println("; --- FIN DEL BLOQUE IF ---");
        pw.println();
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        StringBuilder resultado = new StringBuilder();

        // Grafica el nodo IF
        resultado.append(super.graficar(idPadre));

        // Grafica la condición colgando directamente del nodo IF
        resultado.append(condicion.graficar(miId));

        // Agrega un nodo ficticio THEN colgando del nodo IF
        Nodo nodoThen = new Nodo("Then");
        resultado.append(nodoThen.graficar(miId));

        // Grafica las sentencias asociadas al "then" colgando del nodo ficticio THEN
        String idNodoThen = nodoThen.getIdNodo();
        for (NodoSentencia sentencia: sentenciasThen) {
            resultado.append(sentencia.graficar(idNodoThen));
        }

        // Si hay sentencias asociadas al "else"...
        if (sentenciasElse != null) {
            // Agrega un nodo ficticio "ELSE" colgando del nodo IF
            Nodo nodoElse = new Nodo("Else");
            resultado.append(nodoElse.graficar(miId));

            // Grafica las sentencias asociadas al "else" colgando del nodo ficticio ELSE
            String idNodoElse = nodoElse.getIdNodo();
            for (NodoSentencia sentencia: sentenciasElse) {
                resultado.append(sentencia.graficar(idNodoElse));
            }
        }

        return resultado.toString();
    }
}
