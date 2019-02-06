package id.onestep.loginsocmed

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_profile.*
import org.json.JSONObject

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        if (intent.getStringExtra("datafb") != null) {

            getDataFb()
        } else {
            getData()

        }

        btn_signOut.setOnClickListener {
            signOut()
        }
    }

    fun getData() {
        //var dataFb: JSONObject? = JSONObject(intent.getStringExtra("datafb"))
        var dataGoogle: GoogleSignInAccount? = intent.getParcelableExtra("datagoogle")

        //Log.d("data fb", dataFb.toString())
        Log.d("data google", dataGoogle.toString())

        if (dataGoogle != null) {

            Glide.with(this)
                .load(dataGoogle?.photoUrl)
                .apply(RequestOptions().circleCrop())
                .into(img_profile)

            txt_username.setText("Welcome Back, ${dataGoogle?.displayName}")
        } /*else if (dataFb != null) {
            Glide.with(this)
                .load("https://graph.facebook.com/${dataFb?.getString("id")}/picture?type=large")
                .apply(RequestOptions().circleCrop())
                .into(img_profile)

            txt_username.setText("Welcome Back, ${dataFb?.getString("name")}")
        }*/

    }

    fun getDataFb() {
        var dataFb: JSONObject? = JSONObject(intent.getStringExtra("datafb"))


        Log.d("data fb", dataFb.toString())

        if (dataFb != null) {
            Glide.with(this)
                .load("https://graph.facebook.com/${dataFb?.getString("id")}/picture?type=large")
                .apply(RequestOptions().circleCrop())
                .into(img_profile)

            txt_username.setText("Welcome Back, ${dataFb?.getString("name")}")
        }

    }

    fun signOut() {
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val gsoClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
        gsoClient.signOut()
            .addOnCompleteListener(this) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                GraphRequest.Callback {
                    AccessToken.setCurrentAccessToken(null)
                    LoginManager.getInstance().logOut()
                    finish()
                }).executeAsync()
        }


    }
}
