package com.mycompany.analizadorlexico.ast;

import java.io.PrintWriter;

import com.mycompany.analizadorlexico.GeneradorCodigo;

public class NodoVariable extends NodoExpresion {
    private final String nombre;
    private String tipoSemantico;

    public NodoVariable(String nombre) {
        super("VAR");
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setTipoSemantico(String tipo) {
        this.tipoSemantico = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String getTipoSemantico() { return tipoSemantico; }

    @Override
    public String generarASM(PrintWriter pw, GeneradorCodigo gc) {
        // Asegura que devuelva el ID en minúsculas (ej: _msg, _a)
        return "_" + this.nombre.toLowerCase();
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_var_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"Var: " + nombre + "\"]\n");
        if (idPadre != null) resultado.append(idPadre + " -- " + miId + "\n");
        return resultado.toString();
    }
}
