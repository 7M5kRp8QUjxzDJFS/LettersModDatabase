package server;

import java.sql.SQLException;

/**
 * The Main class, which connects the database and spark routes.
 */
public final class Main {

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  @SuppressWarnings("checkstyle:AvoidNestedBlocks")
  private void run() throws SQLException, ClassNotFoundException {
    //  - postgresql filepath
    //LettersDatabase db = new LettersDatabase("MailDatabase.sqlite3");
    LettersDatabase db = new LettersDatabase("//localhost:5432/addresses");
    System.out.println("Database connected!");
  }
}