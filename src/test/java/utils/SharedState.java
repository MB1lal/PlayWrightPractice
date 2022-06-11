package utils;


import com.microsoft.playwright.APIResponse;

import java.util.List;

public class SharedState {
    public static APIResponse PET_RESPONSE;
    public static long PET_ID;
    public static String PET_STATUS;
    public static int PET_STORE_ID;
    public static APIResponse PET_STORE_RESPONSE;
    public static int PET_ORDER_ID;

    public static class UserData {
        public static String USERNAME;
        public static String PASSWORD;
        public static String EMAIL;
        public static String PHONE;
        public static String FIRST_NAME;
        public static String LAST_NAME;
        public static Integer STATUS;
        public static Integer USER_ID;
    }

    public static APIResponse USER_RESPONSE;

    public static List<List<String>> EXCEL_DATA;
}
