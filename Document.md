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
argument parsing is being moved to this class. I'll have the driver decide which controller to use through the
command line arguments. A new command line argument -c for console or -g gui will be added. if none is provided
we will default to gui.
------------------------------------------------
+ DNInfoApp()
+ getParsedArgs(String[]):HashMap~String, String~
+ getHelp(): void
+ main(String[]): void
}
namespace view {
class IView {
<<Interface>>
This will serve as an Interface for our view.Defining this correctly early on will allow the team to easily interact and 
code simultaneously. We suspect that we will only need the argument `addFeatures` which will take the controller and map"
the appropriate functions.
+ addFeatures(IControler): void
}
class MainView {
Main view will serve as a base plate for all views. It will incorporate FindJobView and SaveJobsView as tabs. Additionally,
all pane logic will mapped to the task bar on the main page which includes but is not limited to download data, and light
and dark mode
---------------------------------------------------------------------------------------------------------------------------
- FindJobView findJob
- SavedJobsView savedJobs
+ getFindJobView() : FindJobView
+ getSavedJobsView() : FindJobView
}
class FindJobView {
This show will reach out to an api and find relevant Jobs given a clients query. From here, the user will be able to
view and save the like. Upon saving, the user must rate there job and provide a comment at every save.
}
class SavedJobsView {

["This will show all saved Jobs that were found in the FindJobView. On this page, users will be able to view current 
jobs they have saved, edit them, and delete them . When clients views a job, we will also conduct an API call that 
will grab related skill to the job. Download will be an pane"]
-

}
}
namespace Controller {
class IController {
<<Interface>>
I controller will have three main arguments setView, setModel, and run. This will only be inherited by MainController.
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
+ downloadJobs(str)
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
