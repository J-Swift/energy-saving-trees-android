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

package com.codefororlando.streettrees.requesttree.contactinfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codefororlando.streettrees.R;
import com.codefororlando.streettrees.api.models.ContactInfo;
import com.codefororlando.streettrees.requesttree.BlurredBackgroundFragment;

public class ContactInfoFragment extends BlurredBackgroundFragment
        implements ContactInfoPresenter.ContactInfoView, TextView.OnEditorActionListener {

    private Button nextButton;
    private EditText nameField;
    private EditText phoneNumberField;
    private EditText emailField;

    private ContactInfoListener contactInfoListener;

    ContactInfoPresenter presenter;

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(getString(R.string.contact_info_fragment_title));
        presenter.attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_tree_contact_info, container, false);
        bindUi(view);
        float blurRadius = 25f;
        float blurScale = .05f;
        initBlurredBackground(view, R.drawable.bg_forrest, blurRadius, blurScale);
        presenter = new ContactInfoPresenter();
        return view;
    }

    private void bindUi(View view) {
        nameField = (EditText) view.findViewById(R.id.name_field);
        phoneNumberField = (EditText) view.findViewById(R.id.phone_number_field);
        emailField = (EditText) view.findViewById(R.id.email_field);

        nameField.setOnEditorActionListener(this);
        phoneNumberField.setOnEditorActionListener(this);
        emailField.setOnEditorActionListener(this);

        nextButton = (Button) view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.validateName(nameField.getText().toString());
                presenter.validateNumber(phoneNumberField.getText().toString());
                presenter.validateEmail(emailField.getText().toString());
                if(presenter.canProceed()) {
                    nextFragment();
                }
            }
        });
    }

    private void nextFragment() {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setName(nameField.getText().toString());
        contactInfo.setPhoneNumber(phoneNumberField.getText().toString());
        contactInfo.setEmail(emailField.getText().toString());
        contactInfoListener.onFormFilled(contactInfo);
        pageListener.next();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            contactInfoListener = (ContactInfoListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ContactInfoListener");
        }
    }

    @Override
    public void setNameError(String errorMsg) {
        nameField.setError(errorMsg);
    }

    @Override
    public void setEmailError(String errorMsg) {
        emailField.setError(errorMsg);
    }

    @Override
    public void setPhoneNumberError(String errorMsg) {
        phoneNumberField.setError(errorMsg);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        int textViewId = v.getId();
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            if (textViewId == nameField.getId()) {
                return !presenter.validateName(nameField.getText().toString());
            } else if (textViewId == phoneNumberField.getId()) {
                return !presenter.validateNumber(phoneNumberField.getText().toString());
            }
        } else if(actionId == EditorInfo.IME_ACTION_DONE) {
            if (textViewId == emailField.getId()) {
                return !presenter.validateEmail(emailField.getText().toString());
            }
        }
        return false;
    }

    public interface ContactInfoListener {
        void onFormFilled(ContactInfo contactInfo);
    }

}
