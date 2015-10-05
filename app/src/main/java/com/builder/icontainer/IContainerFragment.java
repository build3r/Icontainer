package com.builder.icontainer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.builder.icontainer.Adapters.ContainerAdapter;
import com.builder.icontainer.models.Container;
import com.builder.icontainer.utils.ContantsStatics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class IContainerFragment extends ListFragment
{
    ProgressBar mProgressBar;
    final String tag = IContainerFragment.class.getSimpleName();
    OkHttpClient client;

    String container_end_point ="getContainers";
    String json_str = "";
    List<Container> mContainer;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static IContainerFragment newInstance(String param1, String param2)
    {
        IContainerFragment fragment = new IContainerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IContainerFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        client = new OkHttpClient();
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        try
        {
            run_ask_quest(ContantsStatics.BASEURL+container_end_point);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        // TODO: Change Adapter to display your content
        /*List<Integer> mList = new ArrayList<>(3);
        mList.add(1);
        mList.add(2);
        mList.add(3);
        setListAdapter(new ContainerAdapter(mList));*/

    }


    @Override
    public void onAttach(Activity activity)
    {
        Log.d(tag,"onAttach");
        super.onAttach(activity);
        try
        {

            mListener = (OnFragmentInteractionListener) activity;
            mProgressBar = (ProgressBar) activity.findViewById(R.id.loadData);
            mProgressBar.setVisibility(View.GONE);
            Log.d(tag, "Setting invisble");
        } catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        Log.d(tag, "Clicked Position " + position);
        if (null != mListener)
        {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction((new Gson()).toJson(mContainer.get(position)));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String jsonString);
    }
    public void run_ask_quest(final String url)
            throws Exception {
        Log.d(tag,"URL");
        Request request = new Request.Builder()
                .url(url)
                .get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("deb exception", e.toString());
                runThread("");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);
                json_str = response.body().string();
                System.out.println(json_str);
                runThread(json_str);
            }
        });
    }

    private void runThread(final String json_str) {

        new Thread() {
            public void run() {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Type listType = new TypeToken<ArrayList<Container>>()
                        {
                        }.getType();
                        String json_str1 = json_str;
                         mContainer = null;
                        try
                        {
                             mContainer = new Gson().fromJson(json_str1, listType);
                        }catch (Exception e)
                        {
                            json_str1 = "[{\"containerId\": 1 ,\"item\": \"salt\",\"itemWeight\": [1,2] ,\"date\" : [1432536,2345678]},{\"containerId\": 2 ,\"item\": \"sugar\",\"itemWeight\": [1,2] ,\"date\" : [1432536,1423567]},{\"containerId\": 3 ,\"item\": \"salt\",\"itemWeight\": [0,5] ,\"date\" : [1432536,2345678]},{\"containerId\": 4 ,\"item\": \"salt\",\"itemWeight\": [3,2] ,\"date\" : [1432536,2345678]}]";
                            mContainer = new Gson().fromJson(json_str1, listType);

                        }
                        Log.d(tag, mContainer.toString());
                        ContainerAdapter mAdapter = new ContainerAdapter(mContainer);
                        setListAdapter(mAdapter);

                        mProgressBar.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }
}
