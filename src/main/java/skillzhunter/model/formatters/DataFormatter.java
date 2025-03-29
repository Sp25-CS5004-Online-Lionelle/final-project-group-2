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
import skillzhunter.model.JobRecord;

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
    private static void prettyPrint(Collection<JobRecord> records, OutputStream out) {
        PrintStream pout = new PrintStream(out); // so i can use println
        for (JobRecord record : records) {
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
    private static void prettySingle(@Nonnull JobRecord record, @Nonnull PrintStream out) {
        out.println(record.jobTitle());
        out.println("             Company: " + record.companyName());
        out.println("       Pay: " + record.annualSalaryMin() + " to " + record.annualSalaryMax());
        out.println("       Date Published: " + record.pubDate());
        out.println("       Description: " + record.jobDescription());

    }

    /**
     * Write the data as XML.
     *
     * @param records the records to write
     * @param out the output stream to write to
     */
    private static void writeXmlData(Collection<JobRecord> records, OutputStream out) {
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
    private static void writeJsonData(Collection<JobRecord> records, OutputStream out) {
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
    private static void writeCSVData(Collection<JobRecord> records, OutputStream out) {
                CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(JobRecord.class).withHeader();
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
    public static void write(@Nonnull Collection<JobRecord> records, @Nonnull Formats format,
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
