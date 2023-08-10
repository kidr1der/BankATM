import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class Blocked {
    private final String fileBlockedCard;

    public Blocked(String fileBlockedCard) {
        this.fileBlockedCard = fileBlockedCard;
    }

    public void addBlocked(long currentTime, String card) {

        OutputStream os = null;
        try {
            os = new FileOutputStream(new File(fileBlockedCard), true);
            String client = String.join(" ", card, String.valueOf(currentTime));
            os.write(client.getBytes(), 0, client.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean checkBlocked(long currentTime, String card) {
        Map<String, Long> blockedCard = new HashMap<>();
        String line;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileBlockedCard));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] parts = line.split(" ");
                    String key = parts[0];
                    Long value = Long.valueOf(parts[1]);
                    blockedCard.put(key, value);
                }
            }
            reader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (blockedCard.get(card) != null) {
            if (currentTime - blockedCard.get(card) >= 86400000) {
                blockedCard.remove(card);
                rewriteBlocked(blockedCard);
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public void rewriteBlocked(Map<String, Long> clientBaseBlocked) {
        try (FileWriter writer = new FileWriter(fileBlockedCard)) {
            for (Map.Entry<String, Long> entry : clientBaseBlocked.entrySet()) {
                String key = entry.getKey();
                String value = String.valueOf(entry.getValue());
                String client = String.join(" ", key, value);
                writer.write(client + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
