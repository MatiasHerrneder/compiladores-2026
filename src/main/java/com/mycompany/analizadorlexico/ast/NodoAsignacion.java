package com.mycompany.analizadorlexico.ast;

import java.io.PrintWriter;

import com.mycompany.analizadorlexico.GeneradorCodigo;

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
    public void generarASM(PrintWriter pw, GeneradorCodigo gc) {
        // 1. Primero resolvemos de forma recursiva toda la expresión matemática de la derecha.
        // Como 'generarASM' en expresiones devuelve el ID de dónde quedó el resultado (un @aux o cte)...
        String resultadoDerecho = this.expresion.generarASM(pw, gc);
        
        // 2. Traemos el nombre de la variable de destino (ej: _id)
        String destino = this.variable.generarASM(pw, gc);
        
        // 3. Escribimos la instrucción de la FPU para mover el valor final al ID de destino
        pw.println("  fld " + resultadoDerecho + "\t\t; Carga el resultado final de la expresion"); // fld [cite: 317]
        pw.println("  fstp " + destino + "\t\t; Asigna sacando el valor de la FPU hacia la variable"); // fstp [cite: 317, 643]
        pw.println();
    }

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
}