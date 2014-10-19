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
 * �Q�[����ʊǗ��N���X
 *
 * @author ima-ryu
 */
public class GameSurfaceView extends SurfaceView {

	private GameCallback gameCallback;
	private GestureDetector gestureDetector;	

	// ��ʏ�̃I�u�W�F�N�g���
	private PlayerObject playerObject;
	private Set<EnemyObject> enemyObjects;

	// �Q�[���I�[�o�[�ɂȂ������ǂ���
	private boolean isGameOver = false;

	public GameSurfaceView(Context context) {
		super(context);
		
		// �L�[�C�x���g��L����
		setFocusable(true);
		requestFocus();

		gameCallback = new GameCallback(this);
		gestureDetector = new GestureDetector(context, new GameGestureListener(this)); 
		
		// ��ʏ�̃I�u�W�F�N�g������
		playerObject = new PlayerObject(this);
		enemyObjects = new HashSet<EnemyObject>();

		// �C���[�W�摜�̓ǂݍ���
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

	// ���[�U�̉�ʑ�������m�������̏���
	public void recentEvent(MotionEvent event) {
		if(isGameOver) {
			// �Q�[���I�[�o�[��Ԃŉ�ʑ��삪�������ꍇ
			gameCallback.init(); // �I�u�W�F�N�g�̈ʒu��������
			isGameOver = false; // �Q�[���ĊJ
			return;
		}
		
		// �Q�[���I�[�o�[�łȂ���Ύ��@�̑���
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
			init(); // �I�u�W�F�N�g�̈ʒu��������
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
			enemyObjects.clear(); // �G�@�̐���������

			// ��ʏ�̃I�u�W�F�N�g�����ʒu�ݒ�
			playerObject.setX(getWidth() / 2 - playerObject.getBitmap().getWidth() / 2);
			playerObject.setY(getHeight() - playerObject.getBitmap().getHeight());
			playerObject.setDx(0);
			playerObject.setDy(0);

			// �G�@�̏������
			EnemyObject enemy01 = new EnemyObject(gameSurfaceView);
			enemy01.setX(0);
			enemy01.setY(getHeight() / 2 - EnemyObject.CIRCLE_SIZE / 2);
			enemy01.setDx(10);
			enemy01.setDy(0);
			enemyObjects.add(enemy01);

			// �G�@�̏������
			EnemyObject enemy02 = new EnemyObject(gameSurfaceView);
			enemy02.setX(getWidth() - EnemyObject.CIRCLE_SIZE / 2);
			enemy02.setY(getHeight() / 2 - EnemyObject.CIRCLE_SIZE / 2);
			enemy02.setDx((float)-7.1);
			enemy02.setDy((float)-7.1);
			enemyObjects.add(enemy02);
		}
		
		// �Q�[���X�V�X���b�h�̃��C�����[�`��
	    @Override
		public void run() {
	        Canvas canvas;
	        long beginTime; // �����J�n����
	        long pastTick;  // �o�ߎ���
	        int sleep;

	        // �t���[�����[�g�̌v�Z�p
	        int frameCount = 0;
	        long beforeTick = 0;
	        long currTime = 0;
	        String tmp = "";

	        // �����̕\��
	        Paint paint = new Paint();
	        paint.setColor(Color.BLUE);
	        paint.setAntiAlias(true);
	        paint.setTextSize(24);

	        // �X���b�h�����ł��Ă��Ȃ��Ԃ͂����Ə�����������
	        while (thread != null) {

	            // �t���[�����[�g�̕\��
	            frameCount++;
	            currTime = System.currentTimeMillis();
	            if (beforeTick + 1000 < currTime) {
	                beforeTick = currTime;
	                tmp = "" + frameCount;
	                frameCount = 0;
	            }

	            canvas = surfaceHolder.lockCanvas();
				canvas.drawColor(0, Mode.CLEAR); // ��ʃN���A
				canvas.drawColor(Color.WHITE);
                canvas.drawText("FPS:" + tmp, 10, 50, paint);
				
				synchronized (this.surfaceHolder) {
                    // ���ݎ���
                    beginTime = System.currentTimeMillis();
                     
                    // �`��
                    this.move();
                    this.draw(canvas);
                     
                    // �o�ߎ���
                    pastTick = System.currentTimeMillis() - beginTime;
                     
                    // �]��������
                    sleep = (int)(Constant.ONE_FRAME_TICK - pastTick);
 
                    // �]�������Ԃ�����Α҂�
                    if (0 < sleep) {
                        try {
                            Thread.sleep(sleep);
                        } catch (Exception e) {}
                    }
        			surfaceHolder.unlockCanvasAndPost(canvas);
                }
			}
		}

	    // ���t���[���̃A�N�V����
		public void move() {
			boolean isClashed = false; // �G�ɓ����������ǂ���
			playerObject.move();
			for(EnemyObject enemy : enemyObjects) {
				enemy.move();
				isClashed = enemy.isClash(playerObject); // �����蔻��
				
				// �Q�[���I�[�o�[�ɂȂ�
				if(isClashed) {
					isGameOver = true;
					break;
				}
			}
		}

	    // ���t���[���̕`��
		public void draw(Canvas canvas) {
			/*
			canvas.save(); // ��Ԃ�ۑ�
			canvas.rotate(45.0f);
			Paint paint2 = new Paint();
			canvas.drawColor(Color.WHITE);
			paint2.setColor(Color.BLUE);
			paint2.setAntiAlias(true);
			paint2.setTextSize(24);
			canvas.drawText("Hello, SurfaceView!", paint2.getTextSize(), 0, paint2);
			canvas.restore(); // ��Ԃ𕜌�
			 */

			Paint paint = new Paint();

			// �Q�[���I�[�o�[�������ꍇ�̕\��
			if(isGameOver) {
				paint.setColor(Color.RED);
				paint.setAntiAlias(true);
				paint.setTextSize(60);

				// ��ʐ^�񒆂ɕ�����\��
				canvas.drawText("Game Over", 30, canvas.getHeight() / 2 - 30, paint);
				return;
			}

			// �Q�[���������Ă���΁A���@�ƓG�@��\��
			canvas.drawBitmap(playerObject.getBitmap(), playerObject.getX(), playerObject.getY(), paint);
			for(EnemyObject enemy : enemyObjects) {
				canvas.drawCircle( enemy.getX(), enemy.getY(), EnemyObject.CIRCLE_SIZE, paint);
			}
		}
	}
}
