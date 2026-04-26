package com.mycompany.analizadorlexico.ast;

public class NodoAsignacion extends NodoSentencia {
    private final String nombreVariable;
    private final NodoExpresion expresion;

    public NodoAsignacion(String nombreVariable, NodoExpresion expresion) {
        super("ASIG");
        this.nombreVariable = nombreVariable;
        this.expresion = expresion;
    }

    public String getNombreVariable() { return nombreVariable; }
    public NodoExpresion getExpresion() { return expresion; }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_asig_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\":= (" + nombreVariable + ")\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }
        resultado.append(this.expresion.graficar(miId));
        return resultado.toString();
    }
}