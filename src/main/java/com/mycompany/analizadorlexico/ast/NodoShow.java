package com.mycompany.analizadorlexico.ast;

public class NodoShow extends NodoSentencia {
    private final NodoExpresion expresion;

    public NodoShow(NodoExpresion expresion) {
        super("SHOW");
        this.expresion = expresion;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_show_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"SHOW\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }
        resultado.append(this.expresion.graficar(miId));
        return resultado.toString();
    }
}