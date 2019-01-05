package vn.izisolution.xmlrpc.axmlrpc.serializer;

import vn.izisolution.xmlrpc.axmlrpc.XMLRPCException;
import vn.izisolution.xmlrpc.axmlrpc.XMLUtil;
import vn.izisolution.xmlrpc.axmlrpc.xmlcreator.XmlElement;
import vn.izisolution.xmlrpc.base64.Base64;
import org.w3c.dom.Element;

/**
 *
 * @author Tim Roes
 */
public class Base64Serializer implements Serializer {

	public Object deserialize(Element content) throws XMLRPCException {
		return Base64.decode(XMLUtil.getOnlyTextContent(content.getChildNodes()));
	}

	public XmlElement serialize(Object object) {
		return XMLUtil.makeXmlTag(SerializerHandler.TYPE_BASE64,
				Base64.encode((Byte[])object));
	}

}