
import gui.TextClientGUI;
import gui.TextServerGUI;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jan
 */
public class Start {

    public static void main(String[] args) {

        TextServerGUI server = new TextServerGUI();
        server.setVisible(true);
        TextClientGUI clientGUI;

        for (int i = 0; i < 3; i++) {
            clientGUI = new TextClientGUI();
            clientGUI.setVisible(true);
        }

    }
}
