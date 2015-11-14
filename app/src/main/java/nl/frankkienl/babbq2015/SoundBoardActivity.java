package nl.frankkienl.babbq2015;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaCodecInfo;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by FrankkieNL on 11/14/2015.
 */
public class SoundBoardActivity extends AppCompatActivity {

    SoundPool soundPool;
    SoundPool.OnLoadCompleteListener onLoadCompleteListener;
    ArrayList<Integer> soundIds = new ArrayList<Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initSounds();
    }

    public void initSounds() {

        findViewById(R.id.soundboard_btn).setEnabled(false);

        if (Build.VERSION.SDK_INT >= 21) {
            //Argh all these builders are driving me nuts.
            AudioAttributes aa = new AudioAttributes
                    .Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(aa)
                    .setMaxStreams(1)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        onLoadCompleteListener = new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.v("BABBQ Soundboard", "Loading complete");
                findViewById(R.id.soundboard_btn).setEnabled(true);
            }
        };

        soundPool.setOnLoadCompleteListener(onLoadCompleteListener);

        //Load sounds
        soundIds.add(soundPool.load(this, R.raw.all_yours_partner, 1));
        soundIds.add(soundPool.load(this, R.raw.be_ready_for_a_ride, 1));
        soundIds.add(soundPool.load(this, R.raw.can_you_ever_forgive_me, 1));
        soundIds.add(soundPool.load(this, R.raw.fancy_mathematics, 1));
        soundIds.add(soundPool.load(this, R.raw.geronimo, 1));
        soundIds.add(soundPool.load(this, R.raw.hint_hint, 1));
        soundIds.add(soundPool.load(this, R.raw.hmmmm_nah, 1));
        soundIds.add(soundPool.load(this, R.raw.hoho_there_lover_boy, 1));
        soundIds.add(soundPool.load(this, R.raw.i_told_you_so, 1));
        soundIds.add(soundPool.load(this, R.raw.im_applejack, 1));
        soundIds.add(soundPool.load(this, R.raw.no_can_do_sugar_cube, 1));
        soundIds.add(soundPool.load(this, R.raw.oooohoooo, 1));
        soundIds.add(soundPool.load(this, R.raw.oops_sry, 1));
        soundIds.add(soundPool.load(this, R.raw.what_in_tarnation, 1));
        soundIds.add(soundPool.load(this, R.raw.yeehaw, 1));
        soundIds.add(soundPool.load(this, R.raw.youre_welcome, 1));
    }

    public void playSound(View v) {
        soundPool.play(
                soundIds.get((int) (Math.random() * soundIds.size())) /* get random id */
                , 1, 1, 1, 0 /*repeat, 0=play once*/, 1);
    }

}
