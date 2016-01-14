import java.io.*;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.*;

public class SharedData {
    
    private Vector<String> playernames = null;
    private ConcurrentHashMap<String, Integer> scores = null;

    public SharedData(SharedData data) {
        this.playernames = data.getPlayerNames();
        this.scores = data.getScores();
    }

    public SharedData(Vector<String> names, ConcurrentHashMap<String, Integer> scores) {
        this.playernames = names;
        this.scores = scores;
    }

    public Vector<String> getPlayerNames() {
        return this.playernames;
    }

    public ConcurrentHashMap<String, Integer> getScores() {
        return this.scores;
    }

    public int getScoreSize() {
        return scores.size();
    }

    public int getPlayerNamesSize() {
        return playernames.size();
    }

    public synchronized void addScore(String name, int score) {
        scores.put(name, score);
    }

    public synchronized void clearScore() {
        scores.clear();
    }

    public synchronized void addPlayerName(String name) {
        playernames.add(name);
    }

    public synchronized void removePlayerName(String name) {
        playernames.remove(name);
    }

    public byte[] getPlayerBytes() {
        //System.out.println(timesCalled);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (String s : getPlayerNames()) {
            byte[] tmp = s.getBytes();
            try {
                buffer.write(tmp);
                byte rs = 0x1E;
                buffer.write(rs);
            }
            catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }
        return buffer.toByteArray();
    }

    public byte[] getScoreBytes() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            try {
                buffer.write(entry.getKey().getBytes());
                byte rs = 0x1E;
                buffer.write(rs);
                int tscore = entry.getValue().intValue();
                System.out.println("tscore: " + tscore);
                byte[] scoreb = ByteHelper.intToBytes(tscore);
                buffer.write(scoreb);
                buffer.write(rs);
            }
            catch (IOException ioex) {
                // pass
            }
        }
        return buffer.toByteArray();
    }
}