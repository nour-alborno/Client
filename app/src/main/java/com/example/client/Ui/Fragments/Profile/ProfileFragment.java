
package com.example.client.Ui.Fragments.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.client.Model.Benefeciares;
import com.example.client.R;
import com.example.client.Ui.Activities.ContactUs.ContactUsActivity;
import com.example.client.Ui.Activities.EditProfile.EditProfileActivity;
import com.example.client.Ui.Activities.History.HistoryActivity;
import com.example.client.Ui.Activities.Login.LoginActivity;
import com.example.client.Ui.Activities.about_us.AboutUsActivity;
import com.example.client.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements ProfileView{

    Benefeciares benefeciares;

    SharedPreferences sp;
    public final String CLIENT_ID_KEY = "clientId";

    ProfilePresenter presenter;
    FragmentProfileBinding binding;
    String id;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         binding = FragmentProfileBinding.inflate(inflater,container,false);

       presenter = new ProfilePresenter(this);


        sp =requireActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
         id = sp.getString(CLIENT_ID_KEY,null);


        presenter.benfInfo(id);

        presenter.gettingProfileImage(id);

        binding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class).putExtra("userInfo",benefeciares));
            }
        });
         binding.linLayoutHistory.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(getActivity(), HistoryActivity.class));
             }
         });


         binding.tvContactus.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(getActivity(), ContactUsActivity.class));
             }
         });

         binding.linLayoutLogout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 FirebaseAuth.getInstance().signOut();
                 startActivity(new Intent(getActivity(), LoginActivity.class));
                 getActivity().finish();
             }
         });

         binding.linLayoutAboutUs.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(getActivity(), AboutUsActivity.class));
             }
         });



        return binding.getRoot();
    }

    @Override
    public void onbenfInfoSuccess(Benefeciares benefeciares) {
        Log.d("Beneficiaries",benefeciares.toString());
        this.benefeciares = benefeciares;
        binding.tvName.setText(benefeciares.getName());
        binding.tvNumber.setText("+970".concat(String.valueOf(benefeciares.getMobile())));

    }

    @Override
    public void onbenfInfoFailure(Exception e) {
        Log.d("Beneficiaries",e.getMessage());
    }

    @Override
    public void onGettingImgeSuccess(String img) {


        if(!isAdded() || requireActivity() == null)
            return;

        Glide.with(requireActivity()).load(img)
                    .into(binding.imgProfile);


        Log.d("Success",img);
    }

    @Override
    public void onGettingImgFailure(Exception e) {
        binding.imgProfile.setImageResource(R.drawable.profile_avtar);
        Log.d("FailureImg",e.getMessage());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isAdded()) return;

        presenter.gettingProfileImage(id);

    }
}