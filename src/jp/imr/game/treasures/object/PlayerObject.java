package jp.imr.game.treasures.object;

import android.view.MotionEvent;
import jp.imr.game.treasures.Constant;
import jp.imr.game.treasures.GameSurfaceView;

/**
 * �Q�[����ʏ�ɏo������e��I�u�W�F�N�g�̈ʒu�E������`����N���X�B
 * 
 * @author ima-ryu
 */
public class PlayerObject extends GameObject {

	public PlayerObject(GameSurfaceView gameSurfaceView) {
		super(gameSurfaceView);
	}

	// ���t���[���Ŏ��s�����A�N�V����
	@Override
	public void move() {
		// ��ʒ[�ɂ����甽�˂���
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

	// ���[�U�̑���ɉ������A�N�V����
	public void manualMove(MotionEvent e) {
		float touch_x = e.getX();
		float touch_y = e.getY();

		// ��ʏ�ŁA���[�U���������ʒu�Ɍ������Đi��
		dx = (touch_x - x) / Constant.ONE_FRAME_TICK;
		dy = (touch_y - y) / Constant.ONE_FRAME_TICK;
	}
}
