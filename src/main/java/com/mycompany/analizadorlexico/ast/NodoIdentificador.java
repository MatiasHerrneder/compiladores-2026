package com.mycompany.analizadorlexico.ast;

public class NodoIdentificador extends NodoExpresion {
    private final String identificador;

    public NodoIdentificador(String identificador) {
        super("ID");
        this.identificador = identificador;
    }

    @Override
    public String getTipoSemantico() {
        return "ID";
    }

    @Override
    public String getDescripcionNodo() {
        return "ID: " + identificador;
    }
}
