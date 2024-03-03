package com.jc.jcsports.activities.phoneFunctions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jc.jcsports.activities.signupFunctions.SignUpActivity;
import com.jc.jcsports.bundle.SignUpBundle;
import com.jc.jcsports.utils.Utils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class PhoneFunctions {

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    public PhoneNumberFormattingTextWatcher phoneNumberFormattingTextWatcher;
    private final String authenticValidation = "^[a-zA-Z0-9]*$";
    public Pattern ps;
    public TextWatcher authenticTextWatcher;
    public String mVerificationId = "empty", smsCode = "empty";
    public PhoneAuthProvider.ForceResendingToken mResendToken;
    public PhoneAuthCredential copyCredential;
    private Button authenticButton, checkAuthenticButton;

    public void setAuthenticButton(Button authenticButton) {
        this.authenticButton = authenticButton;
    }

    public void setCheckAuthenticButton(Button checkAuthenticButton) {
        this.checkAuthenticButton = checkAuthenticButton;
    }

    public PhoneFunctions(Context context, EditText authenticEditText) {
        authenticTextWatcherInit(context, authenticEditText);
        patternInit();
        phoneNumberTextWatcherInit();
        setCallbacks(context);
        mAuthInit();
    }


    //todo : mAuthInit
    private void mAuthInit() {
        mAuth = FirebaseAuth.getInstance();
    }

    //todo : 폰 형식 editText 세팅
    private void phoneNumberTextWatcherInit() {
        phoneNumberFormattingTextWatcher = new PhoneNumberFormattingTextWatcher();
    }

    //todo : 커스텀 인증번호 형식 editText 세팅
    private void patternInit() {
        ps = Pattern.compile(authenticValidation);
    }

    //todo : 문자인증 형식 TextWatcher 세팅
    private void authenticTextWatcherInit(Context context, EditText authenticEditText) {
        authenticTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().replaceAll(" ", "").trim();
                if (!checkValidation(text)) {
                    authenticEditText.setText(text.substring(0, text.length() - 1));
                    authenticEditText.setSelection(text.length() - 1);
                    Toast.makeText(context, "공백 및 특수문자 등 입력이 불가합니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    //todo : 문자인증 유효성 검사
    private boolean checkValidation(String authentic) {
        return ps.matcher(authentic).matches();
    }


    //todo : 문자인증 정규식 기능 필터
    public void getAuthenticSmsCode(PhoneAuthenticActivity activity, String text) {
        String nationNumber = "+82 ";
        String regEx = "(\\d{3})-(\\d{3,4})-(\\d{4})";
        String phoneNumber = text;
        phoneNumber = phoneNumber.replaceAll(regEx, "$1-$2-$3");
        boolean result = Pattern.matches(regEx, phoneNumber.trim());
        if (result) {
            String customPhoneNumber = nationNumber + phoneNumber;
            String buttonText = authenticButton.getText().toString();
            if (buttonText.equals("인증번호 재발송")) {
                reSendVerificationCode(activity, customPhoneNumber, mResendToken);
            } else {
                sendVerificationCode(activity, customPhoneNumber);
            }
            Toast.makeText(activity, "인증번호를 발송했습니다. \n잠시만 기다려주세요.", Toast.LENGTH_LONG).show();
            authenticButton.setText("인증번호 발송중...");
            authenticButton.setBackgroundColor(Color.LTGRAY);
            authenticButton.setEnabled(false);
        } else {
            Toast.makeText(activity, "전화번호를 형식에 맞게 입력하세요.", Toast.LENGTH_SHORT).show();
        }
    }


    //todo : 문자인증 정규식 형식 판별
    public void checkAuthentic(PhoneAuthenticActivity activity, Context context, String writtenCode) {
        if (smsCode.equals(writtenCode)) {
            checkAuthenticButton.setText("인증 확인중...");
            checkAuthenticButton.setBackgroundColor(Color.LTGRAY);
            checkAuthenticButton.setEnabled(false);
            signInWithPhoneAuthCredential(context, activity, copyCredential);
        } else {
            Toast.makeText(context, "인증번호가 다릅니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    //todo : 전화번호 형식 관련 파이어베이스 환경설정 세팅
    private void setCallbacks(Context context) {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Toast.makeText(context, "인증번호가 도착했습니다. 60초 이내로 입력해주세요.", Toast.LENGTH_SHORT).show();
                smsCode = credential.getSmsCode();
                copyCredential = credential;
                authenticButton.setText("인증번호 재발송");
                authenticButton.setBackgroundColor(Color.WHITE);
                authenticButton.setEnabled(true);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Utils.logCheck("onVerificationFailed : " + e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(context, "인증을 실패했습니다.", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(context, "인증 서버에 문제가 발생했습니다.\n나중에 다시 시도해 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                }
                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.e("abc", "onCodeSent:" + verificationId);
                // Save verification ID and resending token so we can use them later
                Utils.logCheck("verificationId : " + verificationId + " , token : " + token);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

    }

    private void sendVerificationCode(PhoneAuthenticActivity activity, String changeMyPhoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(changeMyPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void reSendVerificationCode(PhoneAuthenticActivity activity, String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(Context context, PhoneAuthenticActivity activity, PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        checkAuthenticButton.setText("인증 완료");
                        Toast.makeText(context, "인증 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = task.getResult().getUser();
                        // Update UI
                        Intent intent = new Intent(activity, SignUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        SignUpBundle.getBundle().putString("s_PhoneNumber", "010-1234-5678");
                        activity.startActivity(intent);
                        activity.finish();
                    } else {
                        checkAuthenticButton.setText("재인증");
                        Log.e("abc", "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                        Toast.makeText(context, "인증 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    checkAuthenticButton.setBackgroundColor(Color.WHITE);
                    checkAuthenticButton.setEnabled(true);
                });
    }
}
