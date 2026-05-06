package com.mycompany.analizadorlexico.ast;

public class NodoVariable extends NodoExpresion {
    private final String nombre;
    private String tipoSemantico;

    public NodoVariable(String nombre) {
        super("VAR");
        this.nombre = nombre;
    }

    public void setTipoSemantico(String tipo) {
        this.tipoSemantico = tipo;
    }

    @Override
    public String getTipoSemantico() { return tipoSemantico; }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_var_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"Var: " + nombre + "\"]\n");
        if (idPadre != null) resultado.append(idPadre + " -- " + miId + "\n");
        return resultado.toString();
    }
}