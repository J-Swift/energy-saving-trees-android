// Copyright © 2017 Code for Orlando.
// 
// MIT License
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package com.codefororlando.streettrees.requesttree.deliveryaddress;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.codefororlando.streettrees.R;
import com.codefororlando.streettrees.api.models.Address;
import com.codefororlando.streettrees.requesttree.BlurredBackgroundFragment;


public class AddressFormFragment extends BlurredBackgroundFragment {

    private Button nextButton;
    private EditText streetAddressField;
    private EditText streetAddressExtraField;
    private EditText cityField;
    private EditText stateField;
    private EditText zipField;

    private AddressFormListener addressFormListener;

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(getString(R.string.address_form_fragment_title));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_tree_address, container, false);
        bindUi(view);
        float blurRadius = 25f;
        float blurScale = .05f;
        initBlurredBackground(view, R.drawable.bg_tall_trees, blurRadius, blurScale);
        return view;
    }

    private void bindUi(View view) {
        streetAddressField = (EditText) view.findViewById(R.id.street_address_field);
        streetAddressExtraField = (EditText) view.findViewById(R.id.street_address_extra_field);
        cityField = (EditText) view.findViewById(R.id.city_field);
        stateField = (EditText) view.findViewById(R.id.state_field);
        zipField = (EditText) view.findViewById(R.id.zip_field);

        nextButton = (Button) view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextFragment();
            }
        });
    }

    private void nextFragment() {
        Address address = new Address();
        address.setStreetAddress(streetAddressField.getText().toString());
        address.setStreetAddressExtra(streetAddressExtraField.getText().toString());
        address.setCity(cityField.getText().toString());
        address.setState(stateField.getText().toString());
        address.setZip(zipField.getText().toString());
        addressFormListener.onFormFilled(address);
        pageListener.next();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            addressFormListener = (AddressFormListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement AddressFormListener");
        }
    }

    public interface AddressFormListener {
        void onFormFilled(Address address);
    }
}
