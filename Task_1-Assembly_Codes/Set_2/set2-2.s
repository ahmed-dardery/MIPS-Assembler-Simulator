.data
array: .space 1024
vowels: .asciiz "aeiouAEIOU"
.text
.globl main
main:
	# input String
    li $v0,8
    la $a0,array
    li $a1,1024
    syscall

    li $s2,0   # count = 0
    la $s0,array # String array

	stringLoop:
    lb      $t0,0($s0) # t0 = current character
    addiu   $s0,$s0,1  # s0 = next character
    beqz    $t0,end    # if you reached null character in string end

    la      $s1,vowels  # s1 = vowel array

	vowelLoop:
    lb      $t1,0($s1)  # t1 = current vowel
    beqz    $t1,stringLoop    # if you reached null character in string go to original loop
    addiu   $s1,$s1,1          # s1 = next vowel
    bne     $t0,$t1,vowelLoop # if current letter != current vowel -> loop
    addi    $s2,$s2,1          # count += 1
    j       stringLoop        # go to original loop
	
	
	end:
	li $v0, 1
	addi $a0, $s2, 0 # print result
	
	syscall
	jr $ra # temp instruction
.end main