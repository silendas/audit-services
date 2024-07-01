package com.cms.audit.api.Common.constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.cms.audit.api.Common.dto.GapDTO;
import com.cms.audit.api.Dashboard.dto.DateCompareDTO;

public class convertDateToRoman {

    public static Date setTimeToZero(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String dateString = sdf.format(date);
            return sdf.parse(dateString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Date dateAddTime(Date dateToConvert) {
        // Mengatur zona waktu ke UTC
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        // Kembalikan tanggal dengan waktu T00:00:00
        return dateToConvert;
    }

    public static GapDTO calculateDateDifference(Date date1, Date date2) {
        // Konversi Date ke LocalDateTime
        LocalDateTime localDateTime1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime localDateTime2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Hitung selisih antara dua LocalDateTime
        Duration duration = Duration.between(localDateTime1, localDateTime2);

        // Mendapatkan jumlah hari, jam, menit, dan detik dari selisih
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        if(days < 0 || hours < 0 || minutes < 0 || seconds < 0) {
            Duration duration2 = Duration.between(localDateTime2, localDateTime1);
            long days2 = duration2.toDays();
            long hours2 = duration2.toHours() % 24;
            long minutes2 = duration2.toMinutes() % 60;
            long seconds2 = duration2.getSeconds() % 60;
            days = days2;
            hours = hours2;
            minutes = minutes2;
            seconds = seconds2;
        }

        // Kembalikan GapDTO dengan format yang diinginkan
        return GapDTO.builder()
                .day(days)
                .hour(hours)
                .minute(minutes)
                .second(seconds)
                .build();
    }

    public static Date setTimeToLastSecond(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            return calendar.getTime();
        }
        return null;
    }

    public static String formatToISO8601(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return sdf.format(date);
    }

    public static String getRomanMonth() {
        Date date = new Date();
        LocalDate localdate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localdate.getMonthValue();
        String roman = "";
        if (month == 1) {
            roman = "I";
        } else if (month == 2) {
            roman = "II";
        } else if (month == 3) {
            roman = "III";
        } else if (month == 4) {
            roman = "IV";
        } else if (month == 5) {
            roman = "V";
        } else if (month == 6) {
            roman = "VI";
        } else if (month == 7) {
            roman = "VII";
        } else if (month == 8) {
            roman = "VIII";
        } else if (month == 9) {
            roman = "IX";
        } else if (month == 10) {
            roman = "X";
        } else if (month == 11) {
            roman = "XII";
        } else if (month == 12) {
            roman = "XII";
        }
        return roman;
    }


    public static Integer convertIntYear(Date date) {
        LocalDate localdate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localdate.getYear();
        return year;
    }

    public static Integer getIntYear() {
        Date date = new Date();
        LocalDate localdate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localdate.getYear();
        return year;
    }

    public static Long getLongYearNumber(Date date) {
        // Buat instance Calendar dan atur waktunya dengan objek Date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
    
        // Dapatkan tahun dari Calendar
        int year = calendar.get(Calendar.YEAR);
    
        // Kembalikan tahun sebagai Long
        return Long.valueOf(year);
    }
    

    public static String convertDateToString(Date dt) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dateToString = df.format(dt);
        return dateToString;
    }

    public static DateCompareDTO getDateRange(Long month) {
        // Pastikan parameter month valid
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        // Dapatkan tahun saat ini
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        // Set tanggal mulai bulan
        calendar.set(year, month.intValue() - 1, 1);
        Date startDate = calendar.getTime();

        // Set tanggal akhir bulan
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();

        // Kembalikan DateRangeDTO dengan startDate dan endDate
        return new DateCompareDTO(setTimeToZero(startDate), setTimeToLastSecond(endDate));
    }

    public static Long getLongMonthNumber(Date date) {
        // Buat instance Calendar dan atur waktunya dengan objek Date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Dapatkan bulan dari Calendar dan tambahkan 1 karena bulan dalam Java dimulai dari 0
        int month = calendar.get(Calendar.MONTH) + 1;

        // Kembalikan nomor bulan sebagai Long
        return Long.valueOf(month);
    }
    public static String getMonthName(Long monthNumber) {
        // Validasi input
        if (monthNumber < 1 || monthNumber > 12) {
            throw new IllegalArgumentException("Bulan harus antara 1 dan 12");
        }
    
        // Array nama bulan dalam bahasa Indonesia
        String[] monthNames = {
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        };
    
        // Kembalikan nama bulan sebagai String
        return monthNames[monthNumber.intValue() - 1];
    }


    public static String convertDateToIndonesia(Date dt) {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"));
        String dateToString = df.format(dt);
        return dateToString;
    }

    public static String convertDateHehe(Date dt) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String dateToString = df.format(dt);
        return dateToString;
    }

}
