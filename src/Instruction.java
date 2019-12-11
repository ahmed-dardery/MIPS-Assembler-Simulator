import java.util.ArrayList;

//TODO: figure out what to do with jump instructions (because they jump to a label)
public abstract class Instruction {
	
	public enum instructionType{
		RTypeInstruction,
		JTypeInstruction,
		ITypeInstruction;
		
	}
	
	
	
	public Instruction(){
		
	}
	
	public abstract instructionType getInstructionType();
	
	
	public static Instruction getInstructionObject(String input) {
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
		else {
			System.out.println("will return null");
			return null;
		}
		return type;
	}
	
    public static String makeStringReadyToUse(String input) {
    	if(input.startsWith(" ")) {
    		int loc = input.indexOf("\\w");
    		input=input.substring(loc);
    	}
    	input=input.replace("(",",");
    	input=input.replace(")","");
    	input=input.replaceAll("\\s+,\\s+|\\s+,|,\\s+",",");
    	return input;
	}

    public static String[] splitArgs(String input) {
    	String result[]=input.split("\\s+|,");/*
    	while(result.length<4) {
    		int length=result.length;
    		result[length+1]="0";
    	}*/
    	if(result.length<4) return null;
    	return result;
    	
    }
	
	public static int[] getRegNumber(String []args) {
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

	private static boolean rType(String input) {
    	for(RTypeInstruction.RTypeNames tst :RTypeInstruction.RTypeNames.values()) {
    		if(tst.toString().equals(input)) return true;
		
    	}
    	return false;
	}

	private static boolean iType(String input) {
    	for(ITypeInstruction.ITypeNames tst :ITypeInstruction.ITypeNames.values()) {
    		if(tst.toString().equals(input)) return true;
		
    	}
    	return false;
		
	}

	private static boolean jType(String input) {
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
    	String input= "add $a0, $a0,$a0";
    	String input2= "addi $a0, $a0,5";
    	String input3= "sllv $a0, $a0,$a0";
    	ArrayList<Instruction>in = new ArrayList<Instruction>();
    	in.add(getInstructionObject(input));
    	in.add(getInstructionObject(input2));
    	in.add(getInstructionObject(input3));
    	for(Instruction t:in) {
    		System.out.println(t.getInstructionType());
    	}

    	
    }
}
