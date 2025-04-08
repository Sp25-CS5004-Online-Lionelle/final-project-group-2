package skillzhunter;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.util.Collections;
import java.util.List;



import skillzhunter.model.JobRecord;
import skillzhunter.model.formatters.DataFormatter;
import skillzhunter.model.formatters.Formats;
import skillzhunter.model.net.JobBoardApi;




public class TestJobBoardApi {
    private MockJobBoardApi jobBoardApi;
    private List<JobRecord> jobRecords;
    private String url;

    class MockJobBoardApi extends JobBoardApi {
        public String interceptedUrl;
    
        @Override
        protected List<JobRecord> searchApi(String url) {
            this.interceptedUrl = url;
            return Collections.emptyList(); // avoid real call
        }
    }

    @BeforeEach
    public void setUp() {
        jobBoardApi = new MockJobBoardApi();
    }

    @Test
    public void normalTestGetJobRecords() {
        // Normal Test Case
        jobRecords = jobBoardApi.getJobBoard("python", 5, "austria", "devops & sysadmin");
        url = jobBoardApi.interceptedUrl;
        assert url.equals("https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=python");
    }
    @Test
    public void testEmptyQuery() {
        // Edge Case: Empty Query
        jobRecords = jobBoardApi.getJobBoard("", 5, "austria", "devops & sysadmin");
        url = jobBoardApi.interceptedUrl;
        assert url.equals("https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=all");
    }
    @Test
    public void testNullQuery() {
        // Edge Case: Null Query
        jobRecords = jobBoardApi.getJobBoard(null, 5, "austria", "devops & sysadmin");
        url = jobBoardApi.interceptedUrl;
        assert url.equals("https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=all");
    }
    @Test
    public void testInvalidLocation() {
        // Test Invalid Location
        jobRecords = jobBoardApi.getJobBoard("python", 5, "invalid_location", "devops & sysadmin");
        url = jobBoardApi.interceptedUrl;
        // Should drop location from the URL
        assert url.equals("https://jobicy.com/api/v2/remote-jobs?count=5&industry=admin&tag=python");
    }
    @Test
    public void testInvalidIndustry() {
        // Test Invalid Industry
        jobRecords = jobBoardApi.getJobBoard("python", 5, "austria", "invalid_industry");
        url = jobBoardApi.interceptedUrl;
        // Should drop industry from the URL
        assert url.equals("https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&tag=python");
    }
    @Test
    public void testInvalidNumberOfResults() {
        // Test Invalid Number of Results
        jobRecords = jobBoardApi.getJobBoard("python", -1, "austria", "devops & sysadmin");
        url = jobBoardApi.interceptedUrl;
        assert url.equals("https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=python");
    }
    @Test
    public void testQueryOnly() {
        // test query only
        jobRecords = jobBoardApi.getJobBoard("python");
        url = jobBoardApi.interceptedUrl;
        assert url.equals("https://jobicy.com/api/v2/remote-jobs?count=5&tag=python");
    }
    @Test
    public void testQueryAndCount() {
        // test query and count
        jobRecords = jobBoardApi.getJobBoard("python", 10);
        url = jobBoardApi.interceptedUrl;
        assert url.equals("https://jobicy.com/api/v2/remote-jobs?count=10&tag=python");
    }


}
