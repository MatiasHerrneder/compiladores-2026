package com.mycompany.analizadorlexico.ast;

public class NodoAsignacion extends NodoSentencia {
    private final NodoVariable variable;
    private final NodoExpresion expresion;

    public NodoAsignacion(NodoVariable variable, NodoExpresion expresion) {
        super("ASIG");
        this.variable = variable;
        this.expresion = expresion;
    }

    public NodoVariable getVariable() { return variable; }
    public NodoExpresion getExpresion() { return expresion; }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_asig_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"=\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }
        resultado.append(this.variable.graficar(miId));
        resultado.append(this.expresion.graficar(miId));
        return resultado.toString();
    }
}