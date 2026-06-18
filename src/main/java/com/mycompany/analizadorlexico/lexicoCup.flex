package com.mycompany.analizadorlexico;
import java_cup.runtime.*;
import java.util.*;

import com.mycompany.analizadorlexico.TablaSimbolos;


%%

%cup
%public
%class Lexico
%unicode
%line
%column
%implements java.io.Serializable

// ---------- Definiciones de macros ----------
WHITESPACE = [ \t\f\r\n]+
LETTER     = [a-zA-Z]
DIGIT      = [0-9]
ID         = {LETTER}({LETTER}|{DIGIT})*
INT        = {DIGIT}+
FLOAT      = ({DIGIT}+\.{DIGIT}*|{DIGIT}*\.{DIGIT}+)
HEX        = 0[hH][0-9a-fA-F]+
STRING     = \"([^\"\\]|\\.)*\"

%{
/**
*   Instancia de tabla de simbolos
*
*/
TablaSimbolos symtbl = new TablaSimbolos("ts.txt");


%}

%%

// ---------- Palabras reservadas ----------
"REPEAT"             { return new Symbol(sym.REPEAT, yyline, yycolumn, yytext()); }
"UNTIL"              { return new Symbol(sym.UNTIL, yyline, yycolumn, yytext()); }
"IF"                 { return new Symbol(sym.IF, yyline, yycolumn, yytext()); }
"THEN"               { return new Symbol(sym.THEN, yyline, yycolumn, yytext()); }
"ELSE"               { return new Symbol(sym.ELSE, yyline, yycolumn, yytext()); }
"ENDIF"              { return new Symbol(sym.ENDIF, yyline, yycolumn, yytext()); }
"DECVAR"             { return new Symbol(sym.DECVAR, yyline, yycolumn, yytext()); }
"ENDDECVAR"          { return new Symbol(sym.ENDDECVAR, yyline, yycolumn, yytext()); }
"PROGRAM.SECTION"    { return new Symbol(sym.PROGRAM_SECTION, yyline, yycolumn, yytext()); }
"ENDPROGRAM.SECTION" { return new Symbol(sym.END_PROGRAM_SECTION, yyline, yycolumn, yytext()); }
"INT"                { return new Symbol(sym.TYPE_INT, yyline, yycolumn, yytext()); }
"FLOAT"              { return new Symbol(sym.TYPE_FLOAT, yyline, yycolumn, yytext()); }
"STRING"             { return new Symbol(sym.TYPE_STRING, yyline, yycolumn, yytext()); }
"HEX"                { return new Symbol(sym.TYPE_HEX, yyline, yycolumn, yytext());}
"show"               { return new Symbol(sym.SHOW, yyline, yycolumn, yytext()); }

// ---------- Operadores y símbolos ----------
"=="   { return new Symbol(sym.EQ, yyline, yycolumn, yytext()); }
"!="   { return new Symbol(sym.NEQ, yyline, yycolumn, yytext()); }
"<="   { return new Symbol(sym.LE, yyline, yycolumn, yytext()); }
">="   { return new Symbol(sym.GE, yyline, yycolumn, yytext()); }
"<"    { return new Symbol(sym.LT, yyline, yycolumn, yytext()); }
">"    { return new Symbol(sym.GT, yyline, yycolumn, yytext()); }
"&&"   { return new Symbol(sym.AND, yyline, yycolumn, yytext()); }
"||"   { return new Symbol(sym.OR, yyline, yycolumn, yytext()); }
"!"    { return new Symbol(sym.NOT, yyline, yycolumn, yytext()); }
"+"    { return new Symbol(sym.MAS, yyline, yycolumn, yytext()); }
"-"    { return new Symbol(sym.MENOS, yyline, yycolumn, yytext()); }
"*"    { return new Symbol(sym.MULT, yyline, yycolumn, yytext()); }
"/"    { return new Symbol(sym.DIV, yyline, yycolumn, yytext()); }
"="    { return new Symbol(sym.ASSIGN, yyline, yycolumn, yytext()); }
"["    { return new Symbol(sym.LBRACK, yyline, yycolumn, yytext()); }
"]"    { return new Symbol(sym.RBRACK, yyline, yycolumn, yytext()); }
"("    { return new Symbol(sym.LPAREN, yyline, yycolumn, yytext()); }
")"    { return new Symbol(sym.RPAREN, yyline, yycolumn, yytext()); }
","    { return new Symbol(sym.COMA, yyline, yycolumn, yytext()); }
"#Iguales" { return new Symbol(sym.FUNC_IGUALES, yyline, yycolumn, yytext()); }

// ---------- Literales ----------
{INT} {
    try {
        int valor = Integer.parseInt(yytext());
        if (valor < -32768 || valor > 32767) {
            throw new Error("ERROR: INT fuera de rango (" + yytext() + 
                ") en línea " + yyline);
        }
        symtbl.agregarSimbolo("_" + yytext(), "INT_CONST", yytext(), null);
        // System.out.println("Token INT_CONST encontrado, Lexema " + yytext());
        return new Symbol(sym.INT_CONST, yyline, yycolumn, valor);
    } catch (NumberFormatException e) {
        throw new Error("ERROR: INT inválido: " + yytext() + " en línea " + yyline);
    }
}   
{FLOAT} {
    double v = Double.parseDouble(yytext());
    if (Math.abs(v) > 1000000) {
        throw new Error("ERROR: FLOAT fuera de rango (" + yytext() + 
            ") en línea " + yyline);
    }
    symtbl.agregarSimbolo("_" + yytext(), "FLOAT_CONST", yytext(), null);
    // System.out.println("Token FLOAT_CONST encontrado, Lexema " + yytext());
    return new Symbol(sym.FLOAT_CONST, yyline, yycolumn, v);
}
{STRING} {
    if (yytext().length() > 255) {
        throw new Error("ERROR: STRING demasiado largo (" + yytext().length() +
            " caracteres) en línea " + yyline);
    }
    symtbl.agregarSimbolo("_" + yytext(), "STRING_CONST", yytext(), yytext().length());
    // System.out.println("Token STRING_CONST encontrado, Lexema " + yytext());
    return new Symbol(sym.STRING_CONST, yyline, yycolumn, yytext());
}
{HEX} {
    int value = Integer.parseInt(yytext().substring(2), 16);
    if (value > 0xFFFF) {
        throw new Error("ERROR: HEX fuera de rango (" + yytext() + 
            ") en línea " + yyline);
    }
    symtbl.agregarSimbolo(yytext(), "HEX_CONST", String.valueOf(value), null);
    // System.out.println("Token HEX_CONST encontrado, Lexema " + yytext());
    return new Symbol(sym.HEX_CONST, yyline, yycolumn, value);
}

// ---------- Identificadores ----------
{ID}     { 
  if (yytext().length() > 255) {
      throw new Error("ERROR: ID demasiado largo (" + yytext().length() +
          " caracteres) en línea " + yyline);
  }
  symtbl.agregarSimbolo(yytext(), "ID", "-", null);
  return new Symbol(sym.ID, yyline, yycolumn, yytext());
}

// ---------- Comentarios ----------
"//".* { /* se ignora */ }
"/*"([^*]|(\*+[^*/]))*"*/" { /* se ignora */ }

// ---------- Espacios ----------
{WHITESPACE} { /* se ignora */ }

// ---------- Error léxico ----------
. { throw new Error("Caracter no permitido: <" + yytext() + "> en la linea " + yyline); }

<<EOF>> { return null; }