package me.t3sl4.upcortex.UI.Screens.Exam;

import android.app.AlertDialog;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import me.t3sl4.upcortex.Model.Exam.CategoryInfo;
import me.t3sl4.upcortex.Model.Exam.Difficulty;
import me.t3sl4.upcortex.Model.Exam.Exam;
import me.t3sl4.upcortex.Model.Exam.Question;
import me.t3sl4.upcortex.Model.Exam.QuestionCategory;
import me.t3sl4.upcortex.Model.Exam.QuestionOption;
import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.CircularCountdown.CircularCountdownView;
import me.t3sl4.upcortex.Utility.Screen.ScreenUtil;

public class ExamProcess extends AppCompatActivity {
    private Exam receivedExam;

    // DifficultyStars
    private ImageView difficultyStarOne;
    private ImageView difficultyStarTwo;
    private ImageView difficultyStarThree;

    private TextView difficultyText;
    private TextView categoryOrder;
    private TextView categoryName;
    private CircularCountdownView circularCountdownView;

    private Button preTextButton;
    private Button answerButton;
    private TextView mainText;

    private LinearLayout imageQuestionLayout;

    // Image Question Layout
    private ImageView imageView1_1;
    private ImageView imageView1_2;
    private ImageView imageView1_3;
    private ImageView imageView2_1;
    private ImageView imageView2_2;
    private ImageView imageView2_3;
    private ImageView imageView3_1;
    private ImageView imageView3_2;
    private ImageView imageView3_3;
    private ImageView imageView4_1;
    private ImageView imageView4_2;
    private ImageView imageView4_3;

    private long questionTime = 10000; // 10 saniye
    private long answerTime = 10000;   // 10 saniye

    private List<CategoryInfo> categoryInfoList = new ArrayList<>();
    private int examPoint = 0;

    private List<ImageView> imageViewList = new ArrayList<>();

    private List<Question> shortTermMemoryQuestions = new ArrayList<>();
    private List<Question> longTermMemoryQuestions = new ArrayList<>();
    private List<Question> visualMemoryQuestions = new ArrayList<>();
    private List<Question> proceduralTermMemoryQuestions = new ArrayList<>();

    private int currentCategoryIndex = 0; // İşlenecek mevcut kategori indeksi

    private int currentQuestionIndex = 0; // Soru indeksi
    private Question currentQuestion;
    private List<QuestionOption> currentOptions = new ArrayList<>();

    // Yeni Eklenen Listeler
    private List<QuestionOption> correctOptionsList = new ArrayList<>();
    private List<QuestionOption> allOptionsRandomizedList = new ArrayList<>();
    private List<ImageView> selectedImageViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_process);

        // JSON verisini al ve Exam nesnesine dönüştür
        String examJson = getIntent().getStringExtra("examJson");
        if (examJson != null) {
            Gson gson = new Gson();
            receivedExam = gson.fromJson(examJson, Exam.class);
            Log.d("ExamProcess", "Exam nesnesi başarıyla alındı: " + receivedExam.getExamName());
        } else {
            Log.e("ExamProcess", "Exam JSON verisi alınamadı!");
            // Hata durumunda geri dön veya kullanıcıya bildirim göster
            finish();
        }

        ScreenUtil.hideNavAndStatus(ExamProcess.this);
        ScreenUtil.fullScreenMode(ExamProcess.this);

        initializeComponents();
        processExamData();
        sortQuestions();

        processCurrentCategory();
    }

    private void initializeComponents() {
        difficultyStarOne = findViewById(R.id.difficultyStarOne);
        difficultyStarTwo = findViewById(R.id.difficultyStarTwo);
        difficultyStarThree = findViewById(R.id.difficultyStarThree);

        difficultyText = findViewById(R.id.difficultyText);
        categoryOrder = findViewById(R.id.categoryOrder);
        categoryName = findViewById(R.id.categoryName);
        circularCountdownView = findViewById(R.id.circularCountdownView);

        preTextButton = findViewById(R.id.preTextButton);
        answerButton = findViewById(R.id.answerButton);
        mainText = findViewById(R.id.mainText);

        imageQuestionLayout = findViewById(R.id.imageQuestionLayout);

        // Image Question Layout
        imageView1_1 = findViewById(R.id.imageView1_1);
        imageView1_2 = findViewById(R.id.imageView1_2);
        imageView1_3 = findViewById(R.id.imageView1_3);
        imageView2_1 = findViewById(R.id.imageView2_1);
        imageView2_2 = findViewById(R.id.imageView2_2);
        imageView2_3 = findViewById(R.id.imageView2_3);
        imageView3_1 = findViewById(R.id.imageView3_1);
        imageView3_2 = findViewById(R.id.imageView3_2);
        imageView3_3 = findViewById(R.id.imageView3_3);
        imageView4_1 = findViewById(R.id.imageView4_1);
        imageView4_2 = findViewById(R.id.imageView4_2);
        imageView4_3 = findViewById(R.id.imageView4_3);

        imageViewList.add(imageView1_1);
        imageViewList.add(imageView1_2);
        imageViewList.add(imageView1_3);
        imageViewList.add(imageView2_1);
        imageViewList.add(imageView2_2);
        imageViewList.add(imageView2_3);
        imageViewList.add(imageView3_1);
        imageViewList.add(imageView3_2);
        imageViewList.add(imageView3_3);
        imageViewList.add(imageView4_1);
        imageViewList.add(imageView4_2);
        imageViewList.add(imageView4_3);

        // Başlangıçta imageQuestionLayout görünmez yap
        imageQuestionLayout.setVisibility(View.GONE);

        // AnswerButton ve mainText başlangıçta görünmez
        answerButton.setVisibility(View.GONE);
        mainText.setVisibility(View.GONE);

        // AnswerButton'a tıklama dinleyicisi ekle
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserAnswers(new CategoryCompletionListener() {
                    @Override
                    public void onCategoryCompleted() {
                        // Kategori tamamlandı, sonraki kategoriye geç
                        currentCategoryIndex++;
                        processCurrentCategory();
                    }
                });
            }
        });

        // preTextButton'ın işlevselliği sadece metni göstermek ile sınırlı
        preTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preTextButton.setVisibility(View.GONE);
                mainText.setVisibility(View.VISIBLE);
            }
        });
    }

    private void processExamData() {
        if (receivedExam != null && receivedExam.getQuestions() != null) {
            LinkedList<QuestionCategory> categories = receivedExam.getQuestionCategories();
            if (categories != null) {
                Log.d("ProcessExamData", "Kategori sayısı: " + categories.size());
                for (QuestionCategory category : categories) {
                    Log.d("ProcessExamData", "Kategori: " + category.getName() + ", Sıra: " + category.getOrder());
                    CategoryInfo info = new CategoryInfo(category.getName(), category.getOrder());
                    categoryInfoList.add(info);
                }

                // Kategori sırasına göre sırala
                Collections.sort(categoryInfoList, Comparator.comparingInt(CategoryInfo::getOrder));

                // Sıralandıktan sonra logla
                Log.d("ProcessExamData", "Sıralandıktan sonra kategori listesi:");
                for (CategoryInfo info : categoryInfoList) {
                    Log.d("ProcessExamData", "Kategori: " + info.getName() + ", Sıra: " + info.getOrder());
                }
            } else {
                Log.d("ProcessExamData", "Kategoriler null veya boş");
            }

            // Soruları kategorilere göre say
            LinkedList<Question> questions = receivedExam.getQuestions();
            if (questions != null) {
                Log.d("ProcessExamData", "Soru sayısı: " + questions.size());
                for (Question question : questions) {
                    String categoryName = question.getCategoryName();
                    Log.d("ProcessExamData", "Soru kategorisi: " + categoryName);
                    boolean matched = false;
                    for (CategoryInfo info : categoryInfoList) {
                        if (info.getName().trim().equalsIgnoreCase(categoryName.trim())) {
                            info.incrementQuestionCount();
                            matched = true;
                            Log.d("ProcessExamData", "Kategori eşleşti: " + categoryName);
                            break;
                        }
                    }
                    if (!matched) {
                        Log.d("ProcessExamData", "Kategori eşleşmedi: " + categoryName);
                    }
                }
            } else {
                Log.d("ProcessExamData", "Sorular null veya boş");
            }
            displayCategoryInfo();
        } else {
            Log.d("ProcessExamData", "receivedExam veya questions null");
        }
    }

    private void sortQuestions() {
        for (Question question : receivedExam.getQuestions()) {
            String categoryName = question.getCategoryName();
            if (categoryName.equals("Kısa Süreli Bellek")) {
                shortTermMemoryQuestions.add(question);
            } else if (categoryName.equals("Uzun Süreli Bellek")) {
                longTermMemoryQuestions.add(question);
            } else if (categoryName.equals("Görsel Bellek")) {
                visualMemoryQuestions.add(question);
            } else if (categoryName.equals("İşlemsel Bellek")) {
                proceduralTermMemoryQuestions.add(question);
            }
        }
    }

    private void displayCategoryInfo() {
        for (CategoryInfo info : categoryInfoList) {
            Log.d("CategoryInfo", "Kategori: " + info.getName() +
                    ", Sıra: " + info.getOrder() +
                    ", Soru Sayısı: " + info.getQuestionCount() +
                    ", Kullanıcı Puanı: " + info.getUserPoint());
        }

        Log.d("ExamPoint", "Toplam Sınav Puanı: " + examPoint);
    }

    private void difficultyMode(Difficulty difficultyEnum) {
        String difficultyString = "";
        if (difficultyEnum.name().equals("beginner")) {
            setStarColor(difficultyStarOne, R.color.warningColor);
            setStarColor(difficultyStarTwo, R.color.darkOnTop);
            setStarColor(difficultyStarThree, R.color.darkOnTop);
            difficultyString = getString(R.string.exam_difficulty_beginner);
        } else if (difficultyEnum.name().equals("medium")) {
            setStarColor(difficultyStarOne, R.color.warningColor);
            setStarColor(difficultyStarTwo, R.color.warningColor);
            setStarColor(difficultyStarThree, R.color.darkOnTop);
            difficultyString = getString(R.string.exam_difficulty_medium);
        } else if (difficultyEnum.name().equals("hard")) {
            setStarColor(difficultyStarOne, R.color.warningColor);
            setStarColor(difficultyStarTwo, R.color.warningColor);
            setStarColor(difficultyStarThree, R.color.warningColor);
            difficultyString = getString(R.string.exam_difficulty_hard);
        }

        difficultyText.setText(difficultyString);
    }

    private void setStarColor(ImageView star, int colorResId) {
        int color = ContextCompat.getColor(this, colorResId);
        star.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
    }

    // Sayaç tamamlandığında tetiklenecek dinleyici arayüzü
    interface CountdownListener {
        void onCountdownFinished();
    }

    // Kategori tamamlandığında tetiklenecek dinleyici arayüzü
    interface CategoryCompletionListener {
        void onCategoryCompleted();
    }

    // Seçenekler gösterildiğinde tetiklenecek dinleyici arayüzü
    interface OptionsDisplayListener {
        void onOptionsDisplayed();
    }

    // Seçenekler rastgeleleştirildiğinde tetiklenecek dinleyici arayüzü
    interface OptionsRandomizedListener {
        void onOptionsRandomized();
    }

    private void startCountdown(long duration, CountdownListener listener) {
        new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                circularCountdownView.setRemainingTime(millisUntilFinished);
            }

            public void onFinish() {
                circularCountdownView.setRemainingTime(0);
                if (listener != null) {
                    listener.onCountdownFinished();
                }
            }
        }.start();
    }

    private void processCurrentCategory() {
        if (currentCategoryIndex >= categoryInfoList.size()) {
            // Tüm kategoriler tamamlandı
            Log.d("ExamProcess", "Tüm kategoriler tamamlandı.");
            // Gerekirse sonuçları göster veya başka işlemler yap
            showFinalScore();
            return;
        }

        CategoryInfo currentCategory = categoryInfoList.get(currentCategoryIndex);
        categoryName.setText(currentCategory.getName());
        categoryOrder.setText(String.valueOf(currentCategory.getOrder()));

        if (currentCategory.getName().equals("Kısa Süreli Bellek")) {
            imageQuestionLayout.setVisibility(View.VISIBLE);
            startShortTermExam(new CategoryCompletionListener() {
                @Override
                public void onCategoryCompleted() {
                    // Bu kategori tamamlandı, sonraki kategoriye geç
                    currentCategoryIndex++;
                    processCurrentCategory();
                }
            });
        } else {
            imageQuestionLayout.setVisibility(View.GONE);
            // Diğer kategoriler için gerekli işlemleri ekleyin
            // Şu anda sadece "Kısa Süreli Bellek" işleniyor
            currentCategoryIndex++;
            processCurrentCategory();
        }
    }

    private void startShortTermExam(CategoryCompletionListener listener) {
        if (shortTermMemoryQuestions.isEmpty()) {
            Log.d("ExamProcess", "Kısa Süreli Bellek kategorisinde hiç soru yok.");
            listener.onCategoryCompleted();
            return;
        }

        currentQuestionIndex = 0; // Soru indeksini sıfırla
        examPoint = 0; // Puanı sıfırla
        displayNextQuestion(listener);
    }

    private void displayNextQuestion(CategoryCompletionListener listener) {
        if (currentQuestionIndex >= shortTermMemoryQuestions.size()) {
            // Tüm sorular tamamlandı
            imageQuestionLayout.setVisibility(View.GONE);
            Log.d("ExamProcess", "Kısa Süreli Bellek kategorisindeki tüm sorular tamamlandı.");
            listener.onCategoryCompleted();
            return;
        }

        currentQuestion = shortTermMemoryQuestions.get(currentQuestionIndex);
        mainText.setText(currentQuestion.getMainText());
        preTextButton.setText(currentQuestion.getPreText());

        // Difficulty Mode'u çağır
        difficultyMode(currentQuestion.getDifficulty());

        // Layout ve butonları ayarla
        imageQuestionLayout.setVisibility(View.VISIBLE);
        preTextButton.setVisibility(View.VISIBLE);
        answerButton.setVisibility(View.GONE);
        mainText.setVisibility(View.GONE);

        // Doğru ve tüm seçenekleri ayır
        correctOptionsList.clear();
        List<QuestionOption> incorrectOptions = new ArrayList<>();

        for (QuestionOption option : currentQuestion.getQuestionOptions()) {
            if (option.isCorrect()) {
                correctOptionsList.add(option);
            } else {
                incorrectOptions.add(option);
            }
        }

        // Doğru seçenekleri order'a göre sırala
        Collections.sort(correctOptionsList, Comparator.comparingInt(QuestionOption::getOrder));

        // Tüm seçenekleri birleştir ve karıştır
        allOptionsRandomizedList.clear();
        allOptionsRandomizedList.addAll(currentQuestion.getQuestionOptions());
        Collections.shuffle(allOptionsRandomizedList);

        // İlk 10 saniye için doğru görselleri göster
        List<String> correctImageFileNames = new ArrayList<>();
        for (QuestionOption option : correctOptionsList) {
            correctImageFileNames.add(option.getFileName());
        }

        locateImages(currentQuestion.getCorrectOptionsCount(), correctImageFileNames);

        // preTextButton'ın tıklanması sadece metni göstermek içindir
        preTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preTextButton.setVisibility(View.GONE);
                mainText.setVisibility(View.VISIBLE);
            }
        });

        // İlk 10 saniyelik sayaç başlat
        startCountdown(questionTime, new CountdownListener() {
            @Override
            public void onCountdownFinished() {
                // Tüm görselleri rastgele sırala ve göster
                List<String> allImageFileNames = new ArrayList<>();
                for (QuestionOption option : allOptionsRandomizedList) {
                    allImageFileNames.add(option.getFileName());
                }

                locateImages(currentQuestion.getTotalOptionsCount(), allImageFileNames);

                // 2. sayaç için answerButton'ı göster ve preTextButton'ı gizle
                answerButton.setVisibility(View.VISIBLE);
                preTextButton.setVisibility(View.GONE);
            }
        });
    }

    private void handleImageClick(ImageView imageView, QuestionOption option) {
        if (selectedImageViews.contains(imageView)) {
            // Görsel zaten seçilmişse, seçimi kaldır
            selectedImageViews.remove(imageView);
            imageView.setAlpha(1.0f);
            imageView.setTag(null);
        } else {
            // Doğru seçenek sayısına ulaşıldıysa ekleme yapma
            if (selectedImageViews.size() < currentQuestion.getCorrectOptionsCount()) {
                selectedImageViews.add(imageView);
                imageView.setAlpha(0.5f); // Seçildiğini belirtmek için opaklığı azalt
                imageView.setTag("selected");
            }
        }
    }

    private void checkUserAnswers(CategoryCompletionListener listener) {
        int correctSelections = 0;

        for (ImageView imageView : selectedImageViews) {
            int index = imageViewList.indexOf(imageView);
            if (index < allOptionsRandomizedList.size()) { // allOptionsRandomizedList kullanıldı
                QuestionOption option = allOptionsRandomizedList.get(index);
                if (option.isCorrect()) {
                    correctSelections++;
                }
            }
        }

        if (correctSelections == currentQuestion.getCorrectOptionsCount()) {
            // Doğru cevap, puan ekle
            examPoint += currentQuestion.getPoint();
            Log.d("ExamProcess", "Doğru cevap! Puan: " + currentQuestion.getPoint());
        } else {
            // Yanlış cevap, puan ekleme
            Log.d("ExamProcess", "Yanlış cevap! Puan alamadınız.");
        }

        // AnswerButton'ı görünmez yap
        answerButton.setVisibility(View.GONE);
        mainText.setVisibility(View.GONE);

        // Bir sonraki soruya geç
        currentQuestionIndex++;
        displayNextQuestion(listener);
    }

    private void locateImages(int imageCount, List<String> imageFileNames) {
        if(imageCount == 2) {
            changeImageVisibility(View.GONE);
            imageView1_1.setVisibility(View.VISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
        } else if(imageCount == 4) {
            changeImageVisibility(View.GONE);
            imageView1_1.setVisibility(View.VISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
            imageView2_1.setVisibility(View.VISIBLE);
            imageView2_2.setVisibility(View.VISIBLE);
        } else if(imageCount == 6) {
            changeImageVisibility(View.GONE);
            imageView1_1.setVisibility(View.VISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
            imageView2_1.setVisibility(View.VISIBLE);
            imageView2_2.setVisibility(View.VISIBLE);
            imageView3_1.setVisibility(View.VISIBLE);
            imageView3_2.setVisibility(View.VISIBLE);
        } else if(imageCount == 8) {
            changeImageVisibility(View.GONE);
            imageView1_1.setVisibility(View.INVISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
            imageView1_3.setVisibility(View.INVISIBLE);
            imageView2_1.setVisibility(View.VISIBLE);
            imageView2_2.setVisibility(View.VISIBLE);
            imageView2_3.setVisibility(View.VISIBLE);
            imageView3_1.setVisibility(View.VISIBLE);
            imageView3_2.setVisibility(View.VISIBLE);
            imageView3_3.setVisibility(View.VISIBLE);
            imageView4_1.setVisibility(View.INVISIBLE);
            imageView4_2.setVisibility(View.VISIBLE);
            imageView4_3.setVisibility(View.INVISIBLE);
        } else if(imageCount == 12) {
            changeImageVisibility(View.VISIBLE);
        }

        // İlk 'imageCount' kadar ImageView'i kullanarak görselleri yükle
        for(int i = 0; i < imageFileNames.size() && i < imageViewList.size(); i++) {
            ImageView imageView = imageViewList.get(i);
            loadImageIntoImageView(imageView, imageFileNames.get(i));
        }
    }

    private void changeImageVisibility(int visibility) {
        for (ImageView imageView : imageViewList) {
            imageView.setVisibility(visibility);
        }
        Log.d("ChangeImageVisibility", "Tüm ImageView'ların görünürlüğü değiştirildi: " + visibility);
    }

    private void loadImageIntoImageView(ImageView imageView, String fileName) {
        String baseUrl = "http://160.20.111.45:3200/file-upload/get-file?fileName=";
        String imageUrl = baseUrl + fileName;

        Glide.with(this)
                .load(imageUrl)
                .into(imageView);
    }

    private void showFinalScore() {
        // Örneğin, bir dialog ile toplam puanı göster
        new AlertDialog.Builder(this)
                .setTitle("Sınav Tamamlandı")
                .setMessage("Toplam Puanınız: " + examPoint)
                .setPositiveButton("Tamam", null)
                .show();
    }
}