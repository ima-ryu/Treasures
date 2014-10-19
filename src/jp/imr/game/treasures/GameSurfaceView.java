package jp.imr.game.treasures;

import java.util.HashSet;
import java.util.Set;

import jp.imr.game.treasures.object.EnemyObject;
import jp.imr.game.treasures.object.PlayerObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * ゲーム画面管理クラス
 *
 * @author ima-ryu
 */
public class GameSurfaceView extends SurfaceView {

	private GameCallback gameCallback;
	private GestureDetector gestureDetector;	

	// 画面上のオブジェクト情報
	private PlayerObject playerObject;
	private Set<EnemyObject> enemyObjects;

	// ゲームオーバーになったかどうか
	private boolean isGameOver = false;

	public GameSurfaceView(Context context) {
		super(context);
		
		// キーイベントを有効化
		setFocusable(true);
		requestFocus();

		gameCallback = new GameCallback(this);
		gestureDetector = new GestureDetector(context, new GameGestureListener(this)); 
		
		// 画面上のオブジェクト初期化
		playerObject = new PlayerObject(this);
		enemyObjects = new HashSet<EnemyObject>();

		// イメージ画像の読み込み
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.droid);
		playerObject.setBitmap(bitmap);

		getHolder().addCallback(gameCallback);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return true;
	}

	// ユーザの画面操作を検知した時の処理
	public void recentEvent(MotionEvent event) {
		if(isGameOver) {
			// ゲームオーバー状態で画面操作があった場合
			gameCallback.init(); // オブジェクトの位置を初期化
			isGameOver = false; // ゲーム再開
			return;
		}
		
		// ゲームオーバーでなければ自機の操作
		playerObject.manualMove(event);
	}

	public class GameCallback implements SurfaceHolder.Callback, Runnable {
		private GameSurfaceView gameSurfaceView;
		private SurfaceHolder surfaceHolder;
		private Thread thread;

		public GameCallback(GameSurfaceView gameSurfaceView) {
			this.gameSurfaceView = gameSurfaceView;
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			init(); // オブジェクトの位置を初期化
			surfaceHolder = holder;
			thread = new Thread(this);
			thread.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			thread = null;
			surfaceHolder = null;
		}

		public void init() {
			enemyObjects.clear(); // 敵機の数を初期化

			// 画面上のオブジェクト初期位置設定
			playerObject.setX(getWidth() / 2 - playerObject.getBitmap().getWidth() / 2);
			playerObject.setY(getHeight() - playerObject.getBitmap().getHeight());
			playerObject.setDx(0);
			playerObject.setDy(0);

			// 敵機の初期状態
			EnemyObject enemy01 = new EnemyObject(gameSurfaceView);
			enemy01.setX(0);
			enemy01.setY(getHeight() / 2 - EnemyObject.CIRCLE_SIZE / 2);
			enemy01.setDx(10);
			enemy01.setDy(0);
			enemyObjects.add(enemy01);

			// 敵機の初期状態
			EnemyObject enemy02 = new EnemyObject(gameSurfaceView);
			enemy02.setX(getWidth() - EnemyObject.CIRCLE_SIZE / 2);
			enemy02.setY(getHeight() / 2 - EnemyObject.CIRCLE_SIZE / 2);
			enemy02.setDx((float)-7.1);
			enemy02.setDy((float)-7.1);
			enemyObjects.add(enemy02);
		}
		
		// ゲーム更新スレッドのメインルーチン
	    @Override
		public void run() {
	        Canvas canvas;
	        long beginTime; // 処理開始時間
	        long pastTick;  // 経過時間
	        int sleep;

	        // フレームレートの計算用
	        int frameCount = 0;
	        long beforeTick = 0;
	        long currTime = 0;
	        String tmp = "";

	        // 文字の表示
	        Paint paint = new Paint();
	        paint.setColor(Color.BLUE);
	        paint.setAntiAlias(true);
	        paint.setTextSize(24);

	        // スレッドが消滅していない間はずっと処理し続ける
	        while (thread != null) {

	            // フレームレートの表示
	            frameCount++;
	            currTime = System.currentTimeMillis();
	            if (beforeTick + 1000 < currTime) {
	                beforeTick = currTime;
	                tmp = "" + frameCount;
	                frameCount = 0;
	            }

	            canvas = surfaceHolder.lockCanvas();
				canvas.drawColor(0, Mode.CLEAR); // 画面クリア
				canvas.drawColor(Color.WHITE);
                canvas.drawText("FPS:" + tmp, 10, 50, paint);
				
				synchronized (this.surfaceHolder) {
                    // 現在時刻
                    beginTime = System.currentTimeMillis();
                     
                    // 描画
                    this.move();
                    this.draw(canvas);
                     
                    // 経過時間
                    pastTick = System.currentTimeMillis() - beginTime;
                     
                    // 余った時間
                    sleep = (int)(Constant.ONE_FRAME_TICK - pastTick);
 
                    // 余った時間があれば待つ
                    if (0 < sleep) {
                        try {
                            Thread.sleep(sleep);
                        } catch (Exception e) {}
                    }
        			surfaceHolder.unlockCanvasAndPost(canvas);
                }
			}
		}

	    // 毎フレームのアクション
		public void move() {
			boolean isClashed = false; // 敵に当たったかどうか
			playerObject.move();
			for(EnemyObject enemy : enemyObjects) {
				enemy.move();
				isClashed = enemy.isClash(playerObject); // 当たり判定
				
				// ゲームオーバーになる
				if(isClashed) {
					isGameOver = true;
					break;
				}
			}
		}

	    // 毎フレームの描画
		public void draw(Canvas canvas) {
			/*
			canvas.save(); // 状態を保存
			canvas.rotate(45.0f);
			Paint paint2 = new Paint();
			canvas.drawColor(Color.WHITE);
			paint2.setColor(Color.BLUE);
			paint2.setAntiAlias(true);
			paint2.setTextSize(24);
			canvas.drawText("Hello, SurfaceView!", paint2.getTextSize(), 0, paint2);
			canvas.restore(); // 状態を復元
			 */

			Paint paint = new Paint();

			// ゲームオーバーだった場合の表示
			if(isGameOver) {
				paint.setColor(Color.RED);
				paint.setAntiAlias(true);
				paint.setTextSize(60);

				// 画面真ん中に文字を表示
				canvas.drawText("Game Over", 30, canvas.getHeight() / 2 - 30, paint);
				return;
			}

			// ゲームが続いていれば、自機と敵機を表示
			canvas.drawBitmap(playerObject.getBitmap(), playerObject.getX(), playerObject.getY(), paint);
			for(EnemyObject enemy : enemyObjects) {
				canvas.drawCircle( enemy.getX(), enemy.getY(), EnemyObject.CIRCLE_SIZE, paint);
			}
		}
	}
}
