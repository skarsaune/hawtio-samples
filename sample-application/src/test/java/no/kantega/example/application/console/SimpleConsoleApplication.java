package no.kantega.example.application.console;

import java.io.IOException;
/**
 * I am a simple application that simply hangs around in order to demonstrate
 * a jolokia agent
 * @author marska
 *
 */
public class SimpleConsoleApplication {

    public static void main(String[] args) throws IOException {
        System.out.println("Trivial console application that hangs around ....");
        System.out.println("Press any key to exit");
        System.in.read();
    }

}
