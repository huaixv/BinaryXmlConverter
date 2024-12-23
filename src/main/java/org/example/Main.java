package org.example;

import android.util.TypedXmlPullParser;
import android.util.TypedXmlSerializer;
import android.util.Xml;
import org.apache.commons.cli.*;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.util.Xml.resolvePullParser;
import static android.util.Xml.resolveSerializer;


public class Main {



    public static void main(String[] args) {
        Options options = new Options();

        // Define options
        Option outputFormat = new Option("O", "outputFormat", true, "Output format (bin or text)");
        Option outputFile = new Option("o", "output", true, "Output file name");

        // Add options
        options.addOption(outputFormat);
        options.addOption(outputFile);

        // Define a positional argument (input file)
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            // Parse command line arguments
            CommandLine cmd = parser.parse(options, args, true);

            // Positional argument (input file)
            String inputFileName = cmd.getArgs().length > 0 ? cmd.getArgs()[0] : null;

            if (inputFileName == null) {
                System.out.println("Error: Input file is required.");
                formatter.printHelp("XmlTransformer", options);
                System.exit(1);
            }

            // Parse other options
            String outputFmt = cmd.getOptionValue("O");
            String outputFileName = cmd.getOptionValue("o");

            // Validate input and output formats
            if (outputFmt == null) {
                System.out.println("Error: Output format (-O) are required.");
                formatter.printHelp("XmlTransformer", options);
                System.exit(1);
            }

            if (!(outputFmt.equals("bin") || outputFmt.equals("text"))) {
                System.out.println("Error: Output format must be either 'bin' or 'text'.");
                formatter.printHelp("XmlTransformer", options);
                System.exit(1);
            }

            // Output parsed values
            System.out.println("Input File: " + inputFileName);
            System.out.println("Output File: " + outputFileName);
            System.out.println("Output Format: " + outputFmt);


            // Do conversion
            try {
                FileInputStream inputStream = new FileInputStream(inputFileName);
                TypedXmlPullParser xmlPullParser = resolvePullParser(inputStream);


                Boolean useBinary = outputFmt == "bin";
                FileOutputStream outputStream = new FileOutputStream(outputFileName);
                TypedXmlSerializer xmlSerializer = resolveSerializer(outputStream);

                Xml.copy(xmlPullParser, xmlSerializer);
                System.out.println("Output written to " + outputFileName);

            } catch (IOException | XmlPullParserException e) {
                throw new RuntimeException(e);
            }


        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("XmlTransformer", options);
            System.exit(1);
        }


    }

}