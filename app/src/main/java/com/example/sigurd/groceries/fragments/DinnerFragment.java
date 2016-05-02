package com.example.sigurd.groceries.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.example.sigurd.groceries.MainActivity;
import com.example.sigurd.groceries.R;
import com.example.sigurd.groceries.models.Dinner;
import com.example.sigurd.groceries.models.Grocery;
import com.example.sigurd.groceries.util.DinnerAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.example.sigurd.groceries.fragments.DinnerFragment.DinnerFragmentListener} interface
 * to handle interaction events.
 * Use the {@link DinnerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DinnerFragment extends Fragment {

    private static final String TAG = DinnerFragment.class.getSimpleName();
    private PopupMenu popup;
    private ArrayList<Dinner> dinners;
    private DinnerFragmentListener mListener;

    public DinnerFragment() {
        // Required empty public constructor
    }

    public static DinnerFragment newInstance() {
        return new DinnerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Use this if arguments is needed
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDinners();
        View rootView = inflater.inflate(R.layout.fragment_dinner, container, false);
        if(rootView==null) Log.d(TAG, "Dinner-rootView is null!");
        setAddDinnerListener(rootView);
        ListView list = (ListView) rootView.findViewById(R.id.dinner_list);
        setItemListener(list);
        DinnerAdapter adapter = new DinnerAdapter(getContext(),dinners);
        list.setAdapter(adapter);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DinnerFragmentListener) {
            mListener = (DinnerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DinnerFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface DinnerFragmentListener {
        void onAddDinnerIngredients(Dinner dinner, View view);
        void onAddDinnerView(int dinnerIndex);
    }

    private void getDinners(){
        // TODO: Make real dinners
        MainActivity activity =(MainActivity) getActivity();
        ArrayList<Grocery> essentials = activity.groceries.get(MainActivity.ESSENTIALS);
        dinners = new ArrayList<>();
        Dinner lasang = new Dinner(0);
        lasang.setName("Lasagne");
        lasang.addIngredient(essentials.get(0));
        Dinner kylling = new Dinner(1);
        kylling.setName("Kylling");
        kylling.addIngredient(essentials.get(1));
        kylling.addIngredient(essentials.get(2));
        dinners.add(lasang);
        dinners.add(kylling);
    }

    private void setAddDinnerListener(View view){
        view.findViewById(R.id.add_dinner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddDinnerView(-1);
            }
        });
    }


    public Dinner getDinner(int dinnerIndex){
        return dinners.get(dinnerIndex);
    }

    private void setItemListener(ListView list){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                makePopUpMenu(view, position);
            }
        });
    }

    private void makePopUpMenu(final View anchor, final int position){
        popup = new PopupMenu(getActivity(), anchor);
        //popup.setOnDismissListener(new OnDismissListener());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.add_ingredients:
                        mListener.onAddDinnerIngredients(dinners.get(position),anchor);
                        return true;
                    case R.id.edit_dinner:
                        mListener.onAddDinnerView(position);
                        return true;
                }
                return false;
            }

        });
        popup.inflate(R.menu.dinner_menu);
        popup.show();
    }
}
