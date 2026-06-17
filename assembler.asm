.MODEL LARGE
.386
.STACK 200h

.DATA

x dd ?
y1 dd ?
cte_int_42 dd 42.0
cte_float_0_75 dd 0.75

.CODE

MOV AX,@DATA
MOV DS,AX
MOV ES,AX

FLD cte_int_42
FSTP x
FLD cte_float_0_75
FSTP y1

MOV AX,4C00h
INT 21h

END
