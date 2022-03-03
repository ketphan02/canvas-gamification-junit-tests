package temp;

import global.BaseInputTest;
import global.variables.Clause;
import global.variables.clauses.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class QuestionExample extends BaseInputTest {
    public Clause[] testSentence() {
        return new Clause[]{
                new StringLiteral("Enter a number of fingers held up behind your back: "),
                new NewLine(),
                new IntegerLiteral(1, 2000, "testInt"),
                new NewLine(),
                new StringLiteral("The prediction by Simon was: "),
                new RandomInteger(0, 10),
                new StringLiteral(", and the number of fingers you held up was: "),
                new RandomInteger(0, 10)
        };
    }

    public void runMain() {
        ProphetRobot.main(new String[0]);
    }

    @ParameterizedTest
    @MethodSource("inputTestCases")
    public void testMain() {
        System.err.println("testInt: " + getItemByName("testInt"));
    }
}
