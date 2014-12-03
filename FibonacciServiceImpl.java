package com.marakana.android.fibonacciservice;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Arrays;

import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.util.Log;

import com.marakana.android.fibonaccicommon.FibonacciRequest;
import com.marakana.android.fibonaccicommon.FibonacciResponse;
import com.marakana.android.fibonaccicommon.IFibonacciService;
import com.marakana.android.fibonaccicommon.MemoryFileUtil;
import com.marakana.android.fibonaccinative.FibLib;

public class IFibonacciServiceImpl extends IFibonacciService.Stub {
	
	private void compareBuffers(byte[] buffer1, byte[] buffer2, int length) throws Exception {
        for (int i = 0; i < length; i++) {
            if (buffer1[i] != buffer2[i]) {
                throw new Exception("readBytes did not read back what writeBytes wrote");
            }
        }
        Log.d(TAG, String.format("The bytes match"));
    }
	
	static int flag = 0;
	private static final String TAG = "IFibonacciServiceImpl";

	public long fibJI(long n) {
		Log.d(TAG, String.format("fibJI(%d)", n));
		return FibLib.fibJI(n);
	}

	public long fibJR(long n) {
		Log.d(TAG, String.format("fibJR(%d)", n));
		return FibLib.fibJR(n);
	}

	public long fibNI(long n) {
		Log.d(TAG, String.format("fibNI(%d)", n));
		return FibLib.fibNI(n);
	}

	public long fibNR(long n) {
		Log.d(TAG, String.format("fibNR(%d)", n));
		return FibLib.fibNR(n);
	}

	public FibonacciResponse fib(FibonacciRequest request) {
		byte[] buffer = new byte[testString.length];
		Log.d(TAG,
				String.format("fib(%d, %s)", request.getN(), request.getType()));
		long timeInMillis = SystemClock.uptimeMillis();
		
		FileDescriptor fd1;
		ParcelFileDescriptor mem1 = null;
		long result;
		switch (request.getType()) {
		case ITERATIVE_JAVA:
			result = this.fibJI(request.getN());
			break;
		case RECURSIVE_JAVA:
			result = this.fibJR(request.getN());
			break;
		case ITERATIVE_NATIVE:
			result = this.fibNI(request.getN());
			break;
		case RECURSIVE_NATIVE:
			result = this.fibNR(request.getN());
			break;
		default:
			return null;
		}
		timeInMillis = SystemClock.uptimeMillis() - timeInMillis;
		if(flag ==0)
		{
			for (int i = 0; i < testString.length; i++) {
	            buffer[i] = testString[i];}
			/*
			Log.d(TAG, String.format("Creating memory file now"));
			MemoryFile newFile = null;
			try {
				newFile = new MemoryFile("MemoryFileTest", 10000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d(TAG, String.format("getting fd from util function"));
			fd1 = MemoryFileUtil.getFileDescriptor(newFile);
			try {
				mem1 = ParcelFileDescriptor.dup(fd1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				newFile.writeBytes(testString, 0, 0, testString.length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			/*
			try {
				newFile.readBytes(buffer, 0, 0, testString.length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			try {
				compareBuffers(testString, buffer, testString.length);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d(TAG, String.format("Past the compare Buffers"));
			Log.d(TAG, String.format("Buffer: %s",Arrays.toString(buffer)));
			//int a = mem1.getFd();
			//Log.d(TAG, String.format("Got  fd from parcel as - %d",a));
			flag = 1;
			
		}
		return new FibonacciResponse(result, buffer.length, buffer);
	}


	private static final byte[] testString = new byte[] {
        3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3, 8, 4, 6, 2, 6, 4, 3, 3, 8, 3, 2, 7, 9, 5, 0, 2, 8, 8, 4, 1, 9, 7, 1, 6, 9, 3, 9, 9, 3, 7, 5, 1, 0, 5, 8, 2, 0, 9, 7, 4, 9, 4, 4, 5, 9, 2, 3, 0, 7, 8, 1, 6, 4,
        0, 6, 2, 8, 6, 2, 0, 8, 9, 9, 8, 6, 2, 8, 0, 3, 4, 8, 2, 5, 3, 4, 2, 1, 1, 7, 0, 6, 7, 9, 8, 2, 1, 4, 8, 0, 8, 6, 5, 1, 3, 2, 8, 2, 3, 0, 6, 6, 4, 7, 0, 9, 3, 8, 4, 4, 6, 0, 9, 5, 5, 0, 5, 8, 2, 2, 3, 1, 7, 2,
        5, 3, 5, 9, 4, 0, 8, 1, 2, 8, 4, 8, 1, 1, 1, 7, 4, 5, 0, 2, 8, 4, 1, 0, 2, 7, 0, 1, 9, 3, 8, 5, 2, 1, 1, 0, 5, 5, 5, 9, 6, 4, 4, 6, 2, 2, 9, 4, 8, 9, 5, 4, 9, 3, 0, 3, 8, 1, 9, 6, 4, 4, 2, 8, 8, 1, 0, 9, 7, 5,
        6, 6, 5, 9, 3, 3, 4, 4, 6, 1, 2, 8, 4, 7, 5, 6, 4, 8, 2, 3, 3, 7, 8, 6, 7, 8, 3, 1, 6, 5, 2, 7, 1, 2, 0, 1, 9, 0, 9, 1, 4, 5, 6, 4, 8, 5, 6, 6, 9, 2, 3, 4, 6, 0, 3, 4, 8, 6, 1, 0, 4, 5, 4, 3, 2, 6, 6, 4, 8, 2,
        1, 3, 3, 9, 3, 6, 0, 7, 2, 6, 0, 2, 4, 9, 1, 4, 1, 2, 7, 3, 7, 2, 4, 5, 8, 7, 0, 0, 6, 6, 0, 6, 3, 1, 5, 5, 8, 8, 1, 7, 4, 8, 8, 1, 5, 2, 0, 9, 2, 0, 9, 6, 2, 8, 2, 9, 2, 5, 4, 0, 9, 1, 7, 1, 5, 3, 6, 4, 3, 6,
        7, 8, 9, 2, 5, 9, 0, 3, 6, 0, 0, 1, 1, 3, 3, 0, 5, 3, 0, 5, 4, 8, 8, 2, 0, 4, 6, 6, 5, 2, 1, 3, 8, 4, 1, 4, 6, 9, 5, 1, 9, 4, 1, 5, 1, 1, 6, 0, 9, 4, 3, 3, 0, 5, 7, 2, 7, 0, 3, 6, 5, 7, 5, 9, 5, 9, 1, 9, 5, 3,
        0, 9, 2, 1, 8, 6, 1, 1, 7, 3, 8, 1, 9, 3, 2, 6, 1, 1, 7, 9, 3, 1, 0, 5, 1, 1, 8, 5, 4, 8, 0, 7, 4, 4, 6, 2, 3, 7, 9, 9, 6, 2, 7, 4, 9, 5, 6, 7, 3, 5, 1, 8, 8, 5, 7, 5, 2, 7, 2, 4, 8, 9, 1, 2, 2, 7, 9, 3, 8, 1,
        8, 3, 0, 1, 1, 9, 4, 9, 1, 2, 9, 8, 3, 3, 6, 7, 3, 3, 6, 2, 4, 4, 0, 6, 5, 6, 6, 4, 3, 0, 8, 6, 0, 2, 1, 3, 9, 4, 9, 4, 6, 3, 9, 5, 2, 2, 4, 7, 3, 7, 1, 9, 0, 7, 0, 2, 1, 7, 9, 8, 6, 0, 9, 4, 3, 7, 0, 2, 7, 7,
        0, 5, 3, 9, 2, 1, 7, 1, 7, 6, 2, 9, 3, 1, 7, 6, 7, 5, 2, 3, 8, 4, 6, 7, 4, 8, 1, 8, 4, 6, 7, 6, 6, 9, 4, 0, 5, 1, 3, 2, 0, 0, 0, 5, 6, 8, 1, 2, 7, 1, 4, 5, 2, 6, 3, 5, 6, 0, 8, 2, 7, 7, 8, 5, 7, 7, 1, 3, 4, 2,
        7, 5, 7, 7, 8, 9, 6, 0, 9, 1, 7, 3, 6, 3, 7, 1, 7, 8, 7, 2, 1, 4, 6, 8, 4, 4, 0, 9, 0, 1, 2, 2, 4, 9, 5, 3, 4, 3, 0, 1, 4, 6, 5, 4, 9, 5, 8, 5, 3, 7, 1, 0, 5, 0, 7, 9, 2, 2, 7, 9, 6, 8, 9, 2, 5, 8, 9, 2, 3, 5,
        4, 2, 0, 1, 9, 9, 5, 6, 1, 1, 2, 1, 2, 9, 0, 2, 1, 9, 6, 0, 8, 6, 4, 0, 3, 4, 4, 1, 8, 1, 5, 9, 8, 1, 3, 6, 2, 9, 7, 7, 4, 7, 7, 1, 3, 0, 9, 9, 6, 0, 5, 1, 8, 7, 0, 7, 2, 1, 1, 3, 4, 9, 9, 9, 9, 9, 9, 8, 3, 7,
        2, 9, 7, 8, 0, 4, 9, 9, 5, 1, 0, 5, 9, 7, 3, 1, 7, 3, 2, 8, 1, 6, 0, 9, 6, 3, 1, 8, 5, 9, 5, 0, 2, 4, 4, 5, 9, 4, 5, 5, 3, 4, 6, 9, 0, 8, 3, 0, 2, 6, 4, 2, 5, 2, 2, 3, 0, 8, 2, 5, 3, 3, 4, 4, 6, 8, 5, 0, 3, 5,
        2, 6, 1, 9, 3, 1, 1, 8, 8, 1, 7, 1, 0, 1, 0, 0, 0, 3, 1, 3, 7, 8, 3, 8, 7, 5, 2, 8, 8, 6, 5, 8, 7, 5, 3, 3, 2, 0, 8, 3, 8, 1, 4, 2, 0, 6, 1, 7, 1, 7, 7, 6, 6, 9, 1, 4, 7, 3, 0, 3, 5, 9, 8, 2, 5, 3, 4, 9, 0, 4,
        2, 8, 7, 5, 5, 4, 6, 8, 7, 3, 1, 1, 5, 9, 5, 6, 2, 8, 6, 3, 8, 8, 2, 3, 5, 3, 7, 8, 7, 5, 9, 3, 7, 5, 1, 9, 5, 7, 7, 8, 1, 8, 5, 7, 7, 8, 0, 5, 3, 2, 1, 7, 1, 2, 2, 6, 8, 0, 6, 6, 1, 3, 0, 0, 1, 9, 2, 7, 8, 7,
	}; 

}
