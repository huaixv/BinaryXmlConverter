package org.example;

import android.annotation.NonNull;
import android.util.TypedXmlPullParser;
import com.android.internal.util.BinaryXmlPullParser;
import com.android.internal.util.XmlUtils;
import org.apache.commons.cli.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {


    private static final boolean DEBUG = true;

    /**
     * Creates a new {@link XmlPullParser} that reads XML documents using a
     * custom binary wire protocol which benchmarking has shown to be 8.5x
     * faster than {@code Xml.newFastPullParser()} for a typical
     * {@code packages.xml}.
     *
     * @hide
     */
    public static @NonNull TypedXmlPullParser newBinaryPullParser() {
        return new BinaryXmlPullParser();
    }


    public static void dumpXml(String inputFile) {
        String inputFileName = "input.xml";  // Change this to the input binary XML file path
        try {
            FileInputStream fis = new FileInputStream(inputFileName);
            BinaryXmlPullParser in = new BinaryXmlPullParser();
            in.setInput(fis, null);  // Initialize the parser with the input stream

            int event;
            while ((event = in.next()) != XmlPullParser.END_DOCUMENT) {
                // Skip the END_TAG event, since we are only interested in START_TAG
                if (event == XmlPullParser.START_TAG) {
                    String name = in.getName();  // Get the name of the current tag
                    if (DEBUG) {
                        System.out.println("START_TAG name=" + name);  // Log the tag name
                    }

                    // Dump the attributes (if any)
                    int attributeCount = in.getAttributeCount();
                    if (attributeCount > 0) {
                        for (int i = 0; i < attributeCount; i++) {
                            String attributeName = in.getAttributeName(i);
                            String attributeValue = in.getAttributeValue(i);
                            if (DEBUG) {
                                System.out.println("  Attribute name=" + attributeName + ", value=" + attributeValue);
                            }
                        }
                    }
                }
                // Skip the current tag (whether it's START_TAG or END_TAG)
                XmlUtils.skipCurrentTag(in);
            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }


    private static void bin2TextConversion(String inputFile, String outputFileName) {

        dumpXml(inputFile);
    }


    public static void main(String[] args) {
        Options options = new Options();

        // Define options
        Option inputFormat = new Option("I", "inputFormat", true, "Input format (bin or text)");
        Option outputFormat = new Option("O", "outputFormat", true, "Output format (bin or text)");
        Option outputFile = new Option("o", "output", true, "Output file name");

        // Add options
        options.addOption(inputFormat);
        options.addOption(outputFormat);
        options.addOption(outputFile);

        // Define a positional argument (input file)
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            // Parse command line arguments
            CommandLine cmd = parser.parse(options, args, true);

            // Positional argument (input file)
            String inputFile = cmd.getArgs().length > 0 ? cmd.getArgs()[0] : null;

            if (inputFile == null) {
                System.out.println("Error: Input file is required.");
                formatter.printHelp("XmlTransformer", options);
                System.exit(1);
            }

            // Parse other options
            String inputFmt = cmd.getOptionValue("I");
            String outputFmt = cmd.getOptionValue("O");
            String outputFileName = cmd.getOptionValue("o");

            // Validate input and output formats
            if (inputFmt == null || outputFmt == null) {
                System.out.println("Error: Both input format (-I) and output format (-O) are required.");
                formatter.printHelp("XmlTransformer", options);
                System.exit(1);
            }

            if (!(inputFmt.equals("bin") || inputFmt.equals("text"))) {
                System.out.println("Error: Input format must be either 'bin' or 'text'.");
                formatter.printHelp("XmlTransformer", options);
                System.exit(1);
            }

            if (!(outputFmt.equals("bin") || outputFmt.equals("text"))) {
                System.out.println("Error: Output format must be either 'bin' or 'text'.");
                formatter.printHelp("XmlTransformer", options);
                System.exit(1);
            }

            if (inputFmt.equals(outputFmt)) {
                System.out.println("Error: Input and output formats cannot be the same.");
                formatter.printHelp("XmlTransformer", options);
                System.exit(1);
            }

            // Perform conversion based on input and output formats
            if (inputFmt.equals("bin") && outputFmt.equals("text")) {
                bin2TextConversion(inputFile, outputFileName);
            } else if (inputFmt.equals("text") && outputFmt.equals("bin")) {
//                text2BinConversion(inputFile, outputFileName);
            } else {

            }


        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("XmlTransformer", options);
            System.exit(1);
        }


    }

}