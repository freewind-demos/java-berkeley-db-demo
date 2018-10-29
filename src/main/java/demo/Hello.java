package demo;

import com.sleepycat.je.*;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class Hello {

    public static void main(String[] args) throws Exception {

        Environment env = new Environment(new File("./db"), createEnvConfig());
        Database db = env.openDatabase(null, "testdb", createDbConfig());

        save(db, "key1", "value1");

        String value = read(db, "key1");
        System.out.println("value: " + value);

        delete(db, "key1");

        String result = read(db, "key1");
        System.out.println(result);

    }

    private static void delete(Database db, String key) throws DatabaseException {
        db.delete(null, toEntry(key));
    }

    private static String read(Database db, String key) throws DatabaseException {
        DatabaseEntry value = new DatabaseEntry();
        db.get(null, toEntry(key), value, LockMode.DEFAULT);
        byte[] result = value.getData();
        if (result == null) {
            return null;
        } else {
            return new String(result, StandardCharsets.UTF_8);
        }
    }

    private static void save(Database db, String key, String value) throws DatabaseException {
        OperationStatus status = db.put(null, toEntry(key), toEntry(value));
        System.out.println(status);
    }

    private static DatabaseEntry toEntry(String key1) {
        return new DatabaseEntry(key1.getBytes(StandardCharsets.UTF_8));
    }

    private static DatabaseConfig createDbConfig() {
        return new DatabaseConfig() {{
            this.setAllowCreate(true);
            this.setReadOnly(false);
            this.setTransactional(true);
        }};
    }

    private static EnvironmentConfig createEnvConfig() {
        return new EnvironmentConfig() {{
            this.setAllowCreate(true);
            this.setReadOnly(false);
            this.setTransactional(true);
        }};
    }

}
