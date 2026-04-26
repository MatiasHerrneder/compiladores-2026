package com.mycompany.analizadorlexico.ast;

public class NodoConstanteFloat extends NodoExpresion {
    private final float valor;

    public NodoConstanteFloat(float valor) {
        super("CTE");
        this.valor = valor;
    }

    @Override
    public String getDescripcionNodo() {
        return "CTE: " + Float.toString(valor);
    }
}
