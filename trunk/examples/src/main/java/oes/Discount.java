package oes;


import java.util.Date;


public class Discount
{


    private Price     _price;


    private int       _discountId;


    private int       _minimum;


    private int       _discount;


    private Date      _effectiveFrom;


    private Date      _effectiveTo;


    public Price getPrice()
    {
        return _price;
    }


    public void setPrice( Price price )
    {
        _price = price;
    }


    public int getDiscountId()
    {
        return _discountId;
    }


    public void setDiscountId( int id )
    {
        _discountId = id;
    }


    public int getMinimum()
    {
        return _minimum;
    }


    public void setMinimum( int minimum )
    {
        _minimum = minimum;
    }


    public int getDiscount()
    {
        return _discount;
    }


    public void setDiscount( int discount )
    {
        _discount = discount;
    }


    public Date getEffectiveFrom()
    {
        return _effectiveFrom;
    }


    public void setEffectiveFrom( Date date )
    {
        _effectiveFrom = date;
    }


    public boolean hasEffectiveFrom()
    {
        return _effectiveFrom != null;
    }


    public void deleteEffectiveFrom()
    {
        _effectiveFrom = null;
    }


    public Date getEffectiveTo()
    {
        return _effectiveTo;
    }


    public void setEffectiveTo( Date date )
    {
        _effectiveTo = date;
    }


    public boolean hasEffectiveTo()
    {
        return _effectiveTo != null;
    }


    public void deleteEffectiveTo()
    {
        _effectiveTo = null;
    }


}
