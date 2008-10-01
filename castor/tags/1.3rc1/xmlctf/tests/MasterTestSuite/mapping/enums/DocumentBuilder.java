import org.castor.xmlctf.ObjectModelBuilder;

public class DocumentBuilder implements ObjectModelBuilder {

    public Object buildInstance() throws Exception {
        Document document = new Document();
        document.setVersion(DocumentVersion.ADDOCUMENT_1_0);
        return document;
    }

}
