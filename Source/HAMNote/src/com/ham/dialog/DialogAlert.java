package com.ham.dialog;
import com.example.hamnote.R;
import com.ham.database.DatabaseAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.WindowManager.LayoutParams;

public class DialogAlert extends DialogFragment{
	//Context context;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int songID = getArguments().getInt("SongID");
		final int CODE = getArguments().getInt("CODE");
		final  MediaPlayer player;
		switch(songID){
			case 0:
				player = MediaPlayer.create(getActivity(), R.raw.beautifulinwhite);
				break;
			case 1:
				player = MediaPlayer.create(getActivity(), R.raw.becauseyoulive);
				break;
			case 2:
				player = MediaPlayer.create(getActivity(), R.raw.chomuamangemve);
				break;
			case 3:
				player = MediaPlayer.create(getActivity(), R.raw.emcuangayhomqua);
				break;
			case 4:
				player = MediaPlayer.create(getActivity(), R.raw.songforlove);
				break;
			case 5:
				player = MediaPlayer.create(getActivity(), R.raw.whatarewords);
				break;
			default:
					player = MediaPlayer.create(getActivity(), R.raw.beautifulinwhite);
		}
		//player = MediaPlayer.create(getActivity(), R.raw.beautifulinwhite);
			player.setVolume(60, 60);
			player.start();
		getActivity().getWindow().addFlags(LayoutParams.FLAG_TURN_SCREEN_ON | LayoutParams.FLAG_DISMISS_KEYGUARD);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("HAM Note");
		builder.setMessage("You have important note!");
		builder.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				player.stop();
				player.release();
				//Delete Important after alert
				DatabaseAdapter database  = new DatabaseAdapter(getActivity());
				database.open();
				database.DeleteImportantRecordbyCode(CODE);
				database.close();
				getActivity().finish();
			}						
		});
		
		return builder.create();
	}
	@Override
	public void onDestroy() {		
		super.onDestroy();
		getActivity().finish();
	}
}
