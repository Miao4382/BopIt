/*
The "Game Center" Page

This page displays all the current games and scores the user has between other people

The user, at the top of the page, enters the specific username of the person
they want to play. The button will then launch the game.

 */


package com.example.bopit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.game_layout.*


class GameCenter : AppCompatActivity() {

    val database = Firebase.database
    var username: String = ""
    var opponent: String = ""

    private var dbRef: DatabaseReference = Firebase.database.reference

    private val challengeList: MutableList<String> = mutableListOf()
    private val displaychallengeList: MutableList<String> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_layout)

        val intent = intent
        username = intent.getStringExtra("username")

        display_challenges()

        btnChallenge.setOnClickListener{
            addChallenge(username)
        }
    }


    fun addChallenge(username: String) {

        //Check if the challenge already exists
            //if so, don't create the challenge
            //if not, create the challenge
        opponent = opponentName.text.toString()

        //Create a ValueEventListener to read from the database
        val challengeListener = object : ValueEventListener {
            //On datachange, get a datasnapshot
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                challengeList.clear()
                //Map the data into a list
                dataSnapshot.children.mapNotNullTo(challengeList) { it.getValue<String>(String::class.java) }
                for ((index, item) in challengeList.withIndex()) {
                    if (item.contains(username) && item.contains(opponent)) {
                        //the challenge already exists
                    } else {
                        //create the challenge between them
                        val myRef = database.getReference("challenges/$username+$opponent")
                        myRef.setValue("$username+0+$opponent+0")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        dbRef.child("challenges").addListenerForSingleValueEvent(challengeListener)


        start_game()
    }

    fun display_challenges() {
        //Displays the current games in text views

        var counter = 0
        val displaychallengeListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                displaychallengeList.clear()
                dataSnapshot.children.mapNotNullTo(displaychallengeList) { it.getValue<String>(String::class.java) }
                for ((index, item) in displaychallengeList.withIndex()) {
                    if (item.contains(username)) {

                        //Parse the string to get the usernames and scores
                        val plus = item.indexOf("+")
                        var firstuser = item.substring(0, plus)
                        val plus2 = item.indexOf("+", plus+1)
                        var firstscore = item.substring(plus+1, plus2)
                        val plus3 = item.indexOf("+", plus2+1)
                        var seconduser = item.substring(plus2+1, plus3)
                        var secondscore = item.substring(plus3+1, item.length)
                        var mytext = "GAME -- $firstuser's score: $firstscore ~ $seconduser's score: $secondscore"

                        //Set the text of the text views
                        when (counter) {
                            0 -> tv0.setText(mytext)
                            1 -> tv1.setText(mytext)
                            2 -> tv2.setText(mytext)
                            3 -> tv2.setText(mytext)
                            4 -> tv2.setText(mytext)
                            5 -> tv2.setText(mytext)
                            6 -> tv2.setText(mytext)
                        }
                        counter++
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        dbRef.child("challenges").addValueEventListener(displaychallengeListener)
    }

    fun start_game() {
        //Launches the "multigame" activity, which is just solo player but with firebase stuff in it
        val intent = Intent(this, MultiGame::class.java)
        intent.putExtra("username", username)
        intent.putExtra("opponent", opponent)
        startActivity(intent)
    }

}