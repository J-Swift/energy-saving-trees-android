package com.codefororlando.streettrees.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        int resId = activityListener.getTreeDrawable();

        ((ImageView)layoutView.findViewById(R.id.treeTypeIcon)).setImageResource(resId);
        updateTextOrHideGroup(layoutView, R.id.treeTypeGroup, R.id.treeType, treeDescription.getName());
        updateTextOrHideGroup(layoutView, R.id.treeDescription, R.id.treeDescription, treeDescription.getDescription());

        updateTextOrHideGroup(layoutView, R.id.treeWidthGroup, R.id.treeWidth, String.format("%s - %s", treeDescription.getMinWidth(), treeDescription.getMaxWidth()));
        updateTextOrHideGroup(layoutView, R.id.treeHeightGroup, R.id.treeHeight, String.format("%s - %s", treeDescription.getMinHeight(), treeDescription.getMaxHeight()));

        updateTextOrHideGroup(layoutView, R.id.treeLeafGroup, R.id.treeLeaf, treeDescription.getLeaf());
        updateTextOrHideGroup(layoutView, R.id.treeShapeGroup, R.id.treeShape, treeDescription.getShape());
        updateTextOrHideGroup(layoutView, R.id.treeSunlightGroup, R.id.treeSunlight, treeDescription.getSunlight());
        updateTextOrHideGroup(layoutView, R.id.treeSoilGroup, R.id.treeSoil, treeDescription.getSoil());
        updateTextOrHideGroup(layoutView, R.id.treeMoistureGroup, R.id.treeMoisture, treeDescription.getMoisture());
    }

    // Avoid showing empty/null text by removing the offending views
    private static void updateTextOrHideGroup(View parentView, int sectionId, int textNodeId, String updateText) {
        if ( updateText == null || updateText.trim().isEmpty() ) {
            parentView.findViewById(sectionId).setVisibility(View.GONE);
            return;
        }

        ((TextView) parentView.findViewById(textNodeId)).setText(updateText.trim());
    }

    public interface DetailListener {
        TreeDescription getTreeData();
        @DrawableRes int getTreeDrawable();
    }

}
