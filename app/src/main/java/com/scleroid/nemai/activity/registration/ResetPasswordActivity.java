package com.scleroid.nemai.activity.registration;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scleroid.nemai.R;

import java.util.regex.Pattern;

public class ResetPasswordActivity extends EmailAutoCompleteActivity {

    private static final int REQUEST_READ_CONTACTS = 0;
    //TODO add Open Email App option here
    TextInputLayout mResetEmailTIL;

    private View focusView, mLoginFormView, mProgressView;
    private boolean cancel;

    public static void setSnackBar(View coordinatorLayout, String snackTitle) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, snackTitle, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        super.mEmailView = findViewById(R.id.email_reset);
        addListenerToEmailView();
        mResetEmailTIL = findViewById(R.id.reset_email_text_input_layout);
        mLoginFormView = findViewById(R.id.reset_form);
        //     mProgressView = findViewById(R.id.reset_progress);

        populateAutoComplete();
        Button mResetPassword = findViewById(R.id.reset_password_button);
        mResetPassword.setOnClickListener(v -> attemptResetPassword());
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptResetPassword() {

        //process the data further


        mResetEmailTIL.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mResetEmailTIL.setError(getString(R.string.error_field_required_mobile));
            focusView = mEmailView;
            cancel = true;
        } else if (!isValidField(email)) {
            mResetEmailTIL.setError(getString(R.string.error_invalid_mobile_only));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //Toast.makeText(this, "Validation Successful", Toast.LENGTH_LONG).show();

            findViewById(R.id.reset_text_view).setVisibility(View.VISIBLE);
            findViewById(R.id.reset_image_view).setVisibility(View.VISIBLE);
            showProgress(true);
            if (email.contains("@")) {
                resetPassword(email);
                Button mLoginButton = findViewById(R.id.back_to_login_button);
                mLoginButton.setVisibility(View.VISIBLE);
                mLoginButton.setOnClickListener(v -> {
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                });
            } else {
                Intent intent = new Intent(ResetPasswordActivity.this, OtpVerificationActivity.class);
                intent.putExtra(SocialRegisterActivity.INTENT_PHONENUMBER, email);
                intent.putExtra(SocialRegisterActivity.INTENT_COUNTRY_CODE, "91");
                intent.putExtra(SocialRegisterActivity.INTENT_REASON, true);
                startActivity(intent);
            }

        }


    }

    private void resetPassword(String email) { /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
        // Tag used to cancel the request


    /*
        if (isNetworkAvailable(getApplicationContext())) {
            String tag_string_req = "req_reset";
            userEmail = email;

            showProgress(true);

            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                    ServerConstants.serverUrl.POST_RESET, null, new Response.Listener<JSONObject>() {
                @SuppressLint("LongLogTag")

                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d(TAG, "Login Response: " + jsonObject.toString());
                    showProgress(false);

                    try {


                        // Create login session


                        //JSONObject jObj = new JSONObject(jsonObject);

                        boolean error = jsonObject.getBoolean("error");
                        if (!error) {


                            Toast.makeText(getApplicationContext(), "UserProfile successfully logged in", Toast.LENGTH_LONG).show();

                            session.setLogin(true);

                            Intent verification = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(verification);
                            finish();
                        } else {

                            // Error occurred in login. Get the error
                            // message

                            session.setLogin(false);
                            String errorMsg = jsonObject.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "" + e, Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {

                @SuppressLint("LongLogTag")
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Registration Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    showProgress(false);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);


                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else
            Toast.makeText(getApplicationContext(), "Network not connected, try again", Toast.LENGTH_LONG).show();

*/
        showProgress(false);
    }

    private boolean isValidField(String input) {

       /* if (input.contains("@")) {
            Pattern regex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

            return regex.matcher(input).matches();
        } else {*/
            Pattern regex = Pattern.compile("^[789]\\d{9}$");

            return regex.matcher(input).matches();

//        }

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}
