**Web Crawler**

Simple web crawler.

Using concurrent URL processors and building in-memory page graph.

Run Main.class with following parameters:
1. String - Root crawling URL
2. boolean - Limit domain to the root URL domain - other domains will not be added or processed. Default true.
3. long - milliseconds crawler timeout, for processing big websites. 0 = no timeout

Notes
- Linked pages are collected only by a href links
- The result page graph will be printed to System.out.

Object diagram
![alt text](http://s3.amazonaws.com/bezmoog.com/crawler_small.png)





