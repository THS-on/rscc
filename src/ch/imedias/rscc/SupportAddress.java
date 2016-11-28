package ch.imedias.rscc;

import java.beans.DefaultPersistenceDelegate;
import java.beans.PersistenceDelegate;
import java.io.Serializable;

/**
 * a VNC support address
 *
 * @author Ronny Standtke <ronny.standtke@fhnw.ch>
 */
public class SupportAddress implements Serializable {

    private String description;
    private String address;
    private boolean encrypted;

    /**
     * creates a new SupportAddress
     *
     * @param description the description
     * @param address the address
     * @param encrypted if the connection is encrypted
     */
    public SupportAddress(
            String description, String address, boolean encrypted) {
        this.description = description;
        this.address = address;
        this.encrypted = encrypted;
    }

    /**
     * returns the description
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the description
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * returns the address
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * sets the address
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * returns
     * <code>true</code>, if the connection is encrypted,
     * <code>false</code> otherwise
     *
     * @return
     * <code>true</code>, if the connection is encrypted,
     * <code>false</code> otherwise
     */
    public boolean isEncrypted() {
        return encrypted;
    }

    /**
     * sets the encrypted property of the SupportAddress
     *
     * @param encrypted if the SupportAddress is used for encrypted connections
     */
    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    /**
     * returns the PersistenceDelegate
     *
     * @return the PersistenceDelegate
     */
    public static PersistenceDelegate getPersistenceDelegate() {
        return new DefaultPersistenceDelegate(
                new String[]{"description", "address", "encrypted"});
    }
}
