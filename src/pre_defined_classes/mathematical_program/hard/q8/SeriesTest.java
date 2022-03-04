package pre_defined_classes.mathematical_program.hard.q8;

import global.BaseTest;
import global.variables.Clause;
import global.variables.clauses.*;

public class SeriesTest extends BaseTest {
    public Clause[] testSentence() {
        return new Clause[]{
                new StringLiteral("The 7th term in the geometric sequence is: 2.44140625E-4")
        };
    }

    public void runMain() {
        Series.main(new String[]{});
    }

}
