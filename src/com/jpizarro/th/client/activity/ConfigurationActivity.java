package com.jpizarro.th.client.activity;


import com.jpizarro.th.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ConfigurationActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}

}
