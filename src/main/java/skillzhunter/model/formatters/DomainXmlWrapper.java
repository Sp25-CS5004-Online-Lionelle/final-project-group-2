package skillzhunter.model.formatters;

import java.util.Collection;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import skillzhunter.model.JobRecord;

/**
 * This wrapper helps when using Jackson to serialize a list of domain records to xml. Without this,
 * it tries to use <ArrayList> and <item> tags instead of <domainList> and <domain> tags.
 *
 * Suggested use (note you need try/catch with this)
 *
 * <pre>
 * XmlMapper mapper = new XmlMapper();
 * mapper.enable(SerializationFeature.INDENT_OUTPUT);
 * DomainXmlWrapper wrapper = new DomainXmlWrapper(records);
 * mapper.writeValue(out, wrapper);
 * </pre>
 */
@JacksonXmlRootElement(localName = "domainList")
public final class DomainXmlWrapper {

    /** List of the records. */
    @JacksonXmlElementWrapper(useWrapping = false)
    private Collection<JobRecord> job;

    /**
     * Constructor.
     *
     * @param records the records to wrap
     */
    public DomainXmlWrapper(Collection<JobRecord> records) {
        this.job = records;
    }
}
