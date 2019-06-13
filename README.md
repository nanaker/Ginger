# Ginger

Ginger is a toolkit to detect and correct  some code smells in analysed Android applications.

# Table of contents
*   [Code smells detection](#code_smells_detection)
*   [How to use it?](#how_to_use_it)
*   [Troubleshootings?](#troubleshootings)


### <a name="code_smells_detection"></a>Code smells detection

Ginger supports currently 5 Android code smells.


* Member Ignoring Method (MIM),
* No Low Memory Resolver (NLMR),
* Leaking Inner Class (LIC),
* Heavy AsyncTask (HAS),
* Heavy Broadcast Receiver (HBR),


### <a name="hoz_to_use_it"></a>How to use it ?



You can choose between three modes: **detect** , **correct** and  **detect&correct** .
The **detect** mode will allows you to scan with [Spoon](https://github.com/INRIA/spoon) your  application, then detect contained code smells.
You can use after the **correct** mode to correct code smells detected or suggest some recommendation instructions to correct them. Or **detect&correct** mode that combine the two privious mode.

Note that Ginger use Neo4j To save the application analysed but you don't need to install Neo4J on your side since it's embedded into Ginger (however it can be useful if you want to visualize the database , use the Neo4J Desktop Application).
```

#### Detect mode usage



usage: Ginger [-h] detect  -f folder -c codeSmell 

positional arguments:
  -f folder                  Path of the code source folder /src
  -c codeSmell               Code smell to detect MIM,LIC,NLMR,HBR,HAS or ALL to detect all code smells 
```

```

#### Correct mode usage


usage: Ginger [-h] correct   -c codeSmell 

positional arguments:
  -c codeSmell               Code smell to detect MIM,LIC,NLMR,HBR,HAS or ALL to detect all code smells 

```
```
#### Detect&Correct mode usage


usage: Ginger [-h] detect&correct  -f folder  -c codeSmell 

positional arguments:
  -f folder                  Path of the code source folder /src
  -c codeSmell               Code smell to detect MIM,LIC,NLMR,HBR,HAS or ALL to detect all code smells 


```
#### Example of usage

First we launch the detection mode to detect potential code smells present in the android application  :

```
java -Xmx2G -XX:+UseConcMarkSweepGC -jar  Ginger.jar detect -f "/path/to/application/src" -c "MIM" 
```

Then you can launch the correction  using correct mode, for example :
```
java -Xmx2G -XX:+UseConcMarkSweepGC -jar  Ginger.jar correct -c "MIM" 
```

OR  you can directly  launch the detection and the correction at once  using detect&correct mode, for example :
```
java -Xmx2G -XX:+UseConcMarkSweepGC -jar  Ginger.jar detect&correct -f "/path/to/application/src" -c "ALL" 
```

### <a name="troubleshootings"></a>Troubleshootings

**ginger** is still in development.  
Found a bug? We'd love to know about it!  
Please report all issues on the github issue tracker.

