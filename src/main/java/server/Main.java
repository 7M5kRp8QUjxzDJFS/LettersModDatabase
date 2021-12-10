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
 */
public final class Main {

  private static int DEFAULT_PORT = Integer.parseInt(System.getenv("PORT"));

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
    } else {
      db = new LettersDatabase("jdbc:postgresql://localhost:5432/addresses");
    }
    System.out.println("Database connected!");

    // TODO: parse commands/handle spark routes
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
}