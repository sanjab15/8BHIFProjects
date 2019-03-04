/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author Jan
 */
public enum Command {

    WHOAMI("#whoami"),
    WHOAMI2("#whoami <new Mailaddres>"),
    LIST("#list");
    private String line;

    private Command(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return line;
    }

}
