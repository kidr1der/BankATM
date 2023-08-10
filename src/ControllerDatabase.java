import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class ControllerDatabase {
    private final String pathDataBase;

    public ControllerDatabase(String pathDataBase) {
        this.pathDataBase = pathDataBase;
    }

    public Map<String, String[]> getClientDatabase() {
        Map<String, String[]> map = new HashMap<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(pathDataBase))){
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()){
                    String[] parts = line.split(" ");
                    String key = parts[0];
                    String[] value = {parts[1], parts[2]};
                    map.put(key, value);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("БАНКОМАТ ВРЕМЕННО НЕ РАБОТАЕТ! НЕТ СВЯЗИ С БАЗОЙ ДАННЫХ");;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    public void writeToDatabase(Map<String, String[]> clientBase) {
        try (FileWriter writer = new FileWriter(pathDataBase)) {
            for (Map.Entry<String, String[]> entry : clientBase.entrySet()) {
                String key = entry.getKey();
                String[] value = entry.getValue();
                String client = String.join(" ", key, value[0], value[1]);
                writer.write(client + System.lineSeparator());
            }

        } catch (FileNotFoundException e) {
            System.out.println("БАНКОМАТ ВРЕМЕННО НЕ РАБОТАЕТ! НЕТ СВЯЗИ С БАЗОЙ ДАННЫХ");;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
