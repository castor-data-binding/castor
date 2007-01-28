/*
 * Created on Jul 28, 2006
 *
 * Interface to set & get common filter field properties.
 */

public interface FilterPropertyInterface {

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return String
     * @return the value of field 'content'.
     */
    public java.lang.String getContent();
 
    /**
     * Returns the value of field 'title'.
     * 
     * @return String
     * @return the value of field 'title'.
     */
    public java.lang.String getTitle();

    /**
     * Returns the value of field 'update'.
     * 
     * @return boolean
     * @return the value of field 'update'.
     */
    public boolean getUpdate();

    /**
     * Returns the value of field 'view'.
     * 
     * @return boolean
     * @return the value of field 'view'.
     */
    public boolean getView();

    /**
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(java.lang.String content);

    /**
     * Sets the value of field 'title'.
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(java.lang.String title);

    /**
     * Sets the value of field 'update'.
     * 
     * @param update the value of field 'update'.
     */
    public void setUpdate(boolean update);

    /**
     * Sets the value of field 'view'.
     * 
     * @param view the value of field 'view'.
     */
    public void setView(boolean view);
    
}
