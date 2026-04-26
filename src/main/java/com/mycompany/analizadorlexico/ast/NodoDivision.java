package com.mycompany.analizadorlexico.ast;

public class NodoDivision extends NodoExpresionBinaria {

    public NodoDivision(NodoExpresion izquierda, NodoExpresion derecha) {
        super("/", izquierda, derecha);
    }
}
