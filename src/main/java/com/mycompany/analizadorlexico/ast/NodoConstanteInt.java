package com.mycompany.analizadorlexico.ast;

public class NodoConstanteInt extends NodoExpresion {
    private final int valor;

    public NodoConstanteInt(int valor) {
        super("INT");
        this.valor = valor;
    }

    public int getValor() { return valor; }

    @Override
    public String getTipoSemantico() { return "INT"; }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_cte_i_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"CTE_I: " + valor + "\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }
        return resultado.toString();
    }
}
