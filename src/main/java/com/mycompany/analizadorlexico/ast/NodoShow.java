package com.mycompany.analizadorlexico.ast;

public class NodoShow extends NodoSentencia {
    private final NodoExpresion expresion;

    public NodoShow(NodoExpresion expresion) {
        super("SHOW");
        this.expresion = expresion;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_show_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(this.expresion.graficar(miId));
        resultado.append(miId + " [label=\"SHOW\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }
        return resultado.toString();
    }

    @Override
    public void generarAssembler(StringBuilder asm, GeneradorAssemblerContext contexto) {
        try {
            if (!"STRING".equals(expresion.getTipoSemantico())) {
                asm.append("; TODO SHOW solo soporta STRING en esta primera version\n");
                return;
            }

            String nombre = expresion.generarAssembler(asm, contexto);
            asm.append("MOV DX, OFFSET ").append(nombre).append("\n");
            asm.append("MOV AH, 9\n");
            asm.append("INT 21h\n");
        } catch (UnsupportedOperationException ex) {
            asm.append("; ").append(ex.getMessage()).append("\n");
        }
    }
}
