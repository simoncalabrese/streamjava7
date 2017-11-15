# streamjava7
Api for simulating StreamApi in java8 in Java7 converting project in functional programming progect

How to include library in your project:

in pom add:

'<repositories>'
        '<repository>'
            '<id>simoncalabrese</id>'
                '<name>streamjava7</name>'
                '<url>https://github.com/simoncalabrese/streamjava7/raw/master</url>'
        '</repository>'
 </repositories>
 
 and under <dependecies>
 
  <dependency>
       <groupId>com.streamjava7</groupId>
       <artifactId>streamApi</artifactId>
       <version>0.0.2</version>
  </dependency>
