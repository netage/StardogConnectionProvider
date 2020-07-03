# StardogConnectionProvider
Repository containing code belonging to our [blog article](https://blog.netage.nl/using-the-stardog-connection-pool-in-a-java-container/) about connection pool usage with Stardog in Java Application servers

The demo assumes that Stardog is running at localhost and has a store available named 'mystore' with credential 'guest':'readonly' this can be changed in the [tomee.xml](tomee/src/main/tomee/conf/tomee.xml) to match your setup.

To run the demo use:
> mvn clean install

The demo has a deliberate error to show how the connection pool works the [webapp](webapp/src/main/java/nl/netage/storetest/Reader.java#L111) does not close a connection which will result in a pool error after a number of tries since the connection is not returned.

This can be observed with JConsole see the [blog article](https://blog.netage.nl/using-the-stardog-connection-pool-in-a-java-container/) for more information

### Note

the demo will install artifacts in your local .m2 repository if you want to get rid of them, simply remove:
> ~/.m2/repository/nl/netage/demo
