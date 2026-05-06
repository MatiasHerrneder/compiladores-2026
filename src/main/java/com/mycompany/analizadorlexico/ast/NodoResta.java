package com.mycompany.analizadorlexico.ast;

public class NodoResta extends NodoExpresionBinaria {

    public NodoResta(NodoExpresion izquierda, NodoExpresion derecha) {
        super(izquierda, "-", derecha);
    }
}
