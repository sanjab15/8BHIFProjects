package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTextArea;

public class TextServer {

    private final int PORT_NR = 4001;
    private Map<String, List<ObjectOutputStream>> map_workingUsers = new HashMap();
    private Map<String, String> map_content = new HashMap();
    private JTextArea logArea;

    public TextServer(JTextArea logArea) throws IOException {
        this.logArea = logArea;
        this.startServer();
    }

    private void startServer() throws IOException {
        System.out.println("StartServer");
        ServerCommunication serverThread = new ServerCommunication();
        serverThread.start();
        log("Servlog: up and running");
    }

    private void log(String text) {
        logArea.append(text + "\n");
    }

    class ServerCommunication extends Thread {

        ServerSocket server;

        public ServerCommunication() throws IOException {
            server = new ServerSocket(PORT_NR);
            //server.setSoTimeout(500);
        }

        @Override

        public void run() {
            while (true) {
                try {
                    Socket socket = server.accept();
                    log("Serverlog: " + socket.getRemoteSocketAddress() + " connected");
                    Thread clientThread = new Thread(new ClientCommunication(socket));
                    clientThread.start();
                } catch (IOException ex) {

                }
            }
        }

    }

    class ClientCommunication implements Runnable {

        private Socket socket;
        private ObjectOutputStream output;
        private ObjectInputStream input;
        private String workingOnFile;

        public ClientCommunication(Socket socket) throws IOException {
            this.socket = socket;
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            workingOnFile = "";
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Object inputObj = input.readObject();
                    String inputStr = (String) inputObj;
                    if (inputStr == null) {
                        closeStreams();
                        map_workingUsers.get(workingOnFile).remove(output);
                        sendObjectToAll(map_workingUsers.get(workingOnFile).size());
                        workingOnFile = "";
                        break;
                    }
                    if (inputStr.startsWith("file/")) {
                        String fileName = inputStr.replace("file/", "");
                        workingOnFile = fileName;
                        if (!map_workingUsers.containsKey(fileName)) {
                            List<ObjectOutputStream> outputStreams = new ArrayList();
                            outputStreams.add(output);
                            map_workingUsers.put(fileName, outputStreams);
                        } else {
                            ArrayList<ObjectOutputStream> outputs = (ArrayList<ObjectOutputStream>) map_workingUsers.get(fileName);
                            outputs.add(output);
                        }
                        log("Serverlog: " + socket.getRemoteSocketAddress() + " started working on file " + workingOnFile);
                        sendObjectToAll(map_workingUsers.get(fileName).size());
                        if (!map_content.containsKey(fileName)) {
                            map_content.put(fileName, "");
                        } else {
                            sendObject(map_content.get(fileName));
                        }
                    } else {
                        map_content.replace(workingOnFile, inputStr);
                        for (ObjectOutputStream out : map_workingUsers.get(workingOnFile)) {
                            if (out != output) {
                                out.writeObject(inputStr);
                                out.flush();
                            }
                        }

                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void closeStreams() throws IOException {
            output.close();
            input.close();
        }

        private void sendObjectToAll(Object obj) throws IOException {
            for (ObjectOutputStream outputStr : map_workingUsers.get(workingOnFile)) {
                outputStr.writeObject(obj);
                outputStr.flush();
            }
        }

        private void sendObject(Object obj) throws IOException {
            output.writeObject(obj);
            output.flush();
        }

    }
}
