package skillzhunter.model;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class JobBean {

    private int id;
    private String url;
    private String jobSlug;
    private String jobTitle;
    private String companyName;
    private String companyLogo;
    private List<String> jobIndustry;
    private List<String> jobType;
    private String jobGeo;
    private String jobLevel;
    private String jobExcerpt;
    private String jobDescription;
    private String pubDate;
    private int annualSalaryMin;
    private int annualSalaryMax;
    private String salaryCurrency;

    public JobBean() {
        // no-arg constructor for Jackson and JavaBeans
    }

    // Getters and setters for all fields

    /**
     * Get the ID of the job.
     * @return the ID of the job
     */
    public int getId() {
        return id;
    }

    /**
     * Set the ID of the job.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the URL of the job.
     * @return the URL of the job
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the URL of the job.
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get the slug of the job.
     * @return the slug of the job
     */
    public String getJobSlug() {
        return jobSlug;
    }

    /**
     * Set the slug of the job.
     * @param jobSlug
     */
    public void setJobSlug(String jobSlug) {
        this.jobSlug = jobSlug;
    }

    /**
     * Get the title of the job.
     * @return the title of the job
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Set the title of the job.
     * @param jobTitle
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Get the name of the company.
     * @return the name of the company
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Set the name of the company.
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Get the logo of the company.
     * @return the logo of the company
     */
    public String getCompanyLogo() {
        return companyLogo;
    }

    /**
     * Set the logo of the company.
     * @param companyLogo
     */
    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    /**
     * Get the industry of the job.
     * @return the industry of the job
     */
    public List<String> getJobIndustry() {
        return jobIndustry;
    }

    /**
     * Set the industry of the job.
     * @param jobIndustry
     */
    public void setJobIndustry(List<String> jobIndustry) {
        this.jobIndustry = jobIndustry;
    }

    /**
     * Get the type of the job.
     * @return the type of the job
     */
    public List<String> getJobType() {
        return jobType;
    }

    /**
     * Set the type of the job.
     * @param jobType
     */
    public void setJobType(List<String> jobType) {
        this.jobType = jobType;
    }

    /**
     * Get the geographical location of the job.
     * @return the geographical location of the job
     */
    public String getJobGeo() {
        return jobGeo;
    }

    /**
     * Set the geographical location of the job.
     * @param jobGeo
     */
    public void setJobGeo(String jobGeo) {
        this.jobGeo = jobGeo;
    }

    /**
     * Get the level of the job.
     * @return the level of the job
     */
    public String getJobLevel() {
        return jobLevel;
    }

    /**
     * Set the level of the job.
     * @param jobLevel
     */
    public void setJobLevel(String jobLevel) {
        this.jobLevel = jobLevel;
    }

    /**
     * Get the excerpt of the job.
     * @return the excerpt of the job
     */
    public String getJobExcerpt() {
        return jobExcerpt;
    }

    /**
     * Set the excerpt of the job.
     * @param jobExcerpt
     * 
     */
    public void setJobExcerpt(String jobExcerpt) {
        this.jobExcerpt = jobExcerpt;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public int getAnnualSalaryMin() {
        return annualSalaryMin;
    }

    public void setAnnualSalaryMin(int annualSalaryMin) {
        this.annualSalaryMin = annualSalaryMin;
    }

    public int getAnnualSalaryMax() {
        return annualSalaryMax;
    }

    public void setAnnualSalaryMax(int annualSalaryMax) {
        this.annualSalaryMax = annualSalaryMax;
    }

    public String getSalaryCurrency() {
        return salaryCurrency;
    }

    public void setSalaryCurrency(String salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }

    //add comments and records to the bean and record
    @Override
    public String toString() {
        return "JobBean{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", jobSlug='" + jobSlug + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyLogo='" + companyLogo + '\'' +
                ", jobIndustry=" + jobIndustry +
                ", jobType=" + jobType +
                ", jobGeo='" + jobGeo + '\'' +
                ", jobLevel='" + jobLevel + '\'' +
                ", jobExcerpt='" + jobExcerpt + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", annualSalaryMin=" + annualSalaryMin +
                ", annualSalaryMax=" + annualSalaryMax +
                ", salaryCurrency='" + salaryCurrency + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Returns a hash code value for the object.
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Convert the domain object to a record object.
     * @return DNRecord with all details
     */
    public JobRecord toRecord() {
        return new JobRecord(
                this.getId(),
                this.getUrl(),
                this.getJobSlug(),
                this.getJobTitle(),
                this.getCompanyName(),
                this.getCompanyLogo(),
                this.getJobIndustry(),
                this.getJobType(),
                this.getJobGeo(),
                this.getJobLevel(),
                this.getJobExcerpt(),
                this.getJobDescription(),
                this.getPubDate(),
                this.getAnnualSalaryMin(),
                this.getAnnualSalaryMax(),
                this.getSalaryCurrency()
        );
    }
}
