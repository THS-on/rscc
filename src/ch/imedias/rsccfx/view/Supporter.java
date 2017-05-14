package ch.imedias.rsccfx.view;

import java.beans.DefaultPersistenceDelegate;
import java.beans.PersistenceDelegate;
import java.io.Serializable;

/**
 * the address of a VNX supporter.
 */

public class Supporter implements Serializable {

  private String toString;
  private String description;
  private String address;
  private String port;
  private boolean encrypted;
  private boolean chargeable;

  /**
   * creates a new empty Supporter.
   */

  public Supporter() {
    this.description = "";
    this.address = "";
    this.port = "";
    this.encrypted = false;
    this.chargeable = false;
    this.toString = "+";
  }

  /**
   * creates a new Supporter.
   *
   * @param description the description.
   * @param address     the address.
   * @param port        the port.
   * @param encrypted   if the connection is encrypted.
   * @param chargeable  if the supporter is chargeable.
   */
  public Supporter(
      String description, String address, String port, boolean encrypted, boolean chargeable) {
    this.description = description;
    this.address = address;
    this.port = port;
    this.encrypted = encrypted;
    this.chargeable = chargeable;
    this.toString = description;
  }

  /**
   * returns the PersistenceDelegate.
   *
   * @return the PersistenceDelegate.
   */
  public static PersistenceDelegate getPersistenceDelegate() {
    return new DefaultPersistenceDelegate(
        new String[] {"description", "address", "port", "encrypted", "chargeable"});
  }

  /**
   * returns the description.
   * @return the description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * sets the description.
   * @param description the description to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * returns the address.
   * @return the address.
   */
  public String getAddress() {
    return address;
  }

  /**
   * sets the address.
   * @param address the address to set.
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * returns the port.
   * @return the port.
   */
  public String getPort() {
    return port;
  }

  /**
   * sets the port.
   * @param port the port to set.
   */
  public void setPort(String port) {
    this.port = port;
  }

  /**
   * returns <code>true</code>, if the connection is encrypted,
   * <code>false</code> otherwise.
   * @return <code>true</code>, if the connection is encrypted,
   * <code>false</code> otherwise.
   */
  public boolean isEncrypted() {
    return encrypted;
  }

  /**
   * sets the encrypted property of the Supporter.
   * @param encrypted if the Supporter is used for encrypted connections.
   */
  public void setEncrypted(boolean encrypted) {
    this.encrypted = encrypted;
  }

  /**
   * returns <code>true</code>m if the supporter is chargeable,
   * <code>false</code> otherwise.
   * @return <code>true</code>, if the connection is chargeable,
   * <code>false</code> otherwise.
   */

  public boolean isChargeable() {
    return chargeable;
  }

  /**
   * sets the chargeable property of the Supporter.
   * @param chargeable if the Supporter is chargeable.
   */
  public void setChargeable(boolean chargeable) {
    this.chargeable = chargeable;
  }

  /**
   * returns a + or the address depending on the constructor.
   * @return returns the string set in the constructor
   */
  @Override
  public String toString() {
    return toString;
  }
}