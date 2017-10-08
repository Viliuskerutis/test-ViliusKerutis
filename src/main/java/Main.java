import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        Spark.port(8080);
        DataClass dataBase = new DataClass();
        boolean a = arYra("labas, mano vardas Ciklopas");

        Spark.get("/image", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return loadImage("mini.jpg", request, response);
            }
        });

        Spark.get("/home", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return loadHtml("home.html");
            }
        });

        Spark.get("/adduser", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return loadHtml("adduser.html");
            }
        });

        Spark.post("/adduser", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String vardas = request.queryParams("vardas");
                String pavarde = request.queryParams("pavarde");
                String email = request.queryParams("email");
                String adresas = request.queryParams("adresas");
                dataBase.insert(vardas, pavarde, email, adresas);
                return loadHtml("home.html");
            }
        });

        Spark.get("/getuser", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return loadHtml("getuser.html");
            }
        });

        Spark.post("/getuser", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String vardas = request.queryParams("vardas");
                String pavarde = request.queryParams("pavarde");
                String email = request.queryParams("email");
                String rezultatai = dataBase.select(vardas, pavarde, email);
                String html = loadHtml("postusers.html");
                html = html.replace("{USERS}", rezultatai);
                return html;
            }
        });
    }

    public static String loadHtml(String filePath) {
        try {
            URI path = Main.class.getClassLoader().getResource(filePath).toURI();
            String html = new String(Files.readAllBytes(Paths.get(path)), Charset.forName("UTF-8"));
            return html;
        } catch (URISyntaxException e) {
            System.out.println("Nepavyko užkrauti failo " + e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println("Nepavyko užkrauti failo " + e.getLocalizedMessage());
        }
        return null;
    }

    public static boolean arYra(String tekstas){
        return tekstas.contains("c");
    }

    public static HttpServletResponse loadImage(String filePath, Request request, Response response) {

        byte[] data = null;
        try {
            URI path = Main.class.getClassLoader().getResource(filePath).toURI();
            data = Files.readAllBytes(Paths.get(path));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        HttpServletResponse raw = response.raw();
        response.header("Content-Disposition", "attachment; filename=image.jpg");
        response.type("application/force-download");
        try {
            raw.getOutputStream().write(data);
            raw.getOutputStream().flush();
            raw.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return raw;
    }
}
