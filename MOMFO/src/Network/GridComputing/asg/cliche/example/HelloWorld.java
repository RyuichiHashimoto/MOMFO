/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Network.GridComputing.asg.cliche.example;

import java.io.IOException;

import Network.GridComputing.asg.cliche.Command;
import Network.GridComputing.asg.cliche.ShellFactory;

/**
 * 'Hello World!' example.
 *
 * @author ASG
 */
public class HelloWorld {

    @Command
    public int add(int a, int b) {
        return a + b;
    }

    @Command
    public String echo(String s) {
        return s;
    }

    public static void main(String[] params) throws IOException {
        ShellFactory.createConsoleShell("hello", null, new HelloWorld())
                .commandLoop();
    }
}
