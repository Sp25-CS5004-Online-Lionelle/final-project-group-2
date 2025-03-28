package skillzhunter.model.formatters;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

// import student.model.DomainNameModel.DNRecord;

/**
 * A class to format the data in different ways.
 */
public final class DataFormatter {

    /**
     * Private constructor to prevent instantiation.
     */
    private DataFormatter() {
        // empty
    }

    /**
     * Pretty print the data in a human readable format.
     *
     * @param records the records to print
     * @param out the output stream to write to
     */
    private static void prettyPrint(Collection<DNRecord> records, OutputStream out) {
        PrintStream pout = new PrintStream(out); // so i can use println
        for (DNRecord record : records) {
            prettySingle(record, pout);
            pout.println();
        }
    }

    /**
     * Pretty print a single record.
     *
     * Let this as an example, so you didn't have to worry about spacing.
     *
     * @param record the record to print
     * @param out the output stream to write to
     */
    private static void prettySingle(@Nonnull DNRecord record, @Nonnull PrintStream out) {
        out.println(record.hostname());
        out.println("             IP: " + record.ip());
        out.println("       Location: " + record.city() + ", " + record.region() + ", "
                + record.country() + ", " + record.postal());
        out.println("    Coordinates: " + record.latitude() + ", " + record.longitude());

    }

    /**
     * Write the data as XML.
     *
     * @param records the records to write
     * @param out the output stream to write to
     */
    private static void writeXmlData(Collection<DNRecord> records, OutputStream out) {
        ObjectMapper mapper = new XmlMapper();
        DomainXmlWrapper wrapper = new DomainXmlWrapper(records);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(out, wrapper);
        } catch (IOException e) {
            throw new RuntimeException("Error writing XML data");
    }
}


    /**
     * Write the data as JSON.
     *
     * @param records the records to write
     * @param out the output stream to write to
     */
    private static void writeJsonData(Collection<DNRecord> records, OutputStream out) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(out, records);
        } catch (IOException e) {
           throw new RuntimeException("Error writing JSON data");
        }
    }

    /**
     * Write the data as CSV.
     *
     * @param records the records to write
     * @param out the output stream to write to
     */
    private static void writeCSVData(Collection<DNRecord> records, OutputStream out) {
                CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(DNRecord.class).withHeader();
        try {
            mapper.writer(schema).writeValues(out).writeAll(records);
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV data");
        }
    }

    /**
     * Write the data in the specified format.
     *
     * @param records the records to write
     * @param format the format to write the records in
     * @param out the output stream to write to
     */
    public static void write(@Nonnull Collection<DNRecord> records, @Nonnull Formats format,
            @Nonnull OutputStream out) {

        switch (format) {
            case XML:
                writeXmlData(records, out);
                break;
            case JSON:
                writeJsonData(records, out);
                break;
            case CSV:
                writeCSVData(records, out);
                break;
            default:
                prettyPrint(records, out);

        }
    }



}
