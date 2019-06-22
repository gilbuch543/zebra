import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.*;
import java.util.*;


public class TestScenarios {


    static final String INPUT_PATH = "C:\\\\Users\\\\gil b\\\\Desktop\\\\project1\\\\input.txt";
    static final String TEST_FAILURE_OUTPUT = "D:\\\\testFailureUsername.txt";
    static final String TEST_RESULTS = "D:\\\\testResults.txt";
    static final String RESULT_SUCCESS = "successful login";
    static final String RESULT_FAILED = "login failed";
    static final Random RANDOM = new Random();

    public static void main(String[] args) throws IOException {

        List<Scenario> testScenarios;
        List<Scenario> testResults;
        String[] failureLogins;
        FileInputStream input = new FileInputStream(INPUT_PATH);
        testScenarios = parseFileIntoListOfScenarios(input);
        testResults = runTestScenarios(testScenarios);
        createTestResultsOutPutFile(testResults);
        failureLogins = checkForFailuresToLogin(testResults);
        createFailureOutPutFile(failureLogins);
    }

    /**
     * <b>Method Steps:</b><br>
     * - Get list of Scenarios<br>
     * - Create new file in specific path<br>
     * - Write each scenario in the file<br>
     * - print the files absolute path<br>
     */
    private static void createTestResultsOutPutFile(List<Scenario> myList) throws IOException {
        File file = new File(TEST_RESULTS);
        ObjectMapper objectMapper = new ObjectMapper();
        try (FileOutputStream fos = new FileOutputStream(file);
             PrintStream printStream = new PrintStream(fos)) {
            myList.forEach(scenario -> {
                try {
                    printStream.println(objectMapper.writeValueAsString(scenario));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println("Absolute Path: " + file.getAbsolutePath());
    }

    /**
     * <b>Method Steps:</b><br>
     * - Get Array of unique failed usernames<br>
     * - Create new file in specific path<br>
     * - Write each username in the file <br>
     * - print the files absolute path<br>
     */
    private static void createFailureOutPutFile(String[] failureList) throws IOException {

        File file = new File(TEST_FAILURE_OUTPUT);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        for (String currentFailure :
                failureList) {
            writer.write(currentFailure);
            writer.newLine();
        }
        writer.close();
        System.out.println("Absolute Path: " + file.getAbsolutePath());
    }

    /**
     * <b>Method Steps:</b><br>
     * - Get List of scenarios<br>
     * - Check if the expected result and actual results are alike<br>
     * - add the failed username to failedScenarios list<br>
     * - Convert the list to set (to remove duplicated usernames)<br>
     * - Convert the set to Array of failed usernames<br>
     */
    private static String[] checkForFailuresToLogin(List<Scenario> testResults) {
        ArrayList<String> failedScenarios = new ArrayList<>();
        int index = 0;

        for (Scenario currentScenario :
                testResults) {
            if (currentScenario.expected_result.contentEquals("successful login") && currentScenario.actual_result.contentEquals("login failed") ||
                    currentScenario.expected_result.contentEquals("login failed") && currentScenario.actual_result.contentEquals("successful login")) {

                failedScenarios.add(index, currentScenario.scenario_data.getUsername());
                index++;
            }

        }

        Set set = new HashSet();
        set.addAll(failedScenarios);//remove duplicates
        String[] Array = new String[set.size()];
        set.toArray(Array);

        return Array;

    }

    /**
     * <b>Method Steps:</b><br>
     * - Get List of scenarios<br>
     * - Execute login on each scenario<br>
     * - Set the actual result with the execute login result (successful login/login failed)<br>
     * - return the list with the results<br>
     */
    private static List<Scenario> runTestScenarios(List<Scenario> testScenarios) {

        for (Scenario currentScenario :
                testScenarios) {
            String currentResult = executeLogin();
            currentScenario.setActual_result(currentResult);
        }
        return testScenarios;
    }

    /**
     * <b>Method Steps:</b><br>
     * - random the result options(successful login/login failed)<br>
     * - return the randomized result<br>
     */
    private static String executeLogin() {
        String[] randomResults = {RESULT_SUCCESS, RESULT_FAILED};
        int select = RANDOM.nextInt(randomResults.length);
        String result = randomResults[select];
        return result;
    }

    /**
     * <b>Method Steps:</b><br>
     * - Create list of Scenarios<br>
     * - Prase the file into the list (each line is a scenario in JSON)<br>
     * - Return the list <br>
     */
    private static List<Scenario> parseFileIntoListOfScenarios(FileInputStream input) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Scenario> scenariosList = new ArrayList();

        JsonFactory jf = new JsonFactory();
        JsonParser jp = jf.createParser(input);
        jp.setCodec(objectMapper);
        jp.nextToken();
        while (jp.hasCurrentToken()) {
            Scenario scenario = jp.readValueAs(Scenario.class);
            scenariosList.add(scenario);
            jp.nextToken();
        }
        return scenariosList;
    }


    public static class Scenario {
        private int scenario_number;
        private String scenario_name;
        private ScenarioData scenario_data;
        private String expected_result;
        private String actual_result;

        public int getScenario_number() {
            return scenario_number;
        }

        public void setScenario_number(int scenario_number) {
            this.scenario_number = scenario_number;
        }

        public String getScenario_name() {
            return scenario_name;
        }

        public void setScenario_name(String scenario_name) {
            this.scenario_name = scenario_name;
        }

        public ScenarioData getScenario_data() {
            return scenario_data;
        }

        public void setScenario_data(ScenarioData scenario_data) {
            this.scenario_data = scenario_data;
        }

        public String getExpected_result() {
            return expected_result;
        }

        public void setExpected_result(String expected_result) {
            this.expected_result = expected_result;
        }

        public String getActual_result() {
            return actual_result;
        }

        public void setActual_result(String actual_result) {
            this.actual_result = actual_result;
        }

    }

    public static class ScenarioData {

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}






