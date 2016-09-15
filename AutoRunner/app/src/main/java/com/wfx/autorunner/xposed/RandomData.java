package com.wfx.autorunner.xposed;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


public class RandomData {

    private static String HexadecimalChars = "abcdef0123456789";
    private static String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static String[] browserOV = {"535.19", "537.36", "536.26",
            "533.1", "534.24", "534.30"};

    private static String province = null;

    public static String randomBSSID() {

        String res = "";
        Random rnd = new Random();
        int leng = HexadecimalChars.length();

        for (int i = 0; i < 17; i++) {
            if (i % 3 == 2)
                res = res + ":";
            else
                res = res + HexadecimalChars.charAt(rnd.nextInt(leng));
        }

        return res;
    }


    public static String randomHexadecimalString(int n) {

        String res = "";
        Random rnd = new Random();
        int leng = chars.length();
        for (int i = 0; i < n; i++) {
            res = res + chars.charAt(rnd.nextInt(leng));
        }
        return res;

    }

    public static int getScanResultLevel(int rssi) {
        // RSSI = level - NoiceFloor
        int NoiceFloor = -96;
        return rssi + NoiceFloor;
    }


    public static String randomSSID() {
        String res = "";
        Random rnd = new Random();
        int leng = chars.length();
        int n = rnd.nextInt(5) + 5;
        for (int i = 0; i < n; i++) {
            res = res + chars.charAt(rnd.nextInt(leng));
        }
        return res;
    }


    private static int[] randomLevel(int size) {

        int[] str = new int[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {

            int n = random.nextInt(70) + 20;
            str[i] = (n);
        }

        for (int i = 0; i < str.length - 1; i++) { // �����n-1������
            for (int j = 0; j < str.length - i - 1; j++) { // �Ե�ǰ��������score[0......length-i-1]��������(j�ķ�Χ�ܹؼ��������Χ��������С��)
                if (str[j] > str[j + 1]) { // ��С��ֵ����������
                    int temp = str[j];
                    str[j] = str[j + 1];
                    str[j + 1] = temp;
                }
            }

        }

        return str;
    }

    /**
     * @param str ��Ӫ��IMSI�� ��46000
     */
    public static String randCellLocation(String str) {

        String cellLocation = "GsmCellLocation";
        if (str.equals("46003") || str.equals("46007"))
            cellLocation = "CdmaCellLocation";

        return cellLocation;
    }

    /**
     * �������nλ����
     *
     * @param n
     * @return
     */
    public static String randomNum(int n) {
        String res = "";
        Random rnd = new Random();
        for (int i = 0; i < n; i++) {
            res = res + rnd.nextInt(10);
        }
        return res;
    }

    /**
     * ��������ֻ���״̬
     *
     * @return
     */
    public static int randomSimState() {
        int res = 1;
        Random rnd = new Random();
        while (res == 1) {
            res = rnd.nextInt(6);
        }
        return res;
    }

    /**
     * �������UserAgent
     *
     * @param ov      ����ϵͳ�汾
     * @param model   �ֻ�����
     * @param buildId ro.build.id
     * @return
     */
    public static String randomUA(String ov, String model, String buildId) {

        String ua = null;
        String s1 = "";
        String s2 = "";
        String s3 = "";
        String s4 = "";

        try {

            Random random = new Random();
            if (random.nextBoolean())
                s1 = "U; ";
            if (random.nextBoolean())
                s2 = "zh-cn; ";

            switch (random.nextInt(3)) {
                case 1:
                    s3 = "Chrome/33.0.0.0 ";
                    break;
                case 2:
                    s3 = "Chrome/32.0.1700.107 ";
                    break;
            }

            s4 = browserOV[random.nextInt(6)];
            // Mozilla/5.0 (Linux; Android 4.4.4; HM 2A Build/KTU84Q)
            // AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0
            // Chrome/33.0.0.0 Mobile Safari/537.36
            String str = "Mozilla/5.0 (Linux; %sAndroid %s; %s%s Build/%s) AppleWebKit/%s (KHTML, like Gecko) Version/4.0 %sMobile Safari/%s";
            ua = String.format(str, s1, ov, s2, model, buildId, s4, s3, s4);

        } catch (Exception e) {
            // TODO: handle exception
        }

        return ua;
    }

    /**
     * ���imei��
     */
    public static String randomImei(String imei) {
        String str = "";
        String s14 = imei + randomNum(6);

        if (imei.toUpperCase().startsWith("A"))
            str = s14;
        else
            str = s14 + getimei15(s14);

        return str;
    }

    /**
     * ���android_id
     */
    public static String getRandomCharAndNumr(Integer length) {
        String str = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            boolean b = random.nextBoolean();
            if (b) { // �ַ���

                String chars = "abcdef0123456789";

                int leng = chars.length();
                str += chars.charAt(random.nextInt(leng));// ȡ��Сд��ĸ
            } else { // ����
                str += String.valueOf(random.nextInt(10));
            }
        }
        return str;
    }

    /**
     * ���mac��ַ
     */
    public static String randomMac() {
        String chars = "abcdef0123456789";
        String res = "";
        Random rnd = new Random();
        int leng = chars.length();
        for (int i = 0; i < 17; i++) {
            if (i % 3 == 2) {
                res = res + ":";
            } else {
                res = res + chars.charAt(rnd.nextInt(leng));
            }

        }
        return res;
    }

    /**
     * ���nλ16������
     *
     * @param n
     */
    public static String random16(int n) {
        String chars = "abcdef0123456789";
        String res = "";
        Random rnd = new Random();
        int leng = chars.length();
        for (int i = 0; i < n; i++) {

            res = res + chars.charAt(rnd.nextInt(leng));

        }
        return res;
    }

    /**
     * �������
     */
    public static String randomPhone(final String s) {

        String string;
        Random random = new Random();

        HashMap<String, String[]> map = new HashMap<String, String[]>();
        map.put("46000", new String[]{"135", "136", "137", "138", "139"});
        map.put("46002", new String[]{"150", "151", "152", "158", "159",
                "134"});
        map.put("46007", new String[]{"147", "157", "187", "188"});
        map.put("46001", new String[]{"130", "131", "132", "155", "156"});
        map.put("46003", new String[]{"133", "1349", "180", "153", "189"});
        map.put("46006", new String[]{"185", "186"});

        if (s.length() > 5) {
            String key = s.substring(0, 5);

            String[] array = map.get(key);
            if (array == null)
                Log.e("@@@@@@@@@@@@@@@@@@@@", key + "");
            string = array[random.nextInt(array.length)];
        } else {
            String[] array2 = {"3", "5", "4", "8"};
            string = String.valueOf("1")
                    + array2[random.nextInt(array2.length)];
        }
        int n = 11 - string.length();
        String string2 = string;
        for (int i = 0; i < n; ++i) {
            string2 = String.valueOf(string2) + random.nextInt(10);
        }
        return string2;
    }

    // private String randomYys() {
    // /** ǰ��Ϊ */
    // String head[] = { "0", "1", "2", "3", "7" };
    // Random rnd = new Random();
    // String res = head[rnd.nextInt(head.length)];
    //
    // return res;
    // }

    /**
     * ���ip
     */
    public static int randomIP(Context context) {
        Random random = new Random();
        WifiManager wifi_service = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifi_service.getConnectionInfo();

        int a = wifiInfo.getIpAddress();

        String b = intToIp(a);
        if (a == 0 || b.split("\\.").length != 4)
            b = "192.168.1.100";

        final String[] split = b.split("\\.");

        split[3] = 1 + random.nextInt(254) + "";
        split[2] = 1 + random.nextInt(254) + "";
        split[1] = 1 + random.nextInt(254) + "";

        int i = 0;

        while (i == 127 || i == 0) {
            i = 1 + random.nextInt(254);
        }

        split[0] = i + "";

        String ip = split[0] + "." + split[1] + "." + split[2] + "." + split[3];
        return ipToInt(ip);
    }

    /**
     * ����汾ֵ
     */
    public static String randomBBZ(String ov) {
        int bbz = 18;

        if (ov.length() == 1) {
            int i = Integer.valueOf(ov);
            if (i == 5)
                bbz = 21;
        } else if (ov.length() == 3) {

            if (ov.equals("2.0"))
                bbz = 6;
            else if (ov.equals("2.1"))
                bbz = 7;
            else if (ov.equals("2.2"))
                bbz = 8;
            else if (ov.equals("3.0"))
                bbz = 11;
            else if (ov.equals("3.1"))
                bbz = 12;
            else if (ov.equals("3.2"))
                bbz = 13;
            else if (ov.equals("4.1"))
                bbz = 16;
            else if (ov.equals("4.2"))
                bbz = 17;
            else if (ov.equals("4.3"))
                bbz = 18;
            else if (ov.equals("4.4"))
                bbz = 19;
            else if (ov.equals("5.0"))
                bbz = 21;
            else if (ov.equals("5.1"))
                bbz = 22;

        } else if (ov.length() == 5) {

            if (ov.indexOf("2.0.") >= 0)
                bbz = 6;
            else if (ov.indexOf("2.1.") >= 0)
                bbz = 7;
            else if (ov.indexOf("2.2.") >= 0)
                bbz = 8;
            else if (ov.indexOf("3.0.") >= 0)
                bbz = 11;
            else if (ov.indexOf("3.1.") >= 0)
                bbz = 12;
            else if (ov.indexOf("3.2.") >= 0)
                bbz = 13;
            else if (ov.indexOf("4.1.") >= 0)
                bbz = 16;
            else if (ov.indexOf("4.2.") >= 0)
                bbz = 17;
            else if (ov.indexOf("4.3.") >= 0)
                bbz = 18;
            else if (ov.indexOf("4.4.") >= 0)
                bbz = 19;
            else if (ov.indexOf("5.0.") >= 0)
                bbz = 21;
            else if (ov.indexOf("5.1.") >= 0)
                bbz = 22;
            else if (ov.indexOf("2.3.") >= 0) {
                String str = ov.substring(ov.length() - 1);
                if (Integer.valueOf(str) < 4)
                    bbz = 9;
                else
                    bbz = 10;
            } else if (ov.indexOf("4.0.") >= 0) {
                String str = ov.substring(ov.length() - 1);
                if (Integer.valueOf(str) < 4)
                    bbz = 14;
                else
                    bbz = 15;
            }
        }
        return bbz + "";
    }


    public static String randomLng(float lng) {
        float l = Float.valueOf(randomNum(3)) / 1000000;
        String str = (lng - l) + "";
        return str;
    }

    /**
     * ���ά��
     *
     * @return
     */
    public static String randomLat(float lat) {
        float l = Float.valueOf(randomNum(3)) / 1000000;
        String str = (lat - l) + "";
        return str;
    }


    public static boolean ishave(String temp, String str) {

        boolean b = str.contains(temp);

        return b;

    }


    public static String randomLangAndLat2(double min, double max) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");


        float f = (float) (min + Math.random() * (max - min + 0.01));

        String s = df.format(f);

        String str = s + randomNum(6);

        return str;
    }

    public static String randomLangAndLat(double min, double max) {

        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");


        float f = (float) (min + Math.random() * (max - min + 0.01));

        String s = df.format(f);

        String str = s + randomNum(6);

        return str;
    }

    /**
     * ��һ����Χ�������
     */
    public static String rand(int min, int max) {
        Random random = new Random();

        while (true) {
            int a = random.nextInt(max + 1);

            if (a >= min)
                return a + "";
        }
    }

    /**
     * ���sim������
     */
    public static String[] getSimData(String imeiHead) {
        final String[] array = new String[5];

        Random random = new Random();
        int n = 1;

        int a = random.nextInt(6);
        if (a == 0)
            n = 1;
        else if (a > 0 && a < 3)
            n = 2;
        else
            n = 3;

        if (imeiHead.toUpperCase().startsWith("A"))
            n = 3;

        final String subscriberId =  getSubscriberId(n);
        array[0] = subscriberId;
        array[1] = subscriberId.substring(0, 5);
        if (n == 1) {
            array[2] = "�й��ƶ�";
            final String[] array2 = {"�й��ƶ�", "China Mobile", "CMCC",
                    "CHINA MOBILE", "�й��ƶ� 3G", "�й��ƶ� 4G"};
            array[2] = array2[random.nextInt(array2.length)];
        } else if (n == 2) {
            array[2] = "�й���ͨ";
            final String[] array3 = {"�й���ͨ", "CHN-UNICOM"};
            array[2] = array3[random.nextInt(array3.length)];
        } else if (n == 3) {
            array[2] = "�й�����";
        }
        array[3] = getSimSerial(n);
        array[4] = new StringBuilder(String.valueOf(getNetWorkType(n)))
                .toString();
        return array;
    }

    /**
     * ���sim�����к�
     */
    public static String getSimSerial(final int n) {
        String s = "";
        if (n == 1) {
            final int nextInt = new Random(new Date().getTime()).nextInt(2);
            if (nextInt == 0) {
                s = "898600";
            } else if (nextInt == 1) {
                s = "898602";
            }
        } else if (n == 2) {
            new Random(new Date().getTime()).nextInt(2);
            s = "898601";
        } else if (n == 3) {
            new Random(new Date().getTime()).nextInt(2);
            s = "898603";
        }
        final Random random = new Random(new Date().getTime());
        String string = s;
        for (int i = 0; i < 14; ++i) {
            string = String.valueOf(string)
                    + (int) (10.0f * random.nextFloat());
        }
        return string;
    }

    /**
     * �����������
     */
    public static int getNetWorkType(final int n) {
        if (n == 1) {
            return (new int[]{2, 8, 13})[new Random(new Date().getTime())
                    .nextInt(3)];
        }
        if (n == 2) {
            final int[] array = {8, 3};
            return array[new Random(new Date().getTime()).nextInt(array.length)];
        }
        if (n == 3) {
            final int[] array2 = {4, 5, 6, 12};
            return array2[new Random(new Date().getTime())
                    .nextInt(array2.length)];
        }
        return 0;
    }

    /**
     * ���imsi
     */
    public static String getSubscriberId(final int n) {
        String s = "4600";
        if (n == 1) {
            final int nextInt = new Random(new Date().getTime()).nextInt(3);
            if (nextInt == 0) {
                s = String.valueOf(s) + "0";
            } else if (nextInt == 1) {
                s = String.valueOf(s) + "2";
            } else if (nextInt == 2) {
                s = String.valueOf(s) + "7";
            }
        } else if (n == 2) {
            final int nextInt2 = new Random(new Date().getTime()).nextInt(2);
            if (nextInt2 == 0) {
                s = String.valueOf(s) + "1";
            } else if (nextInt2 == 1) {
                s = String.valueOf(s) + "6";
            }
        } else if (n == 3) {
            final int nextInt3 = new Random(new Date().getTime()).nextInt(1);
            if (nextInt3 == 0) {
                s = String.valueOf(s) + "3";
            } else if (nextInt3 == 1) {
                s = String.valueOf(s) + "5";
            }
        }
        final Random random = new Random(new Date().getTime());
        String string = s;
        for (int i = 0; i < 10; ++i) {
            string = String.valueOf(string)
                    + (int) (10.0f * random.nextFloat());
        }
        return string;
    }

    public static float getDensity(String sw) {

        float density = 2.0f;
        if (TextUtils.isEmpty(sw))
            return density;

        int i = Integer.valueOf(sw);
        if (i >= 1080)
            density = 3.0f;
        else if (i >= 720)
            density = 2.0f;
        else if (i >= 540)
            density = 1.5f;
        else if (i >= 480)
            density = 1.5f;
        else if (i >= 320)
            density = 1.0f;

        return density;
    }

    public static int getDensityDpi(String sw) {

        int densityDpi = 320;
        if (TextUtils.isEmpty(sw))
            return densityDpi;

        int i = Integer.valueOf(sw);
        if (i >= 1080)
            densityDpi = 480;
        else if (i >= 720)
            densityDpi = 320;
        else if (i >= 540)
            densityDpi = 240;
        else if (i >= 480)
            densityDpi = 240;
        else if (i >= 320)
            densityDpi = 160;

        return densityDpi;
    }

    public static float getDpi(int densityDpi) {

        float dpi = densityDpi;
        Random random = new Random();
        float n = random.nextInt(50000) / 1000;
        if (random.nextBoolean())
            n = -n;

        dpi = dpi + n;

        return densityDpi;
    }

    public static String intToIp(int i) {
        return (i & 0xFF) + "." +

                ((i >> 8) & 0xFF) + "." +

                ((i >> 16) & 0xFF) + "." +

                (i >> 24 & 0xFF);

    }

    public static int ipToInt(final String s) {
        final String[] split = s.split("\\.");
        if (split.length != 4) {
            return 0;
        }
        return 0 + Integer.parseInt(split[0])
                + (Integer.parseInt(split[1]) << 8)
                + (Integer.parseInt(split[2]) << 16)
                + (Integer.parseInt(split[3]) << 24);
    }

    /**
     * ����ȡ��������ת ����JSONArray
     **/
    public static JSONArray dataToJSONArray(String data) {
        JSONArray array = null;

        try {
            array = new JSONArray(data);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return array;
    }

    /**
     * ��ȡimei����
     */
    public static String getImeiToFile(Context context, String s) {

        String s1 = "";
        try {

            InputStreamReader inputReader = new InputStreamReader(context
                    .getResources().getAssets().open(s));
            BufferedReader bufReader = new BufferedReader(inputReader);

            String str1 = "";
            while ((str1 = bufReader.readLine()) != null) {

                s1 = s1 + str1;
            }

            bufReader.close();
            inputReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s1.trim();

    }

    /**
     * ����IMEI��ǰ14λ���õ���15λ��У��λ IMEIУ�����㷨�� (1).��ż��λ���ֱַ����2���ֱ�����λ����ʮλ��֮��
     * (2).������λ������ӣ��ټ�����һ����õ�ֵ (3).����ó�������λ��0��У��λΪ0������Ϊ10��ȥ��λ�� �磺35 89 01 80 69
     * 72 41 ż��λ����2�õ�5*2=10 9*2=18 1*2=02 0*2=00 9*2=18 2*2=04
     * 1*2=02,��������λ����֮�ͺ�ż��λ��λʮλ֮�ͣ� �õ�
     * 3+(1+0)+8+(1+8)+0+(0+2)+8+(0+0)+6+(1+8)+7+(0+4)+4+(0+2)=63 У��λ 10-3 = 7
     *
     * @param imei
     * @return
     */
    public static String getimei15(String imei) {
        if (imei.length() == 14) {
            char[] imeiChar = imei.toCharArray();
            int resultInt = 0;
            for (int i = 0; i < imeiChar.length; i++) {
                int a = Integer.parseInt(String.valueOf(imeiChar[i]));
                i++;
                final int temp = Integer.parseInt(String.valueOf(imeiChar[i])) * 2;
                final int b = temp < 10 ? temp : temp - 9;
                resultInt += a + b;
            }
            resultInt %= 10;
            resultInt = resultInt == 0 ? 0 : 10 - resultInt;
            return resultInt + "";
        } else {
            return "";
        }
    }

}
