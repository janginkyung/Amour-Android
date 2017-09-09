package orgm.androidtown.amour;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by InKyung on 2017-09-09.
 */

public class SignupActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN =10 ;
    Button loginButton,googleButton,findPassword,signupButton;
    EditText userName, password;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private String TAG="GOOGLELOGIN";

    @Override
    protected void onStart() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);//로그인 안되어있을 경우 진행할 화면을 의미하는듯
        super.onStart();
    }

    public void updateUI(FirebaseUser currentUser){
        Intent intent=new Intent(SignupActivity.this,SignupCoupleActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
        GoogleSignIn();
    }

    public void init(){
        userName=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);

        loginButton=(Button)findViewById(R.id.loginButton);
        googleButton=(Button)findViewById(R.id.googleButton);
        findPassword=(Button)findViewById(R.id.forgetPasswordButton);
        signupButton=(Button)findViewById(R.id.signupButton);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


    }

    private void GoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder
                (GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                savePreferences(account.getEmail());
                firebaseAuthWithGoogle(account);
            } else {
                Log.d(TAG, String.valueOf(result.getStatus()));
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),"인증이 되지 않았습니다.",Toast.LENGTH_LONG);
    }

    // 값 저장하기
    private void savePreferences(String email){
        Log.d("ㄴㄴㄴ", "savePreferences 호출");
        SharedPreferences pref = getSharedPreferences("GoogleLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("hi", email);
        editor.commit();
    }

}
