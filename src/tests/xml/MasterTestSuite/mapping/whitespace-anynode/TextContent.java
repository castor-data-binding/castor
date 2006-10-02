/**
 *
 */
import org.exolab.castor.types.AnyNode;

/**
 * Test Klasse zum Testen des Castor Any-Nodes
 */
public class TextContent {
    private AnyNode contentNode = new AnyNode(AnyNode.ELEMENT, "content-node",
                                              null, null, null);
    private String contentString;

    /**
     * Default Constrcutor
     */
    public TextContent() {
    }

    /**
     * @return the contentNode
     */
    public AnyNode getContentNode() {
        return contentNode;
    }

    /**
     * @return the contentString
     */
    public String getContentString() {
        return contentString;
    }

    /**
     * @param contentNode the contentNode to set
     */
    public void setContentNode(AnyNode contentNode) {
        this.contentNode = contentNode;
    }

    /**
     * @param contentString the contentString to set
     */
    public void setContentString(String contentString) {
        this.contentString = contentString;
    }
}
