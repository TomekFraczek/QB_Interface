import controller.QBOController;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.io.FileWriter;
import java.io.IOException;


@Configuration
@PropertySource(value="classpath:/application.properties", ignoreResourceNotFound=true)
public class JSONWriter {
    /**
     * Class to simplify writing out JSON files and safely handle Exceptions
     */

    @Autowired
    Environment env;

    // Get access to the console logger
    private static final Logger logger = Logger.getLogger(QBOController.class);

    // The directory for the fileWriter to write all files to
    private String directoryPath = env.getProperty("datapath");

    // Convert a JSON object to a pretty-printed string and write it out to the file
    public void write(String filename, JSONObject obj) {

        // Combine the given file name with the directory
        String fullFilename = this.directoryPath + filename + ".json";

        // Perform the write, logging wither success of failure
        try (FileWriter file = new FileWriter(fullFilename)) {
            file.write(obj.toString(2));
            logger.info("Successfully Copied JSON Object to File...");
        } catch (IOException e) {
            logger.warn("Object write FAILED!");
        }
    }

    // Write the given string to a file, ensuring that it is pretty-printed
    public void write(String filename, String jsonString) {

        // Convert the jsonString to a json object so it can get pretty printed
        JSONObject obj = new JSONObject(jsonString);

        // The basic write method can handle it from here on out
        this.write(filename, obj);
    }
}