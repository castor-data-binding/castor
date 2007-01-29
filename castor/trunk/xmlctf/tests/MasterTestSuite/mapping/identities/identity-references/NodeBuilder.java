import java.util.List;
import java.util.ArrayList;
import org.castor.xmlctf.ObjectModelBuilder;

public class NodeBuilder implements ObjectModelBuilder {

    /**
     * Build the object we expect when we unmarshal 'input.xml'.
     */
    public Object buildInstance() {
        NodeList nodeList = new NodeList();

        Node nodeOne = new Node();
        nodeOne.setNodeNum(1);

        Node nodeTwo = new Node();
        nodeTwo.setNodeNum(2);

        Node nodeThree = new Node();
        nodeThree.setNodeNum(3);

        List siblings = new ArrayList();
        siblings.add(nodeTwo);
        siblings.add(nodeThree);
        nodeOne.setSiblings(siblings);

        siblings = new ArrayList();
        siblings.add(nodeOne);
        siblings.add(nodeThree);
        nodeTwo.setSiblings(siblings);

        siblings = new ArrayList();
        siblings.add(nodeOne);
        siblings.add(nodeTwo);
        nodeThree.setSiblings(siblings);

        nodeList.addNode(nodeOne);
        nodeList.addNode(nodeTwo);
        nodeList.addNode(nodeThree);

        return nodeList;
    }

}
