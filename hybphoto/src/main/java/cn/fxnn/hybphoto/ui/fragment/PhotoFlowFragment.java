package cn.fxnn.hybphoto.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.fxnn.hybphoto.R;

/**
 * **************************
 * Class:       PhotoFlowFragment
 * Author:      fangx
 * Date:        16/9/1
 * Description:
 * ***************************
 */
public class PhotoFlowFragment extends Fragment {
    private final static String SHOW_CAMERA = "show_camera";
    private final static String SELECTED_PHOTOS = "selected_photos";

    public static PhotoFlowFragment newInstance(boolean showCamera, ArrayList<String> selectedPhotos) {
        Bundle args = new Bundle();
        args.putBoolean(SHOW_CAMERA, showCamera);
        args.putStringArrayList(SELECTED_PHOTOS, selectedPhotos);
        PhotoFlowFragment fragment = new PhotoFlowFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.pf_fragment_layout, container, false);



        return rootView;
    }

}
