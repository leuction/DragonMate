package com.example.shuowang.dragonmate;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.greenrobot.event.EventBus;

/**
 * Created by shuowang on 7/31/15.
 */
public class AccountSettingFragment extends Fragment{

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private static int CAMERA_PIC_REQUEST = 0;
    private static int RESULT_LOAD_IMAGE = 1;
    EditText et_email,et_phone;
    RadioGroup rg_sex;
    RadioButton rb_male,rb_female;
    ImageView iv_avatar,SL_iv_avatar;
    boolean sex,avatarHasBeenChanged=false;
    String path,path_url;
    File destination;
    int house,car,marriage,education,salary;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting_account, container, false);
        et_email = (EditText) root.findViewById(R.id.S_EmailText);
        et_phone = (EditText) root.findViewById(R.id.S_PhoneText);
        rg_sex = (RadioGroup)root.findViewById(R.id.S_SexChoiceRadioGroup);
        rb_male = (RadioButton)root.findViewById(R.id.S_MaleChoice);
        rb_female = (RadioButton)root.findViewById(R.id.S_FemaleChoice);
        iv_avatar = (ImageView)root.findViewById(R.id.S_profile_image);
        String fileName = dateToString(new Date(),"yyyy-MM-dd-hh-mm-ss");
        destination = new File(Environment.getExternalStorageDirectory(), fileName + ".jpg");


        final MyUser currentUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.getObject(getActivity(), currentUser.getObjectId(), new GetListener<MyUser>() {
            @Override
            public void onSuccess(MyUser myUser) {


                house=myUser.itsHouse();
                car=myUser.itsCar();
                marriage=myUser.itsMarriage();
                education=myUser.itsEducation();
                salary=myUser.itsSalary();


                if (myUser.isAvatarInit()) {
                    UrlImageViewHelper.setUrlDrawable(iv_avatar,myUser.getMyAvatar().getFileUrl(getActivity()));
                } else {
                    UrlImageViewHelper.setUrlDrawable(iv_avatar,"http://file.bmob.cn/M01/AB/44/oYYBAFW8UkWATmoPAACguaHH6So482.jpg");
                }
                et_email.setHint(myUser.getEmail());
                et_phone.setHint(myUser.getMobilePhoneNumber());
                if (myUser.itsSex()) {
                    rg_sex.check(R.id.S_MaleChoice);
                } else {
                    rg_sex.check(R.id.S_FemaleChoice);
                }
                Toast.makeText(getActivity(), "查询成功.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
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
                newUser.setEducation(education);
                newUser.setSalary(salary);
                newUser.setHouse(house);
                newUser.setCar(car);
                newUser.setMarriage(marriage);
                if (!"".equals(et_email.getText().toString().trim()) && et_email.getText() != null) {
                    newUser.setEmail(et_email.getText().toString());
                }
                if (!"".equals(et_phone.getText().toString().trim()) && et_phone.getText() != null) {
                    newUser.setMobilePhoneNumber(et_phone.getText().toString());
                }
                newUser.setSex(sex);
                if(avatarHasBeenChanged){
                    final BmobFile file = new BmobFile(new File(path));
                    file.upload(getActivity(), new UploadFileListener() {
                        @Override
                        public void onSuccess() {
                            newUser.setMyAvatar(file);
                            avatarHasBeenChanged = false;
                            Toast.makeText(getActivity(), "头像上传成功", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new OnAvatarChangedEvent(path));
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

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT).show();
                            avatarHasBeenChanged = false;
                        }
                    });

                }else {
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

            }
        });
        //打开相册
        root.findViewById(R.id.S_SelectPhotoFromGalleryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });
        //打开摄像头
        root.findViewById(R.id.S_TakePhotoFromCameraButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
            }
        });

        return root;
    }
    //从相册中查找图片
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE
                && resultCode == Activity.RESULT_OK) {
            path = getPathFromGallery(data, getActivity());
            if (path != null) {
                setFullImageFromFilePath(iv_avatar, path);
                avatarHasBeenChanged = true;
            }
        }
        //打开摄像头
        if (requestCode == CAMERA_PIC_REQUEST
                && resultCode == Activity.RESULT_OK) {
            //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            //iv_avatar.setImageBitmap(bitmap);
            try {
                FileInputStream in = new FileInputStream(destination);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 10;
                path = destination.getAbsolutePath();
                //tvPath.setText(imagePath);
                Bitmap bmp = BitmapFactory.decodeStream(in, null, options);
                iv_avatar.setImageBitmap(bmp);
                avatarHasBeenChanged = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public static String getPathFromGallery(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }
    public void setFullImageFromFilePath(ImageView imageView, String path) {
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
    }
    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }


}