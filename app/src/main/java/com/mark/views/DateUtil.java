package com.mark.views;

import android.annotation.SuppressLint;
import android.text.TextUtils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @描述 日期格式化工具。
 */

public class DateUtil {
    public static final SimpleDateFormat sequenceFormat = new SimpleDateFormat(
            "yyyyMMddHHmmss");

    public static final SimpleDateFormat sequenceFormat2 = new SimpleDateFormat(
            "HH:mm:ss");
    public static final SimpleDateFormat sequenceFormat3 = new SimpleDateFormat(
            "HH:mm");

    public static String getDateFormat(long time, DateFormat df) {
        Date d = new Date(time);
        return df.format(d);
    }

    // 格式化日期（20170101->2017？01？01）
    public static String formatDate(String date, String mark) {
        if (date != null) {
            if (8 == date.length()) {
                String yy = date.substring(0, 4);
                String mm = date.substring(4, 6);
                String dd = date.substring(6, 8);
                return yy + mark + mm + mark + dd;
            }
            if (6 == date.length()) {
                String yy = date.substring(0, 4);
                String mm = date.substring(4, 6);

                return yy + mark + mm;
            }
        }

        return date;
    }

    // 格式化日期（20170101->2017-01-01）
    public static String formatDate(String date) {
        if (date == null) {
            return null;
        }
        if (date.length() != 8)
            return date;
        String yy = date.substring(0, 4);
        String mm = date.substring(4, 6);
        String dd = date.substring(6, 8);
        return yy + "-" + mm + "-" + dd;
    }

    // 格式化日期（0101->01/01）
    public static String formatMonth(String date) {
        if (date == null) {
            return null;
        }
        if (date.length() != 4)
            return date;
        String mm = date.substring(0, 2);
        String dd = date.substring(2, 4);
        return mm + "-" + dd;
    }

    // 格式化日期（20170331110915->2017-01-01 11:11:11）
    public static String formatDateTime(String date) {
        if (date == null) {
            return null;
        }
        if (date.length() != 14)
            return date;
        String yy = date.substring(0, 4);
        String mm = date.substring(4, 6);
        String dd = date.substring(6, 8);
        String hh = date.substring(8, 10);
        String mmi = date.substring(10, 12);
        String ss = date.substring(12, 14);
        return yy + "-" + mm + "-" + dd + " " + hh + ":" + mmi + ":" + ss;
    }


    public static String DateToWeek(Date date) {
        String[] WEEK = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        final int WEEKDAYS = 7;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > WEEKDAYS) {
            return null;
        }

        return WEEK[dayIndex - 1];
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */

    public static Date parse(String strDate, String pattern) {

        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return returnValue;
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间戳
     * @return
     */
    public static String getStandardDate(String timeStr) {

        StringBuffer sb = new StringBuffer();

        long t = Long.parseLong(timeStr);
        long time = System.currentTimeMillis() - (t);
        long mill = (long) Math.ceil(time / 1000.00);//秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    public static Date getDateWithYear(int year) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getDateWithYearandDay(int year, int day) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * 日期前字符串格式化转化
     */
    public static String getStringToDate(String timeStr) {

        Date date = null;
        String now = "";
        if (!TextUtils.isEmpty(timeStr)) {
            if (timeStr.equals(""))
                return now;
            try {
                if (!timeStr.contains("-")) {
                    date = new SimpleDateFormat("yyyyMMdd").parse(timeStr);
                } else {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(timeStr);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            now = new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
        return now;
    }

    //以年月日格式
    public static String formatDateTimeNew(String date) {
        if (date == null) {
            return null;
        }
        if (date.length() != 14)
            return date;
        String yy = date.substring(0, 4);
        String mm = date.substring(4, 6);
        String dd = date.substring(6, 8);
        String hh = date.substring(8, 10);
        String mmi = date.substring(10, 12);
        String ss = date.substring(12, 14);
        //        return yy + "-" + mm + "-" + dd + " " + hh + ":" + mmi + ":" + ss;
        return yy + "年" + mm + "月" + dd + "日";
    }


    /**
     * 获取两个时间差的天数
     *
     * @param starTime
     * @param endTime
     * @return
     */
    public static int dayDifference(Long starTime, Long endTime) {
        return (int) ((endTime - starTime) / (1000 * 60 * 60 * 24));
    }


    /**
     * 将时间戳转换为时间
     *
     * @param s 时间戳
     * @return 时间字符串
     */
    public static String stampToDate(String s) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            long lt = Long.parseLong(s);
            Date date = new Date(lt);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @SuppressLint("SimpleDateFormat")
    public static String simpleDateFormat(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        //获取当前时间
        Date curDate = new Date(System.currentTimeMillis());
        return format.format(curDate);
    }


    /**
     * 格式化为HH：mm
     *
     * @param time
     * @return
     */
    public static String formatHHMM(long time) {
        Date now = new Date(time);
        return new SimpleDateFormat("HH:mm").format(now);

    }

    /**
     * 格式化为HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String formatHHMMss(long time) {
        Date now = new Date(time);
        return new SimpleDateFormat("HH:mm:ss").format(now);

    }

    /**
     * 格式化为yyyy MM.dd
     *
     * @param time
     * @return
     */
    public static String formatYYHHMM(long time) {
        Date now = new Date(time);
        return new SimpleDateFormat("yyyy MM.dd").format(now);

    }

    /**
     * 格式化为MM月dd日
     *
     * @param time
     * @return
     */
    public static String formatMMDD(long time) {
        Date now = new Date(time);
        return new SimpleDateFormat("MM月dd日").format(now);

    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };


    /**
     * 判断当天时间具体的时间
     *
     * @param time
     * @return
     */
    public static String asserTime(long time) {
        long currTime = System.currentTimeMillis();
        Date timeDate = new Date(time);
        long diffTime = currTime - time;
        long diff = diffTime / (24 * 60 * 60 * 1000);

        if (diff < 1) {
            //今天
            return formatHHMM(time);
        } else if (diff >= 1 && diff <= 7) {
            if (diff == 1) {
                return "昨天";
            } else {
                return new SimpleDateFormat("EEEE").format(timeDate);
            }

        } else {
            return new SimpleDateFormat("yyyy年M月dd日").format(timeDate);
        }
    }


    /**
     * 判断当天会话时间
     *
     * @param timesamp
     * @return
     */
    public static String asserConversationTime(long timesamp) {
        String result = "";
        Calendar todayCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(timesamp);

        String timeFormat = "M月d日 HH:mm";
        String yearTimeFormat = "yyyy年M月d日 HH:mm";
        timeFormat = "M月d日 ";


        /**
         * 其他年份的时间显示
         */
        String otheryearTimeFormat = "yyyy年M月d日";

        boolean yearTemp = todayCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);
        if (yearTemp) {
            int todayMonth = todayCalendar.get(Calendar.MONTH);
            int otherMonth = otherCalendar.get(Calendar.MONTH);
            if (todayMonth == otherMonth) {//表示是同一个月
                final int temp = todayCalendar.get(Calendar.DATE) - otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        result = getHourAndMin(timesamp);
                        break;
                    case 1:
                        result = "昨天 ";
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int dayOfMonth = otherCalendar.get(Calendar.WEEK_OF_MONTH);
                        int todayOfMonth = todayCalendar.get(Calendar.WEEK_OF_MONTH);
                        if (dayOfMonth == todayOfMonth) {//表示是同一周
                            int dayOfWeek = otherCalendar.get(Calendar.DAY_OF_WEEK);
                            if (dayOfWeek != 1) {//判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
                                result = dayNames[otherCalendar.get(Calendar.DAY_OF_WEEK) - 1];
                            } else {
                                result = getTime(timesamp, timeFormat);
                            }
                        } else {
                            result = getTime(timesamp, timeFormat);
                        }
                        break;
                    default:
                        result = getTime(timesamp, timeFormat);
                        break;
                }
            } else {
                result = getTime(timesamp, timeFormat);
            }
        } else {
            result = getYearTime(timesamp, otheryearTimeFormat);
        }
        return result;
        // return getNewChatTime(timesamp);
       /* long currTime = System.currentTimeMillis();
        Date now = new Date(currTime);
        Date timeDate = new Date(time);
        String nowStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
        String datStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeDate);
        long diffTime = currTime - time;
        long diff = diffTime / (24 * 60 * 60 * 1000);
        long hours = diffTime / (60 * 60 * 1000);

        int nowDay = Integer.parseInt(nowStr.substring(8, 10));
        int timeDay = Integer.parseInt(datStr.substring(8, 10));
        if (diff > 1) {
            return getWeekOfDate(new Date(currTime), diff, timeDate);
        } else if (hours > 0) {
            if (nowDay != timeDay) {
                return "昨天";
            } else {
                return formatHHMM(time);
            }
        } else {
            return formatHHMM(time);
        }*/

     /*   if (diff > 0) {
            //今天
            return formatHHMM(time);
        } else if (diff == 1) {
            return "昨天";


        } else {
            return getWeekOfDate(new Date(currTime), diff, timeDate);
        }*/
    }

    /**
     * 判断聊天时间
     *
     * @param time
     * @return
     */
    public static String asserChatTime(long time) {
        long currTime = System.currentTimeMillis();
        Date now = new Date(currTime);
        Date timeDate = new Date(time);
        String nowStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
        String datStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeDate);
        long diffTime = currTime - time;
        long diff = diffTime / (24 * 60 * 60 * 1000);
        long hours = diffTime / (60 * 60 * 1000);


        int nowDay = Integer.parseInt(nowStr.substring(8, 10));
        int timeDay = Integer.parseInt(datStr.substring(8, 10));
        if (diff > 1) {
            return getChatWeekOfDate(new Date(currTime), diff, timeDate);
        } else if (hours > 0) {
            if (nowDay != timeDay) {
                return "昨天" + formatHHMM(time);
            } else {
                return formatHHMM(time);
            }
        } else {
            return formatHHMM(time);
        }

     /*   if (diff > 0) {
            //今天
            return formatHHMM(time);
        } else if (diff == 1) {
            return "昨天";


        } else {
            return getWeekOfDate(new Date(currTime), diff, timeDate);
        }*/
    }


    /**
     * nowtime-oldtime 是否大于5分钟；大于：返回true；否：false
     *
     * @param nowTime 时间戳
     * @param oldTime 时间戳
     * @return
     */
    public static boolean differTime(String nowTime, String oldTime) {
        if (TextUtils.isEmpty(nowTime) || TextUtils.equals("null", nowTime)) {
            nowTime = "0";
        }

        if (TextUtils.isEmpty(oldTime) || TextUtils.equals("null", oldTime)) {
            oldTime = "0";
        }
        long now = Long.parseLong(nowTime);
        long old = Long.parseLong(oldTime);
        long diff = (now - old) / (60 * 1000);
        return diff >= 5 ? true : false;
    }

    public static boolean timeUpdate(String nowTime, String oldTime) {
        if (TextUtils.isEmpty(nowTime) || TextUtils.equals("null", nowTime)) {
            nowTime = "0";
        }

        if (TextUtils.isEmpty(oldTime)) {
            oldTime = "0";
        }
        long now = Long.parseLong(nowTime);
        long old = Long.parseLong(oldTime);
        long diff = (now - old);
        return diff > 0 ? true : false;
    }

    /**
     * 2019-09-17T05:59:48.937Z
     * 2018-07-19 11:30:14
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s) {
        return dateToStamp(s, "yyyy-MM-dd HH:mm:ss");
    }

    public static long dateToStamp(String s, String pattern) {
        if (TextUtils.isEmpty(s)) {
            s = System.currentTimeMillis() + "";
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date;
        try {
            date = simpleDateFormat.parse(s);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }


    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt 当前是周几
     *           显示时间的和当前时间相差的天数
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt, long diff, Date timeDate) {
        String[] weekDays = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 7;
        return w > diff ? weekDays[(int) (w - diff - 1)] : new SimpleDateFormat("yyyy年M月dd日").format(timeDate);
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt 当前是周几
     *           显示时间的和当前时间相差的天数
     * @return 当前日期是星期几
     */
    public static String getChatWeekOfDate(Date dt, long diff, Date timeDate) {
        String[] weekDays = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 7;
        return w > diff ? weekDays[(int) (w - diff - 1)] + " " + new SimpleDateFormat("HH:mm").format(timeDate) : new SimpleDateFormat("yyyy年M月dd日 HH:mm").format(timeDate);
    }


    /**
     * 将时间戳转换为时间
     *
     * @param s 时间戳
     * @return 时间字符串
     */
    public static String formatBroadcast(String s) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            long lt = Long.parseLong(s);
            Date date = new Date(lt);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将时间戳转换为时间
     *
     * @param s 时间戳
     * @return 时间字符串
     */
    public static String formatBroadcastDetail(String s) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long lt = Long.parseLong(s);
            Date date = new Date(lt);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



    public static String toGMTString(long milliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM y HH:mm:ss 'GMT'", Locale.US);
        TimeZone gmtZone = TimeZone.getTimeZone("GMT");
        sdf.setTimeZone(gmtZone);
        GregorianCalendar gc = new GregorianCalendar(gmtZone);
        gc.setTimeInMillis(milliseconds);
        return sdf.format(new Date(milliseconds));
    }

    /**
     * 时间戳格式转换
     */
    static String dayNames[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public static String getNewChatTime(long timesamp) {
        String result = "";
        Calendar todayCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(timesamp);

        String timeFormat = "M月d日 HH:mm";
        String yearTimeFormat = "yyyy年M月d日 HH:mm";
        String am_pm = "";
        int hour = otherCalendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 6) {
            am_pm = "凌晨";
        } else if (hour >= 6 && hour < 12) {
            am_pm = "早上";
        } else if (hour == 12) {
            am_pm = "中午";
        } else if (hour > 12 && hour < 18) {
            am_pm = "下午";
        } else if (hour >= 18) {
            am_pm = "晚上";
        }
        timeFormat = "M月d日 " + am_pm + "HH:mm";
        yearTimeFormat = "yyyy年M月d日 " + am_pm + "HH:mm";

        boolean yearTemp = todayCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);
        if (yearTemp) {
            int todayMonth = todayCalendar.get(Calendar.MONTH);
            int otherMonth = otherCalendar.get(Calendar.MONTH);
            if (todayMonth == otherMonth) {//表示是同一个月
                final int temp = todayCalendar.get(Calendar.DATE) - otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        result = getHourAndMin(timesamp);
                        break;
                    case 1:
                        result = "昨天 " + getHourAndMin(timesamp);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int dayOfMonth = otherCalendar.get(Calendar.WEEK_OF_MONTH);
                        int todayOfMonth = todayCalendar.get(Calendar.WEEK_OF_MONTH);
                        if (dayOfMonth == todayOfMonth) {//表示是同一周
                            int dayOfWeek = otherCalendar.get(Calendar.DAY_OF_WEEK);
                            if (dayOfWeek != 1) {//判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
                                result = dayNames[otherCalendar.get(Calendar.DAY_OF_WEEK) - 1] + getHourAndMin(timesamp);
                            } else {
                                result = getTime(timesamp, timeFormat);
                            }
                        } else {
                            result = getTime(timesamp, timeFormat);
                        }
                        break;
                    default:
                        result = getTime(timesamp, timeFormat);
                        break;
                }
            } else {
                result = getTime(timesamp, timeFormat);
            }
        } else {
            result = getYearTime(timesamp, yearTimeFormat);
        }
        return result;
    }

    /**
     * 当天的显示时间格式
     *
     * @param time
     * @return
     */
    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    /**
     * 不同一周的显示时间格式
     *
     * @param time
     * @param timeFormat
     * @return
     */
    public static String getTime(long time, String timeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(new Date(time));
    }

    /**
     * 不同年的显示时间格式
     *
     * @param time
     * @param yearTimeFormat
     * @return
     */
    public static String getYearTime(long time, String yearTimeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(yearTimeFormat);
        return format.format(new Date(time));
    }


    /**
     * 根据时间戳 转化为 需要的时间格式
     *
     * @param s       时间戳
     * @param pattern 需要的格式
     * @return 时间字符串
     */
    public static String getTimeByFormat(String s, String pattern) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            long lt = Long.parseLong(s);
            Date date = new Date(lt);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 将时间戳转换为时间
     *
     * @param s 时间戳
     * @return 时间字符串
     */
    public static String formatTaskDetail(String s) {

        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
            long ts = date.getTime();
            res = String.valueOf(ts);
            return res;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取当天的日期 2020/02/21
     * 将时间戳转换为时间
     *
     * @return 时间字符串
     */
    public static String formatTaskDate() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            String s = System.currentTimeMillis() + "";
            Date date = new Date(Long.parseLong(s));
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static String stringToDate(String checktime, String formatType) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatType);
            Date currentTime_2 = formatter.parse(checktime);
            return currentTime_2.getTime() + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取现在时间
     *
     * @return返回短时间格式 yyyy/MM/dd
     */
    public static Date getDateShort(String checktime) {
        Date currentTime = new Date(checktime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * method 将字符串类型的日期转换为一个Date（java.sql.Date）
     * dateString 需要转换为Date的字符串
     * dataTime Date
     */
    public final static String string2Date(String dateString, String patten) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(patten);
            return formatter.parse(dateString).getTime() + "";
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    public static String queryTaskDate(String checktime, String formatType) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatType);
            return formatter.format(checktime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static int getMonthOfMaxDay(String dyear, String dmouth) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM");

        Calendar rightNow = Calendar.getInstance();
        try {
            rightNow.setTime(simpleDate.parse(dyear + "/" + dmouth));
        } catch (ParseException e) {
            e.printStackTrace();

        }
        int days = rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);//根据年月 获取月份天数
        return days;
    }

    /**
     * 格式化为MMDD HH：mm
     *
     * @param time
     * @return
     */
    public static String formatMMDDHHMM(long time) {
        Date now = new Date(time);
        return new SimpleDateFormat("M月dd日 HH:mm").format(now);

    }
    /**
     * 格式化为HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String formatHHMMSS(long time) {
      return   sequenceFormat2.format(time);
    }

    /**
     * 判断两个时间是否是通同一天
     *
     * @param time1
     * @param time2
     * @return true:同一天；false:不是同一天
     */
    public static boolean isSameDay(String time1, String time2) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");
        Date date1 = new Date(Long.parseLong(time1));
        Date date2 = new Date(Long.parseLong(time2));
        String format1 = simpleDate.format(date1);
        String format2 = simpleDate.format(date2);
        return TextUtils.equals(format1, format2);
    }

    /**
     * 返回hh:mm
     *
     * @return int[0] 时；int[1] 分
     */
    public static int[] formatHHmm() {
        long time = System.currentTimeMillis();
        int[] timeArr = new int[2];
        Date now = new Date(time);
        timeArr[0] = Integer.parseInt(new SimpleDateFormat("HH").format(now));
        timeArr[1] = Integer.parseInt(new SimpleDateFormat("mm").format(now));
        return timeArr;

    }


    /**
     * 传入的时间和当前系统时间比较
     *
     * @param compareDate
     * @return boolean true:传入的时间大于等于当前时间；false：传入的时间小于当前时间
     */
    public static boolean compareDate(int[] compareDate) {
        long time = System.currentTimeMillis();
        Date now = new Date(time);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date compare = df.parse(compareDate[0] + "-" + compareDate[1] + "-" + compareDate[2]);
            String nowFormat = df.format(now);
            if (df.parse(nowFormat).getTime() <= compare.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 传入的时间和4：00比较；
     *
     * @param compareDate
     * @return boolean false：大于等于4:00  当天任务；true：小于04：00;第二天任务
     */
    public static boolean compareTimeDate(String compareDate) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        try {
            Date compare = df.parse(compareDate);
            Date date1 = df.parse("04:00");
            if (date1.getTime() <= compare.getTime()) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 传入的时间和4：00比较；
     *
     * @param startTime
     * @param endTime
     * @return boolean true：开始时间小于结束时间；false:开始时间大于结束时间
     */
    public static boolean compareTwoTimeDate(String startTime, String endTime) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        try {
            Date startDate = df.parse(startTime);
            Date endDate = df.parse(endTime);
            if (startDate.getTime() < endDate.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getTaskTime(String checktime, String formatType) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatType);
            Date currentTime_2 = formatter.parse(checktime);
            return (currentTime_2.getTime() + 24 * 60 * 60 * 1000) + "";//加上一天时间戳，第二天凌晨的任务
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /*如果0<=开始时间<4，那开始时间 < 结束时间  < 4；
 如果开始时间>=4，那 开始时间<结束时间<=23:59，或者0<=结束时间<4*/
    public static String[] getStartEndTime(String dateTime, String start, String end) {
        if (TextUtils.isEmpty(dateTime)) {
            dateTime = new SimpleDateFormat("yyyy/MM/dd").format(new Date(System.currentTimeMillis()));
        }
        String[] time = new String[3];

        if (((DateUtil.compareTimeDate(start) && DateUtil.compareTimeDate(end)) || (!DateUtil.compareTimeDate(start) && !DateUtil.compareTimeDate(end)))
                && !DateUtil.compareTwoTimeDate(start, end)) {
            //0<开始时间>结束时间<4 或者 4<=开始时间>结束时间<23:59 (3:00-2:00；12:00-10:00)
            //  ToastUtil.showMessage("开始时间不能晚于结束时间");
            time[2] = "开始时间不能晚于结束时间";
        } else if (DateUtil.compareTimeDate(start) && !DateUtil.compareTimeDate(end) && DateUtil.compareTwoTimeDate(start, end)) {
            //0<开始时间<4  结束时间>4  2:00-6:00
            //  ToastUtil.showMessage("开始时间不能早于当日4点");
            time[2] = "开始时间不能早于当日4点";
        }
        if (DateUtil.compareTimeDate(start) && DateUtil.compareTimeDate(end) && DateUtil.compareTwoTimeDate(start, end)) {
            //如果0<=开始时间<4，那开始时间 < 结束时间  < 4
            /**
             * 第二天凌晨任务
             */
            time[0] = DateUtil.getTaskTime(dateTime + " "
                    + start, "yyyy/MM/dd HH:mm");
            time[1] = DateUtil.getTaskTime(dateTime + " "
                    + end, "yyyy/MM/dd HH:mm");
        } else if (!DateUtil.compareTimeDate(start) && DateUtil.compareTimeDate(end) && !DateUtil.compareTwoTimeDate(start, end)) {
            //开始时间当日；结束时间小于4:00；跨日任务
            time[0] = DateUtil.stringToDate(dateTime + " "
                    + start, "yyyy/MM/dd HH:mm");
            time[1] = DateUtil.getTaskTime(dateTime + " "
                    + end, "yyyy/MM/dd HH:mm");

        } else if (!DateUtil.compareTimeDate(start) && !DateUtil.compareTimeDate(end) && DateUtil.compareTwoTimeDate(start, end)) {
            //当天任务
            time[0] = DateUtil.stringToDate(dateTime + " "
                    + start, "yyyy/MM/dd HH:mm");
            time[1] = DateUtil.stringToDate(dateTime + " "
                    + end, "yyyy/MM/dd HH:mm");
        }

        return time;
    }

    /**
     * 格式化为2019.10.1
     *
     * @param time
     * @return
     */
    public static String formatTaskOfDate(String time) {
        Long time1 = Long.parseLong(time);
        Date now = new Date(time1);
        return new SimpleDateFormat("yyyy.M.d").format(now);

    }


    public static String formatHHmm(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        try {
            Date compare = df.parse(time);
            return df.format(compare);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 判断是否在同一天
     *
     * @param t1
     * @param t2
     * @return
     */
    public static boolean isSameDay(long t1, long t2) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String s1 = dateFormat.format(t1);
        String s2 = dateFormat.format(t2);
        return TextUtils.equals(s1, s2);
    }


    public static String formatSubmitTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date now = new Date(Long.parseLong(time));
            return df.format(now);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
