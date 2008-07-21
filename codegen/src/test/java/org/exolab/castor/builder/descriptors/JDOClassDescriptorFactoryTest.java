package org.exolab.castor.builder.descriptors;

import java.util.Iterator;
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.xml.JavaNamingImpl;
import org.exolab.castor.builder.BuilderConfiguration;
import org.exolab.castor.builder.factory.FieldMemberAndAccessorFactory;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.builder.info.nature.JDOClassInfoNature;
import org.exolab.castor.builder.info.nature.JDOFieldInfoNature;
import org.exolab.castor.builder.info.nature.XMLInfoNature;
import org.exolab.castor.builder.info.nature.relation.JDOOneToManyNature;
import org.exolab.castor.builder.info.nature.relation.JDOOneToOneNature;
import org.exolab.castor.builder.types.XSClass;
import org.exolab.castor.builder.types.XSInt;
import org.exolab.castor.builder.types.XSList;
import org.exolab.castor.builder.types.XSString;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JCodeStatement;
import org.exolab.javasource.JSourceCode;





/**
 * Unit test of the JDOClassDescriptorFactory
 * @author Filip Hianik
 *
 */
public class JDOClassDescriptorFactoryTest extends TestCase {

    /**
     * Logger.
     */
    private static final Log LOG = LogFactory.getLog(JDOClassDescriptorFactoryTest.class);
    
    private JDOClassDescriptorFactory _factory;
    private ClassInfo _classInfo;
    private BuilderConfiguration _config;
    private JClass _jClass;
    private JSourceCode _jsc;
    private JClass _classDesc;
    private String _statement;
    private Vector _vector;
    private Iterator _vIterator;
    private JCodeStatement _jStatement;
    private FieldInfo _fieldInfo;
    private JDOClassInfoNature _cNature;
    private JDOFieldInfoNature _fNature; 
    private XMLInfoNature _xmlNature;
    private FieldMemberAndAccessorFactory _memberAndAccessorFactory;
        
    /**
     * Default Constructor.
     */
    public JDOClassDescriptorFactoryTest() {
        super();
    }
        
    /**
     * Test SetUp Method.
     * 
     * @throws Exception
     *             If an error during SetUp occured.
     */
    protected final void setUp() throws Exception {
        LOG.debug("SetUp");
        super.setUp();
        
        _config = new BuilderConfiguration();
        _factory = new JDOClassDescriptorFactory(_config);
        
        //-- FieldInfo initialization
        JavaNamingImpl naming = new JavaNamingImpl();
        _memberAndAccessorFactory = new FieldMemberAndAccessorFactory(naming); 
    }
    
    /**
     * Test TearDown Method.
     * 
     * @throws Exception
     *             If an error during TearDown occured.
     */
    protected final void tearDown() throws Exception {
        LOG.debug("TearDown");
        super.tearDown();
    }
    
    public final void testCreateSourceEntity() {
     
        //-- ClassInfo initialization
        _jClass = new JClass("com.assembla.ase2.jdo.entity.Book");
        _classInfo = new ClassInfo(_jClass);
        _classInfo.addNature(JDOClassInfoNature.class.getName());
        _cNature = new JDOClassInfoNature(_classInfo);         
        
        //-- classInfo settings
        _cNature.setTableName("book");
        _cNature.setAccessMode(AccessMode.Shared);
        
        //-- fieldInfo settings
        XSString type = new XSString();
        
        //-- ISBN field
        String fieldName = "isbn";     
        _fieldInfo = new FieldInfo(type, fieldName, _memberAndAccessorFactory);
        _fieldInfo.addNature(JDOFieldInfoNature.class.getName());
                
        _xmlNature = new XMLInfoNature(_fieldInfo);
        _xmlNature.setNodeName(fieldName);
        _xmlNature.setRequired(true);
        
        _fNature = new JDOFieldInfoNature(_fieldInfo);
        _fNature.setColumnName("isbn");
        _fNature.setColumnType("varchar");
        _fNature.setDirty(false);
        _fNature.setReadOnly(false);        
        
        _classInfo.addFieldInfo(_fieldInfo);
        _cNature.addPrimaryKey(_xmlNature.getNodeName());
        
        //-- TITLE field
        fieldName = "title";     
        _fieldInfo = new FieldInfo(type, fieldName, _memberAndAccessorFactory);
        _fieldInfo.addNature(JDOFieldInfoNature.class.getName());
                
        _xmlNature = new XMLInfoNature(_fieldInfo);
        _xmlNature.setNodeName(fieldName);
        _xmlNature.setRequired(true);
        
        _fNature = new JDOFieldInfoNature(_fieldInfo);
        _fNature.setColumnName("title");
        _fNature.setColumnType("varchar");
        _fNature.setDirty(false);
        _fNature.setReadOnly(false);        
        
        _classInfo.addFieldInfo(_fieldInfo);
        
        //-- creating of source
        _classDesc = _factory.createSource(_classInfo);
        _jsc = _classDesc.getConstructor(0).getSourceCode();
        _vector = _jsc.getSource();
        _vIterator = _vector.iterator();
        
        //-- assertions
        _statement = "setTableName(\"book\");";
        assertTrue(checkSource(_statement));
        
        _statement = "setJavaClass(Book.class);";
        assertTrue(checkSource(_statement));
        
        _statement = "setAccessMode(AccessMode.valueOf(\"shared\"))";
        assertTrue(checkSource(_statement));
        
        _statement = "addCacheParam(\"name\", \"com.assembla.ase2.jdo.entity.Book\");";
        assertTrue(checkSource(_statement));
        
        _statement = "mapping.setAccess(ClassMappingAccessType.valueOf(\"shared\"));";
        assertTrue(checkSource(_statement));
        
        _statement = "mapping.setName(\"com.assembla.ase2.jdo.entity.Book\");";
        assertTrue(checkSource(_statement));
        
        _statement = "mapTo.setTable(\"book\");";
        assertTrue(checkSource(_statement));
        
        //-- ISBN
        _statement = "String isbnFieldName = \"isbn\";";
        assertTrue(checkSource(_statement));
    
        _statement = "JDOFieldDescriptorImpl isbnFieldDescr;";
        assertTrue(checkSource(_statement));
        
        _statement = "FieldMapping isbnFM = new FieldMapping();";
        assertTrue(checkSource(_statement));
        
        _statement = "TypeInfo isbnType = new TypeInfo(java.lang.String.class);";
        assertTrue(checkSource(_statement));
        
        _statement = "isbnType.setRequired(true);";
        assertTrue(checkSource(_statement));
        
        _statement = "FieldHandler isbnHandler;";
        assertTrue(checkSource(_statement));                
        
        _statement = "Method isbnGetMethod = Book.class.getMethod(\"getIsbn\", null);";
        assertTrue(checkSource(_statement));
        
        _statement = "Method isbnSetMethod = Book.class.getMethod(\"setIsbn\", new Class[]{";
        assertTrue(checkSource(_statement));
        
        _statement = "java.lang.String.class});";
        assertTrue(checkSource(_statement));
        
        _statement = "isbnHandler = new FieldHandlerImpl(isbnFieldName, null, null";
        assertTrue(checkSource(_statement));
        
        _statement = "isbnGetMethod, isbnSetMethod, isbnType);";
        assertTrue(checkSource(_statement));
        
        _statement = "isbnFieldDescr = new JDOFieldDescriptorImpl(isbnFieldName, isbnType,";
        assertTrue(checkSource(_statement));
        
        _statement = "isbnHandler, false, new String[] { \"isbn\" },";
        assertTrue(checkSource(_statement));
        
        _statement = "new int[] {SQLTypeInfos";
        assertTrue(checkSource(_statement));
        
        _statement = ".javaType2sqlTypeNum(java.lang.String.class) },";
        assertTrue(checkSource(_statement));
        
        _statement = "null, new String[] {}, false, false);";
        assertTrue(checkSource(_statement));
        
        _statement = "isbnFieldDescr.setContainingClassDescriptor(this);";
        assertTrue(checkSource(_statement));
        
        _statement = "isbnFieldDescr.setIdentity(true)";
        assertTrue(checkSource(_statement));

        _statement = "isbnFM.setIdentity(true);";
        assertTrue(checkSource(_statement));

        _statement = "isbnFM.setDirect(false);";
        assertTrue(checkSource(_statement));
        
        _statement = "isbnFM.setName(\"isbn\");";
        assertTrue(checkSource(_statement));
        
        _statement = "isbnFM.setRequired(true);";
        assertTrue(checkSource(_statement));
        
        _statement = "isbnFM.setSetMethod(\"setIsbn\");";
        assertTrue(checkSource(_statement));
        
        _statement = "isbnFM.setGetMethod(\"getIsbn\");";
        assertTrue(checkSource(_statement));
        
        _statement = "Sql isbnSql = new Sql();";
        assertTrue(checkSource(_statement));
        
        _statement = "isbnSql.addName(\"isbn\");";
        assertTrue(checkSource(_statement));
        
        _statement = "isbnSql.setType(\"varchar\");";
        assertTrue(checkSource(_statement));

        _statement = "isbnFM.setSql(isbnSql);";
        assertTrue(checkSource(_statement));

        _statement = "isbnFM.setType(\"java.lang.String\");";
        assertTrue(checkSource(_statement));

        _statement = "choice.addFieldMapping(isbnFM);";
        assertTrue(checkSource(_statement));
        
        //-- TITLE
        _statement = "String titleFieldName = \"title\";";
        assertTrue(checkSource(_statement));
    
        _statement = "JDOFieldDescriptorImpl titleFieldDescr;";
        assertTrue(checkSource(_statement));
        
        _statement = "FieldMapping titleFM = new FieldMapping();";
        assertTrue(checkSource(_statement));
        
        _statement = "TypeInfo titleType = new TypeInfo(java.lang.String.class);";
        assertTrue(checkSource(_statement));
        
        _statement = "titleType.setRequired(true);";
        assertTrue(checkSource(_statement));
        
        _statement = "FieldHandler titleHandler;";
        assertTrue(checkSource(_statement));                
        
        _statement = "Method titleGetMethod = Book.class.getMethod(\"getTitle\", null);";
        assertTrue(checkSource(_statement));
        
        _statement = "Method titleSetMethod = Book.class.getMethod(\"setTitle\", new Class[]{";
        assertTrue(checkSource(_statement));
        
        _statement = "java.lang.String.class});";
        assertTrue(checkSource(_statement));
        
        _statement = "titleHandler = new FieldHandlerImpl(titleFieldName, null, null";
        assertTrue(checkSource(_statement));
        
        _statement = "titleGetMethod, titleSetMethod, titleType);";
        assertTrue(checkSource(_statement));
        
        _statement = "titleFieldDescr = new JDOFieldDescriptorImpl(titleFieldName, titleType,";
        assertTrue(checkSource(_statement));
        
        _statement = "titleHandler, false, new String[] { \"title\" },";
        assertTrue(checkSource(_statement));
        
        _statement = "new int[] {SQLTypeInfos";
        assertTrue(checkSource(_statement));
        
        _statement = ".javaType2sqlTypeNum(java.lang.String.class) },";
        assertTrue(checkSource(_statement));
        
        _statement = "null, new String[] {}, false, false);";
        assertTrue(checkSource(_statement));
        
        _statement = "titleFieldDescr.setContainingClassDescriptor(this);";
        assertTrue(checkSource(_statement));
        
        _statement = "titleFieldDescr.setIdentity(false)";
        assertTrue(checkSource(_statement));

        _statement = "titleFM.setIdentity(false);";
        assertTrue(checkSource(_statement));

        _statement = "titleFM.setDirect(false);";
        assertTrue(checkSource(_statement));
        
        _statement = "titleFM.setName(\"title\");";
        assertTrue(checkSource(_statement));
        
        _statement = "titleFM.setRequired(true);";
        assertTrue(checkSource(_statement));
        
        _statement = "titleFM.setSetMethod(\"setTitle\");";
        assertTrue(checkSource(_statement));
        
        _statement = "titleFM.setGetMethod(\"getTitle\");";
        assertTrue(checkSource(_statement));
        
        _statement = "Sql titleSql = new Sql();";
        assertTrue(checkSource(_statement));
        
        _statement = "titleSql.addName(\"title\");";
        assertTrue(checkSource(_statement));
        
        _statement = "titleSql.setType(\"varchar\");";
        assertTrue(checkSource(_statement));

        _statement = "titleFM.setSql(titleSql);";
        assertTrue(checkSource(_statement));

        _statement = "titleFM.setType(\"java.lang.String\");";
        assertTrue(checkSource(_statement));

        _statement = "choice.addFieldMapping(titleFM);";
        assertTrue(checkSource(_statement));
        
        _statement = "setFields(new FieldDescriptor[] {titleFieldDescr});";
        assertTrue(checkSource(_statement));
        
        _statement = "setIdentities(new FieldDescriptor[] {isbnFieldDescr});";
        assertTrue(checkSource(_statement));

    }    
    
    public final void testCreateSourceOneToOne() {
        
        //-- ClassInfo initialization
        _jClass = new JClass("ctf.jdo.sg.onetoone.Employee");
        _classInfo = new ClassInfo(_jClass);
        _classInfo.addNature(JDOClassInfoNature.class.getName());
        _cNature = new JDOClassInfoNature(_classInfo);               
        
        //-- classInfo settings
        _cNature.setTableName("employee");
        _cNature.setAccessMode(AccessMode.Shared);
        
        //-- fieldInfo settings
        XSInt type = new XSInt(true);
        
        //-- ID
        String fieldName = "id";     
        _fieldInfo = new FieldInfo(type, fieldName, _memberAndAccessorFactory);
        _fieldInfo.addNature(JDOFieldInfoNature.class.getName());
        
        _xmlNature = new XMLInfoNature(_fieldInfo);
        _xmlNature.setNodeName(fieldName);
        _xmlNature.setRequired(true);
        
        _fNature = new JDOFieldInfoNature(_fieldInfo);
        _fNature.setColumnName("id");
        _fNature.setColumnType("integer");
        _fNature.setDirty(false);
        _fNature.setReadOnly(false);        
        
        _classInfo.addFieldInfo(_fieldInfo);
        _cNature.addPrimaryKey(_fieldInfo.getName());
        
        //-- ADDRESS_ID
        XSClass type2 = new XSClass(new JClass("ctf.jdo.sg.onetoone.Address"));
        fieldName = "address";     
        _fieldInfo = new FieldInfo(type2, fieldName, _memberAndAccessorFactory);
        _fieldInfo.addNature(JDOFieldInfoNature.class.getName());
        _fieldInfo.addNature(JDOOneToOneNature.class.getName());        
        
        JDOOneToOneNature oneNature = new JDOOneToOneNature(_fieldInfo);
        oneNature.addForeignKey("address_id");
                
        _xmlNature = new XMLInfoNature(_fieldInfo);
        _xmlNature.setNodeName(fieldName);
        _xmlNature.setRequired(true);
        
        _fNature = new JDOFieldInfoNature(_fieldInfo);
        _fNature.setColumnName("address");
        _fNature.setColumnType("integer");
        _fNature.setDirty(false);
        _fNature.setReadOnly(false);        
        
        _classInfo.addFieldInfo(_fieldInfo);
        
        //-- creating of source
        _classDesc = _factory.createSource(_classInfo);
        _jsc = _classDesc.getConstructor(0).getSourceCode();
        _vector = _jsc.getSource();
        _vIterator = _vector.iterator();
        
        //-- assertions
        _statement = "setTableName(\"employee\");";
        assertTrue(checkSource(_statement));
        
        _statement = "setJavaClass(Employee.class);";
        assertTrue(checkSource(_statement));
        
        _statement = "setAccessMode(AccessMode.valueOf(\"shared\"))";
        assertTrue(checkSource(_statement));
        
        _statement = "addCacheParam(\"name\", \"ctf.jdo.sg.onetoone.Employee\");";
        assertTrue(checkSource(_statement));
        
        _statement = "mapping.setAccess(ClassMappingAccessType.valueOf(\"shared\"));";
        assertTrue(checkSource(_statement));
        
        _statement = "mapping.setName(\"ctf.jdo.sg.onetoone.Employee\");";
        assertTrue(checkSource(_statement));
        
        _statement = "mapTo.setTable(\"employee\");";
        assertTrue(checkSource(_statement));
        
        //-- ID
        _statement = "String idFieldName = \"id\";";
        assertTrue(checkSource(_statement));
    
        _statement = "JDOFieldDescriptorImpl idFieldDescr;";
        assertTrue(checkSource(_statement));
        
        _statement = "FieldMapping idFM = new FieldMapping();";
        assertTrue(checkSource(_statement));
        
        _statement = "TypeInfo idType = new TypeInfo(java.lang.Integer.class);";
        assertTrue(checkSource(_statement));
        
        _statement = "idType.setRequired(true);";
        assertTrue(checkSource(_statement));
        
        _statement = "FieldHandler idHandler;";
        assertTrue(checkSource(_statement));                
        
        _statement = "Method idGetMethod = Employee.class.getMethod(\"getId\", null);";
        assertTrue(checkSource(_statement));
        
        _statement = "Method idSetMethod = Employee.class.getMethod(\"setId\", new Class[]{";
        assertTrue(checkSource(_statement));
        
        _statement = "java.lang.Integer.class});";
        assertTrue(checkSource(_statement));
        
        _statement = "idHandler = new FieldHandlerImpl(idFieldName, null, null";
        assertTrue(checkSource(_statement));
        
        _statement = "idGetMethod, idSetMethod, idType);";
        assertTrue(checkSource(_statement));
        
        _statement = "idFieldDescr = new JDOFieldDescriptorImpl(idFieldName, idType,";
        assertTrue(checkSource(_statement));
        
        _statement = "idHandler, false, new String[] { \"id\" },";
        assertTrue(checkSource(_statement));
        
        _statement = "new int[] {SQLTypeInfos";
        assertTrue(checkSource(_statement));
        
        _statement = ".javaType2sqlTypeNum(java.lang.Integer.class) },";
        assertTrue(checkSource(_statement));
        
        _statement = "null, new String[] {}, false, false);";
        assertTrue(checkSource(_statement));
        
        _statement = "idFieldDescr.setContainingClassDescriptor(this);";
        assertTrue(checkSource(_statement));
        
        _statement = "idFieldDescr.setIdentity(true)";
        assertTrue(checkSource(_statement));

        _statement = "idFM.setIdentity(true);";
        assertTrue(checkSource(_statement));

        _statement = "idFM.setDirect(false);";
        assertTrue(checkSource(_statement));
        
        _statement = "idFM.setName(\"id\");";
        assertTrue(checkSource(_statement));
        
        _statement = "idFM.setRequired(true);";
        assertTrue(checkSource(_statement));
        
        _statement = "idFM.setSetMethod(\"setId\");";
        assertTrue(checkSource(_statement));
        
        _statement = "idFM.setGetMethod(\"getId\");";
        assertTrue(checkSource(_statement));
        
        _statement = "Sql idSql = new Sql();";
        assertTrue(checkSource(_statement));
        
        _statement = "idSql.addName(\"id\");";
        assertTrue(checkSource(_statement));
        
        _statement = "idSql.setType(\"integer\");";
        assertTrue(checkSource(_statement));

        _statement = "idFM.setSql(idSql);";
        assertTrue(checkSource(_statement));

        _statement = "idFM.setType(\"java.lang.Integer\");";
        assertTrue(checkSource(_statement));

        _statement = "choice.addFieldMapping(idFM);";
        assertTrue(checkSource(_statement));
        
        //-- ADDRESS_ID
        _statement = "String addressFieldName = \"address\";";
        assertTrue(checkSource(_statement));
        
        _statement = "String addressSqlName = \"address_id\";";
        assertTrue(checkSource(_statement));
        
        _statement = "JDOFieldDescriptorImpl addressFieldDescr;";
        assertTrue(checkSource(_statement));
        
        _statement = "FieldMapping addressFM = new FieldMapping();";
        assertTrue(checkSource(_statement));
        
        _statement = "TypeInfo addressType = new TypeInfo(ctf.jdo.sg.onetoone.Address.class);";
        assertTrue(checkSource(_statement));
        
        _statement = "addressType.setRequired(true);";
        assertTrue(checkSource(_statement));
        
        _statement = "FieldHandler addressHandler;";
        assertTrue(checkSource(_statement));                
        
        _statement = "Method addressGetMethod = Employee.class.getMethod(\"getAddress\", null);";
        assertTrue(checkSource(_statement));
        
        _statement = "Method addressSetMethod = Employee.class.getMethod(\"setAddress\", new Class[]{";
        assertTrue(checkSource(_statement));
        
        _statement = "ctf.jdo.sg.onetoone.Address.class});";
        assertTrue(checkSource(_statement));
        
        _statement = "addressHandler = new FieldHandlerImpl(addressFieldName, null, null";
        assertTrue(checkSource(_statement));
        
        _statement = "addressGetMethod, addressSetMethod, addressType);";
        assertTrue(checkSource(_statement));
        
        _statement = "addressFieldDescr = new JDOFieldDescriptorImpl(addressFieldName, addressType,";
        assertTrue(checkSource(_statement));
        
        _statement = "addressHandler, false, new String[] { addressSqlName },";
        assertTrue(checkSource(_statement));
        
        _statement = "new int[] {SQLTypeInfos";
        assertTrue(checkSource(_statement));
        
        _statement = ".javaType2sqlTypeNum(ctf.jdo.sg.onetoone.Address.class) },";
        assertTrue(checkSource(_statement));
        
        _statement = "null, new String[] { addressSqlName }, false, false);";
        assertTrue(checkSource(_statement));
        
        _statement = "addressFieldDescr.setContainingClassDescriptor(this);";
        assertTrue(checkSource(_statement));
        
        _statement = "addressFieldDescr.setClassDescriptor(new AddressJDODescriptor());";
        assertTrue(checkSource(_statement));
        
        _statement = "addressFM.setIdentity(false);";
        assertTrue(checkSource(_statement));

        _statement = "addressFM.setDirect(false);";
        assertTrue(checkSource(_statement));
        
        _statement = "addressFM.setName(\"address\");";
        assertTrue(checkSource(_statement));
        
        _statement = "addressFM.setRequired(true);";
        assertTrue(checkSource(_statement));
        
        _statement = "addressFM.setSetMethod(\"setAddress\");";
        assertTrue(checkSource(_statement));
        
        _statement = "addressFM.setGetMethod(\"getAddress\");";
        assertTrue(checkSource(_statement));
        
        _statement = "Sql addressSql = new Sql();";
        assertTrue(checkSource(_statement));
                 
        _statement = "addressSql.addName(\"address_id\");";
        assertTrue(checkSource(_statement));
                
        _statement = "addressSql.setManyKey(new String[] {\"address_id\"});";
        assertTrue(checkSource(_statement));
        
        _statement = "addressFM.setSql(addressSql);";
        assertTrue(checkSource(_statement));

        _statement = "addressFM.setType(\"ctf.jdo.sg.onetoone.Address\");";
        assertTrue(checkSource(_statement));

        _statement = "choice.addFieldMapping(addressFM);";
        assertTrue(checkSource(_statement));
        
        _statement = "setFields(new FieldDescriptor[] {addressFieldDescr});";
        assertTrue(checkSource(_statement));
        
        _statement = "setIdentities(new FieldDescriptor[] {idFieldDescr});";
        assertTrue(checkSource(_statement));
    }   
    
    public final void testCreateSourceOneToMany() {
        
      //-- ClassInfo initialization
        _jClass = new JClass("org.castor.cpa.functional.onetomany.House");
        _classInfo = new ClassInfo(_jClass);
        _classInfo.addNature(JDOClassInfoNature.class.getName());
        _cNature = new JDOClassInfoNature(_classInfo);               
        
        //-- classInfo settings
        _cNature.setTableName("house");
        _cNature.setAccessMode(AccessMode.Shared);
        
        //-- fieldInfo settings
        XSInt type = new XSInt(true);
        
        //-- ID
        String fieldName = "id";     
        _fieldInfo = new FieldInfo(type, fieldName, _memberAndAccessorFactory);
        _fieldInfo.addNature(JDOFieldInfoNature.class.getName());
        
        _xmlNature = new XMLInfoNature(_fieldInfo);
        _xmlNature.setNodeName(fieldName);
        _xmlNature.setRequired(true);
        
        _fNature = new JDOFieldInfoNature(_fieldInfo);
        _fNature.setColumnName("id");
        _fNature.setColumnType("integer");
        _fNature.setDirty(false);
        _fNature.setReadOnly(false);        
        
        _classInfo.addFieldInfo(_fieldInfo);
        _cNature.addPrimaryKey(_fieldInfo.getName());
        
        //-- FLAT_ID
        XSList type2 = new XSList("array", 
                new XSClass(new JClass("org.castor.cpa.functional.onetomany.Flat")), true);
        fieldName = "flat";     
        _fieldInfo = new FieldInfo(type2, fieldName, _memberAndAccessorFactory);
        _fieldInfo.addNature(JDOFieldInfoNature.class.getName());
        _fieldInfo.addNature(JDOOneToManyNature.class.getName());        
        
        JDOOneToManyNature manyNature = new JDOOneToManyNature(_fieldInfo);
        manyNature.addForeignKey("flat_id");
                
        _xmlNature = new XMLInfoNature(_fieldInfo);
        _xmlNature.setNodeName(fieldName);
        _xmlNature.setRequired(true);
        
        _fNature = new JDOFieldInfoNature(_fieldInfo);
        _fNature.setColumnName("flat");
        _fNature.setColumnType("integer");
        _fNature.setDirty(false);
        _fNature.setReadOnly(false);        
        
        _classInfo.addFieldInfo(_fieldInfo);
        
        //-- creating of source
        _classDesc = _factory.createSource(_classInfo);
        _jsc = _classDesc.getConstructor(0).getSourceCode();
        _vector = _jsc.getSource();
        _vIterator = _vector.iterator();
        
        //-- assertions
        _statement = "setTableName(\"house\");";
        assertTrue(checkSource(_statement));
        
        _statement = "setJavaClass(House.class);";
        assertTrue(checkSource(_statement));
        
        _statement = "setAccessMode(AccessMode.valueOf(\"shared\"))";
        assertTrue(checkSource(_statement));
        
        _statement = "addCacheParam(\"name\", \"org.castor.cpa.functional.onetomany.House\");";
        assertTrue(checkSource(_statement));
        
        _statement = "mapping.setAccess(ClassMappingAccessType.valueOf(\"shared\"));";
        assertTrue(checkSource(_statement));
        
        _statement = "mapping.setName(\"org.castor.cpa.functional.onetomany.House\");";
        assertTrue(checkSource(_statement));
        
        _statement = "mapTo.setTable(\"house\");";
        assertTrue(checkSource(_statement));
        
        //-- ID
        _statement = "String idFieldName = \"id\";";
        assertTrue(checkSource(_statement));
    
        _statement = "JDOFieldDescriptorImpl idFieldDescr;";
        assertTrue(checkSource(_statement));
        
        _statement = "FieldMapping idFM = new FieldMapping();";
        assertTrue(checkSource(_statement));
        
        _statement = "TypeInfo idType = new TypeInfo(java.lang.Integer.class);";
        assertTrue(checkSource(_statement));
        
        _statement = "idType.setRequired(true);";
        assertTrue(checkSource(_statement));
        
        _statement = "FieldHandler idHandler;";
        assertTrue(checkSource(_statement));                
        
        _statement = "Method idGetMethod = House.class.getMethod(\"getId\", null);";
        assertTrue(checkSource(_statement));
        
        _statement = "Method idSetMethod = House.class.getMethod(\"setId\", new Class[]{";
        assertTrue(checkSource(_statement));
        
        _statement = "java.lang.Integer.class});";
        assertTrue(checkSource(_statement));
        
        _statement = "idHandler = new FieldHandlerImpl(idFieldName, null, null";
        assertTrue(checkSource(_statement));
        
        _statement = "idGetMethod, idSetMethod, idType);";
        assertTrue(checkSource(_statement));
        
        _statement = "idFieldDescr = new JDOFieldDescriptorImpl(idFieldName, idType,";
        assertTrue(checkSource(_statement));
        
        _statement = "idHandler, false, new String[] { \"id\" },";
        assertTrue(checkSource(_statement));
        
        _statement = "new int[] {SQLTypeInfos";
        assertTrue(checkSource(_statement));
        
        _statement = ".javaType2sqlTypeNum(java.lang.Integer.class) },";
        assertTrue(checkSource(_statement));
        
        _statement = "null, new String[] {}, false, false);";
        assertTrue(checkSource(_statement));
        
        _statement = "idFieldDescr.setContainingClassDescriptor(this);";
        assertTrue(checkSource(_statement));
        
        _statement = "idFieldDescr.setIdentity(true)";
        assertTrue(checkSource(_statement));

        _statement = "idFM.setIdentity(true);";
        assertTrue(checkSource(_statement));

        _statement = "idFM.setDirect(false);";
        assertTrue(checkSource(_statement));
        
        _statement = "idFM.setName(\"id\");";
        assertTrue(checkSource(_statement));
        
        _statement = "idFM.setRequired(true);";
        assertTrue(checkSource(_statement));
        
        _statement = "idFM.setSetMethod(\"setId\");";
        assertTrue(checkSource(_statement));
        
        _statement = "idFM.setGetMethod(\"getId\");";
        assertTrue(checkSource(_statement));
        
        _statement = "Sql idSql = new Sql();";
        assertTrue(checkSource(_statement));
        
        _statement = "idSql.addName(\"id\");";
        assertTrue(checkSource(_statement));
        
        _statement = "idSql.setType(\"integer\");";
        assertTrue(checkSource(_statement));

        _statement = "idFM.setSql(idSql);";
        assertTrue(checkSource(_statement));

        _statement = "idFM.setType(\"java.lang.Integer\");";
        assertTrue(checkSource(_statement));

        _statement = "choice.addFieldMapping(idFM);";
        assertTrue(checkSource(_statement));
    
        //-- FLAT_ID
        _statement = "String flatFieldName = \"flat\";";
        assertTrue(checkSource(_statement));
        
        _statement = "String flatSqlName = \"flat_id\";";
        assertTrue(checkSource(_statement));
        
        _statement = "JDOFieldDescriptorImpl flatFieldDescr;";
        assertTrue(checkSource(_statement));
        
        _statement = "FieldMapping flatFM = new FieldMapping();";
        assertTrue(checkSource(_statement));
        
        _statement = "TypeInfo flatType = new TypeInfo(org.castor.cpa.functional.onetomany.Flat.class);";
        assertTrue(checkSource(_statement));
        
        _statement = "flatType.setRequired(true);";
        assertTrue(checkSource(_statement));
        
        _statement = "FieldHandler flatHandler;";
        assertTrue(checkSource(_statement));                
        
        _statement = "Method flatGetMethod = House.class.getMethod(\"getFlat\", null);";
        assertTrue(checkSource(_statement));
        
        _statement = "Method flatSetMethod = House.class.getMethod(\"setFlat\", new Class[]{";
        assertTrue(checkSource(_statement));
        
        _statement = "org.castor.cpa.functional.onetomany.Flat[].class});";
        assertTrue(checkSource(_statement));
                
        _statement = "flatHandler = new FieldHandlerImpl(flatFieldName, null, null";
        assertTrue(checkSource(_statement));
        
        _statement = "flatGetMethod, flatSetMethod, flatType);";
        assertTrue(checkSource(_statement));
        
        _statement = "flatFieldDescr = new JDOFieldDescriptorImpl(flatFieldName, flatType,";
        assertTrue(checkSource(_statement));
        
        _statement = "flatHandler, false, new String[] { },";
        assertTrue(checkSource(_statement));
        
        _statement = "new int[] {SQLTypeInfos";
        assertTrue(checkSource(_statement));
        
        _statement = ".javaType2sqlTypeNum(org.castor.cpa.functional.onetomany.Flat.class) },";
        assertTrue(checkSource(_statement));
        
        _statement = "null, new String[] { flatSqlName }, false, false);";
        assertTrue(checkSource(_statement));
        
        _statement = "flatFieldDescr.setContainingClassDescriptor(this);";
        assertTrue(checkSource(_statement));
        
        _statement = "flatFieldDescr.setClassDescriptor(new FlatJDODescriptor());";
        assertTrue(checkSource(_statement));
        
        _statement = "flatFieldDescr.setMultivalued(true);";
        assertTrue(checkSource(_statement));
        
        _statement = "flatFM.setIdentity(false);";
        assertTrue(checkSource(_statement));

        _statement = "flatFM.setDirect(false);";
        assertTrue(checkSource(_statement));
        
        _statement = "flatFM.setName(\"flat\");";
        assertTrue(checkSource(_statement));
        
        _statement = "flatFM.setRequired(true);";
        assertTrue(checkSource(_statement));
        
        _statement = "flatFM.setCollection(FieldMappingCollectionType.ARRAY);";
        assertTrue(checkSource(_statement));
        
        _statement = "Sql flatSql = new Sql();";
        assertTrue(checkSource(_statement));
                 
        _statement = "flatSql.addName(\"flat_id\");";
        assertTrue(checkSource(_statement));
                
        _statement = "flatSql.setManyKey(new String[] {\"flat_id\"});";
        assertTrue(checkSource(_statement));
        
        _statement = "flatFM.setSql(flatSql);";
        assertTrue(checkSource(_statement));

        _statement = "flatFM.setType(\"org.castor.cpa.functional.onetomany.Flat\");";
        assertTrue(checkSource(_statement));

        _statement = "choice.addFieldMapping(flatFM);";
        assertTrue(checkSource(_statement));
        
        _statement = "setFields(new FieldDescriptor[] {flatFieldDescr});";
        assertTrue(checkSource(_statement));
        
        _statement = "setIdentities(new FieldDescriptor[] {idFieldDescr});";
        assertTrue(checkSource(_statement));
        
    }
    
    //-------
    // Helper
    //-------
    /**
     * Checks if the statement was correctly created.
     * @param statement Statement which is to find
     * @return true if Statement is in JSourceCode, false otherwise
     */
    private boolean checkSource(final String statement) {
        while (_vIterator.hasNext()) {
            _jStatement = (JCodeStatement) _vIterator.next();
            if (_jStatement.toString().indexOf(statement) > -1) {
                return true;
            }
        }
        return false;
    }
    
}
