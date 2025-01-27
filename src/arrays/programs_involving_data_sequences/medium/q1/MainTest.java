package arrays.programs_involving_data_sequences.medium.q1;

import global.BaseTest;
import global.tools.CustomAssertions;
import global.utils.ArrayUtil;
import global.utils.MethodUtil;
import global.variables.Clause;
import global.variables.clauses.StringLiteral;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class MainTest extends BaseTest {
    // Parsons with distractors
    public Clause[] testSentence() {
        return new Clause[]{
                new StringLiteral("24601")
        };
    }

    public void runMain() {
        Stringify.main(new String[0]);
    }

    static Stream<int[]> methodInputProvider() {
        return Stream.of(
                new int[]{2, 4, 6, 0, 1},
                new int[]{},
                new int[]{-11, 12, 37, 11, 0, 0, 0, -39, 345},
                new int[]{1},
                ArrayUtil.generateRandomIntArray(-537, 364, 49)
        );
    }

    @ParameterizedTest
    @MethodSource("methodInputProvider")
    void correctIntoStringMethod(int[] input) throws Throwable {
        String result = ArrayUtil.intArrayToInput(input).replaceAll(" ", "");
        Object output = MethodUtil.invokeIfMethodExists(Stringify.class, "intoString", new Object[]{input}, int[].class);
        CustomAssertions._assertEquals(result, output, "Your intoString method does not correctly convert the integers in the array into a String.");
    }
}
