.data
array: .space 16 # 4 numbers
a: .word 4 # size
space: .ascii " "
.text
.globl main
main:
li $s0,0 #s0 = 0
move $t0,$s0 #t0 = 0
la $s1, array #s1 = array
lw $s2, a #s2 = a

#input 4 numbers
input:        
    beq $t0,16,exit
    li $v0,5
    syscall
    sw $v0,array($t0)
    add $t0,$t0,4
    j input
	
exit:
#print array

move $t1,$s0 #t1 = 0
addi $t2,$t1,4 #t2 = first element

# the required program calculates cummulative array
loop:
	lw $t3, array($t1) # t3 = arr[i-1]
	lw $t4, array($t2) # t4 = arr[i]
	
	add $t4,$t3,$t4 # t4 = arr[i-1] + arr[i]
	
	sw $t4,array($t2)
	
	addi $t1, $t1, 4 #i+=1
	addi $t2, $t2, 4 #j+=1
	
    bne $t1, 16, loop



move $t1,$s0 #t1 = 0
print:
    lw $t0, array($t1)
	
    li $v0, 1
    move $a0, $t0
    syscall
	
	la $a0, space #reload space to primary address
    li $v0,4 # print string
    syscall
	
    addi $t1, $t1, 4
    bne $t1, 16, print
	
jr $ra # temp instruction
	
.end main