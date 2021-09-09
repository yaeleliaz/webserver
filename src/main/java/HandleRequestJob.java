import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.Callable;

/**
 * @author Yael
 */

public class HandleRequestJob implements Callable {
  private final Socket client;

  public HandleRequestJob(Socket client) {
    this.client = client;
  }

  public Object call() throws Exception {
    InputStreamReader message = new InputStreamReader(client.getInputStream());
    BufferedReader br = new BufferedReader(message);

    StringBuilder request = new StringBuilder();

    String line = br.readLine();
    while (!(line.equals(""))) {
      request.append(line + "\r\n");
      line = br.readLine();

    }
       /* System.out.println("REQURST:::::::::::::");
        System.out.println(request);
        OutputStream response = client.getOutputStream();
        response.write(("HTTP:// 200 OK\r\n").getBytes());
        response.write(("\r\n").getBytes());
        response.write(("WE HAVE THE CODE!!").getBytes());
        response.flush();*/


    System.out.println("REQUEST:::::::::::::");
    System.out.println(request);
    OutputStream response = client.getOutputStream();

    String query = request.toString().split("\\?")[1].split(" ")[0].split("=")[1];
    query = query.replaceAll("\\%20", " ");

    System.out.println("MY QUERY:::::   " + query);
    response.write(("HTTP:// 200 OK\r\n").getBytes());
    response.write(("\r\n").getBytes());
    String responseString = connectToMySql(query);

    response.write(responseString.getBytes());
    response.flush();

    client.close();

    return null;
  }

  private static String connectToMySql(String query) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection(
          "jdbc:mysql://localhost:3306/mynewdatabase", "root", "");
      Statement stmt = con.createStatement();

      String result = "";
      if (query.toLowerCase().startsWith("select")) {
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
          // System.out.println("THE QUERY RESULT::: " + rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
          result += "\n" + rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3);
        }
      }

      else {
        stmt.executeUpdate(query);
        return "query was executed";
      }

      con.close();
      return result;
    }
    catch (Exception e) {
      System.out.println(e);
    }

    return "ERROR";
  }
}
