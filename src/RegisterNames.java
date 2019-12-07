public class RegisterNames {
    private static String[] registers = {
            "$0",
            "$at",
            "$v0",
            "$v1",
            "$a0", "$a1", "$a2", "$a3",
            "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7",
            "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7",
            "$t8", "$t9",
            "$k0", "$k1",
            "$gp",
            "$sp",
            "$fp",
            "$ra"
    };

    static int getRegisterIndex(String id) {
        int v;
        for (v = 0; v < registers.length; ++v) {
            if (id.equals(registers[v])) return v;
        }
        return -1;
    }

    static String getRegisterIdentifier(int r) {
        return registers[r];
    }
}
