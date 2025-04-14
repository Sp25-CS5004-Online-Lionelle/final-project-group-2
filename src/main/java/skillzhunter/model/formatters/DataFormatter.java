package skillzhunter.model.formatters;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

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

    /**
     * Export the job records to a CSV file.
     *  This method will escape HTML tags.
     *  This method will also cut down
     * @param jobs
     * @param filePath
     */
    public static void exportCustomCSV(List<JobRecord> jobs, String filePath) {
        // Create a File object to check directory existence
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        
        // Ensure the directory exists
        if (parentDir != null && !parentDir.exists()) {
            boolean dirCreated = parentDir.mkdirs();
            if (!dirCreated) {
                System.err.println("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            
            writer.write("id,url,jobSlug,jobTitle,companyName,companyLogo,"
            + "jobIndustry,jobType,jobGeo,jobLevel,jobExcerpt,jobDescription,pubDate," 
            + "annualSalaryMin,annualSalaryMax,salaryCurrency,rating,comments");
            writer.newLine();

            for (JobRecord job : jobs) {
                StringBuilder line = new StringBuilder();
                line.append(job.id()).append(",");
                line.append(escapeCSV(job.url())).append(",");
                line.append(escapeCSV(job.jobSlug())).append(",");
                line.append(escapeCSV(job.jobTitle())).append(",");
                line.append(escapeCSV(job.companyName())).append(",");
                line.append(escapeCSV(job.companyLogo())).append(",");

                if (job.jobIndustry() != null && !job.jobIndustry().isEmpty()) {
                    line.append(escapeCSV(String.join(", ", job.jobIndustry())));
                }
                line.append(",");

                if (job.jobType() != null && !job.jobType().isEmpty()) {
                    line.append(escapeCSV(String.join(", ", job.jobType())));
                }
                line.append(",");

                line.append(escapeCSV(job.jobGeo())).append(",");
                line.append(escapeCSV(job.jobLevel())).append(",");

                String excerpt = "";
                if (job.jobExcerpt() != null && !job.jobExcerpt().isEmpty()) {
                    excerpt = extractFirstSentence(stripHTML(job.jobExcerpt()));
                }
                line.append(escapeCSV(excerpt)).append(",");

                String description = "Position at " + job.companyName() + " - " + job.jobTitle();
                line.append(escapeCSV(description)).append(",");

                line.append(escapeCSV(job.pubDate())).append(",");
                line.append(job.annualSalaryMin()).append(",");
                line.append(job.annualSalaryMax()).append(",");
                line.append(escapeCSV(job.salaryCurrency())).append(",");
                line.append(job.rating()).append(",");
                line.append(escapeCSV(job.comments()));

                writer.write(line.toString());
                writer.newLine();
                
                // Force flush after each record to ensure data is written
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing CSV file: " + e.getMessage());
            throw new RuntimeException("Failed to save jobs to CSV: " + e.getMessage(), e);
        }
    }

private static String stripHTML(String html) {
    if (html == null) {
        return "";
    }
    return html.replaceAll("<[^>]*>", "").replaceAll("\\s+", " ").trim();
}

private static String extractFirstSentence(String text) {
    if (text == null || text.isEmpty()) {
        return "";
    }
    int endPos = -1;
    for (String endMark : new String[]{".", "!", "?"}) {
        int pos = text.indexOf(endMark);
        if (pos >= 0 && (endPos == -1 || pos < endPos)) {
            endPos = pos + 1;
        }
    }
    if (endPos > 0) {
        return text.substring(0, endPos).trim();
    }
    return text.length() > 100 ? text.substring(0, 97) + "..." : text;
}

private static String escapeCSV(String value) {
    if (value == null) {
        return "\"\"";
    }
    return "\"" + value.replace("\"", "\"\"") + "\"";
}


}
