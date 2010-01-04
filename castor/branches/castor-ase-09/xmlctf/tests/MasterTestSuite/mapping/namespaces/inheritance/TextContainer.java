public class TextContainer
{
    private String _text = null;

    private String _type = null;

    public String getText()
    {
        return _text;
    }

    public void setText(final String text)
    {
        _text = text.trim();
    }

    public String getType()
    {
        return _type;
    }

    public void setType(final String type)
    {
        _type = type;
    }
}
