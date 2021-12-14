package server;

import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import javax.sound.midi.SysexMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

/**
 * The Main class, which connects the database and spark routes.
 * Code modeled off of CS32 project Main classes (project 2 integration).
 */
public final class Main {

  private static final int DEFAULT_PORT = setDefaultPort();

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
    LettersDatabase db = null;
    if (System.getenv("JDBC_DATABASE_URL") != null) {
      db = new LettersDatabase(System.getenv("JDBC_DATABASE_URL"));
      System.out.println("Heroku Postgresql database connected!");
    } else {
      // Don't think this works if you don't have postgres, so change it to connect to the
      // local sqlite3 DB instead if necessary.
      db = new LettersDatabase("jdbc:postgresql://localhost:5432/addresses");
      System.out.println("Local database connected!");
    }

    ParseCommands replit = new ParseCommands();

    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);

    OptionSet options = parser.parse(args);

    runSparkServer((int) options.valueOf("port"));

    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(System.in))) {
      String input;
      while ((input = br.readLine()) != null) {
        input = input.trim();
        ParseCommands.setInputLine(input);
        String command = ParseCommands.getArguments().get(0);
        replit.handleArgs(command);
      }
    } catch (Exception e) {
      System.out.println("ERROR: Invalid input for REPL");
      ParseCommands.setOutputString("ERROR: Invalid input for REPL");
    }
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

    // TODO: Update temporary code to actually interact with DB.
    // Setup Spark Routes
    Spark.post("/changeaddr", new ChangeAddr());
    Spark.post("/initaddr", new InitAddr());
    Spark.post("/getmail", new GetMail());
    Spark.post("/send", new Send());
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  /**
   * Helper method that dynamically sets the default port based on
   * whether the code is being run locally or on heroku.
   * @return - The default port as a int
   */
  private static int setDefaultPort() {
    if (System.getenv("PORT") != null) {
      System.out.println("Port set to: " + System.getenv("PORT"));
      return Integer.parseInt(System.getenv("PORT"));
    } else {
      System.out.println("Port set to: 4567");
      return 4567;
    }
  }
}