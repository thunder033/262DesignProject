################################################
# SWEN-262 Design Project README               #
# Project: Financial Portfolio Tracking System #
# Revised: 4/10/16 By Alexander Kidd           #
# Version: 2.0.5                               #
################################################

The Financial Portfolio Tracking System (FPTS) is a JavaFX application for 
managing a portfolio of holdings (equities and cash accounts).  Users can also 
import and export said holdings, simulate market environments with their holdings,
track a special list of holdings for price fluctuations, and make transactions.

Below is how to install and run FPTS:

Install Java 8.1+: http://java.com/en/download/
(This comes with JavaFX. For JavaFX development, read this: https://www.jetbrains.com/idea/help/preparing-for-javafx-application-development.html)

Have a folder in the same location as the FPTS.jar file on your machine named "data" without quotes.
This is where you can put the csv files (equities.csv, portfolio.csv, etc.)

If you would like to use the start.bat file as well for debugging purposes, put it in
this FPTS.jar directory as well and run it.

If you would like to use the administrator action of deleting an account, run the jar from the command
line as such, where portfolio-username is the portfolio-to-be-deleted's username 
(WARNING: This cannot be undone!):

java -jar FPTS.jar -delete portfolio-username

A sample portfolio with the following credentials has been set up: 

username: a
password: b

Buildlog Note: buildlog.txt was copied over from the output from IntelliJ's "Make" rebuild function.
