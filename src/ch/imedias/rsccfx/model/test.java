package ch.imedias.rsccfx.model;

/**
 * Created by pwg on 15.05.17.
 */
public class test {

  public static void main(String[] args) {


    VncServerHandler handler = new VncServerHandler(null);
 handler.config(false, null, null);
 handler.startVncServer();
  }

}
