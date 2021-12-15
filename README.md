# README
## Project Details
Project Name: Letters Mod Database 

Project Description: This repository contains everything related to the server and 
database part of our final project. This includes the ORM, the objects used to represent
the data, and the classes that handle our spark requests. The database it connects to is
also hosted on Heroku, but there are local variants. The goal is to allow users to send mail
to our database that can then be received by the intended recipient.

Team Members and Contributions:
- Sheridan Feucht (sfeucht): Created the mockups for the classes that handle the spark routes
and helped get the code up on Heroku.
- Lucas Brito (lbrito2): Created the ORM and integrated the Send and GetMail classes
with the database.
- Olena Mursalova (omursalo): Created the Address and Parcel classes. Integrated the
InitAddr and ChangeAddr classes with the database. Got the code and database up on Heroku.

To build use:
`mvn package`

To run use:
`./run`

To start the server use:
`./run --gui [--port=<port>]`

To deploy to Heroku:
If given access, use `git push heroku master` to push local changes to the remote
Heroku repository.

To test:
You can look at the connected database as you use the commands in-game. The commands
also print out messages to the log, which describe what is being done. 
***
## Relevant Links
The other part of this project can be found at: [LettersMod](https://github.com/sfeucht/letters_mod)

This project is hosted on heroku at: [serene-bayou-00030](https://serene-bayou-00030.herokuapp.com/)