package com.mycompany.analizadorlexico.ast;

import java.io.PrintWriter;

import com.mycompany.analizadorlexico.GeneradorCodigo;

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
    public String generarASM(PrintWriter pw, GeneradorCodigo gc) {
        // Convierte el float a String y reemplaza el punto por un guion bajo (ej: _3_14)
        return "_" + String.valueOf(this.valor).replace(".", "_");
    }

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
