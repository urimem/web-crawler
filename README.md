**Web Crawler**

Object diagram

![alt text](http://s3.amazonaws.com/bezmoog.com/crawler_small.png)

Using concurrent URL processors and building in-memory page graph.

**Install & Run instructions**
- Download zip or clone the repository 
- The repository contains intelliJ project file and run configuration
- You can use intelliJ community edition
- You will need Java SDK installed (easily installed with the intelliJ wizard)
- The crawler project has Main class which makes it easy to run as a command line app.

Run Main.class with following parameters (all has defaults):
1. String - Root crawling URL
2. boolean - Limit domain to the root URL domain - other domains will not be added or processed. Default true.
3. long - milliseconds crawler timeout, for processing big websites. 0 = no timeout

**Notes**
- Linked pages are collected only by a href links
- The result page graph will be printed to System.out.




