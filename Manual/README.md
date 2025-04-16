# Skillz Hunter Manual
The Skillz Hunter app is an application designed to allow users to easily find and save employment opportunities in an easy-to-use graphical user interface. This document is the user manual associated with the application with the aim of guiding users by explaining how to use the application. The components of this application can be split between two over-arching goals: Finding and Saving a Job.

## Find a Job:
The user can run the application by running the file `src\main\java\skillzhunter\SkillsHunterApp.java`. After starting the application, the user will arrive at the "Find Jobs" tab. This tab's aim is to allow the user to easily search for promising employment opportunities. The image below shows these tabs and related popouts, and below the image, we quickly describe each labeled component.





![image](/data/images/Slide1.JPG)







### Find Job Components:
1. __Settings__: When the user clicks this button, they will be presented with two options: View Mode and Exit:
  * View Mode: allows the user to select dark or light mode.
  * Exit: allows the user to exit the application.
2. __Search Button Group__: This button group outlines all search functionality and filters available to the user. Using these buttons, users can filter their query by industry, location, and by number of returned results. Users also have the option of displaying a salary graph which outlines all salaries.
3. __Job Details__: After selecting a row from the table, the user can then use this button group to open and investigate individual jobs. By selecting the open button, the "Job Details" popout will appear. From here, the user can comment on the job, rate it and then finally save it for later use. All saved jobs will appear next in the saved jobs tab.
 

## Saved Jobs:
After discovering and saving job opportunities, users can move to the Saved Jobs tab. This tab contains all saved jobs. Jobs on this tab will persist across user sessions. The image below outlines the components of this tab, and the following paragraph describes each component.

![image](/data/images/Slide2.JPG)

### Saved Job Components:
1. __Saved Jobs__: This table shows all jobs saved. This table will only persist across sessions if the user selects the save button.
2. __Saved Jobs Controls__: This button group allows the user to perform basic operations on the saved job table. These operations include the following:*
   * Open: After selecting a row, this button allows the user to investigate the job in a user-friendly format.
   * Edit: After selecting a row, this button allows the user to edit the saved data of the job.
   * Delete: This button deletes the selected job from the table.
   * Save: This button saves the existing table which will persist across user sessions.
   * Export: This button allows the user to export the job list to the format of their choosing.