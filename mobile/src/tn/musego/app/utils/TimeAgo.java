//package tn.musego.app.utils;
//
//import com.codename1.l10n.ParseException;
//import com.codename1.l10n.SimpleDateFormat;
//import com.codename1.ui.Calendar;
//
//import java.util.Date;
//
///**
// * @author Skander Ben Fredj
// * @created 5/8/2023
// * @project pi-3a-mobile
// */
//
//public class TimeAgo {
//    String getTimeAgo(String fromDateStr, String format, long timeAdjustment){
//        Date currentDate = new Date();
//
//        SimpleDateFormat df = new SimpleDateFormat(format);
//        Date fromDate = currentDate;
//        try {
//            fromDate = df.parse(fromDateStr);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        long diffl = currentDate.getTime() - (fromDate.getTime() + timeAdjustment);
//
//        Date diff = new Date(diffl);
//        Calendar c = new Calendar();
//        c.setTime(diff);
//
//
//        if(c.get(Calendar.YEAR) >= 1972){
//            return c.get(Calendar.YEAR)+" years ago";
//        }
//
//        if(c.get(Calendar.YEAR) == 1971){
//            return c.get(Calendar.YEAR)+" year ago";
//        }
//
//        if(c.get(Calendar.MONTH) >= 2){
//            return c.get(Calendar.MONTH)+" months ago";
//        }
//
//        if(c.get(Calendar.MONTH) == 1){
//            return c.get(Calendar.MONTH)+" month ago";
//        }
//
//        if((c.get(Calendar.DAY_OF_MONTH) - 1) / 7 >= 2){
//            return (c.get(Calendar.DAY_OF_MONTH) - 1) / 7 +" weeks ago";
//        }
//
//        if((c.get(Calendar.DAY_OF_MONTH) - 1) / 7 == 1){
//            return (c.get(Calendar.DAY_OF_MONTH) - 1) / 7+" week ago";
//        }
//
//        if(c.get(Calendar.DAY_OF_MONTH) - 1 >= 2){
//            return c.get(Calendar.DAY_OF_MONTH) - 1+" days ago";
//        }
//
//        if(c.get(Calendar.DAY_OF_MONTH) - 1 == 1){
//            return c.get(Calendar.DAY_OF_MONTH) - 1+" day ago";
//        }
//
//
//
//        if(c.get(Calendar.HOUR_OF_DAY) >= 2){
//            return c.get(Calendar.HOUR_OF_DAY)+" hours ago";
//        }
//
//        if(c.get(Calendar.HOUR_OF_DAY) == 1){
//            return c.get(Calendar.HOUR_OF_DAY)+" hour ago";
//        }
//
//        if(c.get(Calendar.MINUTE) >= 2){
//            return c.get(Calendar.MINUTE)+" minutes ago";
//        }
//
//        if(c.get(Calendar.MINUTE) == 1){
//            return c.get(Calendar.MINUTE)+" minute ago";
//        }
//
//        if(c.get(Calendar.SECOND) >= 2){
//            return c.get(Calendar.SECOND)+" seconds ago";
//        }
//
//        if(c.get(Calendar.SECOND) == 1){
//            return c.get(Calendar.SECOND)+" second ago";
//        }
//
//        return "0 seconds ago";
//    }
//}
