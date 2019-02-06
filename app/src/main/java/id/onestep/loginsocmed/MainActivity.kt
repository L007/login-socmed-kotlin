package id.onestep.loginsocmed

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {
    val RC_SIGN_IN: Int = 1
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupGoogle()
        setupFb()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data) //penting

        if (requestCode == RC_SIGN_IN) {
            val result: GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignResult(result)
        }

    }

    private fun handleSignResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            val account: GoogleSignInAccount? = result.signInAccount
            Log.d("account : ", "nama : ${account?.displayName} email : ${account?.email}")
            val intent: Intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("datagoogle", account)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT)
        }
    }

    fun setupGoogle() {
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val gsoClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)

        btn_sign_google.setOnClickListener {
            val signIntent: Intent = gsoClient.signInIntent
            startActivityForResult(signIntent, RC_SIGN_IN)

        }
    }

    fun setupFb() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                //Log.d("response FB : ",result?.accessToken?.token)
                fbGraphReq(result?.accessToken!!)


            }

            override fun onCancel() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(error: FacebookException?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })



    }

    fun fbGraphReq(accessToken: AccessToken) {
        val request: GraphRequest =
            GraphRequest.newMeRequest(accessToken, object : GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(`object`: JSONObject?, response: GraphResponse?) {
                    val intent = Intent(applicationContext, ProfileActivity::class.java)
                    if (response != null) {
                        try {
                            val data: JSONObject? = response?.jsonObject
                            //Log.d("response object ", data.toString())
                            intent.putExtra("datafb",data?.toString())
                            startActivity(intent)
                        } catch (e: Exception) {
                            Log.d("pesan error", e.message)
                        }
                    }


                }

            })

        val param = Bundle()
        param.putString("fields", "id,name,email");
        request.parameters = param
        request.executeAsync()
    }
}
