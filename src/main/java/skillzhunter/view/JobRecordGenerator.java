package skillzhunter.view;

import java.util.Arrays;
import java.util.List;
import skillzhunter.model.JobRecord;

//Class that makes some dummy JobRecords for testing purposes
public class JobRecordGenerator {
  public static void main(String[] args) {
    List<JobRecord> dummyRecords = generateDummyRecords(10);
  }

  public static List<JobRecord> generateDummyRecords(int count) {
    JobRecord[] records = new JobRecord[count];

    records[0] = new JobRecord(
        1001,
        "https://example.com/jobs/software-engineer-1001",
        "senior-software-engineer-java",
        "Senior Software Engineer (Java)",
        "TechCorp Solutions",
        "https://example.com/logos/techcorp.png",
        Arrays.asList("Technology", "Software Development"),
        Arrays.asList("Full-time", "Remote"),
        "San Francisco, CA",
        "Senior",
        "Join our team of expert Java developers building enterprise solutions.",
        "TechCorp is seeking a Senior Java Engineer to help architect and develop our cloud-based platform. Requirements include 5+ years of Java experience, knowledge of Spring Boot, and experience with AWS services.",
        "2025-02-15",
        120000,
        160000,
        "USD",
        5,
        "Passionate about Java and cloud technologies. Looking for a challenging role in a fast-paced environment."
    );

    records[1] = new JobRecord(
        1002,
        "https://example.com/jobs/data-scientist-1002",
        "data-scientist-ml",
        "Data Scientist - Machine Learning",
        "DataMinds Inc.",
        "https://example.com/logos/dataminds.png",
        Arrays.asList("Data Science", "Machine Learning"),
        Arrays.asList("Full-time", "Hybrid"),
        "Boston, MA",
        "Mid-level",
        "Apply ML techniques to solve complex business problems.",
        "We're looking for a talented data scientist to join our ML team. You'll work on projects involving NLP, recommendation systems, and predictive analytics using Python, TensorFlow, and PyTorch.",
        "2025-02-28",
        110000,
        145000,
        "USD",
        4,
        "Strong background in statistics and machine learning. Experience with big data technologies like Spark is a plus."
    );

    records[2] = new JobRecord(
        1003,
        "https://example.com/jobs/ux-designer-1003",
        "senior-ux-designer",
        "Senior UX Designer",
        "CreativeWorks",
        "https://example.com/logos/creativeworks.png",
        Arrays.asList("Design", "User Experience"),
        Arrays.asList("Contract", "On-site"),
        "Austin, TX",
        "Senior",
        "Shape the future of digital experiences for our clients.",
        "We're seeking a Senior UX Designer to lead design thinking workshops and create user-centered designs for web and mobile applications. Must have a strong portfolio and 4+ years experience.",
        "2025-03-05",
        95000,
        130000,
        "USD",
        3,
        "Creative thinker with a passion for user experience. Proficient in Figma, Sketch, and Adobe Creative Suite."
    );

    records[3] = new JobRecord(
        1004,
        "https://example.com/jobs/devops-engineer-1004",
        "devops-engineer-kubernetes",
        "DevOps Engineer (Kubernetes)",
        "CloudNative Systems",
        "https://example.com/logos/cloudnative.png",
        Arrays.asList("DevOps", "Cloud Infrastructure"),
        Arrays.asList("Full-time", "Remote"),
        "Toronto, ON",
        "Mid-level",
        "Build and maintain our cloud infrastructure and CI/CD pipelines.",
        "Looking for a DevOps engineer familiar with Kubernetes, Docker, and CI/CD pipelines. You'll help us automate deployments, manage our cloud infrastructure, and improve system reliability.",
        "2025-03-10",
        105000,
        135000,
        "CAD",
        4,
        "Experience with AWS or Azure is a plus. Strong scripting skills in Python or Bash are required."
    );

    records[4] = new JobRecord(
        1005,
        "https://example.com/jobs/product-manager-1005",
        "product-manager-fintech",
        "Product Manager - FinTech",
        "MoneyWise Solutions",
        "https://example.com/logos/moneywise.png",
        Arrays.asList("Product Management", "FinTech"),
        Arrays.asList("Full-time", "On-site"),
        "London, UK",
        "Senior",
        "Lead the development of innovative financial products.",
        "We're looking for an experienced Product Manager to oversee our personal finance application. You'll work with developers, designers, and stakeholders to define the product roadmap and deliver new features.",
        "2025-03-12",
        75000,
        95000,
        "GBP",
        5,
        "Experience in FinTech is a plus. Strong analytical skills and ability to work in a fast-paced environment."
    );

    records[5] = new JobRecord(
        1006,
        "https://example.com/jobs/mobile-developer-1006",
        "ios-developer-swift",
        "iOS Developer (Swift)",
        "MobileFirst Apps",
        "https://example.com/logos/mobilefirst.png",
        Arrays.asList("Mobile Development", "iOS"),
        Arrays.asList("Part-time", "Remote"),
        "Berlin, Germany",
        "Junior",
        "Create elegant and efficient mobile applications for iOS.",
        "Seeking a Swift developer to join our mobile team. You'll be responsible for developing and maintaining iOS applications, implementing new features, and ensuring app performance and stability.",
        "2025-03-15",
        45000,
        65000,
        "EUR",
        4,
        "Familiarity with SwiftUI and Combine is a plus. Strong problem-solving skills and attention to detail are required."
    );

    records[6] = new JobRecord(
        1007,
        "https://example.com/jobs/security-analyst-1007",
        "cybersecurity-analyst",
        "Cybersecurity Analyst",
        "SecureDefense",
        "https://example.com/logos/securedefense.png",
        Arrays.asList("Cybersecurity", "Information Security"),
        Arrays.asList("Full-time", "On-site"),
        "Singapore",
        "Mid-level",
        "Protect our systems and data from cyber threats.",
        "Join our security team to monitor, detect, and respond to security incidents. You'll perform vulnerability assessments, security audits, and help implement security best practices across the organization.",
        "2025-03-18",
        90000,
        120000,
        "SGD",
        3,
        "Experience with SIEM tools and incident response is required. Strong knowledge of network security and threat intelligence is a plus."
    );

    records[7] = new JobRecord(
        1008,
        "https://example.com/jobs/frontend-developer-1008",
        "frontend-react-developer",
        "Frontend React Developer",
        "WebUI Innovations",
        "https://example.com/logos/webui.png",
        Arrays.asList("Frontend Development", "Web Development"),
        Arrays.asList("Full-time", "Hybrid"),
        "Stockholm, Sweden",
        "Mid-level",
        "Create responsive and intuitive user interfaces using React.",
        "We're looking for a React developer with experience in modern frontend technologies. You'll work on building user-friendly interfaces, implementing UI components, and ensuring cross-browser compatibility.",
        "2025-03-20",
        50000,
        70000,
        "SEK",
        4,
        "Experience with Redux and RESTful APIs is a plus. Strong understanding of HTML, CSS, and JavaScript is required."
    );

    records[8] = new JobRecord(
        1009,
        "https://example.com/jobs/data-engineer-1009",
        "data-engineer-big-data",
        "Data Engineer (Big Data)",
        "BigDataAnalytics",
        "https://example.com/logos/bigdata.png",
        Arrays.asList("Data Engineering", "Big Data"),
        Arrays.asList("Full-time", "Remote"),
        "Melbourne, Australia",
        "Senior",
        "Design and build data pipelines for processing large datasets.",
        "Join our data team to design, build, and maintain our data processing infrastructure. Experience with Spark, Hadoop, and cloud-based data solutions required. You'll help us transform raw data into actionable insights.",
        "2025-03-22",
        130000,
        170000,
        "AUD",
        5,
        "Experience with ETL processes and data warehousing is a plus. Strong programming skills in Python or Scala are required."
    );

    records[9] = new JobRecord(
        1010,
        "https://example.com/jobs/project-manager-1010",
        "technical-project-manager",
        "Technical Project Manager",
        "DeliveryExperts",
        "https://example.com/logos/delivery.png",
        Arrays.asList("Project Management", "IT"),
        Arrays.asList("Full-time", "On-site"),
        "Tokyo, Japan",
        "Senior",
        "Lead technical projects from inception to successful delivery.",
        "Seeking an experienced Technical Project Manager to oversee software development projects. You'll coordinate cross-functional teams, manage project scope and timelines, and ensure successful delivery of high-quality solutions.",
        "2025-03-25",
        12000000,
        15000000,
        "JPY",
        4,
        "Strong background in software development and project management. Experience with Agile methodologies is a plus."
    );

    return Arrays.asList(records);
  }
}