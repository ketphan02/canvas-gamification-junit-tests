package global.annotations;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
// TODO: Finish implementation so MethodTest sets up input and output for a method being tested
@Retention(RetentionPolicy.RUNTIME)
public @interface  MethodTest {
    String value() default "";
}
