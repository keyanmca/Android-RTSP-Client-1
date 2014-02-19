package com.qplaytech.screenshare;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import jcifs.netbios.NbtAddress;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	private Button btnLogin;
	private Socket client;
	private EditText textPwd, txtHost;
	private String ipAddress, host, password, errMg, liveStreamingLink = null;
	private ProgressDialog waitDialog;
	private NbtAddress nbtAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		checkOS();
		setContentView(R.layout.activity_login);

		textPwd = (EditText) findViewById(R.id.txtPwd);
		txtHost = (EditText) findViewById(R.id.txtHostname);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		waitDialog = new ProgressDialog(Login.this);
		waitDialog.setTitle("Connecting Server");
		waitDialog.setMessage("Trying to connect server. Please wait.");
		waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		waitDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				if (liveStreamingLink != null) {
					Intent passFilePath = new Intent(getApplicationContext(),
							Player.class);
					passFilePath.putExtra("liveStreamingLink",
							liveStreamingLink);
					startActivityForResult(passFilePath, 0);
				}else {
					Toast.makeText(getApplicationContext(), "No responds from server! Please check your session password!", Toast.LENGTH_LONG).show();
				}
			}
		});

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				host = txtHost.getText().toString();
				password = textPwd.getText().toString(); // get the text
				// message on the
				// text field
				//textPwd.setText(""); // Reset the text field to blank

				waitDialog.show();

				new Thread(new Runnable() {
					public void run() {
						try {
							nbtAddress = NbtAddress.getByName(host);
							InetAddress address = nbtAddress.getInetAddress();
							ipAddress = address.getHostAddress();
							client = new Socket(ipAddress, 1024); // connect to
																	// server

							BufferedReader inBufferedReader = new BufferedReader(
									new InputStreamReader(client
											.getInputStream()));
							PrintWriter printwriter = new PrintWriter(
									new BufferedWriter(new OutputStreamWriter(
											client.getOutputStream())), true);

							printwriter.println(password); // write the message
															// to

							printwriter.flush();

								// accept server response
								liveStreamingLink = inBufferedReader.readLine();
							
							inBufferedReader.close();
							printwriter.close();
							client.close();
						} catch (Exception e) {
							errMg = e.toString();
						} finally {

							waitDialog.dismiss();
							// confirmPlay.show();
						}
					}
				}).start();
			}
		});
	}

	private void checkOS() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//		    getActionBar().hide();
		}
	}
}
