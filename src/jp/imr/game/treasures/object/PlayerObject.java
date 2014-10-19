package jp.imr.game.treasures.object;

import android.view.MotionEvent;
import jp.imr.game.treasures.Constant;
import jp.imr.game.treasures.GameSurfaceView;

/**
 * ゲーム画面上に出現する各種オブジェクトの位置・動作を定義するクラス。
 * 
 * @author ima-ryu
 */
public class PlayerObject extends GameObject {

	public PlayerObject(GameSurfaceView gameSurfaceView) {
		super(gameSurfaceView);
	}

	// 毎フレームで実行されるアクション
	@Override
	public void move() {
		// 画面端にきたら反射する
		if(x < 0) {
			x = 0;
			dx = Math.abs(dx);
		} else if(x + bitmap.getWidth() > gameSurfaceView.getWidth()) {
			x = gameSurfaceView.getWidth() - bitmap.getWidth();
			dx = -Math.abs(dx);
		}
		if(y < 0) {
			y = 0;
			dy = Math.abs(dy);
		} else if(y + bitmap.getHeight() > gameSurfaceView.getHeight()) {
			y = gameSurfaceView.getHeight() - bitmap.getHeight();
			dy = -Math.abs(dy);
		}

		x += dx;
		y += dy;	
	}

	// ユーザの操作に応じたアクション
	public void manualMove(MotionEvent e) {
		float touch_x = e.getX();
		float touch_y = e.getY();

		// 画面上で、ユーザが押した位置に向かって進む
		dx = (touch_x - x) / Constant.ONE_FRAME_TICK;
		dy = (touch_y - y) / Constant.ONE_FRAME_TICK;
	}
}
