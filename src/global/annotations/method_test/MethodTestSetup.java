package global.annotations.method_test;

import global.BaseTest;

public class MethodTestSetup {
    private static BaseTest currentTest;
    private static String [] inputs;
    private static int count = 0;

    public MethodTestSetup (BaseTest mainTest){
        currentTest = mainTest;
    }

    public void setupMethodInputAndOutput(){

        count++;
    }
}
