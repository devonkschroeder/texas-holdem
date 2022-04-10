import java.util.Arrays;

public class TestHarness {
    private int testCount = 0;
    private int passedTests = 0;
    private int erroredTests = 0;

    public void assertEqual(String testName, Object expected, Object actual, String errorMessage) {
        if (expected.equals(actual)) {
            testPassed(testName);
        } else {
            testFailed(testName, errorMessage);
            System.out.println("  > Expected value: " + expected);
            System.out.println("  > Actual value: " + actual);
            
        }
    }

    public void assertArraysEqual(String testName, int[] expected, int[] actual, String errorMessage) {
        if (Arrays.equals(expected, actual)) {
            testPassed(testName);
        } else {
            testFailed(testName, errorMessage);
            System.out.println("  > Expected value: " + Arrays.toString(expected));
            System.out.println("  > Actual value: " + Arrays.toString(actual));
            
        }
    }

    public <T> void assertArraysEqual(String testName, T[] expected, T[] actual, String errorMessage) {
        if (Arrays.equals(expected, actual)) {
            testPassed(testName);
        } else {
            testFailed(testName, errorMessage);
            System.out.println("  > Expected value: " + Arrays.toString(expected));
            System.out.println("  > Actual value: " + Arrays.toString(actual));
            
        }
    }

    public void testPassed(String testName) {
        testCount++;
        passedTests++;
        System.out.println("- Test " + testName + ": OK");
    }

    public void testFailed(String testName, String errorMessage) {
        testCount++;
        erroredTests++;
        System.out.println("- Test " + testName + " ERROR: " + errorMessage);
    }

    public String getEvaluationSummary() {
        return String.format("passed %d/%d tests; %d tests failed", passedTests, testCount, erroredTests);
    }


}