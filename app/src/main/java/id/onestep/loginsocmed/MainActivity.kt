package id.onestep.loginsocmed

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val RC_SIGN_IN:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupGoogle()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==RC_SIGN_IN){
            val result : GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignResult(result)
        }

    }

    private fun handleSignResult(result: GoogleSignInResult) {
        if (result.isSuccess){
            val account : GoogleSignInAccount? = result.signInAccount
            Log.d("account : ", "nama : ${account?.displayName} email : ${account?.email}")
            val intent : Intent = Intent(this,ProfileActivity::class.java)
            intent.putExtra("account",account)
            startActivity(intent)
        }
        else{
            Toast.makeText(this,"Login Gagal",Toast.LENGTH_SHORT)
        }
    }

    fun setupGoogle(){
        val gso:GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val gsoClient:GoogleSignInClient = GoogleSignIn.getClient(this,gso)

        btn_sign_google.setOnClickListener {
            val signIntent : Intent = gsoClient.signInIntent
            startActivityForResult(signIntent,RC_SIGN_IN)

        }
    }
}
