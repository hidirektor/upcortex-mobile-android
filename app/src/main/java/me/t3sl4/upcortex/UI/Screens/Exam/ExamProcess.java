package me.t3sl4.upcortex.UI.Screens.Exam;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
import me.t3sl4.upcortex.UI.Components.Sneaker.Sneaker;
import me.t3sl4.upcortex.Utils.Screen.ScreenUtil;
import me.t3sl4.upcortex.Utils.Screen.TextDrawable;

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
    private CircularCountdownView examCountdownView;

    private Button preTextButton;
    private Button answerButton;
    private Button nextButton;
    private TextView mainText;
    private TextView subText;
    private ImageView subImage;

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
    private CardView cardView1_1;
    private CardView cardView1_2;
    private CardView cardView1_3;
    private CardView cardView2_1;
    private CardView cardView2_2;
    private CardView cardView2_3;
    private CardView cardView3_1;
    private CardView cardView3_2;
    private CardView cardView3_3;
    private CardView cardView4_1;
    private CardView cardView4_2;
    private CardView cardView4_3;
    private ImageView imageViewCount1_1;
    private ImageView imageViewCount1_2;
    private ImageView imageViewCount1_3;
    private ImageView imageViewCount2_1;
    private ImageView imageViewCount2_2;
    private ImageView imageViewCount2_3;
    private ImageView imageViewCount3_1;
    private ImageView imageViewCount3_2;
    private ImageView imageViewCount3_3;
    private ImageView imageViewCount4_1;
    private ImageView imageViewCount4_2;
    private ImageView imageViewCount4_3;

    // Text Option Components
    private LinearLayout textQuestionLayout;
    private TextView questionNumber;
    private TextView mainQuestionText;

    private LinearLayout option1Layout;
    private TextView option1Text;
    private ImageView option1Tick;
    private LinearLayout option2Layout;
    private TextView option2Text;
    private ImageView option2Tick;
    private LinearLayout option3Layout;
    private TextView option3Text;
    private ImageView option3Tick;
    private LinearLayout option4Layout;
    private TextView option4Text;
    private ImageView option4Tick;

    private ImageView option1Image;
    private ImageView option2Image;
    private ImageView option3Image;
    private ImageView option4Image;

    private LinearLayout difficultyLinearLayout;
    private LinearLayout difficultyLayout;

    private LinearLayout beforeQuestionLayout;
    private TextView examScenerio;
    private TextView examScenerioDesc;

    private long questionTime = 10000; // 9 seconds for displaying the question
    private long totalExamTime = 30 * 60 * 1000; //30 Dakika

    private List<CategoryInfo> categoryInfoList = new ArrayList<>();
    private float examPoint = 0;

    private List<ImageView> imageViewList = new ArrayList<>();
    private List<CardView> cardViewList = new ArrayList<>();
    private List<ImageView> imageViewCountList = new ArrayList<>();

    private List<String> examCategories = new ArrayList<>();
    private List<Question> questionList = new ArrayList<>();
    private HashMap<String, List<Question>> categoryQuestionsMap = new HashMap<>();

    private int currentCategoryIndex = 0; // Current category index
    private int currentQuestionIndex = 0; // Current question index
    private Question currentQuestion;

    // Newly Added Lists
    private List<QuestionOption> correctOptionsList = new ArrayList<>();
    private List<QuestionOption> allOptionsRandomizedList = new ArrayList<>();
    private List<ImageView> selectedImageViews = new ArrayList<>();
    private List<ImageView> selectedImageCountViews = new ArrayList<>();

    // Timers
    private CountDownTimer questionTimer;
    private CountDownTimer examTimer;
    private boolean hasAnswered = false; // Flag to check if user has answered
    private boolean isAnswerPhase = false; // Flag to determine the current phase
    private boolean isNormalExam = false;

    private Handler handler = new Handler(); // Handler for delayed dialog dismissal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_process);

        // Retrieve JSON data and convert to Exam object
        String examJson = getIntent().getStringExtra("examJson");
        Log.d("Test Json", examJson);
        if (examJson != null) {
            Gson gson = new Gson();
            receivedExam = gson.fromJson(examJson, Exam.class);
            totalExamTime = receivedExam.getExamTime() * 60 * 1000; //Gelen süre kadar dk
            Log.d("ExamProcess", "Exam object successfully received: " + receivedExam.getExamName());
        } else {
            Log.e("ExamProcess", "Exam JSON data could not be retrieved!");
            // Handle error by closing the activity or notifying the user
            finish();
            return;
        }

        ScreenUtil.hideNavAndStatus(ExamProcess.this);
        ScreenUtil.fullScreenMode(ExamProcess.this);

        initializeComponents();
        processExamData();

        startExamTimer(totalExamTime, () -> {
            showFinalScore();
        });

        if(receivedExam.getExamName().equals("Risk Düzeyi Hesaplama Sınavı")) {
            processCurrentCategory();
        } else {
            isNormalExam = true;
            handleNormalExam();
        }
    }

    /**
     * Initialize all UI components and set up listeners.
     */
    private void initializeComponents() {
        // Initialize Difficulty Stars
        difficultyStarOne = findViewById(R.id.difficultyStarOne);
        difficultyStarTwo = findViewById(R.id.difficultyStarTwo);
        difficultyStarThree = findViewById(R.id.difficultyStarThree);

        // Initialize TextViews and Countdown
        difficultyText = findViewById(R.id.difficultyText);
        categoryOrder = findViewById(R.id.categoryOrder);
        categoryName = findViewById(R.id.categoryName);
        circularCountdownView = findViewById(R.id.circularCountdownView);
        examCountdownView = findViewById(R.id.examCountdownView);

        // Initialize Buttons and Main Text
        preTextButton = findViewById(R.id.preTextButton);
        answerButton = findViewById(R.id.answerButton);
        nextButton = findViewById(R.id.nextButton);
        mainText = findViewById(R.id.mainText);
        subText = findViewById(R.id.subText);
        subImage = findViewById(R.id.subImage);

        // Initialize Image Question Layout
        imageQuestionLayout = findViewById(R.id.imageQuestionLayout);

        // Initialize ImageViews
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
        cardView1_1 = findViewById(R.id.cardView1_1);
        cardView1_2 = findViewById(R.id.cardView1_2);
        cardView1_3 = findViewById(R.id.cardView1_3);
        cardView2_1 = findViewById(R.id.cardView2_1);
        cardView2_2 = findViewById(R.id.cardView2_2);
        cardView2_3 = findViewById(R.id.cardView2_3);
        cardView3_1 = findViewById(R.id.cardView3_1);
        cardView3_2 = findViewById(R.id.cardView3_2);
        cardView3_3 = findViewById(R.id.cardView3_3);
        cardView4_1 = findViewById(R.id.cardView4_1);
        cardView4_2 = findViewById(R.id.cardView4_2);
        cardView4_3 = findViewById(R.id.cardView4_3);
        imageViewCount1_1 = findViewById(R.id.imageViewCount1_1);
        imageViewCount1_2 = findViewById(R.id.imageViewCount1_2);
        imageViewCount1_3 = findViewById(R.id.imageViewCount1_3);
        imageViewCount2_1 = findViewById(R.id.imageViewCount2_1);
        imageViewCount2_2 = findViewById(R.id.imageViewCount2_2);
        imageViewCount2_3 = findViewById(R.id.imageViewCount2_3);
        imageViewCount3_1 = findViewById(R.id.imageViewCount3_1);
        imageViewCount3_2 = findViewById(R.id.imageViewCount3_2);
        imageViewCount3_3 = findViewById(R.id.imageViewCount3_3);
        imageViewCount4_1 = findViewById(R.id.imageViewCount4_1);
        imageViewCount4_2 = findViewById(R.id.imageViewCount4_2);
        imageViewCount4_3 = findViewById(R.id.imageViewCount4_3);

        // Add all ImageViews to the list for easy management
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
        cardViewList.add(cardView1_1);
        cardViewList.add(cardView1_2);
        cardViewList.add(cardView1_3);
        cardViewList.add(cardView2_1);
        cardViewList.add(cardView2_2);
        cardViewList.add(cardView2_3);
        cardViewList.add(cardView3_1);
        cardViewList.add(cardView3_2);
        cardViewList.add(cardView3_3);
        cardViewList.add(cardView4_1);
        cardViewList.add(cardView4_2);
        cardViewList.add(cardView4_3);
        imageViewCountList.add(imageViewCount1_1);
        imageViewCountList.add(imageViewCount1_2);
        imageViewCountList.add(imageViewCount1_3);
        imageViewCountList.add(imageViewCount2_1);
        imageViewCountList.add(imageViewCount2_2);
        imageViewCountList.add(imageViewCount2_3);
        imageViewCountList.add(imageViewCount3_1);
        imageViewCountList.add(imageViewCount3_2);
        imageViewCountList.add(imageViewCount3_3);
        imageViewCountList.add(imageViewCount4_1);
        imageViewCountList.add(imageViewCount4_2);
        imageViewCountList.add(imageViewCount4_3);

        // Initially hide the imageQuestionLayout
        imageQuestionLayout.setVisibility(View.GONE);

        // Initialize Text Question Layout
        textQuestionLayout = findViewById(R.id.textQuestionLayout);
        questionNumber = findViewById(R.id.questionOrder);
        mainQuestionText = findViewById(R.id.mainQuestionText);

        beforeQuestionLayout = findViewById(R.id.beforeQuestionLayout);
        examScenerio = findViewById(R.id.examScenerio);
        examScenerioDesc = findViewById(R.id.examScenerioDesc);

        option1Layout = findViewById(R.id.option1Layout);
        option1Text = findViewById(R.id.option1Text);
        option1Tick = findViewById(R.id.option1Tick);
        option2Layout = findViewById(R.id.option2Layout);
        option2Text = findViewById(R.id.option2Text);
        option2Tick = findViewById(R.id.option2Tick);
        option3Layout = findViewById(R.id.option3Layout);
        option3Text = findViewById(R.id.option3Text);
        option3Tick = findViewById(R.id.option3Tick);
        option4Layout = findViewById(R.id.option4Layout);
        option4Text = findViewById(R.id.option4Text);
        option4Tick = findViewById(R.id.option4Tick);

        option1Image = findViewById(R.id.option1Image);
        option2Image = findViewById(R.id.option2Image);
        option3Image = findViewById(R.id.option3Image);
        option4Image = findViewById(R.id.option4Image);

        difficultyLinearLayout = findViewById(R.id.difficultyLinearLayout);
        difficultyLayout = findViewById(R.id.difficultyLayout);

        // Initially hide answerButton and mainText for image questions
        answerButton.setVisibility(View.GONE);
        mainText.setVisibility(View.GONE);

        // Set up AnswerButton click listener
        answerButton.setOnClickListener(view -> handleAnswerButtonClick());

        // Add click listeners to all ImageViews for selection
        for (int i = 0; i < imageViewList.size(); i++) {
            ImageView imageView = imageViewList.get(i);
            ImageView imageCount = imageViewCountList.get(i);
            imageView.setOnClickListener(view -> handleImageClick(imageView, imageCount));
        }

        // Add click listeners to text option layouts
        setupTextOptionClickListeners();

        nextButton.setOnClickListener(v -> {
            beforeQuestionLayout.setVisibility(View.GONE);
            circularCountdownView.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            //questionTimer.cancel();
            processNormalCurrentCategory();
        });
    }

    /**
     * Processes the received exam data by categorizing and counting questions.1
     */
    private void processExamData() {
        if (receivedExam != null && receivedExam.getQuestions() != null) {
            // Sadece Risk Düzeyi Hesaplama Sınavı için özel işlemler yapılacak
            if (receivedExam.getExamName().equals("Risk Düzeyi Hesaplama Sınavı")) {
                LinkedList<QuestionCategory> categories = receivedExam.getQuestionCategories();
                if (categories != null) {
                    for (QuestionCategory category : categories) {
                        CategoryInfo info = new CategoryInfo(category.getName(), category.getOrder());
                        categoryInfoList.add(info);
                    }

                    // Kategorileri sıralayın
                    Collections.sort(categoryInfoList, Comparator.comparingInt(CategoryInfo::getOrder));
                } else {
                    Log.d("ProcessExamData", "Kategoriler boş veya null");
                }

                // Soruları kategoriye göre ayırın
                LinkedList<Question> questions = receivedExam.getQuestions();
                if (questions != null) {
                    for (Question question : questions) {
                        String categoryName = question.getCategoryName();
                        boolean matched = false;

                        for (CategoryInfo info : categoryInfoList) {
                            if (info.getName().trim().equalsIgnoreCase(categoryName.trim())) {
                                info.incrementQuestionCount();
                                if (!categoryQuestionsMap.containsKey(categoryName)) {
                                    categoryQuestionsMap.put(categoryName, new ArrayList<>());
                                }
                                categoryQuestionsMap.get(categoryName).add(question);
                                matched = true;
                                break;
                            }
                        }
                        if (!matched) {
                            Log.d("ProcessExamData", "Kategori eşleşmedi: " + categoryName);
                        }
                    }
                } else {
                    Log.d("ProcessExamData", "Sorular boş veya null");
                }

                displayCategoryInfo();
            } else {
                Log.d("Start", "Parçalama başladı.");
                // Risk Düzeyi Hesaplama Sınavı dışındaki sınavlarda sadece text sorularını işle
                LinkedList<Question> questions = receivedExam.getQuestions();
                Log.d("Soru Sayısı", ""+questions.size());
                if (questions != null) {
                    for (Question question : questions) {
                        String categoryName = question.getCategoryName();
                        if (!categoryQuestionsMap.containsKey(categoryName)) {
                            categoryQuestionsMap.put(categoryName, new ArrayList<>());
                        }
                        categoryQuestionsMap.get(categoryName).add(question);
                        questionList.add(question); // Soruları ana listeye ekliyoruz
                    }
                } else {
                    Log.d("Questions", "Sorular boş.");
                }
            }
        } else {
            Log.d("ProcessExamData", "receivedExam veya sorular null");
        }
    }

    private void handleNormalExam() {
        difficultyLayout.setVisibility(View.GONE); // Hide difficulty layout
        circularCountdownView.setVisibility(View.GONE);

        if (questionList == null || questionList.isEmpty()) {
            Log.e("handleNormalExam", "No questions available for the exam.");
            return;
        }

        // Tek kategori olduğundan direkt soruları göster
        if(receivedExam.getBeforeText() != null && !receivedExam.getBeforeText().isEmpty()) {
            beforeQuestionLayout.setVisibility(View.VISIBLE);
            textQuestionLayout.setVisibility(View.GONE);
            preTextButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
            circularCountdownView.setVisibility(View.GONE);

            String gelenString = receivedExam.getBeforeText();

            String[] splittedStrings = gelenString.split(";", 2);

            String senaryoKismi = splittedStrings.length > 0 ? splittedStrings[0] : "";
            String problemKismi = splittedStrings.length > 1 ? splittedStrings[1] : "";

            examScenerio.setText(senaryoKismi);
            examScenerioDesc.setText(problemKismi);
            categoryName.setText(senaryoKismi);

            /*
            Kadir ağamın emri üzerine normal sınavlarda ki bekleme süresi kaldırıldı
             */
           /*startQuestionTimer(questionTime, () -> {
                beforeQuestionLayout.setVisibility(View.GONE);
                nextButton.setVisibility(View.GONE);
                circularCountdownView.setVisibility(View.GONE);
                processNormalCurrentCategory();
            });*/
        } else {
            beforeQuestionLayout.setVisibility(View.GONE);
            processNormalCurrentCategory();
        }
    }

    /**
     * Displays detailed information about each category for debugging purposes.
     */
    private void displayCategoryInfo() {
        for (CategoryInfo info : categoryInfoList) {
            examCategories.add(info.getOrder()-1, info.getName());
            Log.d("CategoryInfo", "Category: " + info.getName() +
                    ", Order: " + info.getOrder() +
                    ", Question Count: " + info.getQuestionCount() +
                    ", User Points: " + info.getUserPoint());
        }

        Log.d("ExamPoint", "Total Exam Points: " + examPoint);
    }

    /**
     * Sets the difficulty mode by updating star colors and difficulty text.
     *
     * @param difficultyEnum The difficulty level of the current question.
     */
    private void difficultyMode(Difficulty difficultyEnum) {
        String difficultyString = "";
        if (difficultyEnum == Difficulty.BEGINNER) {
            setStarColor(difficultyStarOne, R.color.warningColor);
            setStarColor(difficultyStarTwo, R.color.darkOnTop);
            setStarColor(difficultyStarThree, R.color.darkOnTop);
            difficultyString = getString(R.string.exam_difficulty_beginner);
        } else if (difficultyEnum == Difficulty.MEDIUM) {
            setStarColor(difficultyStarOne, R.color.warningColor);
            setStarColor(difficultyStarTwo, R.color.warningColor);
            setStarColor(difficultyStarThree, R.color.darkOnTop);
            difficultyString = getString(R.string.exam_difficulty_medium);
        } else if (difficultyEnum == Difficulty.HARD) {
            setStarColor(difficultyStarOne, R.color.warningColor);
            setStarColor(difficultyStarTwo, R.color.warningColor);
            setStarColor(difficultyStarThree, R.color.warningColor);
            difficultyString = getString(R.string.exam_difficulty_hard);
        }

        difficultyText.setText(difficultyString);
    }

    /**
     * Sets the color of a star ImageView based on the provided color resource ID.
     *
     * @param star       The ImageView representing the star.
     * @param colorResId The color resource ID to apply.
     */
    private void setStarColor(ImageView star, int colorResId) {
        int color = ContextCompat.getColor(this, colorResId);
        star.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
    }

    // Listener Interfaces
    interface CountdownListener {
        void onCountdownFinished();
    }

    interface CategoryCompletionListener {
        void onCategoryCompleted();
    }

    /**
     * Starts the initial question display countdown.
     *
     * @param duration The duration of the countdown in milliseconds.
     * @param listener The listener to invoke when the countdown finishes.
     */
    private void startQuestionTimer(long duration, CountdownListener listener) {
        if (questionTimer != null) {
            questionTimer.cancel();
        }

        questionTimer = new CountDownTimer(duration, 1000) {
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

    private void startExamTimer(long duration, CountdownListener listener) {
        if (examTimer != null) {
            examTimer.cancel();
        }

        examTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                examCountdownView.setRemainingTime(millisUntilFinished);
            }

            public void onFinish() {
                examCountdownView.setRemainingTime(0);
                if (listener != null) {
                    listener.onCountdownFinished();
                }
            }
        }.start();
    }

    /**
     * Processes the current category by initiating the corresponding exam flow.
     */
    private void processCurrentCategory() {
        if (currentCategoryIndex >= categoryInfoList.size()) {
            Log.d("ExamProcess", "All categories have been completed.");
            showFinalScore();
            return;
        }

        CategoryInfo currentCategory = categoryInfoList.get(currentCategoryIndex);

        // Skip the category if there are no questions
        if (!categoryQuestionsMap.containsKey(currentCategory.getName()) || categoryQuestionsMap.get(currentCategory.getName()).isEmpty()) {
            Log.d("ExamProcess", "No questions available in " + currentCategory.getName() + " category. Skipping...");
            currentCategoryIndex++;
            processCurrentCategory(); // Proceed to the next category
            return;
        }

        categoryName.setText(currentCategory.getName());
        categoryOrder.setText(String.valueOf(currentCategory.getOrder()));

        imageQuestionLayout.setVisibility(View.GONE);
        textQuestionLayout.setVisibility(View.VISIBLE);

        questionList = categoryQuestionsMap.get(examCategories.get(currentCategoryIndex));

        // Soruları döngüyle değil sırayla gösterecek
        startCategoryExam(questionList, () -> {
            // Category completed, proceed to next category
            currentCategoryIndex++;
            processCurrentCategory();
        });
    }

    private void processNormalCurrentCategory() {
        // Kategori kontrolünü kaldırıyoruz çünkü tek kategori var
        if (questionList == null || questionList.isEmpty()) {
            Log.e("processNormalCurrentCategory", "No questions available.");
            return;
        }

        imageQuestionLayout.setVisibility(View.GONE);
        textQuestionLayout.setVisibility(View.VISIBLE);
        categoryName.setText(questionList.get(currentQuestionIndex).getCategoryName());

        // Soruları sırayla gösterecek
        startCategoryExam(questionList, () -> {
            showFinalScore();  // Sınav tamamlandığında puanı göster
        });
    }

    /**
     * Initiates the exam flow for a given category.
     *
     * @param questionList The list of questions for the current category.
     * @param listener     The listener to invoke when the category is completed.
     */
    private void startCategoryExam(List<Question> questionList, CategoryCompletionListener listener) {
        if (questionList.isEmpty()) {
            Log.d("ExamProcess", "No questions available in " + categoryName.getText().toString() + " category.");
            listener.onCategoryCompleted();
            return;
        }

        currentQuestionIndex = 0; // Reset question index
        displayNextQuestion(questionList, listener);
    }

    /**
     * Displays the next question in the given question list.
     *
     * @param questionList The list of questions for the current category.
     * @param listener     The listener to invoke when the category is completed.
     */
    private void displayNextQuestion(List<Question> questionList, CategoryCompletionListener listener) {
        if (currentQuestionIndex >= questionList.size()) {
            Log.d("ExamProcess", "All questions in " + categoryName.getText().toString() + " category have been completed.");
            listener.onCategoryCompleted();
            return;
        }

        // Mevcut soruyu al
        currentQuestion = questionList.get(currentQuestionIndex);

        if(currentQuestionIndex != 0) {
            if(!currentQuestion.getCategoryName().equals(questionList.get(currentQuestionIndex-1).getCategoryName())) {
                currentCategoryIndex++;
            }
        }

        // Soru detaylarını göster
        mainText.setText(currentQuestion.getMainText());
        preTextButton.setText(currentQuestion.getPreText());
        if(currentQuestion.getSubText() != null || !currentQuestion.getSubText().isEmpty()) {
            subText.setText(currentQuestion.getSubText());
        }

        if(receivedExam.getExamName().equals("Metin Anlama Testi")) {
            categoryName.setText(currentQuestion.getCategoryName());
        }

        // Zorluk modunu ayarla
        difficultyMode(currentQuestion.getDifficulty());

        // Sorunun görüntü tipi olup olmadığını kontrol et
        boolean isImageQuestion = isImageQuestion(currentQuestion);

        if (isImageQuestion) {
            if(!isNormalExam) {
                imageQuestionLayout.setVisibility(View.VISIBLE);
                textQuestionLayout.setVisibility(View.GONE);
                preTextButton.setVisibility(View.VISIBLE);
                answerButton.setVisibility(View.GONE);
                mainText.setVisibility(View.GONE);
                setupImageQuestion(() -> {
                    currentQuestionIndex++;
                    displayNextQuestion(questionList, listener);
                }, questionList);
            } else {
                subImage.setVisibility(View.VISIBLE);
                loadImageIntoImageView(subImage, currentQuestion.getFileName());
                imageQuestionLayout.setVisibility(View.GONE);
                textQuestionLayout.setVisibility(View.VISIBLE);
                preTextButton.setVisibility(View.GONE);
                answerButton.setVisibility(View.VISIBLE);
                mainText.setVisibility(View.GONE);
                setupTextQuestion(() -> {
                    currentQuestionIndex++;
                    displayNextQuestion(questionList, listener);
                }, questionList);
            }
        } else {
            subImage.setVisibility(View.GONE);
            imageQuestionLayout.setVisibility(View.GONE);
            textQuestionLayout.setVisibility(View.VISIBLE);
            preTextButton.setVisibility(View.GONE);
            answerButton.setVisibility(View.VISIBLE);
            mainText.setVisibility(View.GONE);
            setupTextQuestion(() -> {
                currentQuestionIndex++;
                displayNextQuestion(questionList, listener);
            }, questionList);
        }
    }

    /**
     * Determines if a question is of type image based on its options.
     *
     * @param question The question to check.
     * @return True if at least one option is of type image, false otherwise.
     */
    private boolean isImageQuestion(Question question) {
        for (QuestionOption option : question.getQuestionOptions()) {
            if ("image".equalsIgnoreCase(option.getType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets up the image question by loading images and starting timers.
     *
     * @param listener     The listener to invoke when the category is completed.
     * @param questionList The list of questions for the current category.
     */
    private void setupImageQuestion(CategoryCompletionListener listener, List<Question> questionList) {
        if (currentQuestion.getQuestionOptions() == null || currentQuestion.getQuestionOptions().isEmpty()) {
            Log.e("setupImageQuestion", "No options available for the current image question.");
            listener.onCategoryCompleted();
            return;
        }

        circularCountdownView.setVisibility(View.VISIBLE);

        // Reset selected images
        selectedImageViews.clear();
        selectedImageCountViews.clear();
        for (ImageView imageView : imageViewList) {
            imageView.setAlpha(1.0f); // Reset opacity
            imageView.setTag(null);
            imageView.setClickable(false); // Disable clicks initially
        }

        // **Step 1:** Add all QuestionOption objects to allOptionsRandomizedList
        allOptionsRandomizedList.clear();
        allOptionsRandomizedList.addAll(currentQuestion.getQuestionOptions());
        Log.d("Option Count", String.valueOf(currentQuestion.getQuestionOptions().size()));

        // **Step 2:** Shuffle the allOptionsRandomizedList
        Collections.shuffle(allOptionsRandomizedList);
        Log.d("setupImageQuestion", "All options shuffled.");

        // **Step 3:** Extract correct options from allOptionsRandomizedList based on correctOptionsCount
        correctOptionsList.clear();
        for (QuestionOption option : allOptionsRandomizedList) {
            if (option.isCorrect()) {
                correctOptionsList.add(option);
                if (correctOptionsList.size() == currentQuestion.getCorrectOptionsCount()) {
                    break; // Ensure only the specified number of correct options are added
                }
            }
        }
        Log.d("setupImageQuestion", "Correct options extracted based on correctOptionsCount.");

        // **Step 4:** Sort correctOptionsList by order ascending
        Collections.sort(correctOptionsList, Comparator.comparingInt(QuestionOption::getOrder));
        Log.d("setupImageQuestion", "Correct options sorted by order.");

        // Phase 1: Show only correct images in sorted order
        List<String> correctImageFileNames = new ArrayList<>();
        for (QuestionOption option : correctOptionsList) {
            if ("image".equalsIgnoreCase(option.getType())) {
                correctImageFileNames.add(option.getFileName());
            }
        }

        locateImages(correctImageFileNames, false); // Phase 1: Display correct images

        // Start the initial question display timer (e.g., 4 seconds)
        startQuestionTimer(questionTime, () -> {
            // After timer, proceed to Phase 2: Show all images
            preTextButton.setVisibility(View.GONE);
            mainText.setVisibility(View.VISIBLE);
            answerButton.setVisibility(View.VISIBLE);

            isAnswerPhase = true; // Now in answer phase

            // Phase 2: Show all images (correct and incorrect) from allOptionsRandomizedList up to totalOptionsCount
            List<String> allImageFileNames = new ArrayList<>();
            for (int i = 0; i < allOptionsRandomizedList.size(); i++) {
                QuestionOption option = allOptionsRandomizedList.get(i);
                Log.d("Option " + i, option.getFileName());
                allImageFileNames.add(option.getFileName());
            }

            locateImages(allImageFileNames, true); // Phase 2: Display all images

            circularCountdownView.setVisibility(View.INVISIBLE);

            // Enable image selection
            for (ImageView imageView : imageViewList) {
                imageView.setClickable(true);
            }
        });
    }

    /**
     * Sets up the text question by displaying texts and starting timers.
     *
     * @param listener     The listener to invoke when the category is completed.
     * @param questionList The list of questions for the current category.
     */
    private void setupTextQuestion(CategoryCompletionListener listener, List<Question> questionList) {
        questionNumber.setText(String.valueOf(currentQuestionIndex + 1));
        mainQuestionText.setText(currentQuestion.getMainText());

        // Set question order if needed
        categoryOrder.setText(String.valueOf(currentCategoryIndex + 1));

        // Seçenekleri doldurun
        List<QuestionOption> options = currentQuestion.getQuestionOptions();
        if (options.size() >= 4) {
            if(options.get(0).getFileName() != null) {
                option1Text.setVisibility(View.GONE);
                option2Text.setVisibility(View.GONE);
                option3Text.setVisibility(View.GONE);
                option4Text.setVisibility(View.GONE);
                option1Image.setVisibility(View.VISIBLE);
                option2Image.setVisibility(View.VISIBLE);
                option3Image.setVisibility(View.VISIBLE);
                option4Image.setVisibility(View.VISIBLE);
                loadImageIntoImageView(option1Image, options.get(0).getFileName());
                loadImageIntoImageView(option2Image, options.get(1).getFileName());
                loadImageIntoImageView(option3Image, options.get(2).getFileName());
                loadImageIntoImageView(option4Image, options.get(3).getFileName());
            } else {
                option1Text.setVisibility(View.VISIBLE);
                option2Text.setVisibility(View.VISIBLE);
                option3Text.setVisibility(View.VISIBLE);
                option4Text.setVisibility(View.VISIBLE);
                option1Image.setVisibility(View.GONE);
                option2Image.setVisibility(View.GONE);
                option3Image.setVisibility(View.GONE);
                option4Image.setVisibility(View.GONE);
                option1Text.setText(options.get(0).getText());
                option2Text.setText(options.get(1).getText());
                option3Text.setText(options.get(2).getText());
                option4Text.setText(options.get(3).getText());
            }
        } else {
            // Handle cases where there are fewer than 4 options
            option1Text.setText(options.size() > 0 ? options.get(0).getText() : "");
            option2Text.setText(options.size() > 1 ? options.get(1).getText() : "");
            option3Text.setText(options.size() > 2 ? options.get(2).getText() : "");
            option4Text.setText(options.size() > 3 ? options.get(3).getText() : "");
        }

        // Reset option ticks
        resetOptions();

        // Enable option layouts
        option1Layout.setClickable(true);
        option2Layout.setClickable(true);
        option3Layout.setClickable(true);
        option4Layout.setClickable(true);

        // Reset ticks
        option1Tick.setVisibility(View.INVISIBLE);
        option2Tick.setVisibility(View.INVISIBLE);
        option3Tick.setVisibility(View.INVISIBLE);
        option4Tick.setVisibility(View.INVISIBLE);
    }

    /**
     * Assigns images to ImageViews based on the provided file names and phase.
     *
     * @param imageFileNames The list of image file names to load.
     * @param isAnswerPhase  Indicates whether it's the answer phase.
     */
    private void locateImages(List<String> imageFileNames, boolean isAnswerPhase) {
        // First, hide all ImageViews
        changeImageVisibility(View.GONE);

        // Determine which ImageViews to make visible based on imageCount
        int imageCount = imageFileNames.size();
        Log.d("Image file Name Count", String.valueOf(imageCount));

        if (imageCount == 2) {
            // Pattern:
            // 1 1 0
            // 0 0 0
            // 0 0 0
            // 0 0 0
            imageView1_1.setVisibility(View.VISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
            cardView1_1.setVisibility(View.VISIBLE);
            cardView1_2.setVisibility(View.VISIBLE);
        } else if (imageCount == 4) {
            // Pattern:
            // 1 1 0
            // 1 1 0
            // 0 0 0
            // 0 0 0
            imageView1_1.setVisibility(View.VISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
            imageView2_1.setVisibility(View.VISIBLE);
            imageView2_2.setVisibility(View.VISIBLE);
            cardView1_1.setVisibility(View.VISIBLE);
            cardView1_2.setVisibility(View.VISIBLE);
            cardView2_1.setVisibility(View.VISIBLE);
            cardView2_2.setVisibility(View.VISIBLE);
        } else if (imageCount == 6) {
            // Pattern:
            // 1 1 0
            // 1 1 0
            // 1 1 0
            // 0 0 0
            imageView1_1.setVisibility(View.VISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
            imageView2_1.setVisibility(View.VISIBLE);
            imageView2_2.setVisibility(View.VISIBLE);
            imageView3_1.setVisibility(View.VISIBLE);
            imageView3_2.setVisibility(View.VISIBLE);
            cardView1_1.setVisibility(View.VISIBLE);
            cardView1_2.setVisibility(View.VISIBLE);
            cardView2_1.setVisibility(View.VISIBLE);
            cardView2_2.setVisibility(View.VISIBLE);
            cardView3_1.setVisibility(View.VISIBLE);
            cardView3_2.setVisibility(View.VISIBLE);
        } else if (imageCount == 8) {
            // Pattern:
            // 0 1 0
            // 1 1 1
            // 1 1 1
            // 0 1 0

            // Row 1
            imageView1_2.setVisibility(View.VISIBLE);
            cardView1_2.setVisibility(View.VISIBLE);

            // Row 2
            imageView2_1.setVisibility(View.VISIBLE);
            imageView2_2.setVisibility(View.VISIBLE);
            imageView2_3.setVisibility(View.VISIBLE);
            cardView2_1.setVisibility(View.VISIBLE);
            cardView2_2.setVisibility(View.VISIBLE);
            cardView2_3.setVisibility(View.VISIBLE);

            // Row 3
            imageView3_1.setVisibility(View.VISIBLE);
            imageView3_2.setVisibility(View.VISIBLE);
            imageView3_3.setVisibility(View.VISIBLE);
            cardView3_1.setVisibility(View.VISIBLE);
            cardView3_2.setVisibility(View.VISIBLE);
            cardView3_3.setVisibility(View.VISIBLE);

            // Row 4
            imageView4_2.setVisibility(View.VISIBLE);
            cardView4_2.setVisibility(View.VISIBLE);
        } else if (imageCount == 12) {
            // Pattern:
            // 1 1 1
            // 1 1 1
            // 1 1 1
            // 1 1 1
            changeImageVisibility(View.VISIBLE);
        } else {
            // Handle unexpected image counts gracefully
            Log.w("locateImages", "Unexpected imageCount: " + imageCount);
        }

        // Assign each imageFileName to a unique visible ImageView sequentially
        Log.d("File Name Count", String.valueOf(imageFileNames.size()));

        int imageIndex = 0; // Separate index for imageFileNames
        for (int i = 0; i < imageViewList.size(); i++) {
            ImageView imageView = imageViewList.get(i);
            ImageView imageCountView = imageViewCountList.get(i);
            CardView cardView = cardViewList.get(i);

            if (imageView.getVisibility() == View.VISIBLE && cardView.getVisibility() == View.VISIBLE) {
                if (imageIndex >= imageFileNames.size()) {
                    break; // Prevent index out of bounds
                }

                String fileName = imageFileNames.get(imageIndex);
                Log.d("File Name", fileName);

                loadImageIntoImageView(imageView, fileName);

                // Assign contentDescription based on the phase
                if (!isAnswerPhase) {
                    // Phase 1: Assign sorted correct option texts
                    // Find the corresponding correct option
                    String optionText = "";
                    for (QuestionOption option : correctOptionsList) {
                        if (option.getFileName().equals(fileName)) {
                            optionText = option.getText();
                            break;
                        }
                    }
                    addCircleWithNumber(imageCountView, imageIndex+1);
                    imageView.setContentDescription(optionText); // Assign text to contentDescription
                } else {
                    for(int j=0; j<imageViewCountList.size(); j++) {
                        imageViewCountList.get(j).setForeground(null);
                    }
                    if (imageIndex < allOptionsRandomizedList.size()) {
                        QuestionOption option = allOptionsRandomizedList.get(imageIndex);
                        imageView.setContentDescription(option.getText()); // Assign text to contentDescription
                    }
                }

                imageIndex++; // Move to the next image
            }
        }

        // Enable or disable ImageView clicks based on the phase
        for (ImageView imageView : imageViewList) {
            imageView.setClickable(isAnswerPhase);
        }

        Log.d("locateImages", "Images located for phase: " + (isAnswerPhase ? "Answer" : "Question"));
    }

    /**
     * Changes the visibility of all ImageViews.
     *
     * @param visibility The desired visibility (e.g., View.VISIBLE, View.GONE).
     */
    private void changeImageVisibility(int visibility) {
        for(int i=0; i<imageViewList.size(); i++) {
            imageViewList.get(i).setVisibility(visibility);
            cardViewList.get(i).setVisibility(visibility);
        }
        Log.d("ChangeImageVisibility", "All ImageViews visibility set to: " + visibility);
    }

    /**
     * Loads an image into an ImageView using Glide.
     *
     * @param imageView The ImageView where the image will be loaded.
     * @param fileName  The name of the image file to load.
     */
    private void loadImageIntoImageView(ImageView imageView, String fileName) {
        String baseUrl = "http://160.20.111.45:3200/file-upload/get-file?fileName=";
        String imageUrl = baseUrl + fileName;

        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }

    /**
     * Handles user clicks on ImageViews for selecting answers.
     *
     * @param imageView The ImageView that was clicked.
     */
    private void handleImageClick(ImageView imageView, ImageView imageCountView) {
        if (!isAnswerPhase) {
            // If not in answer phase, ignore clicks
            return;
        }

        if (imageView.getTag() != null && imageView.getTag().equals("selected")) {
            // Image is already selected, deselect it
            selectedImageViews.remove(imageView);
            imageView.setAlpha(1.0f);
            imageView.setTag("unselected");
            // Remove the circle if deselected
            imageView.setForeground(null);

            selectedImageCountViews.remove(imageCountView);
            imageCountView.setAlpha(1.0F);
            imageCountView.setTag("unselected");
            imageCountView.setForeground(null);

            // Update the numbers on the remaining selected images
            updateSelectedImageNumbers();
        } else {
            // Check if maximum selections have been reached
            if (selectedImageViews.size() < currentQuestion.getCorrectOptionsCount()) {
                selectedImageViews.add(imageView);
                imageView.setAlpha(1.0f); // Indicate selection
                imageView.setTag("selected");

                selectedImageCountViews.add(imageCountView);
                imageCountView.setAlpha(1.0F);
                imageCountView.setTag("selected");

                // Add a circle with the selection number
                addCircleWithNumber(imageCountView, selectedImageViews.size());
            } else {
                // Inform the user that no more selections are allowed
                Toast.makeText(this, "You can only select " + currentQuestion.getCorrectOptionsCount() + " images.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addCircleWithNumber(ImageView imageView, int number) {
        imageView.post(() -> {
            // Çember oluşturma
            ShapeDrawable circle = new ShapeDrawable(new OvalShape());
            circle.getPaint().setColor(Color.WHITE);
            circle.setIntrinsicWidth(24);
            circle.setIntrinsicHeight(24);

            // Text oluşturma
            TextDrawable textDrawable = new TextDrawable(ExamProcess.this, String.valueOf(number), Color.BLACK, 16);
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{circle, textDrawable});
            layerDrawable.setLayerInset(1, 10, 10, 10, 10);

            // Foreground olarak ayarla
            imageView.setForeground(layerDrawable);
        });
    }


    private void updateSelectedImageNumbers() {
        for (int i = 0; i < selectedImageCountViews.size(); i++) {
            ImageView selectedImageView = selectedImageCountViews.get(i);
            addCircleWithNumber(selectedImageView, i + 1); // Update the circle with the new number
        }
    }

    /**
     * Sets up click listeners for text option layouts.
     */
    private void setupTextOptionClickListeners() {
        // Option1
        option1Layout.setOnClickListener(view -> {
            toggleTextOption(option1Tick);
            resetOtherOptions(option1Tick); // Diğer seçenekleri sıfırla
        });

        // Option2
        option2Layout.setOnClickListener(view -> {
            toggleTextOption(option2Tick);
            resetOtherOptions(option2Tick); // Diğer seçenekleri sıfırla
        });

        // Option3
        option3Layout.setOnClickListener(view -> {
            toggleTextOption(option3Tick);
            resetOtherOptions(option3Tick); // Diğer seçenekleri sıfırla
        });

        // Option4
        option4Layout.setOnClickListener(view -> {
            toggleTextOption(option4Tick);
            resetOtherOptions(option4Tick); // Diğer seçenekleri sıfırla
        });
    }

    private void resetOtherOptions(ImageView selectedOptionTick) {
        // Diğer tüm seçeneklerin işaretini kaldır
        if (selectedOptionTick != option1Tick) {
            option1Tick.setVisibility(View.INVISIBLE);
        }
        if (selectedOptionTick != option2Tick) {
            option2Tick.setVisibility(View.INVISIBLE);
        }
        if (selectedOptionTick != option3Tick) {
            option3Tick.setVisibility(View.INVISIBLE);
        }
        if (selectedOptionTick != option4Tick) {
            option4Tick.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Toggles the visibility of a text option's tick.
     *
     * @param tickView The ImageView representing the tick.
     */
    private void toggleTextOption(ImageView tickView) {
        if (tickView.getVisibility() == View.VISIBLE) {
            tickView.setVisibility(View.INVISIBLE);
        } else {
            tickView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Handles the AnswerButton click by checking user answers based on question type.
     */
    private void handleAnswerButtonClick() {
        for(int i=0; i<imageViewList.size(); i++) {
            ImageView imageView, imageCountView;
            imageView = imageViewList.get(i);
            imageCountView = imageViewCountList.get(i);

            imageView.setForeground(null);
            imageCountView.setForeground(null);
        }
        if (isImageQuestion(currentQuestion)) {
            checkUserImageAnswers(() -> {
                // Category completed, proceed to next category
                currentCategoryIndex++;
                processCurrentCategory();
            });
        } else {
            checkUserTextAnswers(() -> {
                // Category completed, proceed to next category
                currentCategoryIndex++;
                processCurrentCategory();
            });
        }
    }

    /**
     * Checks the user's selected image answers and updates the score accordingly.
     *
     * @param listener The listener to invoke when the category is completed.
     */
    private void checkUserImageAnswers(CategoryCompletionListener listener) {
        // User has answered
        hasAnswered = true;

        // Collect selected contentDescriptions
        List<String> selectedAnswers = new ArrayList<>();
        for (ImageView imageView : selectedImageViews) {
            selectedAnswers.add(imageView.getContentDescription().toString());
        }

        // Collect correct answers' texts
        List<String> correctAnswers = new ArrayList<>();
        for (QuestionOption option : correctOptionsList) {
            correctAnswers.add(option.getText());
        }

        // Check if selected answers match the correct answers in order
        boolean isCorrect = true;
        if (selectedAnswers.size() != correctAnswers.size()) {
            isCorrect = false;
        } else {
            for (int i = 0; i < selectedAnswers.size(); i++) {
                if (!correctAnswers.get(i).equals(selectedAnswers.get(i))) {
                    isCorrect = false;
                    break;
                }
            }
        }

        if (isCorrect) {
            // Correct answer, add points
            examPoint += currentQuestion.getPoint();
            // Also add to category-specific points
            if(!isNormalExam) {
                categoryInfoList.get(currentCategoryIndex).addUserPoint(currentQuestion.getPoint());
            }
            Log.d("ExamProcess", "Correct answer! Points awarded: " + currentQuestion.getPoint());
            Sneaker.with(ExamProcess.this)
                    .setTitle(getString(R.string.exam_correct_answer))
                    .setMessage(currentQuestion.getPoint() + " " + getString(R.string.exam_point))
                    .setHeight(100)
                    .setDuration(900)
                    .sneakSuccess();
        } else {
            // Incorrect answer, no points
            Log.d("ExamProcess", "Incorrect answer! No points awarded.");
            Sneaker.with(ExamProcess.this)
                    .setTitle(getString(R.string.exam_wrong_answer))
                    .setMessage(getString(R.string.exam_zero_point))
                    .setHeight(100)
                    .setDuration(900)
                    .sneakError();
        }

        // Reset selections
        selectedImageViews.clear();
        for(int i=0; i<imageViewList.size(); i++) {
            ImageView imageView, imageCountView;
            imageView = imageViewList.get(i);
            imageCountView = imageViewCountList.get(i);

            imageView.setAlpha(1.0f); // Reset opacity
            imageView.setTag("unselected"); // Reset tag

            imageCountView.setAlpha(1.0f); // Reset opacity
            imageCountView.setTag("unselected"); // Reset tag
        }

        // Hide AnswerButton and mainText
        answerButton.setVisibility(View.GONE);
        mainText.setVisibility(View.GONE);

        // Proceed to the next question
        currentQuestionIndex++;
        displayNextQuestion(questionList, listener);
    }

    /**
     * Checks the user's selected text answers and updates the score accordingly.
     *
     * @param listener The listener to invoke when the category is completed.
     */
    private void checkUserTextAnswers(CategoryCompletionListener listener) {
        hasAnswered = true;

        // Kullanıcının seçtiği seçenekleri kontrol et
        List<QuestionOption> options = currentQuestion.getQuestionOptions();
        List<Integer> selectedIndices = new ArrayList<>();

        if (option1Tick.getVisibility() == View.VISIBLE) selectedIndices.add(0);
        if (option2Tick.getVisibility() == View.VISIBLE) selectedIndices.add(1);
        if (option3Tick.getVisibility() == View.VISIBLE) selectedIndices.add(2);
        if (option4Tick.getVisibility() == View.VISIBLE) selectedIndices.add(3);

        boolean allCorrect = true;
        for (int i = 0; i < options.size(); i++) {
            QuestionOption option = options.get(i);
            if (option.isCorrect() && !selectedIndices.contains(i)) {
                allCorrect = false;
                break;
            }
            if (!option.isCorrect() && selectedIndices.contains(i)) {
                allCorrect = false;
                break;
            }
        }

        if (allCorrect) {
            examPoint += currentQuestion.getPoint();
            // Also add to category-specific points
            if(!isNormalExam) {
                categoryInfoList.get(currentCategoryIndex).addUserPoint(currentQuestion.getPoint());
            }
            Log.d("ExamProcess", "Correct answer! Points awarded: " + currentQuestion.getPoint());
            Sneaker.with(ExamProcess.this)
                    .setTitle(getString(R.string.exam_correct_answer))
                    .setMessage(currentQuestion.getPoint() + " " + getString(R.string.exam_point))
                    .setHeight(100)
                    .setDuration(900)
                    .sneakSuccess();
        } else {
            // Incorrect answer, no points
            Log.d("ExamProcess", "Incorrect answer! No points awarded.");
            Sneaker.with(ExamProcess.this)
                    .setTitle(getString(R.string.exam_wrong_answer))
                    .setMessage(getString(R.string.exam_zero_point))
                    .setHeight(100)
                    .setDuration(900)
                    .sneakError();
        }

        // Reset selections
        resetOptions();

        // Proceed to the next question
        currentQuestionIndex++;
        displayNextQuestion(questionList, listener);
    }

    /**
     * Resets both image and text option selections.
     */
    private void resetOptions() {
        // Reset image selections
        selectedImageViews.clear();
        for (ImageView imageView : imageViewList) {
            imageView.setAlpha(1.0f); // Reset opacity
            imageView.setTag(null);
            imageView.setClickable(false); // Disable clicks after reset
        }

        // Reset text option ticks
        option1Tick.setVisibility(View.INVISIBLE);
        option2Tick.setVisibility(View.INVISIBLE);
        option3Tick.setVisibility(View.INVISIBLE);
        option4Tick.setVisibility(View.INVISIBLE);
    }

    /**
     * Displays the final score in an AlertDialog, showing both total and category-based scores.
     * The dialog is displayed for 5 seconds and then automatically dismissed.
     */
    private void showFinalScore() {
        Gson gson = new Gson();
        String updatedCategoryListJson;
        if(!isNormalExam) {
            updatedCategoryListJson = gson.toJson(categoryInfoList);
        } else {
            updatedCategoryListJson = gson.toJson(examPoint);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedCategoryListJson", updatedCategoryListJson);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * Cancels any running timers to prevent memory leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (questionTimer != null) {
            questionTimer.cancel();
        }
        if (examTimer != null) {
            examTimer.cancel();
        }
        handler.removeCallbacksAndMessages(null);
    }
}