package jp.imr.game.treasures.object;

import jp.imr.game.treasures.GameSurfaceView;

/**
 * �Q�[����ʏ�ɏo������e��I�u�W�F�N�g�̈ʒu�E������`����N���X�B
 * 
 * @author ima-ryu
 */
public class EnemyObject extends GameObject {

	public static final int CIRCLE_SIZE = 50;
	
	public EnemyObject(GameSurfaceView gameSurfaceView) {
		super(gameSurfaceView);
	}

	// ���t���[���Ŏ��s�����A�N�V����
	@Override
	public void move() {
		// ��ʒ[�ɂ����甽�˂���
		if(x - CIRCLE_SIZE< 0) {
			x = CIRCLE_SIZE;
			dx = Math.abs(dx);
		} else if(x + CIRCLE_SIZE > gameSurfaceView.getWidth()) {
			x = gameSurfaceView.getWidth() - CIRCLE_SIZE;
			dx = -Math.abs(dx);
		}
		if(y - CIRCLE_SIZE < 0) {
			y = CIRCLE_SIZE;
			dy = Math.abs(dy);
		} else if(y + CIRCLE_SIZE > gameSurfaceView.getHeight()) {
			y = gameSurfaceView.getHeight() - CIRCLE_SIZE;
			dy = -Math.abs(dy);
		}

		x += dx;
		y += dy;	
	}

	// ���@�ƓG�Ƃ̓����蔻��
	public boolean isClash(PlayerObject playerObject) {
		float player_x = playerObject.getX() + playerObject.getBitmap().getWidth() / 2;
		float player_y = playerObject.getY() + playerObject.getBitmap().getHeight() / 2;
		double distance = distance(x, player_x, y, player_y);
		return distance <= CIRCLE_SIZE;
	}
	
	// 2�_�Ԃ̋����v�Z
	private double distance(float x1, float x2, float y1, float y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1- y2));
	}
}
