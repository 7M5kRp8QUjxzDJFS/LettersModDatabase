package server;

/**
 * CommandHandler interface meant for standardizing output for the input and
 * output of commands, currently including stars, neighbors, and radius.
 */
public interface CommandHandler {

  /**
   * Executes the necessary actions of any class that implements CommandHandler.
   */
  void handle();

}
