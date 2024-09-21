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
import android.widget.Toast;

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

    private long questionTime = 10000; // 10 seconds for displaying the question
    private long answerTime = 10000;   // 10 seconds for answering

    private List<CategoryInfo> categoryInfoList = new ArrayList<>();
    private int examPoint = 0;

    private List<ImageView> imageViewList = new ArrayList<>();

    private List<Question> shortTermMemoryQuestions = new ArrayList<>();
    private List<Question> longTermMemoryQuestions = new ArrayList<>();
    private List<Question> visualMemoryQuestions = new ArrayList<>();
    private List<Question> proceduralTermMemoryQuestions = new ArrayList<>();

    private int currentCategoryIndex = 0; // Current category index

    private int currentQuestionIndex = 0; // Current question index
    private Question currentQuestion;
    private List<QuestionOption> currentOptions = new ArrayList<>();

    // Newly Added Lists
    private List<QuestionOption> correctOptionsList = new ArrayList<>();
    private List<QuestionOption> allOptionsRandomizedList = new ArrayList<>();
    private List<ImageView> selectedImageViews = new ArrayList<>();

    // Timers
    private CountDownTimer questionTimer;
    private CountDownTimer answerTimer;
    private boolean hasAnswered = false; // Flag to check if user has answered
    private boolean isAnswerPhase = false; // Flag to determine the current phase

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
        processExamData();
        sortQuestions();

        processCurrentCategory();
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

        // Initially hide the imageQuestionLayout
        imageQuestionLayout.setVisibility(View.GONE);

        // Initially hide answerButton and mainText
        answerButton.setVisibility(View.GONE);
        mainText.setVisibility(View.GONE);

        // Set up AnswerButton click listener
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserAnswers(new CategoryCompletionListener() {
                    @Override
                    public void onCategoryCompleted() {
                        // Category completed, proceed to next category
                        currentCategoryIndex++;
                        processCurrentCategory();
                    }
                });
            }
        });

        // Set up preTextButton click listener
        preTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preTextButton.setVisibility(View.GONE);
                mainText.setVisibility(View.VISIBLE);
            }
        });

        // Add click listeners to all ImageViews for selection
        for (ImageView imageView : imageViewList) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleImageClick(imageView);
                }
            });
        }
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

    interface OptionsDisplayListener {
        void onOptionsDisplayed();
    }

    interface OptionsRandomizedListener {
        void onOptionsRandomized();
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
                startShortTermExam(new CategoryCompletionListener() {
                    @Override
                    public void onCategoryCompleted() {
                        // Category completed, proceed to next category
                        currentCategoryIndex++;
                        processCurrentCategory();
                    }
                });
                break;
            // Add cases for other categories as needed
            default:
                imageQuestionLayout.setVisibility(View.GONE);
                // Handle other categories here
                currentCategoryIndex++;
                processCurrentCategory();
                break;
        }
    }

    /**
     * Initiates the short-term memory exam category.
     *
     * @param listener The listener to invoke when the category is completed.
     */
    private void startShortTermExam(CategoryCompletionListener listener) {
        if (shortTermMemoryQuestions.isEmpty()) {
            Log.d("ExamProcess", "No questions available in Kısa Süreli Bellek category.");
            listener.onCategoryCompleted();
            return;
        }

        currentQuestionIndex = 0; // Reset question index
        examPoint = 0; // Reset exam points
        displayNextQuestion(listener);
    }

    /**
     * Displays the next question in the current category.
     *
     * @param listener The listener to invoke when the category is completed.
     */
    private void displayNextQuestion(CategoryCompletionListener listener) {
        if (currentQuestionIndex >= shortTermMemoryQuestions.size()) {
            imageQuestionLayout.setVisibility(View.GONE);
            Log.d("ExamProcess", "All questions in Kısa Süreli Bellek category have been completed.");
            listener.onCategoryCompleted();
            return;
        }

        currentQuestion = shortTermMemoryQuestions.get(currentQuestionIndex);
        mainText.setText(currentQuestion.getMainText());
        preTextButton.setText(currentQuestion.getPreText());

        difficultyMode(currentQuestion.getDifficulty());

        imageQuestionLayout.setVisibility(View.VISIBLE);
        preTextButton.setVisibility(View.VISIBLE);
        answerButton.setVisibility(View.GONE);
        mainText.setVisibility(View.GONE);

        hasAnswered = false; // Reset the flag for the new question
        isAnswerPhase = false; // Initially in question display phase

        // Reset selected images
        selectedImageViews.clear();
        for (ImageView imageView : imageViewList) {
            imageView.setAlpha(1.0f); // Reset opacity
            imageView.setTag(null);
        }

        // Separate correct and incorrect options
        correctOptionsList.clear();
        List<QuestionOption> incorrectOptions = new ArrayList<>();

        for (QuestionOption option : currentQuestion.getQuestionOptions()) {
            if (option.isCorrect()) {
                correctOptionsList.add(option);
            } else {
                incorrectOptions.add(option);
            }
        }

        // Sort correct options based on their order
        Collections.sort(correctOptionsList, Comparator.comparingInt(QuestionOption::getOrder));

        // Randomize all options
        allOptionsRandomizedList.clear();
        allOptionsRandomizedList.addAll(currentQuestion.getQuestionOptions());
        Collections.shuffle(allOptionsRandomizedList);

        // Collect file names of correct options with type 'image'
        List<String> correctImageFileNames = new ArrayList<>();
        for (QuestionOption option : correctOptionsList) {
            if ("image".equalsIgnoreCase(option.getType())) {
                correctImageFileNames.add(option.getFileName());
            }
        }

        // Locate and display correct images (Phase 1)
        locateImages(correctImageFileNames, false);

        // Start the initial question display timer (10 seconds)
        startQuestionTimer(questionTime, new CountdownListener() {
            @Override
            public void onCountdownFinished() {
                // After 10 seconds, show all image options and allow answering
                preTextButton.setVisibility(View.GONE);
                mainText.setVisibility(View.VISIBLE);
                answerButton.setVisibility(View.VISIBLE);

                isAnswerPhase = true; // Now in answer phase

                // Collect all image file names with type 'image'
                List<String> allImageFileNames = new ArrayList<>();
                for (QuestionOption option : currentQuestion.getQuestionOptions()) {
                    if ("image".equalsIgnoreCase(option.getType())) {
                        allImageFileNames.add(option.getFileName());
                    }
                }

                // Locate and display all images (Phase 2)
                locateImages(allImageFileNames, true);

                // Start the answer timer (10 seconds)
                startAnswerTimer(answerTime, new CountdownListener() {
                    @Override
                    public void onCountdownFinished() {
                        if (!hasAnswered) {
                            Log.d("ExamProcess", "User did not answer in time. Awarding 0 points.");
                            // Move to the next question without awarding points
                            currentQuestionIndex++;
                            displayNextQuestion(listener);
                        }
                    }
                });
            }
        });
    }

    /**
     * Handles user clicks on ImageViews for selecting answers.
     *
     * @param imageView The ImageView that was clicked.
     */
    private void handleImageClick(ImageView imageView) {
        if (imageView.getTag() != null && imageView.getTag().equals("selected")) {
            // Image is already selected, deselect it
            selectedImageViews.remove(imageView);
            imageView.setAlpha(1.0f);
            imageView.setTag(null);
        } else {
            // Check if maximum selections have been reached
            if (selectedImageViews.size() < currentQuestion.getCorrectOptionsCount()) {
                selectedImageViews.add(imageView);
                imageView.setAlpha(0.5f); // Indicate selection
                imageView.setTag("selected");
            } else {
                // Inform the user that no more selections are allowed
                Toast.makeText(this, "You can only select " + currentQuestion.getCorrectOptionsCount() + " images.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Checks the user's selected answers and updates the score accordingly.
     *
     * @param listener The listener to invoke when the category is completed.
     */
    private void checkUserAnswers(CategoryCompletionListener listener) {
        // User has answered
        hasAnswered = true;

        // Cancel the answer timer as the user has responded
        if (answerTimer != null) {
            answerTimer.cancel();
        }

        int correctSelections = 0;

        for (ImageView imageView : selectedImageViews) {
            int index = imageViewList.indexOf(imageView);
            if (index < allOptionsRandomizedList.size()) { // Ensure index is within bounds
                QuestionOption option = allOptionsRandomizedList.get(index);
                if (option.isCorrect()) {
                    correctSelections++;
                }
            }
        }

        if (correctSelections == currentQuestion.getCorrectOptionsCount()) {
            // Correct answer, add points
            examPoint += currentQuestion.getPoint();
            Log.d("ExamProcess", "Correct answer! Points awarded: " + currentQuestion.getPoint());
            Toast.makeText(this, "Correct! +" + currentQuestion.getPoint() + " points.", Toast.LENGTH_SHORT).show();
        } else {
            // Incorrect answer, no points
            Log.d("ExamProcess", "Incorrect answer! No points awarded.");
            Toast.makeText(this, "Incorrect! No points awarded.", Toast.LENGTH_SHORT).show();
        }

        // Reset selections
        selectedImageViews.clear();
        for (ImageView imageView : imageViewList) {
            imageView.setAlpha(1.0f); // Reset opacity
            imageView.setTag(null);
        }

        // Hide AnswerButton and mainText
        answerButton.setVisibility(View.GONE);
        mainText.setVisibility(View.GONE);

        // Proceed to the next question
        currentQuestionIndex++;
        displayNextQuestion(listener);
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
        if (imageCount == 2) {
            imageView1_1.setVisibility(View.VISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
        } else if (imageCount == 4) {
            imageView1_1.setVisibility(View.VISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
            imageView2_1.setVisibility(View.VISIBLE);
            imageView2_2.setVisibility(View.VISIBLE);
        } else if (imageCount == 6) {
            imageView1_1.setVisibility(View.VISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
            imageView2_1.setVisibility(View.VISIBLE);
            imageView2_2.setVisibility(View.VISIBLE);
            imageView3_1.setVisibility(View.VISIBLE);
            imageView3_2.setVisibility(View.VISIBLE);
        } else if (imageCount == 8) {
            imageView1_1.setVisibility(View.VISIBLE);
            imageView1_2.setVisibility(View.VISIBLE);
            imageView1_3.setVisibility(View.VISIBLE);
            imageView2_1.setVisibility(View.VISIBLE);
            imageView2_2.setVisibility(View.VISIBLE);
            imageView2_3.setVisibility(View.VISIBLE);
            imageView3_1.setVisibility(View.VISIBLE);
            imageView3_2.setVisibility(View.VISIBLE);
            imageView3_3.setVisibility(View.VISIBLE);
            imageView4_1.setVisibility(View.VISIBLE);
            imageView4_2.setVisibility(View.VISIBLE);
            imageView4_3.setVisibility(View.VISIBLE);
        } else if (imageCount == 12) {
            changeImageVisibility(View.VISIBLE);
        } else {
            // Handle unexpected image counts gracefully
            Log.w("locateImages", "Unexpected imageCount: " + imageCount);
        }

        // Assign each imageFileName to a unique visible ImageView
        int assignedImages = 0;
        for (int i = 0; i < imageFileNames.size(); i++) {
            if (assignedImages >= imageViewList.size()) {
                break; // Prevent index out of bounds
            }
            ImageView imageView = imageViewList.get(i);
            if (imageView.getVisibility() == View.VISIBLE) {
                String fileName = imageFileNames.get(i);
                loadImageIntoImageView(imageView, fileName);
                imageView.setVisibility(View.VISIBLE);
                assignedImages++;
            }
        }

        // Hide any extra ImageViews that are not needed
        for (int i = imageFileNames.size(); i < imageViewList.size(); i++) {
            ImageView imageView = imageViewList.get(i);
            imageView.setVisibility(View.GONE);
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
        for (ImageView imageView : imageViewList) {
            imageView.setVisibility(visibility);
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
                .into(imageView);
    }

    /**
     * Displays the final score in an AlertDialog.
     */
    private void showFinalScore() {
        new AlertDialog.Builder(this)
                .setTitle("Exam Completed")
                .setMessage("Your Total Score: " + examPoint)
                .setPositiveButton("OK", null)
                .setCancelable(false)
                .show();
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
    }
}