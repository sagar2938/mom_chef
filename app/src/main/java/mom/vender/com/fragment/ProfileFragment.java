package mom.vender.com.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import mom.vender.com.activity.ContactAdminActivity;
import mom.vender.com.activity.ContactDetailActivity;
import mom.vender.com.R;
import mom.vender.com.activity.LogInActivity;
import mom.vender.com.history_invice.HistoryDetailActivity;
import mom.vender.com.utils.Preferences;

public class ProfileFragment  extends Fragment {
    CardView profile;
    ImageView image;
    TextView name;
    TextView email;
    TextView mobile;
    RelativeLayout logout,history_invice;



    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_menu, null);

        profile = view.findViewById(R.id.profile);
        image = view.findViewById(R.id.image);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        mobile = view.findViewById(R.id.mobile);
        logout = view.findViewById(R.id.logout);
        history_invice=view.findViewById(R.id.history_invice);
        name.setText(Preferences.getInstance(getContext()).getFirstName()+" "+Preferences.getInstance(getContext()).getMiddleName()+" "+Preferences.getInstance(getContext()).getLastName());
        email.setText(Preferences.getInstance(getContext()).getEmail());
        mobile.setText(Preferences.getInstance(getContext()).getMobileNumber());
        Glide.with(this)
                .load(Preferences.getInstance(getContext()).getProfileImage())
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .apply(RequestOptions.skipMemoryCacheOf(true))
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(new RequestOptions().placeholder(R.drawable.user))
                .into(image);
        history_invice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), HistoryDetailActivity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ContactDetailActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.getInstance(getActivity()).setLogin(false);
                Intent trackingActivity = new Intent(getActivity(), LogInActivity.class);
                trackingActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(trackingActivity);
            }
        });


        view.findViewById(R.id.rateOnStore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=mom.vender.com"));
                startActivity(i);
            }
        });


        view.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sent from MOM app");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Signup now on MOM and order delicious MOM COOKED FOOD. Use this link and get up to 50% off. https://play.google.com/store/apps/details?id=mom.vender.com");
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        view.findViewById(R.id.aboutUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://delhi-1018.appspot.com/#!/main/about"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.contactUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ContactAdminActivity.class));
            }
        });
        view.findViewById(R.id.tnc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://delhi-1018.appspot.com/#!/main/terms-con"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://delhi-1018.appspot.com/#!/main/privacy-policy"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        return  view ;
    }
}
