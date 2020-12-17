# stocks-portfolio-update-engine
update own stocks booking excel file by parsing web/ using API

Calculation of PnL for each stocks, various statistics of the whole portfolio

Generate email with the PnL report

Config file setting
-------------------
##### resources/config.properties:  
set sender's email acc and pw, and the receiver's email
change the email smtp setting if needs to

Run Java Instruction
--------------------
##### -- only update PnL report
java -jar stock-parser-1.0-SNAPSHOT-jar-with-dependencies.jar /xyz/abc.xlsx  
##### -- send email after updating
java -jar stock-parser-1.0-SNAPSHOT-jar-with-dependencies.jar /xyz/abc.xlsx email

