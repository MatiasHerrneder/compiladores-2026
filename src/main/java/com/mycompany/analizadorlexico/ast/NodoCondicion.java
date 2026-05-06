package com.mycompany.analizadorlexico.ast;

public class NodoCondicion extends Nodo {
    // Para comparaciones simples: expr OP expr
    private final NodoExpresion izquierda;
    private final NodoExpresion derecha;

    // Para condiciones compuestas: cond AND/OR/NOT cond
    private final NodoCondicion izqCondicion;
    private final NodoCondicion segundaCondicion;

    private final String operador;

    // expr OP expr
    public NodoCondicion(NodoExpresion izquierda, String operador, NodoExpresion derecha) {
        super("COND");
        this.izquierda     = izquierda;
        this.operador      = operador;
        this.derecha       = derecha;
        this.izqCondicion  = null;
        this.segundaCondicion = null;
    }

    // cond AND/OR cond
    public NodoCondicion(String operador, NodoCondicion izqCond, NodoCondicion derCond) {
        super("COND");
        this.izquierda     = null;
        this.derecha       = null;
        this.operador      = operador;
        this.izqCondicion  = izqCond;
        this.segundaCondicion = derCond;
    }

    // NOT cond
    public NodoCondicion(String operador, NodoCondicion cond) {
        super("COND");
        this.izquierda     = null;
        this.derecha       = null;
        this.operador      = operador;
        this.izqCondicion  = cond;
        this.segundaCondicion = null;
    }

    public NodoExpresion getIzquierda() { return izquierda; }
    public NodoExpresion getDerecha()   { return derecha; }
    public String getOperador()         { return operador; }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_cond_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();

        resultado.append(miId + " [label=\"" + operador + "\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }

        // Condición simple
        if (izquierda != null) resultado.append(izquierda.graficar(miId));
        if (derecha   != null) resultado.append(derecha.graficar(miId));

        // Condición compuesta
        if (izqCondicion     != null) resultado.append(izqCondicion.graficar(miId));
        if (segundaCondicion != null) resultado.append(segundaCondicion.graficar(miId));

        return resultado.toString();
    }
}