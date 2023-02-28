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
1. Go to File>Project Structure
2. Set the Project SDK to corretto-18 (Amazon Corretto version 18.0.2)
    -Go to Project tab
    -Click the Project SDK dropdown
    -Click Add SDK>Download SDK
    -Change the version to 18 and vendor to Amazon Corretto
    -Download
3. Add MySQL connector through Maven
    -For some reason, the external library doesn't get automatically downloaded, so it must be done manually.
    -Navigate to Libraries in the Project Structure window.
    -Click the plus button
    -Click From Maven...
    -Type in com.mysql:mysql-connector-j:8.0.32
    -Check the download to box and leave the directory as-is
    -Click Ok
4. Invalidate Caches and run.
    -Click file>Invalidate Caches
    -Check the Clear file system cache and Local History and Clear downloaded shared indexes boxes.
    -Wait for IntelliJ to restart and it should be good to go.