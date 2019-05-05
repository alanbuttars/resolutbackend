package com.alanbuttars.resolut.backend;

import java.io.*;

/**
 * A collection of utility functions.
 */
public class Utils {

    /**
     * Reads a given {@link InputStream} to its entirety and returns the contents.
     *
     * @throws IOException
     */
    public static String readResponse(InputStream inputStream) throws IOException {
        try (Reader reader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            StringBuilder stringBuilder = new StringBuilder();
            String output = null;
            while ((output = bufferedReader.readLine()) != null) {
                stringBuilder.append(output);
            }

            return stringBuilder.toString();
        }
    }
}
