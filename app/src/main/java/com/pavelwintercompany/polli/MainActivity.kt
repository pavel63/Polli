package com.pavelwintercompany.polli

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.EditText
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



GlobalScope.launch {
    val notesDao = getDb()

    notesDao.noteDao().insertAll(Note(Random.nextInt(), System.currentTimeMillis().toString(), 1, "ilw", "fw"))
    val notes: List<Note> = notesDao.noteDao().getAll()

    val note = notes[0]
}

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    fun generateAlertAddDialog(){
        val factory = LayoutInflater.from(this)
        val textEntryView: View = factory.inflate(R.layout.text_entry_add_item, null)
//text_entry is an Layout XML file containing two text field to display in alert dialog
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
                    /* User clicked OK so do some stuff */
                })
            .setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, whichButton -> })
        alert.show()
    }


   suspend fun getDb(): AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "database-name"
    ).build()
}