package com.example.shuowang.dragonmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends BaseActivity {
    EditText et_username,et_password;
    Button btn_login,btn_changepassword,btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username = (EditText)findViewById(R.id.L_UserNameText);
        et_password = (EditText)findViewById(R.id.L_PassWordText);
        btn_register = (Button)findViewById(R.id.L_RegisterButton);
        btn_login = (Button)findViewById(R.id.L_LoginButton);
        btn_changepassword = (Button)findViewById(R.id.L_ChangPasswordButton);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                final MyUser user = new MyUser();
                user.setUsername(username);
                user.setPassword(password);
                user.login(LoginActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        if(user.getEmailVerified()) {
                            toast("succeed");
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            toast("please verify your email address");
                        }
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        toast("failed");
                    }
                });
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
