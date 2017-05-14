package ch.imedias.rsccfx.model.xml;

import ch.imedias.rsccfx.RsccApp;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SupporterHelper {

  private static final Logger LOGGER =
      Logger.getLogger(SupporterHelper.class.getName());
  private final Preferences preferences = Preferences.userNodeForPackage(RsccApp.class);
  private static final String SUPPORT_ADDRESSES = "supportAddresses";


  public List<Supporter> getSupportersFromXml(){
    List<Supporter> supportersList = null;
    try {
      File file = new File("G:\\file.xml");
      JAXBContext jaxbContext = JAXBContext.newInstance(Supporters.class);

      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      Supporters supporters = (Supporters) jaxbUnmarshaller.unmarshal(file);
      supportersList = supporters.getSupporters();
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    return supportersList;
  }

  public void supportersToXml(List<Supporter> supporters) {
    try {
      File file = new File("G:\\file.xml");
      JAXBContext jaxbContext = JAXBContext.newInstance(Supporters.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

      Supporters owl = new Supporters();
      owl.setSupporters(supporters);

      // output pretty printed
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.marshal(owl, file);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
  }

    /**
   * create default supporter list from file
   * @return list of supporter
   */
  private List<Supporter> getDefaultXmlList() {
    List<Supporter> defaultList = new ArrayList<>();
    try {

      File fXmlFile = new File(getClass().getClassLoader().getResource("rscc-defaults-lernstick.xml").getFile());
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fXmlFile);

      doc.getDocumentElement().normalize();

      NodeList nodeList = doc.getElementsByTagName("address");

      for (int i = 0; i < nodeList.getLength(); i++) {
        Node node = nodeList.item(i);

        if (node.getNodeType() == Node.ELEMENT_NODE) {
          Element element = (Element) node;

          String description = element.getElementsByTagName("description").item(0).getTextContent();
          String address = element.getElementsByTagName("address").item(0).getTextContent();
          String port = element.getElementsByTagName("port").item(0).getTextContent();;
          boolean encrypted = Boolean.parseBoolean(element.getElementsByTagName("encrypted").item(0).getTextContent());
          boolean chargeable = Boolean.parseBoolean(element.getElementsByTagName("chargeable").item(0).getTextContent());
          defaultList.add(new Supporter(description, address, port, encrypted, chargeable));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return defaultList;
  }

  /**
   * Gets the supporter list.
   * If no preferences are found the defaultList is generated.
   */
  public List<Supporter> createSupporterList() {
    // load preferences

    String supportAddressesXml = preferences.get(SUPPORT_ADDRESSES, null);
    if (supportAddressesXml == null) {
      // use some hardcoded defaults
      return getDefaultXmlList();


    } else {
      byte[] array = supportAddressesXml.getBytes();
      ByteArrayInputStream inputStream = new ByteArrayInputStream(array);
      XMLDecoder decoder = new XMLDecoder(inputStream);
      try {
        List<Supporter> supporters = (List<Supporter>) decoder.readObject();
        // check if the decoded object is really of the type "Supporter" and not "SupportAddress"
        if (!(supporters.get(0) instanceof Supporter)) {
          throw new ClassCastException();
        }
        return supporters;
      } catch (ClassCastException e) {
        // FIXME somehow if invalid preferences are found it doesnt catch it.
        LOGGER.info("invalid preferences found, default list loaded");
        return getDefaultXmlList();
      }
    }
  }
}

