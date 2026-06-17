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

    public String getIdentificador() {
        return identificador;
    }

    @Override
    public String generarAssembler(StringBuilder asm, GeneradorAssemblerContext contexto) {
        return identificador;
    }

    @Override
    public String getDescripcionNodo() {
        return "ID: " + identificador;
    }
}
