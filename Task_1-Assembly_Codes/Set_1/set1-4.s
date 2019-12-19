.data
info: .asciiz"find summation of odd numbers to n \nformula ceil((n-1)/2)^2\n"
msg: .asciiz"enter the number\n"
.text
.globl main

main:
la $a0,info
li $v0,4
syscall

la $a0,msg
syscall

li $v0,5
syscall

addi $a0,$v0,0

jal sumOdd

addi $a0,$v0,0

li $v0,1
syscall

li $v0,10
syscall

sumOdd:
li $t0,0  #reminder
li $s0,0  #counter
li $s1,2  #mod
li $s2,0  #comparator
li $v0,0  #result
loop:
	beq $s0,$a0,end	#s0==n break
	rem $t0,$s0,$s1 #t0 = s0%s1
	beq $t0,$s2,continue
	
	add $v0,$v0,$s0	#if t0==1 , v0+=s0
	
	continue:
	addi $s0,$s0,1
j loop

end:
jr $ra
	