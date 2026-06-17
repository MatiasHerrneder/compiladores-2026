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
            String tipo = expresion.getTipoSemantico();
            String nombre = expresion.generarAssembler(asm, contexto);

            if ("STRING".equals(tipo)) {
                asm.append("displayString ").append(nombre).append("\n");
                return;
            }

            if ("INT".equals(tipo)) {
                asm.append("FLD ").append(nombre).append("\n");
                asm.append("FISTP DWORD PTR _show_int_value\n");
                asm.append("DisplayInteger _show_int_value\n");
                return;
            }

            if ("FLOAT".equals(tipo)) {
                asm.append("DisplayFloat ").append(nombre).append(", 3\n");
                return;
            }

            asm.append("; SHOW no soporta el tipo ").append(tipo).append("\n");
        } catch (UnsupportedOperationException ex) {
            asm.append("; ").append(ex.getMessage()).append("\n");
        }
    }
}
