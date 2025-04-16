# Skillz Hunter App Desing

The Diagram below outlines the general structure of the Skillz Hunter App.

# Initial Design

```mermaid
---
config:
  theme: mc
title: Skillz Hunter App Initial Design
---
classDiagram
Main *.. IController
Main *.. IView: uses
Main *.. Jobs: uses

IController *.. IView: uses
IController *.. Jobs: uses
IController <-- MainController: Inheritance
IController <-- SavedJobController: Inheritance
IController <-- FindJobController: Inheritance

MainController *.. SavedJobController: uses
MainController *.. FindJobController: uses

IView <-- MainView: Inheritance
IView <-- FindJobView: Inheritance
IView <-- SavedJobsView: Inheritance

MainView *.. FindJobView: use
MainView *.. SavedJobsView: use

Jobs *.. JobFilters: uses
Jobs *.. JobSorts: uses
Jobs *.. DataFormatters: uses
Jobs *.. NetUtils: uses

class Main{
<<driver>>
This will be the main driver for the entire program. Command-line argument parsing
will happen here and we will also select our controller.
------------------------------------------------
+ DNInfoApp()
+ getParsedArgs(String[]):HashMap~String, String~
+ getHelp(): void
+ main(String[]): void
}
namespace view {
class IView {
<<Interface>>
This will serve as an interface for our view. Defining it correctly early on will enable the 
team to interact and code seamlessly. We anticipate needing only the **addFeatures** argument, 
which will take the controller and map the appropriate functions.
+ addFeatures(IControler): void
}
class MainView {
The main view will serve as a base platform for all other views. It will incorporate 
**FindJobView** and **SaveJobsView** as tabs. Additionally, all pane logic will be mapped 
to the taskbar on the main page, including but not limited to data downloads and light/dark 
mode toggling.
---------------------------------------------------------------------------------------------------------------------------
- FindJobView findJob
- SavedJobsView savedJobs
+ getFindJobView() : FindJobView
+ getSavedJobsView() : FindJobView
}
class FindJobView {
This view will connect to an API to find relevant jobs based on a client's query. From there, the user will be able to view
and save jobs they like. Upon saving, the user must rate the job and provide a comment for each save.
}
class SavedJobsView {

This view will display all jobs saved from the **FindJobView**. On this page, users can view, edit, and delete their saved jobs.
When a user views a job, an API call will be made to fetch related skills for that job. The download feature will be available as 
a pane.

}
}
namespace Controller {
class IController {
<<Interface>>
The controller will have three main arguments: **setView**, **setModel**, and **run**. These will only be inherited by 
**MainController**.
+ setView(IView): void
+ setModel(IModel): void
+ getView(): IView
+ getModel(): IModel
}
class MainController {
This will set the main pane and implement the two tabs.
------------------------------------------------------
- SavedJobController jobController
- FindJobController findJobController
+ setView(IView): void
+ run(DataModel, IView): void
}
class SavedJobController {
This will implement the logic for review saved jobs.
}
class FindJobController {
This will set the main pane and implement the two tabs.
}
}
namespace model {
class Jobs {
- dbFile: str
+ searchJob(str): list<jobRecord>
+ addJobs(jobRecord)
+ removeJob(str)
+ getSavedJobs() : list<~jobRecord~>
+ getSavedJobs(str): list<~jobRecord~>
+ getSavedJobs(str, str): list<~jobRecord~>
+ getSavedJobs(str, str, str): list<~jobRecord~>
+ jobRecord(str, str, str)
}
class JobFilters {
Filtering logic
}
class JobSorts {
Sorting Logic for jobs
}
class DataFormatters {
Port over from HW 9 => helper functions for saving data
}
class NetUtils {
Port over from HW 9 => helper functions for get request
}
}

```









# Final Design
```mermaid
---
config:
  theme: mc
title: Skillz Hunter App - Comprehensive Design
---

classDiagram

%% Relationships Section
SkillzHunterApp --> IModel : creates
SkillzHunterApp --> IController : creates
SkillzHunterApp --> IView : creates
SkillzHunterApp --> Jobs : creates
SkillzHunterApp --> MainController : creates
SkillzHunterApp --> MainView : creates

IController <|-- MainController : implements
IController --> IModel : uses
IController --> IView : uses
IController --> JobRecord : uses

IModel <|.. Jobs : implements
IModel --> AlertListener : uses

MainController --> AlertListener : implements
MainView --> AlertObserver : implements
MainView --> IView : implements

Jobs --> JobBoardApi : uses
Jobs --> JobBoardApiResult : uses
Jobs --> DataFormatter : uses
Jobs --> QuerySuggestionService : contains
Jobs --> JobRecord : uses
Jobs --> JobBean : uses
Jobs --> ResponseRecord : uses

DataFormatter --> JobRecord : uses
DataFormatter --> Formats : uses
DataFormatter --> DomainXmlWrapper : uses

DomainXmlWrapper --> JobRecord : contains

JobBoardApi --> JobBoardApiResult : returns
JobBoardApi --> ResponseRecord : uses
JobBoardApi --> JobRecord : uses

JobView <|-- FindJobTab : extends
JobView <|-- SavedJobsTab : extends
JobView --> IController : uses
JobView --> JobsTable : contains
JobView --> ThemedButton : uses
JobView --> ColorTheme : uses

BaseJobDetailsDialogue <|-- JobDetailsDialogue : extends
BaseJobDetailsDialogue <|-- SavedJobDetailsDialogue : extends
BaseJobDetailsDialogue --> JobRecord : uses
BaseJobDetailsDialogue --> IconLoader : uses

JobDetailsDialogue --> IController : uses
JobDetailsDialogue --> JobActionHelper : uses
JobDetailsDialogue --> SavedJobDetailsDialogue : uses
JobDetailsDialogue --> StarRatingPanel : uses

SavedJobDetailsDialogue --> IController : uses
SavedJobDetailsDialogue --> JobActionHelper : uses
SavedJobDetailsDialogue --> ThemedButton : uses

FindJobTab --> IController : uses
FindJobTab --> SalaryVisualizationPanel : contains
FindJobTab --> JobRecord : uses
FindJobTab --> JobDetailsDialogue : uses
FindJobTab --> JobActionHelper : uses
FindJobTab --> IconLoader : uses

SavedJobsTab --> IController : uses
SavedJobsTab --> JobRecord : uses
SavedJobsTab --> JobActionHelper : uses
SavedJobsTab --> IconLoader : uses

JobActionHelper --> IController : uses
JobActionHelper --> JobRecord : uses
JobActionHelper --> IconLoader : uses
JobActionHelper --> StarRatingPanel : uses
JobActionHelper --> SavedJobsTab : accesses

CustomMenuBar --> ColorTheme : uses
CustomMenuBar --> IconLoader : uses

CustomHeader --> IconLoader : uses

JobsTable --> CustomHeader : uses
JobsTable --> ImageCellRenderer : contains
JobsTable --> ColorTheme : uses

TabStyleManager --> ColorTheme : uses

SalaryVisualizationPanel --> JobRecord : uses
SalaryVisualizationPanel --> ColorTheme : uses

ThemedButton --> ColorTheme : uses

ImageCellRenderer --> IconLoader : uses

JobBean --> JobRecord : creates

%% Class Definitions Section

class SkillsHunterApp {
    <<driver>>
    -SkillsHunterApp() 
    +main(String[] args): void
}

namespace controller {
    class IController {
        <<interface>>
        +setModel(IModel): void
        +setView(IView): void
        +registerAlertObserver(AlertObserver): void
        +unregisterAlertObserver(AlertObserver): void
        +getLocations(): List~String~
        +getIndustries(): List~String~
        +getModel(): IModel
        +getView(): IView
        +getApiCall(String, Integer, String, String): List~JobRecord~
        +getSavedJobs(): List~JobRecord~
        +setSavedJobs(List~JobRecord~): List~JobRecord~
        +isJobAlreadySaved(JobRecord): boolean
        +jobToSavedList(JobRecord): void
        +tryAddJobToSavedList(JobRecord): boolean
        +removeJobFromList(int): void
        +pathToCSV(String): void
        +exportToFileType(List~JobRecord~, String, String): void
        +getUpdateJob(int, String, int): JobRecord
        +sendAlert(String): void
        +cleanJobRecord(JobRecord): JobRecord
        +suggestQueryCorrection(String, int): String
    }

    class MainController {
        -model: IModel
        -view: IView
        -alertObservers: List~AlertObserver~
        +MainController()
        +registerAlertObserver(AlertObserver): void
        +unregisterAlertObserver(AlertObserver): void
        +setModel(IModel): void
        +setView(IView): void
        +getView(): IView
        +getModel(): IModel
        +getLocations(): List~String~
        +getIndustries(): List~String~
        -capitalizeItems(List~String~, Map~String, String~): List~String~
        -capitalizeWord(String): String
        +getApiCall(String, Integer, String, String): List~JobRecord~
        +getSavedJobs(): List~JobRecord~
        +setSavedJobs(List~JobRecord~): List~JobRecord~
        +isJobAlreadySaved(JobRecord): boolean
        +jobToSavedList(JobRecord): void
        +tryAddJobToSavedList(JobRecord): boolean
        +removeJobFromList(int): void
        +pathToCSV(String): void
        +exportToFileType(List~JobRecord~, String, String): void
        +getUpdateJob(int, String, int): JobRecord
        +onAlert(String): void
        +sendAlert(String): void
        +cleanJobRecord(JobRecord): JobRecord
        +suggestQueryCorrection(String, int): String
    }
    
    class AlertObserver {
        <<interface>>
        +onAlert(String): void
    }
}

namespace model {
    class IModel {
        <<interface>>
        +setAlertListener(AlertListener): void
        +getIndustries(): List~String~
        +getLocations(): List~String~
        +capitalizeItems(List~String~, Map~String, String~): List~String~
        +addJob(JobRecord): void
        +getJobRecord(String): JobRecord
        +getJobRecords(): List~JobRecord~
        +removeJob(int): boolean
        +updateJob(int, String, int): void
        +searchJobs(String, Integer, String, String): List~JobRecord~
        +suggestQueryCorrection(String, int): String
        +saveJobsToCsv(String): void
        +exportSavedJobs(List~JobRecord~, String, String): void
        +cleanJob(JobRecord): JobRecord
        +sendAlert(String): void
    }

    class Jobs {
        -static final INDUSTRY_MAP: Map~String, String~
        -static final LOCATION_MAP: Map~String, String~
        -alertListener: AlertListener
        -jobList: List~JobRecord~
        -runs: int
        -api: JobBoardApi
        -static final DEFAULT_SAVED_JOBS_PATH: String
        -isTestMode: boolean
        -isInitialSearch: boolean
        -suggestionService: QuerySuggestionService
        +Jobs()
        +setAlertListener(AlertListener): void
        -isRunningInTestEnvironment(): boolean
        #createJobBoardApi(): JobBoardApi
        +getIndustries(): List~String~
        +getLocations(): List~String~
        +addJob(JobRecord): void
        +getJobRecord(String): JobRecord
        +getJobRecords(): List~JobRecord~
        +removeJob(int): boolean
        +updateJob(int, String, int): void
        +searchJobs(String, Integer, String, String): List~JobRecord~
        +saveJobsToCsv(String): void
        +exportSavedJobs(List~JobRecord~, String, String): void
        +cleanJob(JobRecord): JobRecord
        +sendAlert(String): void
        -loadJobsFromCsv(String): void
        +cleanJobIndustries(): void
        +capitalizeItems(List~String~, Map~String, String~): List~String~
        -capitalizeWord(String): String
        +suggestQueryCorrection(String, int): String
    }

    class AlertListener {
        <<interface>>
        +onAlert(String): void
    }
    
    class QuerySuggestionService {
        -static final MAX_EDIT_DISTANCE: int
        -static final MIN_QUERY_LENGTH: int
        -recentQueries: List~String~
        -commonTerms: List~String~
        +addSuccessfulQuery(String): void
        +suggestCorrection(String, int): String
        -findBestMatch(String, Lis~String~)
        -calculateLevenshteinDistance(String, String): int
    }

    class JobRecord {
        <<record>>
        +id: int
        +url: String
        +jobSlug: String
        +jobTitle: String
        +companyName: String
        +companyLogo: String
        +jobIndustry: List~String~
        +jobType: List~String~
        +jobGeo: String
        +jobLevel: String
        +jobExcerpt: String
        +jobDescription: String
        +pubDate: String
        +annualSalaryMin: int
        +annualSalaryMax: int
        +salaryCurrency: String
        +rating: int
        +comments: String
    }
    
    class JobBean {
        -id: int
        -url: String
        -jobSlug: String
        -jobTitle: String
        -companyName: String
        -companyLogo: String
        -jobIndustry: List~String~
        -jobType: List~String~
        -jobGeo: String
        -jobLevel: String
        -jobExcerpt: String
        -jobDescription: String
        -pubDate: String
        -annualSalaryMin: int
        -annualSalaryMax: int
        -salaryCurrency: String
        -rating: int
        -comments: String
        +JobBean()
        +getId(): int
        +setId(int): void
        +getUrl(): String
        +setUrl(String): void
        +getJobSlug(): String
        +setJobSlug(String): void
        +getJobTitle(): String
        +setJobTitle(String): void
        +getCompanyName(): String
        +setCompanyName(String): void
        +getCompanyLogo(): String
        +setCompanyLogo(String): void
        +getJobIndustry(): List~String~
        +setJobIndustry(List~String~): void
        +getJobType(): List~String~
        +setJobType(List~String~): void
        +getJobGeo(): String
        +setJobGeo(String): void
        +getJobLevel(): String
        +setJobLevel(String): void
        +getJobExcerpt(): String
        +setJobExcerpt(String): void
        +getJobDescription(): String
        +setJobDescription(String): void
        +getPubDate(): String
        +setPubDate(String): void
        +getAnnualSalaryMin(): int
        +setAnnualSalaryMin(int): void
        +getAnnualSalaryMax(): int
        +setAnnualSalaryMax(int): void
        +getSalaryCurrency(): String
        +setSalaryCurrency(String): void
        +getRating(): int
        +setRating(int): void
        +getComments(): String
        +setComments(String): void
        +toRecord(): JobRecord
        +equals(Object): boolean
        +hashCode(): int
        +toString(): String
    }
    
    class ResponseRecord {
        <<record>>
        +apiVersion: String
        +documentationUrl: String
        +friendlyNotice: String
        +jobCount: int
        +xRayHash: String
        +clientKey: String
        +lastUpdate: String
        +jobs: List~JobRecord~
    }
}

namespace model.formatters {
    class DataFormatter {
        -DataFormatter()
        +static write(Collection~JobRecord~, Formats, OutputStream): void
        +static exportCustomCSV(List~JobRecord~, String): void
        -static prettyPrint(Collection~JobRecord~, OutputStream): void
        -static prettySingle(JobRecord, PrintStream): void
        -static writeXmlData(Collection~JobRecord~, OutputStream): void
        -static writeJsonData(Collection~JobRecord~, OutputStream): void
        -static writeCSVData(Collection~JobRecord~, OutputStream): void
        +static stripHTML(String): String
        +static extractFirstSentence(String): String
        +static escapeCSV(String): String
        +static replaceHtmlEntities(String): String
        +static processJobHtml(JobRecord): JobRecord
        +static read(InputStream, Formats): List~JobRecord~
    }
    
    class DomainXmlWrapper {
        -job: Collection~JobRecord~
        +DomainXmlWrapper(Collection~JobRecord~)
    }
    
    class Formats {
        <<enumeration>>
        JSON
        XML
        CSV
        PRETTY
        +static containsValues(String): Formats
    }
}

namespace model.net {
    class JobBoardApi {
        -static CLIENT: OkHttpClient
        -static OBJECT_MAPPER: ObjectMapper
        -static INDUSTRY_MAP: Map~String, String~
        -static LOCATION_MAP: Map~String, String~
        -errorMessage: String
        +JobBoardApi()
        +static loadCsvData(String, String, String): Map~String, String~
        +getJobBoard(String): JobBoardApiResult
        +getJobBoard(String, Integer): JobBoardApiResult
        +getJobBoard(String, Integer, String): JobBoardApiResult
        +getJobBoard(String, Integer, String, String): JobBoardApiResult
        #searchApi(String): List~JobRecord~
        +main(String[]): void
    }
    
    class JobBoardApiResult {
        -jobs: List~JobRecord~
        -errorMessage: String
        +JobBoardApiResult(List~JobRecord~, String)
        +getJobs(): List~JobRecord~
        +getErrorMessage(): String
        +hasError(): boolean
    }
}

namespace view {
    class IView {
        <<interface>>
        +setController(IController): void
        +run(): void
        +notifyUser(String): void
    }

    class MainView {
        -mainPane: JPanel
        -findJobTab: JobView
        -savedJobTab: JobView
        -tabbedPane: JTabbedPane
        -tabStyleManager: TabStyleManager
        -controller: IController
        -customMenuBar: CustomMenuBar
        -theme: ColorTheme
        +MainView()
        +setController(IController): void
        +onAlert(String): void
        -setupExitKeyAction(): void
        -buildTabbedPane(JobView, JobView): JTabbedPane
        -createCustomMenu(): void
        -mapMenuEvents(): void
        -applyTheme(ColorTheme): void
        +notifyUser(String): void
        +run(): void
        -exitHelper(): void
    }
    
    class JobView {
        <<abstract>>
        #searchButton: ThemedButton
        #searchField: JTextArea
        #recordText: JTextArea
        #jobsTable: JobsTable
        #theme: ColorTheme
        #jobsList: List~JobRecord~
        #openJob: ThemedButton
        #controller: IController
        #topButtonLayout: JPanel
        #mainPanel: JPanel
        #savedJobs: boolean
        +JobView()
        +initView(): void
        +makeTopButtonPanel(): JPanel
        +makeBottomButtonPanel(): JPanel
        +makeTablePanel(): JPanel
        +applyTheme(ColorTheme): void
        +setJobsList(List~JobRecord~): void
        +getJobsList(): List~JobRecord~
        +setRecordText(String): void
        +addJobRecord(JobRecord): void
        +removeJobRecord(int): void
        +updateJobsList(List~JobRecord~): void
        +addFeatures(IController): void
        #getTheme(): ColorTheme
        #createThemedButton(String): ThemedButton
        #createThemedButton(String, ThemedButton.ButtonType): ThemedButton
    }
    
    class FindJobTab {
        -controller: IController
        -locations: String[]
        -industries: String[]
        -searchResults: List~JobRecord~
        -industryLabel: JLabel
        -locationLabel: JLabel
        -resultsLabel: JLabel
        -titleLabel: JLabel
        -openIcon: ImageIcon
        -saveIcon: ImageIcon
        -warningIcon: ImageIcon
        -questionIcon: ImageIcon
        -salaryVisualizationPanel: SalaryVisualizationPanel
        -showVisualizationCheckbox: JCheckBox
        -tablePanel: JPanel
        -saveJob: ThemedButton
        -searchField: TextField
        -industryCombo: JComboBox
        -locationCombo: JComboBox
        -resultsCombo: JComboBox
        +FindJobTab(IController)
        -setupEnterKeyAction(): void
        -disableEnterKeyTraversalIn(Container): void
        -modifyTablePanel(): void
        +makeTopButtonPanel(): JPanel
        -performSearch(String, int, String, String): void
        -handleSearchResults(List~JobRecord~, String): void
        +makeBottomButtonPanel(): JPanel
        -updateVisualizationIfNeeded(List~JobRecord~): void
        -applyThemeToVisualization(ColorTheme): void
        +applyTheme(ColorTheme): void
        -openSelectedJob(): void
        -saveSelectedJob(): void
        +updateJobsList(List~JobRecord~): void
        +setJobsList(List~JobRecord~): void
    }
    
    class SavedJobsTab {
        -openButton: ThemedButton
        -saveButton: ThemedButton
        -exportButton: ThemedButton
        -editButton: ThemedButton
        -deleteButton: ThemedButton
        -openIcon: ImageIcon
        -saveIcon: ImageIcon
        -exportIcon: ImageIcon
        -warningIcon: ImageIcon
        -successIcon: ImageIcon
        -editIcon: ImageIcon
        -deleteIcon: ImageIcon
        +SavedJobsTab(IController, List~JobRecord~)
        +makeTopButtonPanel(): JPanel
        +makeBottomButtonPanel(): JPanel
        -handleSaveAction(): void
        -handleExportAction(JComboBox): void
        -showNoJobsMessage(JFrame, String): void
        -showSaveConfirmDialog(JFrame, int): boolean
        -showExportConfirmDialog(JFrame, String): boolean
        -createDataDirectoryAndGetFilePath(String): String
        -getExportFilePath(JFrame, String): String
        -saveJobsToFile(JFrame, List~JobRecord~, String, String): void
        -exportJobsToFile(JFrame, List~JobRecord~, String, String): void
        -openSelectedJob(): void
        -editSelectedJob(): void
        -deleteSelectedJob(): void
        +applyTheme(ColorTheme): void
    }
    
    class ColorTheme {
        -colors: Map~String, Color~
        +static LIGHT: ColorTheme
        +static DARK: ColorTheme
        -ColorTheme(Map~String, Color~)
        -static createLightTheme(): ColorTheme
        -static createDarkTheme(): ColorTheme
        +getColor(String): Color
        +getButtonNormal(): Color
        +getButtonHover(): Color
        +getSecondaryButtonNormal(): Color
        +getSecondaryButtonHover(): Color
        +getSuccessButtonNormal(): Color
        +getSuccessButtonHover(): Color
        +getDangerButtonNormal(): Color
        +getDangerButtonHover(): Color
        +getWarningButtonNormal(): Color
        +getWarningButtonHover(): Color
        +getInfoButtonNormal(): Color
        +getInfoButtonHover(): Color
        +getBackground(): Color
        +getForeground(): Color
        +getFieldBackground(): Color
        +getFieldForeground(): Color
        +getButtonForeground(): Color
        +getLabelForeground(): Color
        +getTitleTextColor(): Color
        +getTaglineTextColor(): Color
        +getMenuBarBackgroundLight(): Color
        +getMenuBarForegroundLight(): Color
        +getMenuBarBackgroundDark(): Color
        +getMenuBarForegroundDark(): Color
        +getWinSelectedBgLight(): Color
        +getWinSelectedFgLight(): Color
        +getWinUnselectedBgLight(): Color
        +getWinUnselectedFgLight(): Color
        +getWinSelectedBgDark(): Color
        +getWinSelectedFgDark(): Color
        +getWinUnselectedBgDark(): Color
        +getWinUnselectedFgDark(): Color
        +getMacSelectedBgLight(): Color
        +getMacSelectedFgLight(): Color
        +getMacUnselectedBgLight(): Color
        +getMacUnselectedFgLight(): Color
        +getMacSelectedBgDark(): Color
        +getMacSelectedFgDark(): Color
        +getMacUnselectedBgDark(): Color
        +getMacUnselectedFgDark(): Color
        +getWinTabPaneBgLight(): Color
        +getWinTabPaneBgDark(): Color
        +getMacTabPaneBgLight(): Color
        +getMacTabPaneBgDark(): Color
    }
    
    class CustomHeader {
        -defaultRenderer: TableCellRenderer
        -upArrowLight: ImageIcon
        -downArrowLight: ImageIcon
        -upArrowDark: ImageIcon
        -downArrowDark: ImageIcon
        -isDarkMode: boolean
        +CustomHeader(TableCellRenderer)
        +setDarkMode(boolean): void
        -createColoredArrow(ImageIcon, Color): ImageIcon
        +getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int): Component
    }
    
    class CustomMenuBar {
        -settingsButton: JButton
        -settingsMenu: JPopupMenu
        -exitItem: JMenuItem
        -lightModeItem: JMenuItem
        -darkModeItem: JMenuItem
        -lineColor: Color
        -lineThickness: int
        -titleLabel: JLabel
        -taglineLabel: JLabel
        -static SETTINGS_WIDTH: int
        +CustomMenuBar()
        -createSettingsButton(): JButton
        +paintComponent(Graphics): void
        -buildMenuStructure(): void
        +applyTheme(ColorTheme): void
        -isDarkColor(Color): boolean
        +setLineThickness(int): void
        +getExitItem(): JMenuItem
        +getLightModeItem(): JMenuItem
        +getDarkModeItem(): JMenuItem
    }
    
    class ThemedButton {
        -theme: ColorTheme
        -isHovering: boolean
        -buttonType: ButtonType
        +ThemedButton(String)
        +ThemedButton(String, ButtonType)
        -initializeButton(): void
        +applyTheme(ColorTheme): void
        +setButtonType(ButtonType): void
        +getButtonType(): ButtonType
        -updateButtonColors(): void
        +getTheme(): ColorTheme
    }
    
    class JobsTable {
        -tableModel: DefaultTableModel
        -sorter: TableRowSorter
        -columnNames: String[]
        -customHeaderRenderer: CustomHeader
        -currentTheme: ColorTheme
        -imageCellRenderer: ImageCellRenderer
        +JobsTable()
        +JobsTable(String[], Object[][])
        -setupColumnRenderers(): void
        +applyTheme(ColorTheme): void
        -setupHeaderRenderer(): void
        +setData(Object[][]): void
        -configureColumnSorters(): void
        -extractMinSalary(String): int
        +setColumnNames(String[]): void
        +addRow(Object[]): void
        +removeRow(int): void
        +updateCell(int, int, Object): void
        +getRowCount(): int
        +getColumnCount(): int
        +getColumnNames(): String[]
        +cleanup(): void
    }
    
    class BaseJobDetailsDialogue {
        <<abstract>>
        #static LOGO_WIDTH: int
        #static LOGO_HEIGHT: int
        #static DEFAULT_DIALOG_WIDTH: int
        #static DEFAULT_DIALOG_HEIGHT: int
        #static MIN_DIALOG_WIDTH: int
        #static CHAR_WIDTH_FACTOR: double
        #static MAX_WIDTH_SCREEN_PERCENT: double
        +static createBaseDialog(Component, JobRecord, String): JDialog
        +static createMainContentPanel(JobRecord): JPanel
        +static calculateAndSetDialogSize(JDialog, JobRecord): void
        +static addDetailRow(JPanel, String, String): void
    }
    
    class JobDetailsDialogue {
        -static INFO_ICON: ImageIcon
        -static WARNING_ICON: ImageIcon
        -static SUCCESS_ICON: ImageIcon
        +static showJobDetails(Component, JobRecord, List~JobRecord~, IController): void
        -static showFindJobDetails(Component, JobRecord, IController): void
        -static createContentPanel(JobRecord): JPanel
        -static createRatingPanel(JobRecord): JPanel
        -static createButtonPanel(JDialog, Component, JobRecord, IController): JPanel
        -static handleSaveAction(JDialog, Component, JobRecord, IController): void
    }
    
    class SavedJobDetailsDialogue {
        -static EDIT_ICON: ImageIcon
        -static DELETE_ICON: ImageIcon
        -static CLOSE_ICON: ImageIcon
        -static WARNING_ICON: ImageIcon
        -static BUTTON_WIDTH: int
        +static show(Component, JobRecord, IController): void
        -static validateInputs(Component, JobRecord, IController): boolean
        -static createButtonPanel(JDialog, JobRecord, IController, Component): JPanel
        -static createButton(String, ThemedButton.ButtonType, ImageIcon): ThemedButton
        -static calculateButtonHeight(ThemedButton...): int
        -static setButtonSizes(ThemedButton, ThemedButton, ThemedButton, int): void
        -static configureButtonActions(JDialog, ThemedButton, ThemedButton, ThemedButton, JobRecord, IController, Component): void
        -static configureDialogProperties(JDialog, Component): void
    }
    
    class JobActionHelper {
        -static EDIT_ICON: ImageIcon
        -static DELETE_ICON: ImageIcon
        -static WARNING_ICON: ImageIcon
        -static SUCCESS_ICON: ImageIcon
        -JobActionHelper()
        +static saveJob(JobRecord, String, int, IController, Component): void
        +static editJob(JobRecord, IController, Component): JobRecord
        +static deleteJob(JobRecord, IController, Component): boolean
        +static showNoSelectionMessage(String, Component): void
        +static updateSavedJobsTabView(Component, IController): void
        +static switchToSavedJobsTab(Component, IController): void
        -static findTabbedPane(Component): JTabbedPane
    }
    
    class SalaryVisualizationPanel {
        -jobs: List~JobRecord~
        -static PADDING: int
        -static POINT_SIZE: int
        -static POINT_COLOR: Color
        -static POINT_HOVER_COLOR: Color
        -static LINE_COLOR: Color
        -dataPoints: List~Point2D~
        -hitBoxes: List~Rectangle2D~
        -hoveredIndex: int
        -theme: ColorTheme
        +SalaryVisualizationPanel(List~JobRecord~)
        -sortJobsBySalary(): void
        +updateJobs(List~JobRecord~): void
        +applyTheme(ColorTheme): void
        #paintComponent(Graphics): void
        -drawNoDataMessage(Graphics): void
        -drawAxes(Graphics2D, int, int, int): void
        -formatSalaryLabel(int): String
        -drawDataPoints(Graphics2D, int, int, int): void
        -calculateAverageSalary(JobRecord): int
        -truncateString(String, int): String
    }
    
    class IconLoader {
        -IconLoader()
        +static loadIcon(String): ImageIcon
        +static loadIcon(String, int, int): ImageIcon
        +static createTextIcon(String, int): ImageIcon
        +static loadCompanyLogo(String, int, int, String): ImageIcon
    }
    
    class ImageCellRenderer {
        -static IMAGE_WIDTH: int
        -static IMAGE_HEIGHT: int
        -imageCache: Map~String, ImageIcon~
        -loadingStatus: Map~String, Boolean~
        +getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int): Component
        -loadImageAsync(String, JLabel, JTable): void
        +clearCache(): void
    }
    
    class TabStyleManager {
        -tabbedPane: JTabbedPane
        -tabLabels: JLabel[]
        -theme: ColorTheme
        -isMacOS: boolean
        +TabStyleManager(JTabbedPane, String[])
        -isMacOSPlatform(): boolean
        +applyTheme(ColorTheme): void
        -getTabColors(boolean): Color[]
        -updateTabStyles(): void
        +getSelectedTabIndex(): int
        +setSelectedTab(int): void
    }
    
    class JobsLoader {
        -static COLUMN_NAMES: String[]
        -JobsLoader()
        +static getColumnNames(): String[]
        +static getJobList(Collection~JobRecord~): List~JobRecord~
        +static getData(Collection~JobRecord~): Object[][]
        +static getLogoUrls(Collection~JobRecord~): String[]
    }
    
    class StarRatingPanel {
        -stars: JLabel[]
        -rating: int
        -listener: RatingChangeListener
        +StarRatingPanel(int)
        +setRating(int): void
        +getRating(): int
        +setRatingChangeListener(RatingChangeListener): void
        -updateStarsDisplay(int): void
        -getEmptyStar(): ImageIcon
        -getFilledStar(): ImageIcon
    }
}

```
