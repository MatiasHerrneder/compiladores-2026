package com.mycompany.analizadorlexico.ast;

public class NodoExpresionBinaria extends NodoExpresion {
    private final NodoExpresion izquierda;
    private final String operador;
    private final NodoExpresion derecha;

    public NodoExpresionBinaria(NodoExpresion izquierda, String operador, NodoExpresion derecha) {
        super("EXP_BIN");
        this.izquierda = izquierda;
        this.operador = operador;
        this.derecha = derecha;
    }

    @Override
    public String getTipoSemantico() {
        String ti = izquierda.getTipoSemantico();
        String td = derecha.getTipoSemantico();
        if (ti == null || td == null) return null;
        if (ti.equals("FLOAT") || td.equals("FLOAT")) return "FLOAT";
        return "INT";
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_expbin_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"" + operador + "\"]\n");
        if (idPadre != null) resultado.append(idPadre + " -- " + miId + "\n");
        resultado.append(izquierda.graficar(miId));
        resultado.append(derecha.graficar(miId));
        return resultado.toString();
    }
}