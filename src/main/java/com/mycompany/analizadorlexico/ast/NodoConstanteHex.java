package com.mycompany.analizadorlexico.ast;

public class NodoConstanteHex extends NodoExpresion {
    private final int valor;

    public NodoConstanteHex(int valor) {
        super("CTE");
        this.valor = valor;
    }

    @Override
    public String getDescripcionNodo() {
        return "CTE: " + Integer.toString(valor);
    }
}
