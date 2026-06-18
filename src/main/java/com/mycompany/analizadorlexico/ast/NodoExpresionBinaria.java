package com.mycompany.analizadorlexico.ast;

import java.io.PrintWriter;

import com.mycompany.analizadorlexico.GeneradorCodigo;

public class NodoExpresionBinaria extends NodoExpresion {
    private final NodoExpresion izquierda;
    private final String operador;
    private final NodoExpresion derecha;

    public NodoExpresionBinaria(NodoExpresion izquierda, String operador, NodoExpresion derecha) {
        super("EXP_BIN");
        this.izquierda = izquierda;
        this.operador = operador;
        this.derecha = derecha;
    }

    @Override
    public String getTipoSemantico() {
        String ti = izquierda.getTipoSemantico();
        String td = derecha.getTipoSemantico();
        if (ti == null || td == null) return null;
        if (ti.equals("FLOAT") || td.equals("FLOAT")) return "FLOAT";
        return "INT";
    }

    public NodoExpresion getIzquierda() {
        return izquierda;
    }

    public String getOperador() {
        return operador;
    }

    public NodoExpresion getDerecha() {
        return derecha;
    }

    @Override
    public String generarAssembler(StringBuilder asm, GeneradorAssemblerContext contexto) {
        if ("STRING".equals(getTipoSemantico())) {
            throw new UnsupportedOperationException("No se soportan operaciones aritmeticas sobre STRING");
        }

        String nombreIzquierda = izquierda.generarAssembler(asm, contexto);
        String nombreDerecha = derecha.generarAssembler(asm, contexto);
        String temporal = contexto.nuevoTemporal(getTipoSemantico());

        asm.append("FLD ").append(nombreIzquierda).append("\n");

        switch (operador) {
            case "+":
                asm.append("FADD ").append(nombreDerecha).append("\n");
                break;
            case "-":
                asm.append("FSUB ").append(nombreDerecha).append("\n");
                break;
            case "*":
                asm.append("FMUL ").append(nombreDerecha).append("\n");
                break;
            case "/":
                asm.append("FDIV ").append(nombreDerecha).append("\n");
                break;
            default:
                throw new UnsupportedOperationException("Operador no soportado: " + operador);
        }

        asm.append("FSTP ").append(temporal).append("\n");
        return temporal;
    }

    @Override
    public String generarASM(PrintWriter pw, GeneradorCodigo gc) {
        // 1. Post-orden recursivo: Resolvemos primero los hijos de abajo 
        String idIzq = this.izquierda.generarASM(pw, gc);
        String idDer = this.derecha.generarASM(pw, gc);
        
        // 2. Solicitamos un nuevo nombre de variable auxiliar (ej: @aux1) 
        String aux = gc.nuevoAuxiliar();
        
        // 3. Volcamos las instrucciones usando las reglas del Coprocesador Matemático
        pw.println("  fld " + idIzq + "\t\t; Carga operando izquierdo a ST(0)");
        pw.println("  fld " + idDer + "\t\t; Carga operando derecho a ST(0), desplazando el anterior");
        
        switch(this.operador) {
            case "+":
                pw.println("  fadd\t\t\t; Suma ST(1) + ST(0) y hace pop");
                break;
            case "-":
                pw.println("  fsub\t\t\t; Resta ST(1) - ST(0) y hace pop");
                break;
            case "*":
                pw.println("  fmul\t\t\t; Multiplica ST(1) * ST(0) y hace pop");
                break;
            case "/":
                pw.println("  fdiv\t\t\t; Divide ST(1) / ST(0) y hace pop");
                break;
        }
        
        // 4. Sacamos el resultado de la pila de la FPU y lo guardamos en la variable auxiliar
        pw.println("  fstp " + aux + "\t\t; Descarga el resultado en el temporal");
        pw.println();

        // 5. Devolvemos el nombre del auxiliar para que el nodo padre sepa de dónde leer este resultado
        return aux; 
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_expbin_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"" + operador + "\"]\n");
        if (idPadre != null) resultado.append(idPadre + " -- " + miId + "\n");
        resultado.append(izquierda.graficar(miId));
        resultado.append(derecha.graficar(miId));
        return resultado.toString();
    }
}
