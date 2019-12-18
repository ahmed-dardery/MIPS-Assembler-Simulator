.data
		Char_Array: .byte 'A', 'D', 'H', 'A', 'M', ' ', 'M', 'A', 'M', 'D', 'O', 'U', 'H' 
.text
.globl main

main: 
	addi $s1, $s0, 1  			#Const 1
	addi $s2, $s0, 0  			#Iterator
	addi $s3, $s0, 13 			#Array Size
	addi $t1, $s0, 0  			#Pointer Start
	addi $t2, $s0, 12 			#Pointer End
	andi $s0, $s0, 0
	
Reverse_Array:
	bge	$t1, $t2, Print_Array	#t3 == 0 goto Print_Array
	
	lb  $t4, Char_Array($t1) 	#t4 = Char_Array[start]
	lb  $t5, Char_Array($t2) 	#t5 = Char_Array[end]

    sb  $t5, Char_Array($t1) 	#Char_Array[start] = t5
	sb  $t4, Char_Array($t2) 	#Char_Array[end] = t4
	
	add $t1, $t1, $s1 			#Start++
	sub $t2, $t2, $s1 			#end--
	
	j Reverse_Array 			#jump to Reverse_Array
	
Print_Array:
	beq  $s2, $s3, Terminate  	# i == Char_Array Size goto Terminate
	lb $a0, Char_Array($s2)		# load into register a0 value of Char_Array[i]
	li $v0, 11					# preper for output system call
	syscall						# system call
	add $s2, $s2, $s1			# increamnet i by 1 
	j Print_Array				# jump to Print_Array
	
Terminate:
	li $v0, 10					# preper for termination
	syscall						# system call

	
	