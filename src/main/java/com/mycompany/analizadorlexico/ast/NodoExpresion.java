package com.mycompany.analizadorlexico.ast;

public abstract class NodoExpresion extends Nodo {
    public NodoExpresion(String tipo) {
        super(tipo);
    }
    
    public abstract String getTipoSemantico();

    public String graficarConVariable (NodoVariable variable, String id) {
        return variable.graficar(id) + "\n" + this.graficar(id);
    }

    public String generarAssembler(StringBuilder asm, GeneradorAssemblerContext contexto) {
        throw new UnsupportedOperationException(
            "Expresion no soportada para assembler: " + getClass().getSimpleName()
        );
    }
}
