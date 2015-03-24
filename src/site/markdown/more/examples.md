# Running The Examples

      
In order to run the examples located in `src/examples`, there are
a number of steps that must first take place. These steps are outlined below:
      

## Step 1

Get the Castor sources from http://github.com/castor-data-binding. 
 
## Step 2
 
If it is not set yet, please set the `JAVA_HOME` environment variable.

## Step 3
Edit `src/examples/jdo/database.xml` for the database being used.
(There are examples for all supported databases in `src/tests/jdo`.)

## Step 4
Build the examples like so:

```
build.[sh|bat] examples
```

This will not only build the examples, but will also build the Castor classes.

# Step 5

Add the appropriate classes for the database driver to the `CLASSPATH`.

## Step 6
Run the examples using the directions in the next section.
      
The script files `example.sh` and `example.bat` can
be used to run the provided examples. The first argument is the example
package name. For example, to build and run the JDO example:
      
```
  build.sh examples
  example.sh jdo
```

# Castor JDO
      
The JDO sample will test persistence between a set of Java classes
(Product, ProductGroup, ProductDetail) and a known SQL schema.
You can use the `create.sql` file to generate the database schema
for the purpose of this test.
      
The mapping between the Java objects and SQL schema is described in
the file `mapping.xml`. The JDBC connection to use is described
in the file `database.xml`. The provided file uses PostgreSQL as
the database server and JDBC driver.  You can modify this file for your
database server and use any JDBC driver.
      
The database information and mapping are automatically read from the
JDO example directory. There is no need to provide any parameters on
the command line.
      
The performance test uses the ODMG engine as well as direct JDBC
access. You must provide the JDBC URI and class name to use for
this test on the command line.
      
Usage:

```
  example.sh jdo
```

# Castor DSML
      
Do not run this sample against a production LDAP server unless you
are sure it will not affect the information in your directory!
      
The DSML sample will attempt to import the supplied directory
information from the `test.xml` file using the import policy described
in the `import.xml`. file. It will then run a search against the
LDAP server using the search criteria in the `search.xml` file and dump it
to the console.
      
You must modify `search.xml`, `import.xml` and
`test.xml` and adapt them to your LDAP directory structure.
The default files assumes the root directory `dc=intalio,dc=com` and
includes some sample LDAP entries.
      
*Usage*:

```      
  test.sh dsml [jndi|mozilla] <host> <root-dn> <root-pwd>

  jndi|mozilla  Selects whether to use the JNDI or Mozilla Directory
            SDK implementations
  host          The LDAP host name (port number is optional)
  rood-dn       The root DN used for authenticating when importing
  root-pwd      The root password used for authenticating when importing
```      
      
For example:
      
```      
  example.sh dsml mozilla ldap.intalio.com dc=intalio,dc=com secret
```      

