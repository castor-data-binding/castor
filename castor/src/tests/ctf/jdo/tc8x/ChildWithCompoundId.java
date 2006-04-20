package ctf.jdo.tc8x;

public final class ChildWithCompoundId extends ParentWithCompoundId {
    private String _description;
    
    public String getDescription() {
        return _description;
    }
    
    public void setDescription(final String description) {
        this._description = description;
    }
}
