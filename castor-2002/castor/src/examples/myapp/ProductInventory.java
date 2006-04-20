package myapp;


import java.io.Serializable;


public class ProductInventory
    implements Serializable
{


    public Product   product;


    public int       quantity;


    public String toString()
    {
	return "in stock: " + quantity;
    }


}
