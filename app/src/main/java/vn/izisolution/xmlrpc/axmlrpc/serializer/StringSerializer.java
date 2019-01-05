package vn.izisolution.xmlrpc.axmlrpc.serializer;

import vn.izisolution.xmlrpc.axmlrpc.XMLRPCException;
import vn.izisolution.xmlrpc.axmlrpc.XMLUtil;
import vn.izisolution.xmlrpc.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;

/**
 *
 * @author Tim Roes
 */
public class StringSerializer implements Serializer {

	private boolean decodeStrings;
	private boolean encodeStrings;

	public StringSerializer(boolean encodeStrings, boolean decodeStrings) {
		this.decodeStrings = decodeStrings;
		this.encodeStrings = encodeStrings;
	}

	public Object deserialize(Element content) throws XMLRPCException {
		String text = XMLUtil.getOnlyTextContent(content.getChildNodes());
		if(decodeStrings) {
			text = text.replaceAll("&lt;", "<").replaceAll("&amp;", "&");
		}
		return text;
	}

	public XmlElement serialize(Object object) {
		String content = object.toString();
		if(encodeStrings) {
			content = content
					.replaceAll("&", "&amp;")
					.replaceAll("<", "&lt;")
					.replaceAll("]]>", "]]&gt;");
		}
		return XMLUtil.makeXmlTag(SerializerHandler.TYPE_STRING, content);
	}

}