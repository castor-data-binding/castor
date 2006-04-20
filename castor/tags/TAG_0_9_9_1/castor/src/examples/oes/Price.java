package oes;


import java.util.Collection;


public class Price
{


    private int       _priceId;


    private double    _msrp;


    private double    _cost;


    private boolean   _hasCost;


    private String      _currency;


    private Collection  _discounts;


    public int getPriceId()
    {
        return _priceId;
    }


    public void setPriceId( int id )
    {
        _priceId = id;
    }


    public double getMsrp()
    {
        return _msrp;
    }


    public void setMsrp( double msrp )
    {
        _msrp = msrp;
    }


    public double getCost()
    {
        return _cost;
    }


    public void setCost( double cost )
    {
        _cost = cost;
        _hasCost = true;
    }


    public boolean hasCost()
    {
        return _hasCost;
    }


    public void deleteCost()
    {
        _hasCost = false;
    }


    public String getCurrency()
    {
        return _currency;
    }


    public void setCurrency( String currency )
    {
        if ( currency.length() != 3 )
            throw new IllegalArgumentException( "Argument 'currency' is three letters" );
        _currency = currency;
    }


    public Collection getDiscounts()
    {
        return _discounts;
    }


    public Discount createDiscount()
    {
        return new Discount();
    }


}
