package arrays.programs_involving_data_sequences.medium.q2;

import global.BaseTest;
import global.tools.CustomAssertions;
import global.utils.ArrayUtil;
import global.utils.MethodUtil;
import global.variables.Clause;
import global.variables.clauses.StringLiteral;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class MainTest extends BaseTest {
    // Parsons with distractors
    public Clause[] testSentence() {
        return new Clause[]{
                new StringLiteral("Stringbuild")
        };
    }

    public void runMain() {
        WordleBuilder.main(new String[0]);
    }

    static Stream<Arguments> methodInputProvider() {
        return Stream.of(
                Arguments.of("storm", new char[]{'e', 'a', 'r', 'l', 's', 'y', 'm', 'c', 'a'}),
                Arguments.of("", new char[]{}),
                Arguments.of("", new char[]{'e', 'l', 'y', 'z', 'q', '?'}),
                Arguments.of("Yikes", new char[]{}),
                Arguments.of("RandomAt", ArrayUtil.generateRandomCharArray('a', (char) ('z' + 1), 39))
        );
    }

    @ParameterizedTest
    @MethodSource("methodInputProvider")
    void correctCharzIIStringMethod(String word, char[] letters) throws Throwable {
        String result = word + ArrayUtil.charArrayToInput(letters).replaceAll(" ", "");
        Object output = MethodUtil.invokeIfMethodExists(WordleBuilder.class, "charzIIString",
                new Object[]{word, letters}, String.class, char[].class);
        CustomAssertions._assertEquals(result, output, "Your charzIIString method does not correctly combine the input word and char array.");
    }
}
