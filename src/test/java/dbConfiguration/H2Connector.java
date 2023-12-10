package dbConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2Connector {
    public static Connection get() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        init(connection);
        return connection;
    }

    private static void init(Connection connection) throws IOException, SQLException {
        Statement statement = connection.createStatement();

        String initDB = new String(Files.readAllBytes(Paths.get("src/main/java/com/gmail/luchyk/viktoriia/ddl/init.sql")));
        statement.execute(initDB);

        String insertGames = new String(Files.readAllBytes(Paths.get("src/main/java/com/gmail/luchyk/viktoriia/ddl/insert_games.sql")));
        statement.execute(insertGames);

        statement.close();
    }
}