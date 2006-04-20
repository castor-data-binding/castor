package myapp;


public class Computer
    extends Product
{

    public String cpu;


    public String toString()
    {
	return super.toString() + " " + cpu;
    }


}
