import java.io.*;
import java.net.*;
import java.util.Vector;

// This is the thread on the server that is spawned for each client.

// race conditions? what race conditions?
// deadlocks? what deadlocks?
// try catch? what try catch?

// 0: quit
// 1: connect
// 2: connect response (names list)
// 3: ready
// 4: ready response
// 5: score request
// 6: score response

public class ServerThread implements Runnable {
    
    private Socket connection;
    private InputStream clientInput;
    private OutputStream clientOutput;
    private SharedData sdata;
    private Vector<ServerThread> threads;

    public ServerThread(Socket connection, SharedData sdata, Vector<ServerThread> threads) {
        this.connection = connection;
        this.sdata = sdata;
        this.threads = threads;
        try {
            clientInput = connection.getInputStream();
            clientOutput = connection.getOutputStream();
        }
        catch (IOException ioex) {
            // pass
        }
    }

    @Override
    public void run() {
        boolean done = false;
        while (!done) {
            try {
                byte[] messageTypeB = new byte[4];
                clientInput.read(messageTypeB);
                //System.out.println("messageTypeB: " + new String(messageTypeB));
                int messageType = ByteHelper.bytesToInt(messageTypeB);
                // determine what to do based on message type
                System.out.println("server received message type: " + messageType);
                switch (messageType) {
                    case 1:
                        clientHello();
                        for (int  i = 0; i < threads.size(); i++) {
                            System.out.println(i);
                            threads.get(i).sendHelloResponse();
                        }
                        break;
                    case 3:
                        sdata.clearScore();
                        for (int i = 0; i < threads.size(); i++) {
                            threads.get(i).readyResponse();
                        }
                        break;
                    case 5:
                        score();
                        if (sdata.getScoreSize() == sdata.getPlayerNamesSize()) {
                            for (int i = 0; i < threads.size(); i++) {
                                threads.get(i).scoreResponse();
                            }
                        }
                        break;
                    case 9:
                        disconnect();
                        for (int i = 0; i < threads.size(); i++) {
                            threads.get(i).sendHelloResponse();
                        }
                        done = true;
                        break;
                }
                // may need to flush clientInput
            }
            catch (IOException ioex) {
                // maybe need to account for shutdown here?
                ioex.printStackTrace();
            }
        }
        // may need to remove connection here.
    }

    private void clientHello() {
        System.out.println("Receiving client hello.");
        // get up to 16 bytes of username
        byte[] namesizeb = new byte[4];
        try {
            clientInput.read(namesizeb);
            int namesize = ByteHelper.bytesToInt(namesizeb);
            byte[] username = new byte[namesize];
            clientInput.read(username);
            sdata.addPlayerName(new String(username));
            System.out.println("new username added.");
        }
        catch (IOException ioex) {
            System.out.println("error.");
        }
    }

    private void sendHelloResponse() {
        System.out.println("Sending hello response.");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int outmessage = 2;
        byte[] outmessageb = ByteHelper.intToBytes(outmessage);
        try {
            buffer.write(outmessageb);
            byte[] playerdata = sdata.getPlayerBytes();
            byte[] playerDataLength = ByteHelper.intToBytes(playerdata.length);
            buffer.write(playerDataLength);
            buffer.write(playerdata);
            clientOutput.write(buffer.toByteArray());
            clientOutput.flush();
        }
        catch (IOException ioex) {
            System.out.println("error sending hello response.");
        }
    }

    private void disconnect() {
        System.out.println("disconnecting...");
        byte[] namesizeb = new byte[4];
        try {
            clientInput.read(namesizeb);
            int namesize = ByteHelper.bytesToInt(namesizeb);
            byte[] username = new byte[namesize];
            clientInput.read(username);
            String user = new String(username);
            sdata.removePlayerName(user);
            threads.remove(this);
            connection.close();
        }
        catch (IOException ioex) {
            // pass
        }
    }

    private void readyResponse() {
        System.out.println("Sending ready response");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int outmessage = 4;
        byte[] outmessageb = ByteHelper.intToBytes(outmessage);
        try {
            buffer.write(outmessageb);
            long startTime = System.currentTimeMillis() + 10000;
            System.out.println("start time: " + startTime);
            byte[] ttime = ByteHelper.longToBytes(startTime);
            buffer.write(ttime);
            clientOutput.write(buffer.toByteArray());
            clientOutput.flush();
        }
        catch (IOException ioex) {
            // pass
        }
    }

    private void score() {
        System.out.println("Score Method..");
        try {
            byte[] scoreb = new byte[4];
            byte[] namesizeb = new byte[4];
            clientInput.read(scoreb);
            int score = ByteHelper.bytesToInt(scoreb);
            clientInput.read(namesizeb);
            int namesize = ByteHelper.bytesToInt(namesizeb);
            byte[] nameb = new byte[namesize];
            clientInput.read(nameb);

            String name = new String(nameb);
            sdata.addScore(name, score);
        }
        catch (IOException ioex) {
            // pass
        }
    }

    private void scoreResponse() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int outmessage = 6;
        byte[] outmessageb = ByteHelper.intToBytes(outmessage);
        try {
            buffer.write(outmessageb);
            byte[] dataSize = ByteHelper.intToBytes(sdata.getScoreSize());
            buffer.write(dataSize);
            byte[] scoredata = sdata.getScoreBytes();
            int scoredatalen = scoredata.length;
            buffer.write(ByteHelper.intToBytes(scoredatalen));
            buffer.write(scoredata);
            clientOutput.write(buffer.toByteArray());
            clientOutput.flush();
        }
        catch (IOException ioex) {
            // pass
        }
        System.out.println("Sent score response...");
    }
}