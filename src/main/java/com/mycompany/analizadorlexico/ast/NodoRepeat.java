package com.mycompany.analizadorlexico.ast;

import java.util.List;

import com.mycompany.analizadorlexico.GeneradorCodigo;

public class NodoRepeat extends NodoSentencia {
    private final List<NodoSentencia> sentencias;
    private final NodoCondicion condicion;

    public NodoRepeat(List<NodoSentencia> sentencias, NodoCondicion condicion) {
        super("REPEAT");
        this.sentencias = sentencias;
        this.condicion = condicion;
    }

    @Override
    public void generarASM(java.io.PrintWriter pw, GeneradorCodigo gc) {
        // 1. Creamos una etiqueta unica para marcar el inicio del bucle REPEAT
        String etiquetaInicio = gc.nuevoAuxiliar().replace("@aux", "Etiq_Inicio_Repeat_");

        pw.println("; --- INICIO DE UN BUCLE REPEAT ---");
        pw.println(etiquetaInicio + ":");

        // 2. Traducimos recursivamente el cuerpo de sentencias del bucle
        for (NodoSentencia s : this.sentencias) {
            s.generarASM(pw, gc);
        }

        pw.println("; --- EVALUACION DE CONDICION UNTIL ---");
        
        // 3. Evaluamos la condicion. Generara los fld, fcom, fstsw ax, sahf.
        // Y nos devuelve el salto condicional para cuando la condicion NO se cumple (FALSA).
        String saltoFalso = this.condicion.generarSaltoFalso(pw, gc);

        // 4. Si la condicion es falsa (UNTIL no alcanzado), saltamos de nuevo al inicio
        pw.println("  " + saltoFalso + " " + etiquetaInicio);
        pw.println("; --- FIN DEL BUCLE REPEAT ---");
        pw.println();
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_repeat_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"REPEAT\"]\n");
        resultado.append(this.condicion.graficarRotado(miId));
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }

        for (NodoSentencia s : sentencias) {
            resultado.append(s.graficar(miId));
        }

        return resultado.toString();
    }
}


