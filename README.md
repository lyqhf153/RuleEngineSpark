# RuleEngine

Logic behind this rule engine is to take the rules in json format 
and apply those rules over the data to search for the signals which 
are violating the rules.

## Design Consideration:-
Created the project in considering if we need to add more filters
or operators we can be able to do in less time and minimal changes.

**Listed below the Classes which will be required to be changed if enhancement need to be made**
- DataType.java  : ***Add new data types***
- Operator.java : ***Add new operators***
- FilterPredicate.java :  ***Include the new check for new/exisitng Data type***
- Util.java : ***create new static method for new Data type. Listed below method as reference***
	- *checkInteger()*
	- *checkDateTime()*
	- *checkString()*

Assumption:-
- There would be multiple of rules can be present for same signal.
if any of the rule is satisfied so that signal will be filtered 
and shown in output.
- Output will so distinct result.
- Operatore support as per data types
    - STRING :  Equal
    - INTEGER : Equal , Greater Than , Less Than
    - DATETIME : Equal , Greater Than , Lesser Than


## Build the project 
<code>  mvn clean install </code>
Fat jar **RuleEngine-jar-with-dependencies.jar**  will be created in target folder 

## Generate Rule file
Here Json should be the list of rules where each signal can have multiple rules.
Say for ATL1 we have rules as below
***For ATL1*** 
- ATL1 value should not be greater than 240.0
- ATL1 datetime should be equal to "2017-04-10 10:16:11"
***For ATL2***
- ATL2 value is LOW
***For ATL10*** 
- ATL10 value equal to 46.691

so the Json will be
```javascript
[
  {
    "signal": "ATL1",
    "rule": [
			{
				"type": "Integer",
				"operator": ">",
				"value": "240.0"
			},
			{
				"type": "Datetime",
				"operator": ">",
				"value": "2017-04-10 10:16:11"
			}
	  ]
  },
  {
    "signal": "ATL2",
    "rule": [
			{
				"type": "String",
				"operator": "=",
				"value": "LOW"
			}
		]
  },
  {
    "signal": "ATL10",
    "rule": [
			{
				"type": "Integer",
				"operator": "=",
				"value": "46.691"
			}
   ]
 }
]
```
**Sample rule.json is present in main/resources folder for reference**

## Run the code
Go to target folder , run below command
Program takes two argument
- Signal Input Json File
- Rule Json File (**Create the rule.json as per "Generate Rule file" section"**

Syntax
<code>  java -jar RuleEngine-jar-with-dependencies.jar (**Signal Input Json File**)  (**Rule Json File**) </code>
	
Example
<code> java -jar RuleEngine-jar-with-dependencies.jar /tmp/raw_data.json  /tmp/rule.json </code>

**Output will printed in console**


## Improvement can be done
-  more data type can be included
-  more operators can be added for existing data types
