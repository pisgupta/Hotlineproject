package com.jaskirat.event.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jaskirat.event.tracker.com.jaskirat.event.preference.Preferenceclass;

public class AddNumberActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText inputPolice, inputFire, inputambulance;
    private TextInputLayout inputLayoutPolice, inputLayoutFire, inputLayoutAmbulance;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_number);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputLayoutPolice = (TextInputLayout) findViewById(R.id.input_layout_police);
        inputLayoutFire = (TextInputLayout) findViewById(R.id.input_layout_fire);
        inputLayoutAmbulance = (TextInputLayout) findViewById(R.id.input_layout_abbulance);

        inputPolice = (EditText) findViewById(R.id.input_police);
        inputFire = (EditText) findViewById(R.id.input_fire);
        inputambulance = (EditText) findViewById(R.id.input_ambulance);
        btnSave = (Button) findViewById(R.id.btn_signup);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validatePolice()) {
            return;
        }
        if (!validateFire()) {
            return;
        }
        if (!validateAmbulance()) {
            return;
        }
        Preferenceclass preferenceclass = new Preferenceclass(this);
        preferenceclass.setPolice(inputPolice.getText().toString().trim());
        preferenceclass.setFire(inputFire.getText().toString().trim());
        preferenceclass.setAmbulane(inputambulance.getText().toString().trim());
        preferenceclass.setFlag(true);

        Intent it = new Intent(AddNumberActivity.this, MainActivity.class);
        startActivity(it);
    }


    private boolean validatePolice() {
        if (inputPolice.getText().toString().trim().isEmpty()) {
            inputLayoutPolice.setError(getString(R.string.err_msg_police));
            requestFocus(inputPolice);
            return false;
        } else {
            inputLayoutPolice.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateFire() {
        if (inputFire.getText().toString().trim().isEmpty()) {
            inputLayoutFire.setError(getString(R.string.err_msg_deptt));
            requestFocus(inputFire);
            return false;
        } else {
            inputLayoutFire.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validateAmbulance() {
        if (inputambulance.getText().toString().trim().isEmpty()) {
            inputLayoutAmbulance.setError(getString(R.string.err_msg_ambulance));
            requestFocus(inputambulance);
            return false;
        } else {
            inputLayoutAmbulance.setErrorEnabled(false);
        }
        return true;
    }

    //
//    private boolean validateEmail() {
//        String email = inputFire.getText().toString().trim();
//
//        if (email.isEmpty() || !isValidEmail(email)) {
//            inputLayoutFire.setError(getString(R.string.err_msg_deptt));
//            requestFocus(inputFire);
//            return false;
//        } else {
//            inputLayoutFire.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//    private boolean validatePassword() {
//        if (inputambulance.getText().toString().trim().isEmpty()) {
//            inputLayoutAmbulance.setError(getString(R.string.err_msg_ambulance));
//            requestFocus(inputambulance);
//            return false;
//        } else {
//            inputLayoutAmbulance.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//    private static boolean isValidEmail(String email) {
//        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
//
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        AddNumberActivity.this.finish();
    }
}

