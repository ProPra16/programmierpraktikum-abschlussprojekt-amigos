package de.hhu.propra16.amigos.tdd.logik;

public class CodeObject {
    private String code;
    private String test;

    public CodeObject(String pCode, String pTest) {
        code = pCode;
        test = pTest;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String pCode) {
        code = pCode;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String pTest) {
        test = pTest;
    }
}
