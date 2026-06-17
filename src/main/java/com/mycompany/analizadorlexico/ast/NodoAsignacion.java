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
        String idDer = this.expresion.generarASM(pw, gc); 
        String idIzq = "_" + this.variable.getNombre().toLowerCase(); 

        // Si es un STRING, no emitimos las instrucciones mov que causan el descalce de bits
        if ("STRING".equals(this.expresion.getTipoSemantico())) {
            pw.println("; --- ASIGNACION DE STRING OMITIDA (USO DE LITERAL EN HOJAS) ---");
        } 
        // Si es un INT o FLOAT, va por la FPU tradicional que anda perfecto
        else {
            pw.println("  fld " + idDer + "\t\t; Carga el resultado final de la expresion");
            pw.println("  fstp " + idIzq + "\t\t; Asigna sacando el valor de la FPU");
        }
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