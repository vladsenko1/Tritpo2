/*
Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy of this
software and associated documentation files (the "Software"), to deal in the Software
without restriction, including without limitation the rights to use, copy, modify,
merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.amazonaws.mobile.samples.mynotes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.amazonaws.mobile.samples.mynotes.ShoppingListApp;
import com.amazonaws.mobile.samples.mynotes.R;

public class ShoppDetailActivity extends AppCompatActivity {
    /**
     * Injection of the Analytics Service
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist_detail);

        // Set up the action bar so that it has a back button
        Toolbar detail_toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(detail_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Load the fragment
        ShoppDetailFragment fragment = new ShoppDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ShoppingListApp.ITEM_ID, getIntent().getStringExtra(ShoppingListApp.ITEM_ID));
        fragment.setArguments(arguments);

        // Add the fragment to the UI
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.note_detail_container, fragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ShoppListActivity.class);
                navigateUpTo(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
