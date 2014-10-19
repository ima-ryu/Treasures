package jp.imr.game.treasures;

import android.app.Activity;
import android.os.Bundle;

/**
 * ゲーム初期化用のアクティビティ
 *
 * @author ima-ryu
 */
public class GameActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(new GameSurfaceView(this));
    }
}