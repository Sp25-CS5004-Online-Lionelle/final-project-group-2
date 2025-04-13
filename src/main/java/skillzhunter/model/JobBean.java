package skillzhunter.model;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class JobBean {
    /**
     * ID of the job.
     */
    private int id;
    /**
     * URL of the job.
     */
    private String url;
    /**
     * Slug of the job.
     */
    private String jobSlug;
    /**
     * Title of the job.
     */
    private String jobTitle;
    /**
     * Name of the company.
     */
    private String companyName;
    /**
     * Logo of the company.
     */
    private String companyLogo;
    /**
     * Industry of the job.
     */
    private List<String> jobIndustry;
    /**
     * Type of the job.
     */
    private List<String> jobType;
    /**
     * Geographical location of the job.
     */
    private String jobGeo;
    /**
     * Level of the job.
     */
    private String jobLevel;
    /**
     * Excerpt of the job.
     */
    private String jobExcerpt;
    /**
     * Description of the job.
     */
    private String jobDescription;
    /**
     * Publication date of the job.
     */
    private String pubDate;
    /**
     * Minimum annual salary for the job.
     */
    private int annualSalaryMin;
    /**
     * Maximum annual salary for the job.
     */
    private int annualSalaryMax;
    /**
     * Currency of the salary.
     */
    private String salaryCurrency;
    /**
     * Rating of the job.
     */
    private int rating;
    /**
     * Comments about the job.
     */
    private String comments;
    /**
     * EMPTY constructor for Jackson and JavaBeans.
     */
    public JobBean() {
        // no-arg constructor for Jackson and JavaBeans
    }

    //IDEA:
    //constructor that accepts a JobRecord
    //OR
    //public void fromRecord(JobRecord record){...}

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
    /**
     * Get the description of the job.
     * @return the description of the job
     */
    public String getJobDescription() {
        return jobDescription;
    }
    /**
     * Set the description of the job.
     * @param jobDescription
     */
    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
    /**
     * Get the publication date of the job.
     * @return the publication date of the job
     */
    public String getPubDate() {
        return pubDate;
    }
    /**
     * Set the publication date of the job.
     * @param pubDate
     */
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
    /**
     * Get the minimum annual salary for the job.
     * @return the minimum annual salary for the job
     */
    public int getAnnualSalaryMin() {
        return annualSalaryMin;
    }
    /**
     * Set the minimum annual salary for the job.
     * @param annualSalaryMin
     */
    public void setAnnualSalaryMin(int annualSalaryMin) {
        this.annualSalaryMin = annualSalaryMin;
    }
    /**
     * Get the maximum annual salary for the job.
     * @return the maximum annual salary for the job
     */
    public int getAnnualSalaryMax() {
        return annualSalaryMax;
    }
    /**
     * Set the maximum annual salary for the job.
     * @param annualSalaryMax
     */
    public void setAnnualSalaryMax(int annualSalaryMax) {
        this.annualSalaryMax = annualSalaryMax;
    }
    /**
     * Get the currency of the salary.
     * @return the currency of the salary
     */
    public String getSalaryCurrency() {
        return salaryCurrency;
    }
    /**
     * Set the currency of the salary.
     * @param salaryCurrency
     */
    public void setSalaryCurrency(String salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }
    /**
     * Get the rating of the job.
     * @return the rating of the job
     */
    public int getRating() {
        return rating;
    }
    /**
     * Set the rating of the job.
     * @param rating
     */
    public void setRating(int rating) {
        this.rating = rating;
    }
    /**
     * Get the comments about the job.
     * @return the comments about the job
     */
    public String getComments() {
        return comments;
    }
    /**
     * Set the comments about the job.
     * @param comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
    /**
     * Get the job record.
     * @return the job record
     */
    @Override
    public String toString() {
        return "JobBean{"
                + "id=" + id
                + ", url='" + url + '\''
                + ", jobSlug='" + jobSlug + '\''
                + ", jobTitle='" + jobTitle + '\''
                + ", companyName='" + companyName + '\''
                + ", companyLogo='" + companyLogo + '\''
                + ", jobIndustry=" + jobIndustry
                + ", jobType=" + jobType
                + ", jobGeo='" + jobGeo + '\''
                + ", jobLevel='" + jobLevel + '\''
                + ", jobExcerpt='" + jobExcerpt + '\''
                + ", jobDescription='" + jobDescription + '\''
                + ", pubDate='" + pubDate + '\''
                + ", annualSalaryMin=" + annualSalaryMin
                + ", annualSalaryMax=" + annualSalaryMax
                + ", salaryCurrency='" + salaryCurrency + '\''
                + ", rating=" + rating
                + ", comments='" + comments + '\''
                + '}';
    }
    /**
     * Compare this jobrecord equality.
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            return EqualsBuilder.reflectionEquals(this, obj);
        }
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
                this.getSalaryCurrency(),
                this.getRating(),
                this.getComments()
        );
    }

}
