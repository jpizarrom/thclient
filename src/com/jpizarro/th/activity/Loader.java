package com.jpizarro.th.activity;

import java.util.ArrayList;

import com.jpizarro.th.entity.Competitor;
import com.jpizarro.th.entity.Game;
import com.jpizarro.th.entity.Goal;
import com.jpizarro.th.entity.Hint;
import com.jpizarro.th.entity.Team;
import com.jpizarro.th.entity.Test;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Loader extends ListActivity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final ArrayList<String> list = new ArrayList<String>();
		
		list.add("Test");
		list.add("Login");
		list.add("Game List");
		
		this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch(position){
			case 0:
				test();
			break;
			case 1:
				this.startActivity(new Intent(this, Login.class));
				break;
			case 2:
				this.startActivity(new Intent(this, GameList.class));
				break;
		}
	}
	
	private void test(){			
		Toast.makeText(this, Test.test(), Toast.LENGTH_LONG).show();
	}

}
