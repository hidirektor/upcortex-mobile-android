package me.t3sl4.upcortex.UI.Screens.Exam;

import android.app.AlertDialog;
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
import me.t3sl4.upcortex.Utility.Screen.TextDrawable;

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

    private long questionTime = 4000; // 4 seconds for displaying the question
    private long answerTime = 4000;   // 4 seconds for answering
    private boolean answerTimerVal = false;

    private List<CategoryInfo> categoryInfoList = new ArrayList<>();
    private int examPoint = 0;

    private List<ImageView> imageViewList = new ArrayList<>();
    private List<CardView> cardViewList = new ArrayList<>();

    private List<Question> questionList = new ArrayList<>();
    private List<Question> shortTermMemoryQuestions = new ArrayList<>();
    private List<Question> longTermMemoryQuestions = new ArrayList<>();
    private List<Question> visualMemoryQuestions = new ArrayList<>();
    private List<Question> proceduralTermMemoryQuestions = new ArrayList<>();

    private int currentCategoryIndex = 0; // Current category index

    private int currentQuestionIndex = 0; // Current question index
    private Question currentQuestion;

    // Newly Added Lists
    private List<QuestionOption> correctOptionsList = new ArrayList<>();
    private List<QuestionOption> allOptionsRandomizedList = new ArrayList<>();
    private List<ImageView> selectedImageViews = new ArrayList<>();

    // Timers
    private CountDownTimer questionTimer;
    private CountDownTimer answerTimer;
    private boolean hasAnswered = false; // Flag to check if user has answered
    private boolean isAnswerPhase = false; // Flag to determine the current phase

    private Handler handler = new Handler(); // Handler for delayed dialog dismissal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_process);

        // Retrieve JSON data and convert to Exam object
        String examJson = getIntent().getStringExtra("examJson");
        if (examJson != null) {
            Gson gson = new Gson();
            receivedExam = gson.fromJson(examJson, Exam.class);
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
        if(receivedExam.getExamName().equals("Risk Düzeyi Hesaplama Sınavı")) {
            //Risk Düzeyi Belirleme Sınavı
            processExamData();
            sortQuestions();

            processCurrentCategory();
        } else {
            //Normal Sınav
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

        // Initialize Buttons and Main Text
        preTextButton = findViewById(R.id.preTextButton);
        answerButton = findViewById(R.id.answerButton);
        mainText = findViewById(R.id.mainText);

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

        // Initially hide the imageQuestionLayout
        imageQuestionLayout.setVisibility(View.GONE);

        // Initialize Text Question Layout
        textQuestionLayout = findViewById(R.id.textQuestionLayout);
        questionNumber = findViewById(R.id.questionOrder);
        mainQuestionText = findViewById(R.id.mainQuestionText);

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

        // Initially hide answerButton and mainText for image questions
        answerButton.setVisibility(View.GONE);
        mainText.setVisibility(View.GONE);

        // Set up AnswerButton click listener
        answerButton.setOnClickListener(view -> handleAnswerButtonClick());

        // Add click listeners to all ImageViews for selection
        for (ImageView imageView : imageViewList) {
            imageView.setOnClickListener(view -> handleImageClick(imageView));
        }

        // Add click listeners to text option layouts
        setupTextOptionClickListeners();
    }

    /**
     * Processes the received exam data by categorizing and counting questions.
     */
    private void processExamData() {
        if (receivedExam != null && receivedExam.getQuestions() != null) {
            LinkedList<QuestionCategory> categories = receivedExam.getQuestionCategories();
            if (categories != null) {
                Log.d("ProcessExamData", "Number of categories: " + categories.size());
                for (QuestionCategory category : categories) {
                    Log.d("ProcessExamData", "Category: " + category.getName() + ", Order: " + category.getOrder());
                    CategoryInfo info = new CategoryInfo(category.getName(), category.getOrder());
                    categoryInfoList.add(info);
                }

                // Sort categories based on their order
                Collections.sort(categoryInfoList, Comparator.comparingInt(CategoryInfo::getOrder));

                // Log sorted categories
                Log.d("ProcessExamData", "Sorted category list:");
                for (CategoryInfo info : categoryInfoList) {
                    Log.d("ProcessExamData", "Category: " + info.getName() + ", Order: " + info.getOrder());
                }
            } else {
                Log.d("ProcessExamData", "Categories are null or empty");
            }

            // Count questions per category
            LinkedList<Question> questions = receivedExam.getQuestions();
            if (questions != null) {
                Log.d("ProcessExamData", "Number of questions: " + questions.size());
                for (Question question : questions) {
                    String categoryName = question.getCategoryName();
                    Log.d("ProcessExamData", "Question category: " + categoryName);
                    boolean matched = false;
                    for (CategoryInfo info : categoryInfoList) {
                        if (info.getName().trim().equalsIgnoreCase(categoryName.trim())) {
                            info.incrementQuestionCount();
                            matched = true;
                            Log.d("ProcessExamData", "Category matched: " + categoryName);
                            break;
                        }
                    }
                    if (!matched) {
                        Log.d("ProcessExamData", "Category did not match: " + categoryName);
                    }
                }
            } else {
                Log.d("ProcessExamData", "Questions are null or empty");
            }
            displayCategoryInfo();
        } else {
            Log.d("ProcessExamData", "receivedExam or questions are null");
        }
    }

    /**
     * Sorts questions into their respective categories.
     */
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

    /**
     * Displays detailed information about each category for debugging purposes.
     */
    private void displayCategoryInfo() {
        for (CategoryInfo info : categoryInfoList) {
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

    /**
     * Starts the answer phase countdown.
     *
     * @param duration The duration of the countdown in milliseconds.
     * @param listener The listener to invoke when the countdown finishes.
     */
    private void startAnswerTimer(long duration, CountdownListener listener) {
        if (answerTimer != null) {
            answerTimer.cancel();
        }

        answerTimer = new CountDownTimer(duration, 1000) {
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
        categoryName.setText(currentCategory.getName());
        categoryOrder.setText(String.valueOf(currentCategory.getOrder()));

        switch (currentCategory.getName()) {
            case "Kısa Süreli Bellek":
                imageQuestionLayout.setVisibility(View.VISIBLE);
                textQuestionLayout.setVisibility(View.GONE);
                startCategoryExam(shortTermMemoryQuestions, () -> {
                    // Category completed, proceed to next category
                    currentCategoryIndex++;
                    processCurrentCategory();
                });
                break;
            case "Uzun Süreli Bellek":
                imageQuestionLayout.setVisibility(View.VISIBLE);
                textQuestionLayout.setVisibility(View.GONE);
                startCategoryExam(longTermMemoryQuestions, () -> {
                    // Category completed, proceed to next category
                    currentCategoryIndex++;
                    processCurrentCategory();
                });
                break;
            case "Görsel Bellek":
                imageQuestionLayout.setVisibility(View.VISIBLE);
                textQuestionLayout.setVisibility(View.GONE);
                startCategoryExam(visualMemoryQuestions, () -> {
                    // Category completed, proceed to next category
                    currentCategoryIndex++;
                    processCurrentCategory();
                });
                break;
            case "İşlemsel Bellek":
                imageQuestionLayout.setVisibility(View.GONE);
                textQuestionLayout.setVisibility(View.VISIBLE);
                startCategoryExam(proceduralTermMemoryQuestions, () -> {
                    // Category completed, proceed to next category
                    currentCategoryIndex++;
                    processCurrentCategory();
                });
                break;
            default:
                Log.w("ExamProcess", "Unknown category: " + currentCategory.getName());
                currentCategoryIndex++;
                processCurrentCategory();
                break;
        }
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
            imageQuestionLayout.setVisibility(View.GONE);
            textQuestionLayout.setVisibility(View.GONE);
            Log.d("ExamProcess", "All questions in " + categoryName.getText().toString() + " category have been completed.");
            listener.onCategoryCompleted();
            return;
        }

        currentQuestion = questionList.get(currentQuestionIndex);
        mainText.setText(currentQuestion.getMainText());
        preTextButton.setText(currentQuestion.getPreText());

        difficultyMode(currentQuestion.getDifficulty());

        // Determine if the question is image type or text type
        boolean isImageQuestion = isImageQuestion(currentQuestion);

        if (isImageQuestion) {
            imageQuestionLayout.setVisibility(View.VISIBLE);
            textQuestionLayout.setVisibility(View.GONE);
            preTextButton.setVisibility(View.VISIBLE);
            answerButton.setVisibility(View.GONE);
            mainText.setVisibility(View.GONE);
            setupImageQuestion(listener, questionList);
        } else {
            imageQuestionLayout.setVisibility(View.GONE);
            textQuestionLayout.setVisibility(View.VISIBLE);
            preTextButton.setVisibility(View.GONE);
            answerButton.setVisibility(View.VISIBLE);
            mainText.setVisibility(View.GONE);
            setupTextQuestion(listener, questionList);
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

            // Start the answer timer (e.g., 4 seconds)
            if (answerTimerVal) {
                startAnswerTimer(answerTime, () -> {
                    if (!hasAnswered) {
                        Log.d("ExamProcess", "User did not answer in time. Awarding 0 points.");
                        Toast.makeText(this, "Süre doldu! Puan verilmedi.", Toast.LENGTH_SHORT).show();
                        // Move to the next question without awarding points
                        currentQuestionIndex++;
                        displayNextQuestion(questionList, listener);
                    }
                });
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
            option1Text.setText(options.get(0).getText());
            option2Text.setText(options.get(1).getText());
            option3Text.setText(options.get(2).getText());
            option4Text.setText(options.get(3).getText());
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

        // Start the answer timer (e.g., 4 seconds)
        startAnswerTimer(answerTime, () -> {
            if (!hasAnswered) {
                Log.d("ExamProcess", "User did not answer in time. Awarding 0 points.");
                // Move to the next question without awarding points
                currentQuestionIndex++;
                displayNextQuestion(questionList, listener);
            }
        });
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
                    imageView.setContentDescription(optionText); // Assign text to contentDescription
                } else {
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
    private void handleImageClick(ImageView imageView) {
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

            // Update the numbers on the remaining selected images
            updateSelectedImageNumbers();
        } else {
            // Check if maximum selections have been reached
            if (selectedImageViews.size() < currentQuestion.getCorrectOptionsCount()) {
                selectedImageViews.add(imageView);
                imageView.setAlpha(0.5f); // Indicate selection
                imageView.setTag("selected");

                // Add a circle with the selection number
                addCircleWithNumber(imageView, selectedImageViews.size());
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
        for (int i = 0; i < selectedImageViews.size(); i++) {
            ImageView selectedImageView = selectedImageViews.get(i);
            addCircleWithNumber(selectedImageView, i + 1); // Update the circle with the new number
        }
    }

    /**
     * Sets up click listeners for text option layouts.
     */
    private void setupTextOptionClickListeners() {
        // Option1
        option1Layout.setOnClickListener(view -> toggleTextOption(option1Tick));

        // Option2
        option2Layout.setOnClickListener(view -> toggleTextOption(option2Tick));

        // Option3
        option3Layout.setOnClickListener(view -> toggleTextOption(option3Tick));

        // Option4
        option4Layout.setOnClickListener(view -> toggleTextOption(option4Tick));
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
        for (ImageView imageView : imageViewList) {
            imageView.setForeground(null);
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

        // Cancel the answer timer as the user has responded
        if (answerTimer != null) {
            answerTimer.cancel();
        }

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
            categoryInfoList.get(currentCategoryIndex).addUserPoint(currentQuestion.getPoint());
            Log.d("ExamProcess", "Correct answer! Points awarded: " + currentQuestion.getPoint());
            Toast.makeText(this, "Doğru! +" + currentQuestion.getPoint() + " puan.", Toast.LENGTH_SHORT).show();
        } else {
            // Incorrect answer, no points
            Log.d("ExamProcess", "Incorrect answer! No points awarded.");
            Toast.makeText(this, "Yanlış! Puan verilmedi.", Toast.LENGTH_SHORT).show();
        }

        // Reset selections
        selectedImageViews.clear();
        for (ImageView imageView : imageViewList) {
            imageView.setAlpha(1.0f); // Reset opacity
            imageView.setTag("unselected"); // Reset tag
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

        // Cancel the answer timer as the user has responded
        if (answerTimer != null) {
            answerTimer.cancel();
        }

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
            categoryInfoList.get(currentCategoryIndex).addUserPoint(currentQuestion.getPoint());
            Log.d("ExamProcess", "Correct answer! Points awarded: " + currentQuestion.getPoint());
            Toast.makeText(this, "Doğru! +" + currentQuestion.getPoint() + " puan.", Toast.LENGTH_SHORT).show();
        } else {
            // Incorrect answer, no points
            Log.d("ExamProcess", "Incorrect answer! No points awarded.");
            Toast.makeText(this, "Yanlış! Puan verilmedi.", Toast.LENGTH_SHORT).show();
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
        // Build the message string with total score and category-wise scores
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Total Score: ").append(examPoint).append("\n\n");
        messageBuilder.append("Category Scores:\n");
        for (CategoryInfo info : categoryInfoList) {
            messageBuilder.append(info.getName()).append(": ").append(info.getUserPoint()).append(" puan\n");
        }

        AlertDialog finalScoreDialog = new AlertDialog.Builder(this)
                .setTitle("Exam Completed")
                .setMessage(messageBuilder.toString())
                .setCancelable(false)
                .create();

        finalScoreDialog.show();

        // Dismiss the dialog after 3 seconds and return categoryInfoList with results
        handler.postDelayed(() -> {
            finalScoreDialog.dismiss();

            // Prepare the updated categoryInfoList as JSON
            Gson gson = new Gson();
            String updatedCategoryListJson = gson.toJson(categoryInfoList);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedCategoryListJson", updatedCategoryListJson);
            setResult(RESULT_OK, resultIntent); // Send the result back
            finish(); // End activity
        }, 3000); // 3000 milliseconds = 3 seconds
    }


    /**
     * Retrieves the current question list based on the current category.
     *
     * @return The list of questions for the current category.
     */
    private List<Question> getCurrentQuestionList() {
        if (currentCategoryIndex >= categoryInfoList.size()) {
            return new ArrayList<>();
        }

        CategoryInfo currentCategory = categoryInfoList.get(currentCategoryIndex);
        String categoryName = currentCategory.getName();

        switch (categoryName) {
            case "Kısa Süreli Bellek":
                return shortTermMemoryQuestions;
            case "Uzun Süreli Bellek":
                return longTermMemoryQuestions;
            case "Görsel Bellek":
                return visualMemoryQuestions;
            case "İşlemsel Bellek":
                return proceduralTermMemoryQuestions;
            default:
                return new ArrayList<>();
        }
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
        if (answerTimer != null) {
            answerTimer.cancel();
        }
        handler.removeCallbacksAndMessages(null);
    }
}