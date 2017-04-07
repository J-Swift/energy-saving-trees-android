package com.codefororlando.streettrees.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codefororlando.streettrees.R;
import com.codefororlando.streettrees.api.models.TreeDescription;

public class DetailFragment extends Fragment {

    public static final String TAG = "DETAILFRAGMENT";
    private DetailListener activityListener;
    private View layoutView;

    public static DetailFragment newInstance() {
        DetailFragment frag = new DetailFragment();

        Bundle args = new Bundle();
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailListener) {
            activityListener = (DetailListener) context;
        } else {
            throw new IllegalArgumentException("Cannot attach DetailFragment to Activity without DetailListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_detail_activity, container, false);
        return layoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TreeDescription treeDescription = activityListener.getTreeData();

        ((TextView) layoutView.findViewById(R.id.treeHeight)).setText(treeDescription.getMinHeight());
        ((TextView) layoutView.findViewById(R.id.treeLeaf)).setText(treeDescription.getLeaf());
        ((TextView) layoutView.findViewById(R.id.treeShape)).setText(treeDescription.getShape());
        ((TextView) layoutView.findViewById(R.id.treeWidth)).setText(treeDescription.getMinWidth());
        ((TextView) layoutView.findViewById(R.id.treeSunlight)).setText(treeDescription.getSunlight());
        ((TextView) layoutView.findViewById(R.id.treSoil)).setText(treeDescription.getSoil());
        ((TextView) layoutView.findViewById(R.id.treeMoisture)).setText(treeDescription.getMoisture());
        ((TextView) layoutView.findViewById(R.id.treeDescription)).setText(treeDescription.getDescription());
        ((TextView) layoutView.findViewById(R.id.treeType)).setText(treeDescription.getName());
    }

    public interface DetailListener {
        TreeDescription getTreeData();
        @DrawableRes int getTreeDrawable();
    }

}
