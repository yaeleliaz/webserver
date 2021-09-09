import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Yael
 */
public class WebServer {
  private final ExecutorService executorService;

  public WebServer(int threadCount) {
    executorService = Executors.newFixedThreadPool(threadCount);
  }

  public static void main(String[] args) {
    WebServer server = new WebServer(10);
    server.handleRequests();
  }

  private void handleRequests() {
    try {
      ServerSocket socket = new ServerSocket(8080);
      System.out.println("Server started!!!!");

      while (true) {
        Socket client = socket.accept();
        System.out.println("Got new message" + client.toString());
        // Submit the request job to a thread in the executor service
        HandleRequestJob job = new HandleRequestJob(client);
        executorService.submit(job);
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      executorService.shutdown();
    }
  }
}
//}


        /*InputStreamReader message = new InputStreamReader(client.getInputStream());
        BufferedReader br = new BufferedReader(message);

        StringBuilder request = new StringBuilder();

        String line = br.readLine();
        while (!(line.equals(""))) {
          request.append(line + "\r\n");
          line = br.readLine();

        }
       *//* System.out.println("REQURST:::::::::::::");
        System.out.println(request);
        OutputStream response = client.getOutputStream();
        response.write(("HTTP:// 200 OK\r\n").getBytes());
        response.write(("\r\n").getBytes());
        response.write(("WE HAVE THE CODE!!").getBytes());
        response.flush();*//*


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
      }

    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }*/


// http://localhost:8080/?query=INSERT INTO emp (id,name,age)VALUES (7, 5555, 30)
// http://localhost:8080/?query=SELECT * from emp





