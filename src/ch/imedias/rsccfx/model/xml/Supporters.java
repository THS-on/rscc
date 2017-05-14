package ch.imedias.rsccfx.model.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by François Martin on 14.05.2017.
 */
@XmlRootElement(name = "supporters")
@XmlAccessorType (XmlAccessType.FIELD)
public class Supporters {

  @XmlElement(name = "supporter")
  private List<Supporter> supporters = null;

  public List<Supporter> getSupporters() {
    return supporters;
  }

  public void setSupporters(List<Supporter> supporters) {
    this.supporters = supporters;
  }
}
