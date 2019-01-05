package vn.izisolution.xmlrpc.axmlrpc.serializer;

import vn.izisolution.xmlrpc.axmlrpc.XMLRPCException;
import vn.izisolution.xmlrpc.axmlrpc.XMLUtil;
import vn.izisolution.xmlrpc.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;

/**
 *
 * @author Tim Roes
 */
class LongSerializer implements Serializer {

	public Object deserialize(Element content) throws XMLRPCException {
		return Long.parseLong(XMLUtil.getOnlyTextContent(content.getChildNodes()));
	}

	public XmlElement serialize(Object object) {
		return XMLUtil.makeXmlTag(SerializerHandler.TYPE_LONG,
				((Long)object).toString());
	}

}
