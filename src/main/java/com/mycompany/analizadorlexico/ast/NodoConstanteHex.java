package com.mycompany.analizadorlexico.ast;

import java.io.PrintWriter;

import com.mycompany.analizadorlexico.GeneradorCodigo;

public class NodoConstanteHex extends NodoExpresion {
    private final int valorDecimal;  // ej: 3898

    public NodoConstanteHex(int valorDecimal) {
        super("HEX");
        this.valorDecimal = valorDecimal;
    }

    public String getValorHex() { return Integer.toHexString(this.valorDecimal).toUpperCase(); }

    public int getValorDecimal() { return valorDecimal; }

    @Override
    public String getTipoSemantico() { return "HEX"; }

    @Override
    public String generarASM(PrintWriter pw, GeneradorCodigo gc) {
        // Las constantes se mapean en la Tabla de Símbolos con prefijo
        return "_" + this.valorDecimal; // Ejemplo: devuelve "_20"
    }

    @Override
    public String generarAssembler(StringBuilder asm, GeneradorAssemblerContext contexto) {
        return contexto.nombreConstanteHex(valorHex);
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_cte_hex_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"HEX: 0h" + this.getValorHex() + " (" + this.getValorDecimal() + ")\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }
        return resultado.toString();
    }
}
