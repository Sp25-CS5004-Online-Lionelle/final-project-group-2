# Skillz Hunter App Desing

The Diagram below outlines the general structure of the Skillz Hunter App.

```mermaid
---
config:
  theme: mc
title: Skillz Hunter App UML
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
