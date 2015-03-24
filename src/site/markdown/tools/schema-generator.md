# The Ant Task for the Castor schema generator

Describes how to use the Ant task for the Castor XML schema gnerator and
what is currently supported.

## Castor XML schema generator Ant task

      
An alternative to using the command line as shown in the previous section,
the Castor XML schema generator Ant task can be used to call the XML schema
generator for XML schema generation. The only requirement is that the
castor-&lt;version&gt;-anttasks.jar must be on the CLASSPATH.

## Configuration

Please find below the complete list of parameters that can be set on the Castor
XML schema generator.
        
| Attributes | Description | Required
| -- | -- | --
| *file* | The name of the XML file to use as input for XML schema generation. | Yes
| *schemaFileName* | The name of the XML schema file to be generated; if not specified, a suffix '.xsd' will be appended to the input file name. | No
| *defaultGroupingAsAll* | To indicate that &lt;xsd:all/&gt; should be used as default grouping. | No
        
Alternatively to specifying the `file` property, it is possible to work 
with a nested `FileSet` element or with the `dir` property.
        
## Example
        
Below is an example of how to use this task from within an Ant target
definition named `castor:schema:gen`:
        
```
  <target name="castor:schema:gen" depends="init"
          description="Generate an XML schema from an XML document instance.">

    <taskdef name="schema-gen"
             classname="org.castor.anttask.XMLInstance2SchemaTask"
             classpathref="castor.class.path" />
    <mkdir dir="generated" />
    <schema-gen file="src/main/resources/input.xml"
                schemaFileName="target/generated/schema/input.xsd" />
  </target>
```
