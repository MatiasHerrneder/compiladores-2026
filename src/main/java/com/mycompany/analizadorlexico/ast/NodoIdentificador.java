package com.mycompany.analizadorlexico.ast;

import com.mycompany.analizadorlexico.GeneradorCodigo;

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

    @Override
    public String generarASM(java.io.PrintWriter pw, GeneradorCodigo gc) {
        // Retorna el identificador con el prefijo estandar para el archivo .asm
        return "_" + this.identificador;
    }
}
