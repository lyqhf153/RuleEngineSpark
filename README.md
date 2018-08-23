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

## Generate Rule file (rule.json)
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


## Kafka Setup done:-
- Downlaod latest kafka say **kafka_2.12-2.0.0.tgz** from **https://kafka.apache.org/downloads**
- Extract it to the folder kafka
  <code> cd kafka </code>
- Start Zookeper
 <code> bin/zookeeper-server-start.sh config/zookeeper.properties </code>
- Start Kafka Server
 <code>  bin/kafka-server-start.sh  config/server.properties  </code>
- Create Kafka Topic
```javascript
bin/kafka-topics.sh --create \
--zookeeper localhost:2181 \
--replication-factor 1 --partitions 2 \
--topic signal-topic
```
-List Kafka Topic :-
```javascript
bin/kafka-topics.sh --list \
   --zookeeper localhost:2181
```
-Cosumer from beginning:-
```javascript
bin/kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic signal-topic \
--from-beginning
```
```javascript
-Put Data To Topic :-
```javascript
bin/kafka-console-producer.sh \
--broker-list localhost:9092 \
--topic signal-topic
```

Below is the sample ingested to the kafka topic
```javascript
{"signal": "ATL1", "value_type": "Integer", "value": "250.12"}
{"signal": "ATL1", "value_type": "Datetime", "value": "2017-04-10 10:16:12"}
{"signal": "ATL1", "value_type": "Integer", "value": "10.90"}
{"signal": "ATL2", "value_type": "String", "value": "LOW"}
{"signal": "ATL2", "value_type": "String", "value": "HIGH"}
{"signal": "ATL2", "value_type": "Datetime", "value": "2017-04-10 10:16:55"}
{"signal": "ATL3", "value_type": "Integer", "value": "46.691"}
{"signal": "ATL3", "value_type": "Datetime", "value": "2017-04-21 07:55:28"}
{"signal": "ATL4", "value_type": "String", "value": "LOW"}
{"signal": "ATL4", "value_type": "String", "value": "LOW"}
{"signal": "ATL4", "value_type": "Integer", "value": "80.10"}
```


## Run the code
Go to target folder , run below command
Program takes two argument
- Signal Input Json File
- Rule Json File (**Create the rule.json as per "Generate Rule file" section"**

Syntax
<code>  spark-submit  --files (**rule.json Location**) --class (**Job Class Name**)  (**Jar Location**) </code>
	
Example
<code> spark-submit --files file:////tmp/rule.json --class com.engine.rule.RuleEngineSpark /tmp/RuleEngineSpark.jar
 </code>

**Output will printed in console**
- For the above input , Output will be
  - ATL1 , ATL2 , ATL3


## Improvement can be done
-  more data type can be included.
-  more operators can be added for existing data types.



