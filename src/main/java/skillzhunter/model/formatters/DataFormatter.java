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

    /**
     * Strips HTML tags from a string.
     * 
     * @param html The string possibly containing HTML tags
     * @return The string with HTML tags removed
     */
    public static String stripHTML(String html) {
        if (html == null) {
            return "";
        }
        return html.replaceAll("<[^>]*>", "").replaceAll("\\s+", " ").trim();
    }

    /**
     * Extracts the first sentence from a text string.
     * 
     * @param text The text to extract from
     * @return The first sentence or a truncated version if no end marker found
     */
    public static String extractFirstSentence(String text) {
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

    /**
     * Escapes special characters for CSV format.
     * 
     * @param value The value to escape
     * @return The escaped value
     */
    public static String escapeCSV(String value) {
        if (value == null) {
            return "\"\"";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    /**
     * Replaces common HTML special characters in a string with more comprehensive handling.
     * This method is for text content processing, especially for job descriptions and excerpts.
     *
     * @param text Text with potential HTML special characters
     * @return Text with replaced HTML special characters
     */
    public static String replaceHtmlEntities(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Create a more comprehensive map of HTML entities and their replacements
        java.util.Map<String, String> htmlEntities = new java.util.HashMap<>();
        htmlEntities.put("&amp;", "&");
        htmlEntities.put("&lt;", "<");
        htmlEntities.put("&gt;", ">");
        htmlEntities.put("&quot;", "\"");
        htmlEntities.put("&#39;", "'");
        htmlEntities.put("&apos;", "'");
        htmlEntities.put("&nbsp;", " ");
        htmlEntities.put("&ndash;", "–");
        htmlEntities.put("&mdash;", "—");
        htmlEntities.put("&lsquo;", "'");
        htmlEntities.put("&rsquo;", "'");
        htmlEntities.put("&ldquo;", "\"");
        htmlEntities.put("&rdquo;", "\"");
        htmlEntities.put("&bull;", "•");
        htmlEntities.put("&copy;", "©");
        htmlEntities.put("&reg;", "®");
        htmlEntities.put("&trade;", "™");
        htmlEntities.put("&euro;", "€");
        htmlEntities.put("&pound;", "£");
        htmlEntities.put("&yen;", "¥");
        htmlEntities.put("&cent;", "¢");

        // First handle nested entities (like &amp;amp;) by applying multiple passes
        String result = text;
        String prevResult;
        do {
            prevResult = result;
            for (java.util.Map.Entry<String, String> entry : htmlEntities.entrySet()) {
                result = result.replace(entry.getKey(), entry.getValue());
            }
        } while (!result.equals(prevResult));

        // Handle numeric entities (like &#123;)
        java.util.regex.Pattern numericPattern = java.util.regex.Pattern.compile("&#(\\d+);");
        java.util.regex.Matcher numericMatcher = numericPattern.matcher(result);
        StringBuilder numericBuilder = new StringBuilder();
        while (numericMatcher.find()) {
            try {
                String numStr = numericMatcher.group(1);
                int code = Integer.parseInt(numStr);
                numericMatcher.appendReplacement(numericBuilder, String.valueOf((char) code));
            } catch (NumberFormatException e) {
                numericMatcher.appendReplacement(numericBuilder, numericMatcher.group(0));
            } catch (IllegalArgumentException e) {
                // In case of invalid replacement or any other issues
                numericMatcher.appendReplacement(numericBuilder, numericMatcher.group(0));
            }
        }
        numericMatcher.appendTail(numericBuilder);
        result = numericBuilder.toString();

        // Handle hexadecimal entities (like &#x1F600;)
        java.util.regex.Pattern hexPattern = java.util.regex.Pattern.compile("&#[xX]([0-9a-fA-F]+);");
        java.util.regex.Matcher hexMatcher = hexPattern.matcher(result);
        StringBuilder hexBuilder = new StringBuilder();
        while (hexMatcher.find()) {
            try {
                String hex = hexMatcher.group(1);
                int code = Integer.parseInt(hex, 16);
                String replacement = String.valueOf(Character.toChars(code));
                // Need to quote the replacement to avoid special character issues
                hexMatcher.appendReplacement(hexBuilder, java.util.regex.Matcher.quoteReplacement(replacement));
            } catch (Exception e) {
                hexMatcher.appendReplacement(hexBuilder, hexMatcher.group(0));
            }
        }
        hexMatcher.appendTail(hexBuilder);
        result = hexBuilder.toString();

        // Log a message if we detect any remaining HTML entities
        if (result.matches(".*&[a-zA-Z0-9#]+;.*")) {
            System.out.println("Warning: Possible unhandled HTML entity in: " + result);
        }

        return result;
    }

    /**
     * Process all HTML content in a JobRecord, removing entities and cleaning text.
     * This method can be used to clean up job data before displaying or saving.
     * 
     * @param job The JobRecord to process
     * @return A new JobRecord with HTML entities replaced and content cleaned
     */
    public static JobRecord processJobHtml(JobRecord job) {
        if (job == null) {
            return null;
        }
        
        // Create a new JobBean and clean all HTML content
        skillzhunter.model.JobBean bean = new skillzhunter.model.JobBean();
        
        // Copy values, cleaning HTML where needed
        bean.setId(job.id());
        bean.setUrl(job.url());
        bean.setJobSlug(job.jobSlug());
        bean.setJobTitle(job.jobTitle() != null ? replaceHtmlEntities(job.jobTitle()) : job.jobTitle());
        bean.setCompanyName(job.companyName() != null ? replaceHtmlEntities(job.companyName()) : job.companyName());
        bean.setCompanyLogo(job.companyLogo());
        bean.setJobIndustry(job.jobIndustry());
        bean.setJobType(job.jobType());
        bean.setJobGeo(job.jobGeo() != null ? replaceHtmlEntities(job.jobGeo()) : job.jobGeo());
        bean.setJobLevel(job.jobLevel() != null ? replaceHtmlEntities(job.jobLevel()) : job.jobLevel());
        
        // These fields often contain more HTML
        bean.setJobExcerpt(job.jobExcerpt() != null ? replaceHtmlEntities(job.jobExcerpt()) : job.jobExcerpt());
        bean.setJobDescription(job.jobDescription() != null ? replaceHtmlEntities(job.jobDescription()) : job.jobDescription());
        
        bean.setPubDate(job.pubDate());
        bean.setAnnualSalaryMin(job.annualSalaryMin());
        bean.setAnnualSalaryMax(job.annualSalaryMax());
        bean.setSalaryCurrency(job.salaryCurrency());
        bean.setRating(job.rating());
        bean.setComments(job.comments() != null ? replaceHtmlEntities(job.comments()) : job.comments());
        
        return bean.toRecord();
    }
}
