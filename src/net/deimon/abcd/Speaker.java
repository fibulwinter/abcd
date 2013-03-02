package net.deimon.abcd;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: deimon
 * Date: 4/23/12
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class Speaker implements TextToSpeech.OnInitListener {
    private static Speaker instance = new Speaker();
    private Context context;

    public static Speaker get() {
        return instance;
    }

    private TextToSpeech tts;

    public void init(Context context) {
        this.context = context;
        tts = new TextToSpeech(context, this);
    }

    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // Note that a language may not be available, and the result will indicate this.
            int result = tts.setLanguage(Locale.US);
            // Try this someday for some interesting results.
            // int result mTts.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Lanuage data is missing or the language is not supported.
                Log.e("TTSX", "Language is not available.");
            } else {
                // Check the documentation for other possible result codes.
                // For example, the language may be available for the locale,
                // but not for the specified country and variant.
//                tts.speak("Hello A B C D E F G I J K L M N O P Q R S T U V W X Y Z cat",TextToSpeech.QUEUE_FLUSH,null);
//                tts.speak("Hello",TextToSpeech.QUEUE_FLUSH,null);
//                tts.speak("Cat",TextToSpeech.QUEUE_ADD,null);
//                tts.speak("A",TextToSpeech.QUEUE_ADD,null);
//                tts.speak("B C",TextToSpeech.QUEUE_ADD,null);
                // The TTS engine has been successfully initialized.
                // Allow the user to press the button for the app to speak again.
//                tts.speak("Hello",TextToSpeech.QUEUE_FLUSH, null);
                // Greet the user.
                tts.setSpeechRate(0.75f);
            }
        } else {
            // Initialization failed.
            Log.e("TTSX", "Could not initialize TextToSpeech.");
        }

    }

    public void kill() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    public void speakLater(String s) {
        if (tts != null) {
            tts.speak(s, TextToSpeech.QUEUE_ADD, null);
        }
    }

    public void play(String resourceName) {
        MediaPlayer player = new MediaPlayer();
        Resources resources = context.getResources();
        int id = resources.getIdentifier(resourceName, "raw", "com.example");
        AssetFileDescriptor afd = resources.openRawResourceFd(id);
//				.
//				openRawResourceFd(R.raw.test);
        try {
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            player.setLooping(false);
            player.prepare();
            player.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
