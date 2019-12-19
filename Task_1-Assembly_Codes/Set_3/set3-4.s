.data
.text
.globl main
main:
	
	addi $t1, $t1, 8	# Fact(8)
	addi $a0, $t1, 0	#
	jal Fact			#
	addi $t4, $v1, 0	#
	
	addi $a0, $t4, 0	# Print the result
	li $v0, 1			#
	syscall				#
	
	addi $a0, $0, 0xA 	# print \n
    addi $v0, $0, 0xB	# 
    syscall				#
	
	li $t1, 3			#Fact(3)
	addi $a0, $t1, 0	#
	jal Fact			#
	addi $t5, $v1, 0	#
	
	addi $a0, $t5, 0	#print the result
	li $v0, 1			#
	syscall				#
	
	addi $a0, $0, 0xA 	# print \n
    addi $v0, $0, 0xB	# 
    syscall				# 
	
	add $s1, $t4, $t5	# t3 = t1 + t2
	addi $a0, $s1, 0	# print the result(t3)
	li $v0, 1			#
	syscall				#
	
	j Terminate			# Jump Terminate
	
Fact:

	li $t3, 1	# Comparable 1
	li $v1, 0
	addi $t0, $a0, 0 # i = n
	addi $t2, $s0, 1 # result = 1
	
	For_Loop:	# beginning for loop 
		
		beq $t0, $t3, Continue_Fact # if(result = 1) goto Continue_Fact
		mul $t2, $t2, $t0			# result = result * i
		addi $t0, $t0, -1			# i--
		
		j For_Loop					# jump back to For_Loop
		
Continue_Fact:			# Continue the function
	
	addi $v1, $t2, 0 	# save result
	jr $ra				# jump register
	
Terminate:					
	li $v0, 10			# preper for Termination
	syscall				# system call