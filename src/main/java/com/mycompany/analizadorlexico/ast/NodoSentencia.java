package com.mycompany.analizadorlexico.ast;

public class NodoSentencia extends Nodo {

    public NodoSentencia(String nombre) {
        super(nombre);
    }

    public String getTipo () {
        return super.getDescripcionNodo();
    }
}
