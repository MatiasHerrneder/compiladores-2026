package com.mycompany.analizadorlexico.ast;

public class NodoConstanteFloat extends NodoExpresion {
    private final float valor;

    public NodoConstanteFloat(float valor) {
        super("FLOAT");
        this.valor = valor;
    }

    public float getValor() { return valor; }

    @Override
    public String getTipoSemantico() { return "FLOAT"; }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_cte_f_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"CTE_F: " + valor + "\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }
        return resultado.toString();
    }
}