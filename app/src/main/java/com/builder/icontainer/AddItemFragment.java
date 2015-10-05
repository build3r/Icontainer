package com.builder.icontainer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.builder.icontainer.utils.ContantsStatics;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * interface
 * to handle interaction events.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemFragment extends Fragment
{
    String add_item_endpoint ="addContainer/";
    View root_view;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddItemFragment newInstance(String param1, String param2)
    {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddItemFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.fragment_add_item, container, false);
        ImageView imageView = (ImageView) root_view.findViewById(R.id.barcode_scan);
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(intent, 0);
            }
        });
        return root_view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 0) {
            if (resultCode == getActivity().RESULT_OK) {
                final String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                final AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.add_item_popup, null);
                final EditText input = (EditText) mLinearLayout.findViewById(R.id.editText);
                alert.setView(mLinearLayout);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                        String value = input.getText().toString().trim();
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(ContantsStatics.BASEURL+add_item_endpoint+contents+"/"+value)
                                .get().build();



                        client.newCall(request).enqueue(new Callback()
                        {
                            @Override
                            public void onFailure(Request request, IOException e)
                            {
                                Log.d("deb exception", e.toString());
                            }

                            @Override
                            public void onResponse(Response response) throws IOException
                            {
                                if (!response.isSuccessful())
                                    throw new IOException("Unexpected code " + response);
                                String temp = response.body().string();
                                System.out.println(temp);
                                Toast.makeText(getActivity(), "Response " + temp,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                alert.show();
                Toast.makeText(getActivity(), contents,
                        Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),contents , Toast.LENGTH_LONG).show();

                    }

                }
            else if (resultCode == getActivity().RESULT_CANCELED)
        {
            Toast.makeText(getActivity(), "Dint work",
                    Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }



}
