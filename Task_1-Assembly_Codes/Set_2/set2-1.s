.data
g: .word 1
h: .word 10
.text
.globl main
main:
li $s0,0 #s0 = 0
lw $s1, h #s1 = h
lw $s2, g #s2 = g

ble $s2,$s1,first # if (g<=h) goto first condition
j default

first:
bgt $s2,$s0,second # if g>0  g = h
j default

default:
addi $s1,$s2,0 # h = g
j end

second:
addi $s2,$s1,0 # g = h
end:
jr $ra # temp instruction
.end main