package com.mycompany.analizadorlexico.ast;

public abstract class NodoExpresion extends Nodo {
    public NodoExpresion(String tipo) {
        super(tipo);
    }
    
    public abstract String getTipoSemantico();
}