package com.mycompany.analizadorlexico.ast;

import java.io.PrintWriter;

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
    public String generarASM(PrintWriter pw, GeneradorCodigo gc) {
        // Asegura que devuelva el ID en minúsculas (ej: _msg, _a)
        return "_" + this.identificador.toLowerCase();
    }
}
