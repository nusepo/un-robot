package com.android.clientes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Android_Client1 extends Activity implements Runnable {

	private TextView direccionIP;
	private TextView puertoTCP;
	private TextView conexion;
	private TextView mensajeEnviado;

	private SeekBar datoIn;

	private ToggleButton btnOnOf;

	private Button btnConectar;
	private Button btnEnviar;

	private String ip = "";
	private String message = "";
	private int puerto = 0;
	private int dato;

	private Thread hiloClient;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		direccionIP = (TextView) findViewById(R.id.txtIP);
		puertoTCP = (TextView) findViewById(R.id.txtPuerto);
		conexion = (TextView) findViewById(R.id.txtConexion);
		datoIn = (SeekBar) findViewById(R.id.seekBar);
		btnOnOf = (ToggleButton) findViewById(R.id.toggleBtn);
		mensajeEnviado = (TextView) findViewById(R.id.txtMensaje);
		btnConectar = (Button) findViewById(R.id.btnConectar);

		datoIn.setProgress(15);
		btnConectar.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				ip = direccionIP.getText().toString();
				puerto = Integer.parseInt(puertoTCP.getText().toString());
				hiloClient.start();
			}
		});

		btnEnviar = (Button) findViewById(R.id.btnEnviar);
		btnEnviar.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				message = mensajeEnviado.getText().toString();
				mensajeEnviado.setText("");
			}
		});
		
		btnOnOf.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(btnOnOf.isChecked()==true){
					message="ToggleButton is On";
				}else {
					message="ToggleButton is Off";
				}
				
			}
		});
		
		datoIn.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				message = Integer.toString(progress);
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		});

		hiloClient = new Thread(Android_Client1.this);
		hiloClient.setName("Hilo Cliente");

	}

	@Override
	public void run() {
		
		
		try {
			Socket cliente = new Socket(ip, puerto);
			//conexion.setText("Conectado!");

			BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			PrintWriter salida;

			while (true) {

				salida = new PrintWriter(cliente.getOutputStream(), true);
				salida.println(message);
				salida.flush();
				

				//String msnRecibido = entrada.readLine();
				//manejoDatos(msnRecibido);
				Thread.sleep(2000);
			}

		} catch (UnknownHostException e) {
			System.out.println("Problemas");
		} catch (IOException e) {
			System.out.println("Problemas");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	public void manejoDatos(String datoRecibido){
		dato = Integer.parseInt(datoRecibido);
		datoIn.setProgress(dato);
	}

}