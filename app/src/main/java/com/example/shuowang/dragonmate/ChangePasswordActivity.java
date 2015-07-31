package com.example.shuowang.dragonmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;


public class ChangePasswordActivity extends BaseActivity {
    EditText et_email;
    Button btn_changepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        et_email = (EditText)findViewById(R.id.CP_EmailText);
        btn_changepassword = (Button)findViewById(R.id.CP_ChangePasswordButton);
        btn_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                BmobUser.resetPasswordByEmail(ChangePasswordActivity.this, email, new ResetPasswordByEmailListener() {
                    @Override
                    public void onSuccess() {
                        toast("重置密码请求成功，请到邮箱进行密码重置操作");
                        Intent intent = new Intent();
                        intent.setClass(ChangePasswordActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        toast("重置密码失败");
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
