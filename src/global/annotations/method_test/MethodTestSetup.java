package global.annotations.method_test;

import global.BaseTest;
import global.utils.ClassUtil;
import global.utils.MethodUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MethodTestSetup {
    BaseTest test;
    Method [] methods;
     public MethodTestSetup(BaseTest current){
         test = current;
         methods = test.getClass().getMethods();
         setupMethodInputAndOutput();
     }

     private void setupMethodInputAndOutput (){
        for(Method item : methods){
            Annotation methodTestAnnotation = item.getAnnotation(MethodTest.class);
            if(methodTestAnnotation != null)
                MethodUtil.setUpMethodOutput();
            }
        }

}
