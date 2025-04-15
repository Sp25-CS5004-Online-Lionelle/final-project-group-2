package skillzhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import skillzhunter.model.JobBean;

import static org.junit.jupiter.api.Assertions.*;

public class TestJobBean {

  private JobBean jobBean;
  private final int TEST_ID = 123;
  private final String TEST_URL = "https://example.com/job/123";
  private final String TEST_JOB_SLUG = "software-engineer";
  private final String TEST_JOB_TITLE = "Software Engineer";
  private final String TEST_COMPANY_NAME = "Tech Corp";
  private final String TEST_COMPANY_LOGO = "https://example.com/logo.png";
  private final List<String> TEST_JOB_INDUSTRY = Arrays.asList("Technology", "Software");
  private final List<String> TEST_JOB_TYPE = Arrays.asList("Full-time", "Remote");
  private final String TEST_JOB_GEO = "San Francisco, CA";
  private final String TEST_JOB_LEVEL = "Mid-level";
  private final String TEST_JOB_EXCERPT = "Join our team as a software engineer...";
  private final String TEST_JOB_DESCRIPTION = "Detailed job description goes here...";
  private final String TEST_PUB_DATE = "2025-04-01";
  private final int TEST_ANNUAL_SALARY_MIN = 100000;
  private final int TEST_ANNUAL_SALARY_MAX = 150000;
  private final String TEST_SALARY_CURRENCY = "USD";
  private final int TEST_RATING = 5;
  private final String TEST_COMMENTS = "Great opportunity!";

  @BeforeEach
  public void setUp() {
    jobBean = new JobBean();
    jobBean.setId(TEST_ID);
    jobBean.setUrl(TEST_URL);
    jobBean.setJobSlug(TEST_JOB_SLUG);
    jobBean.setJobTitle(TEST_JOB_TITLE);
    jobBean.setCompanyName(TEST_COMPANY_NAME);
    jobBean.setCompanyLogo(TEST_COMPANY_LOGO);
    jobBean.setJobIndustry(TEST_JOB_INDUSTRY);
    jobBean.setJobType(TEST_JOB_TYPE);
    jobBean.setJobGeo(TEST_JOB_GEO);
    jobBean.setJobLevel(TEST_JOB_LEVEL);
    jobBean.setJobExcerpt(TEST_JOB_EXCERPT);
    jobBean.setJobDescription(TEST_JOB_DESCRIPTION);
    jobBean.setPubDate(TEST_PUB_DATE);
    jobBean.setAnnualSalaryMin(TEST_ANNUAL_SALARY_MIN);
    jobBean.setAnnualSalaryMax(TEST_ANNUAL_SALARY_MAX);
    jobBean.setSalaryCurrency(TEST_SALARY_CURRENCY);
    jobBean.setRating(TEST_RATING);
    jobBean.setComments(TEST_COMMENTS);
  }

  @Test
  public void testDefaultConstructor() {
    JobBean emptyBean = new JobBean();
    assertNotNull(emptyBean);
  }

  @Test
  public void testGettersAndSetters() {
    assertEquals(TEST_ID, jobBean.getId());
    assertEquals(TEST_URL, jobBean.getUrl());
    assertEquals(TEST_JOB_SLUG, jobBean.getJobSlug());
    assertEquals(TEST_JOB_TITLE, jobBean.getJobTitle());
    assertEquals(TEST_COMPANY_NAME, jobBean.getCompanyName());
    assertEquals(TEST_COMPANY_LOGO, jobBean.getCompanyLogo());
    assertEquals(TEST_JOB_INDUSTRY, jobBean.getJobIndustry());
    assertEquals(TEST_JOB_TYPE, jobBean.getJobType());
    assertEquals(TEST_JOB_GEO, jobBean.getJobGeo());
    assertEquals(TEST_JOB_LEVEL, jobBean.getJobLevel());
    assertEquals(TEST_JOB_EXCERPT, jobBean.getJobExcerpt());
    assertEquals(TEST_JOB_DESCRIPTION, jobBean.getJobDescription());
    assertEquals(TEST_PUB_DATE, jobBean.getPubDate());
    assertEquals(TEST_ANNUAL_SALARY_MIN, jobBean.getAnnualSalaryMin());
    assertEquals(TEST_ANNUAL_SALARY_MAX, jobBean.getAnnualSalaryMax());
    assertEquals(TEST_SALARY_CURRENCY, jobBean.getSalaryCurrency());
    assertEquals(TEST_RATING, jobBean.getRating());
    assertEquals(TEST_COMMENTS, jobBean.getComments());
  }

  @Test
  public void testToString() {
    String toString = jobBean.toString();

    assertTrue(toString.contains("id=" + TEST_ID));
    assertTrue(toString.contains("url='" + TEST_URL + "'"));
    assertTrue(toString.contains("jobSlug='" + TEST_JOB_SLUG + "'"));
    assertTrue(toString.contains("jobTitle='" + TEST_JOB_TITLE + "'"));
    assertTrue(toString.contains("companyName='" + TEST_COMPANY_NAME + "'"));
    assertTrue(toString.contains("companyLogo='" + TEST_COMPANY_LOGO + "'"));
    assertTrue(toString.contains("jobIndustry=" + TEST_JOB_INDUSTRY));
    assertTrue(toString.contains("jobType=" + TEST_JOB_TYPE));
    assertTrue(toString.contains("jobGeo='" + TEST_JOB_GEO + "'"));
    assertTrue(toString.contains("jobLevel='" + TEST_JOB_LEVEL + "'"));
    assertTrue(toString.contains("jobExcerpt='" + TEST_JOB_EXCERPT + "'"));
    assertTrue(toString.contains("jobDescription='" + TEST_JOB_DESCRIPTION + "'"));
    assertTrue(toString.contains("pubDate='" + TEST_PUB_DATE + "'"));
    assertTrue(toString.contains("annualSalaryMin=" + TEST_ANNUAL_SALARY_MIN));
    assertTrue(toString.contains("annualSalaryMax=" + TEST_ANNUAL_SALARY_MAX));
    assertTrue(toString.contains("salaryCurrency='" + TEST_SALARY_CURRENCY + "'"));
    assertTrue(toString.contains("rating=" + TEST_RATING));
    assertTrue(toString.contains("comments='" + TEST_COMMENTS + "'"));
  }
}