package te.folk.restaurant.register;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
//import te.folk.restaurant.register.R;
import android.os.Environment;
import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CheckBox;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	int[] count = new int[10];
	int[] set_count = new int[count.length];
	int pi = 400;	int to = 250;	int na = 150;
	int cr = 150;	int in = 130;	int ch = 130;
	int ma = 100;	int ps = 600;	int ts = 500;
	int num = 0;//売り上げ・行
	int delnum;
	
	private String filePath = Environment.getExternalStorageDirectory() + "/proceeds.txt";
	private String filePath2 = "/mmt/sdcard";
   
	private final int sub   = 0;
	int[] val = new int[count.length];
	int[] total = new int[count.length];
	int[] idListB;
	int[] idListT;
	int[] idListC;
	String [][] log = new String[1023][(val.length) + 2];
	String[] send_log = new String[(val.length) +2];
	int[] sum = new int[val.length];
	Button[] button;
	TextView[] tv;
	CheckBox[] cb;
	
	final Calendar calendar = Calendar.getInstance();

	final int year = calendar.get(Calendar.YEAR);
	final int month = calendar.get(Calendar.MONTH);
	final int day = calendar.get(Calendar.DAY_OF_MONTH);
	final int hour = calendar.get(Calendar.HOUR_OF_DAY);
	final int minute = calendar.get(Calendar.MINUTE);
	
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
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 // res/values/array.xmlから配列データの取得
        Resources bres = getResources();
        String[] itemListB = bres.getStringArray(R.array.button);
        idListB  = new int[itemListB.length];
        button = new Button[idListB.length];
        //配列データからIDの取得
        for (int i = 0; i < itemListB.length; i++) {
            idListB[i] = bres.getIdentifier(itemListB[i], "id", getPackageName());
        }
        // ボタンにリスナーを設定する
        for(int i = 0;i < button.length;i++) {
        	button[i] = (Button) findViewById(idListB[i]);
        	button[i].setTextSize(8 * setScaleSize(this.getApplicationContext()));
        	//button[i].setHeight((int) (8 * setScaleSize(this.getApplicationContext())));
        	//button[i].setWidth((int) (8 * setScaleSize(this.getApplicationContext())));
        	if(30 < i&& i <36) {
        		button[i].setTextSize((int)(6 * setScaleSize(this.getApplicationContext())));
        	}
        	if(i ==14 || i == 9)
        		button[i].setTextSize((int)(14 * setScaleSize(this.getApplicationContext())));
        	button[i].setOnClickListener(this);
        }
        for(int t = 0;t < 1023;t++) {
        	for(int i = 0;i < send_log.length;i++) { 
        		send_log[i] = "";
        		log[t][i] = "";
        	}
        }
        String[] itemListT = bres.getStringArray(R.array.text);
		idListT = new int[itemListT.length];
		for (int i = 0; i < itemListT.length; i++) {
            idListT[i] = bres.getIdentifier(itemListT[i], "id", getPackageName());
        }
		tv = new TextView[idListT.length]; 
		for(int i = 0;i < tv.length;i++) {
			tv[i] = (TextView) findViewById(idListT[i]);
			if(i != tv.length-1)
				tv[i].setTextSize(10 * setScaleSize(this.getApplicationContext()));
			else
				tv[i].setTextSize(14 * setScaleSize(this.getApplicationContext()));
			//tv[i].setHeight((int)(14 * setScaleSize(this.getApplicationContext())));
		}
		//チェックボックス配置
		String[] itemListC = bres.getStringArray(R.array.check);
		idListC = new int[itemListC.length];
		for (int i = 0; i < itemListC.length; i++) {
            idListC[i] = bres.getIdentifier(itemListC[i], "id", getPackageName());
        }
		cb = new CheckBox[idListC.length]; 
		for(int i = 0;i < cb.length;i++) {
			cb[i] = (CheckBox) findViewById(idListC[i]);
			cb[i].setTextSize(8 * setScaleSize(this.getApplicationContext()));
			cb[i].setOnClickListener(this);
		}
        
	}
	
	public void clear_tx(TextView[] tv,int leng) {
		for(int t = 0;t < leng;t++) {
			tv[t].setText("数 0");
		}
		tv[leng].setText(" ");
		button[31].setText("ナッツチョコ：0");
		button[32].setText("クレープキヌア：0");
		button[33].setText("インカ：0");
		button[34].setText("チチャ：0");
		button[35].setText("マテ茶：0");
		
	}
	
	public void tx_ch(int n,int v,TextView tv) {
		if(count[n] < 0) count[n] = 0;
		val[n] = v*count[n];
		tv.setText("数 "+count[n]+"  "+v+" × "+count[n] +" = " +v*count[n]);
	}
	public void make_log(String[][] log) {
		for(int c = 0;c < sum.length ;c++) sum[c] = 0;
		for(int t = 0;t < 1023;t++) 
        	for(int i = 0;i < send_log.length;i++)  
        		send_log[i] = "";
		for(int n = 0;n < num;n++) { 
			for(int t = 0;t < val.length;t++) { 
				if(log[n][(val.length) +1].equals("1")) {//デリートの確認
					send_log[t] += "--\n";
				}
				else {
					System.out.println(sum[t]);
					send_log[t] += log[n][t] +"\n";
					sum[t] += Integer.parseInt(log[n][t]); 
				}
			}
			send_log[val.length] += String.valueOf(n) +"\n";
		}
	}
	public boolean check_set() {
		boolean ch = false;
		int set,des,drn;
		set = count[8] + count[9];
		des = set_count[0] +set_count[1];
		drn = set_count[2] +set_count[3] +set_count[4];
		if(set == des && set == drn ) ch = true;
		else {
			Toast.makeText(MainActivity.this,
					"セットの数とデザート・ドリンクの数が合っていません。",
					Toast.LENGTH_LONG).show(); 
            ch = false;
		}
		return ch;
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		final Intent intent2 = new Intent(MainActivity.this, SubActivity.class);
		super.onActivityResult(requestCode, resultCode, data);
	    Bundle bundle = data.getExtras();
	    
	    switch (requestCode) {
	    case sub:
	      if (resultCode == 1) {
	    	  log[bundle.getInt("key2")][val.length+1] = String.valueOf("1");
	    	  make_log(log);
	    	  intent2.putExtra("key", send_log);	
	    	  intent2.putExtra("key2",sum);	
	    	  startActivityForResult(intent2,sub);
	      }
	      break;
	 
	    default:
	      break;
	    }
	}

	
    
	public void onClick(View v) {
		//テキスト配置
		/*
		Resources bres = getResources();
		String[] itemListT = bres.getStringArray(R.array.text);
		idListT = new int[itemListT.length];
		for (int i = 0; i < itemListT.length; i++) {
            idListT[i] = bres.getIdentifier(itemListT[i], "id", getPackageName());
        }
		TextView[] tv = new TextView[idListT.length]; 
		for(int i = 0;i < tv.length;i++) {
			tv[i] = (TextView) findViewById(idListT[i]);
			tv[i].setTextSize(14 * setScaleSize(this.getApplicationContext()));
			//tv[i].setHeight((int)(14 * setScaleSize(this.getApplicationContext())));
		}
		//チェックボックス配置
		String[] itemListC = bres.getStringArray(R.array.check);
		idListC = new int[itemListC.length];
		for (int i = 0; i < itemListC.length; i++) {
            idListC[i] = bres.getIdentifier(itemListC[i], "id", getPackageName());
        }
		CheckBox[] cb = new CheckBox[idListC.length]; 
		for(int i = 0;i < cb.length;i++) {
			cb[i] = (CheckBox) findViewById(idListC[i]);
			cb[i].setTextSize(14 * setScaleSize(this.getApplicationContext()));
			cb[i].setOnClickListener(this);
		}
		*/
		final Intent intent = new Intent(MainActivity.this, SubActivity.class);
		switch(v.getId()) {
		case R.id.button1://ピカンテ
		case R.id.button12:
			count[1]++;
			tx_ch(1,pi,tv[0]);
			break;
		case R.id.button2://トルティーヤ
		case R.id.button16:
			count[2]++;
			tx_ch(2,to,tv[1]);
			break;
		case R.id.button3://アロス
		case R.id.button18:
			count[3]++;
			tx_ch(3,na,tv[2]);
			break;
		case R.id.button4://カシューナッツ
		case R.id.button20:
			count[4]++;
			tx_ch(4,cr,tv[3]);
			break;
		case R.id.button5://インカコーラ
		case R.id.button22:
			count[5]++;
			tx_ch(5,in,tv[4]);
			break;
		case R.id.button6://チチャモラーダ
		case R.id.button24:
			count[6]++;
			tx_ch(6,ch,tv[5]);
			break;
		case R.id.button7://マテ茶
		case R.id.button26:
			count[7]++;
			if(!cb[1].isChecked()){ //割引なし
				tx_ch(7,ma,tv[6]);
			}else{
				tv[6].setText("数 "+count[7]+"  "+ "無料  0");
				val[7] = 0;
			}
			break;
		case R.id.button8://ピカンテセット
		case R.id.button28:
			count[8]++;
			if(!cb[1].isChecked()){ //割引なし
				tx_ch(8,ps,tv[7]);
			}else{
				val[8]= (ps - 50) *count[8];
				tv[7].setText("数 "+count[8]+" ( " +ps+" - 割引 50)"+" × "+count[8] +" = "+val[8]);
			}
			break;			
		case R.id.button9://トルテセット
		case R.id.button30:
			count[9]++;
			if(!cb[1].isChecked()){ //割引なし
				tx_ch(9,ps,tv[8]);
			}else{
				val[9]= (ps - 50) *count[9];
				tv[8].setText("数 "+count[9]+" ( " +ts+" - 割引 50)"+" × "+count[9] +" = "+val[9]);
			}
			break;

			//セット用オプション31-41
		case R.id.button32:
			count[3]++;
			set_count[0]++;
			button[31].setText("ナッツチョコ："+count[3]);
			break;
		case R.id.button33:
			count[4]++;
			set_count[1]++;
			button[32].setText("クレープキヌア："+count[4]);
			break;
		case R.id.button34:
			count[5]++;
			set_count[2]++;
			button[33].setText("インカ："+count[5]);
			break;
		case R.id.button35:
			count[6]++;
			set_count[3]++;
			button[34].setText("チチャ："+count[6]);
			break;
		case R.id.button36:
			count[7]++;
			set_count[4]++;
			button[35].setText("マテ茶："+count[7]);
			break;
		case R.id.checkBox1:
			if(!cb[0].isChecked()){ //割引なし
				tx_ch(7,ma,tv[6]);
			}else{
				tv[6].setText("数 "+count[7]+"  "+ "無料  0");
				val[7] = 0;
			}
			break;
		case R.id.checkBox2:
			if(!cb[1].isChecked()){ //割引なし
				tx_ch(8,ps,tv[7]);
			}else{
				val[8]= (ps - 50) *count[8];
				tv[7].setText("数 "+count[8]+" ( " +ps+" - 割引 50)"+" × "+count[8] +" = "+val[8]);
			}
			break;
		case R.id.button10://合計計算
			if(check_set()) {
				val[0] = 0;
				for(int t = 1;t <val.length;t++) val[0] +=val[t];
				tv[9].setText("¥ "+val[0]);
			}
			break;
		case R.id.button11://クリア
			for(int t = 0;t <val.length;t++) {
				val[t] = 0;
				count[t] = 0;
				set_count[t] = 0;
			}
			clear_tx(tv,idListT.length -1);
			break;
			//増減ボタン
		case R.id.button13:
			count[1]--;
			tx_ch(1,pi,tv[0]);
			break;
		case R.id.button17:
			count[2]--;
			tx_ch(2,to,tv[1]);
			break;
		case R.id.button19:
			count[3]--;
			tx_ch(3,na,tv[2]);
			break;
		case R.id.button21:
			count[4]--;
			tx_ch(4,cr,tv[3]);
			break;
		case R.id.button23:
			count[5]--;
			tx_ch(5,in,tv[4]);
			break;
		case R.id.button25:
			count[6]--;
			tx_ch(6,ch,tv[5]);
			break;
		case R.id.button27:
			if(count[7] >0) count[7]--;
			if(!cb[0].isChecked()){ //割引なし
				tx_ch(7,ma,tv[6]);
			}else{
				tv[6].setText("数 "+count[7]+"  "+ "無料  0");
				val[7] = 0;
			}
			break;
		case R.id.button29:
			if(count[8] > 0) count[8]--;
			if(!cb[1].isChecked()){ //割引なし
				tx_ch(8,ps,tv[7]);
			}else{
				val[8]= ps*count[8]-(50*set_count[4]);
				tv[7].setText("数 "+count[8]+" ( " +ps+" - 割引 50)"+" × "+count[8] +" = "+val[8]);
			}
			break;
		case R.id.button31:
			count[9]--;
			tx_ch(9,ts,tv[8]);
			break;
			//増減ボタン終わり
		case R.id.button14: //画面切り替え			 			
			intent.putExtra("key", send_log);
			intent.putExtra("key2", sum);
			startActivityForResult(intent,sub);
			break;
		case R.id.button15: //記録ボタン
			/*
			 * 売り上げの二重表示　画面遷移ぼたんのみで
			 */
			//売り上げ状況の更新
			if(check_set()) {
			val[0] = 0;
			for(int t = 1;t <val.length;t++) val[0] +=val[t];
			boolean check_0 = false;
			for(int t = 1;t < val.length;t++) {
				if(!(count[t]==0)) check_0 = true;
			}
			if(check_0 == true) {
				for(int t = 1;t < val.length;t++) { 
					log[num][t] = String.valueOf(count[t]);
				}
				log[num][0] = String.valueOf(val[0]);
				log[num][val.length] = String.valueOf(num);
				
			     String sdCardState = Environment.getExternalStorageState();                 
                //上で取得したSDカードの状態毎に処理を分離
                //書き込み処理が可能な場合
                if (sdCardState.equals(Environment.MEDIA_MOUNTED)) {                    
             
                    FileOutputStream fos = null;                     
                    try {                        
                        fos = new FileOutputStream(filePath);
                        for(int t = 0;t < val.length;t++) {                        	
                        fos.write(((log[num][t])+",").getBytes());
                        }
                        fos.write("\n".getBytes());
                     
                        Toast.makeText(MainActivity.this, "保存しました。"+filePath, Toast.LENGTH_SHORT).show(); 
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "登録できませんでした。SDカードを確認してください。", Toast.LENGTH_LONG)  .show();                        
                    } finally {
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } 
                    }
                     
                //SDカードが読取専用の場合
                } else if (sdCardState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
                    Toast.makeText(MainActivity.this, "このSDカードは読取専用です。", Toast.LENGTH_LONG).show(); 
                //SDカードが挿入されていない場合
                } else if (sdCardState.equals(Environment.MEDIA_REMOVED)) {
                    Toast.makeText(MainActivity.this, "SDカードが挿入されていません。", Toast.LENGTH_LONG).show();                     
                //SDカードがマウントされていない場合
                } else if (sdCardState.equals(Environment.MEDIA_UNMOUNTED)) {
                    Toast.makeText(MainActivity.this, "SDカードがマウントされていません。", Toast.LENGTH_LONG).show();                
                //その他
                } else {
                    Toast.makeText(MainActivity.this, "SDカードを確認してください。", Toast.LENGTH_LONG).show();
                }
            
			   
				num++;
				make_log(log);
			}
			/*log[][]の構造
			 * val c1~c9 num del
			 */
			//if(check_0) log[0] += (val[0]+"\n");
			check_0 = false;
			for(int t = 0;t <val.length;t++) {
				total[t] += count[t];
				val[t] = 0;
				count[t] = 0;
				set_count[t] = 0;
			}
			clear_tx(tv,idListT.length -1);//クリア
			}
			break;
			/*
			 *
			 * //SDカードの状態を取得
			String sdCardState2 = Environment.getExternalStorageState();
			//メディアがマウントされている、もしくは読取用としてマウントしている場合
			if (sdCardState2.equals(Environment.MEDIA_MOUNTED) || sdCardState2.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {			                     			
				FileInputStream fis = null;
				try {
			
					fis = new FileInputStream(filePath);
			
					byte[] buffer = new byte[100];
					fis.read(buffer);
					String str = new String(buffer).trim();
					Toast.makeText(MainActivity.this, str + "さんが登録されています。", Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(MainActivity.this, "登録できませんでした。", Toast.LENGTH_LONG).show();
				}
			}
			 * クリアのデバック
			 * ピカンテセット６００
			 * ピカンテ・デザート・飲み物
			 * トルティーヤセット５００
			 * トル・デザ・飲み物
			 * git
			 * 対応できない場合
			 * セット３　ドリンク４　３つは無料扱い　もう一つは何？100,130
			 * 
			 * セット３　マテ茶２　ほか１の場合の割引
			 * クリア　チェックボックス
			 * 
			 */
		}	
	}
}

