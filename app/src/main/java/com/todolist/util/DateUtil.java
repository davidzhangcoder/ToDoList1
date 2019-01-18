package com.todolist.util;

import java.util.Calendar;

public class DateUtil
{

    public static boolean before(Calendar cal1, Calendar cal2 )
    {
        boolean bBefore = false;

        if( cal1.get( Calendar.YEAR ) < cal2.get( Calendar.YEAR ) )
        {
            bBefore = true;
        } else if( cal1.get( Calendar.YEAR ) == cal2.get( Calendar.YEAR )
                &&
                cal1.get( Calendar.MONTH ) < cal2.get( Calendar.MONTH ) ) {
            bBefore = true;
        } else if( cal1.get( Calendar.YEAR ) == cal2.get( Calendar.YEAR )
                &&
                cal1.get( Calendar.MONTH ) == cal2.get( Calendar.MONTH )
                &&
                cal1.get( Calendar.DAY_OF_MONTH ) < cal2.get( Calendar.DAY_OF_MONTH ) ) {
            bBefore = true;
        }
        return bBefore;
    }

    public static boolean sameDay( Calendar cal1, Calendar cal2 ) {

        if(cal1 == null || cal2 == null){
            return false;
        }
        return( cal1.get( Calendar.YEAR ) == cal2.get( Calendar.YEAR )
                &&
                cal1.get( Calendar.MONTH ) == cal2.get( Calendar.MONTH )
                &&
                cal1.get( Calendar.DAY_OF_MONTH ) == cal2.get( Calendar.DAY_OF_MONTH ) );
    }

    public static boolean sameDayTime(Calendar cal1, Calendar cal2){
        if(sameDay(cal1,cal2)){
            return ( cal1.get( Calendar.HOUR_OF_DAY ) == cal2.get( Calendar.HOUR_OF_DAY )
                    &&
                    cal1.get( Calendar.MINUTE ) == cal2.get( Calendar.MINUTE )
                    &&
                    cal1.get( Calendar.SECOND ) == cal2.get( Calendar.SECOND ) );
        }
        return false;
    }

    public static boolean after( Calendar cal1, Calendar cal2 ) {
        return before( cal2, cal1 );
    }

    public static boolean after(Calendar cal1, Calendar cal2, boolean isOrSameDay ) {
        boolean bAfter = after( cal1, cal2 );

        if ( !bAfter && isOrSameDay ) {
            bAfter = sameDay( cal1, cal2 );
        }

        return bAfter;
    }

}
