package com.mycompany.analizadorlexico.ast;

public class NodoAsignacion extends NodoSentencia {
    private final NodoVariable variable;
    private final NodoExpresion expresion;

    public NodoAsignacion(NodoVariable variable, NodoExpresion expresion) {
        super("ASIG");
        this.variable = variable;
        this.expresion = expresion;
    }

    public NodoVariable getVariable() { return variable; }
    public NodoExpresion getExpresion() { return expresion; }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_asig_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        //resultado.append(this.variable.graficar(miId));
        resultado.append(this.expresion.graficarConVariable(this.variable, miId));
        resultado.append(miId + " [label=\"=\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }
        return resultado.toString();
    }

    @Override
    public void generarAssembler(StringBuilder asm, GeneradorAssemblerContext contexto) {
        try {
            if ("STRING".equals(expresion.getTipoSemantico())) {
                String origen = expresion.generarAssembler(asm, contexto);
                String etiquetaCopiar = contexto.nuevaEtiqueta();

                asm.append("LEA SI, ").append(origen).append("\n");
                asm.append("LEA DI, ").append(variable.getNombre()).append("\n");
                asm.append(etiquetaCopiar).append(":\n");
                asm.append("MOV AL, [SI]\n");
                asm.append("MOV [DI], AL\n");
                asm.append("INC SI\n");
                asm.append("INC DI\n");
                asm.append("CMP AL, '$'\n");
                asm.append("JNE ").append(etiquetaCopiar).append("\n");
                return;
            }

            String resultadoExpresion = expresion.generarAssembler(asm, contexto);
            asm.append("FLD ").append(resultadoExpresion).append("\n");
            asm.append("FSTP ").append(variable.getNombre()).append("\n");
        } catch (UnsupportedOperationException ex) {
            asm.append("; ").append(ex.getMessage()).append("\n");
        }
    }
}
