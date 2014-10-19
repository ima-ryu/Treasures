package jp.imr.game.treasures;

import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class GameGestureListener extends SimpleOnGestureListener {

	private GameSurfaceView gameSurfaceView;
	
	public GameGestureListener(GameSurfaceView gameSurfaceView) {
		this.gameSurfaceView = gameSurfaceView;
	}
	
	// 画面上で指をスライドさせた時のコールバック
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		Log.i(Constant.GAME_TAG, "onScroll: " + distanceX + "," + distanceY);
		return true;
	}
	
	// 画面に指でタッチした時のコールバック
	@Override
    public boolean onDown(MotionEvent e) {
		Log.i(Constant.GAME_TAG, "onDown: " + e.getX()+ "," + e.getY());
		gameSurfaceView.recentEvent(e);
        return true;
    }
}
