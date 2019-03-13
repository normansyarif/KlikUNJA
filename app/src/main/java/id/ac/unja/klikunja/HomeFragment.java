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

        CardView cardNews = view.findViewById(R.id.card_news);
        CardView cardOpini = view.findViewById(R.id.card_opini);
        CardView cardNotices = view.findViewById(R.id.card_notices);
        CardView cardEvents = view.findViewById(R.id.card_events);
        CardView cardContact = view.findViewById(R.id.card_contact);


        cardNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(NewsActivity.class);
            }
        });

        cardOpini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(OpiniActivity.class);
            }
        });

        cardNotices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(NoticesActivity.class);
            }
        });

        cardEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(EventsActivity.class);
            }
        });

        cardContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ContactActivity.class);
            }
        });

    }

    private void openActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}

