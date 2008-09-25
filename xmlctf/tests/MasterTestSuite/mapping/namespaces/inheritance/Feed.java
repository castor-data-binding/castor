public class Feed
{
    private TextContainer _title = null;
    private TextContainer _subtitle = null;
    private TextContainer _summary = null;
    private TextContainer _rights = null;

    public TextContainer getTitle()
    {
        return _title;
    }

    public void setTitle(final TextContainer title)
    {
        _title = title;
    }

    public TextContainer getSummary()
    {
        return _summary;
    }

    public void setSummary(final TextContainer summary)
    {
        _summary = summary;
    }

    public TextContainer getSubtitle()
    {
        return _subtitle;
    }

    public void setSubtitle(final TextContainer subtitle)
    {
        _subtitle = subtitle;
    }

    public TextContainer getRights()
    {
        return _rights;
    }

    public void setRights(final TextContainer rights)
    {
        _rights = rights;
    }
}
