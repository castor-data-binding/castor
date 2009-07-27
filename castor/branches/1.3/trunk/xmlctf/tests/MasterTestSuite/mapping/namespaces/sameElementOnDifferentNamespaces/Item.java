import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import org.xml.sax.InputSource;

public class Item
{
	private String description;
	private String mediaDescription;

	public Item()
	{
		description = null;
		mediaDescription = null;
	}

	public void setDescription(String val)
	{
		description = val;
	}

	public void setMediaDescription(String val)
	{
		mediaDescription = val;
	}

	public String getDescription()
	{
		return description;
	}

	public String getMediaDescription()
	{
		return mediaDescription;
	}

	public static void main(String[] args) throws Exception
	{
		Mapping mapping = new Mapping(Item.class.getClassLoader());
		mapping.loadMapping("mapping.xml");

		Unmarshaller unmarshaller = new Unmarshaller(mapping);
		unmarshaller.setClassLoader(Item.class.getClassLoader());
		unmarshaller.setValidation(false);
		Item item = (Item) unmarshaller.unmarshal(new InputSource(new java.io.FileInputStream(args[0])));

		System.out.println("description     =" + item.description);
		System.out.println("mediaDescription=" + item.mediaDescription);
	}
}
