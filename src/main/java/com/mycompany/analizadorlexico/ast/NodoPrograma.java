package com.mycompany.analizadorlexico.ast;

import com.mycompany.analizadorlexico.COLUMNA;
import com.mycompany.analizadorlexico.TablaSimbolos;
import java.util.List;
import java.io.PrintWriter;
import com.mycompany.analizadorlexico.GeneradorCodigo; // Asegurate de que el import sea correcto

public class NodoPrograma extends Nodo {
    // private final NodoDecVar decVar;
    private final List<NodoSentencia> sentencias;
    private TablaSimbolos tablaSimbolos;

    public NodoPrograma(/*NodoDecVar decVar, */List<NodoSentencia> sentencias) {
        super("PGM");
        // this.decVar = decVar;
        this.sentencias = sentencias;
    }

    public void generarASM(PrintWriter pw, GeneradorCodigo gc) {
        for (NodoSentencia sentencia : this.sentencias) {
            // Cada sentencia (Asignación, IF, REPEAT) escribirá su bloque de código
            sentencia.generarASM(pw, gc); 
        }
    }

    public List<NodoSentencia> getSentencias() {
        return this.sentencias;
    }

    public String graficar() {
        return this.graficar(null);
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_programa";
        StringBuilder resultado = new StringBuilder();
        resultado.append("graph G {");
        resultado.append(miId + " [label=\"Programa\"]\n");
        
        for (NodoSentencia sentencia : this.sentencias) {
            resultado.append(sentencia.graficar(miId));
        }

        resultado.append("}");
        return resultado.toString();
    }
}