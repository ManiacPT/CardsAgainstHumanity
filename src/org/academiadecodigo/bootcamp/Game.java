package org.academiadecodigo.bootcamp;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by codecadet on 02/03/17.
 */
public class Game {
    private WhiteDeck whiteDeck;
    private BlackDeck czarCard;
    private ConcurrentHashMap<String, String> whiteDeckList;
    private ConcurrentHashMap<String, String> blackDeckList;
    private int[] sockets = new int[5];
    private Player player;

    public void setDeckLists(ConcurrentHashMap<String, String> whiteDeckList, ConcurrentHashMap<String, String> blackDeckList) {
        this.whiteDeckList = whiteDeckList;
        this.blackDeckList = blackDeckList;
        whiteDeck = new WhiteDeck();
        czarCard = new BlackDeck();
    }

    public void start(int winsAt) {
<<<<<<< HEAD

        /*while(player.getScore != winsAt){
=======
        while(player.getScore() != winsAt){
>>>>>>> 0e5b0d5ca34b4ab2cc627c4956809ba2f180e997
            startRound();
        }*/
    }

    private void startRound() {
        giveCzar();
        whiteDeck.giveCard(1);
    }

    private void giveCzar() {

    }

    private void handPlayer() {

    }


    /*
    -ver se estão 5 jogadores -> metodo do server
    -o servidor tem de saber qual o cliente que é o czar.

    começar o jogo (metodo start):
    -dar cartas aos jogadores,
    -começar ronda:
    ----atribuir um czar,
    ----escolher uma carta preta do deck e enviar para todos,
    ----bloquear a escolha de cartas do czar,
    ----esperar que todos os jogadores escolham uma carta,
    ----ver as escolhas de todos os jogadores
    ----enviar as escolhas de cada jogador para o czar,
    ----esperar que o czar faça uma escolha,
    ----atribuir um ponto ao dono da carta escolhida,
    ----mostrar um score a todos os jogadores,
    ----atribuir uma nova carta a cada jogador,
    ----atribuir o novo czar.
    ----fim do round.
    -novo round até alguem ter um score de x,
    -mostrar a todos quem é o winner.
     */


}
