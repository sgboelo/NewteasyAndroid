package com.SmartTech.teasyNew;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.SmartTech.teasyNew.model.BankTransferModel;
import com.codingending.popuplayout.PopupLayout;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRcode#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRcode extends Fragment implements ZXingScannerView.ResultHandler {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ZXingScannerView mScannerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewGroup mGroup;

    public QRcode() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QRcode.
     */
    // TODO: Rename and change types and number of parameters
    public static QRcode newInstance(String param1, String param2) {
        QRcode fragment = new QRcode();
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
        mScannerView = new ZXingScannerView(getActivity());
        mGroup = container;
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 2);
        }

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera(); // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();

        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        if(rawResult.toString().charAt(0) == '{') {
            Bundle bundle = new Bundle();
            BankTransferModel transferModel = new BankTransferModel();
            transferModel.setPreviousFragment("Send Cash");
            bundle.putSerializable("key", transferModel);
            bundle.putString("qr", rawResult.getText());


            Fragment fragment = new Wallet2Wallet();
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .commit();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView,
                            fragment).commit();
        }else{
            err("QRCODE Scanned Not TeasyQRCODE");
        }

    }


    private void err(String t){

        LayoutInflater inflater = LayoutInflater.from(mGroup.getContext());
        View view = inflater.inflate(R.layout.errorpopup, mGroup,false);
        TextView msg = view.findViewById(R.id.textView64);
        PopupLayout popupLayout = PopupLayout.init(getContext(), view);
        msg.setText(t);
        popupLayout.setUseRadius(true);

        popupLayout.show(PopupLayout.POSITION_CENTER);
    }
}