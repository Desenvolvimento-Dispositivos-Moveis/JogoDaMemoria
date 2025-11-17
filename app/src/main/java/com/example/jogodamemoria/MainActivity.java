package com.example.jogodamemoria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.graphics.Typeface;
import com.example.jogodamemoria.model.SoundManager;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.content.res.AssetManager;

import com.example.jogodamemoria.model.Carta;
import com.example.jogodamemoria.model.Logica;
import com.example.jogodamemoria.model.ScoreEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.view.LayoutInflater;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Gson gson = new Gson();

    private Logica jogo;
    private SoundManager soundManager;
    private final Handler handler = new Handler();
    private List<ImageButton> botoesCartas;
    private Map<String, Drawable> imageMap;

    private View menuScreen;
    private View modeScreen;
    private View gameScreen;
    private View rankingScreen;
    private View modalWin;
    private View modalGameOver;

    private TextView textStatus;
    private TextView textLives;
    private GridLayout gridCartas;
    private ImageView menuMonitor;
    private ImageView modeMonitor;
    private Chronometer timer;
    private String modoAtual;
    private TextView winScoreText;
    private TextView gameOverScoreText;
    private LinearLayout rankingListContainer;

    private ImageView winImage;
    private ImageView gameOverImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soundManager = new SoundManager(this);

        setupUIElements();

        initializeImageMap();

        setupListeners();

        initializeStaticImages();

        showMenuScreen();
    }

    private void setupUIElements() {
        menuScreen = findViewById(R.id.menu_screen);
        modeScreen = findViewById(R.id.mode_screen);
        gameScreen = findViewById(R.id.game_screen);
        rankingScreen = findViewById(R.id.ranking_screen);
        rankingListContainer = findViewById(R.id.ranking_list_container);

        modalWin = findViewById(R.id.modal_win);
        modalGameOver = findViewById(R.id.modal_game_over);

        menuMonitor = findViewById(R.id.menu_monitor);
        modeMonitor = findViewById(R.id.mode_monitor);

        textStatus = findViewById(R.id.text_status);
        textLives = findViewById(R.id.text_lives);
        timer = findViewById(R.id.timer);

        winScoreText = findViewById(R.id.win_score_text);
        gameOverScoreText = findViewById(R.id.game_over_score_text);

        winImage = findViewById(R.id.win_image);
        gameOverImage = findViewById(R.id.game_over_image);


        gridCartas = findViewById(R.id.card_grid);

        botoesCartas = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int resID = getResources().getIdentifier("card" + i, "id", getPackageName());
            ImageButton button = findViewById(resID);
            if (button != null) {
                botoesCartas.add(button);
            }
        }
    }

    private void setupListeners() {
        findViewById(R.id.button_start).setOnClickListener(v -> showDifficultyScreen());
        findViewById(R.id.button_ranking).setOnClickListener(v -> showRankingScreen());

        findViewById(R.id.button_easy).setOnClickListener(v -> startGame(3));
        findViewById(R.id.button_normal).setOnClickListener(v -> startGame(5));
        findViewById(R.id.button_hard).setOnClickListener(v -> startGame(6));

        findViewById(R.id.button_reset).setOnClickListener(v -> showMenuScreen());

        findViewById(R.id.button_rank_easy).setOnClickListener(v -> loadRanking("Easy"));
        findViewById(R.id.button_rank_normal).setOnClickListener(v -> loadRanking("Normal"));
        findViewById(R.id.button_rank_hard).setOnClickListener(v -> loadRanking("Hard"));
        findViewById(R.id.button_return).setOnClickListener(v -> showMenuScreen());

        findViewById(R.id.button_win_continue).setOnClickListener(v -> showMenuScreen());
        findViewById(R.id.button_game_over_continue).setOnClickListener(v -> showMenuScreen());

        for (int i = 0; i < botoesCartas.size(); i++) {
            final int index = i;
            botoesCartas.get(i).setOnClickListener(v -> aoClicarNaCarta(index));
        }
    }

    private void showMenuScreen() {
        if (timer != null) timer.stop();
        soundManager.playMenuMusic();
        if (modalWin != null) modalWin.setVisibility(View.GONE);
        if (modalGameOver != null) modalGameOver.setVisibility(View.GONE);

        if (menuScreen != null) menuScreen.setVisibility(View.VISIBLE);
        if (modeScreen != null) modeScreen.setVisibility(View.GONE);
        if (gameScreen != null) gameScreen.setVisibility(View.GONE);
        if (rankingScreen != null) rankingScreen.setVisibility(View.GONE);

        Drawable background = loadAssetDrawableStatic("fundo_circuito_pc");
        if (menuScreen != null && background != null) {
            menuScreen.setBackground(background);
        }
    }

    private void showDifficultyScreen() {

        if (menuScreen != null) menuScreen.setVisibility(View.GONE);
        if (modeScreen != null) modeScreen.setVisibility(View.VISIBLE);
        if (gameScreen != null) gameScreen.setVisibility(View.GONE);
        if (rankingScreen != null) rankingScreen.setVisibility(View.GONE);

        Drawable background = loadAssetDrawableStatic("fundo_circuito");
        if (modeScreen != null && background != null) {
            modeScreen.setBackground(background);
        }
    }

    private void showGameScreen() {

        if (menuScreen != null) menuScreen.setVisibility(View.GONE);
        if (modeScreen != null) modeScreen.setVisibility(View.GONE);
        if (gameScreen != null) gameScreen.setVisibility(View.VISIBLE);
        if (rankingScreen != null) rankingScreen.setVisibility(View.GONE);
    }

    private void showRankingScreen() {

        if (menuScreen != null) menuScreen.setVisibility(View.GONE);
        if (modeScreen != null) modeScreen.setVisibility(View.GONE);
        if (gameScreen != null) gameScreen.setVisibility(View.GONE);
        if (rankingScreen != null) rankingScreen.setVisibility(View.VISIBLE);

        Drawable background = loadAssetDrawableStatic("fundo_circuito");

        if (rankingScreen != null && background != null) {
            rankingScreen.setBackground(background);
        }

        loadRanking("Easy");
    }

    private void startGame(int numPares) {
        showGameScreen();

        if (numPares == 3) modoAtual = "Easy";
        else if (numPares == 5) modoAtual = "Normal";
        else modoAtual = "Hard";

        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        if (numPares > 6) numPares = 6;

        jogo = new Logica();
        jogo.iniciarJogo(numPares, true);

        for (int i = 0; i < botoesCartas.size(); i++) {
            botoesCartas.get(i).setVisibility(i < jogo.getMesa().size() ? View.VISIBLE : View.GONE);
        }

        if (gridCartas != null) {
            gridCartas.setColumnCount(2);
        }

        jogo.virarTodasCartas();
        updateDisplay();

        disableAllCardButtons();

        handler.postDelayed(() -> {
            jogo.desvirarTodasCartas();

            updateDisplay();

            enableAllCardButtons();
        }, 2000);
    }

    private void aoClicarNaCarta(int index) {
        if (jogo == null || jogo.getEstadoJogo() != 1) return;
        soundManager.playSoundVirar();
        jogo.aoClicarNaCarta(index);
        updateDisplay();

        if (jogo.getNumViradas() == 2) {
            disableAllCardButtons();
            handler.postDelayed(() -> {
                boolean match = jogo.verificaCombinacao();

                if (!match) {
                    jogo.desvirarViradas();
                    soundManager.playSoundErro();
                }else{
                    soundManager.playSoundAcerto();
                }

                updateDisplay();
                checkGameOver();
                enableAllCardButtons();
            }, 1000);
        }
    }

    private void checkGameOver() {
        if (jogo.getEstadoJogo() == 2 || jogo.getEstadoJogo() == 3) {
            timer.stop();
            long tempoDecorridoMs = SystemClock.elapsedRealtime() - timer.getBase();
            String tempoFormatado = formatarTempo(tempoDecorridoMs);
            int numTentativas = jogo.getTentativas();

            if (jogo.getEstadoJogo() == 2) {
                if (gameOverScoreText != null) {
                    gameOverScoreText.setText(String.format("Tempo: %s\nTentativas: %d", tempoFormatado, numTentativas));
                }

                if (gameOverImage != null) {
                    gameOverImage.setImageDrawable(loadAssetDrawableStatic("Lose"));
                }

                if (modalGameOver != null) {
                    modalGameOver.setVisibility(View.VISIBLE);
                }
            } else if (jogo.getEstadoJogo() == 3) {

                soundManager.playTriunfoMusic();

                if (winScoreText != null) {
                    winScoreText.setText(String.format("Tempo: %s\nTentativas: %d", tempoFormatado, numTentativas));
                }

                if (winImage != null) {
                    winImage.setImageDrawable(loadAssetDrawableStatic("Win"));
                }

                if (modalWin != null) {
                    modalWin.setVisibility(View.VISIBLE);
                }

                saveScore(modoAtual, tempoDecorridoMs, numTentativas);
            }
        }
    }

    private Drawable loadAssetDrawable(String assetName) {
        AssetManager assetManager = getAssets();
        try (InputStream inputStream = assetManager.open(assetName.toLowerCase(Locale.ROOT) + "-asset.png")) {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return new BitmapDrawable(getResources(), bitmap);
        } catch (IOException e) {
            Log.e("ASSET_LOADER", "Erro ao carregar asset: " + assetName + ". Detalhes: " + e.getMessage());
            return getResources().getDrawable(R.drawable.ic_card_back, getTheme());
        }
    }

    private Drawable loadAssetDrawableStatic(String assetName) {
        AssetManager assetManager = getAssets();
        try (InputStream inputStream = assetManager.open(assetName + ".png")) {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return new BitmapDrawable(getResources(), bitmap);
        } catch (IOException e) {
            Log.e("ASSET_LOADER", "Erro ao carregar asset estático: " + assetName + ". Detalhes: " + e.getMessage());
            return null;
        }
    }

    private void initializeImageMap() {
        imageMap = new HashMap<>();
        List<String> componentNames = Arrays.asList(
                "chip", "hd", "memoria-ram", "monitor", "pc", "placa-de-video", "placa-mae"
        );
        for (String name : componentNames) {
            imageMap.put(name, loadAssetDrawable(name));
        }
    }



    private void initializeStaticImages() {
        if (menuMonitor != null) {
            menuMonitor.setImageDrawable(loadAssetDrawableStatic("monitor_undefined"));
        }
        if (modeMonitor != null) {
            modeMonitor.setImageDrawable(loadAssetDrawableStatic("monitor_game_mode"));
        }
    }

    private void updateDisplay() {
        if (jogo == null) return;

        if (textLives != null) {
            textLives.setText(String.format("Vidas: %d", jogo.getVidaPlayer()));
        }

        if (textStatus != null) {
            textStatus.setText(String.format("Pares: %d/%d",
                    jogo.getParesEncontrados(), jogo.getTotalPares()));
        }

        List<Carta> mesa = jogo.getMesa();
        Drawable cardBack = getResources().getDrawable(R.drawable.ic_card_back, getTheme());

        for (int i = 0; i < mesa.size(); i++) {
            ImageButton button = botoesCartas.get(i);
            Carta carta = mesa.get(i);
            Drawable face = imageMap.get(carta.getImg());

            if (carta.isEncontrada()) {
                button.setImageDrawable(face);
                button.setEnabled(false);
                button.setAlpha(0.6f);
            } else if (carta.isEstado()) {
                button.setImageDrawable(face);
                button.setEnabled(false);
                button.setAlpha(1.0f);
            } else {
                button.setImageDrawable(cardBack);
                button.setEnabled(true);
                button.setAlpha(1.0f);
            }
        }
    }

    private void disableAllCardButtons() {
        for (ImageButton button : botoesCartas) {
            button.setEnabled(false);
        }
    }

    private void enableAllCardButtons() {
        if (jogo == null || jogo.getEstadoJogo() != 1) return;
        List<Carta> mesa = jogo.getMesa();
        for (int i = 0; i < mesa.size(); i++) {
            if (!mesa.get(i).isEncontrada() && !mesa.get(i).isEstado()) {
                botoesCartas.get(i).setEnabled(true);
            }
        }
    }

    private String formatarTempo(long ms) {
        long segundos = (ms / 1000) % 60;
        long minutos = (ms / (1000 * 60)) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutos, segundos);
    }

    private void loadRanking(String difficulty) {
        SharedPreferences prefs = getSharedPreferences("RankingPrefs", MODE_PRIVATE);
        String key = "ranking_" + difficulty;
        String json = prefs.getString(key, null);

        List<ScoreEntry> scores;
        if (json == null) {
            scores = new ArrayList<>();
        } else {
            Type listType = new TypeToken<ArrayList<ScoreEntry>>() {}.getType();
            scores = gson.fromJson(json, listType);
        }

        rankingListContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);

        if (scores.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText("Nenhum score salvo");
            try {
                emptyView.setTextColor(getResources().getColor(R.color.cor_principal, getTheme()));
            } catch (Exception e) {
                emptyView.setTextColor(android.graphics.Color.WHITE);
            }

            emptyView.setTypeface(ResourcesCompat.getFont(this, R.font.pixel_font));
            emptyView.setTextSize(18);

            emptyView.setPadding(0, 20, 0, 0);
            rankingListContainer.addView(emptyView);
            return;
        }

        int position = 1;
        for (ScoreEntry score : scores) {

            View scoreView = inflater.inflate(R.layout.item_ranking, rankingListContainer, false);

            TextView textPosition = scoreView.findViewById(R.id.text_rank_position);
            TextView textTime = scoreView.findViewById(R.id.text_rank_time);
            TextView textAttempts = scoreView.findViewById(R.id.text_rank_attempts);

            String posStr = String.format(Locale.getDefault(), "#%02d", position);
            String timeStr = formatarTempo(score.getTimeInMillis());
            String attStr = String.format(Locale.getDefault(), "%04d", score.getAttempts());

            textPosition.setText(posStr);
            textTime.setText(timeStr);
            textAttempts.setText(attStr);

            rankingListContainer.addView(scoreView);
            position++;
        }
    }

    private void saveScore(String difficulty, long time, int attempts) {
        SharedPreferences prefs = getSharedPreferences("RankingPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String key = "ranking_" + difficulty;
        String json = prefs.getString(key, null);

        List<ScoreEntry> scores;
        if (json == null) {
            scores = new ArrayList<>();
        } else {
            Type listType = new TypeToken<ArrayList<ScoreEntry>>() {}.getType();
            scores = gson.fromJson(json, listType);
        }

        scores.add(new ScoreEntry(time, attempts));
        Collections.sort(scores);

        if (scores.size() > 10) {
            scores = scores.subList(0, 10);
        }

        String newJson = gson.toJson(scores);
        editor.putString(key, newJson);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundManager.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (menuScreen.getVisibility() == View.VISIBLE) {
            soundManager.resumeMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundManager.release();
    }

}