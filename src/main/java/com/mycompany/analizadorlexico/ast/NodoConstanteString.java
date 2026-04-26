package com.mycompany.analizadorlexico.ast;

public class NodoConstanteString extends NodoExpresion {
    private final String valor;

    public NodoConstanteString(String valor) {
        super("CTE_STR");
        // Si el valor viene con comillas, las sacamos
        if (valor.startsWith("\"") && valor.endsWith("\"")) {
            this.valor = valor.substring(1, valor.length() - 1);
        } else {
            this.valor = valor;
        }
    }

    public String getValor() { return valor; }

    @Override
    public String getTipoSemantico() { return "STRING"; }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_cte_str_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();

        // Usar comillas simples para el valor, evitar conflicto con el DOT
        resultado.append(miId + " [label=\"CTE_STR: '" + valor + "'\"]\n");

        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }
        return resultado.toString();
    }
}