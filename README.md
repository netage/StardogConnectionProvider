# StardogConnectionProvider
Repository containing code belonging to our [blog article](https://blog.netage.nl/using-the-stardog-connection-pool-in-a-java-container/) about connection pool usage with Stardog in Java Application servers

The demo assumes that Stardog is running at localhost and has a store available named 'mystore' with credential 'guest':'readonly' this can be changed in the [tomee.xml](tomee/src/main/tomee/conf/tomee.xml) to match your setup.

To run the demo use:
> mvn clean install
