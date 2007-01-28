import org.exolab.castor.tests.framework.ObjectModelBuilder;

public class DocumentWithHandlerBuilder implements ObjectModelBuilder {

    public Object buildInstance() throws Exception {
        Document document = new Document();
        document.setVersionWithHandler(DocumentVersion.ADDOCUMENT_1_1);
        return document;
    }

}
