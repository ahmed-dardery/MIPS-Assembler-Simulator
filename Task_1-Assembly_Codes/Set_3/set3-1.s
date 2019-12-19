.data
.text

.globl main
main:
	li $s1, 5
	li $s2, 6
	
	blt $s1, $s2, ELSE 	# branch if ( g < h )
	addi $s1, $s1, 1 	# g++
	j Terminate
ELSE: 
	addi $s2, $s2, -1 	# else h-- 
	
Terminate:					
	li $v0, 10			# preper for Termination
	syscall				# system call