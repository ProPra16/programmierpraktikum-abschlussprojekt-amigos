package de.hhu.propra16.amigos.tdd.logik;

import vk.core.api.CompilationUnit;

public class CodeObject {
    private String code;
    private CompilationUnit codeUnit;

    private String test;
    private CompilationUnit testUnit;

    public CodeObject() {

    }

    public void convertToValuesOf(CodeObject pObjekt) {
        code = pObjekt.getCode();
        codeUnit = pObjekt.getCodeUnit();
        test = pObjekt.getTest();
        testUnit = pObjekt.getTestUnit();
    }

    public String getCode() {
        return code;
    }

    public CompilationUnit getCodeUnit() {
        return codeUnit;
    }

    public void setCode(String pName, String pCode) throws Exception {
        codeUnit = new CompilationUnit(pName, pCode, false);

        code = pCode;
    }

    public String getTest() {
        return test;
    }

    public CompilationUnit getTestUnit() {
        return testUnit;
    }

    public void setTest(String pName, String pTest) throws Exception {
        testUnit = new CompilationUnit(pName, pTest, true);

        test = pTest;
    }
}
