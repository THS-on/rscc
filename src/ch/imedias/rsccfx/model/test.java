package ch.imedias.rsccfx.model;

import ch.imedias.rsccfx.model.util.KeyUtil;

/**
 * Created by pwg on 15.05.17.
 */
public class test {

  public static void main(String[] args) {
    KeyUtil k = new KeyUtil();
    SystemCommander sc = new SystemCommander();

    Rscc model = new Rscc(sc, k);

    VncServerHandler handler = new VncServerHandler(model);
    System.out.println(handler.startVncServerReverse("127.0.0.1", 5500));

    System.out.println(model.isIsVncSessionRunning());
  }
}
