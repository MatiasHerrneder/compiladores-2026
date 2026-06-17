package com.mycompany.analizadorlexico;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.mycompany.analizadorlexico.ast.*;

public class GeneradorCodigo {
    private NodoPrograma ast;
    private TablaSimbolos tablaSimbolos; // Reemplazá por el tipo real de tu symtbl
    private int contadorAuxiliares = 0;

    public GeneradorCodigo(NodoPrograma ast, TablaSimbolos tablaSimbolos) {
        this.ast = ast;
        this.tablaSimbolos = tablaSimbolos;
    }

    public void generar(String nombreArchivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            
            // 1. Buffer temporal para las instrucciones .CODE
            java.io.StringWriter swCode = new java.io.StringWriter();
            PrintWriter pwCode = new PrintWriter(swCode);

            // El recorrido del AST va a llenar 'contadorAuxiliares' 
            // y el Set de constantes en caliente de forma exacta
            this.ast.generarASM(pwCode, this); 

            // 2. Volcado del archivo final estructurado
            pw.println("include macros2.asm\t; Inclusión de macros");
            pw.println("include number.asm\t; Inclusión de macros");
            pw.println(".MODEL LARGE");
            pw.println(".386");
            pw.println("STACK 200h");
            pw.println();

            // =================================================================
            // SECCIÓN .DATA 100% DINÁMICA
            // =================================================================
            pw.println(".DATA");
            
            // Recorremos la estructura interna de tu tabla de símbolos real
            for (Map<COLUMNA, String> fila : this.tablaSimbolos.getSymtabla()) {
                String nombre = fila.get(COLUMNA.NOMBRE);
                String token = fila.get(COLUMNA.TOKEN);
                String valor = fila.get(COLUMNA.VALOR);
                String tipo = fila.get(COLUMNA.TIPO);

                if (nombre == null) continue;

                // CASO A: Es una variable declarada por el usuario (Token es "ID")
                if ("ID".equals(token)) {
                    pw.println("  _" + nombre.toLowerCase() + " \tdd \t?");
                } 
                
                // CASO B: Es una constante (Por ejemplo "INT_CONST", "FLOAT_CONST", etc.)
                else if (token != null && token.contains("CONST")) {
                    // Si el valor viene vacío, usamos el nombre como fallback
                    String valConst = (valor != null && !valor.isEmpty()) ? valor : nombre;
                    
                    // Adecuamos el formato para flotantes en assembler (intel exige punto decimal)
                    String formatoVal = valConst.contains(".") ? valConst : valConst + ".0";
                    
                    // Creamos el identificador reemplazando posibles puntos por guiones bajos (ej: _1_25)
                    String idAsm = "_" + valConst.replace(".", "_");
                    
                    pw.println("  " + idAsm + " \tdd \t" + formatoVal);
                }
            }
            
            // Registramos manualmente las variables fijas emuladas para #Iguales
            pw.println("  _comparador \tdd \t?");
            pw.println("  _contador   \tdd \t?");
            
            // CASO C: Constantes de control por si las moscas (0 y 1 para #Iguales)
            // Esto previene fallas si el usuario no las tipeó directamente en su código
            pw.println("  _0          \tdd \t0.0");
            pw.println("  _1          \tdd \t1.0");

            // D. Declarar exactamente la cantidad de auxiliares calculados en el recorrido
            for (int i = 1; i <= this.contadorAuxiliares; i++) {
                pw.println("  @aux" + i + " \tdd \t?");
            }
            pw.println();
            // =================================================================

            // Sección .CODE
            pw.println(".CODE");
            pw.println("START:");
            pw.println("  mov AX, @DATA");
            pw.println("  mov DS, AX");
            pw.println("  mov ES, AX");
            pw.println();

            // Volcamos las instrucciones generadas por los nodos
            pw.print(swCode.toString());

            // Cierre
            pw.println();
            pw.println("  mov ax, 4c00h");
            pw.println("  int 21h");
            pw.println("END START");

        } catch (IOException e) {
            System.err.println("Error al escribir el archivo ASM: " + e.getMessage());
        }
    }

    public String nuevoAuxiliar() {
        this.contadorAuxiliares++;
        return "@aux" + this.contadorAuxiliares;
    }
}