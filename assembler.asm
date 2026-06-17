include macros2.asm
include number.asm

.MODEL LARGE
.386
.STACK 200h

.DATA

_show_int_value dd ?
a dd ?
b dd ?
c dd ?
x dd ?
msg db 255 dup ('$')
cte_int_10 dd 10.0
cte_int_15 dd 15.0
cte_float_3_14 dd 3.14
cte_str_425804429 db "Hola mundo",'$'
cte_int_5 dd 5.0
cte_int_2 dd 2.0
cte_int_3 dd 3.0
_aux_1 dd ?
_aux_2 dd ?
_aux_3 dd ?
_aux_4 dd ?

.CODE



MAIN PROC

MOV AX,@DATA
MOV DS,AX
MOV ES,AX

FLD cte_int_10
FSTP a
FLD cte_int_15
FSTP b
FLD cte_float_3_14
FSTP x
LEA SI, cte_str_425804429
LEA DI, msg
ETIQ_1:
MOV AL, [SI]
MOV [DI], AL
INC SI
INC DI
CMP AL, '$'
JNE ETIQ_1
; #Iguales: evaluar pivote una unica vez
; #Iguales: inicializar contador en 0
FLDZ
FSTP _aux_1
; #Iguales: comparar pivote con un elemento de la lista
FLD b
FSUB cte_int_5
FSTP _aux_2
FLD a
FCOMP _aux_2
FSTSW AX
SAHF
JE ETIQ_2
JMP ETIQ_3
ETIQ_2:
; #Iguales: si son iguales, incrementar contador
FLD1
FADD _aux_1
FSTP _aux_1
ETIQ_3:
; #Iguales: comparar pivote con un elemento de la lista
FLD a
FCOMP cte_int_5
FSTSW AX
SAHF
JE ETIQ_4
JMP ETIQ_5
ETIQ_4:
; #Iguales: si son iguales, incrementar contador
FLD1
FADD _aux_1
FSTP _aux_1
ETIQ_5:
; #Iguales: comparar pivote con un elemento de la lista
FLD a
FCOMP cte_int_2
FSTSW AX
SAHF
JE ETIQ_6
JMP ETIQ_7
ETIQ_6:
; #Iguales: si son iguales, incrementar contador
FLD1
FADD _aux_1
FSTP _aux_1
ETIQ_7:
; #Iguales: comparar pivote con un elemento de la lista
FLD a
FCOMP cte_int_3
FSTSW AX
SAHF
JE ETIQ_8
JMP ETIQ_9
ETIQ_8:
; #Iguales: si son iguales, incrementar contador
FLD1
FADD _aux_1
FSTP _aux_1
ETIQ_9:
; #Iguales: comparar pivote con un elemento de la lista
FLD a
FCOMP cte_int_10
FSTSW AX
SAHF
JE ETIQ_10
JMP ETIQ_11
ETIQ_10:
; #Iguales: si son iguales, incrementar contador
FLD1
FADD _aux_1
FSTP _aux_1
ETIQ_11:
FLD _aux_1
FSTP c
FLD c
FCOMP cte_int_2
FSTSW AX
SAHF
JE ETIQ_12
JMP ETIQ_13
ETIQ_12:
; #Iguales: evaluar pivote una unica vez
; #Iguales: inicializar contador en 0
FLDZ
FSTP _aux_3
; #Iguales: comparar pivote con un elemento de la lista
FLD b
FSUB cte_int_3
FSTP _aux_4
FLD a
FCOMP _aux_4
FSTSW AX
SAHF
JE ETIQ_14
JMP ETIQ_15
ETIQ_14:
; #Iguales: si son iguales, incrementar contador
FLD1
FADD _aux_3
FSTP _aux_3
ETIQ_15:
; #Iguales: comparar pivote con un elemento de la lista
FLD a
FCOMP cte_int_5
FSTSW AX
SAHF
JE ETIQ_16
JMP ETIQ_17
ETIQ_16:
; #Iguales: si son iguales, incrementar contador
FLD1
FADD _aux_3
FSTP _aux_3
ETIQ_17:
; #Iguales: comparar pivote con un elemento de la lista
FLD a
FCOMP cte_int_2
FSTSW AX
SAHF
JE ETIQ_18
JMP ETIQ_19
ETIQ_18:
; #Iguales: si son iguales, incrementar contador
FLD1
FADD _aux_3
FSTP _aux_3
ETIQ_19:
; #Iguales: comparar pivote con un elemento de la lista
FLD a
FCOMP cte_int_3
FSTSW AX
SAHF
JE ETIQ_20
JMP ETIQ_21
ETIQ_20:
; #Iguales: si son iguales, incrementar contador
FLD1
FADD _aux_3
FSTP _aux_3
ETIQ_21:
; #Iguales: comparar pivote con un elemento de la lista
FLD a
FCOMP cte_int_10
FSTSW AX
SAHF
JE ETIQ_22
JMP ETIQ_23
ETIQ_22:
; #Iguales: si son iguales, incrementar contador
FLD1
FADD _aux_3
FSTP _aux_3
ETIQ_23:
FLD _aux_3
FISTP DWORD PTR _show_int_value
DisplayInteger _show_int_value
ETIQ_13:

MOV AX,4C00h
INT 21h

MAIN ENDP

END MAIN
