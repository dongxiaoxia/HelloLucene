package xyz.dongxiaoxia.util;

import org.dom4j.DocumentHelper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class XmlUtil {
    private static final String noResult = "<result>no result</result>";

    public static org.dom4j.Document createFromString(String xml){
        try {
            return DocumentHelper.parseText(xml);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String parseObject2XmlString(Object object){
        if (object == null){
            return noResult;
        }
        StringWriter sw = new StringWriter();
        JAXBContext jaxbContext;
        Marshaller marshaller;
        try {
            jaxbContext = JAXBContext.newInstance(object.getClass());
            marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(object,sw);
            return sw.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            return noResult;
        }
    }
}
