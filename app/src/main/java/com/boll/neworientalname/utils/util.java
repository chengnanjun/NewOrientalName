package com.boll.neworientalname.utils;


import android.content.Context;
import android.text.TextUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class util {
    static InputStream is = null;
    /**
     * 判断文件是否是照片
     */
    public static boolean isPict(String fString, Context context) {
        try {
            is = context.getAssets().open(fString);
            byte[] buffer = new byte[2]; //文件类型代码
            String filecode = ""; //文件类型
            String fileType = ""; //通过读取出来的前两个字节来判断文件类型
            if (is.read(buffer) != -1) {
                for (int i = 0; i < buffer.length; i++) { //获取每个字节与0xFF进行与运算来获取高位，
                    // 这个读取出来的数据不是出现负数 //并转换成字符串
                    filecode += Integer.toString((buffer[i]&0xFF));
                } // 把字符串再转换成Integer进行类型判断
                switch (Integer.parseInt(filecode))
                { case 7790: fileType = "exe";
                    break;
                    case 7784:
                        fileType = "midi";
                        break;
                    case 8297:
                        fileType = "rar";
                        break;
                    case 8075:
                        fileType = "zip";
                        break;
                    case 255216:
                        fileType = "jpg";
                        break;
                    case 7173:
                        fileType = "gif";
                        break;
                    case 6677:
                        fileType = "bmp";
                        break;
                    case 13780:
                        fileType = "png";
                        break;
                    default:
                        fileType = "unknown type: " + filecode;
                }
            }
            System.out.println(fileType);
            if (fileType.equals("png") || fileType.equals("gif") || fileType.equals("jpg")) {
                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 接收主界面返回的值，进行转换
     * @param gradeCode
     * @return
     */
    public static String getGradeCodeByString(int gradeCode){
        String grade = "一年级";
        if(!TextUtils.isEmpty(grade)){
            switch (gradeCode){
                case 1:
                    grade = "一年级";
                    break;
                case 2:
                    grade = "二年级";
                    break;
                case 3:
                    grade = "三年级";
                    break;
                case 4:
                    grade = "四年级";
                    break;
                case 5:
                    grade = "五年级";
                    break;
                case 6:
                    grade = "六年级";
                    break;
                case 7:
                    grade = "七年级";
                    break;
                case 8:
                    grade = "八年级";
                    break;
                case 9:
                    grade = "九年级";
                    break;
                case 10:
                    grade = "高中";
                    break;
            }
        }
        return grade;
    }

}
