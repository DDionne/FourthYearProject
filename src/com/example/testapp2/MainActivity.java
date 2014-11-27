package com.example.testapp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	public enum Command {
		moveForward, moveBackward, leftTurn, rightTurn, stop;
	}
	
	Button buttonUp;
	Button buttonDown;
	Button buttonLeft;
	Button buttonRight;
	TextView t1;
	
	HttpClient httpClient;
	String uri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addListenerOnButtons();
		httpClient = new DefaultHttpClient();
		uri = "";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void addListenerOnButtons() {
		
		buttonUp = (Button) findViewById(R.id.buttonUP);
		buttonDown = (Button) findViewById(R.id.buttonDOWN);
		buttonLeft = (Button) findViewById(R.id.buttonLEFT);
		buttonRight = (Button) findViewById(R.id.buttonRIGHT);
		t1 = (TextView) findViewById(R.id.textView1);
		
		buttonUp.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					Log.d("Up Pressed", "Telling robot to start moving forward");
					new MyAsyncTask().execute(Command.moveForward.toString());
				} else if(event.getAction() == MotionEvent.ACTION_UP){
					Log.d("Up Released", "Telling robot to stop moving forward");
					new MyAsyncTask().execute(Command.stop.toString());
				}
				return false;
			}
		});
		
		buttonDown.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					Log.d("Down Pressed", "Telling robot to start movign backwards");
					new MyAsyncTask().execute(Command.moveBackward.toString());
				} else if(event.getAction() == MotionEvent.ACTION_UP){
					new MyAsyncTask().execute(Command.stop.toString());
				}
				return false;
			}
		});
		
		buttonLeft.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					Log.d("Left Pressed", "Telling robot to start turning left");
					new MyAsyncTask().execute(Command.leftTurn.toString());
				} else if(event.getAction() == MotionEvent.ACTION_UP){
					Log.d("Right Released", "Telling robot to stop turning left");
					new MyAsyncTask().execute(Command.stop.toString());
				}
				return false;
			}
		});
		
		buttonRight.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					Log.d("Right Pressed", "Telling robot to start turning right");
					new MyAsyncTask().execute(Command.rightTurn.toString());
				} else if(event.getAction() == MotionEvent.ACTION_UP){
					Log.d("Right Released", "Telling robot to stop turning right");
					new MyAsyncTask().execute(Command.stop.toString());
				}
				return false;
			}
		});
		
			
		
	}
	
	private void sendRequest(String action){
		uri = "http://192.168.240.1/arduino/" + action;//http://192.168.2.24//MidtermProblems/ajaxTest.php
		try {
			HttpResponse response = httpClient.execute(new HttpGet(uri));
			//t1.setText(response.toString());
			Log.d("http response:", response.toString());
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				StringBuilder sb = new StringBuilder();
				try{
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()), 65728);
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
				}
				catch (IOException e) { e.printStackTrace();}
				catch (Exception e) { e.printStackTrace(); }
				final String a = sb.toString();
				
				runOnUiThread(new Runnable() {
				     @Override
				     public void run() {

				    	 //stuff that updates ui
				    	 t1.setText(a.toString());
				    }
				});
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class MyAsyncTask extends AsyncTask<String,Integer,Double> {

		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			sendRequest(params[0]);
			return null;
		}

	}
}

