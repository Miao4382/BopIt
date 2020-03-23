package com.example.bopit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.game_layout.*
import kotlinx.android.synthetic.main.multi_layout.*

class GameCenter : AppCompatActivity() {

    val database = Firebase.database
    var username: String = ""
    var opponent: String = ""

    private var dbRef: DatabaseReference = Firebase.database.reference

    private val challengeList: MutableList<String> = mutableListOf()


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


    fun addChallenge(username: String?) {

        //Check if the challenge already exists
            //if so, don't create the challenge
            //if not, create the challenge

        //Start the game

        //Record the score
        opponent = opponentName.text.toString()

        val myRef = database.getReference("challenges/$username+$opponent")

        myRef.setValue("$username+$opponent")

    }

    fun display_challenges() {
        val challengeListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                challengeList.clear()
                dataSnapshot.children.mapNotNullTo(challengeList) { it.getValue<String>(String::class.java) }
                for ((index, item) in challengeList.withIndex()) {
                    if (item.contains(username))
                        println("YES! CHALLENGE!")
                        //Show challenges view text view

                        //NEED TO FIGURE OUT HOW TO DO THIS

                        //val constraintLayout = findViewById(R.id.constraintLayout) as ConstraintLayout

                        /*val textview = TextView(this@GameCenter)
                        textview.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                        textview.text = item
                        //button.setId(index)

                        constraintLayout.addView(textview)*/

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        dbRef.child("challenges").addValueEventListener(challengeListener)

    }

}