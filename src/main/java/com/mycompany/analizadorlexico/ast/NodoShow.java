package com.mycompany.analizadorlexico.ast;

import com.mycompany.analizadorlexico.GeneradorCodigo;

public class NodoShow extends NodoSentencia {
    private final NodoExpresion expresion;

    public NodoShow(NodoExpresion expresion) {
        super("SHOW");
        this.expresion = expresion;
    }

    @Override
    public void generarASM(java.io.PrintWriter pw, GeneradorCodigo gc) {
        pw.println("; --- SENTENCIA SHOW (IMPRESION) ---");
        
        // 1. Resolvemos la expresion interna para saber de que variable o temporal leer el valor
        String idValor = this.expresion.generarASM(pw, gc);
        
        // 2. Verificamos el tipo semantico para delegar a la macro correspondiente
        String tipo = this.expresion.getTipoSemantico();
        
        if ("STRING".equals(tipo)) {
            // Segun la clase practica, para strings se carga el offset en DX y se llama a la int 21h (ah=9)
            pw.println("  mov dx, OFFSET " + idValor + "\t; Carga la direccion de la cadena");
            pw.println("  mov ah, 9\t\t\t; Funcion de DOS para imprimir string");
            pw.println("  int 21h\t\t\t; Interrupcion de sistema");
        } else {
            // Para reales/enteros mapeados en la FPU, tus apuntes muestran el uso de la macro DisplayFloat
            // Ejemplo de clase: DisplayFloat aa, 2 (donde aa es la variable y 2 los decimales)
            pw.println("  DisplayFloat " + idValor + ", 2\t; Invoca macro de impresion para numeros");
        }
        pw.println();
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
}