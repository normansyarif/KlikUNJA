package id.ac.unja.klikunja;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        CardView card_survey = view.findViewById(R.id.card_survey);
        CardView card_repo = view.findViewById(R.id.card_repo);
        CardView card_borang = view.findViewById(R.id.card_borang);
        CardView card_simpeg = view.findViewById(R.id.card_simpeg);
        CardView card_dss = view.findViewById(R.id.card_dss);
        CardView card_journal = view.findViewById(R.id.card_journal);

        card_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser("survey");
            }
        });

        card_repo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser("repo");
            }
        });

        card_borang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser("borang");
            }
        });

        card_simpeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser("simpeg");
            }
        });

        card_dss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser("dss");
            }
        });

        card_journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser("journal");
            }
        });

    }

    private void openBrowser(String category) {
        Intent intent = new Intent(getActivity(), BrowserActivity.class);
        intent.putExtra("category", category);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}

