package com.mycompany.analizadorlexico.ast;

public class NodoConstanteHex extends NodoExpresion {
    private final String valorHex;   // ej: "F3A"
    private final int valorDecimal;  // ej: 3898

    public NodoConstanteHex(String valorHex, int valorDecimal) {
        super("HEX");
        this.valorHex = valorHex;
        this.valorDecimal = valorDecimal;
    }

    public String getValorHex() { return valorHex; }
    
    public int getValorDecimal() { return valorDecimal; }

    @Override
    public String getTipoSemantico() { return "INT"; }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_cte_hex_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"HEX: 0h" + valorHex + " (" + valorDecimal + ")\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }
        return resultado.toString();
    }
}
