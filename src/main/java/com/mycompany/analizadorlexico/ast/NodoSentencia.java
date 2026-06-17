package com.mycompany.analizadorlexico.ast;

public abstract class NodoSentencia extends Nodo {
    public NodoSentencia(String tipo) {
        super(tipo);
    }

    public void generarAssembler(StringBuilder asm, GeneradorAssemblerContext contexto) {
        asm.append("; TODO assembler para ").append(getClass().getSimpleName()).append("\n");
    }
}
