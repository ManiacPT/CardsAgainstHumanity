package org.academiadecodigo.bootcamp;

import com.sun.xml.internal.bind.v2.TODO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by codecadet on 01/03/17.
 */
public class Server {
    //TODO Quando se fizer o jar é suposto saber se quem inicia vai ser jogador ou servidor

    private ConcurrentHashMap<Socket, String> list;

    public ConcurrentHashMap<Socket, String> getList() {
        return list;
    }

    int counter = 1;
    boolean isCzar;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        Socket clientSocket;
        int portNumber = 9090;
        clientSocket = null;
        ServerSocket serverSocket = null;
        list = new ConcurrentHashMap();

//creates the sockets used and the thread clientHandler, puts the client sockets in a hashmap
        try {
            serverSocket = new ServerSocket(portNumber);

            while (list.size() < 5) {

                clientSocket = serverSocket.accept();
                Thread client = new Thread(new ClientHandler(clientSocket));
                client.start();
                list.put(clientSocket, "player" + counter);
                counter++;
                //System.out.println(list.keySet());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //kind of a garbage collector, removes the closed sockets from the hashmap
    public void checkConnection() {
        Iterator<Socket> iterator = list.keySet().iterator();
        synchronized (list) {
            while (iterator.hasNext()) {
                Socket socket = iterator.next();
                if (socket.isClosed()) {
                    list.remove(socket);
                }
            }
        }
    }

    //finds one specific player socket
    public Socket findPlayer(String stringValue) {
        synchronized (list) {
            Iterator<Socket> it = list.keySet().iterator();
            Socket socket = null;

            while (it.hasNext()) {
                Socket current = it.next();

                if (list.get(current).equals(stringValue.toLowerCase())) {
                    socket = current;
                    break;
                }
            }
            return socket;
        }
    }

    //sends a message to a specific player
    public void sendToPlayer(String string, String stringValue) {

        PrintWriter out = null;

        try {
            out = new PrintWriter(findPlayer(stringValue).getOutputStream(), true);
            out.println(string);
            System.out.println(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //sends message to all players
    public void chatSendToAll(Socket msgSocket, String string) {
        synchronized (list) {
            PrintWriter out = null;
            Iterator<Socket> it = list.keySet().iterator();
            while (it.hasNext()) {
                if (!msgSocket.isClosed()) {
                    try {
                        Socket tmp = it.next();
                        if (tmp != msgSocket)
                            out = new PrintWriter(tmp.getOutputStream(), true);
                            out.println(list.get(msgSocket) + ": " + string);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //sends message to all players
    public void sendToAll(String string) {
        synchronized (list) {
            PrintWriter out;
            Iterator<Socket> it = list.keySet().iterator();
            while (it.hasNext()) {
                Socket tmp = it.next();
                if (!tmp.isClosed()) {

                    try {
                        out = new PrintWriter(tmp.getOutputStream(), true);
                        out.println(string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    // closes a client socket
    public void closeClient(Socket msgSocket, String string) {
        if (string == null) {
            try {
                msgSocket.close();
                checkConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //implements methods used by chat commands
    public boolean parser(Socket msgSocket, String string) {

        boolean sendToAll = false;

        synchronized (list) {
            String[] parts = string.split(" ");

            switch (parts[0]) {

                case ("/exit"):
                    try {
                        msgSocket.close();
                        checkConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case "<@:":
                    if (parts.length > 2) {
                        String playerName = parts[1];
                        String message = "";
                        for (int i = 2; i < parts.length; i++) {
                            message += parts[i] + " ";
                        }
                        sendToPlayer(message, playerName);
                        sendToAll = true;
                    }
                    break;

                case "setAlias":
                    if (parts.length > 1) {
                        String playerAlias = "";
                        for (int i = 1; i < parts.length; i++) {
                            playerAlias += parts[i];
                        }

                        list.put(msgSocket, playerAlias.toLowerCase());
                    }
                    break;

                case "> white":
                    //TODO implementar carta e referencia
                    sendToAll = true;
                    break;

                case "> Czar":
                    //TODO enviar mensagem ao cliente q vai ser o czar
                    sendToAll = true;
                    break;

                case "> winner":
                    //TODO enviar mensagem a todos quem ganhou o round e a carta
                    break;

                case "> score":
                    //TODO enviar o score aos players
                    break;

                case "> black":
                    //TODO enviar uma carta preta
                    break;

                case "> submit":
                    //TODO white card send by players to the czar
                    break;

                case "> round":
                    //TODO number of round
                    break;

                case "> player":
                    //TODO name of player
                    break;

                case "> choice":
                    //TODO card that the cazr pickd as winner
                    break;

                case "> table":
                    //TODO the cards that the czar has to pick the winner
                    break;
                case "> hand":
                    //TODO the cards that the player has
                    break;
            }
        }
        return sendToAll;
    }

    //clientHandler thread
    public class ClientHandler implements Runnable {
        private Socket clientSocket;

        private ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        //run override AKA what the new thread does
        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String msg;

                while (true) {
                    msg = in.readLine();
                    closeClient(clientSocket, msg);
                    boolean sendToAll = parser(clientSocket, msg);
                    checkConnection();
                    if (!clientSocket.isClosed()) {
                        System.out.println(msg);
                        //receber os dados do client e decidir o q fazer com eles no metodo implement methods(acima)
                        if (sendToAll) {
                            continue;
                        }
                        chatSendToAll(clientSocket, msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




