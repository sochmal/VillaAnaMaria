package com.villanamaria.app.villaanamara;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final String TAG = "Login" ;
    SharedPreferences spLogin;
    //String strServiror="http://tucuenca.com/anam/";
    String strServiror="http://192.168.6.229/villa/account/prog/tablet/";
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private static final String DUMMY_USER_ID = "user";
    private static final String DUMMY_PASSWORD = "user";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView,txtServer;
    private View mProgressView;
    private View mLoginFormView;
    String usuario, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_login);
            // Set up the login form.
            mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
            //populateAutoComplete();
            spLogin=getSharedPreferences("login",MODE_PRIVATE);
            mPasswordView = (EditText) findViewById(R.id.password);
            txtServer = (EditText) findViewById(R.id.txtServer);
            strServiror=txtServer.getText().toString();
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                        //attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    usuario=mEmailView.getText().toString();
                    //attemptLogin();
                }
            });

            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
        }catch (Exception e){
            Log.e(TAG, "onCreate: ", e);
        }

    }




    public void invokeLogin(View view){
        usuario = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        Log.i(TAG, "doInBackground: click sendpost");
        new SendPostRequest().execute();
        /*if(username.equals("demo") && password.equals("demos"))
        {
            Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
            intent.putExtra(USER_NAME, username);
            finish();
            startActivity(intent);

        }else{
            Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
        }*/

        //login(username,password);

    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},
                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
    public void ingresar(){
        Intent intent = new Intent(this, MenuLugares.class);
        SharedPreferences.Editor e=spLogin.edit();
        e.putString("servidor",txtServer.getText().toString());
        e.putString("username",usuario);
        //e.putString("password",password);
        e.commit();
        //  intent.putExtra(USER_NAME, username);
        finish();
        startActivity(intent);
    }

    public class SendPostRequest extends AsyncTask<String, Void, String>{
        private Dialog loadingDialog;
        protected void OnPreExecute(){
            loadingDialog = ProgressDialog.show(LoginActivity.this, "Please wait", "Loading...");
        }
        @Override
        protected String doInBackground(String... params) {
            String result=null;
            try{
                URL url=new URL(strServiror+"login.php");
                JSONObject posdataParams=new JSONObject();
                posdataParams.put("nombre","mast");
                posdataParams.put("password",password);
                Log.i(TAG, "doInBackground: click sendpost");

                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                //milisegundos
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os= conn.getOutputStream();
                BufferedWriter writer=new BufferedWriter(
                        new OutputStreamWriter(os,"UTF-8"));
               // writer.write(getPosData(posdataParams));
                writer.write("usuario=mast");
                writer.flush();
                os.close();

                int responseCode=conn.getResponseCode();
                if(responseCode==HttpURLConnection.HTTP_OK){
                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb= new StringBuffer("");
                    String line="";

                    while((line=in.readLine())!=null){
                        sb.append(line);
                        break;
                    }
                    in.close();
                    result= sb.toString();
                }

            }catch (Exception e){
                result= new String("Exception: " + e.getMessage());

            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            String s = result.trim();
            loadingDialog.dismiss();
            JSONObject jsobj= null;
            try {
                jsobj = new JSONObject(result);
            //JSONArray jsalogin=jsobj.getJSONArray("datalogin");
            Log.i(TAG, "onPostExecute: "+jsobj.length());
            String key= jsobj.getString("key");
                Log.i(TAG, "onPostExecute: key login "+key);
                if(s.equalsIgnoreCase("true")){
                      /*  Intent intent = new Intent(ActivityLogin.this, UserProfile.class);
                        intent.putExtra(USER_NAME, username);
                        finish();
                        startActivity(intent);*/
                    Toast.makeText(getApplicationContext(), "Acceso", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Datos Incorrectos", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* Toast.makeText(getApplication(), result,
                    Toast.LENGTH_LONG).show();*/
        }
    }
}

