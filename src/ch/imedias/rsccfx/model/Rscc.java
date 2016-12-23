package ch.imedias.rsccfx.model;

public class Rscc {
  /**
   * Points to the "docker-build_p2p" folder inside resources, relative to the build path.
   * Important: Make sure to NOT include a / in the beginning or the end.
   */
  private static final String PATH_TO_RESOURCE_DOCKER = "resources/docker-build_p2p";

  private final SystemCommander systemCommander;

  public Rscc(SystemCommander systemCommander) {
    this.systemCommander = systemCommander;
  }

  /**
   * Requests a token from the key server.
   */
  public String requestTokenFromServer(int forwardingPort, String keyServerIp, int keyServerSshPort,
                                       int keyServerHttpPort, boolean isCompressionEnabled) {
    StringBuilder command = new StringBuilder();

    // First, setup the server with use.sh
    command.append("bash" + " " + PATH_TO_RESOURCE_DOCKER + "/");
    command.append("use.sh" + " ");
    command.append(keyServerIp + " ");
    command.append(keyServerHttpPort);
    systemCommander.executeTerminalCommand(command.toString());

    command = new StringBuilder();
    // Execute port_share.sh and get a key as output
    command.append("bash" + " " + PATH_TO_RESOURCE_DOCKER + "/");
    command.append("port_share.sh" + " ");
    command.append("--p2p_server=" + keyServerIp + " ");
    command.append("--p2p_port=" + keyServerSshPort + " ");
    command.append("--compress=" + (isCompressionEnabled ? "yes" : "no") + " ");
    command.append(forwardingPort);
    return systemCommander.executeTerminalCommand(command.toString());
  }


}
