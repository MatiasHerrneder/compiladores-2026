package com.mycompany.analizadorlexico.ast;

public class NodoConstanteString extends NodoExpresion {
    private final String valor;

    public NodoConstanteString(String valor) {
        super("CTE");
        this.valor = valor;
    }

    @Override
    public String getDescripcionNodo() {
        return "CTE: " + valor;
    }
}
