package jp.imr.game.treasures;

import android.app.Activity;
import android.os.Bundle;

/**
 * �Q�[���������p�̃A�N�e�B�r�e�B
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