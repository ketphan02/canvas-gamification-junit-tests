package global.variables.clauses;

import global.utils.RandomUtil;
import global.utils.RegexUtil;
import global.variables.Clause;
import global.variables.RandomClause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static global.tools.CustomAssertions.assertWithinRange;
import static global.utils.RandomUtil.frequenciesAreRandom;
import static global.utils.RegexUtil.orNegative;

public class RandomDouble extends Clause implements RandomClause<Double> {
    static Map<Integer, ArrayList<Double>> valueStore = new HashMap<>();
    private final double lower, upper;
    private int precision = 16;  // max double precision

    public RandomDouble(double lower, double upper) {
        super();
        this.lower = lower;
        this.upper = upper;
    }

    public RandomDouble(double lower, double upper, String name) {
        super(name);
        this.lower = lower;
        this.upper = upper;
    }

    public RandomDouble(int lower, int upper) {
        this((double) lower, upper);
        //needs one value to be explicitly casted otherwise it calls the same constructor recursively
    }

    public RandomDouble(int lower, int upper, String name) {
        this((double) lower, upper, name);
    }

    public RandomDouble(double lower, double upper, int precision) {
        super();
        this.lower = lower;
        this.upper = upper;
        this.precision = precision;
    }

    public RandomDouble(double lower, double upper, int precision, String name) {
        super(name);
        this.lower = lower;
        this.upper = upper;
        this.precision = precision;
    }

    public void trackValue(int matchGroupNum, String matchGroupValue) {
        valueStore.computeIfAbsent(matchGroupNum, k -> new ArrayList<>());
        (valueStore.get(matchGroupNum)).add(convertFromRegexGroup(matchGroupValue));
    }

    public boolean validateRandom(int matchGroupNum) {
        if (valueStore.get(matchGroupNum) == null)
            return false;
        ArrayList<Double> values = valueStore.get(matchGroupNum);
        final int numBins = RandomDouble.getNumBins(lower, upper);

        int[] observedCounts = new int[numBins];
        for (double value : values) {
            int binNum = RandomDouble.assignedBinIndex(value, lower, upper, numBins);
            if (binNum == RandomUtil.NO_BIN) return false;
            observedCounts[binNum]++;
        }

        return frequenciesAreRandom(observedCounts, numBins);
    }

    private static int getNumBins(double lower, double upper) {
        int upperCeil = (int) (upper + 1);
        int lowerFloor = (int) lower;
        // Ensures that there will be at least 10 bins
        return (upperCeil - lowerFloor <= 10) ? RandomUtil.getNumBins(0, 10) :
                RandomUtil.getNumBins(lowerFloor, upperCeil); // TODO: do properly
    }

    private static int assignedBinIndex(double value, double lower, double upper, int numBins) {
        double gap = (upper - lower) / numBins;
        assertWithinRange(value, lower, upper, "One or more of your randomly generated numbers fall outside of the required range.");
        int binNumber = (int) ((value - lower) / gap);
        return (binNumber <= numBins) ? binNumber : RandomUtil.NO_BIN;
    }

    public double getLower() {
        return lower;
    }

    public double getUpper() {
        return upper;
    }

    public double getPrecision() {
        return precision;
    }

    public Double convertFromRegexGroup(String matchGroupString) {
        // TODO: try catch
        return Double.parseDouble(matchGroupString);
    }

    @Override
    public ArrayList<Double> getValuesForMatchGroup(int matchGroup) {
        return valueStore.get(matchGroup);
    }

    @Override
    public String getRegex() {
        int lowSplit = RegexUtil.getSplitDecimal(lower)[0];
        int highSplit = RegexUtil.getSplitDecimal(upper)[0];
        String regexContent = RegexUtil.getRegexInt(lowSplit, highSplit);
        return "(" + orNegative(regexContent + "\\.\\d+") + ")";
    }
}
