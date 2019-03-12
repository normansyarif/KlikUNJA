package id.ac.unja.klikunja;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MessageFragment extends Fragment {

    Toolbar mToolbar;
    TextInputLayout textInputSubject;
    TextInputLayout textInputMessage;
    Button btnSend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        mToolbar = view.findViewById(R.id.msg_toolbar);
        textInputSubject = view.findViewById(R.id.text_input_subject);
        textInputMessage = view.findViewById(R.id.text_input_message);
        btnSend = view.findViewById(R.id.btn_send);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Send a message to us");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateSubject() | !validateMessage()) {
                    return;
                }

                String subject = textInputSubject.getEditText().getText().toString();
                String message = textInputMessage.getEditText().getText().toString();

                if(isNetworkAvailable()) {
                    sendEmail(subject, message);
                } else {
                    Toast.makeText(getActivity(), "You're offline. Check your connection.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private boolean validateSubject() {
        String subjectInput = textInputSubject.getEditText().getText().toString().trim();

        if (subjectInput.isEmpty()) {
            textInputSubject.setError("Field can't be empty");
            return false;
        } else {
            textInputSubject.setError(null);
            return true;
        }
    }

    private boolean validateMessage() {
        String messageInput = textInputMessage.getEditText().getText().toString().trim();

        if (messageInput.isEmpty()) {
            textInputMessage.setError("Field can't be empty");
            return false;
        } else {
            textInputMessage.setError(null);
            return true;
        }
    }

    private void sendEmail(String subject, String text) {
        String uriText =
                "mailto:humas@unja.ac.id" +
                        "?subject=" + Uri.encode(subject) +
                        "&body=" + Uri.encode(text);

        Uri uri = Uri.parse(uriText);

        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);

        try {
            startActivity(Intent.createChooser(sendIntent, "Send email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
