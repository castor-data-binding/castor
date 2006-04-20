package myapp;

import org.exolab.castor.jdo.TimeStampable;

public class Computer
    extends Product implements TimeStampable
{


    private String  _cpu;


    private long    _timeStamp;


    public String getCpu()
    {
        return _cpu;
    }


    public void setCpu( String cpu )
    {
        _cpu = cpu;
    }


    public void jdoSetTimeStamp( long timeStamp )
    {
        _timeStamp = timeStamp;
    }


    public long jdoGetTimeStamp()
    {
        return _timeStamp;
    }


}
