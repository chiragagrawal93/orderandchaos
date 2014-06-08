package com.chirag.sudoku;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

public class ConnectedSocket extends Thread
{
	 private final BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
	    private final OutputStream mmOutStream;
	    Handler mHandler;
	    public  ConnectedSocket(BluetoothSocket socket,Handler mHandler) {
	        mmSocket = socket;
	        this.mHandler = mHandler;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) { }
	 
	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }
	 
	    public void run() {
	        byte[] buffer = new byte[1024];  // buffer store for the stream
	        int bytes; // bytes returned from read()
	        int[] read = new int[3]; 
	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	            try {
	                // Read from the InputStream
	                bytes = mmInStream.read(buffer);
//	                System.out.println(buffer);
	                byte[] byteArr = buffer;
	                read[0] = (int)byteArr[0];
	                read[1] = (int)byteArr[1];
	                read[2] = (int)byteArr[2];
//	                System.out.println("read[2]="+read[0] );
	                // Send the obtained bytes to the UI activity
//	                mHandler.obtainMessage(read, bytes, -1, buffer)
	//                        .sendToTarget();
	                Message msg = Message.obtain();
	    	        msg.obj =  read;
	    	        mHandler.sendMessage(msg);
	            } catch (IOException e) {
	                break;
	            }
	        }
	    }
	 
	    /* Call this from the main activity to send data to the remote device */
	    public void write(byte[] bytes) {
	        try {
	            mmOutStream.write(bytes);
	        } catch (IOException e) { }
	    }
	 
	    /* Call this from the main activity to shutdown the connection */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
}