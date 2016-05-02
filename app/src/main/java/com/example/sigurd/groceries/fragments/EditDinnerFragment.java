package com.example.sigurd.groceries.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.example.sigurd.groceries.R;
import com.example.sigurd.groceries.models.Dinner;
import com.example.sigurd.groceries.models.Grocery;
import com.example.sigurd.groceries.util.GroceryAdapter;
import com.example.sigurd.groceries.util.IngredientAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditDinnerFragmentListener} interface
 * to handle interaction events.
 * Use the {@link EditDinnerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditDinnerFragment extends Fragment {

    private PopupMenu popup;
    private String TAG = this.getClass().getSimpleName();
    private Dinner dinner;
    private int dinnerIndex;
    private boolean isNewDinner = true;
    ArrayList<Grocery> ingredients;
    View rootView;
    EditText name;
    ListView dinnerIngredients;

    public static final String DINNER_INDEX = "dinner_index";

    private EditDinnerFragmentListener mListener;

    public EditDinnerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditDinnerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditDinnerFragment newInstance(int dinnerIndex) {
        EditDinnerFragment fragment = new EditDinnerFragment();
        Bundle args = new Bundle();
        args.putInt(DINNER_INDEX, dinnerIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        dinnerIndex = args.getInt(DINNER_INDEX);
        if(dinnerIndex != -1) isNewDinner = false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_dinner, container, false);
        ListView list = (ListView) rootView.findViewById(R.id.dinner_ingredients_list);

        //Log.d(TAG, "ingredients size: " + this.ingredients.size());
        //Log.d(TAG, "ingredients size: " + dinner.getIngredients().size());
        if(!isNewDinner){
            getDinner();
            setEditDinnerIcon((ImageView)rootView.findViewById(R.id.edit_dinner_icon));
            IngredientAdapter adapter = new IngredientAdapter(getContext(),dinner.getIngredients());
            list.setAdapter(adapter);
        }
        else{
            IngredientAdapter adapter = new IngredientAdapter(getContext(),ingredients);
            list.setAdapter(adapter);
        }

        rootView.findViewById(R.id.add_ingredient_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddIngredient();
            }
        });
        rootView.findViewById(R.id.save_dinner_button).setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isNewDinner){
                    onSaveDinner(getCurrentDinnerState());
                }
                else{
                    onUpdateDinner(getCurrentDinnerState(),dinner);
                }
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditDinnerFragmentListener) {
            mListener = (EditDinnerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EditDinnerFragmentListener");
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
    public interface EditDinnerFragmentListener {
        void onGetDinner(int dinnerIndex);
        void onSaveDinner(Dinner newDinner);
        void onUpdateDinner(Dinner newDinner, Dinner oldDinner);
        void onAddIngredient();
    }

    /**
     * Called by containing activity when onGetDinner is called
     * @param dinner
     */
    public void setDinner(Dinner dinner){
        this.dinner = dinner;
        this.ingredients = dinner.getIngredients();
    }

    private void onSaveDinner(Dinner newDinner){
        mListener.onSaveDinner(newDinner);
    }

    private void onUpdateDinner(Dinner newDinner, Dinner oldDinner){
        mListener.onUpdateDinner(newDinner, oldDinner);
    }

    public Dinner getCurrentDinnerState(){
        /* Extract dinner from fields WITHOUT overwriting current dinner */
        Dinner updatedDinner = new Dinner(-1);
        updatedDinner.setName(name.getText().toString());
        updatedDinner.setIngredients(((IngredientAdapter)dinnerIngredients.getAdapter()).getIngredients());
        // TODO: Get images and order
        //updatedDinner.setIconName();
        //updatedDinner.setOrder();
        return updatedDinner;
    }

    private void getDinner(){
        // TODO: get ingredients from given dinner
        if(dinnerIndex != -1){
            mListener.onGetDinner(dinnerIndex);
            ingredients = dinner.getIngredients();
        }
        ingredients = new ArrayList<>();
    }

    private void setEditDinnerIcon(ImageView icon){
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        icon.setImageDrawable(dinner.getIcon(getContext()));
    }

}
