# instagramPhotosDownloader<br>
A program using instagram4j for downloadin instagram photos and videos. The app uses its own SQLite databse for storing necessary informations. Database diagram is in file Database/diagram.png. You can open the databse in SQLite studio or use an interactive mode of the app to reset database, reload names from file, export names atc.<br>
Changing saves folder currently is not supported.

# How to run
Set mandatory information in 'config.txt'. Add instagram profile names into folder names.txt (or your set file) separated by newlines. Or if you want to have more profiles of the same person, you can add them by this format:<br>
\<PersonName\> \<profileName\>, \<profileName\> , ..... \<new line\> <br>
Using terminal open folder release.<br>
Type 'java -jar app.jar' to show all possible start modes (for example 'java -jar app.jar -i' will start application in interactive mode)
