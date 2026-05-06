
package com.mycompany.analizadorlexico.ast;


public class NodoMenor extends NodoCondicion {
        
        public NodoMenor (NodoExpresion izquierda, NodoExpresion derecha) {
        super(izquierda, "<", derecha);
    }
}
