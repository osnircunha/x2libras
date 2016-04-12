package com.ocunha.librasapp.utils;

/**
 * Created by osnircunha on 4/10/16.
 */
public class Constants {
    private static final String BASE_URL = "http://libras.mybluemix.net/api";
    public static final String IMAGE_RECOGNITION_URL = BASE_URL + "/image_recognition";
    public static final String IMAGE_RECOGNITION_CLASSIFY_URL = IMAGE_RECOGNITION_URL + "/classify";

    public static final String LIBRAS_URL = BASE_URL + "/libras";
    public static final String LIBRAS_WORD_URL = LIBRAS_URL + "/word";
    public static final String LIBRAS_WORDS_URL = LIBRAS_URL + "/words";
    public static final String LIBRAS_VIDEO_URL = LIBRAS_URL + "/video/%s";

    public static final String TRANSLATION_URL = BASE_URL + "/translation";
    public static final String TRANSLATION_TRANSLATE_URL = TRANSLATION_URL + "/translate";

}
