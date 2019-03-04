package server;

import beans.Mail;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MailServer {

    private final int PORT_NR;
    private Map<String, ArrayList<Mail>> mailList = new HashMap<>();

    public MailServer() {
        this.PORT_NR = 8430;
    }

    public MailServer(int portNumber) {
        this.PORT_NR = portNumber;
    }

    private void log(String message) {
        System.out.println("ServerLog: " + message);
    }

    private void startServer() throws IOException {
        ServerThread serverThread = new ServerThread();
        serverThread.start();
        log("startServer()");
    }

    private void stopServer() {
        log("stopServer()");
    }

    class ServerThread extends Thread {

        private ServerSocket serverSocket;

        public ServerThread() throws IOException {
            serverSocket = new ServerSocket(PORT_NR);
            log("server is running ... ");
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    Thread thread = new Thread(new ClientThread(socket));
                    thread.start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                log("new client requested ...");
            }
        }
    }

    class ClientThread implements Runnable {

        private Socket socket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private String mailAddress;

        public ClientThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                input = new ObjectInputStream(socket.getInputStream());
                output = new ObjectOutputStream(socket.getOutputStream());
                register();
                Object received;
                while (!Thread.interrupted()) {
                    received = input.readObject();
                    if (received instanceof Mail) {
                        Mail mail = (Mail) received;
                        log("mail received for " + mail.getReceiver());
                        for (String receiver : mailList.keySet()) {
                            if (receiver.equals(mail.getReceiver())) {
                                log("mail added" + receiver);
                                mailList.get(receiver).add(mail);
                            }
                        }
                    }
                    if (received instanceof String) {
                        String line = (String) received;
                        log("instanceofString");
                        if (line.startsWith("#whoami")) {
                            changeMail(line);
                        }
                        if (line.startsWith("#list")) {
                            String receiver = line.replace("#list", "").trim();
                            log("listMails for" + receiver);
                            if (receiver.equals(mailAddress)) {
                                listMails(line);
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        private void listMails(String received) {
            log("list mails for " + mailAddress);
            log("SIZE + " + mailAddress + "  " + mailList.get(mailAddress).size());
            sendObject(mailList.get(mailAddress));
        }

        private void changeMail(String received) throws IOException, ClassNotFoundException {
            log("E-Mail address change request from " + socket.getLocalAddress());
            String newMailAddress = received.replace("#whoami", "");
            if (!mailList.containsKey(newMailAddress)) {
                mailList.remove(mailAddress);
                mailList.put(newMailAddress, new ArrayList<>());
                mailAddress = newMailAddress;
                log("E-Mail changed succesfully changed");
                sendObject(new String("#whoami_successfulChange/" + newMailAddress));
            } else {
                log("E-Mail changed could not be changed");
                sendObject(new String("#whoami_unsuccessfulChange"));
            }
        }

        private void sendObject(Object object) {
            try {
                output.writeObject(object);
                output.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void register() throws IOException, ClassNotFoundException {
            Object received = null;
            int counter = 0;
            do {
                if (counter != 0) {
                    if (counter == 1) {
                        sendObject("Bitte geben Sie eine neue E-Mailadresse ein da die Standardadresse eingestellt ist");
                    } else {
                        sendObject("E-Mailadresse ist bereits vorhanden");
                    }
                }
                received = input.readObject();
                mailAddress = (String) received;
                counter++;
            } while (mailList.containsKey(mailAddress));
            mailList.put(mailAddress, new ArrayList<Mail>());
            log("registration: " + mailAddress);
        }
    }

    public static void main(String[] args) {
        MailServer server = new MailServer();
        try {
            server.startServer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
