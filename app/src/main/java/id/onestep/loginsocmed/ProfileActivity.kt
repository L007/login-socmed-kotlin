package id.onestep.loginsocmed

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        getData()

        btn_signOut.setOnClickListener {
            signOut()
        }
    }

    fun getData(){
        val account : GoogleSignInAccount? = intent.getParcelableExtra("account")

        Glide.with(this)
            .load(account?.photoUrl)
            .apply(RequestOptions().circleCrop())
            .into(img_profile)

        txt_username.setText("Welcome Back, ${account?.displayName}")
    }

    fun signOut(){
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val gsoClient: GoogleSignInClient = GoogleSignIn.getClient(this,gso)
        gsoClient.signOut()
            .addOnCompleteListener(this) {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }

    }
}
