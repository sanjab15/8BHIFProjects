package client;

import beans.Command;
import beans.Mail;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

    private List<Command> commands;
    private String mailAddress = "sanjab@htlkaindorf.at";
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private final int PORT_NR = 8430;

    public Client() {
        commands = new ArrayList();
        fillCommands();
        this.connect();
    }

    private void fillCommands() {
        commands.add(Command.WHOAMI);
        commands.add(Command.WHOAMI2);
        commands.add(Command.LIST);
    }

    private void connect() {
        try {
            socket = new Socket(InetAddress.getLocalHost(), PORT_NR);

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            System.out.println("AVAILABLE COMMANDS: ");
            for (Command command : commands) {
                System.out.println(command.toString());
            }
            output.writeObject(mailAddress);
            output.flush();

            Thread receiveThread = new Thread(new ReceiveMail());
            receiveThread.start();

            Thread sendThread = new Thread(new SendMail());
            sendThread.start();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void log(String line) {
        System.out.println(line);
    }

    class SendMail implements Runnable {

        Scanner scanner = new Scanner(System.in);

        @Override
        public void run() {
            while (true) {
                String input = scanner.nextLine();
                if (isCommand(input)) {
                    String line = "";
                    if (input.toLowerCase().startsWith("#whoami")) {
                        line = input.replace("#whoami", "").trim();
                        input = input.substring(0, 7);
                    }
                    switch (input) {
                        case "#whoami":
                            if (!line.isEmpty()) {
                                if (line.equals(mailAddress)) {
                                    System.out.println("Du verwendest bereits diese Mail!");;
                                } else {
                                    whoAmI(line);
                                }
                            } else {
                                log("WhoAmI: " + whoAmI());
                            }
                            break;
                        case "#send":
                            sendMail();
                            break;
                        case "#list":
                            list();
                            break;

                        default:
                            log("This is not a valid command");
                            break;
                    }
                } else {
                    sendObject(input);
                }
            }
        }

        private void list() {
            sendObject("#list" + mailAddress);
        }

        private boolean isCommand(String line) {
            return line.startsWith("#");
        }

        private void whoAmI(String newAddress) {
            sendObject("#whoami" + newAddress);
        }

        private String whoAmI() {
            return mailAddress;
        }

//        private void sendString(String text) {
//            try {
//                output.writeObject(text);
//                output.flush();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//
//        }
        private void sendObject(Object object) {
            try {
                output.writeObject(object);
                output.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void sendMail() {
            System.out.println("To: ");
            String receiver = scanner.nextLine();
            System.out.println("Subject: ");
            String subject = scanner.nextLine();;
            System.out.println("Message: ");
            String message = scanner.nextLine();

            Mail mail = new Mail(mailAddress, receiver, message, subject);
            sendObject(mail);
        }

    }

    class ReceiveMail implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Object receive = input.readObject();
                    if (receive instanceof Mail) {
                        Mail mail = (Mail) receive;
                        System.out.println("From " + mail.getSender());
                    } else if (receive instanceof String) {
                        String command = (String) receive;
                        if (((String) receive).startsWith("#")) {
                            String parameter = "";
                            if (command.contains("/")) {
                                parameter = command.substring(command.indexOf("/") + 1, command.length());
                                command = command.substring(0, command.indexOf("/"));
                            }
                            switch (command) {
                                case "#whoami_unsuccessfulChange":
                                    log("Die E-Mailaddresse konnte nicht geändert werden, da diese bereits vorhanden ist");
                                    break;
                                case "#whoami_successfulChange":
                                    mailAddress = parameter;
                                    log("new E-Mailaddress is " + parameter);
                                    break;
                                default:
                                    log("OOPS ein Fehler ist aufgetreten GAG");
                                    break;
                            }
                        } else {
                            System.out.println("ELSE");
                            log((String) receive);
                        }
                    } else if (receive instanceof ArrayList) {
                        ArrayList<Mail> mails = (ArrayList<Mail>) receive;
                        listMails(mails);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void listMails(ArrayList<Mail> mails) {
            log("METHOD LISTMAILS");
            if (!mails.isEmpty()) {
                for (Mail mail : mails) {
                    String line = mail.getDate() + " " + mail.getSender();
                    log(line);
                }
            } else {
                log("Noch keine Mails empfangen");
            }

        }

    }

    public static void main(String[] args) {
        Client client = new Client();
    }

}
