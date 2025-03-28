package skillzhunter.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "url", "jobSlug", "jobTitle", "companyName", "companyLogo",
"jobIndustry", "jobType", "jobGeo", "jobLevel", "jobExcerpt", 
"jobDescription", "pubDate", "annualSalaryMin", "annualSalaryMax", "salaryCurrency"})
public record JobRecord(
int id,
String url,
String jobSlug,
String jobTitle,
String companyName,
String companyLogo,
List<String> jobIndustry,
List<String> jobType,
String jobGeo,
String jobLevel,
String jobExcerpt,
String jobDescription,
String pubDate,
int annualSalaryMin,
int annualSalaryMax,
String salaryCurrency
) {}
