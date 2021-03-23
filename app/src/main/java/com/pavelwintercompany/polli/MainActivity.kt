package com.pavelwintercompany.polli

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.pavelwintercompany.polli.data.AppDatabase
import com.pavelwintercompany.polli.data.Note
import com.pavelwintercompany.polli.databinding.ContentMainBinding
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ContentMainBinding

    private lateinit var appBarConfiguration: AppBarConfiguration

    private var currentHash = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       /* binding = ContentMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)*/


       setContentView(R.layout.activity_main)


       val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            /*  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                      .setAction("Action", null).show()*/
            generateAlertAddDialog()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )

        val questionTv : TextView = findViewById(R.id.questionView)
        val trueAnswerTv : TextView = findViewById(R.id.answerTrueTv)

        val buttonCheck : Button = findViewById(R.id.checkBtn)

        val buttonGood : Button =findViewById(R.id.buttonGood)
        val buttonNorm : Button =findViewById(R.id.buttonNorm)
        val buttonBad : Button =findViewById(R.id.buttonBad)
        val buttonNext : Button =findViewById(R.id.nextBtn)

        buttonBad.setOnClickListener {setNoteResult(1)  }
        buttonNorm.setOnClickListener { setNoteResult(2) }
        buttonGood.setOnClickListener { setNoteResult(3) }

        buttonCheck.setOnClickListener {

            GlobalScope.launch {
                val answer = getNextModel()?.answer

                GlobalScope.launch(Dispatchers.Main) {
                    trueAnswerTv.text=answer
                }
                }

          //  Toast.makeText(this, "fdfdf", Toast.LENGTH_SHORT).show()
        }

        buttonNext.setOnClickListener {
            GlobalScope.launch {
                showNextNote(questionTv)
            }
        }


        GlobalScope.launch {
            showNextNote(questionTv)
        }

    }


    fun setNoteResult(settedBall : Int){


        GlobalScope.launch {

            val note = getNextModel()?.copy(rating = settedBall)


            val notesDao = getDb()

            note?.let {
                notesDao.noteDao().update(
                    it
                )
            }
            //val notes: List<Note> = notesDao.noteDao().getAll()

            // val note = notes[0]
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    fun generateAlertAddDialog() {
        val factory = LayoutInflater.from(this)
        val textEntryView: View = factory.inflate(R.layout.text_entry_add_item, null)
//text_entry is an Layout XML file containing two text field to display in alert dialog
        val input1 = textEntryView.findViewById(R.id.alertDialogQuestionEt) as EditText
        val input2 = textEntryView.findViewById(R.id.alertDialogAnswerEt) as EditText
        val alert: AlertDialog.Builder = AlertDialog.Builder(this)

        alert/*setIcon(R.drawable.icon)*/
            .setTitle(getString(R.string.question_answer_example))
            .setView(textEntryView)
            .setPositiveButton(getString(R.string.save),
                DialogInterface.OnClickListener { dialog, whichButton ->
                    Log.i("AlertDialog", "Question Entered " + input1.text.toString())
                    Log.i("AlertDialog", "Answer Entered " + input2.text.toString())

                    addNote(input1.text.toString(), input2.text.toString())

                    /* User clicked OK so do some stuff */
                })
            .setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, whichButton -> })
        alert.show()
    }

    fun addNote(question: String, answer: String) {

        GlobalScope.launch {
            val notesDao = getDb()

            notesDao.noteDao().insertAll(
                Note(
                    Random.nextInt(),
                    System.currentTimeMillis().toString(),
                    1,
                    question,
                    answer
                )
            )
            //val notes: List<Note> = notesDao.noteDao().getAll()

            // val note = notes[0]
        }

    }

     suspend fun getDb(): AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "database-name"
    ).build()


    suspend fun getNextModel(): Note? = getDb().noteDao().getAll().sortedBy { it.rating }.firstOrNull()

    suspend fun showNextNote(questionTv : TextView){
      val nextNote = getNextModel()

        GlobalScope.launch(Dispatchers.Main) {
          questionTv.text=nextNote?.question
            currentHash = nextNote?.hash ?:""
        }

    }

}
