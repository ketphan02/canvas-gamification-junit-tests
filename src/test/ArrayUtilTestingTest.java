package test;

import global.BaseTest;
import global.tools.CustomAssertions;
import global.tools.Logger;
import global.tools.TestOption;
import global.utils.ArrayUtil;
import global.utils.MethodUtil;
import global.variables.Clause;
import global.variables.clauses.IntegerLiteral;
import global.variables.clauses.NewLine;
import global.variables.clauses.StringLiteral;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayUtilTestingTest extends BaseTest {
    public Clause[] testSentence() {
        TestOption.isInputTest = true;
        TestOption.defaultInput = ArrayUtil.intArrayToInput(ArrayUtil.generateRandomIntArray(-5, 5, 10));
        return new Clause[]{
                new StringLiteral("Enter 10 integers: "),
                new NewLine(),
                new StringLiteral("The sum of your array is: "),
                new IntegerLiteral("sum")
        };
    }

    public void runMain() {
        ArrayUtilTesting.main(new String[0]);
    }

    static Stream<int[]> inputProvider() {
        return Stream.of(
                ArrayUtil.generateRandomIntArray(-5, 5, 10),
                ArrayUtil.generateRandomIntArray(-50, -45, 10),
                ArrayUtil.generateRandomIntArray(39, 40, 10),
                ArrayUtil.generateRandomIntArray(-100, 100, 10),
                ArrayUtil.generateRandomIntArray(0, 10, 10)
        );
    }

    @ParameterizedTest
    @MethodSource("inputProvider")
    void correctSumMethod(int[] input) {
        runWithInput(ArrayUtil.intArrayToInput(input));
        assertEquals(ArrayUtil.sum(input), Integer.parseInt(getItemByName("sum")), "Your sum method does not correctly calculate the sum of the integer array.");
    }

    @Test
    void correctHalfMethod() throws Throwable {
        int[] input = new int[]{1, 2, 3, 4, 5};
        Object output = MethodUtil.invokeIfMethodExists(ArrayUtilTesting.class, "half",
                new Object[]{input}, int[].class);
        CustomAssertions._assertArrayEquals(new int[]{1, 2}, output, "Failed");
    }

    @Test
    void failsWithIncorrectType() throws Throwable {
        int[] test1 = new int[]{6};
        int[] test2 = new int[]{6};
        Object in1 = test1;
        Object in2 = test2;
        CustomAssertions._assertArrayEquals(in1, in2, "Failed");
    }
}
