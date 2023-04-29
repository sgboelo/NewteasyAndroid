package com.SmartTech.teasyNew;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.SmartTech.teasyNew.activity.MainActivity;
import com.codingending.popuplayout.PopupLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakePayment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakePayment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View parentView;
    MainActivity activity;
    Session session;


    public MakePayment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MakePayment.
     */
    // TODO: Rename and change types and number of parameters
    public static MakePayment newInstance(String param1, String param2) {
        MakePayment fragment = new MakePayment();
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
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_make_payment, container, false);
        activity =(MainActivity) getActivity();
        session = activity.getSession();
        CardView frcs,nis;
        frcs = parentView.findViewById(R.id.frsc);
        nis = parentView.findViewById(R.id.nis);
        frcs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakePaymentInplementation makePaymentInplementation =
                        new MakePaymentInplementation(activity, false);
                PopupLayout popupLayout =  makePaymentInplementation.applicationID(container, getContext());
                makePaymentInplementation.getApplicationID().setHint("Enter FRSC Application ID");
                popupLayout.setUseRadius(true);
                popupLayout.show(PopupLayout.POSITION_BOTTOM);

            }
        });

        nis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakePaymentInplementation makePaymentInplementation =
                        new MakePaymentInplementation(activity, true);
                PopupLayout popupLayout =  makePaymentInplementation.applicationID(container, getContext());
                makePaymentInplementation.getApplicationID().setHint("Enter NIS Application ID");
                popupLayout.setUseRadius(true);
                popupLayout.show(PopupLayout.POSITION_BOTTOM);

            }
        });
        return parentView;
    }


}