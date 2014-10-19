package jp.imr.game.treasures.object;

import jp.imr.game.treasures.GameSurfaceView;
import android.graphics.Bitmap;

/**
 * ゲーム画面上に出現する各種オブジェクトの位置・動作を定義するクラス。
 * 
 * @author ima-ryu
 */
public class GameObject {
	protected GameSurfaceView gameSurfaceView;
	protected Bitmap bitmap;
	protected float x, y, dx, dy;

	public GameObject(GameSurfaceView gameSurfaceView) {
		this.gameSurfaceView = gameSurfaceView;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getDx() {
		return dx;
	}
	public void setDx(float dx) {
		this.dx = dx;
	}
	public float getDy() {
		return dy;
	}
	public void setDy(float dy) {
		this.dy = dy;
	}

	public void initPosition(float x, float y, float dx, float dy) {
	}

	public void move() {
	}
}
