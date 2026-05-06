package com.mycompany.analizadorlexico.ast;

import java.util.List;

public class NodoDecVar extends Nodo {
    private final List<NodoDeclaracion> declaraciones;

    public NodoDecVar(List<NodoDeclaracion> declaraciones) {
        super("DECVAR");
        this.declaraciones = declaraciones;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_decvar_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"DECVAR\"]\n");

        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }
        for (NodoDeclaracion decl : this.declaraciones) {
            resultado.append(decl.graficar(miId));
        }
        return resultado.toString();
    }
}