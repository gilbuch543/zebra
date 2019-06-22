import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.*;
import java.util.*;


public class TestScenarios {

    public static void main(String[] args) throws IOException {

        List<Scenario> myList;
        String[] Array;


        FileInputStream input = new FileInputStream("C:\\\\Users\\\\gil b\\\\Desktop\\\\project1\\\\input.txt");
        myList = parseFileIntoListOfScenarios(input);
        myList = runTest(myList);
        createTestResultsOutPutFile(myList);
        Array = checkForFailuresToLogin(myList);
        createFailureOutPutFile(Array);


    }

    private static void createTestResultsOutPutFile(List<Scenario> myList) throws IOException {
        File file = new File("D:\\\\testResults.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        // Write these lines to the file.
        // ... We call newLine to insert a newline character.
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
         //car = new Car();
      //  car.brand = "BMW";
      //  car.doors = 4;

     /*   objectMapper.writeValue(
                new FileOutputStream("data/output-2.json"), car);
        for (Scenario currentFailure:
                myList) {
            writer.write(currentFailure.toString());
            writer.newLine();

        }*/
        writer.close();
        System.out.println("Absolute Path: " + file.getAbsolutePath());
    }

    private static void createFailureOutPutFile(String[] failureList) throws IOException {
        //PrintWriter writer = new PrintWriter("testFailureUsername.txt", "UTF-8");

        File file = new File("D:\\\\testFailureUsername.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        // Write these lines to the file.
        // ... We call newLine to insert a newline character.

        for (String currentFailure:
                failureList) {
            writer.write(currentFailure);
            writer.newLine();
        }
        writer.close();
        System.out.println("Absolute Path: " + file.getAbsolutePath());
    }

    private static String[] checkForFailuresToLogin(List<Scenario> myList) {
       ArrayList<String> failedScenarios =  new ArrayList<>();
        int j = 0;
        for (int i = 0; i < myList.size(); i++) {
            Scenario scenario = myList.get(i);
            if (scenario.expected_result.contentEquals("successful login") && scenario.actual_result.contentEquals("login failed") ||
                    scenario.expected_result.contentEquals("login failed") && scenario.actual_result.contentEquals("successful login")) {

                failedScenarios.add(j, scenario.scenario_data.getUsername());
                j++;
            }
        }
        Set set = new HashSet();
        set.addAll(failedScenarios);
        String[] Array = new String[set.size()];
       // set.remove(null);
        set.toArray(Array);

        return Array;

    }

    private static List<Scenario> runTest(List<Scenario> testResults) {
        for (int i = 0; i < testResults.size(); i++) {
            Scenario scenario = testResults.get(i);
            String currentResult = randomResults();
            scenario.setActual_result(currentResult);
        }
        return testResults;
    }

    private static String randomResults() {
        String[] randomResults = {"successful login", "login failed"};
        Random random = new Random();
        int select = random.nextInt(randomResults.length);
        String result = randomResults[select];
        return result;
    }

    private static List<Scenario> parseFileIntoListOfScenarios(FileInputStream input) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Scenario> myList = new ArrayList();

        JsonFactory jf = new JsonFactory();
        JsonParser jp = jf.createParser(input);
        jp.setCodec(objectMapper);
        jp.nextToken();
        while (jp.hasCurrentToken()) {
            Scenario scenario = jp.readValueAs(Scenario.class);
            myList.add(scenario);
            jp.nextToken();
        }
        return myList;
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






