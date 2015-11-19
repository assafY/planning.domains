package global;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class contains static methods shared by the server
 * and clients.
 */
public class Global {

    /**
     * Reads a process' input stream and returns a string
     * containing the output.
     *
     * @param is the process' input stream
     * @return String containing the output
     * @throws IOException
     */
    public static synchronized String getProcessOutput(InputStream is) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                stringBuilder.append(currentLine + System.getProperty("line.separator"));
            }
        } finally {
            reader.close();
        }

        return stringBuilder.toString();
    }
}
