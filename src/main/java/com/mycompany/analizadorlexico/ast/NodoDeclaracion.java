package com.mycompany.analizadorlexico.ast;

import java.util.ArrayList;

public class NodoDeclaracion extends Nodo {
    private final ArrayList<String> nombreVariable;
    private final String tipoDato;

    public NodoDeclaracion(ArrayList<String> nombreVariable, String tipoDato) {
        super("DECL");
        this.nombreVariable = nombreVariable;
        this.tipoDato = tipoDato;
    }

    public ArrayList<String> getNombreVariable() { return nombreVariable; }
    public String getTipoDato() { return tipoDato; }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_decl_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        String res = "Decl: "; 
        res += (tipoDato + " : ");
        for (String var : nombreVariable) {
            res += (var + ", ");
        }
        resultado.append(miId + " [label=\"Decl: " + res + "\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }
        return resultado.toString();
    }
}