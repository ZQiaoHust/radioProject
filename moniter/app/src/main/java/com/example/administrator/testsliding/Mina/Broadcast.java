package com.example.administrator.testsliding.Mina;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.example.administrator.testsliding.bean2server.ListMap;
import com.example.administrator.testsliding.bean2server.List_StationAll;
import com.example.administrator.testsliding.bean2server.List_TerminalOnline;

import java.util.ArrayList;

public class Broadcast {

	public static void sendBroadCast(Context context, String action,
			String key, Parcelable value) {
		Intent intent = new Intent();
		intent.setAction(action);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(key,value);
		context.sendBroadcast(intent);
	}

	public static void sendBroadCastRadioList(Context context, String action,
			String key, ArrayList<ListMap> value) {

		Intent intent = new Intent();
		intent.setAction(action);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putParcelableArrayListExtra(key, value);
		context.sendBroadcast(intent);
	}
	public static void sendBroadCastTerminalAllList(Context context, String action,
											  String key, ArrayList<List_StationAll> value) {

		Intent intent = new Intent();
		intent.setAction(action);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putParcelableArrayListExtra(key, value);
		context.sendBroadcast(intent);
	}

	public static void sendBroadCastTerminalOnlineList(Context context, String action,
													String key, ArrayList<List_TerminalOnline> value) {

		Intent intent = new Intent();
		intent.setAction(action);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putParcelableArrayListExtra(key, value);
		context.sendBroadcast(intent);
	}

	// public static void toPage(Context context,int index) {
	// String action = Constants.WHERE_PAGER_ACTION;
	// String key = Constants.WHERE_PAGER_KEY;
	// Broadcast.sendBroadCast(context, action, key, index);
	// }
}
