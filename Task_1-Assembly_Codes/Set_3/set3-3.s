.data
	array: .word 1, 2, 3, 4, 5
.text
.globl main
main:
	li $s0, 0
	addi $t1, $s0, 0 			# i = 0
	addi $t2, $s0, 0 			# j = 0;
	addi $s1, $s0, 5 			# array Size
	addi $s2, $s0, 0			# index for printing array
	addi $s3, $s0, 1
	addi $s4, $s0, 0
	addi $s5, $s0, 0
main_loop:
	beq $t1, $s1, Print_Array	# i == array Size go to Print_Array
	li $t2, 0					# j = 0
	add $t2, $t2, $t1			# j = i

inner_loop:
	blt $t2, $s0, IncreamentI 	# j < i go to IncreamentI
	lw $t3, array($s4)			# load array[i] in $t3
	mul $t3, $t3, $t2			# mult $t3 by j($t2)
	sw $t3, array($s4)			# set array[i] = $t3
	addi $t2, $t2, -1			# j--
	j inner_loop

IncreamentI:
	addi $t1, $t1, 1			# i++
	addi $s4, $s4, 4
	j main_loop					# jump to main_loop

Print_Array:
	beq  $s2, $s1, Terminate  	# i == array Size goto Terminate
	lw $a0, array($s5)			# load into register a0 value of array[i]
	li $v0, 1					# preper for output system call
	syscall						# system call
	
	addi $a0, $0, 0xA 			# print \n
    addi $v0, $0, 0xB			# 
    syscall						# 
	
	addi $s2, $s2, 1			# increamnet i by 1 
	addi $s5, $s5, 4			#
	
	j Print_Array				# Jump Print_Array
	
Terminate:					
	li $v0, 10					# preper for Termination
	syscall						# system call
	
	