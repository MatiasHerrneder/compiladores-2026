package com.mycompany.analizadorlexico.ast;

public class NodoDeclaracion extends Nodo {
    private final String nombreVariable;
    private final String tipoDato; // "INT" o "FLOAT"

    public NodoDeclaracion(String nombreVariable, String tipoDato) {
        super("DECL");
        this.nombreVariable = nombreVariable;
        this.tipoDato = tipoDato;
    }

    public String getNombreVariable() { return nombreVariable; }
    public String getTipoDato() { return tipoDato; }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_decl_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"Decl: " + nombreVariable + " : " + tipoDato + "\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }
        return resultado.toString();
    }
}