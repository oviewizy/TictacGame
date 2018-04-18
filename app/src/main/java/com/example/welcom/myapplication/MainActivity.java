/*
* Copyright 2018 The Android Tic tac game designed by Okpako Ovie Wisdom
*  A project designed as final product for an android beginner course
*/

package com.example.welcom.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Boolean mCompTurn = true;
    private String[] mBoard = new String[9];
    private int number;
    private Boolean mWin = false;
    private Boolean mGameTie = false;
    private int mXscore = 0;
    private int mOscore = 0;
    private Boolean mSinglePlayerMode = true;
    private Boolean mComPlaysFirst = false;
    private int mComPlayCount = 1;
    private int mHumanPlayCount = 1;
    private Boolean mHumanTurn = false;
    private int mWorstCase = 10;
    private int mBestCase = 10;
    private int mBlankPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* initialize playing board */
        for (int count = 0; count < 9; count++) {
            mBoard[count] = "";
        }

        /*This dialog is used to determine the number of players in the system*/
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("TicTacToe Game Settings");
        builder.setCancelable(false);
        builder.setMessage("Please select the number of Players");
        builder.setPositiveButton("Two Player", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id) {
                setGametoTwoPlayter();
            }
        });
        builder.setNegativeButton("Single Player", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
     /* Choose a player at random between human and computer */
                if (mSinglePlayerMode == true) {
            /* Sets if computer plays first*/
                    Random rand = new Random();
                    int value= rand.nextInt(2)+ 1;
                    if (value == 1) {
                /* Computer plays first*/
                        mComPlaysFirst = true;
                        comPlaysFirstAsFirstPlayer();

                    } else {
                /* Human plays first*/
                        mComPlaysFirst = false;
                    }

                }
            }
        });

        builder.show();



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.size3:
                // startActivity(new Intent(this, Main2Activity.class));
                setGametosize3();
                return true;
            case R.id.size5:
                setGametosize5();
                return true;
            case R.id.single:
                setGameetoSinglePlayer();
                return true;
            case R.id.twoplayer:
                setGametoTwoPlayter();
                return true;
            case R.id.about:
                Toast toastmsg = Toast.makeText(this, "This Game is designed by Okpako Ovie Wisdom of the Android learners Beginner team AB66", Toast.LENGTH_LONG);
                toastmsg.show();
                return true;

        }
        return true;
    }

    public void setGameetoSinglePlayer() {
 /* Sets if computer plays first*/
        mXscore = 0;
        mOscore = 0;
        resetGame();
        mSinglePlayerMode = true;
        Random rand = new Random();
        int value = rand.nextInt(2) + 1;
        if (value == 1) {
                /* Computer plays first*/
            Toast toastmsg = Toast.makeText(this, "Computer: (Player One) plays first!", Toast.LENGTH_LONG);
            toastmsg.show();
            mComPlaysFirst = true;
            comPlaysFirstAsFirstPlayer();

        } else {
                /* Human plays first*/
            Toast toastmsg = Toast.makeText(this, "Human: (Player Two) plays first!", Toast.LENGTH_LONG);
            toastmsg.show();
            mComPlaysFirst = false;
        }
    }

    public void setGametoTwoPlayter() {
        mXscore = 0;
        mOscore = 0;
        resetGame();
        mSinglePlayerMode = false;

    }

    public void setGametosize3() {
        for (int i=0; i<9; i++){
        String buttonID = "button_" + i;
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
        Button button = (Button) findViewById(resID);
            ViewGroup.LayoutParams params =button.getLayoutParams();
            params.width=130;
            params.height=130;
            button.setLayoutParams(params);
        }
    }

    public void setGametosize5() {
        for (int i=0; i<9; i++){
            String buttonID = "button_" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = (Button) findViewById(resID);
            ViewGroup.LayoutParams params =button.getLayoutParams();
            params.width=160;
            params.height=160;
            button.setLayoutParams(params);
        }
    }


    /* Responds to the clicks for each game element on the board*/
    public void displayBtn(View view) {
        Button button = (Button) view;
        if (mWin == false && mGameTie == false) {
            /* This is a method call to update the button as necessary*/
            updateButton(button);
        }
    }

    public void checkForWin() {

        checkRow();

        checkColumn();

        checkRightDiagonal();

        checkLeftDiagonal();

    }

    public int getNumericValue(Button button) {
        switch (button.getId()) {
            case R.id.button_0:
                number = 0;
                break;
            case R.id.button_1:
                number = 1;
                break;
            case R.id.button_2:
                number = 2;
                break;
            case R.id.button_3:
                number = 3;
                break;
            case R.id.button_4:
                number = 4;
                break;
            case R.id.button_5:
                number = 5;
                break;
            case R.id.button_6:
                number = 6;
                break;
            case R.id.button_7:
                number = 7;
                break;
            case R.id.button_8:
                number = 8;
                break;
        }
        return number;
    }

    /* this method handles the declaring of a winner */
    public void declareWin(String x) {
        String playerName = "";
        if (x == "x") {
            playerName = "Player ONE";
            mXscore++;
            TextView textview = (TextView) findViewById(R.id.scorePlayer1_text_view);
            String textValue = "";
            textValue = "Player1 Scores: " + mXscore;
            textview.setText(textValue);
        } else {
            playerName = "Player TWO";
            mOscore++;
            TextView textview = (TextView) findViewById(R.id.scorePlayer2_text_view);
            String textValue = "";
            textValue = "Player2 Scores: " + mOscore;
            textview.setText(textValue);
        }
        Toast toastmsg = Toast.makeText(this, playerName + "  " + "wins", Toast.LENGTH_SHORT);
        toastmsg.show();
        mWin = true;

    }

    /* Method handles the display on the button*/
    public void updateButton(Button button) {

        int numb = getNumericValue(button);

        String btnValue = button.getText().toString();
        int num = btnValue.length();

        if (num < 1) {

            if (mSinglePlayerMode == true) {

                setSinglePlayerMode(button, numb);

            } else {
                twoPlayerMode(button);
            }
        } else {
            Toast toastmsg = Toast.makeText(this, "Value is already set!", Toast.LENGTH_SHORT);
            toastmsg.show();
        }
    }

    /* Methdd checks for win or ties on the right diagonal*/
    public void checkRightDiagonal() {
        // Loop for checking right diagonal

        int xcount = 0;
        int ocount = 0;
        int blankpost = 0;

        for (int index = 0; index < 9; index = index + 4) {

            if (mBoard[index] == "x") {
                ++xcount;
            } else if (mBoard[index] == "o") {
                ++ocount;
            } else {
                ++blankpost;
            }

            if (xcount == 3) {
                xcount = 0;
                declareWin("x");

            } else if (ocount == 3) {
                ocount = 0;
                declareWin("o");

            }
        }

    }

    /* Methdd checks for win or ties on the left diagonal*/
    public void checkLeftDiagonal() {
        // Loop for checking right diagonal

        int xcount = 0;
        int ocount = 0;
        int blankpost = 0;

        for (int index = 2; index < 7; index = index + 2) {

            if (mBoard[index] == "x") {
                ++xcount;
            } else if (mBoard[index] == "o") {
                ++ocount;
            } else {
                ++blankpost;
            }

            if (xcount == 3) {
                xcount = 0;
                declareWin("x");

            } else if (ocount == 3) {
                ocount = 0;
                declareWin("o");

            }
        }

    }

    /* Methdd checks for win or ties on each row*/
    public void checkRow() {
        // Loop for checking all rows
        for (int index = 0; index < 7; index = index + 3) {

            int index2 = index;
            int count = 1;
            int xcount = 0;
            int ocount = 0;
            int blankpost = 0;

            while (count < 4) {
                if (mBoard[index2] == "x") {
                    ++xcount;
                    Log.v("This is board", "updateval: " + mBoard[index2]);
                    Log.v("This is xcount", "updateval: " + xcount);
                } else if (mBoard[index2] == "o") {
                    ++ocount;
                } else {
                    ++blankpost;
                }

                if (xcount == 3) {
                    xcount = 0;
                    declareWin("x");

                } else if (ocount == 3) {
                    ocount = 0;
                    declareWin("o");

                }
                ++count;
                ++index2;
            }

        }

    }

    /* Methdd checks for win or ties on each column */
    public void checkColumn() {
        // Loop for checking all Columns
        for (int index = 0; index < 3; index++) {

            int index2 = index;
            int count = 1;
            int xcount = 0;
            int ocount = 0;
            int blankpost = 0;

            while (count < 4) {
                //   Log.v("This is count", "updateval: " + count);
                //  Log.v("This is index2", "updateval: " + index2);

                if (mBoard[index2] == "x") {
                    ++xcount;
                    //     Log.v("This is board", "updateval: " + Board[index2]);
                    //    Log.v("This is xcount", "updateval: " + xcount);
                } else if (mBoard[index2] == "o") {
                    ++ocount;
                    //      Log.v("This is board", "updateval: " + Board[index2]);
                    //      Log.v("This is ocount", "updateval: " + ocount);
                } else {
                    ++blankpost;
                }

                if (xcount == 3) {
                    xcount = 0;
                    declareWin("x");

                } else if (ocount == 3) {
                    ocount = 0;
                    declareWin("o");

                }
                ++count;
                index2 = index2 + 3;
            }
        }
    }

    /* Methdd responds to the click event of the reset button*/
    public void reset(View v) {

        resetGame();
    }

    /* Methdd handles the reset of the game*/
    public void resetGame() {
        for (int count = 0; count < 9; count++) {
            String buttonID = "button_" + count;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = (Button) findViewById(resID);
            button.setText("");
            mBoard[count] = "";
            mComPlayCount = 1;
            mHumanPlayCount = 1;
            mHumanTurn = false;
            mWorstCase = 10;
            mBestCase = 10;
            mBlankPosition = 0;

        }
        mWin = false;
        mGameTie = false;
    }

    /* Methdd checks for ties on the board*/
    public void checkForTie() {
        boolean tie = true;
        for (int i = 0; i < 9; i++) {
            if (mBoard[i] == "") {
                tie = false;
            }
        }
        if (tie == true && mWin == false) {
            declareTie();
        }
    }

    /* Methdd declares a tie*/
    public void declareTie() {
        Toast toastmsg = Toast.makeText(this, "This game end in a Tie!", Toast.LENGTH_SHORT);
        toastmsg.show();
        mGameTie = true;
    }

    /* Method handles the working for two players*/
    public void twoPlayerMode(Button button) {
        if (mCompTurn == true) {
            TextView textviewX = (TextView) findViewById(R.id.txtX);
            TextView textviewO = (TextView) findViewById(R.id.txtO);
            mBoard[number] = "x";
            button.setText("X");
            // String name;
            // name = button.getResources().geti();
            //button.setText(name);
            mCompTurn = false;
            // sets the color and size of text back as previous
            textviewX.setTextSize(20);
            textviewX.setTextColor(Color.BLACK);
            // sets the color and size of text to show turn
            textviewO.setTextSize(25);
            textviewO.setTextColor(Color.GREEN);

            checkForWin();
            checkForTie();

        } else {
            TextView textviewX = (TextView) findViewById(R.id.txtX);
            TextView textviewO = (TextView) findViewById(R.id.txtO);
            button.setText("O");
            mBoard[number] = "o";
            mCompTurn = true;
            // sets the color and size of text back as previous
            textviewO.setTextSize(20);
            textviewO.setTextColor(Color.BLACK);
            // sets the color and size of text to show turn
            textviewX.setTextSize(25);
            textviewX.setTextColor(Color.GREEN);

            checkForWin();
            checkForTie();
        }
    }

    /* Method handles the working for two players*/
    public void setSinglePlayerMode(Button button, int numb) {
        TextView textviewX = (TextView) findViewById(R.id.txtX);
        TextView textviewO = (TextView) findViewById(R.id.txtO);
        button.setText("O");
        mBoard[numb] = "o";
        mHumanTurn = false;
        //for (int i=0; i<9; i++) {
        //  Log.v("number when used at" + i, "okpako: " + Board[i]);
        // }
        // sets the color and size of text back as previous
        textviewO.setTextSize(20);
        textviewO.setTextColor(Color.BLACK);
        // sets the color and size of text to show turn
        textviewX.setTextSize(25);
        textviewX.setTextColor(Color.GREEN);
        mHumanPlayCount++;
// When computer is made to play as first player
        if (mComPlayCount == 2 && mComPlaysFirst == true) {
            comPlaysSecondAsFirstPlayer();
        } else if (mComPlayCount > 2 && mComPlaysFirst == true) {
            comPlays();
        }

        // When computer is made to play as second player
        if (mComPlayCount == 1 && mComPlaysFirst == false) {
            comPlaysFirstAsSecondPlayer();
        } else if (mComPlayCount == 2 && mComPlaysFirst == false) {
            comPlaysSecondAsSecondPlayer();
        } else if (mComPlayCount >= 3 && mComPlaysFirst == false) {
            comPlays();
        }


        if (mComPlayCount > 2 || mHumanPlayCount > 2) {
            checkForWin();
            checkForTie();
        }

    }

    /* Method handles the first play when the computer is a first player */
    public void comPlaysFirstAsFirstPlayer() {

        Random rand = new Random();
        int value = rand.nextInt(4) + 1;
        switch (value) {
            case 1:
                number = 0;
                break;
            case 2:
                number = 2;
                break;
            case 3:
                number = 6;
                break;
            case 4:
                number = 8;
                break;
        }

        TextView textviewX = (TextView) findViewById(R.id.scorePlayer1_text_view);
        textviewX.setText("Computer Scores: 0");

        String buttonID = "button_" + number;
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
        Button button = (Button) findViewById(resID);
        button.setText("X");
        mBoard[number] = "x";
        mHumanTurn = true;
        mComPlayCount++;

    }

    /* Method handles the Second play when the computer is a first player */
    public void comPlaysSecondAsFirstPlayer() {
        Button buttonCentre = (Button) findViewById(R.id.button_4);
        if (mBoard[4] == "o") {
            int position = 0;
// play at an opposite angle
            for (int i = 0; i < 9; i = i + 2) {

                if (mBoard[i] == "x") {
                    switch (i) {
                        case 0:
                            position = i + 8;
                            break;
                        case 2:
                            position = i + 4;
                            break;
                        case 6:
                            position = i - 4;
                            break;
                        case 8:
                            position = i - 8;
                            break;
                    }
                }
            }

            String buttonID = "button_" + position;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = (Button) findViewById(resID);
            button.setText("X");
            mBoard[position] = "x";
            mHumanTurn = true;
            mComPlayCount++;

        } else {
            // play at the centre of the board
            buttonCentre.setText("X");
            mBoard[4] = "x";
            mHumanTurn = true;
            mComPlayCount++;
        }

    }

    /* Method handles the first play when the computer is a second player */
    public void comPlaysFirstAsSecondPlayer() {
        Button buttonCentre = (Button) findViewById(R.id.button_4);
        if (mBoard[4] == "o") {
            // play at an opposite angle
            Random rand = new Random();
            int position = rand.nextInt(4) + 1;

            String buttonID = "button_" + position;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = (Button) findViewById(resID);
            button.setText("X");
            mBoard[position] = "x";
            mHumanTurn = true;
            mComPlayCount++;

        } else {
            // play at the centre of the board
            buttonCentre.setText("X");
            mBoard[4] = "x";
            mHumanTurn = true;
            mComPlayCount++;
        }

    }

    /* Method handles the second play when the computer is a second player */
    public void comPlaysSecondAsSecondPlayer() {
/*This module searches for worstcase and blocks them
 */
        searchRow();

        searchColumn();

        searchRightDiagonal();

        searchLeftDiagonal();

        Log.v("This is worstCase", "okpakoovie " + mWorstCase);

        if (mWorstCase < 9) {
            String buttonID = "button_" + mWorstCase;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = (Button) findViewById(resID);
            button.setText("X");
            mBoard[mWorstCase] = "x";
            mHumanTurn = true;
            mComPlayCount++;
            mWorstCase = 10;
        } else {
            int position = 0;
            for (int i = 0; i < 9; i++) {
                if (mBoard[i] == "") {
                    position = i;
                }
            }
            String buttonID = "button_" + position;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = (Button) findViewById(resID);
            button.setText("X");
            mBoard[position] = "x";
            mHumanTurn = true;
            mComPlayCount++;
        }
    }

    /* Method handles the first play when the computer is a second player */
    public void comPlays() {
/*This module searches for bestcase and worst cases for the
*third play and subsequent plays
 */
        searchRow();

        searchColumn();

        searchRightDiagonal();

        searchLeftDiagonal();

        //Log.v("This is worstCase", "updateval: " + worstCase);
        // Log.v("This is bestCase", "updateval: " + bestCase);
        //Log.v("This is blankPosition", "updateval: " + blankPosition);

        if (mBestCase < 9) {
            String buttonID = "button_" + mBestCase;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = (Button) findViewById(resID);
            button.setText("X");
            mBoard[mBestCase] = "x";
            mHumanTurn = true;
            mComPlayCount++;

        } else if (mWorstCase < 9) {
            String buttonID = "button_" + mWorstCase;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = (Button) findViewById(resID);
            button.setText("X");
            mBoard[mWorstCase] = "x";
            mHumanTurn = true;
            mComPlayCount++;

        } else {
            String buttonID = "button_" + mBlankPosition;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button button = (Button) findViewById(resID);
            button.setText("X");
            mBoard[mBlankPosition] = "x";
            mHumanTurn = true;
            mComPlayCount++;

        }

    }
/*The method handles the search for worst or best case on right diagonal*/
    public void searchRightDiagonal() {
        // Loop for checking right diagonal

        int xcount = 0;
        int ocount = 0;
        int blankpost = 0;

        for (int index = 0; index < 9; index = index + 4) {

            if (mBoard[index] == "x") {
                ++xcount;
            } else if (mBoard[index] == "o") {
                ++ocount;
            } else {
                ++blankpost;
                mBlankPosition = index;
            }

            if (xcount == 2 && blankpost == 1) {
                mBestCase = mBlankPosition;

            } else if (ocount == 2 && blankpost == 1) {
                mWorstCase = mBlankPosition;

            }
        }

    }
    /*The method handles the search for worst or best case on left diagonal*/
    public void searchLeftDiagonal() {
        // Loop for checking right diagonal

        int xcount = 0;
        int ocount = 0;
        int blankpost = 0;

        for (int index = 2; index < 7; index = index + 2) {

            if (mBoard[index] == "x") {
                ++xcount;
            } else if (mBoard[index] == "o") {
                ++ocount;
            } else {
                ++blankpost;
                mBlankPosition = index;
            }

            if (xcount == 2 && blankpost == 1) {
                mBestCase = mBlankPosition;

            } else if (ocount == 2 && blankpost == 1) {
                mWorstCase = mBlankPosition;

            }
        }

    }
    /*The method handles the search for worst or best case on each row*/
    public void searchRow() {
        // Loop for checking all rows
        for (int index = 0; index < 7; index = index + 3) {

            int index2 = index;
            int count = 1;
            int xcount = 0;
            int ocount = 0;
            int blankpost = 0;

            while (count < 4) {
                // Log.v("This is count", "searchRow: " + count);
                //  Log.v("This is index2", "searchRow: " + index2);

                if (mBoard[index2] == "x") {
                    ++xcount;
                    //   Log.v("This is board value at"+index2, "searchRow: " + Board[index2]);
                    // Log.v("This is xcount", "searchRow " + xcount);
                } else if (mBoard[index2] == "o") {
                    ++ocount;
                    //Log.v("This is board value at"+index2, "searchRow: " + Board[index2]);
                    // Log.v("This is ocount", "searchRow: " + ocount);
                } else {
                    ++blankpost;
                    mBlankPosition = index2;
                    //  Log.v("This is board value at"+index2, "searchRow: " + Board[index2]);
                    //  Log.v("This is blankpost", "searchRow: " + blankpost);
                }
                if (xcount == 2 && blankpost == 1) {
                    mBestCase = mBlankPosition;

                } else if (ocount == 2 && blankpost == 1) {
                    mWorstCase = mBlankPosition;

                }
                ++count;
                ++index2;
            }

        }

    }
    /*The method handles the search for worst or best case on each column*/
    public void searchColumn() {
        // Loop for checking all Columns
        for (int index = 0; index < 3; index++) {

            int index2 = index;
            int count = 1;
            int xcount = 0;
            int ocount = 0;
            int blankpost = 0;

            while (count < 4) {
                 if (mBoard[index2] == "x") {
                    ++xcount;
                } else if (mBoard[index2] == "o") {
                    ++ocount;
                } else {
                    ++blankpost;
                    mBlankPosition = index2;
                }

                if (xcount == 2 && blankpost == 1) {
                    mBestCase = mBlankPosition;

                } else if (ocount == 2 && blankpost == 1) {
                    mWorstCase = mBlankPosition;

                }
                ++count;
                index2 = index2 + 3;
            }
        }
    }


}// Closing braces of the class
