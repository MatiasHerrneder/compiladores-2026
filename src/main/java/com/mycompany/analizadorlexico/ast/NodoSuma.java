package com.mycompany.analizadorlexico.ast;

public class NodoSuma extends NodoExpresionBinaria {

    public NodoSuma(NodoExpresion izquierda, NodoExpresion derecha) {
        super("+", izquierda, derecha);
    }
}