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

package com.codefororlando.streettrees;

import android.app.Application;
import android.content.Context;

import com.codefororlando.streettrees.api.providers.TreeDescriptionProvider;
import com.codefororlando.streettrees.api.providers.TreeProvider;
import com.codefororlando.streettrees.di.DaggerTreeProviderComponent;
import com.codefororlando.streettrees.di.TreeDescriptionProviderModule;
import com.codefororlando.streettrees.di.TreeProviderComponent;
import com.codefororlando.streettrees.di.TreeProviderModule;

public class TreeApplication extends Application {

    private TreeProviderComponent treeProviderComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponents(getApplicationContext());
    }

    private void initComponents(Context context) {
        TreeProvider treeProvider = new TreeProvider(getApplicationContext());
        TreeDescriptionProvider treeDescriptionProvider = new TreeDescriptionProvider(context);

        treeProviderComponent = DaggerTreeProviderComponent.builder()
                .treeProviderModule(new TreeProviderModule(treeProvider))
                .treeDescriptionProviderModule(new TreeDescriptionProviderModule(treeDescriptionProvider))
                .build();
    }

    public TreeProviderComponent getTreeProviderComponent() {
        return treeProviderComponent;
    }
}
