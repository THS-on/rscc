package ch.imedias.rsccfx.view;

import ch.imedias.rsccfx.RsccApp;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

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

  /**
   * create default supportter list from file
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
        return supporters;
      } catch (ClassCastException e) {
        LOGGER.info("invalid preferences found, default list loaded");
        return getDefaultXmlList();
      }
    }
  }
}

