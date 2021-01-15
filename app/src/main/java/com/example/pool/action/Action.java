package com.example.pool.action;

import com.example.pool.Utils;
import com.example.pool.balls.Ball;
import com.example.pool.gui.CurrentPlayerScreen;
import com.example.pool.players.Player;
import com.example.pool.room.DBViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Action {
    protected Player currentPlayer;
    protected Ball currentBall;
    protected Ball kissedBall;
    protected List<Ball> pottedBalls;
    private static final String TAG = "Action";

    public Action(Player currentPlayer, Ball currentBall, Ball kissedBall, List<Ball> pottedBalls) {
        this.currentPlayer = currentPlayer;
        this.kissedBall = kissedBall;
        this.pottedBalls = pottedBalls;
        this.currentBall = currentBall;
    }

    public Action() {

    }

    public Action(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Ball getCurrentBall() {
        return currentBall;
    }

    public Ball getKissedBall() {
        return kissedBall;
    }

    public List<Ball> getPottedBalls() {
        return pottedBalls;
    }

    public int calculatePoints() {
        return 0;
    }


    public String toPrint() {
        return "";
    }

    public static String kickOutStatement(DBViewModel dbViewModel, List<Ball> cachedBalls, List<Player> cachedPlayers) {
        CurrentPlayerScreen currentPlayerScreen = new CurrentPlayerScreen();
        List<Player> allPlayers = dbViewModel.getPlayersList();

        String s = "\n";
        List<Player> kickedOutPlayers = new ArrayList<>();
        List<Player> reinstatedPlayers = new ArrayList<>();
        List<EventAction> allEvents = dbViewModel.getEventList();

        for (Player player : allPlayers) {
            if (player.isKickedOutFromDB(dbViewModel)) {
                kickedOutPlayers.add(player);
            }
        }
        if (!allEvents.isEmpty()) {
            List<Player> previouslyKickedOutPlayers = new ArrayList<>();

            for (Player player : cachedPlayers) {
                if (player.isKickedOut(cachedBalls, cachedPlayers)) {
                    previouslyKickedOutPlayers.add(player);
                }
            }
            if (!previouslyKickedOutPlayers.isEmpty()) {
                for (Player previouslyKickedOutPlayer : previouslyKickedOutPlayers) {
                    if (!kickedOutPlayers.contains(previouslyKickedOutPlayer)) {
                        reinstatedPlayers.add(previouslyKickedOutPlayer);
                    }
                }
            }

        }
        if (!reinstatedPlayers.isEmpty()) {
            String[] playerStringArray = {"and ", " are ", " ", ", ", " is "};
            s += handlePlayerPlural(reinstatedPlayers, playerStringArray);
            s += "BACK in the game\n";
        }

        if (!kickedOutPlayers.isEmpty()) {
            String[] playerStringArray = {"and ", " are ", " ", ", ", " is "};
            s += handlePlayerPlural(kickedOutPlayers, playerStringArray);
            s += "KICKED OUT";

        }
        for (Player player : kickedOutPlayers) {
            System.out.println("KICKED OUT-- " + player.getPlayerName());
        }
        for (Player player : reinstatedPlayers) {
            System.out.println("REINSTATED -- " + player.getPlayerName());
        }
        if (currentPlayerScreen.isGameOver(dbViewModel) && currentPlayerScreen.canRank(dbViewModel)) {
            s += "GAME OVER!!!\n";
            List<Player> winners = currentPlayerScreen.findWinners(dbViewModel);
            if (!winners.isEmpty()) {
                String[] playerStringArray = {"and ", " have ", " ", ", ", " has "};
                s += handlePlayerPlural(winners, playerStringArray);
                s += "won!!! ";
            }
        } else if (currentPlayerScreen.isGameOver(dbViewModel) && !currentPlayerScreen.canRank(dbViewModel)) {
            s += "Game Over!!!\nNobody won";
        }
        for (Player player : cachedPlayers) {
            System.out.printf("%s\t%s\t%d\t%d\t%s\n", player.getPlayerName(), player.getPlayerTurn(), player.getPlayerScores(), player.getPlayerPosition(),player.getFirstTouch());
        }
        System.out.println("*************************************************");
        for (Player player : allPlayers) {
            System.out.printf("%s\t%s\t%d\t%d\t%s\n", player.getPlayerName(), player.getPlayerTurn(), player.getPlayerScores(), player.getPlayerPosition(),player.getFirstTouch());
        }

        Player currentPlayer;
        for (Player player : cachedPlayers) {

            if (player.getPlayerTurn().hashCode() == Utils.MY_TURN.hashCode()) {
                for (Player player1 : allPlayers) {
                    if (player1.equals(player)) {
                        currentPlayer = player1;
                        s += "CURRENT SCORE = ";
                        s += currentPlayer.getPlayerScores();
                        s += " and POSITION = ";
                        int pos = currentPlayer.getPlayerPosition();
                        if (pos == 0) {
                            s += "N/A";
                        } else {
                            s += String.valueOf(pos);
                        }
                        break;
                    }
                }
            }
        }

        return s;
    }

    public static String handlePlayerPlural(List<Player> list, String... grammarStringsPlayer) {
        String s = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.size() > 1) {
                if (i == (list.size() - 1)) {
                    s += grammarStringsPlayer[0];
                    s += list.get(i).getPlayerName();
                    s += grammarStringsPlayer[1];
                } else if (i == list.size() - 2) {
                    s += list.get(i).getPlayerName();
                    s += grammarStringsPlayer[2];
                } else {
                    s += list.get(i).getPlayerName();
                    s += grammarStringsPlayer[3];
                }
            } else {
                s += list.get(i).getPlayerName();
                s += grammarStringsPlayer[4];
            }

        }
        return s;
    }

    public static class SkipTurn extends Action {

        public SkipTurn(Player currentPlayer, Ball currentBall) {
            super(currentPlayer, currentBall, null, null);
        }

        @Override
        public int calculatePoints() {
            return -currentBall.getBallValue();
        }

        @Override
        public String toPrint() {
            StringBuilder sb = new StringBuilder();
            sb.append(currentPlayer.getPlayerName());
            sb.append(" skipped his turn");
            sb.append(" (" + String.valueOf(calculatePoints()) + ")\n");
            return sb.toString();
        }
    }

    public static class MissBall extends Action {

        public MissBall(Player currentPlayer, Ball currentBall) {
            super(currentPlayer, currentBall, null, null);
        }

        @Override
        public int calculatePoints() {
            return -currentBall.getBallValue();
        }

        @Override
        public String toPrint() {
            StringBuilder sb = new StringBuilder();
            sb.append(currentPlayer.getPlayerName());
            sb.append(" missed ");
            sb.append(currentBall.getBallName());
            sb.append(" (" + String.valueOf(calculatePoints()) + ")\n");
            return sb.toString();
        }

    }

    public static class KissCorrectWithPots extends Action {

        public KissCorrectWithPots(Player currentPlayer, Ball currentBall, Ball kissedBall, List<Ball> pottedBalls) {
            super(currentPlayer, currentBall, kissedBall, pottedBalls);
        }

        @Override
        public int calculatePoints() {
            int result = 0;
            if (!potContainsWhiteBall()) {
                for (Ball ball : pottedBalls) {
                    result += ball.getBallValue();
                }
            } else {
                result = -currentBall.getBallValue();
            }
            return result;
        }

        private boolean potContainsWhiteBall() {
            for (Ball ball : pottedBalls) {
                return ball.hashCode() == 0;
            }
            return false;
        }

        @Override
        public String toPrint() {
            StringBuilder sb = new StringBuilder();
            sb.append(currentPlayer.getPlayerName());
            sb.append(" kissed CORRECT ");
            sb.append(currentBall.getBallName());
            sb.append(" and potted ");
            String[] grammarStrings = {"and ", " ", ", "};
            sb.append(handleBallPlurals(pottedBalls, grammarStrings));
            sb.append(" (" + String.valueOf(calculatePoints()) + ")\n");
            return sb.toString();
        }

    }

    public static String handleBallPlurals(List<Ball> list, String... grammarStringsBall) {
        String s = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.size() > 1) {
                if (i == (list.size() - 1)) {
                    s += grammarStringsBall[0];
                    s += list.get(i).getBallName();
                } else if (i == list.size() - 2) {
                    s += list.get(i).getBallName();
                    s += grammarStringsBall[1];
                } else {
                    s += list.get(i).getBallName();
                    s += grammarStringsBall[2];
                }
            } else {
                s += list.get(i).getBallName();
            }
        }
        return s;
    }

    public static class KissCorrect extends Action {

        public KissCorrect(Player currentPlayer, Ball currentBall, Ball kissedBall) {
            super(currentPlayer, currentBall, kissedBall, null);
        }

        @Override
        public int calculatePoints() {
            return super.calculatePoints();
        }

        @Override
        public String toPrint() {
            StringBuilder sb = new StringBuilder();
            sb.append(currentPlayer.getPlayerName());
            sb.append(" kissed CORRECT ");
            sb.append(kissedBall.getBallName());
            sb.append(" (" + String.valueOf(calculatePoints()) + ")\n");
            return sb.toString();
        }


    }

    public static class KissWrong extends Action {

        public KissWrong(Player currentPlayer, Ball currentBall, Ball kissedBall) {
            super(currentPlayer, currentBall, kissedBall, null);
        }

        @Override
        public int calculatePoints() {
            return -kissedBall.getBallValue();
        }

        @Override
        public String toPrint() {
            StringBuilder sb = new StringBuilder();
            sb.append(currentPlayer.getPlayerName());
            sb.append(" kissed WRONG ");
            sb.append(kissedBall.getBallName());
            sb.append(" (" + String.valueOf(calculatePoints()) + ")\n");
            return sb.toString();
        }
    }

    public static class KissWrongWithPots extends Action {

        public KissWrongWithPots(Player currentPlayer, Ball currentBall, Ball kissedBall, List<Ball> pottedBalls) {
            super(currentPlayer, currentBall, kissedBall, pottedBalls);
        }

        @Override
        public int calculatePoints() {
            return -kissedBall.getBallValue();
        }

        @Override
        public String toPrint() {
            StringBuilder sb = new StringBuilder();
            sb.append(currentPlayer.getPlayerName());
            sb.append(" kissed WRONG ");
            sb.append(currentBall.getBallName());
            sb.append(" and wasted ");
            String[] ballStringArray = {"and ", " ", ", "};
            sb.append(handleBallPlurals(pottedBalls, ballStringArray));
            sb.append(" (" + String.valueOf(calculatePoints()) + ")\n");
            return sb.toString();
        }

    }

    public static class WrongAction extends Action {

        public WrongAction() {
            super();
        }

        @Override
        public int calculatePoints() {
            return super.calculatePoints();
        }

        @Override
        public String toPrint() {
            return "KICK OFF";
        }
    }

    public static class KickedOutAction extends Action {
        public KickedOutAction(Player currentPlayer) {
            super(currentPlayer);
        }

        @Override
        public int calculatePoints() {
            return super.calculatePoints();
        }

        @Override
        public String toPrint() {
            return currentPlayer.getPlayerName() + " is Kicked out so didn't play";
        }
    }
}
