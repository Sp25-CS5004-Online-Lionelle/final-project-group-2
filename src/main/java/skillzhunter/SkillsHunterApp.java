package skillzhunter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import skillzhunter.controller.MainController;

public class SkillsHunterApp {
    static final String DEFAULT_DATA_FILE = "data\\temp_data.xml";

    private SkillsHunterApp() {
        // empty
    }

    /**
     * Returns the help message for the program.
     * @return the help message
     */
    public static String getHelp() {
        return """
                Welcome to Skillz Hunter!
                This is a command line application and gui application that helps you find jobs based on your skills.
                Options:
                -c <console/gui> : Specify the controller to use. Default is gui.
                -g : Specify the controller to use. Default is gui.
                -f <format> : Specify the format of the output file. Default is xml.
                -o <outputFilePath> : Specify the output file path. Default is output.txt.
                --data <dataFilePath> : Specify the data file path. Default is data/temp_data.xml.
                -h, --help : Show this help message.
                """;
        }

    /**
     * Parses command line arguments and then returns them as a map.
     * @param args
     * @return Map<String, String> parsedArgs
     */
    public static Map<String, String> getParserArgs(String[] args) {

        ArrayList<String> remainingArgs = new ArrayList<>(List.of(args));
        Map<String, String> parsedArgs = new TreeMap();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-c":
                    if (args[i + 1].equals("console")) {
                        parsedArgs.put("controller", "console");
                    } else {
                        parsedArgs.put("controller", "gui");
                    }
                    i++;
                    break;
                case "-f":
                    parsedArgs.put("format", args[i + 1]);
                    i++;
                    break;
                case "-o":
                    parsedArgs.put("outputFilePath", args[i + 1]);
                    i++;
                    break;
                case "--data":
                    parsedArgs.put("dataFilePath", args[i + 1]);
                    i++;
                    break;
                case "-h":
                case "--help":
                        System.out.println(getHelp());
                        break;
                case "-g":
                    parsedArgs.put("controller", "gui");
                    i++;
                    break;
                default:
                    continue;
            }
            remainingArgs.remove(args[i]);
            remainingArgs.remove(args[i - 1]);
            }
            // sets default controller to gui if not specified
            if (!parsedArgs.containsKey("controller")) {
                parsedArgs.put("controller", "gui");
            }
            // sets default controller to gui if not specified
            if (!parsedArgs.containsKey("dataFilePath")) {
                    parsedArgs.put("dataFilePath", DEFAULT_DATA_FILE);
            }
            if (parsedArgs.get("controller").equals("console")) {
                String hostname;
                if (!remainingArgs.isEmpty()) {
                    hostname = remainingArgs.get(0).equals("all") ? "all" : remainingArgs.get(0);
                } else {
                    hostname = "all";
                }
                parsedArgs.put("hostname", hostname);
            }
        return parsedArgs;
    }


    public static void main(String[] args) {

        // Example usage of getParserArgs method we can easily re-write this
        // for command line arguments later.
        String unparsed = "file -o output.txt -f xml -c console --data data.xml";
        String[] parsedArgs = unparsed.split(" ");
        Map<String, String> argsMap = getParserArgs(parsedArgs);
        System.out.println("Parsed Arguments: " + argsMap);


        MainController mainController = new MainController();
    }
}