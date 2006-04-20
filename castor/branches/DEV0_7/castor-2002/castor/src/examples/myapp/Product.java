package myapp;


import java.io.Serializable;

public class Product
    implements Serializable
{

    public int       id;


    private boolean  flag;


    public String    name = "";


    public double    price;


    public ProductGroup     group;


    public ProductInventory inventory;


    public String toString()
    {
	return ( Integer.toString( id ) ) + " " +
	    ( name == null ? "<no-name>" : name ) + " $" + price + " " +
	    ( group == null ? "<no-group>" : "[" + group.toString() + "]" ) + " " +
	    ( inventory == null ? "<no-stock>" : inventory.toString() );
    }


}
