package loops.simple_programs_with_repitition.medium.q5;

import global.variables.*;
import global.variables.clauses.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import global.BaseTest;
import global.exceptions.InvalidClauseException;
import global.tools.TestOption;
public class PerfektKyoobTest extends BaseTest {
    // Parsons

    public Clause[] testSentence() {
        TestOption.isInputTest = true;
        TestOption.defaultInput = "10";

        return new Clause[] {
                new StringLiteral("Enter a number: "),
                new NewLine(),
                new IntegerLiteral("numberOutput"),
                new PlaceHolder()
        };
    }

    public void runMain() {
        PerfektKyoob.main(new String[0]);
    }

    static Stream<Arguments> inputProvider(){
        return Stream.of(
            Arguments.of(0, " is a Perfect Cube!"),
            Arguments.of(1, " is a Perfect Cube!"),
            Arguments.of(15, " is NOT a Perfect Cube!"),
            Arguments.of(456, " is NOT a Perfect Cube!")
        );
    }

    @ParameterizedTest
    @MethodSource("inputProvider")
    void testWithInput(int input, String output) throws InvalidClauseException {
        runWithInput("" + input, new Clause[] {
            new StringLiteral(output)
        });
        assertEquals(Integer.parseInt(getItemByName("numberOutput")), input, "The output is not formatted correctly.");
    }
}
