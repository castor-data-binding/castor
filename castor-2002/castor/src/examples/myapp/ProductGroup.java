package myapp;


import java.io.Serializable;


public class ProductGroup
    implements Serializable
{


    public int        id;


    // Not null field
    public String     name = "";


    public String toString()
    {
	return id + " " +   ( name == null ? "<no-group>" : name );
    }


}

