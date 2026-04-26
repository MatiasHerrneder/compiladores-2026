package com.mycompany.analizadorlexico.ast;

public class NodoConstanteInt extends NodoExpresion {
    private final int valor;

    public NodoConstanteInt(int valor) {
        super("CTE");
        this.valor = valor;
    }

    @Override
    public String getDescripcionNodo() {
        return "CTE: " + Integer.toString(valor);
    }
}
