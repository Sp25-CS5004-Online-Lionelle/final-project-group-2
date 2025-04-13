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
title: Skillz Hunter App Initial Design
---
classDiagram

%% Main
main *..  IModel: uses
main *..  IView: uses
main *..  MainController: uses

%% IController
IController *.. IModel: uses
IController *.. JobRecord: uses
IController *.. IView: uses
IController *.. SavedJobsTab: uses%% is this needed?

%% Main Controller
IController <|-- MainController: Implements
MainController *.. IModel: uses
MainController *.. JobRecord: uses
MainController *.. Jobs: uses
MainController *.. DataFormatter: uses
MainController *.. IView: uses
MainController *.. MainView: uses
MainController *.. SavedJobsTab: uses

%% fomatters







class Main{
<<driver>>
+ SkillsHunterApp():void
+ main(): void

}



namespace view {
  class ThemedButton {
        - ColorTheme theme
        - boolean isHovering
        - ButtonType buttonType
        + ThemedButton(String)
        + ThemedButton(String, ButtonType)
        - void initializeButton()
        + void applyTheme(ColorTheme)
        + void setButtonType(ButtonType)
        + ButtonType getButtonType()
        - void updateButtonColors()
        + ColorTheme getTheme()
    }


  class TabStyleManager {
        - final JTabbedPane tabbedPane
        - final JLabel[] tabLabels
        - ColorTheme theme
        - final boolean isMacOS
        + TabStyleManager(JTabbedPane, String[])
        - boolean isMacOSPlatform()
        + void applyTheme(ColorTheme)
        - Color[] getTabColors(boolean)
        - void updateTabStyles()
        + int getSelectedTabIndex()
        + void setSelectedTab(int)
    }

  class StarRatingPanel {
          - final JLabel[] stars
          - int rating
          - RatingChangeListener listener
          + StarRatingPanel(int)
          + void setRating(int)
          + int getRating()
          + void setRatingChangeListener(RatingChangeListener)
          - void updateStarsDisplay(int)
          - ImageIcon getEmptyStar()
          - ImageIcon getFilledStar()
      }

    class SavedJobsTab {
        - openButton: ThemedButton
        - saveButton: ThemedButton
        - exportButton: ThemedButton
        - editButton: ThemedButton
        - deleteButton: ThemedButton
        - final openIcon: ImageIcon
        - final saveIcon: ImageIcon
        - final exportIcon: ImageIcon
        - final warningIcon: ImageIcon
        - final successIcon: ImageIcon
        - final editIcon: ImageIcon
        - final deleteIcon: ImageIcon
        + SavedJobsTab(IController, List~JobRecord~)
        - handleSaveAction(): void
        - handleExportAction(JComboBox~String~): void
        - showNoJobsMessage(JFrame, String): void
        - showSaveConfirmDialog(JFrame, int): boolean
        - showExportConfirmDialog(JFrame, String): boolean
        - createDataDirectoryAndGetFilePath(String): String
        - getExportFilePath(JFrame, String): String
        - saveJobsToFile(JFrame, List~JobRecord~, String, String): void
        - exportJobsToFile(JFrame, List~JobRecord~, String, String): void
        - cleanJobRecordsForExport(List~JobRecord~): List~JobRecord~
        - stripHTML(String): String
        - extractFirstSentence(String): String
        - openSelectedJob(): void
        - editSelectedJob(): void
        - deleteSelectedJob(): void
    }




    class SavedJobDetailsDialogue {
        - static final EDIT_ICON: ImageIcon
        - static final DELETE_ICON: ImageIcon
        - static final CLOSE_ICON: ImageIcon
        - static final WARNING_ICON: ImageIcon
        - static final BUTTON_WIDTH: int
        + static show(Component, JobRecord, IController): void
        - static validateInputs(Component, JobRecord, IController): boolean
        - static createButtonPanel(JDialog, JobRecord, IController, Component): JPanel
        - static createButton(String, ThemedButton.ButtonType, ImageIcon): ThemedButton
        - static calculateButtonHeight(ThemedButton...): int
        - static setButtonSizes(ThemedButton, ThemedButton, ThemedButton, int): void
        - static configureButtonActions(JDialog, ThemedButton, ThemedButton, ThemedButton, JobRecord, IController, Component): void
        - static configureDialogProperties(JDialog, Component): void
    }


   class SalaryVisualizationPanel {
        - jobs: List~JobRecord~
        - static final PADDING: int
        - static final POINT_SIZE: int
        - static final POINT_COLOR: Color
        - static final POINT_HOVER_COLOR: Color
        - static final LINE_COLOR: Color
        - dataPoints: List~Point2D~
        - hitBoxes: List~Rectangle2D~
        - hoveredIndex: int
        - theme: ColorTheme
        + SalaryVisualizationPanel(jobs: List~JobRecord~)
        - sortJobsBySalary(): void
        + updateJobs(List~JobRecord~): void
        + applyTheme(ColorTheme): void
        # paintComponent(Graphics): void
        - drawNoDataMessage(Graphics): void
        - drawAxes(Graphics2D, int, int, int): void
        - formatSalaryLabel(int): String
        - drawDataPoints(Graphics2D, int, int, int): void
        - calculateAverageSalary(JobRecord): int
        - truncateString(String, int): String
    }




class MainView {
        <<final>>
        - mainPane: JPanel
        - findJobTab: JobView
        - savedJobTab: JobView
        - tabbedPane: JTabbedPane
        - tabStyleManager: TabStyleManager
        - controller: IController
        - customMenuBar: CustomMenuBar
        - theme: ColorTheme
        + MainView(IController)
        + setupExitKeyAction()
        + buildTabbedPane(JobView, JobView): JTabbedPane
        + createCustomMenu()
        + mapMenuEvents()
        + applyTheme(ColorTheme)
        + notifyUser(String)
        + addFeatures(IController)
        + run()
    }

class JobView {
        <<abstract>>
        +??? shouldn't theses be private and then inherited? 
        # searchButton: ThemedButton
        # searchField: JTextArea
        # recordText: JTextArea
        # jobsTable: JobsTable
        # theme: ColorTheme
        # jobsList: List~JobRecord~
        # openJob: ThemedButton
        # controller: IController
        # topButtonLayout: JPanel
        # mainPanel: JPanel
        # savedJobs: boolean


        + JobView()
        + initView()
        + makeTopButtonPanel(): JPanel
        + makeBottomButtonPanel(): JPanel
        + makeTablePanel(): JPanel
        + applyTheme(ColorTheme)
        + setJobsList(List~JobRecord~)
        + getJobsList(): List~JobRecord~
        + setRecordText(String)
        + addJobRecord(JobRecord)
        + removeJobRecord(int)
        + updateJobsList(List~JobRecord~)
        + addFeatures(IController)
        + getTheme(): ColorTheme
        + createThemedButton(String): ThemedButton
        + createThemedButton(String,ThemedButton.ButtonType): ThemedButton
    }
  
  class JobsTable {
        <<final>>
        - tableModel: DefaultTableModel
        - sorter: TableRowSorter~DefaultTableModel~
        - columnNames: String[]
        - customHeaderRenderer: CustomHeader
        - currentTheme: ColorTheme
        - imageCellRenderer: ImageCellRenderer
        + JobsTable()
        + JobsTable(String[], Object[][])
        + applyTheme(ColorTheme)
        + setData(Object[][])
        + setColumnNames(String[])
        + addRow(Object[])
        + removeRow(int)
        + updateCell(int,int,Object)
        + getRowCount(): int
        + getColumnCount(): int
        + getColumnNames(): String[]
        + cleanup()
        - setupColumnRenderers()
        - setupHeaderRenderer()
        - configureColumnSorters()
        - extractMinSalary(String): int
    }


  class JobsLoader {
      <<final>>
      - static final String[] COLUMN_NAMES: String[]
      + static getColumnNames(): String[]
      + static getJobList( Collection~JobRecord~): List~JobRecord~
      + static getData(Collection~JobRecord~): Object[][]
      + static getLogoUrls(Collection~JobRecord~): String[]
  }


class JobDetailsDialogue {
        <<final>>
        - static final ImageIcon INFO_ICON: ImageIcon
        - static final ImageIcon WARNING_ICON: ImageIcon
        - static final ImageIcon SUCCESS_ICON: ImageIcon
        + static showJobDetails(Component, JobRecord, List~JobRecord~, IController)
        + private static showFindJobDetails( Component, JobRecord, IController)
        + private static createContentPanel( JobRecord): JPanel
        + private static createRatingPanel(JobRecord): JPanel
        + private static createButtonPanel( JDialog, Component,  JobRecord, IController): JPanel
        + private static handleSaveAction( JDialog, Component, JobRecord, IController)

    }



class JobActionHelper {
    <<final>>
    - static final EDIT_ICON: ImageIcon
    - static final DELETE_ICON: ImageIcon
    - static final WARNING_ICON: ImageIcon
    - static final SUCCESS_ICON: ImageIcon
    
    + static saveJob(JobRecord,String, int, IController,Component): void
    + static editJob(JobRecord,IController, Component): JobRecord
    + static deleteJob(JobRecord, IController, Component): boolean
    + static showNoSelectionMessage(String, Component): void
    + static updateSavedJobsTabView(Component, IController): void
    + static switchToSavedJobsTab(Component, IController): void
    - static findTabbedPane(Component): JTabbedPane
}



class IView {
      <<interface>>
      + run(): void
      + notifyUser(message: String): void
      + addFeatures(controller: IController): void
  }

class final ImageCellRenderer {
    <<abstract>>
    - static final IMAGE_WIDTH: int
    - static final IMAGE_HEIGHT: int
    - imageCache: Map~String, ImageIcon~
    - loadingStatus: Map~String, Boolean~
    
    + getTableCellRendererComponent(JTable, Object, boolean,boolean,int,int): Component
    + loadImageAsync(String, JLabel, JTable): void
    + clearCache(): void
}


class final IconLoader {
    <<final>>
    + static final loadIcon(String): ImageIcon
    + static final loadIcon(String, int, int): ImageIcon
    + static final createTextIcon(String, int): ImageIcon
    + static final loadCompanyLogo(String, int, int, String): ImageIcon
}


class FindJobTab {
        - final controller: IController
        - final locations: String[]
        - final industries: String[]
        - searchResults: List~JobRecord~
        - industryLabel: JLabel
        - locationLabel: JLabel
        - resultsLabel: JLabel
        - titleLabel: JLabel
        - final openIcon: ImageIcon
        - final saveIcon: ImageIcon
        - final infoIcon: ImageIcon
        - final warningIcon: ImageIcon
        - salaryVisualizationPanel: SalaryVisualizationPanel
        - showVisualizationCheckbox: JCheckBox
        - tablePanel: JPanel
        - saveJob: ThemedButton

        + FindJobTab(controller)
        - setupEnterKeyAction(): void
        - disableEnterKeyTraversalIn(container): void
        - modifyTablePanel(): void
        - updateVisualizationIfNeeded(List~JobRecord~): void
        - applyThemeToVisualization(ColorTheme): void
        - openSelectedJob(): void
        - saveSelectedJob(): void
        - switchToSavedJobsTab(): void
        - findTabbedPane(Component): JTabbedPane
    }





class CustomMenuBar {
    - final settingsButton: JButton
    - final settingsMenu: JPopupMenu
    - exitItem: JMenuItem
    - lightModeItem: JMenuItem
    - darkModeItem: JMenuItem
    - lineColor: Color
    - lineThickness: int

    + CustomMenuBar()
    - createSettingsButton(): JButton
    + paintComponent(Graphics): void
    - buildMenuStructure(): void
    + applyTheme(ColorTheme): void
    + setLineThickness(thickness: int): void
    + getExitItem(): JMenuItem
    + getLightModeItem(): JMenuItem
    + getDarkModeItem(): JMenuItem
}

class CustomHeader {
    - final defaultRenderer: TableCellRenderer
    - upArrowLight: ImageIcon
    - downArrowLight: ImageIcon
    - upArrowDark: ImageIcon
    - downArrowDark: ImageIcon
    - isDarkMode: boolean

    + CustomHeader(TableCellRenderer)
    + setDarkMode( boolean): void
    - createColoredArrow(ImageIcon, Color): ImageIcon
    + getTableCellRendererComponent(JTable,Object,boolean,boolean,int, int): Component
}

  class BaseJobDetailsDialogue {
  <<abstract>>
      - static final LOGO_WIDTH: int
      - static final LOGO_HEIGHT: int

      + static createBaseDialog(Component, JobRecord, String): JDialog
      + static createMainContentPanel(JobRecord): JPanel
      + static addDetailRow(JPanel, String, String): void
  }

  class ColorTheme {
        <<final>>
        - ??? VVV should these variables be private VVV
        - buttonNormal: Color
        - buttonHover: Color
        - secondaryButtonNormal: Color
        - secondaryButtonHover: Color
        - successButtonNormal: Color
        - successButtonHover: Color
        - dangerButtonNormal: Color
        - dangerButtonHover: Color
        - warningButtonNormal: Color
        - warningButtonHover: Color
        - infoButtonNormal: Color
        - infoButtonHover: Color

        - background: Color
        - foreground: Color
        - fieldBackground: Color
        - fieldForeground: Color
        - buttonForeground: Color
        - labelForeground: Color

        - menuBarBackgroundLight: Color
        - menuBarForegroundLight: Color
        - menuBarBackgroundDark: Color
        - menuBarForegroundDark: Color

        - winSelectedBgLight: Color
        - winSelectedFgLight: Color
        - winUnselectedBgLight: Color
        - winUnselectedFgLight: Color

        - winSelectedBgDark: Color
        - winSelectedFgDark: Color
        - winUnselectedBgDark: Color
        - winUnselectedFgDark: Color

        - macSelectedBgLight: Color
        - macSelectedFgLight: Color
        - macUnselectedBgLight: Color
        - macUnselectedFgLight: Color

        - macSelectedBgDark: Color
        - macSelectedFgDark: Color
        - macUnselectedBgDark: Color
        - macUnselectedFgDark: Color

        - winTabPaneBgLight: Color
        - winTabPaneBgDark: Color
        - macTabPaneBgLight: Color
        - macTabPaneBgDark: Color

        + ColorTheme(color..., color) ?? this is way too many variables for a function we should re-configure this
        + static final LIGHT: ColorTheme
        + static final DARK: ColorTheme
    }
}







namespace Controller {

class IController {
<<Interface>>
+ getView(): IView
+ getModel(): IModel
+ getLocations(): List~String~
+ getIndustries(): List~String~
+ getApiCall(String, Integer, String, String ): List<JobRecord>
+ getSavedJobs(): List<JobRecord>
+ job2SavedList(): JobRecord
+ sendAlert(): String
+ isJobAlreadySaved(): JobRecord
+ removeJobFromList(): int
+ path2CSV(): String
+ export2FileType(List~JobRecord~, String, String): void
+ getSavedJobsTab(): SavedJobsTab
}

class MainController {
- mode: IModel
- view: IView
# setModel: IModel

- capitalizeItems(List<String>, Map<String, String>): List~String~
- capitalizeWord(String): String
+ ()
+ ()
+ () VVVV These should be in main controller VVV
+ sendAlert(String): void
+ getUpdateJob(int, String, int): JobRecord
+ tryAddJobToSavedList(JobRecord): Boolean
+ setSavedJobs(List~JobRecord~): List~JobRecord~
# setModel(IModel): void ?? Why are these protected this would be used externally?
# setView(IModel): void ?? Why are these protected this would be used externally?

}

}

namespace model {
    class final DataFormatter {
        + write(Collection~JobRecord~, Formats, OutputStream): void
        + exportCustomCSV(List~JobRecord~, String): void
        - prettyPrint(Collection~JobRecord~, OutputStream): void
        - prettySingle(JobRecord, PrintStream): void
        - writeXmlData(Collection~JobRecord~, OutputStream): void
        - writeJsonData(Collection~JobRecord~, OutputStream): void
        - writeCSVData(Collection~JobRecord~, OutputStream): void
        - stripHTML(String): String
        - extractFirstSentence(String): String
        - escapeCSV(String): String
        - DataFormatter(): void
    }


  class final DomainXmlWrapper {
  - private final Collection<JobRecord> job

  + DomainXmlWrapper(Collection<JobRecord>)
  }

    
    
  class Formats {
    <<Enumeration>>
    + JSON
    + XML
    + CSV
    + PRETTY
    + static containsValues(String): Formats    
    }

    class JobBoardApi {
        - static final OkHttpClient CLIENT
        - static final ObjectMapper OBJECT_MAPPER
        - static final Map~String, String~ INDUSTRY_MAP
        - static final Map~String, String~ LOCATION_MAP
        - String errorMessage

        + JobBoardApi()
        + static loadCsvData(String, String, String): Map~String, String~
        + getJobBoard(String): JobBoardApiResult
        + getJobBoard(String, Integer): JobBoardApiResult
        + getJobBoard(String, Integer, String): JobBoardApiResult
        + getJobBoard(String, Integer, String, String): JobBoardApiResult
        + static replaceHtmlEntities(String): String
        # searchApi(String): List~JobRecord~ ?? this i should be private I think
        - processJobRecords(List~JobRecord~): List~JobRecord~ ??? is see this was added back in. According to the styler this is not used why are we keeping it?
        + main(String[]): void
    
    }



      class JobBoardApiResult {
        - static final OkHttpClient CLIENT
        - static final ObjectMapper OBJECT_MAPPER
        - static final Map<String, String> INDUSTRY_MAP
        - static final Map<String, String> LOCATION_MAP
        - String errorMessage

        + JobBoardApi()
        + getJobBoard(String): JobBoardApiResult
        + getJobBoard(String, Integer): JobBoardApiResult
        + getJobBoard(String, Integer, String): JobBoardApiResult
        + getJobBoard(String, Integer, String, String): JobBoardApiResult
        + static loadCsvData(String, String, String): Map<String, String>
        + static replaceHtmlEntities(String): String
        # searchApi(String): List<JobRecord>
        - processJobRecords(List<JobRecord>): List<JobRecord>

        + main(String[]): void
      }

      class IModel {
      + setController(IController): void
      + addJob(JobRecord): void
      + getJobRecord(String): JobRecord
      + removeJob(int): Boolean
      + updateJob(int, String, int): void
      + searchJobs(String, Integer, String, String): List<JobRecord>
      + getLocations(): List<String>
      + getIndustries(): List<String>
      + saveJobsToCsv(String): void
      + exportSavedJobs(List<JobRecord>, String, String): void
      + sendAlert(String): void
      }

      class Jobs {
      - static final Map~String, String~ INDUSTRY_MAP
      - static final Map~String, String~ LOCATION_MAP 
      - IController controller
      - final List~JobRecord~ jobList
      - int runs
      - final JobBoardApi api

      + jobs()
      + setController(IController): void ??? I think we just need to add the Override Annotation.
      +


      }


      class JobBean {
        +int id
        +String url
        +String jobSlug
        +String jobTitle
        +String companyName
        +String companyLogo
        +List~String~ jobIndustry
        +List<String~ jobType
        +String jobGeo
        +String jobLevel
        +String jobExcerpt
        +String jobDescription
        +String pubDate
        +int annualSalaryMin
        +int annualSalaryMax
        +String salaryCurrency
        +int rating
        +String comments

        +JobBean(): void
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
        +getJobIndustry(): List<String>
        +setJobIndustry(List<String>): void
        +getJobType(): List<String>
        +setJobType(List<String>): void
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
      <<Record>>
        + apiVersion String
        + documentationUrl String
        + friendlyNotice String
        + jobCount int
        + xRayHash String
        + clientKey String
        + lastUpdate String
        + jobs List<JobRecord>
      }

      class JobRecord {
      <<Record>>
      + id int  
      + url String  
      + jobSlug String  
      + jobTitle String  
      + companyName String  
      + companyLogo String  
      + jobIndustry List<String>  
      + jobType List<String>  
      + jobGeo String  
      + jobLevel String  
      + jobExcerpt String  
      + jobDescription String  
      + pubDate String  
      + annualSalaryMin int  
      + annualSalaryMax int  
      + salaryCurrency String  
      + rating int  
      + comments String
      }

}


class artifacts {
+ /data/industries.csv Path
+ /data/locations.csv Path
+ /data/SavedJobs.csv Path
}


```
