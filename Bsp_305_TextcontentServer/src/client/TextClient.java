/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class TextClient {

    private final int PORT_NR = 4001;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;
    private JTextArea logArea;
    private JLabel users;

    public TextClient(JTextArea logArea, JLabel users) {
        this.logArea = logArea;
        this.users = users;
    }

    public void connect() throws UnknownHostException, IOException {
        InetAddress ip = InetAddress.getLocalHost();
        socket = new Socket(ip, PORT_NR);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        UpdateInBackground thread = new UpdateInBackground();
        thread.start();
    }

    public void update(String text) throws IOException {
        output.writeObject(text);
        output.flush();
    }

    public void disconnect() throws IOException {
        update(null);
        log("");
        output.close();
        input.close();
        if (socket != null) {
            socket.close();
        }
    }

    private void log(String text) {
        logArea.setText(text);
    }

    private void updateUsers(int workingUsers) {
        users.setText("" + workingUsers);
    }

    class UpdateInBackground extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Object inputObj = input.readObject();
                    if (inputObj instanceof String) {
                        String inputStr = (String) inputObj;
                        log(inputStr);
                    }
                    if (inputObj instanceof Integer) {
                        int workingUsers = (Integer) inputObj;
                        updateUsers(workingUsers);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
}
