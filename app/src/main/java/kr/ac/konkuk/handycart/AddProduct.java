package kr.ac.konkuk.handycart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

public class AddProduct extends AppCompatActivity {

    private DBHelper mydb;
    TextView name;
    TextView price;
    int id = 0;
    EditText productName;
    EditText productPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {    //제품 이름 가격 표시란
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        name = (TextView) findViewById(R.id.productName);
        price = (TextView) findViewById(R.id.productPrice);

        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                Cursor rs = mydb.getData(Value);
                id = Value;
                rs.moveToFirst();
                String n = rs.getString(rs.getColumnIndex(DBHelper.PRODUCTS_COLUMN_NAME));  //제품 이름 입력{QRScan 시 자동으로 입력)
                String p = rs.getString(rs.getColumnIndex(DBHelper.PRODUCTS_COLUMN_PRICE)); //제품 가격 입력(QRscan 시 자동으로 입력)

                if (!rs.isClosed()) {
                    rs.close();
                }
                Button b = (Button) findViewById(R.id.button1);
                b.setVisibility(View.INVISIBLE);

                name.setText((CharSequence) n);
                price.setText((CharSequence) p);

            }
        }
    }

    public void insert(View view) { //제품 데이터 입력
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (mydb.updateProduct(id, name.getText().toString(),price.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "수정되었음", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), kr.ac.konkuk.handycart.MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "수정되지 않았음", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (mydb.insertProduct(name.getText().toString(), price.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "추가되었음", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "추가되지 않았음", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }
    }

    public void delete(View view) { //입력된 제품 데이터 삭제
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                mydb.deleteProduct(id);
                Toast.makeText(getApplicationContext(), "삭제되었음", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "삭제되지 않았음", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void scan(View view) {
        new IntentIntegrator(this).initiateScan(); //qr코드 스캐너 실행
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //qr코드 스캔을 통해 얻은 결과값
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
            else {  //스캔 성공
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                productName.setText(result.getContents());  //qr코드 내의 제품 이름 데이터 삽입
                productPrice.setText(result.getContents()); //qr코드 내의 제품 가격 데이터 삽입
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}