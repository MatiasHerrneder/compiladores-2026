package com.mycompany.analizadorlexico.ast;

import java.io.PrintWriter;

import com.mycompany.analizadorlexico.GeneradorCodigo;

public abstract class NodoExpresion extends Nodo {
    public NodoExpresion(String tipo) {
        super(tipo);
    }
    
    public abstract String getTipoSemantico();

    public String graficarConVariable (NodoVariable variable, String id) {
        return variable.graficar(id) + "\n" + this.graficar(id);
    }

    public abstract String generarASM(PrintWriter pw, GeneradorCodigo gc);
}