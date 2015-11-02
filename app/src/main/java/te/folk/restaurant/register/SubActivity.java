package te.folk.restaurant.register;

//import te.folk.restaurant.register.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SubActivity extends Activity implements OnClickListener{
	String[] log ;
	int sum[];
	int[] idListT;
	int delnum;
	int result;
	boolean check,fin;
	EditText editText;
	public static float setScaleSize(Context context) {
        //stone.pngを読み込んでBitmap型で扱う
        Bitmap _bm = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.stone);
        float width = 0;
        float _scale = 0;

        //画面サイズ取得の準備
        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();

        // AndroidのAPIレベルによって画面サイズ取得方法が異なるので条件分岐
        if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13) {
            width = disp.getWidth();

        } else {
            Point size = new Point();
            disp.getSize(size);
            width = size.x;
        }
        _scale = (float) width / (float) _bm.getWidth();
        return _scale;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity2);

		Button btn = (Button)findViewById(R.id.button_2);
		btn.setTextSize(10 * setScaleSize(this.getApplicationContext()));
		btn.setOnClickListener(this);
		Button btn2 = (Button)findViewById(R.id.button_3);
		btn2.setTextSize(10 * setScaleSize(this.getApplicationContext()));
		btn2.setOnClickListener(this);
		
		result = 0;
		Intent intent = getIntent();
		Resources bres = getResources();
        String[] itemListT = bres.getStringArray(R.array.text2);
        idListT  = new int[itemListT.length];
        //配列データからIDの取得
        for (int i = 0; i < itemListT.length; i++) {
            idListT[i] = bres.getIdentifier(itemListT[i], "id", getPackageName());
        }
        TextView[] tv = new TextView[idListT.length]; 
		for(int i = 0;i < tv.length;i++) {
			tv[i] = (TextView) findViewById(idListT[i]);
			tv[i].setTextSize(10 * setScaleSize(this.getApplicationContext()));
		}
		editText = (EditText) findViewById(R.id.editText1);
		if (intent != null) {
			log = intent.getStringArrayExtra("key");
			sum = intent.getIntArrayExtra("key2");
			for(int i = 12;i < tv.length-11;i++) {
			tv[i].setText(log[i-11]);
			}
			tv[11].setText(log[log.length -2]);//番号
			tv[tv.length -12].setText(log[0]);//合計
			tv[22].setText("全");
			
			for(int i = 23;i < tv.length -1;i++) {
				tv[i].setText(String.valueOf(sum[i-22]));
			}
			tv[32].setText(String.valueOf(sum[0]));
			
		}	
	}
	final Intent intent2 = new Intent();
	
	
	
	public void Alert(int del) {
		// 確認ダイアログの生成
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setTitle("num:"+del+"の履歴を削除します");
        alertDlg.setMessage("消してしまうがよろしいか。");
        alertDlg.setPositiveButton(
            "OK",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // OK ボタンクリック処理
                	result = 1;
        			intent2.putExtra("key2", delnum);
        			setResult(result,intent2);
        			Toast.makeText(SubActivity.this, "num:"+delnum+" を削除しました。", Toast.LENGTH_LONG)
        			.show();
        			finish();
                }
            });
        alertDlg.setNegativeButton(
            "Cancel",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Cancel ボタンクリック処理
                	
                }
            });
        // 表示
        alertDlg.create().show();	
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button_2:
			intent2.putExtra("key2", delnum);
			setResult(result,intent2);
			finish();
			break;
		case R.id.button_3:
			check = false;
			delnum = Integer.valueOf(editText.getText().toString());
			Alert(delnum);
			break;
		}
	}
}
	