package global;

import static global.tools.CustomAssertions._assertTrue;
import static global.tools.CustomAssertions._fail;
import static global.utils.RegexUtil.*;
import static org.junit.jupiter.api.Assertions.*;

import global.tools.BeforeAfterEachParameterResolver;
import global.tools.InvalidClauseException;
import global.tools.Logger;
import global.variables.Clause;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(BeforeAfterEachParameterResolver.class)
public abstract class BaseInputTest {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private String currentOutput = null;
    private ByteArrayOutputStream testOut;
    private Clause[] regexSentence;

    // Test Developer defined
    public abstract Clause[] testSentence();

    public static String[] testInputs() {
        return new String[]{"10", "200"};
    }

    public abstract void runMain();


    // Setters and Getters
    public static Stream<String> inputTestCases() {
        return Stream.of(testInputs());
    }

    public void setRegexSentence(Clause[] regexSentence) throws InvalidClauseException {
        //By using a set we can ensure that there are no duplicates in the regexSentence
        Set<String> namesSet = new HashSet<>();
        for (Clause clause : regexSentence) {
            if (clause.getName() != null) {
                if (namesSet.contains(clause.getName())) {
                    _fail("There is an issue with the test definition",
                            "The name " + clause.getName() + " is already in use. make sure all names are unique");
                }
                namesSet.add(clause.getName());
            }
        }

        //validate all parameters used for the clauses
        for (int i = 0; i < regexSentence.length; i++) {
            Clause clause = regexSentence[i];
            try {
                clause.validateParams();
            } catch (InvalidClauseException e) {
                throw new InvalidClauseException("The parameter(s) for the clause at index " + (i + 1) + " are invalid. " + e.getMessage());
            }
        }

        this.regexSentence = regexSentence;
    }

    public Clause[] getRegexSentence() {
        return this.regexSentence;
    }

    // Utilities
    public void executeMain(String input) {
        currentOutput = null;
        provideInput(input);
        runMain();
    }

    public void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    public String getOutput() {
        if (currentOutput != null) {
            return currentOutput;
        }
        currentOutput = testOut.toString();
        return currentOutput;
    }

    public String getItemAtIndex(int index) {
        Matcher matcher = getMatches(getOutput(), processRegexForPrintlnOutput(combineRegex(getRegexSentence())));
        try {
            if (matcher.find()) return matcher.group(index);
            else fail("Your code's output did not follow the correct structure/syntax");
        } catch (IndexOutOfBoundsException e) {
            fail("The specified group (" + index + ") doesn't exist");
        }
        return "";  // TODO: logically how does this behave?
    }

    public String getItemByName(String name) {
        Clause[] regSen = getRegexSentence();
        for (int i = 0; i < regSen.length; i++) {
            if (regSen[i].getName() != null && regSen[i].getName().equals(name))
                return getItemAtIndex(i + 1);
        }
        fail("The specified group (" + name + ") doesn't exist");
        return ""; // TODO: logically how does this behave?
    }

    // Default Tests and Setup
    @BeforeAll
    public static void setUpLogger() {
        Logger.setCurrentSystemOut();
    }

    @BeforeEach
    public void setUp(String input) throws InvalidClauseException {
        setRegexSentence(testSentence());
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        executeMain(input);
    }

    @ParameterizedTest
    @Order(1)
    @MethodSource("inputTestCases")
    public void checkOutputFollowsPattern(String input) {
        String output = getOutput();
        Matcher matcher = getMatches(output, processRegexForPrintlnOutput(combineRegex(getRegexSentence())));
        assertTrue(matcher.find(), "Your code's output did not follow the correct structure/syntax.");
        //Ensures that the output matches the pattern exactly
        assertEquals(output.substring(matcher.start(), matcher.end()), output, "Your code's output did not follow the correct structure/syntax.");
        // This ensures that their output only contains 1 instance of the matched regex string
        assertFalse(matcher.find());
    }

    @ParameterizedTest
    @MethodSource("inputTestCases")
    public void allClausesValid(String input) {
        String output = getOutput();
        Matcher matcher = getMatches(output, processRegexForPrintlnOutput(combineRegex(getRegexSentence())));
        assertTrue(matcher.find(), "Your code's output did not follow the correct structure/syntax.");

        int matchGroupNum = 1;  // match group numbers are 1-indexed
        for (Clause clause : getRegexSentence()) {
            // TODO: devMessage could be improved
            _assertTrue(clause.validate(matcher.group(matchGroupNum)), clause.getInvalidMessage(), "Invalid Clause at index " + matchGroupNum);
            matchGroupNum++;
        }
    }

    @AfterEach
    public void restoreSystemInputOutput(String input) {
        currentOutput = null;
        System.setIn(systemIn);
        System.setOut(systemOut);
    }
}
