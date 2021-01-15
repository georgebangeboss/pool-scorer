package com.example.pool.gui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pool.R;
import com.example.pool.Utils;
import com.example.pool.action.Action;
import com.example.pool.action.EventAction;
import com.example.pool.balls.Ball;
import com.example.pool.players.Player;
import com.example.pool.room.DBViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CurrentPlayerScreen extends AppCompatActivity implements EventEditFragmentDialog.EventDialogListener {

    public static final int POT_REQUEST = 1;
    public static final int KISS_REQUEST = 2;
    public static final String YES_WINNER = "WON";
    public static final String NO_WINNER = "NOT WON";
    public static final String EXTRA_WIN_STATUS = "com.example.pool.gui.EXTRA_WIN_STATUS";
    public static final String EXTRA_WIN_STATEMENT = "com.example.pool.gui.EXTRA_WIN_STATEMENT";
    public static final String NO_WINNER_STATEMENT = "GAME OVER!!!! No Winner, not Everyone Played";
    private static boolean skipPressed = false, missPressed = false, kissPressed = false, potPressed = false;
    private static Action currentAction = null;
    private static final String TAG = "CurrentPlayerScreen";
    private int currentPlayerIndex;
    private boolean isTwoPlayer;
    private int numberOfPlayers;

    private ImageButton undo, nextPlayer;
    private Button skip, miss, kiss, pot;
    private TextView currentPlayerName, currentPlayerPosition, currentPlayerScore, currentBallNumber, currentBallValue, description;
    private FrameLayout mEventsFrame, mScoresFrame, mCurrentMatFrame;
    private LinearLayout mCurrentPlayerScreen;

    private DBViewModel mViewModel;

    private FragmentManager fm;

    private Ball currentBall;
    private Ball kissedBall;
    private Player currentPlayer;
    private List<Player> cachedPlayerList;
    private List<Ball> cachedBallList;
    private List<Ball> pottedBalls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_player_screen);
        Log.d(TAG, "onCreate: created");

        mViewModel = new ViewModelProvider(this).get(DBViewModel.class);
        numberOfPlayers = mViewModel.getPlayersList().size();
        isTwoPlayer = (numberOfPlayers == 2);

        currentBallValue = findViewById(R.id.current_ball_value);
        currentBallNumber = findViewById(R.id.current_ball);
        description = findViewById(R.id.description_box);
        currentPlayerName = findViewById(R.id.current_player_name);
        currentPlayerPosition = findViewById(R.id.current_player_position);
        currentPlayerScore = findViewById(R.id.current_player_score);

        mEventsFrame = findViewById(R.id.events_frame);
        mCurrentPlayerScreen = findViewById(R.id.player_screen_container);
        mCurrentMatFrame = findViewById(R.id.mat_frame);
        mScoresFrame = findViewById(R.id.leader_board_frame);

        skip = findViewById(R.id.skip);
        miss = findViewById(R.id.miss);
        kiss = findViewById(R.id.kiss);
        pot = findViewById(R.id.pot);
        undo = findViewById(R.id.undo);
        nextPlayer = findViewById(R.id.next_player);

        fm = getSupportFragmentManager();

        prepareIndices(mViewModel);
        updateUI(currentPlayer, currentBall);

        ScoresBoardFragment scoresBoardFragment = ScoresBoardFragment.newInstance(mViewModel);
        fm.beginTransaction().replace(R.id.leader_board_frame, scoresBoardFragment).commit();
        MatFragment matFragment = MatFragment.newInstance(mViewModel);
        fm.beginTransaction().replace(R.id.mat_frame, matFragment).commit();

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlayer.isKickedOutFromDB(mViewModel)) {
                    Utils.toaster(CurrentPlayerScreen.this, "Player has been kicked out temporarily ,Go to next player");
                } else {
                    missPressed = false;
                    potPressed = false;
                    kissPressed = false;
                    skipPressed = true;
                    currentAction = createAction(currentPlayer, currentBall, null, null);
                    description.setText(currentAction.toPrint());
                }


            }
        });

        pot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlayer.isKickedOutFromDB(mViewModel)) {
                    Utils.toaster(CurrentPlayerScreen.this, "Player has been kicked out temporarily ,Go to next player");
                } else {
                    missPressed = false;
                    potPressed = false;
                    skipPressed = false;
                    pottedBalls = null;
                    Intent intent = new Intent(CurrentPlayerScreen.this, MatPot.class);
                    startActivityForResult(intent, POT_REQUEST);
                }

            }
        });
        kiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlayer.isKickedOutFromDB(mViewModel)) {
                    Utils.toaster(CurrentPlayerScreen.this, "Player has been kicked out temporarily ,Go to next player");
                } else {
                    missPressed = false;
                    potPressed = false;
                    kissPressed = false;
                    skipPressed = false;
                    Intent intent = new Intent(CurrentPlayerScreen.this, MatKiss.class);
                    startActivityForResult(intent, KISS_REQUEST);
                }

            }
        });
        miss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlayer.isKickedOutFromDB(mViewModel)) {
                    Utils.toaster(CurrentPlayerScreen.this, "Player has been kicked out temporarily ,Go to next player");
                } else {
                    potPressed = false;
                    kissPressed = false;
                    skipPressed = false;
                    missPressed = true;
                    currentAction = createAction(currentPlayer, currentBall, null, null);
                    description.setText(currentAction.toPrint());
                }


            }
        });
        nextPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGameOver(mViewModel)) {
                    //to cache the previous state of balls and players before changes are made
                    fetchFromDBToCacheLater(mViewModel);

                    updateBallAt(currentAction, mViewModel);

                    updatePlayerFirstTouch(currentPlayer, mViewModel);

                    updatePlayerScores(currentAction, mViewModel);

                    updatePositions(mViewModel);

                    updatePlayerTurnDB(mViewModel, currentAction);

                    updateBallStatusDB(mViewModel);


                    Utils.loadEventIntoDB(cachedPlayerList, mViewModel, cachedBallList, currentAction);

                    missPressed = false;
                    potPressed = false;
                    kissPressed = false;
                    skipPressed = false;


                    if (isGameOver(mViewModel)) {
                        if (canRank(mViewModel)) {
                            List<Player> winners = findWinners(mViewModel);
                            String[] grammar = {"GAME OVER!!!! The winners are ", "GAME OVER!!!! The winner is ", " and ", ", "};
                            String str = handleWinnerPlural(winners, grammar);
                            startWinnerPage(YES_WINNER, str);
                        } else {
                            startWinnerPage(NO_WINNER, NO_WINNER_STATEMENT);
                        }
                    } else {
                        prepareIndices(mViewModel);
                        updateUI(currentPlayer, currentBall);
                    }

                } else {
                    if (canRank(mViewModel)) {
                        List<Player> winners = findWinners(mViewModel);
                        String[] grammar = {"The winner are ", "The winners is ", " and ", ", "};
                        String str = handleWinnerPlural(winners, grammar);
                        startWinnerPage(YES_WINNER, str);
                    } else {
                        startWinnerPage(NO_WINNER, NO_WINNER_STATEMENT);
                    }
                }
            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kissedBall = currentBall;
                pottedBalls = null;
                currentAction = createDefaultAction(currentPlayer, currentBall, kissedBall);
                description.setText("DEFAULT: " + currentAction.toPrint());

            }
        });


    }

    public static String handleWinnerPlural(List<Player> list, String... grammarStringsPlayer) {
        String s = "";
        if (list.size() > 1) {
            s += grammarStringsPlayer[0];
        } else {
            s += grammarStringsPlayer[1];
        }
        for (int i = 0; i < list.size(); i++) {
            if (i == (list.size() - 1)) {
                s += list.get(i).getPlayerName();
            } else if (i == list.size() - 2) {
                s += list.get(i).getPlayerName();
                s += grammarStringsPlayer[2];
            } else {
                s += list.get(i).getPlayerName();
                s += grammarStringsPlayer[3];
            }

        }
        return s;
    }

    private void startWinnerPage(String winStatus, String winStatement) {
        Intent intent = new Intent(CurrentPlayerScreen.this, WinnerPage.class);
        intent.putExtra(EXTRA_WIN_STATUS, winStatus);
        intent.putExtra(EXTRA_WIN_STATEMENT, winStatement);
        startActivity(intent);
    }

    private void fetchFromDBToCacheLater(DBViewModel dbViewModel) {
        cachedPlayerList = dbViewModel.getPlayersList();
        cachedBallList = dbViewModel.getBallList();
    }

    public List<Player> findWinners(DBViewModel dbViewModel) {
        List<Player> allPlayers = dbViewModel.getPlayersList();
        Stream<Player> allPlayersStream = allPlayers.stream();

        List<Player> highestPlayers = allPlayersStream.filter(i -> i.getPlayerPosition() == 1).collect(Collectors.toList());
        return highestPlayers;
    }

    private Action createDefaultAction(Player currentPlayer, Ball currentBall, Ball kissedBall) {
        potPressed = false;
        kissPressed = false;
        missPressed = false;
        skipPressed = false;
        return createAction(currentPlayer, currentBall, kissedBall, null);
    }

    private void updateUI(Player theCurrentPlayer, Ball theCurrentBall) {
        if (!isGameOver(mViewModel)) {
            currentBallValue.setText(String.valueOf(theCurrentBall.getBallValue()));
            currentBallNumber.setText(theCurrentBall.getBallName());
            currentPlayerPosition.setText(String.valueOf(theCurrentPlayer.getPlayerPosition()));
            currentPlayerScore.setText(String.valueOf(theCurrentPlayer.getPlayerScores()));
            currentPlayerName.setText(theCurrentPlayer.getPlayerName());
        } else {
            //TODO snack Bar to show edit Event or start new game
        }

    }


    private Action createAction(Player theCurrentPlayer, Ball theCurrentBall, Ball theKissedBall, List<Ball> thePottedBalls) {
        if (!skipPressed && !potPressed && !kissPressed && !missPressed) {
            //return KissCorrect Action
            return new Action.KissCorrect(theCurrentPlayer, theCurrentBall, theKissedBall);
        } else if (skipPressed && !potPressed && !kissPressed && !missPressed) {
            //return Skip Action
            return new Action.SkipTurn(theCurrentPlayer, theCurrentBall);
        } else if (!skipPressed && !potPressed && !kissPressed && missPressed) {
            //return Miss Action
            return new Action.MissBall(theCurrentPlayer, theCurrentBall);
        } else if (!skipPressed && potPressed && kissPressed && !missPressed) {
            if (theKissedBall.equals(theCurrentBall)) {
                //return KissCorrectWithPots
                return new Action.KissCorrectWithPots(theCurrentPlayer, theCurrentBall, theKissedBall, thePottedBalls);
            } else {
                //return KissWrongWithPots
                return new Action.KissWrongWithPots(theCurrentPlayer, theCurrentBall, theKissedBall, thePottedBalls);
            }
        } else if (!skipPressed && potPressed && !kissPressed && !missPressed) {
            //return KissCorrectWithPots
            return new Action.KissCorrectWithPots(theCurrentPlayer, theCurrentBall, theKissedBall, thePottedBalls);
        } else if (!skipPressed && !potPressed && kissPressed && !missPressed) {
            if (theKissedBall.equals(theCurrentBall)) {
                //return KissCorrect
                return new Action.KissCorrect(theCurrentPlayer, theCurrentBall, theKissedBall);
            } else {
                //return KissWrong
                return new Action.KissWrong(theCurrentPlayer, theCurrentBall, theKissedBall);
            }
        } else {
            //return WrongAction
            Utils.toaster(CurrentPlayerScreen.this, "wrong action");
            return new Action.WrongAction();

        }

    }

    public boolean isGameOver(DBViewModel dbViewModel) {
        List<Ball> allBalls = dbViewModel.getBallList();
        removeWhiteBall(allBalls);
        if (isEveryoneKickedOut(dbViewModel)) {
            return true;
        } else if (isBallsOut(allBalls)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isBallsOut(List<Ball> allBalls) {
        for (Ball ball : allBalls) {
            if (ball.isBallAvailable()) {
                return false;
            }
        }
        return true;
    }


    public boolean canRank(DBViewModel dbViewModel) {
        List<Player> playerList = dbViewModel.getPlayersList();
        for (Player player : playerList) {
            if (player.getFirstTouch().hashCode() == Utils.FIRST_TOUCH_FALSE.hashCode()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEveryoneKickedOut(DBViewModel dbViewModel) {
        List<Player> playerList = dbViewModel.getPlayersList();
        boolean isEveryoneKickedOut = true;
        List<Player> theRest = playerList.stream().filter(i -> (i.getPlayerPosition() != 1)).collect(Collectors.toList());
        if (canRank(dbViewModel) && theRest.size() == playerList.size() - 1) {
            for (Player player : theRest) {
                if (player.isKickedOutFromDB(dbViewModel) == false) {
                    isEveryoneKickedOut = false;
                    break;
                }
            }
            return isEveryoneKickedOut;
        } else {
            return false;
        }

    }


    public void prepareIndices(DBViewModel dbViewModel) {
        if (!isGameOver(dbViewModel)) {
            currentBall = dbViewModel.findCurrentBall();
            currentPlayer = dbViewModel.findCurrentPlayer();
            currentPlayerIndex = currentPlayer.getPlayerIndex();
            kissedBall = currentBall;
            pottedBalls = null;
            if (currentPlayer.isKickedOutFromDB(dbViewModel)) {
                description.setText(currentPlayer.getPlayerName() + " can't play temporarily :( Go to Next Player");
                currentAction = new Action.KickedOutAction(currentPlayer);
                return;
            }
            currentAction = createDefaultAction(currentPlayer, currentBall, kissedBall);
            //sets the default action to kiss Correct
            missPressed = false;
            potPressed = false;
            kissPressed = false;
            skipPressed = false;
            currentAction = createAction(currentPlayer, currentBall, kissedBall, null);
            description.setText("DEFAULT: " + currentAction.toPrint());
        } else {
            description.setText("GAME OVER!!!!!");
        }

    }

    public void updatePlayerTurnDB(DBViewModel dbViewModel, Action action) {
        Player nextPlayer;
        Player theCurrentPlayer = dbViewModel.findCurrentPlayer();
        List<Player> playerList = dbViewModel.getPlayersList();
        if (!(action instanceof Action.KissCorrectWithPots)) {
            theCurrentPlayer.setPlayerTurn(Utils.NOT_MY_TURN);
            dbViewModel.updatePlayer(theCurrentPlayer);
            nextPlayer = currentPlayerIndex + 1 > playerList.size() ? playerList.get(0) : playerList.get(currentPlayerIndex);
            nextPlayer.setPlayerTurn(Utils.MY_TURN);
            dbViewModel.updatePlayer(nextPlayer);
        }
    }

    public void updateBallStatusDB(DBViewModel dbViewModel) {
        //we first get the current Ball from the DB
        Ball currentBall = dbViewModel.findCurrentBall();
        if (currentBall.getBallAt().hashCode() == Utils.IN_POT.hashCode()) {
            currentBall.setBallNotActive();
            dbViewModel.updateBall(currentBall);
            System.out.println("current ball out");
        } else {
            //maintain current Ball if it wasn't potted
            System.out.println("nothing changes");
            return;
        }
        //if current ball was potted
        List<Ball> allBallsFromDB = dbViewModel.getBallList();
        for (Ball ball : allBallsFromDB) {
            //first ball that passes the test below
            if (ball.getBallAt().hashCode() == Utils.ON_MAT.hashCode() && ball.getBallIndex() > currentBall.getBallIndex()) {
                ball.setBallActive();
                dbViewModel.updateBall(ball);
                System.out.println("next ball in");
                return;
            }
        }
        for (Player player : dbViewModel.getPlayersList()) {
            System.out.printf("%s\t%s\t%d\t%d\t%s\n", player.getPlayerName(), player.getPlayerTurn(), player.getPlayerScores(), player.getPlayerPosition(), player.getFirstTouch());
        }

    }

    public void updatePlayerFirstTouch(Player theCurrentPlayer, DBViewModel dbViewModel) {
        theCurrentPlayer.setFirstTouch(Utils.FIRST_TOUCH_TRUE);
        dbViewModel.updatePlayer(theCurrentPlayer);
    }

    public void updateBallAt(Action action, DBViewModel dbViewModel) {
        if (action instanceof Action.KissCorrectWithPots || action instanceof Action.KissWrongWithPots) {
            List<Ball> pottedBalls = action.getPottedBalls();
            removeWhiteBall(pottedBalls);//to make sure the white ball always stays on mat
            for (Ball ball : pottedBalls) {
                ball.setBallInPot();
                dbViewModel.updateBall(ball);
            }
        }
    }


    public static void removeWhiteBall(List<Ball> pottedBalls) {
        for (Ball ball : pottedBalls) {
            if (ball.hashCode() == 0) {
                pottedBalls.remove(ball);
            }
        }
    }

    public void updatePositions(DBViewModel dbViewModel) {
        List<Player> playerList = dbViewModel.getPlayersList();
        if (canRank(dbViewModel)) {
            Map<Integer, List<Player>> orderMap = new HashMap<>();
            int index = 1;
            while (playerList.size() > 0) {
                List<Player> playerListLet = new ArrayList<>();
                Player highestPlayer;
                Player player1 = playerList.get(0);
                highestPlayer = playerList.stream().reduce(player1, (a, b) -> (b.getPlayerScores() > a.getPlayerScores()) ? b : a);
                //puts the highest players into a player list-let each time
                for (Player player : playerList) {
                    if (player.getPlayerScores() == highestPlayer.getPlayerScores()) {
                        playerListLet.add(player);
                    }
                }
                playerList.removeIf(i -> i.getPlayerScores() == highestPlayer.getPlayerScores());
                orderMap.put(index, playerListLet);
                index++;
            }
            int nextPosition = 1;
            for (int i = 1; i < index; i++) {
                List<Player> fetchedPlayerList = orderMap.get(i);
                for (Player clusteredPlayer : fetchedPlayerList) {
                    clusteredPlayer.setPlayerPosition(nextPosition);
                    dbViewModel.updatePlayer(clusteredPlayer);
                }
                int offset = fetchedPlayerList.size();
                nextPosition += offset;
            }
        } else {
            for (Player player : playerList) {
                player.setPlayerPosition(0);
                dbViewModel.updatePlayer(player);
            }
        }
    }

    public void updatePlayerScores(Action action, DBViewModel dbViewModel) {
        Player currentPlayer = action.getCurrentPlayer();
        int score = action.calculatePoints();
        if (isTwoPlayer) {
            List<Player> playerList = dbViewModel.getPlayersList();
            Player nextPlayer = currentPlayer.getPlayerIndex() == 1 ? playerList.get(1) : playerList.get(0);
            int currentPlayerScore = currentPlayer.getPlayerScores() + score;
            int nextPlayerScore = nextPlayer.getPlayerScores();
            int difference = Math.abs(currentPlayerScore - nextPlayerScore);
            if (currentPlayerScore > nextPlayerScore) {
                currentPlayer.setPlayerScores(difference);
                nextPlayer.setPlayerScores(0);
            } else if (currentPlayerScore < nextPlayerScore) {
                currentPlayer.setPlayerScores(0);
                nextPlayer.setPlayerScores(difference);
            } else {
                currentPlayer.setPlayerScores(0);
                nextPlayer.setPlayerScores(0);
            }
            dbViewModel.updatePlayer(currentPlayer);
            dbViewModel.updatePlayer(nextPlayer);

        } else {
            currentPlayer.setPlayerScores(currentPlayer.getPlayerScores() + score);
            dbViewModel.updatePlayer(currentPlayer);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (requestCode == POT_REQUEST) {
                if (resultCode == RESULT_OK) {
                    if (bundle.getParcelableArrayList(MatPot.EXTRA_CLICKED_BALLS) != null) {
                        pottedBalls = bundle.getParcelableArrayList(MatPot.EXTRA_CLICKED_BALLS);
                        potPressed = true;
                        currentAction = createAction(currentPlayer, currentBall, kissedBall, pottedBalls);
                        pottedBalls = null;
                        description.setText(currentAction.toPrint());
                    }
                } else {
                    potPressed = false;
                    Utils.toaster(this, "You don't have the balls");
                }

            } else if (requestCode == KISS_REQUEST) {
                if (resultCode == RESULT_OK) {
                    if (bundle.getParcelable(MatKiss.EXTRA_KISSED_BALL) != null) {
                        kissedBall = bundle.getParcelable(MatKiss.EXTRA_KISSED_BALL);
                        kissPressed = true;
                        currentAction = createAction(currentPlayer, currentBall, kissedBall, null);
                        description.setText(currentAction.toPrint());
                    }
                } else {
                    kissPressed = false;
                    Utils.toaster(this, "You don't have the balls");
                }
            }
        } else {
            Utils.toaster(this, "You don't have the balls");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_to_events:
                gotoEvents(mViewModel);
                return true;
            case R.id.go_to_mat:
                gotoMat(mViewModel);
                return true;
            case R.id.go_to_scores:
                gotoScoreBoard(mViewModel);
                return true;
            case R.id.restart_game:
                restartGameDialog(mViewModel);
                return true;
            case R.id.go_to_saved_games:
                //TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void restartGameDialog(DBViewModel dbViewModel) {
        makeFragmentUIDisappear();
        RestartGameDialog restartGameDialog = new RestartGameDialog(dbViewModel);
        restartGameDialog.show(fm, "RESTART GAME DIALOG");
    }

    public void gotoScoreBoard(DBViewModel dbViewModel) {
        makeFragmentUIDisappear();
        makeScoresFragmentAppear();
    }

    public void gotoMat(DBViewModel dbViewModel) {
        makeFragmentUIDisappear();
        makeMatFragmentAppear();
    }


    public void gotoEvents(DBViewModel mViewModel) {
        makeFragmentUIDisappear();
        List<EventAction> theEvents = mViewModel.getEventList();
        EventsFragment eventsFragment = new EventsFragment(theEvents);
        makeEventsFragmentAppear();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.events_frame, eventsFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.general_menu, menu);
        return true;
    }

    private void makeEventsFragmentAppear() {
        mCurrentPlayerScreen.setVisibility(View.GONE);
        mEventsFrame.setVisibility(View.VISIBLE);
    }

    private void makeMatFragmentAppear() {
        mCurrentPlayerScreen.setVisibility(View.GONE);
        mCurrentMatFrame.setVisibility(View.VISIBLE);
    }

    private void makeScoresFragmentAppear() {
        mCurrentPlayerScreen.setVisibility(View.GONE);
        mScoresFrame.setVisibility(View.VISIBLE);
    }

    @Override
    public void makeFragmentUIDisappear() {
        mEventsFrame.setVisibility(View.GONE);
        mCurrentMatFrame.setVisibility(View.GONE);
        mScoresFrame.setVisibility(View.GONE);

        mCurrentPlayerScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateCurrentPlayerUI(DBViewModel dbViewModel) {
        prepareIndices(dbViewModel);
        updateUI(currentPlayer, currentBall);
    }

    @Override
    public void onBackPressed() {
        removeAllFragments();
        makeFragmentUIDisappear();
        ExitDialog exitDialog = new ExitDialog();
        exitDialog.show(fm, "EXIT DIALOG");

    }

    private void removeAllFragments() {
        removeEventsFragment();
        removeScoreBoardFragment();
        removeMatFragment();
    }

    @Override
    public void removeEventsFragment() {
        Fragment fragment = fm.findFragmentById(R.id.events_frame);
        if (fragment != null) {
            fm.beginTransaction().remove(fragment).commit();
        }
    }

    public void removeMatFragment() {
        Fragment fragment = fm.findFragmentById(R.id.mat_frame);
        if (fragment != null) {
            fm.beginTransaction().remove(fragment).commit();
        }
    }

    public void removeScoreBoardFragment() {
        Fragment fragment = fm.findFragmentById(R.id.leader_board_frame);
        if (fragment != null) {
            fm.beginTransaction().remove(fragment).commit();
        }
    }

}
