package com.example.sigurd.groceries.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;

import com.example.sigurd.groceries.MainActivity;
import com.example.sigurd.groceries.R;
import com.example.sigurd.groceries.models.Grocery;
import com.example.sigurd.groceries.util.CameraFileUtil;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditGroceryFragmentListener} interface
 * to handle interaction events.
 * Use the {@link EditGroceryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditGroceryFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String GROCERY_INDEX = "chosen_grocery_index";
    private final String TAG = EditGroceryFragment.class.getSimpleName();

    private Grocery grocery;
    private Context context;
    private EditGroceryFragmentListener mListener;
    private boolean isNewItem = false;
    private View rootView;
    private EditText editName;
    private ImageView image;
    private Uri pictureUri;

    public EditGroceryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param groceryIndex Parameter 1.
     * @return A new instance of fragment EditGroceryFragment.
     */
    public static EditGroceryFragment newInstance( int groceryIndex) {
        EditGroceryFragment fragment = new EditGroceryFragment();
        Bundle args = new Bundle();
        args.putInt(GROCERY_INDEX, groceryIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).setEditFragment(this);
        Log.d(TAG, "OnCreate called");
        if (getArguments() != null) {
            int index = getArguments().getInt(GROCERY_INDEX);
            setGrocery(index, (MainActivity) getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"OnCreateView called");
        View rootView = inflater.inflate(R.layout.fragment_edit_grocery, container, false);
        this.rootView = rootView;
        setSaveListener((Button) rootView.findViewById(R.id.save_grocery_button));
        setDeleteListener((Button) rootView.findViewById(R.id.delete_grocery_button));
        setTakePictureListener((Button) rootView.findViewById(R.id.take_grocery_picture));
        context = getContext();
        fillView();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditGroceryFragmentListener) {
            mListener = (EditGroceryFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EditGroceryFragmentListener");
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
    public interface EditGroceryFragmentListener {
        // TODO: Update argument type and name
        void onAddNewEssentialGrocery(Grocery grocery);
        void onSaveEssentialGrocery(Grocery updatedGrocery);
        void onDeleteEssentialGrocery(Grocery groceryToDelete);
    }

    // Should be possible to fill with given grocery from outside
    public void fillView(){
        image = (ImageView) rootView.findViewById(R.id.grocery_icon);
        editName = (EditText) rootView.findViewById(R.id.editable_name);
        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if(grocery != null){
            image.setImageDrawable(grocery.getIcon(context));
            Log.d(TAG,"Setting grocery name to : "+grocery.getName());
            editName.setText(grocery.getName());
        }
        else{
            // Using wrong view?
            editName.setHint("New grocery name");
            image.setImageDrawable(null);
        }

    }

    private void setSaveListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: get updated grocery
                Grocery updatedGrocery = getUpdatedGrocery();
                if (isNewItem) {
                    // TODO: Update with grocery ID from insert query
                    Log.d(TAG, "inserting into database");
                    mListener.onAddNewEssentialGrocery(updatedGrocery);
                } else {
                    Log.d(TAG, "Updating data row");
                    mListener.onSaveEssentialGrocery(updatedGrocery);
                }
            }
        });

    }

    private void setDeleteListener(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDeleteEssentialGrocery(grocery);
            }
        });
    }

    private void setTakePictureListener(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    private Grocery getUpdatedGrocery(){
        //TODO: Set all values , not just name, or set them in activity
        if(grocery == null){
            // TODO:Insert new grocery with given values
            String groceryName = editName.getText().toString();
            grocery = new Grocery(-1,groceryName,0,-1,Grocery.NEEDED_ESSENTIAL,pictureUri);
        }
        else{
            grocery.setName(editName.getText().toString());
        }
        return grocery;
    }

    private void setGrocery(int index, MainActivity activity){
        Log.d(TAG, "Setting grocery with index : " + index);
        if(index==-1){
            isNewItem = true;
            grocery = null;
        }
        else{
            grocery = activity.groceries.get(MainActivity.ESSENTIALS).get(index);
        }
    }



    private void takePicture(){
        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        pictureUri = CameraFileUtil.getOutputMediaFileUri(CameraFileUtil.MEDIA_TYPE_IMAGE);
        picIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        // do something
        Log.d(TAG, "Taking picture");

        getActivity().startActivityForResult(picIntent, MainActivity.CAPTURE_IMAGE);
    }

    public void onPictureCaptureResult(){
        Log.d(TAG, "Picture is taken , cropping next");
        performCrop();
    }

    private void performCrop(){
        Log.d(TAG,"Performing crop");
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(pictureUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            getActivity().startActivityForResult(cropIntent, MainActivity.CROP_PICTUERE);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            ((MainActivity)getActivity()).messageUser("This device doesn't support the crop action!",rootView);
        }
    }

    public void onCropResult(Intent data){
        Bundle extras = data.getExtras();
        Bitmap croppedPicture = extras.getParcelable("data");
        setNewPicture(croppedPicture);
    }

    private void setNewPicture(Bitmap icon){
        image.setImageBitmap(icon);
        grocery.setIconUri(pictureUri);
    }


}
