//TODO: figure out what to do with jump instructions (because they jump to a label)
public abstract class Instruction {
	
	enum instructionType{
		RTypeInstruction,
		JTypeInstruction,
		ITypeInstruction;
	}
	
	public instructionType getInstructionType(String input) {
		input=makeStringReadyToUse(input);
		String command=splitArgs(input)[0];
		if(jType(command)) return instructionType.JTypeInstruction;
		else if(iType(command)) return instructionType.ITypeInstruction;
		else if(rType(command)) return instructionType.RTypeInstruction;
		
		return null;
	}
	
	public Instruction getInstructionObject(String input) {
		input=makeStringReadyToUse(input);
		
		String result[] =splitArgs(input); 
		if(result == null) return null;
		
    	String cmd=result[0];
    	
    	int args[]=getRegNumber(result);
    	if(args==null) return null;
    	
    	Instruction type;
    	
		if(jType(cmd)) {
			type=new JTypeInstruction(JTypeInstruction.JTypeNames.valueOf(cmd),args[1]);
		}
		
		else if(iType(cmd)) {
			type=new ITypeInstruction(ITypeInstruction.ITypeNames.valueOf(cmd),args[1],args[2],args[3]);
			
		}
		
		else if(rType(cmd)) {
			type=new RTypeInstruction(RTypeInstruction.RTypeNames.valueOf(cmd),args[1],args[2],args[3]);
			
		}
		else return null;
		return type;
	}
	
    public String makeStringReadyToUse(String input) {
    	if(input.startsWith(" ")) {
    		int loc = input.indexOf("\\w");
    		input=input.substring(loc);
    	}
    	input=input.replace("(",",");
    	input=input.replace(")","");
    	input=input.replaceAll("\\s+,\\s+|\\s+,|,\\s+",",");
    	return input;
	}

    public String[] splitArgs(String input) {
    	String result[]=input.split(",");/*
    	while(result.length<4) {
    		int length=result.length;
    		result[length+1]="0";
    	}*/
    	if(result.length<4) return null;
    	return result;
    	
    }
	
	public int[] getRegNumber(String []args) {
		int [] res= new int[args.length];
		for(int i=1;i<args.length;++i) {
			if(!args[i].startsWith("$")) {
				res[i]=Integer.parseInt(args[i]);
				continue;
			}
			res[i]=RegisterNames.getRegisterIndex(args[i]);
			if(res[i]==-1) return null;
		}
		return res;
	}

	private boolean rType(String input) {
    	for(RTypeInstruction.RTypeNames tst :RTypeInstruction.RTypeNames.values()) {
    		if(tst.toString().equals(input)) return true;
		
    	}
    	return false;
	}

	private boolean iType(String input) {
    	for(ITypeInstruction.ITypeNames tst :ITypeInstruction.ITypeNames.values()) {
    		if(tst.toString().equals(input)) return true;
		
    	}
    	return false;
		
	}

	private boolean jType(String input) {
    	for(JTypeInstruction.JTypeNames tst :JTypeInstruction.JTypeNames.values()) {
    		if(tst.toString().equals(input)) return true;
		
    	}
    	return false;
		
	}

	/**
     * @return short instruction name in assembly
     */
	abstract String getInstructionName();
    /**
     * @return id of instruction, it is opcode for non-R type and concatenation of opcode and funct for R-type
     */
    abstract int getIdentifier();
    /**
     * @return op code of instruction, 0 for R-type
     */
    abstract int getOpCode();
    /**
     * @return Assembly String for the instruction, for example "add $s0, $s1, $s2".
     */
    abstract String toAssembly();
    /**
     * @return Machine Language for the instruction in binary formation.
     */
    abstract String toMachineLanguage();
    
    public static void main(String args[]) {
    	String input= "add $a0, $a0,$a0)";

    	input=input.replace("(",",");
    	input=input.replace(")","");
    	System.out.println(input);
    	input=input.replaceAll("\\s+,\\s+|\\s+,|,\\s+",",");
    	String arr[]=input.split(",|\\s+");
    	for(String a :arr) {
    		System.out.println(a);
    	}
    	
    }
}
