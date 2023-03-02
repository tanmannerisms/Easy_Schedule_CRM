Made by Tanner Mills for C195
tmil760@wgu.edu
02/27/2023
Version 1.0


IDE: IntelliJ IDEA 2022.3
JDK: Amazon Corretto 18.0.2
MySQL Driver: mysql-connector-java 8.0.32

This is an application called EasySchedule.
It is meant to be used for quick and easy scheduling of customers with contacts,
along with viewing some important reports.

The custom report I created allows the user to view the customers by their different
countries and division. This can be useful for scheduling in-person meetings.



How to run successfully:
1. Open the project.
    - Click File>Open
    - Navigate to the location of the extracted zip file
    - Double-click on WGU_195\pom.xml
    - Click Open as Project
    - Click Trust Project
2. Set the Project SDK to corretto-18 (Amazon Corretto version 18.0.2)
    - Go to File>Project Structure
    - Go to Project tab
    - Click the Project SDK dropdown
    - Click Add SDK>Download SDK
    - Change the version to 18 and vendor to Amazon Corretto
    - Download
3. Run or Invalidate Caches and run.
    - At this point, the project should run without issue. If it does not run properly, proceed with the following steps:
        - Click File>Invalidate Caches
        - Check the Clear file system cache and Local History and Clear downloaded shared indexes boxes.
        - Wait for IntelliJ to index everything and then click the run button.