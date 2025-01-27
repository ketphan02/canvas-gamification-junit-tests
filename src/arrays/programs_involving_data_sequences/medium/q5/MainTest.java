package arrays.programs_involving_data_sequences.medium.q5;

import global.BaseTest;
import global.exceptions.InvalidClauseException;
import global.tools.CustomAssertions;
import global.tools.TestOption;
import global.utils.ArrayUtil;
import global.utils.MethodUtil;
import global.variables.Clause;
import global.variables.clauses.IntegerLiteral;
import global.variables.clauses.NewLine;
import global.variables.clauses.PlaceHolder;
import global.variables.clauses.StringLiteral;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest extends BaseTest {
    // Parsons with distractors
    private static final int[] mainArray = new int[]{1, 3, 5, 7, 9, 7};
    private static final int replacement = 7;

    public Clause[] testSentence() {
        return new Clause[]{
                new StringLiteral("The number at index "),
                new IntegerLiteral("replacedIndex"),
                new StringLiteral(" was replaced\\."),
                new NewLine(),
                new StringLiteral("The array is now: "),
                new PlaceHolder()
        };
    }

    public void runMain() {
        DoTheDeletion.main(new String[0]);
    }

    static Stream<Arguments> methodInputProvider() {
        return Stream.of(
                Arguments.of(new int[]{3, 4, 5, 0, -11, 3, 3, 1, 5, 3}, 3, 9),
                Arguments.of(new int[]{}, 0, -1),
                Arguments.of(new int[]{1, 5, -11, -11, 2, 3, -11, 5, 4, -100}, 5, 7),
                Arguments.of(new int[]{311, 12, 4, -45, 1, 2, -311}, 311, 0),
                Arguments.of(new int[]{1, -453, 23, 6, 32, 5, 5, 2, 52}, -453, 1),
                Arguments.of(new int[]{0, 1, 2, 3, 4, 5}, 6, -1)
        );
    }

    @ParameterizedTest
    @MethodSource("methodInputProvider")
    void correctDeleteThisMethod(int[] input, int replace, int index) throws Throwable {
        Object output = MethodUtil.invokeIfMethodExists(DoTheDeletion.class, "deleteThis",
                new Object[]{input, replace}, int[].class, int.class);
        CustomAssertions._assertEquals(index, output, "Your deleteThis method does not return the index the item in the array was replaced at.");
        String consoleOutput = MethodUtil.getMethodOutput();
        assertEquals("", consoleOutput, "Your deleteThis method should not have any printed output.");
    }

    @Test
    void printsCorrectOutput() throws InvalidClauseException {
        TestOption.incorrectStructureErrorMessage = "Your program does not correctly print the array with the replaced values.";
        int[] output = replace();
        runWithInput("", new Clause[]{
                new StringLiteral(ArrayUtil.intArrayToInput(output))
        });
        assertEquals(replacementIndex(), Integer.parseInt(getItemByName("replacedIndex")), "Your program does not correctly print the index of the replaced integer.");
    }

    private static int[] replace() {
        return Arrays.stream(mainArray).map(number -> number == replacement ? 0 : number).toArray();
    }

    private static int replacementIndex() {
        return Arrays.stream(mainArray).boxed().collect(Collectors.toList()).lastIndexOf(replacement);
    }

}
