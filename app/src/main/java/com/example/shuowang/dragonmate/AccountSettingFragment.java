package com.example.shuowang.dragonmate;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by shuowang on 7/31/15.
 */
public class AccountSettingFragment extends Fragment{
    EditText et_email,et_phone;
    RadioGroup rg_sex;
    RadioButton rb_male,rb_female;
    boolean sex;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting_account, container, false);
        et_email = (EditText) root.findViewById(R.id.S_EmailText);
        et_phone = (EditText) root.findViewById(R.id.S_PhoneText);
        rg_sex = (RadioGroup)root.findViewById(R.id.S_SexChoiceRadioGroup);
        rb_male = (RadioButton)root.findViewById(R.id.S_MaleChoice);
        rb_female = (RadioButton)root.findViewById(R.id.S_FemaleChoice);
        final MyUser currentUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("objectId", currentUser.getObjectId());
        query.findObjects(getActivity(), new FindListener<MyUser>() {
            @Override
            public void onSuccess(List<MyUser> list) {
                et_email.setHint(list.get(0).getEmail());
                et_phone.setHint(list.get(0).getMobilePhoneNumber());
                if (list.get(0).itsSex()) {
                    rg_sex.check(R.id.S_MaleChoice);
                } else {
                    rg_sex.check(R.id.S_FemaleChoice);
                }
                Toast.makeText(getActivity(), "查询成功.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(getActivity(), "查询失败.", Toast.LENGTH_SHORT).show();
            }
        });

        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb_male.getId()) {
                    sex = true;
                } else if (checkedId == rb_female.getId()) {
                    sex = false;
                }
            }
        });

        root.findViewById(R.id.S_UpdateAccountInfoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyUser newUser = new MyUser();
                if (!"".equals(et_email.getText().toString().trim())&&et_email.getText()!=null) {
                    newUser.setEmail(et_email.getText().toString());
                }
                if (!"".equals(et_phone.getText().toString().trim())&&et_phone.getText()!=null) {
                    newUser.setMobilePhoneNumber(et_phone.getText().toString());
                }
                newUser.setSex(sex);
                newUser.update(getActivity(), currentUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(), "updatesucceed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(getActivity(), "updatefailed", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        return root;
    }
}
