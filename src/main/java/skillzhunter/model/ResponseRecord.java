package skillzhunter.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"apiVersion", "documentationUrl", "friendlyNotice",
                    "jobCount", "xRayHash", "clientKey", "lastUpdate", "jobs"})
public record ResponseRecord(
    String apiVersion,
    String documentationUrl,
    String friendlyNotice,
    int jobCount,
    String xRayHash,
    String clientKey,
    String lastUpdate,
    List<JobRecord> jobs
) { }
