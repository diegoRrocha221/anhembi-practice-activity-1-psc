# anhembi-practice-activity-1-psc
<p align="center">

  <a href="https://github.com/diegoRrocha221/anhembi-practice-activity-1-psc/blob/main/LICENSE"><img src="https://img.shields.io/github/license/diegoRrocha221/xarray-for-golang" alt="GitHub License"></a>
  <a href=""><img src="https://raw.githubusercontent.com/jmnote/z-icons/master/svg/java.svg" width="40px" height="40px" alt="GitHub License"></a>
   <a href=""><img src="https://raw.githubusercontent.com/jmnote/z-icons/master/svg/git.svg" width="40px" height="40px" alt="GitHub License"></a>
  
</p>
<p align="center">
A very simple implementation of a console system for registering events and users. divided into two access hierarchies, exhibitor and common user.

exhibitors can:
register events;<br>
cancel events;<br>
view events;<br>

users can:
create their accounts and use a simple login system;<br>
once logged in:<br>
can view available events;<br>
confirm attendance at the event;<br>
attend the event;<br>
cancel their commitment to attend the event;<br>
go to the event;<br>
log out;<br>

some bonuses integration with IBGE API, use of SQlite database, some of tests in JUnit, CI</p>



<h2 align="center">Requirements</h2>

```bash
Java:
openjdk 11.0.21 2023-10-17

Maven:
Apache Maven 3.6.3
```
<h2 align="center">Usage WSL OR UBUNTO</h2>

On Ubunto:

update your dependencies

```bash
sudo apt update
sudo apt upgrade
```

Install the openjdk 11:

```bash
sudo apt install openjdk-11-jdk
```
Install Maven:
```bash
sudo apt-get install maven
```
Is just it, for the WSL we need a few more steps.
after you have done the previous steps on your WSL, we will continue by installing eclipse
```bash
wget https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/2022-06/R/eclipse-java-2022-06-R-linux-gtk-x86_64.tar.gz -O eclipse.tar.gz
```
Once downloaded, extract them and go to directory
```bash
tar -xzvf eclipse.tar.gz && cd eclipse
```
On your windows you will now need to install VcXsrv which you can get here <a href="https://sourceforge.net/projects/vcxsrv/" target="_blank"/>
after installing it, when you return to WSL, you must indicate the port of your graphical interface
```bash
export DISPLAY=:0
```
now just call the eclipse executable

```bash
./eclipse
```
and clone this repository
```bash
git clone git@github.com:diegoRrocha221/anhembi-practice-activity-1-psc.git
```


<h2>Here's a suggestion for improvement if anyone's interested:</h2>

Decouple this code, I'd start with something simple like:
```bash
EventSystemAnhembi/
|-- src/
|   |-- main/
|       |-- java/
|           |-- model/
|               |-- User.java
|               |-- Event.java
|           |-- database/
|               |-- DatabaseManager.java
|           |-- managers/
|               |-- EventManager.java
|               |-- UserManager.java
|               |-- MenuManager.java
|           |-- EventSystemAnhembi.java
```

or do everything following some architecture pattern, as this in java means creating 1,000,000 auxiliary files LOL, I didn't do it but feel free to make a fork of it :)

Another thing I would change would be to implement a pagination system for the cities, because it's getting too messy. eventually even including more flows by country would be nice;

The last changes would be to not always return the user to home, and to apply T.D.D to the letter (I confess I tried at first but fell into the temptation of writing the tests afterwards).

that's it feel free to clone, break, use as a study. make a fork with some improvement :)



