package com.mycompany.analizadorlexico.ast;

import java.util.List;

public class NodoRepeat extends NodoSentencia {
    private final List<NodoSentencia> sentencias;
    private final NodoCondicion condicion;

    public NodoRepeat(List<NodoSentencia> sentencias, NodoCondicion condicion) {
        super("REPEAT");
        this.sentencias = sentencias;
        this.condicion = condicion;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_repeat_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"REPEAT\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }

        for (NodoSentencia s : sentencias) {
            resultado.append(s.graficar(miId));
        }
        resultado.append(this.condicion.graficar(miId));

        return resultado.toString();
    }
}


